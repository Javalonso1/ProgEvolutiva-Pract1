public class ChromosomeRover extends Chromosome{

    protected  int MAX_NODOS_BLOQUE = 5;
    private int PROFUNDIDAD_MAXIMA;

    public ChromosomeRover(int profMax){
        PROFUNDIDAD_MAXIMA = profMax;
    }

    @Override
    public void setFenotipo(NodoAST f){
        fenotipo = f;
    }
    @Override
    public void initializeRandom(boolean full){
        if(full) initializeRandomFull(0);
        else initializeRandomGrow(0);
    }
    private NodoAST initializeRandomFull(int prof){
        if(prof >= PROFUNDIDAD_MAXIMA){
            NodoAccion a = new NodoAccion();
            a.randomize();
            return a;
        }
        else {
            int rnd = (int)(Math.random() * 2);
            if(rnd == 0){
                NodoBloque b = new NodoBloque();
                rnd = (int)(Math.random() * (MAX_NODOS_BLOQUE-2)) +2;
                for(int i = 0; i < rnd; i++){
                    b.AddNodo(initializeRandomFull(prof + 1));
                }
                return b;
            }
            else {
                NodoCondicional c = new NodoCondicional();
                c.randomize();
                c.setHijoD(initializeRandomFull(prof + 1));
                c.setHijoI(initializeRandomFull(prof + 1));
                return c;
            }
        }
    }
    private NodoAST initializeRandomGrow(int prof){
        if(prof >= PROFUNDIDAD_MAXIMA){
            NodoAccion a = new NodoAccion();
            a.randomize();
            return a;
        }
        else {
            int rnd = (int)(Math.random() * 3);
            if(rnd == 0){
                NodoBloque b = new NodoBloque();
                rnd = (int)(Math.random() * (MAX_NODOS_BLOQUE-2)) +2;
                for(int i = 0; i < rnd; i++){
                    b.AddNodo(initializeRandomGrow(prof + 1));
                }
                return b;
            }
            else if(rnd == 1) {
                NodoCondicional c = new NodoCondicional();
                c.randomize();
                c.setHijoD(initializeRandomGrow(prof + 1));
                c.setHijoI(initializeRandomGrow(prof + 1));
                return c;
            }
            else {
                NodoAccion a = new NodoAccion();
                a.randomize();
                return a;
            }
        }
    }

    @Override
    void cruceSUBARBOL(int[]pos, NodoAST n){
        fenotipo.changeNodoAtPos(pos, 0, n);
    }

    @Override
    void mutate(GeneticManager.MUTATION_TYPE t, double mutationP){
        double r = Math.random() * 100;
        if (r < mutationP){
            switch (t){
                case SUBARBOL:
                    int[] path = new int[0];
                    boolean stop = false;
                    while (!stop){
                        int a = fenotipo.getNodoAtPos(path, 0).ranomizeBranch();
                        if(a == -1){
                            stop = true;
                        }
                        else {
                            int[] pathAux = new int[path.length +1];
                            for(int j = 0; j < path.length; j++){
                                pathAux[j] = path[j];
                            }
                            pathAux[path.length] = a;
                            path = pathAux;

                            if(Math.random() * 2 == 0) stop = true;
                        }
                    }
                    NodoAST n;
                    if(Math.random() * 2 == 0) n = initializeRandomFull(path.length);
                    else n = initializeRandomGrow(path.length);
                    fenotipo.setNodoAtPos(path, 0, n);
                    break;
                case FUNCIONAL:
                    fenotipo.randomizeNodoFuncional();
                    break;
                case TERMINAL:
                    fenotipo.randomizeNodoTerminal();
                    break;
                case PODA:

                    break;
                case ALEATORIA:
                    int rnd = (int)(Math.random() * 4);
                    switch (rnd){
                        case 0:
                            path = new int[0];
                            stop = false;
                            while (!stop){
                                int a = fenotipo.getNodoAtPos(path, 0).ranomizeBranch();
                                if(a == -1){
                                    stop = true;
                                }
                                else {
                                    int[] pathAux = new int[path.length +1];
                                    for(int j = 0; j < path.length; j++){
                                        pathAux[j] = path[j];
                                    }
                                    pathAux[path.length] = a;
                                    path = pathAux;

                                    if(Math.random() * 2 == 0) stop = true;
                                }
                            }
                            if(Math.random() * 2 == 0) n = initializeRandomFull(path.length);
                            else n = initializeRandomGrow(path.length);
                            fenotipo.setNodoAtPos(path, 0, n);
                            break;
                        case 1:
                            fenotipo.randomizeNodoFuncional();
                            break;
                        case 2:
                            fenotipo.randomizeNodoTerminal();
                            break;
                        case 3:
                            break;
                        default:
                            break;
                    }
                    break;
                default:
                    break;
            }
        }
    }
    private ChromosomeRover(ChromosomeRover other) {
        this.aptitud = other.aptitud;
        this.puntuacion = other.puntuacion;
        this.punt_acumulada = other.punt_acumulada;
        this.fenotipo = other.fenotipo;
    }
    @Override
    public Chromosome copy() {
        return new ChromosomeRover(this);
    }
}
