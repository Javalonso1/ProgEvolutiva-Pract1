public class RoverInfo {
    public int energyLevel;
    public int[][] map; //1: pared, 2: arena 3: muestra
    public boolean[][] visitedSquares;
    public int posX;
    public int posY;
    public int currDir;
    public int[][] directions = {{1, 0},{0, 1}, {-1, 0}, {0, -1}};
    public int nTurnos;
    public int girosSeguidos = 0;

    public RoverInfo(int[][] map)
    {
        this.map = map;
        posX = 1;
        posY = 1;
        currDir = 0;
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
        currDir = 0;
        this.energyLevel = 100; //CONSTANTE
        this.visitedSquares = new boolean[15][15];
    }

    public int distToNearestMuestra()
    {
        //TODO
        return -1;
    }
    public int distToNearestObstaculo()
    {
        //TODO
        return -1;
    }

    public int distToNearestArena()
    {
        //TODO
        return -1;
    }

    public void girarIzq()
    {
        energyLevel--;
        girosSeguidos++;
        if(girosSeguidos >= 4)
            energyLevel-=100;

        currDir = (currDir + currDir - 1) % 4;
    }
    public void girarDer()
    {
        energyLevel--;
        girosSeguidos++;
        if(girosSeguidos >= 4)
            energyLevel-=100;

        currDir = (currDir + 1)%4;
    }
    public int avanzar()
    {
        girosSeguidos = 0;
        posX += directions[currDir][0];
        posY += directions[currDir][1];
        int recompensa = 0;
        if (map[posX][posY] == 3)   //si es una muestra
        {
            //energyLevel --;
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
            posX -= directions[currDir][0];
            posY -= directions[currDir][1];
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
        //TODO
        return false;
    }
    public boolean isLazy()
    {
        int visitadas = 0;
        int i = 0;
        while (i < map.length && visitadas < 4)
        {
            for (int j = 0; j < map[i].length; j++)
            {
                if (visitedSquares[i][j])
                    visitadas++;
            }
        }

        return visitadas < 4;
    }
}
