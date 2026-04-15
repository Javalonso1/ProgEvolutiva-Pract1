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
        posX = 1;
        posY = 1;
        dirX = 1;
        dirY = 0;
        this.nTurnos = 0;
        this.energyLevel = 100; //CONSTANTE
        this.visitedSquares = new boolean[15][15];
    }

    public void clearState(int[][] map)
    {
        this.map = map;
        this.nTurnos = 0;
        posX = 1;
        posY = 1;
        dirX = 1;
        dirY = 0;
        this.energyLevel = 100; //CONSTANTE
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
        //TODO: q gire
        energyLevel--;
        girosSeguidos++;
        if(girosSeguidos >= 4)
            energyLevel-=100;
    }
    public void girarDer()
    {
        //TODO: q gire
        energyLevel--;
        girosSeguidos++;
        if(girosSeguidos >= 4)
            energyLevel-=100;
    }
    public int avanzar()
    {
        girosSeguidos = 0;
        posX+= dirX;
        posY += dirY;
        int recompensa = 0;
        if (map[posX][posY] == 3)
        {
            energyLevel --;
            map[posX][posY] = 0;    //quitamos la muestra
            recompensa += 500;
            visitedSquares[posX][posY] = true;
        }
        else if(map[posX][posY] == 2)   //si es arena
        {
            energyLevel -= 10;
            recompensa -= 30;
        }
        else if(map[posX][posY] == 1)   //si es un muro
        {
            energyLevel -= 2;
            recompensa -= 30;

            //y te desmueves
            posX -= dirX;
            posY -= dirY;
        }
        else                    //si es un sitio libre
        {
            energyLevel --;
            if (!visitedSquares[posX][posY])
            {
                visitedSquares[posX][posY] = true;
                recompensa += 20;
            }
        }
        return recompensa;
    }
    public boolean checkIfMuestraInVision()
    {
        return false;
    }
}
