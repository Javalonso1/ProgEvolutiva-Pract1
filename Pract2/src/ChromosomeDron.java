import java.util.Random;

public class ChromosomeDron extends Chromosome<Float, Integer>{
    int C;  //Numero de camaras
    int D;  //Numero de drones
    protected int MAX_INSERCIONES = 3;
    protected int MAX_PERMUTACIONES_HEURISTICA = 4;
    public ChromosomeDron(int NumCams, int numDrones){
        fenotipo = new Integer[NumCams + (numDrones-1)];
        C = NumCams;
        D = numDrones;
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
            case INSERTION:
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
            case EXCHANGE:
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

                    //Ahora que tenemos las permutaciones tocara evaluar todas las posibilidades. Lo hare luego


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
    void cruceMonopunto(Chromosome c1, Chromosome c2, int corte)
    {
        //No se usa el cruce monopunto
    };
    @Override
    void cruceUniforme(Chromosome c1, Chromosome c2, float prob, float[]results)
    {
        //No se usa el cruce uniforme
    };
    @Override
    void cruceAritmetico(Chromosome c1, Chromosome c2)
    {
        //No se usa el cruce aritmético
    };
    @Override
    void cruceBLX_Alpha(Chromosome c1, Chromosome c2, float alpha)
    {
        //No se usa el cruce BLX_ALpha
    }

    private ChromosomeDron(ChromosomeDron other) {
        this.aptitud = other.aptitud;
        this.puntuacion = other.puntuacion;
        this.punt_acumulada = other.punt_acumulada;
        this.C = other.C;
        this.D = other.D;
        this.fenotipo = other.fenotipo.clone();
    }
    @Override
    public Chromosome copy() {
        return new ChromosomeDron(this);
    }

}
