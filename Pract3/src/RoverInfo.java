public class RoverInfo {
    public int energyLevel;
    public int[][] map; //1: pared, 2: arena 3: muestra
    public boolean[][] visitedSquares;
    public int posX;
    public int posY;
    public int dirX;
    public int dirY;
    public int nTurnos;
    public int girosSeguidos = 0;

    public RoverInfo(int[][] map)
    {
        this.map = map;
        this.nTurnos = 0;
        this.energyLevel = 100; //CONSTANTE
        this.visitedSquares = new boolean[15][15];
    }

    public void clearState(int[][] map)
    {
        this.map = map;
        this.nTurnos = 0;
        this.energyLevel = 50; //CONSTANTE
        this.visitedSquares = new boolean[15][15];
    }

    public int distToNearestMuestra()
    {
        return -1;
    }
    public int distToNearestObstaculo()
    {
        return -1;
    }

    public int distToNearestArena()
    {
        return -1;
    }

    public void girarIzq()
    {

    }
    public void girarDer()
    {

    }
    public boolean checkIfMuestraInVision()
    {
        return false;
    }
}
