import java.util.*;

public class RoverEvolver extends GeneticManager{

    private int profundidadMax; //Profunidad maxima de los arboles
    private boolean initFull;   //Si la inicializacion random es Full o Grow

    public RoverEvolver(UIclass g, int prof, boolean _inFull)
    {
        super(g);
        profundidadMax = prof;
        initFull = _inFull;
    }
    @Override
    protected Chromosome[] initializePopulation(int p_size){
        Chromosome[] ini_pop = new ChromosomeRover[p_size];
        for ( int i = 0; i < p_size; i++)
        {
            ini_pop[i] = new ChromosomeRover(profundidadMax);
            ini_pop[i].initializeRandom(initFull);
        }
        return  ini_pop;
    }

    @Override
    protected Chromosome[] crossover(Chromosome[] pop){
        Chromosome[] sol = new Chromosome[pop.length];
        for(int i = 0; i < pop.length; i += 2){
            if(Pcruce <= Math.random()){
                sol[i] = new ChromosomeRover(profundidadMax);
                sol[i+1] = new ChromosomeRover(profundidadMax);
                switch (crossMethod){
                    case SUBARBOL:
                        int[] path1 = new int[0];
                        NodoAST n1;
                        n1 = sol[i].fenotipo;
                        boolean stop = false;
                        while (!stop){
                            int a = n1.ranomizeBranch();
                            if(a == -1){
                                stop = true;
                            }
                            else {
                                int[] pathAux = new int[path1.length +1];
                                for(int j = 0; j < path1.length; j++){
                                    pathAux[j] = path1[j];
                                }
                                pathAux[path1.length] = a;
                                path1 = pathAux;
                                n1 = sol[i].fenotipo.getNodoAtPos(path1, 0);

                                if(Math.random() * 2 == 0) stop = true;
                            }
                        }
                        int[] path2 = new  int[0];
                        NodoAST n2;
                        n2 = sol[i].fenotipo;
                        stop = false;
                        while (!stop){
                            int a = n2.ranomizeBranch();
                            if(a == -1){
                                stop = true;
                            }
                            else {
                                int[] pathAux = new int[path2.length +1];
                                for(int j = 0; j < path2.length; j++){
                                    pathAux[j] = path2[j];
                                }
                                pathAux[path2.length] = a;
                                path2 = pathAux;
                                n2 = sol[i].fenotipo.getNodoAtPos(path2, 0);

                                if(Math.random() * 2 == 0) stop = true;
                            }
                        }
                        sol[i].cruceSUBARBOL(path1, n2);
                        sol[i+1].cruceSUBARBOL(path2, n1);
                        break;
                    default:
                        break;
                }
            }
            else {
                sol[i] = pop[i].copy();
                sol[i+1] =pop[i+1].copy();
            }
        }
        return sol;
    }

    @Override
    protected void evaluate(Chromosome[] pop) {
        //TO DO
        for (Chromosome c: pop)
        {

        }
    }

    public void showSolution(Chromosome c) {
        //TO DO
    }
}
