import static java.lang.Math.min;

public class RealCameraEvolver extends GeneticManager{
    private int NCameras;
    private int VisionRange;
    private int anguloApertura;
    private boolean[][] map;
    private int[][] importancia;
    public RealCameraEvolver(int nc, int vr, int aa, boolean[][] m, int[][] i, UIclass g)
    {
        super(g);
        NCameras = nc;
        VisionRange = vr;
        anguloApertura = aa;
        map = m;
        importancia = i;
    }
    @Override
    protected Chromosome[] initializePopulation(int p_size) {
        Chromosome[] ini_pop = new ChromosomeReal[p_size];
        for ( int i = 0; i < p_size; i++)
        {
            ini_pop[i] = new ChromosomeReal(NCameras, map.length, map[0].length);
            ini_pop[i].initializeRandom();
        }
        return ini_pop;
    }

    @Override
    protected Chromosome[] crossover(Chromosome[] pop) {
        Chromosome[] sol = new Chromosome[pop.length];
        for(int i = 0; i < pop.length; i += 2){
            if(Pcruce <= Math.random()){
                sol[i] = new ChromosomeReal(NCameras,  map.length, map[0].length);
                sol[i+1] = new ChromosomeReal(NCameras,  map.length, map[0].length);
                switch (crossMethod){
                    case MONOPUNTO:
                        int cru = (int)(Math.random() * pop[i].getFenotipo().length);
                        sol[i].cruceMonopunto(pop[i], pop[i+1], cru);
                        sol[i+1].cruceMonopunto(pop[i+1], pop[i], cru);
                        break;
                    case UNIFORME:
                        float[] results = new float[pop[i].getFenotipo().length];
                        for(int j = 0; j < results.length; j++){
                            results[j] = (float) Math.random();
                        }
                        float prob = (float) Math.random();
                        sol[i].cruceUniforme(pop[i], pop[i+1], prob, results);
                        sol[i+1].cruceUniforme(pop[i+1], pop[i], prob, results);
                        break;
                    case ARITMETICO:
                        sol[i].cruceAritmetico(pop[i], pop[i+1]);
                        sol[i+1].cruceAritmetico(pop[i+1], pop[i]);
                        break;
                    case BLXALPHA:
                        float alpha = (float) Math.random();
                        sol[i].cruceBLX_Alpha(pop[i], pop[i+1], alpha);
                        sol[i+1].cruceBLX_Alpha(pop[i+1], pop[i], alpha);
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
        //FORMATO CROMOSOMA: (posx, posy, angulo) x nCameras
        for (Chromosome c : pop)
        {
            boolean[][] seen  = new boolean[map.length][map[0].length];
            int puntuacion = 0;

            Integer[] sol = (Integer[]) c.fenotipo;

            //recorremos la solución, viendo cuanto ven las camaras
            for (int i = 0; i < sol.length; i+=3)
            {
                //si está en un obstaculo, la penalizamos
                if(map[sol[i]][sol[i+1]])
                {
                    puntuacion -= 100;
                }
                //este o no, vemos como va
                {
                    //Preguntamos a todas las casillas (dentro del rango "VisionRange") si estan siendo vigiladas
                    for(int x = sol[i] - VisionRange; x <= VisionRange; x++){
                        //Se comprueba que la casilla a comprobar esta dentro del mapa
                        if(x >= 0 && x < map.length){
                            for(int y = sol[i+1] - VisionRange; y <= sol[i+1] + VisionRange; y++){
                                //Lo mismo para y
                                if(y >= 0 && y < map[0].length){
                                    //Si no se ha visto ya y si no es un muro
                                    if(!seen[x][y] && !map[x][y]){
                                        //Se le pregunta si esta dentro de su rango de vision
                                        double dis = Math.sqrt( Math.pow(sol[i] - x, 2) + Math.pow(sol[i+1] - y, 2)); //Formula matematica de distancias jaja
                                        if(dis < VisionRange){
                                            //Comprueba si la camara esta mirando la casilla
                                            int dx = x - sol[i];
                                            int dy = y - sol[i+1];
                                            double angulo = Math.toDegrees(Math.atan2(dy, dx));
                                            angulo = (angulo + 360) % 360;
                                            if(angulo > sol[i+2]-(anguloApertura /2) &&angulo < sol[i+2]+(anguloApertura /2)){
                                                //Por utlimo, comprueba que no haya nada bloqueando la vista
                                                double tmpX = dx;
                                                if(tmpX < 0) tmpX = tmpX * -1;
                                                double tmpY = dy;
                                                if(tmpY < 0) tmpY = tmpY * -1;

                                                if(tmpX < tmpY){
                                                    tmpX = dx / tmpY;
                                                    tmpY = dy / tmpY;
                                                }
                                                else{
                                                    tmpY = dy / tmpX;
                                                    tmpX = dx / tmpX;
                                                }
                                                int k = 0;
                                                boolean bloquea = false;
                                                while (k <= VisionRange && !bloquea){
                                                    int _x = (int)(sol[i] + Math.round(tmpX*k));
                                                    int _y = (int)(sol[i+1] + Math.round(tmpY*k));
                                                    if(_x >=0 &&_x <map.length &&_y >=0 &&_y <map[0].length){
                                                        if(map[_x][_y]){
                                                            bloquea = true;
                                                        }
                                                    }
                                                    k++;
                                                }
                                                if(!bloquea){
                                                    if(casillaImport){
                                                        puntuacion += importancia[sol[i]][sol[i+1]];
                                                    }
                                                    else  puntuacion += reward;
                                                    seen[x][y] = true;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            c.aptitud = puntuacion;
        }
    }

    public void drawSolutionMap(UIclass ui, ChromosomeReal c)
    {
        //FORMATO CROMOSOMA: (posx, posy, angulo) x nCameras
        boolean[][] seen  = new boolean[map.length][map[0].length];

        Integer[] sol = (Integer[]) c.fenotipo;
        float[] cameras = new float[NCameras*2];

        //recorremos la solución, viendo cuanto ven las camaras
        for (int i = 0; i < sol.length; i+=3)
        {
            {
                int camIndex = i / 3;
                cameras[camIndex * 2] = sol[i];
                cameras[camIndex * 2 + 1] = sol[i+1];

                //Preguntamos a todas las casillas (dentro del rango "VisionRange") si estan siendo vigiladas
                for(int x = sol[i] -VisionRange; x <= sol[i] + VisionRange; x++){
                    //Se comprueba que la casilla a comprobar esta dentro del mapa
                    if(x >= 0 && x < map.length){
                        for(int y = sol[i+1] -VisionRange; y <= sol[i+1] + VisionRange; y++){
                            //Lo mismo para y
                            if(y >= 0 && y < map[0].length){
                                //Si no se ha visto ya y si no es un muro
                                if(!seen[x][y] && !map[x][y]){
                                    //Se le pregunta si esta dentro de su rango de vision
                                    double dis = Math.sqrt( Math.pow(sol[i] - x, 2) + Math.pow(sol[i+1] - y, 2)); //Formula matematica de distancias jaja
                                    if(dis < VisionRange){
                                        //Comprueba si la camara esta mirando la casilla
                                        int dx = x - sol[i];
                                        int dy = y - sol[i+1];
                                        double angulo = Math.toDegrees(Math.atan2(dy, dx));
                                        angulo = (angulo + 360) % 360;
                                        if(angulo > sol[i+2]-(anguloApertura /2) &&angulo < sol[i+2]+(anguloApertura /2)){
                                            //Por utlimo, comprueba que no haya nada bloqueando la vista
                                            double tmpX = dx;
                                            if(tmpX < 0) tmpX = tmpX * -1;
                                            double tmpY = dy;
                                            if(tmpY < 0) tmpY = tmpY * -1;

                                            if(tmpX < tmpY){
                                                tmpX = dx / tmpY;
                                                tmpY = dy / tmpY;
                                            }
                                            else{
                                                tmpY = dy / tmpX;
                                                tmpX = dx / tmpX;
                                            }
                                            int k = 0;
                                            boolean bloquea = false;
                                            while (k <= VisionRange && !bloquea){
                                                int _x = (int)(sol[i] + Math.round(tmpX*k));
                                                int _y = (int)(sol[i+1] + Math.round(tmpY*k));
                                                if(_x >=0 &&_x <map.length &&_y >=0 &&_y <map[0].length){
                                                    if(map[_x][_y]){
                                                        bloquea = true;
                                                    }
                                                }
                                                k++;
                                                if(!bloquea) {
                                                    seen[x][y] = true;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        ui.setCameras(cameras, seen);

    }
}


