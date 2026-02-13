public abstract class Chromosome {

    public enum MUTATION_TYPE {UNIFORM, GAUSSEAN}

    boolean[] genotipo;
    double[] fenotipo;
    public double aptitud;
    public double puntuacion;
    public double punt_acumulada;

    public double[] getFenotipo()
    {
        calculateFenotipo();
        return  fenotipo;
    };

    public abstract void initializeRandom();
    abstract void calculateFenotipo();
    abstract void mutate();
}
