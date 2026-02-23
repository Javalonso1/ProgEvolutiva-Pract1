import static java.lang.Math.min;

public class BinaryCameraEvolver extends GeneticManager{

    private int NCameras;
    private int VisionRange;
    private boolean[][] map;
    private int[][] importancia;
    public BinaryCameraEvolver(int nc, int vr, boolean[][] m, int[][] i, UIclass.GraphPanel g)
    {
        super(g);
        NCameras = nc;
        VisionRange = vr;
        map = m;
        importancia = i;
    }
    @Override
    protected Chromosome[] initializePopulation(int p_size) {
        Chromosome[] ini_pop = new ChromosomeBinario[p_size];
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
        for ( int i = 0; i < p_size; i++)
        {
            ini_pop[i] = new ChromosomeBinario(NCameras, cifX, cifY);
            ini_pop[i].initializeRandom();
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
            if(Pcruce < Math.random()){
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
            else {
                sol[i] = pop[i].copy();
                sol[i+1] =pop[i+1].copy();
            }
        }
        return sol;
    }

    @Override
    protected void evaluate(Chromosome[] pop, boolean casillaImport)
    {
        int reward = 1;
        //FORMATO CROMOSOMA: (posx, posy) x nCameras
        for (Chromosome c : pop)
        {
            //vamos construyendo el mapita y eso (para llevar la cuenta de las camaras)
            //basicamente, las camaras no ven a traves de las camaras
            //0 nada, 1 cuadrado visto, 2 cuadrado con camara
            int[][] seen  = new int[map.length][map[0].length];
            int puntuacion = 0;
            boolean changeGenome = false;
            c.calculateFenotipo();
            Integer[] sol = (Integer[]) c.fenotipo;

            //si el fenotipo no es correcto, lo corregimos

            //recorremos la solución, viendo cuanto ven las camaras
            for (int i = 0; i < sol.length; i+=2)
            {
                //si la posición no es correcta, la corregimos:
                if(sol[i] > map.length-1 || sol[i+1] > map[sol[i]].length-1)
                {
                    sol[i] = min(map.length-1, sol[i]);
                    sol[i+1] = min(map[sol[i]].length-1, sol[i+1]);
                    changeGenome = true;
                }

                //si está en un obstaculo, la penalizamos
                if(map[sol[i]][sol[i+1]])
                {
                    puntuacion -= 100;
                }
                //vemos como va
                else{
                    seen[sol[i]][sol[i+1]] = 2;
                    //Se miran por los 4 lados
                    boolean stopArriba = false;
                    boolean stopAbajo = false;
                    boolean stopIzquierda = false;
                    boolean stopDerecha = false;
                    int aux = 1;
                    while (!stopArriba || ! stopAbajo|| ! stopDerecha|| ! stopIzquierda){
                        if(sol[i+1] - aux < 0 || map[sol[i]][sol[i+1] - aux] ||seen[sol[i]][sol[i+1] - aux]==2) stopArriba = true;
                        if(sol[i+1] + aux >= map[0].length || map[sol[i]][sol[i+1] + aux] ||seen[sol[i]][sol[i+1] + aux] ==2) stopAbajo = true;
                        if(sol[i] - aux < 0 || map[sol[i]- aux][sol[i+1]]||seen[sol[i]- aux][sol[i+1]]==2) stopIzquierda = true;
                        if(sol[i] + aux >= map.length || map[sol[i]+ aux][sol[i+1]]||seen[sol[i]+ aux][sol[i+1]]==2) stopDerecha = true;


                        //Arriba
                        if(!stopArriba && seen[sol[i]][sol[i+1] - aux]!=1){
                            seen[sol[i]][sol[i+1] - aux] = 1;
                            if(casillaImport){
                                puntuacion += importancia[sol[i]][sol[i+1]];
                            }
                            else  puntuacion += reward;
                        }

                        //Abajo
                        if(!stopAbajo && seen[sol[i]][sol[i+1] + aux]!= 1){
                            seen[sol[i]][sol[i+1] + aux] = 1;
                            if(casillaImport){
                                puntuacion += importancia[sol[i]][sol[i+1]];
                            }
                            else  puntuacion += reward;
                        }

                        //Izquierda
                        if(!stopIzquierda && seen[sol[i]- aux][sol[i+1]]!=1){
                            seen[sol[i] - aux][sol[i+1]] = 1;
                            if(casillaImport){
                                puntuacion += importancia[sol[i]][sol[i+1]];
                            }
                            else  puntuacion += reward;
                        }

                        //Derecha
                        if(!stopDerecha && seen[sol[i]+ aux][sol[i+1]]!=1){
                            seen[sol[i] + aux][sol[i+1]] = 1;
                            if(casillaImport){
                                puntuacion += importancia[sol[i]][sol[i+1]];
                            }
                            else  puntuacion += reward;
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
            //por si ha cambiado su fenotipo (no era valido), lo recalculamos:
            if (changeGenome)
                c.setFenotipo(sol);
        }
    }
}
