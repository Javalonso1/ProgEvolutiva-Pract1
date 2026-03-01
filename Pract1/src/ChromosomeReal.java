import java.util.Random;

public class ChromosomeReal extends Chromosome<Float, Integer> {

        int TX;
        int TY;
        public ChromosomeReal(int NumCams, int X, int Y){
            fenotipo = new Integer[NumCams * 3];
            TX = X;
            TY = Y;
        }

        @Override
        public void setFenotipo(Integer[] f) {
            if(fenotipo != f)
            {
                fenotipo = f;
            }
        }

        @Override
        protected void recalculateGenome() {
            //El Cromosoma no binario no usa el genotipo
        }

        public void initializeRandom(){
            for(int i = 0; i < fenotipo.length; i += 3){
                fenotipo[i] = (int)(Math.random() * TX);
                fenotipo[i + 1] = (int)(Math.random() * TY);
                fenotipo[i + 2] = (int)(Math.random() * 360);
            }
        }

        void calculateFenotipo(){
            //Solo se usan los fenotipos, por lo que no hace falta calcular nada en base del genotipo
        }

        @Override
        void mutate(GeneticManager.MUTATION_TYPE t, double mutationp) {
            switch (t){
                case UNIFORM:
                    int pos = (int) (Math.random() * fenotipo.length);
                    if(pos%3 == 0){
                        fenotipo[pos] = (int) (Math.random()*TX);
                    }
                    else if(pos%3 == 1){
                        fenotipo[pos] = (int) (Math.random()*TY);
                    }
                    else {
                        fenotipo[pos] =(int) (Math.random()*360);
                    }
                    break;
                case GAUSSEAN:
                    for(int i = 0; i < fenotipo.length; i +=3){
                        fenotipo[i] = (int) (Math.random()*TX);
                        fenotipo[i+1] = (int) (Math.random()*TY);
                        fenotipo[i+2] = (int) (Math.random()*360);
                    }
                    break;
                default:
                    break;

            }
        }

    @Override
        void cruceMonopunto(Chromosome c1, Chromosome c2, int corte)
        {
            for(int i = 0; i < fenotipo.length; i++){
                if(i < corte) fenotipo[i] = (int)c1.getFenotipo()[i];
                else fenotipo[i] = (int)c2.getFenotipo()[i];
            }
        };
    @Override
        void cruceUniforme(Chromosome c1, Chromosome c2, float prob, float[]results)
        {
            for(int i = 0; i < fenotipo.length; i++){
                if(results[i] < prob) fenotipo[i] = (int) c1.getFenotipo()[i];
                else fenotipo[i] = (int) c2.getFenotipo()[i];
            }
        };
    @Override
        void cruceAritmetico(Chromosome c1, Chromosome c2)
        {
            for(int i = 0; i < fenotipo.length; i++){
                fenotipo[i] =  ((int)c1.getFenotipo()[i]+ (int)c2.getFenotipo()[i])/2;
            }
        };
    @Override
        void cruceBLX_Alpha(Chromosome c1, Chromosome c2, float alpha)
        {
            for(int i = 0; i < fenotipo.length; i++){
                int cmin, cmax;
                if((int)c1.getFenotipo()[i]> (int)c2.getFenotipo()[i]){
                    cmin = (int)c2.getFenotipo()[i];
                    cmax = (int)c1.getFenotipo()[i];
                }
                else {
                    cmin = (int)c1.getFenotipo()[i];
                    cmax = (int)c2.getFenotipo()[i];
                }
                int aux = cmax - cmin;
                cmin = (int)(cmin - aux * alpha);
                cmax = (int)(cmax + aux * alpha);
                fenotipo[i] = (int)(Math.random()* (cmax-cmin))+ cmin;
                fenotipo[i] = Math.max(0, Math.min(fenotipo[i], TX-1));
            }
        }

    private ChromosomeReal(ChromosomeReal other) {
        this.aptitud = other.aptitud;
        this.puntuacion = other.puntuacion;
        this.punt_acumulada = other.punt_acumulada;
        this.TX = other.TX;
        this.TY = other.TY;
        this.fenotipo = other.fenotipo.clone();
    }
    @Override
    public Chromosome copy() {
        return new ChromosomeReal(this);
    }

    ;
}
