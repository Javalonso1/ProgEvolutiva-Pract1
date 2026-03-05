public abstract class Chromosome <T, G> {

    T[] genotipo;
    G[] fenotipo;
    public double aptitud;
    public double puntuacion;
    public double punt_acumulada;

    public T[] getGenotipo() {return  genotipo;}
    public G[] getFenotipo()
    {
        return  fenotipo;
    };
    public abstract void setFenotipo(G[] f);
    protected abstract void recalculateGenome();

    public abstract void initializeRandom();
    abstract void calculateFenotipo();
    abstract void mutate(GeneticManager.MUTATION_TYPE t, double mutationP);
    abstract void  cruceMonopunto(Chromosome c1, Chromosome c2, int corte);
    abstract void  cruceUniforme(Chromosome c1, Chromosome c2, float prob, float[]results);
    abstract void  cruceAritmetico(Chromosome c1, Chromosome c2);
    abstract void  cruceBLX_Alpha(Chromosome c1, Chromosome c2, float alpha);

    public abstract Chromosome copy();
}
