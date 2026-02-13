public abstract class Chromosome {

    boolean[] genotipo;
    double[] fenotipo;
    public double aptitud;
    public double puntuacion;
    public double punt_acumulada;

    public boolean[] getGenotipo() {return  genotipo;}
    public double[] getFenotipo(){return  fenotipo;};

    public abstract void initializeRandom();
    abstract void calculateFenotipo();
    abstract void mutate(GeneticManager.MUTATION_TYPE t);
    abstract void  cruceMonopunto(Chromosome c1, Chromosome c2, int corte);
    abstract void  cruceUniforme(Chromosome c1, Chromosome c2, float prob, float[]results);
    abstract void  cruceAritmetico(Chromosome c1, Chromosome c2);
    abstract void  cruceBLX_Alpha(Chromosome c1, Chromosome c2);
}
