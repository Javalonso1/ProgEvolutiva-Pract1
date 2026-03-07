import java.util.Random;

public class ChromosomeDron extends Chromosome<Float, Integer>{
    int C;  //Numero de camaras
    int D;  //Numero de drones
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
    void mutate(GeneticManager.MUTATION_TYPE t, double mutationp) {
        //A hacer

        /*
        switch (t){
            case UNIFORM:
                int pos = (int) (Math.random() * fenotipo.length);
                if(pos%3 == 0){
                    fenotipo[pos] = (int) (Math.random()*TX);
                }
                else if(pos%3 == 1){
                    fenotipo[pos] = (int) (Math.random()*TY);
                }
                else {
                    fenotipo[pos] =(int) (Math.random()*360);
                }
                break;
            case GAUSSEAN:
                for(int i = 0; i < fenotipo.length; i +=3){
                    fenotipo[i] = (int) (Math.random()*TX);
                    fenotipo[i+1] = (int) (Math.random()*TY);
                    fenotipo[i+2] = (int) (Math.random()*360);
                }
                break;
            default:
                break;

        }
        */
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
