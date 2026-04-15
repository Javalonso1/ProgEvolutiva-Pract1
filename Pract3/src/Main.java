import java.io.*;
import java.util.ArrayList;
import java.util.Random;

public class Main {
    static int ANCHO = 15;
    static  int ALTO = 15;
    static int[][] map;
    public static void main(String[] args) {
      UIclass ui = new UIclass();
        GenerateMap(3000);
        ui.setMap(map, importancia);

        ui.simulateButton.addActionListener(e -> evolve(ui));

    }

    public static void evolve(UIclass ui)
    {
        GenerateMap(ui.seed());
        ui.setMap(map, importancia);
        RoverEvolver ev = new RoverEvolver(ui, 5, false);
        ev.evolve(ui.getGenNumber(), ui.getPopSize(), ui.elitism(),
                ui.cross(),
                ui.selection(),
                ui.mutation());

        ChromosomeDron solution = (ChromosomeDron) ev.getBestSolution();
        ev.showSolution(solution);
    }

    public static void GenerateMap(int semilla){
        map = new int[ANCHO][ALTO];
        Random rand = new Random(semilla);

        // 1  muro     2  arena   3  muestra
        for (int i = 0; i < ALTO; i++) {
            for (int j = 0; j < ANCHO; j++) {
                if (i == 0 || i == ALTO - 1 || j == 0 || j == ANCHO - 1) map[i][j] = 1;
                else  if (rand.nextDouble() < 0.15 && (i != 1 || j != 1)) map[i][j] = 1;
                else  if (rand.nextDouble() < 0.15 && (i != 1 || j != 1)) map[i][j] = 2;
                else  if (rand.nextDouble() < 0.08 && (i != 1 || j != 1)) map[i][j] = 3;
            }
        }
        /*
        visitado = new boolean[ALTO][ANCHO];
        visitado[y][x] = true;*/
    }
}