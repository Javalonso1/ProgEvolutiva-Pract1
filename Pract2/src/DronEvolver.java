import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;

public class DronEvolver extends GeneticManager{

    float[] velocidades = {1.5F, 1, 0.7F, 1.2F, 0.5F};
    private boolean[][] map;
    private int[][] importancia;
    private int nDrones;
    private ArrayList<Integer> posCameras;
    int  Xbase= 1, Ybase= 1;
    int maxPosicionesCruceOXPP = 4;
    public DronEvolver(UIclass g, int _nDrones, ArrayList<Integer> _posCameras, boolean[][] m, int[][] i)
    {
        super(g);
        this.nDrones = _nDrones;
        map =  m;
        importancia = i;
        posCameras = _posCameras;
    }
    @Override
    protected Chromosome[] initializePopulation(int p_size){
        Chromosome[] ini_pop = new ChromosomeDron[p_size];
        for ( int i = 0; i < p_size; i++)
        {
            ini_pop[i] = new ChromosomeDron(posCameras.size()/2, nDrones,this);
            ini_pop[i].initializeRandom();
        }
        return  ini_pop;
    }

    @Override
    protected Chromosome[] crossover(Chromosome[] pop){
        Chromosome[] sol = new Chromosome[pop.length];
        for(int i = 0; i < pop.length; i += 2){
            if(Pcruce <= Math.random()){
                sol[i] = new ChromosomeDron(posCameras.size()/2, nDrones,this);
                sol[i+1] = new ChromosomeDron(posCameras.size()/2, nDrones,this);
                switch (crossMethod){
                    case PMX:
                        int pos1 = (int)(Math.random() * pop[i].getFenotipo().length);
                        int pos2 = (int)(Math.random() * pop[i].getFenotipo().length);
                        if(pos1 == pos2) {
                            if (pos1==0) pos2++;
                            else  pos1--;
                        }
                        else if(pos1 > pos2){
                            int aux = pos1;
                            pos1 = pos2;
                            pos2 = aux;
                        }
                        sol[i].crucePMX(pop[i], pop[i+1], pos1,pos2);
                        sol[i+1].crucePMX(pop[i+1], pop[i], pos1,pos2);
                        break;
                    case OX:
                        pos1 = (int)(Math.random() * pop[i].getFenotipo().length);
                        pos2 = (int)(Math.random() * pop[i].getFenotipo().length);
                        if(pos1 == pos2) {
                            if (pos1==0) pos2++;
                            else  pos1--;
                        }
                        else if(pos1 > pos2){
                            int aux = pos1;
                            pos1 = pos2;
                            pos2 = aux;
                        }
                        sol[i].cruceOX(pop[i], pop[i+1], pos1,pos2);
                        sol[i+1].cruceOX(pop[i+1], pop[i], pos1,pos2);
                        break;
                    case OXPP:
                        int mx = (int)(Math.random() * maxPosicionesCruceOXPP);
                        if(mx >= pop[i].getFenotipo().length) mx = pop[i].getFenotipo().length-1;
                        int [] cruces = new  int[mx];
                        for(int j = 0; j < mx; j++) {
                            cruces[j] =(int)(Math.random() * pop[i].getFenotipo().length);
                            boolean correct = true;
                            int k = j-1;
                            while (k>=0 && correct){
                                if(cruces[j] == cruces[k]) correct = false;
                                k--;
                            }
                            if(!correct) j--;
                        }
                        sol[i].cruceOXPP(pop[i], pop[i+1], cruces);
                        sol[i+1].cruceOXPP(pop[i+1], pop[i], cruces);
                        break;
                    case CX:
                        sol[i].cruceCX(pop[i], pop[i+1]);
                        sol[i+1].cruceCX(pop[i+1], pop[i]);
                        break;
                    case CO:
                        int cru = (int)(Math.random() * pop[i].getFenotipo().length);
                        sol[i].cruceCO(pop[i], pop[i+1], cru);
                        sol[i+1].cruceCO(pop[i+1], pop[i], cru);
                        break;
                    case ERX:
                        sol[i].cruceERX(pop[i], pop[i+1]);
                        sol[i+1].cruceERX(pop[i+1], pop[i]);
                        break;
                    case CUSTOM:
                        //A hacer
                        sol[i] = pop[i].copy();
                        sol[i+1] = pop[i+1].copy();
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
    protected void evaluate(Chromosome[] pop) {
        //por cada dron, calculamos el coste de su trayecto
        for (Chromosome c: pop)
        {
            Integer[] fenotipo = (Integer[]) c.fenotipo;
            double[] tiempoDrones = new double[nDrones];
            int index=0, currDron=0;

            int xOrigen = Xbase, yOrigen = Ybase, xDestino, yDestino;
            while(index < fenotipo.length)
            {
                if(fenotipo[index]-1 >= posCameras.size()/2)
                {
                    double coste = aStar(xOrigen, yOrigen, Xbase, Ybase);
                    tiempoDrones[currDron] += coste / velocidades[currDron];

                    currDron++;
                    if(currDron == 3)
                    {
                        int i = 0;
                    }
                    xOrigen = Xbase;
                    yOrigen = Ybase;
                }
                else {

                    xDestino = posCameras.get((fenotipo[index]-1)*2);
                    yDestino = posCameras.get((fenotipo[index]-1)*2 +1);

                    double coste = aStar(xOrigen, yOrigen, xDestino, yDestino);
                    tiempoDrones[currDron] += coste / velocidades[currDron];

                    xOrigen = xDestino;
                    yOrigen = yDestino;
                }
                index++;
            }

            // último dron
            double coste = aStar(xOrigen, yOrigen, Xbase, Ybase);
            tiempoDrones[currDron] += coste / velocidades[currDron];

            // fitness = max(T_i) + Penalizacion_Desequilibrio[(max - min) * peso]
            double maxTiempo = tiempoDrones[0];
            double minTiempo = tiempoDrones[0];
            for (int i = 1; i < tiempoDrones.length; i++) {
                if (tiempoDrones[i] > maxTiempo)
                    maxTiempo = tiempoDrones[i];
                if (tiempoDrones[i] < minTiempo)
                    minTiempo = tiempoDrones[i];
            }

            c.aptitud = maxTiempo + ((maxTiempo - minTiempo) * 0.5);
        }

    }

    public double aStar(int xo, int yo, int xd, int yd) {

        PriorityQueue<Node> open = new PriorityQueue<>(Comparator.comparingDouble(n -> n.f));

        boolean[][] closed = new boolean[importancia.length][importancia[0].length];
        double[][] gScore = new double[map.length][map[0].length];
        for (int i = 0; i < map.length; i++) {
            Arrays.fill(gScore[i], Double.MAX_VALUE);
        }

        Node startNode = new Node(xo, yo);
        gScore[xo][yo] = 0;
        open.add(startNode);

        while(!open.isEmpty()){

            Node current = open.poll();

            if (closed[current.x][current.y])
                continue;


            if(current.x == xd && current.y == yd){
                return current.g;
            }

            closed[current.x][current.y] = true;

            int[][] dirs = {{1,0},{-1,0},{0,1},{0,-1}};

            for(int[] d : dirs) {

                int nx = current.x + d[0];
                int ny = current.y + d[1];

                if(nx<0 || ny<0 || nx>=map.length || ny>=map[0].length)
                    continue;

                if(importancia[nx][ny] == 0) // muro
                    continue;

                if(closed[nx][ny])
                    continue;

                double cost = (double) importancia[nx][ny];

                if(hayCamara(nx, ny) && nx != xd && ny !=yd)
                    cost+=500;

                double tentativeG = current.g + cost;

                // solo aceptar si mejora el camino
                if (tentativeG < gScore[nx][ny]) {

                    gScore[nx][ny] = tentativeG;

                    Node n = new Node(nx, ny);
                    n.g = tentativeG;
                    n.h = Math.abs(xd - nx) + Math.abs(yd - ny);
                    n.f = n.g + n.h;
                    n.parent = current;

                    open.add(n);
                }
            }
        }

        return Double.MAX_VALUE;
    }

    private boolean hayCamara(int x, int y) {
        for (int i = 0; i < posCameras.size()/2; i++)
            if (posCameras.get(i*2) == x &&posCameras.get(i*2+1) == y)
                return true;
        return false;
    }

}
