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
                else {
                    //Se miran por los 4 lados
                    boolean stopArriba = false;
                    boolean stopAbajo = false;
                    boolean stopIzquierda = false;
                    boolean stopDerecha = false;
                    int aux = 0;
                    while (!stopArriba || ! stopAbajo|| ! stopDerecha|| ! stopIzquierda){
                        if(sol[i+1] - aux < 0 || map[sol[i]][sol[i+1] - aux]) stopArriba = true;
                        if(sol[i+1] + aux >= map[0].length || map[sol[i]][sol[i+1] + aux]) stopAbajo = true;
                        if(sol[i] - aux < 0 || map[sol[i]- aux][sol[i+1]]) stopIzquierda = true;
                        if(sol[i] + aux >= map.length || map[sol[i]+ aux][sol[i+1]]) stopDerecha = true;

                        //Arriba
                        if(!stopArriba && !seen[sol[i]][sol[i+1] - aux]){
                            seen[sol[i]][sol[i+1] - aux] = true;
                            //Aqui habra que añadir algo mas si se quisiera que las casillas pudieran valer distinto
                            puntuacion += 1;
                        }

                        //Abajo
                        if(!stopAbajo && !seen[sol[i]][sol[i+1] + aux]){
                            seen[sol[i]][sol[i+1] + aux] = true;
                            //Aqui habra que añadir algo mas si se quisiera que las casillas pudieran valer distinto
                            puntuacion += 1;
                        }

                        //Izquierda
                        if(!stopIzquierda && !seen[sol[i]- aux][sol[i+1]]){
                            seen[sol[i] - aux][sol[i+1]] = true;
                            //Aqui habra que añadir algo mas si se quisiera que las casillas pudieran valer distinto
                            puntuacion += 1;
                        }

                        //Derecha
                        if(!stopDerecha && !seen[sol[i]+ aux][sol[i+1]]){
                            seen[sol[i] + aux][sol[i+1]] = true;
                            //Aqui habra que añadir algo mas si se quisiera que las casillas pudieran valer distinto
                            puntuacion += 1;
                        }

                        aux++;
                        if(aux > VisionRange) {
                            stopArriba = true;
                            stopAbajo = true;
                            stopDerecha = true;
                            stopIzquierda = true;
                        }
                    }
                }
            }
            c.aptitud = puntuacion;
        }
    }
}
