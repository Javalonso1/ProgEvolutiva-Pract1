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
    abstract void  crucePMX(Chromosome c1, Chromosome c2, int corte1, int corte2);
    abstract void  cruceOX(Chromosome c1, Chromosome c2, int corte1, int corte2);
    abstract void  cruceOXPP(Chromosome c1, Chromosome c2, int[] pos);
    abstract void  cruceCX(Chromosome c1, Chromosome c2,int ini);
    abstract void  cruceCO(Chromosome c1, Chromosome c2, int corte);
    abstract void  cruceERX(Chromosome c1, Chromosome c2);

    public abstract Chromosome copy();
}
