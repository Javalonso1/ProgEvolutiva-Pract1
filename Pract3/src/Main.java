import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {
    static int ANCHO = 15;
    static  int ALTO = 15;
    static ArrayList<int[][]> maps;
    public static void main(String[] args) {
      UIclass ui = new UIclass();
      maps = new ArrayList<>();
      GenerateMap(3000);
      ui.setMap(maps.get(0));

      ui.simulateButton.addActionListener(e -> evolve(ui));

    }

    public static void evolve(UIclass ui)
    {
        maps.clear();
        GenerateMap(ui.seed());
        ui.setMap(maps.get(0));
        RoverEvolver ev = new RoverEvolver(ui, ui.profundidad(), ui.inicializacionFull(), maps, ui.bloating());
        ev.evolve(ui.getGenNumber(), ui.getPopSize(), ui.elitism(),
                ui.cross(),
                ui.selection(),
                ui.mutation());

        ChromosomeRover solution = (ChromosomeRover) ev.getBestSolution();
        ev.showSolution(solution);
    }

    public static void GenerateMap(int semilla){
        for(int k = 0; k < 3; k++){
            int[][] map =new int[ANCHO][ALTO];
            Random rand = new Random(semilla + k);

            // 1  muro     2  arena   3  muestra
            for (int i = 0; i < ALTO; i++) {
                for (int j = 0; j < ANCHO; j++) {
                    if (i == 0 || i == ALTO - 1 || j == 0 || j == ANCHO - 1) map[i][j] = 1;
                    else  if (rand.nextDouble() < 0.15 && (i != 1 || j != 1)) map[i][j] = 1;
                    else  if (rand.nextDouble() < 0.15 && (i != 1 || j != 1)) map[i][j] = 3;
                    else  if (rand.nextDouble() < 0.08 && (i != 1 || j != 1)) map[i][j] = 2;
                }
            }
        /*
        visitado = new boolean[ALTO][ANCHO];
        visitado[y][x] = true;*/
            maps.add(map);
        }
    }
}