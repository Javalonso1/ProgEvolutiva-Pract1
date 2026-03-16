import java.io.*;
import java.util.ArrayList;
import java.util.Random;

public class Main {
    static boolean[][] map; // Mapa de bools para guardar objetos que bloqueen las camaras
    static int[][] importancia; // Mapa de valor de vigilancia por cada casilla (solo si es codificacion real)
    static int numCameras = 40;
    static  int numDrones = 3;
    static  int semilla = 3000;
    public static void main(String[] args) {
        LeerDataP1("data3");

        UIclass ui = new UIclass();
        ui.setMap(map);
        ui.simulateButton.addActionListener(e -> evolve(ui));

    }

    public static void evolve(UIclass ui)
    {
        LeerDataP1("data"+ ui.mapChosen());
        ui.setMap(map);
        DronEvolver ev = new DronEvolver(ui, numDrones, placeCameras(semilla), map, importancia);
        ev.evolve(ui.getGenNumber(), ui.getPopSize(), ui.elitism(), ui.ponderation(),
                ui.cross(),
                ui.selection(),
                ui.mutation());

        //ChromosomeReal solution = (ChromosomeReal) ev.getBestSolution();
        //ev.drawSolutionMap(ui, solution);
    }

    public static ArrayList<Integer> placeCameras(int seed) {
        Random rand = new Random(seed);
        //La semilla se selecciona de la interfaz

        // Lista para guardar las posiciones (asumiendo una clase simple Punto(x,y))
        ArrayList<Integer> posicionesCamaras = new ArrayList<>();

        // 3. Generar las posiciones de las cámaras
        while (posicionesCamaras.size() < numCameras*2) {
            // Generar coordenadas aleatorias dentro de los límites del mapa
            int x = rand.nextInt(map.length);
            int y = rand.nextInt(map[0].length);

            boolean valid = true;
            for(int i = 0; i < posicionesCamaras.size()/2; i++)
            {
                if(posicionesCamaras.get(i) == x && posicionesCamaras.get(i+1) == y)
                    valid = false;
            }
            if(map[x][y])
                valid = false;

            if(valid)
            {
                posicionesCamaras.add(x);
                posicionesCamaras.add(y);
            }
        }

        return posicionesCamaras;
    }


    public static void LeerDataP1(String filename) {
        try (FileReader fr = new FileReader("./data/" + filename + ".txt")) {
            BufferedReader br = new BufferedReader(fr);
            // Lectura del fichero
            String linea;
            br.readLine();
            br.readLine();
            br.readLine();
            br.readLine();
            br.readLine();
            br.readLine();
            br.readLine();
            linea = br.readLine();
            int x = Integer.parseInt(linea);
            linea = br.readLine();
            int y = Integer.parseInt(linea);
            map = new boolean[x] [y];
            for (int i = 0; i < x; i++) {
                String[] valores = br.readLine().trim().split("\\s+");
                for (int j = 0; j < y; j++) {
                    map[i][j] = (valores[j].equals("1"));
                }
            }
            importancia = new int[x][y];
            br.readLine();
            for (int i = 0; i < x; i++) {
                String[] valores = br.readLine().trim().split("\\s+");
                for (int j = 0; j < y; j++) {
                    importancia[i][j] = Integer.parseInt(valores[j]);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}