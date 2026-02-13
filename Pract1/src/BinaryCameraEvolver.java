public class BinaryCameraEvolver extends GeneticManager{

    private int NCameras;
    private int VisionRange;
    private boolean[][] map;
    public BinaryCameraEvolver(int nc, int vr, boolean[][] m)
    {
        super();
        NCameras = nc;
        VisionRange = vr;
        map = m;
    }
    @Override
    protected Chromosome[] initializePopulation(int p_size) {
        Chromosome[] ini_pop = new ChromosomeBinario[p_size];
        for ( Chromosome c : ini_pop)
        {
            c.initializeRandom();
        }

        return ini_pop;
    }

    @Override
    protected Chromosome[] crossover(Chromosome[] pop) {
        return new Chromosome[0];
    }

    @Override
    protected void evaluate(Chromosome[] pop) {

    }
}
