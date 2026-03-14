import java.util.Random;

public class ChromosomeDron extends Chromosome<Float, Integer>{

    protected int MAX_INSERCIONES = 3;
    protected int MAX_PERMUTACIONES_HEURISTICA = 4;

    protected  DronEvolver DronEv;
    protected boolean casImp;
    public ChromosomeDron(int NumCams, int numDrones, DronEvolver mDronEv, boolean _casImp){
        fenotipo = new Integer[NumCams + (numDrones-1)];
        DronEv = mDronEv;
        casImp = _casImp;
    }

    @Override
    public void setFenotipo(Integer[] f) {
        if(fenotipo != f)
        {
            fenotipo = f;
        }
    }

    @Override
    protected void recalculateGenome() {
        //El Cromosoma de drones no usa el genotipo
    }

    public void initializeRandom(){
        //Primero se colocan todos los numeros
        for(int i = 0; i < fenotipo.length; i++){
            fenotipo[i] = i;
        }
        //Luego se barajan
        for(int i = 0; i <fenotipo.length; i++){
            int rnd1 = (int)(Math.random() * fenotipo.length);
            int rnd2 = (int)(Math.random() * fenotipo.length);
            if(rnd1 != rnd2){
                int aux = fenotipo[rnd1];
                fenotipo[rnd1] = fenotipo[rnd2];
                fenotipo[rnd2] = aux;
            }
        }
    }

    void calculateFenotipo(){
        //Solo se usan los fenotipos, por lo que no hace falta calcular nada en base del genotipo
    }

    @Override
    void mutate(GeneticManager.MUTATION_TYPE t, double mutationP) {
        switch (t){
            case INSERCION:
                double r = Math.random() * 100;
                if (r < mutationP){
                    r = (Math.random() * MAX_INSERCIONES)+1;
                    for (int i = 0; i < r; i++){
                        int pos1 = (int)(Math.random() * fenotipo.length);
                        int pos2 = (int)(Math.random() * fenotipo.length);
                        if(pos1 != pos2){
                            int aux = fenotipo[pos1];
                            int j = pos1;
                            if(pos1 > pos2){
                                while(j > pos2){
                                    fenotipo[j] = fenotipo[j-1];
                                    j--;
                                }
                                fenotipo[pos2] = aux;
                            }
                            else {
                                while(j < pos2){
                                    fenotipo[j] = fenotipo[j+1];
                                    j++;
                                }
                                fenotipo[pos2] = aux;
                            }
                        }
                    }
                }
                break;
            case INTERCAMBIO:
                r = Math.random() * 100;
                if (r < mutationP){
                    int pos1 = (int)(Math.random() * fenotipo.length);
                    int pos2 = (int)(Math.random() * fenotipo.length);
                    if(pos1 != pos2){
                        int aux = fenotipo[pos1];
                        fenotipo[pos1] = fenotipo[pos2];
                        fenotipo[pos2] = aux;
                    }
                }
                break;
            case INVERSION:
                r = Math.random() * 100;
                if (r < mutationP){
                    int pos1 = (int)(Math.random() * fenotipo.length);
                    int pos2 = (int)(Math.random() * fenotipo.length);
                    if(pos1 != pos2){
                        if(pos1 > pos2){
                            int aux = pos1;
                            pos1 = pos2;
                            pos2 = aux;
                        }
                        while (pos1 < pos2){
                            int aux = fenotipo[pos1];
                            fenotipo[pos1] = fenotipo[pos2];
                            fenotipo[pos2] = aux;
                            pos2--;
                            pos1++;
                        }
                    }
                }
                break;
            case HEURISTICA:
                r = Math.random() * 100;
                if (r < mutationP){
                    int n = (int)((Math.random() * MAX_PERMUTACIONES_HEURISTICA-1)+2);

                    int[] pos = new int[n];
                    for(int i = 0; i < n; i++){
                        pos[i] = (int)(Math.random() *fenotipo.length);
                        boolean stop = false;
                        int j = i - 1;
                        while (j >= 0 && !stop){
                            if(pos[i] == pos[j]){
                                i--;
                                stop = true;
                            }
                        }
                    }
                    int realval[] = new int[n];
                    for(int i = 0; i < n; i++) realval[i] = fenotipo[pos[i]];

                    int[][]permutaciones;
                    permutaciones = createPermutaciones(realval);

                    //Ahora que tenemos las permutaciones tocara evaluar todas las posibilidades

                    Chromosome[] drons = new ChromosomeDron[permutaciones.length];
                    for(int i = 0; i < permutaciones.length; i++){
                        drons[i] = copy();
                        for(int j = 0; j < permutaciones[0].length; j++){
                            drons[i].fenotipo[pos[j]] = permutaciones[i][j];
                        }
                    }
                    DronEv.evaluate(drons,casImp);
                    int sol = 0;
                    for(int i = 1; i < drons.length; i++){
                        if(drons[i].aptitud > drons[sol].aptitud) sol = i;
                    }
                    fenotipo = (Integer[]) drons[sol].fenotipo.clone();
                }
                break;
            default:
                break;
        }
    }


    private int[][] createPermutaciones(int[] perm){
        int size = 1;
        for(int i = 2; i <= perm.length; i++) size = size * i;
        int[][] sol = new  int[size][perm.length];
        if(size == 2){
            sol[0][0] = perm[0];
            sol[0][1] = perm[1];

            sol[1][0] = perm[1];
            sol[1][1] = perm[0];

        }
        else {
            int[] perm2 = new  int[perm.length -1];
            for(int i = 1; i < perm.length; i++) perm2[i-1] = perm[i];
            int[][] aux2 = createPermutaciones(perm2);
            int l = 0;
            for(int pos = 0; pos < perm.length; pos++){
                for(int i = 0; i < aux2.length; i++){
                    for(int k = 0; k < perm.length; k++){
                        if(k == pos) sol[l][k] = perm[0];
                        else{
                            if(k > pos){
                                sol[l][k] = aux2[i][k-1];
                            }
                            else {
                                sol[l][k] = aux2[i][k];
                            }
                        }
                    }
                    l++;
                }
            }
        }

        return sol;
    }
    @Override
    void crucePMX(Chromosome c1, Chromosome c2, int corte1, int corte2, boolean first)
    {
        for(int i = 0; i < c1.getFenotipo().length; i++){
            if(i>= corte1 && i<=corte2){
                if(first) fenotipo[i] = (Integer) c2.getFenotipo()[i];
                else fenotipo[i] = (Integer)c1.getFenotipo()[i];
            }
            else {
                if(first) {
                    int sol = (int)c1.getFenotipo()[i];
                    boolean isConflicting = false;
                    int j = corte1;
                    while (j <= corte2 && !isConflicting){
                        if(sol == (int)c2.getFenotipo()[j]){
                            sol = (int)c1.getFenotipo()[j];
                            isConflicting = true;
                        }
                        j++;
                    }
                    fenotipo[i] = sol;
                }
                else {
                    int sol = (int)c2.getFenotipo()[i];
                    boolean isConflicting = false;
                    int j = corte1;
                    while (j <= corte2 && !isConflicting){
                        if(sol == (int)c1.getFenotipo()[j]){
                            sol = (int)c2.getFenotipo()[j];
                            isConflicting = true;
                        }
                        j++;
                    }
                    fenotipo[i] = sol;
                }
            }
        }
    };
    @Override
    void cruceOX(Chromosome c1, Chromosome c2, int corte1, int corte2, boolean first)
    {
        int start = corte1;
        for(int i = 0; i < c1.getFenotipo().length; i++){
            if(i>= corte1 && i<=corte2){
                if(first) fenotipo[i] = (Integer)c2.getFenotipo()[i];
                else fenotipo[i] = (Integer)c1.getFenotipo()[i];
            }
            else {
                if(first) {
                    int sol = 0;
                    boolean stop = false;
                    while (!stop){
                        sol = (int)c1.getFenotipo()[start];
                        stop = true;
                        int j = corte1;
                        while (j <=corte2 && stop){
                            if(sol == (int)c2.getFenotipo()[j]){
                                stop = false;
                                start++;
                                if(start == c1.getFenotipo().length) start = 0;
                            }
                            j++;
                        }
                    }
                    fenotipo[i] = sol;
                }
                else {
                    int sol = 0;
                    boolean stop = false;
                    while (!stop){
                        sol = (int)c2.getFenotipo()[start];
                        stop = true;
                        int j = corte1;
                        while (j <=corte2 && stop){
                            if(sol == (int)c1.getFenotipo()[j]){
                                stop = false;
                                start++;
                                if(start == c2.getFenotipo().length) start = 0;
                            }
                            j++;
                        }
                    }
                    fenotipo[i] = sol;
                }
            }
        }
    };
    @Override
    void cruceOXPP(Chromosome c1, Chromosome c2, int[]pos, boolean first)
    {
        int start = pos[0];
        for(int i = 0; i < c1.getFenotipo().length; i++){
            boolean found = false;
            int k = 0;
            while (k < pos.length && !found){
                if(i == pos[k]) found = true;
                else k++;
            }

            if(found){
                if(first) fenotipo[i] = (Integer)c2.getFenotipo()[i];
                else fenotipo[i] = (Integer)c1.getFenotipo()[i];
            }
            else {
                if(first) {
                    int sol = 0;
                    boolean stop = false;
                    while (!stop){
                        sol = (int)c1.getFenotipo()[start];
                        stop = true;
                        int j = 0;
                        while (j <=pos.length && stop){
                            if(sol == (int)c2.getFenotipo()[pos[j]]){
                                stop = false;
                                start++;
                                if(start == c1.getFenotipo().length) start = 0;
                            }
                            j++;
                        }
                    }
                    fenotipo[i] = sol;
                }
                else {
                    int sol = 0;
                    boolean stop = false;
                    while (!stop){
                        sol = (int)c2.getFenotipo()[start];
                        stop = true;
                        int j = 0;
                        while (j <=pos.length && stop){
                            if(sol == (int)c1.getFenotipo()[pos[j]]){
                                stop = false;
                                start++;
                                if(start == c2.getFenotipo().length) start = 0;
                            }
                            j++;
                        }
                    }
                    fenotipo[i] = sol;
                }
            }
        }
    };
    @Override
    void cruceCX(Chromosome c1, Chromosome c2, int ini, boolean first)
    {
        if(first){
            boolean[] selected = new  boolean[fenotipo.length];
            int a = ini;
            boolean stop = false;
            while (!stop){
                selected[a] = true;
                boolean stop2 = false;
                int j = 0;
                while (!stop2){
                    if(c1.fenotipo[j] == c2.fenotipo[a])stop2 = true;
                    else j++;
                }
                if(j == ini) stop = true;
                else a = j;
            }
            for(int i = 0; i < fenotipo.length; i++){
                if(selected[i]) fenotipo[i] = (Integer) c1.fenotipo[i];
                else fenotipo[i] = (Integer) c2.fenotipo[i];
            }
        }
        else {
            boolean[] selected = new  boolean[fenotipo.length];
            int a = ini;
            boolean stop = false;
            while (!stop){
                selected[a] = true;
                boolean stop2 = false;
                int j = 0;
                while (!stop2){
                    if(c2.fenotipo[j] == c1.fenotipo[a])stop2 = true;
                    else j++;
                }
                if(j == ini) stop = true;
                else a = j;
            }
            for(int i = 0; i < fenotipo.length; i++){
                if(selected[i]) fenotipo[i] = (Integer) c2.fenotipo[i];
                else fenotipo[i] = (Integer) c1.fenotipo[i];
            }
        }
    }
    @Override
    void cruceCO(Chromosome c1, Chromosome c2)
    {

    }
    @Override
    void cruceERX(Chromosome c1, Chromosome c2)
    {

    }


    private ChromosomeDron(ChromosomeDron other) {
        this.aptitud = other.aptitud;
        this.puntuacion = other.puntuacion;
        this.punt_acumulada = other.punt_acumulada;
        this.fenotipo = other.fenotipo.clone();
        this.DronEv = other.DronEv;
        this.casImp = other.casImp;
    }
    @Override
    public Chromosome copy() {
        return new ChromosomeDron(this);
    }

}
