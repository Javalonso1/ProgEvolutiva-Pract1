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

    @Override
    void mutate(GeneticManager.MUTATION_TYPE t) {

    }

    void cruceMonopunto(Chromosome c1, Chromosome c2, int corte)
    {
        for(int i = 0; i < genotipo.length; i++){
            if(i < corte) genotipo[i] = c1.getGenotipo()[i];
            else genotipo[i] = c2.getGenotipo()[i];
        }
        calculateFenotipo();
    };
    void cruceUniforme(Chromosome c1, Chromosome c2, float prob, float[]results)
    {
        for(int i = 0; i < genotipo.length; i++){
            if(results[i] < prob) genotipo[i] = c1.getGenotipo()[i];
            else genotipo[i] = c2.getGenotipo()[i];
        }
        calculateFenotipo();
    };
    void cruceAritmetico(Chromosome c1, Chromosome c2)
    {
        //En el cromosoma binario no se utiliza
    };
    void cruceBLX_Alpha(Chromosome c1, Chromosome c2)
    {
        //En el cromosoma binario no se utiliza
    };
}
