public class ChromosomeBinario extends Chromosome<Boolean, Integer> {

    int TX;
    int TY;
    public ChromosomeBinario(int NumCams, int X, int Y){
        fenotipo = new Integer[NumCams * 2];
        TX = X;
        TY = Y;
        genotipo = new Boolean[NumCams * (TX + TY)];
    }

    @Override
    public void setFenotipo(Integer[] f) {
        fenotipo = f;
        recalculateGenome();

    }

    @Override
    protected void recalculateGenome() {
        int pos = 0;
        for(int i = 0; i < fenotipo.length; i+=2){
            boolean[] aux = new boolean[TX];

            //Primero con X
            int j = 0;
            int fen = fenotipo[i];
            while (fen >1){
                aux[j] = fen%2 == 1;
                fen = fen / 2;
                j++;
            }
            aux[j] = true;
            for(j = 1; j <= TX; j++){
                genotipo[pos+ (TX-j)] = aux[j-1];
            }
            pos +=TX;

            //Luego con Y
            j = 0;
            fen = fenotipo[i+1];
            aux = new boolean[TY];
            while (fen >1){
                aux[j] = fen%2 == 1;
                fen = fen / 2;
                j++;
            }
            aux[j] = true;
            for(j = 1; j <= TY; j++){
                genotipo[pos+ (TY-j)] = aux[j-1];
            }
            pos +=TY;
        }
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
    void mutate(GeneticManager.MUTATION_TYPE t, double mutationP) {

        switch (t){
            case UNIFORM:
                for(int i = 0; i < genotipo.length; i++)
                {
                    double r = Math.random() * 100;
                    if (r > mutationP)
                        genotipo[i] = !genotipo[i];

                }
                calculateFenotipo();
                break;
            default:
                break;

        }
    }

    @Override
    void cruceMonopunto(Chromosome c1, Chromosome c2, int corte)
    {
        for(int i = 0; i < genotipo.length; i++){
            if(i < corte) genotipo[i] = (Boolean) c1.getGenotipo()[i];
            else genotipo[i] = (Boolean) c2.getGenotipo()[i];
        }
        calculateFenotipo();
    };
    @Override
    void cruceUniforme(Chromosome c1, Chromosome c2, float prob, float[]results)
    {
        for(int i = 0; i < genotipo.length; i++){
            if(results[i] < prob) genotipo[i] = (Boolean) c1.getGenotipo()[i];
            else genotipo[i] = (Boolean) c2.getGenotipo()[i];
        }
        calculateFenotipo();
    };
    @Override
    void cruceAritmetico(Chromosome c1, Chromosome c2)
    {
        //En el cromosoma binario no se utiliza
    };
    @Override
    void cruceBLX_Alpha(Chromosome c1, Chromosome c2, float alpha)
    {
        //En el cromosoma binario no se utiliza
    }

    private ChromosomeBinario(ChromosomeBinario other) {
        this.genotipo = other.genotipo.clone(); // deep copy array
        this.aptitud = other.aptitud;
        this.puntuacion = other.puntuacion;
        this.punt_acumulada = other.punt_acumulada;
        this.TX = other.TX;
        this.TY = other.TY;
        this.fenotipo = other.fenotipo.clone();
    }

    @Override
    public Chromosome copy() {
        return new ChromosomeBinario(this);
    }

    ;
}
