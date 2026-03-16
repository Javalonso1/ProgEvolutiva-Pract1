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
    int  Xbase= 0, Ybase= 0;
    public DronEvolver(UIclass g, int nDrones, ArrayList<Integer> posCameras, boolean[][] m, int[][] i)
    {
        super(g);
        this.nDrones = nDrones;
        map =  m;
        importancia = i;
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
        //por cada dron, calculamos el coste de su trayecto
        for (Chromosome c: pop)
        {
            Integer[] fenotipo = (Integer[]) c.fenotipo;
            double[] tiempoDrones = new double[nDrones];
            int index=0, currDron=0;

            int xOrigen = Xbase, yOrigen = Ybase, xDestino, yDestino;
            while(index < fenotipo.length)
            {
                if(fenotipo[index] >= posCameras.size()/2)
                {
                    currDron++;
                    double coste = aStar(xOrigen, yOrigen, Xbase, Ybase);
                    tiempoDrones[currDron] += coste / velocidades[currDron];

                    currDron++;
                    xOrigen = Xbase;
                    yOrigen = Ybase;
                }
                else {

                    xDestino = posCameras.get(fenotipo[index]*2);
                    yDestino = posCameras.get(fenotipo[index]*2 +1);

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

            // fitness = max(T_i)
            double maxTiempo = tiempoDrones[0];
            for (int i = 1; i < tiempoDrones.length; i++) {
                if (tiempoDrones[i] > maxTiempo)
                    maxTiempo = tiempoDrones[i];
            }

            c.aptitud = maxTiempo;
        }

    }

    public double aStar(int xo, int yo, int xd, int yd) {

        double[][] costeMin = new double[map.length][map[0].length];   
        boolean[][] closed = new boolean[map.length][map[0].length];

        for (int i = 0; i < map.length; i++)
            Arrays.fill(costeMin[i], Double.POSITIVE_INFINITY);

        PriorityQueue<int[]> open = new PriorityQueue<>(    //aqui con nuestra eurisitica y eso
                Comparator.comparingDouble(a -> costeMin[a[0]][a[1]] + Math.abs(a[0] - xd) + Math.abs(a[1] - yd))
        );


        costeMin[xo][yo] = 0;
        open.add(new int[]{xo, yo});

        int[][] mov = {{1,0},{-1,0},{0,1},{0,-1}};

        while (!open.isEmpty()) {

            int[] actual = open.poll();
            int x = actual[0];
            int y = actual[1];

            if (x == xd && y == yd)
                return costeMin[x][y];

            closed[x][y] = true;

            for (int[] m : mov) {

                int nx = x + m[0];
                int ny = y + m[1];

                if (nx < 0 || ny < 0 || nx >= map.length || ny >= map[0].length ||closed[nx][ny] ||map[nx][ny])
                    continue;


                double coste = importancia[nx][ny];

                if (hayCamara(nx, ny) && !(nx == xd && ny == yd))
                    coste += 500;

                double nuevoCoste = costeMin[x][y] + coste;

                if (nuevoCoste < costeMin[nx][ny]) {
                    costeMin[nx][ny] = nuevoCoste;
                    open.add(new int[]{nx, ny});
                }
            }
        }

        return Double.POSITIVE_INFINITY;
    }

    private boolean hayCamara(int x, int y) {
        for (int i = 0; i < posCameras.size()/2; i++)
            if (posCameras.get(i*2) == x &&posCameras.get(i*2+1) == y)
                return true;
        return false;
    }

}
