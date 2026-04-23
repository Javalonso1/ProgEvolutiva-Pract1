import java.util.*;

public class RoverEvolver extends GeneticManager{

    private int profundidadMax; //Profunidad maxima de los arboles
    private boolean initFull;   //Si la inicializacion random es Full o Grow
    private ArrayList<int[][]> maps;
    private int NTicks = 100;
    private float bloating;

    public RoverEvolver(UIclass g, int prof, boolean _inFull, ArrayList<int[][]> maps, float _bloat)
    {
        super(g);
        profundidadMax = prof;
        initFull = _inFull;
        this.maps = maps;
        this.bloating = _bloat;
    }
    @Override
    protected Chromosome[] initializePopulation(int p_size){
        Chromosome[] ini_pop = new ChromosomeRover[p_size];
        for ( int i = 0; i < p_size; i++)
        {
            ini_pop[i] = new ChromosomeRover(profundidadMax);
            ini_pop[i].initializeRandom(initFull);
        }
        return  ini_pop;
    }

    @Override
    protected Chromosome[] crossover(Chromosome[] pop){
        Chromosome[] sol = new Chromosome[pop.length];
        for(int i = 0; i < pop.length; i += 2){
            if(Pcruce <= Math.random()){
                sol[i] = pop[i].copy();
                sol[i+1] = pop[i+1].copy();
                switch (crossMethod){
                    case SUBARBOL:
                        ArrayList<NodoAST> arr = sol[i].fenotipo.getAllTerminalNodos();
                        NodoAST n1 = arr.get((int)(Math.random()*arr.size()));
                        arr = sol[i+1].fenotipo.getAllTerminalNodos();
                        NodoAST n2 = arr.get((int)(Math.random()*arr.size()));
                        int x1 = n1.pickRandomSon();
                        int x2 = n2.pickRandomSon();
                        NodoAST n1_1 = n1.getSon(x1).copy();
                        NodoAST n2_1 = n2.getSon(x2).copy();
                        n1.sustituteSon(x1,n2_1);
                        n2.sustituteSon(x2,n1_1);
                        break;
                    default:
                        break;
                }
            }
            else {
                sol[i] = pop[i].copy();
                sol[i+1] =pop[i+1].copy();
            }
        }
        return sol;
    }

    @Override
    protected void evaluate(Chromosome[] pop) {
        for (Chromosome c: pop)
        {
            int aptitud = 0;
            for (int i = 0; i < maps.size(); i++)
            {
                RoverInfo info = new RoverInfo(maps.get(i));
                ArrayList<Integer> path = new ArrayList<Integer>();
                while (info.nTurnos < NTicks && info.energyLevel > 0)
                    aptitud += evaluateNode(c.fenotipo, info, path);

                if (info.isLazy())
                    aptitud -= 1000;
            }
            c.aptitud = (aptitud/maps.size()) - (c.fenotipo.numNodos() * bloating);

        }
    }

    private int evaluateNode(NodoAST n, RoverInfo rover, ArrayList<Integer> path)
    {
        if (rover.nTurnos >= NTicks || rover.energyLevel <= 0) {
            return 0;
        }

        int aptitud = 0;
        if (n.getType() == NodoAST.NODETYPE.BLOCK)
        {
            NodoBloque nodo = (NodoBloque) n;
            ArrayList<NodoAST>nodos = nodo.GetNodos();
            int i = 0;
            while (i < nodos.size() && rover.nTurnos < NTicks && rover.energyLevel > 0)
            {
                aptitud += evaluateNode(nodos.get(i), rover, path);
                i++;
            }
            return aptitud;

        }
        else if (n.getType() == NodoAST.NODETYPE.ACTION)
        {
            NodoAccion nodo = (NodoAccion) n;
            if (nodo.tipoAccion == NodoAccion.Accion.AVANZAR)
            {
                aptitud += rover.avanzar();
                path.add(rover.posY);
                path.add(rover.posX);
            }
            else if (nodo.tipoAccion == NodoAccion.Accion.GIRAR_DER)
            {
                rover.girarDer();
            }
            else if (nodo.tipoAccion == NodoAccion.Accion.GIRAR_IZQ)
            {
                rover.girarIzq();
            }
            rover.nTurnos ++;
            if (rover.checkIfMuestraInVision())
                aptitud += 2;

        }
        else if (n.getType() == NodoAST.NODETYPE.CONDITIONAL)
        {
            NodoCondicional nodo = (NodoCondicional) n;
            int value = translateSensor(nodo.getSensor(), rover);

            if(evaluateExpression(value, nodo.getOperador(), nodo.getUmbral()))
            {
                aptitud += evaluateNode(nodo.hijoIzquierdo, rover, path);
            }
            else
            {
                aptitud += evaluateNode(nodo.hijoDerecho, rover, path);
            }

        }
        return aptitud;
    }

    private boolean evaluateExpression(int value1, NodoCondicional.Operdor o, int value2)
    {

        switch (o)
        {
            case IGUAL:
                return value1 == value2;
            case MAYOR_QUE:
                return value1 > value2;
            case MENOR_QUE:
                return value1 < value2;
            default:
                return false;
        }

    }

    private int translateSensor(NodoCondicional.Sensores s, RoverInfo info)
    {
        switch (s)
        {
            case DIST_ARENA:
                return info.distToNearestArena();
            case NIVEL_ENERGIA:
                return info.energyLevel;
            case DIST_OBSTACULO:
                return info.distToNearestObstaculo();
            case DIST_MUESTRA:
                return info.distToNearestMuestra();
            default:
                return -1;
        }
    }

    public void showSolution(Chromosome c) {
        RoverInfo info = new RoverInfo(maps.get(0));
        ArrayList<Integer> path = new ArrayList<Integer>();
        path.add(1); path.add(1);
        while (info.nTurnos < NTicks && info.energyLevel > 0)
            evaluateNode(c.fenotipo, info, path);

        Ui.setPath(path);
        Ui.setBottomText(c.fenotipo.nodoToText());

    }

}
