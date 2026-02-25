import javax.swing.*;
import java.awt.*;
import java.io.*;
public class Main {
    static int numCameras; // Numero de camaras
    static int rangVision; // Alcance de camaras
    static int angApertura; // Angulo de las camaras (solo si es con codificacion real)
    static boolean[][] map; // Mapa de bools para guardar objetos que bloqueen las camaras
    static int[][] importancia; // Mapa de valor de vigilancia por cada casilla (solo si es codificacion real)
    static boolean modeReal; // Bool que es true si el algoritmo tiene codificación real y false si es binaria

    public static void main(String[] args) {
        LeerData("data3");

        UIclass ui = new UIclass();
        ui.setMap(map);
        ui.simulateButton.addActionListener(e -> evolve(ui));

    }

    public static void evolve(UIclass ui)
    {
        GeneticManager ev;
        if (ui.isBinary())
        {
            ev = new BinaryCameraEvolver(numCameras, rangVision, map, importancia, ui);
            ev.evolve(1000, 200, ui.elitism(), true,
                    ui.cross(),
                    ui.selection(),
                    ui.mutation());
        }
        else
        {
            ev = new RealCameraEvolver(numCameras, rangVision, angApertura, map, importancia, ui);
            ev.evolve(1000, 200, ui.elitism(), true,
                    ui.cross(),
                    ui.selection(),
                    ui.mutation());
        }


        Chromosome solution = ev.getBestSolution();
    }


    public static void LeerData(String filename) {
        try (FileReader fr = new FileReader("./data/" + filename + ".txt")) {
            BufferedReader br = new BufferedReader(fr);
            // Lectura del fichero
            String linea;
            br.readLine();
            linea = br.readLine();
            numCameras = Integer.parseInt(linea);
            br.readLine();
            linea = br.readLine();
            rangVision = Integer.parseInt(linea);
            br.readLine();
            linea = br.readLine();
            angApertura = (int)Float.parseFloat(linea);
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