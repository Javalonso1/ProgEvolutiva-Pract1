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
        Chromosome[] sol = new Chromosome[pop.length];
        int cifX = 1;
        int cifY = 1;
        int aux = map.length;
        while (aux > 1){
            cifX++;
            aux = aux / 2;
        }
        aux = map[0].length;
        while (aux > 1){
            cifY++;
            aux = aux / 2;
        }
        for(int i = 0; i < pop.length; i += 2){
            sol[i] = new ChromosomeBinario(NCameras, cifX, cifY);
            sol[i+1] = new ChromosomeBinario(NCameras, cifX, cifY);
            switch (crossMethod){
                case MONOPUNTO:
                    int cru = (int)(Math.random() * pop[i].getGenotipo().length);
                    sol[i].cruceMonopunto(pop[i], pop[i+1], cru);
                    sol[i+1].cruceMonopunto(pop[i+1], pop[i], cru);
                    break;
                case UNIFORME:
                    float[] results = new float[pop[i].getGenotipo().length];
                    for(int j = 0; j < results.length; j++){
                        results[j] = (float) Math.random();
                    }
                    float prob = (float) Math.random();
                    sol[i].cruceUniforme(pop[i], pop[i+1], prob, results);
                    sol[i+1].cruceUniforme(pop[i+1], pop[i], prob, results);
                    break;
                default:
                    break;
            }
        }
        return sol;
    }

    @Override
    protected void evaluate(Chromosome[] pop) {

    }
}
