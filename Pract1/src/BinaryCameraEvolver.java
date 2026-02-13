public class BinaryCameraEvolver extends GeneticManager{

    private int NCameras;
    private int VisionRange;
    private boolean[][] map;
    public BinaryCameraEvolver(int nc, int vr, boolean[][] m)
    {
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
    protected void evaluate(Chromosome[] pop)
    {
        //FORMATO CROMOSOMA: (posx, posy) x nCameras
        for (Chromosome c : pop)
        {
            boolean[][] seen  = new boolean[map.length][map[0].length];
            int puntuacion = 0;
            c.calculateFenotipo();
            Integer[] sol = (Integer[]) c.fenotipo;

            //recorremos la solución, viendo cuanto ven las camaras
            for (int i = 0; i < sol.length; i+=2)
            {
                //si está en un obstaculo, la penalizamos
                if(map[sol[i]][sol[i+1]])
                {
                    puntuacion -= 100;
                }
            }




        }

    }
}
