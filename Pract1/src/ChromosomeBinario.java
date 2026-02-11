public class ChromosomeBinario extends Chromosome{

    int TX;
    int TY;
    public ChromosomeBinario(int NumCams, int X, int Y){
        fenotipo = new double[NumCams * 2];
        TX = X;
        TY = Y;
        genotipo = new boolean[NumCams * (TX + TY)];
    }

    public void initializeRandom(){
        for(int i = 0; i < genotipo.length; i++){
            genotipo[i] = (int)(Math.random() * 2) == 1;
        }
        calculateFenotipo();
    }

    void calculateFenotipo(){
        for(int i = 0; i < fenotipo.length * 0.5f; i++){
            int sol = 0;
            for(int j = 0; j < TX; j++){
                int aux = 0;
                if(genotipo[i*(TX+TY)+j]) aux = 1;
                sol += Math.pow(2, TX-(j+1)) * aux;
            }
            fenotipo[i*2] = sol;
            sol = 0;
            for(int j = 0; j < TY; j++){
                int aux = 0;
                if(genotipo[i*(TX+TY)+j]) aux = 1;
                sol += Math.pow(2, TY-(j+1)) * aux;
            }
            fenotipo[i*2 + 1] = sol;
        }
    }
}
