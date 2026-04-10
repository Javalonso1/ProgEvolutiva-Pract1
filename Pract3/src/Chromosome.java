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

    public abstract void initializeRandomFull();
    public abstract void initializeRandomGrow();
    abstract void mutate(GeneticManager.MUTATION_TYPE t, double mutationP);
    //abstract void  crucePMX(Chromosome c1, Chromosome c2, int corte1, int corte2);
    //abstract void  cruceOX(Chromosome c1, Chromosome c2, int corte1, int corte2);
    //abstract void  cruceOXPP(Chromosome c1, Chromosome c2, int[] pos);
    //abstract void  cruceCX(Chromosome c1, Chromosome c2);
    //abstract void  cruceCO(Chromosome c1, Chromosome c2, int corte);
    //abstract void  cruceERX(Chromosome c1, Chromosome c2);
    //abstract void  cruceCustom(Chromosome c1, Chromosome c2);

    public abstract Chromosome copy();
}
