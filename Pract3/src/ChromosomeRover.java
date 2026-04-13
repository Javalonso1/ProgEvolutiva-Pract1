public class ChromosomeRover extends Chromosome{

    protected  int MAX_NODOS_BLOQUE = 5;
    private int PROFUNDIDAD_MAXIMA;

    public ChromosomeRover(int profMax){
        PROFUNDIDAD_MAXIMA = profMax;
    }

    @Override
    public void setFenotipo(NodoAST f){
        fenotipo = f;
    }
    @Override
    public void initializeRandom(boolean full){
        if(full) initializeRandomFull(0);
        else initializeRandomGrow(0);
    }
    private NodoAST initializeRandomFull(int prof){
        if(prof >= PROFUNDIDAD_MAXIMA){
            NodoAccion a = new NodoAccion();
            a.randomize();
            return a;
        }
        else {
            int rnd = (int)(Math.random() * 2);
            if(rnd == 0){
                NodoBloque b = new NodoBloque();
                rnd = (int)(Math.random() * (MAX_NODOS_BLOQUE-2)) +2;
                for(int i = 0; i < rnd; i++){
                    b.AddNodo(initializeRandomFull(prof + 1));
                }
                return b;
            }
            else {
                NodoCondicional c = new NodoCondicional();
                c.randomize();
                c.setHijoD(initializeRandomFull(prof + 1));
                c.setHijoI(initializeRandomFull(prof + 1));
                return c;
            }
        }
    }
    private NodoAST initializeRandomGrow(int prof){
        if(prof >= PROFUNDIDAD_MAXIMA){
            NodoAccion a = new NodoAccion();
            a.randomize();
            return a;
        }
        else {
            int rnd = (int)(Math.random() * 3);
            if(rnd == 0){
                NodoBloque b = new NodoBloque();
                rnd = (int)(Math.random() * (MAX_NODOS_BLOQUE-2)) +2;
                for(int i = 0; i < rnd; i++){
                    b.AddNodo(initializeRandomGrow(prof + 1));
                }
                return b;
            }
            else if(rnd == 1) {
                NodoCondicional c = new NodoCondicional();
                c.randomize();
                c.setHijoD(initializeRandomGrow(prof + 1));
                c.setHijoI(initializeRandomGrow(prof + 1));
                return c;
            }
            else {
                NodoAccion a = new NodoAccion();
                a.randomize();
                return a;
            }
        }
    }

    @Override
    void cruceSUBARBOL(int[]pos, NodoAST n){
        fenotipo.changeNodoAtPos(pos, 0, n);
    }

    @Override
    void mutate(GeneticManager.MUTATION_TYPE t, double mutationP){

    }
    private ChromosomeRover(ChromosomeRover other) {
        this.aptitud = other.aptitud;
        this.puntuacion = other.puntuacion;
        this.punt_acumulada = other.punt_acumulada;
        this.fenotipo = other.fenotipo;
    }
    @Override
    public Chromosome copy() {
        return new ChromosomeRover(this);
    }
}
