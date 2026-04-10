public class ChromosomeRover extends Chromosome{

    public ChromosomeRover(){

    }

    @Override
    public void setFenotipo(NodoAST f){
        fenotipo = f;
    }
    @Override
    public void initializeRandomFull(){

    }
    @Override
    public void initializeRandomGrow(){

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
