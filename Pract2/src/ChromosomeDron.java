import java.util.Random;

public class ChromosomeDron extends Chromosome<Float, Integer>{

    protected int MAX_INSERCIONES = 3;
    protected int MAX_PERMUTACIONES_HEURISTICA = 4;

    protected  DronEvolver DronEv;
    public ChromosomeDron(int NumCams, int numDrones, DronEvolver mDronEv){
        fenotipo = new Integer[NumCams + (numDrones-1)];
        DronEv = mDronEv;
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
                    DronEv.evaluate(drons, true);
                    int sol = 0;
                    for(int i = 1; i < drons.length; i++){
                        if(drons[i].aptitud < drons[sol].aptitud) sol = i;
                    }
                    fenotipo = (Integer[]) drons[sol].fenotipo.clone();
                }
                break;
            case CUSTOM:
                //A hacer
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
    void crucePMX(Chromosome c1, Chromosome c2, int corte1, int corte2)
    {
        for(int i = 0; i < c1.getFenotipo().length; i++){
            if(i>= corte1 && i<=corte2){
                fenotipo[i] = (Integer) c2.getFenotipo()[i];
            }
            else {
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
        }
    };
    @Override
    void cruceOX(Chromosome c1, Chromosome c2, int corte1, int corte2)
    {
        int start = corte1;
        for(int i = 0; i < c1.getFenotipo().length; i++){
            if(i>= corte1 && i<=corte2){
                fenotipo[i] = (Integer)c2.getFenotipo()[i];
            }
            else {
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
        }
    };
    @Override
    void cruceOXPP(Chromosome c1, Chromosome c2, int[]pos)
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
                fenotipo[i] = (Integer)c2.getFenotipo()[i];
            }
            else {
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
        }
    };
    @Override
    void cruceCX(Chromosome c1, Chromosome c2)
    {
        boolean[] selected = new  boolean[fenotipo.length];
        int a = (int)c1.fenotipo[0];
        boolean stop = false;
        while (!stop){
            selected[a] = true;
            boolean stop2 = false;
            int j = 0;
            while (!stop2){
                if(c1.fenotipo[j] == c2.fenotipo[a])stop2 = true;
                else j++;
            }
            if(j == (int)c1.fenotipo[0]) stop = true;
            else a = j;
        }
        for(int i = 0; i < fenotipo.length; i++){
            if(selected[i]) fenotipo[i] = (Integer) c1.fenotipo[i];
            else fenotipo[i] = (Integer) c2.fenotipo[i];
        }
    }
    @Override
    void cruceCO(Chromosome c1, Chromosome c2, int corte)
    {
        int[] L1 = new int[fenotipo.length];
        int[] L2 = new int[fenotipo.length];
        boolean[] B1 = new boolean[fenotipo.length];
        boolean[] B2 = new boolean[fenotipo.length];
        for(int i = 0; i < fenotipo.length; i++){
            int j = 0;
            int resta = 0;
            boolean stop = false;
            if(i <= corte){
                while (!stop){
                    if((int)c1.fenotipo[j] == i) stop = true;
                    if(B1[j]) resta++;
                    j++;
                }
                L1[i] = j-resta;
            }
            j = 0;
            resta = 0;
            stop = false;
            while (!stop){
                if((int)c2.fenotipo[j] == i) stop = true;
                if(B2[j]) resta++;
                j++;
            }
            L2[i] = j-resta;
        }
        for(int i = 0; i <= corte; i++) L2[i] = L1[i];
        boolean[] B3 = new boolean[fenotipo.length];

        for(int i = 0; i < fenotipo.length; i++){
            int j = 0;
            int resta = 0;
            boolean stop = false;
            while (!stop){
                if(B3[j]) resta++;
                j++;
                if(j - resta == L2[i]){
                    stop = true;
                    B3[j] = true;
                    fenotipo[i] = (Integer) c1.fenotipo[j];
                }
            }
        }
    }
    @Override
    void cruceERX(Chromosome c1, Chromosome c2)
    {
        int[][] rutas = new int[c1.fenotipo.length][4];
        for(int i = 0; i < c1.fenotipo.length; i++){
            int aux;
            if(i -1 < 0) aux = (int)c1.fenotipo[c1.fenotipo.length -1];
            else aux = (int)c1.fenotipo[i - 1];
            rutas[i][0] = aux;

            if(i +1 >= c1.fenotipo.length) aux = (int)c1.fenotipo[0];
            else aux = (int)c1.fenotipo[i + 1];
            rutas[i][1] = aux;

            if(i -1 < 0) aux = (int)c2.fenotipo[c2.fenotipo.length -1];
            else aux = (int)c2.fenotipo[i - 1];
            if(rutas[i][0] != aux && rutas[i][1] != aux) rutas[i][2] = aux;
            else rutas[i][2] = -1;

            if(i +1 >= c2.fenotipo.length) aux = (int)c2.fenotipo[0];
            else aux = (int)c2.fenotipo[i + 1];
            if(rutas[i][0] != aux && rutas[i][1] != aux) rutas[i][3] = aux;
            else rutas[i][3] = -1;
        }
        fenotipo[0] = (Integer) c2.fenotipo[0];
        ERXAuxiliarVoid(rutas,0);
    }

    public boolean ERXAuxiliarVoid(int[][]rutas, int pos){
        if(pos == fenotipo.length -1 ){
            return true;
        }
        else {
            boolean exitoso = false;
            int vueltas = 0;
            int rnd = (int)(Math.random() * 4);
            while (!exitoso && vueltas < 4){
                if(rutas[pos][rnd] != -1){
                    boolean stop = false;
                    int j = 0;
                    while (!stop && j <= pos){
                        if(fenotipo[j] == rutas[pos][rnd]) stop = true;
                        j++;
                    }
                    if(!stop) {
                        fenotipo[pos+1] = rutas[pos][rnd];
                        exitoso = ERXAuxiliarVoid(rutas, pos+1);
                    }
                }
                if(!exitoso) {
                    vueltas++;
                    rnd++;
                    if(rnd == 4) rnd = 0;
                }
            }
            return exitoso;
        }
    }

    private ChromosomeDron(ChromosomeDron other) {
        this.aptitud = other.aptitud;
        this.puntuacion = other.puntuacion;
        this.punt_acumulada = other.punt_acumulada;
        this.fenotipo = other.fenotipo.clone();
        this.DronEv = other.DronEv;
    }
    @Override
    public Chromosome copy() {
        return new ChromosomeDron(this);
    }

}
