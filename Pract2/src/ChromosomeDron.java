import java.util.Random;

public class ChromosomeDron extends Chromosome<Float, Integer>{
    int C;  //Numero de camaras
    int D;  //Numero de drones
    protected int MAX_INSERCIONES = 3;
    protected int MAX_PERMUTACIONES = 4;
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
                    //NO voy a hacer esto ahora
                }
                break;
            default:
                break;
        }
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
