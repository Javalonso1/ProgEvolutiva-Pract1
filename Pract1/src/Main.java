import javax.swing.*;
import java.awt.*;
import java.io.*;
public class Main {
    static int numCameras; // Numero de camaras
    static int rangVision; // Alcance de camaras
    static float angApertura; // Angulo de las camaras (solo si es con codificacion real)
    static boolean[][] map; // Mapa de bools para guardar objetos que bloqueen las camaras
    static int[][] importancia; // Mapa de valor de vigilancia por cada casilla (solo si es codificacion real)
    static boolean modeReal; // Bool que es true si el algoritmo tiene codificación real y false si es binaria

    public static void main(String[] args) {
        /*
        System.out.printf("Hello and welcome!");

        for (int i = 1; i <= 5; i++) {
            System.out.println("i = " + i);
        }

         */
        LeerData("data1");

        SwingUtilities.invokeLater(() -> new UIclass() );
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
            angApertura = Float.parseFloat(linea);
            br.readLine();
            linea = br.readLine();
            int x = Integer.parseInt(linea);
            linea = br.readLine();
            int y = Integer.parseInt(linea);
            map = new boolean[x] [y];
            for (int i = 0; i < x; i++) {
                String[] valores = br.readLine().trim().split("\\s+");
                for (int j = 0; j < y; j++) {
                    map[i][j] = (valores[j] == "1");
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