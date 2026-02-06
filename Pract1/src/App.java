import java.io.*;

public class App {

    int numCameras; // Numero de camaras
    int rangVision; // Alcance de camaras
    float angApertura; // Angulo de las camaras (solo si es con codificacion real)
    boolean[][] map; // Mapa de bools para guardar objetos que bloqueen las camaras
    int[][] importancia; // Mapa de valor de vigilancia por cada casilla (solo si es codificacion real)
    boolean modeReal; // Bool que es true si el algoritmo tiene codificación real y false si es
                      // binaria

    public static void main(String[] args) throws Exception {
        LeerData("data1");
    }

    // Metodo que lee el fichero de la carpeta /data y pasa los valores a las
    // variables locales
    public static void LeerData(String filename) {
        try (FileReader fr = new FileReader("./data/" + filename + ".txt")) {
            BufferedReader br = new BufferedReader(fr);
            // Lectura del fichero
            String linea;
            /*
             * while ((linea = br.readLine()) != null)
             * System.out.println(linea);
             */
            br.readLine();
            linea = br.readLine();
            System.out.println(linea);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
