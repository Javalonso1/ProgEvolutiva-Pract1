public class DronEvolver extends GeneticManager{

    public DronEvolver(UIclass g)
    {
        super(g);
    }
    @Override
    protected Chromosome[] initializePopulation(int p_size) {
        return new Chromosome[0];
    }

    @Override
    protected Chromosome[] crossover(Chromosome[] pop) {
        return new Chromosome[0];
    }

    @Override
    protected void evaluate(Chromosome[] pop, boolean _casillaImport) {

    }
}
