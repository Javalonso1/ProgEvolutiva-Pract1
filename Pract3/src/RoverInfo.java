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
        int dist = map.length;
        int x = posX, y = posY;
        for (int i = 1; i < map.length; i++) {
            x += directions[currDir][0];
            y += directions[currDir][1];

            // Check bounds properly
            if (x < 0 || y < 0 || x >= map.length || y >= map[0].length) {
                return 100; // nothing found
            }

            if (map[x][y] == 3) {
                return i - 1; // distance excluding current position
            }
        }

        return 100; // nothing found
    }
    public int distToNearestObstaculo()
    {
        int dist = map.length;
        int x = posX, y = posY;
        for (int i = 1; i < map.length; i++) {
            x += directions[currDir][0];
            y += directions[currDir][1];

            // Check bounds properly
            if (x < 0 || y < 0 || x >= map.length || y >= map[0].length) {
                return 100; // nothing found
            }

            if (map[x][y] == 1) {
                return i - 1; // distance excluding current position
            }
        }

        return 100; // nothing found
    }

    public int distToNearestArena()
    {
        int dist = map.length;
        int x = posX, y = posY;
        for (int i = 1; i < map.length; i++) {
            x += directions[currDir][0];
            y += directions[currDir][1];

            // Check bounds properly
            if (x < 0 || y < 0 || x >= map.length || y >= map[0].length) {
                return 100; // nothing found
            }

            if (map[x][y] == 2) {
                return i - 1; // distance excluding current position
            }
        }

        return 100; // nothing found
    }

    public void girarIzq()
    {
        energyLevel--;
        girosSeguidos++;
        if(girosSeguidos >= 4)
            energyLevel-=100;

        currDir = (currDir + 4 - 1) % 4;
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
    public boolean checkIfMuestraInVision() {
        int x = posX;
        int y = posY;

        for (int i = 1; i < map.length; i++) {
            x += directions[currDir][0];
            y += directions[currDir][1];

            // Bounds check
            if (x < 0 || y < 0 || x >= map.length || y >= map[0].length) {
                return false;
            }

            if (map[x][y] == 3) {
                return true;
            }
        }

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
            i++;
        }

        return visitadas < 4;
    }
}
