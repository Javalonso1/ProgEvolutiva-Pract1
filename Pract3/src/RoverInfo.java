public class RoverInfo {
    public int energyLevel;
    public int[][] map; //1: pared, 2: arena 3: muestra
    public boolean[][] visitedSquares;
    public int posX;
    public int posY;

    public RoverInfo(int[][] map)
    {
        this.map = map;
        this.energyLevel = 100; //CONSTANTE
        this.visitedSquares = new boolean[15][15];
    }

    public void clearState(int[][] map)
    {
        this.map = map;
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
}
