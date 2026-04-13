public abstract class Chromosome {

    NodoAST fenotipo;
    public double aptitud;
    public double puntuacion;
    public double punt_acumulada;

    public NodoAST getFenotipo()
    {
        return  fenotipo;
    };
    public abstract void setFenotipo(NodoAST f);

    public abstract void initializeRandom(boolean full);
    abstract void mutate(GeneticManager.MUTATION_TYPE t, double mutationP);
    abstract void  cruceSUBARBOL(int[]pos, NodoAST n);

    public abstract Chromosome copy();
}
