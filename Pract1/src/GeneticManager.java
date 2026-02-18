import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

//el manager para
public abstract class GeneticManager {

    private Chromosome[][] population;

    public enum SELECTION_METHOD {RULETA, TORNEOS, ESTOCASTICO, TRUNCAMIENTO, RESTOS}
    public enum CROSS_METHOD {MONOPUNTO, UNIFORME, ARITMETICO, BLXALPHA}
    public enum MUTATION_TYPE {UNIFORM, GAUSSEAN}

    protected CROSS_METHOD crossMethod;
    protected SELECTION_METHOD selectionMethod;
    protected  MUTATION_TYPE mutationType;
    protected double MUTATION_PROBABILITY;

    //estas para la grafica
     private double[] generationAverage;
     private double[] generationBest;
     private double[] absoluteBest;
     private Chromosome bestSolution;

     //Porcentaje cruce
     public float Pcruce;

     public float Pelitismo;
     private int numCromosomasElite;
     private Chromosome[] eliteChromosomes;

     private UIclass.GraphPanel graph;

     public GeneticManager(UIclass.GraphPanel g)
     {
         graph = g;
         Pcruce = 0.6f;
     }
    //esto ahora mismo es super guarro, en un futuro será chulo
    public void evolve(int n_gen, int p_size, boolean elitism, CROSS_METHOD cm, SELECTION_METHOD sm, MUTATION_TYPE mt)
    {
        this.crossMethod = cm;
        this.selectionMethod = sm;
        this.mutationType = mt;

        int i = 0; //i es la generación actual
        population = new Chromosome[n_gen][p_size];
        generationAverage = new double[n_gen];
        generationBest = new double[n_gen];
        absoluteBest = new double[n_gen];

        population[0] = initializePopulation(p_size);

        setElitismoPorcentaje(0.02f);

        evaluate(population[0]);
        while (i < n_gen-1)   //por si queremos meter otra condición de ruptura
        {
            if (elitism) {
                //eliteChromosomes.addAll(0, this.elite.getElite(population));
            }

            Chromosome[] parents = selectParents(population[i]);
            population[i+1] = crossover(parents);
            mutate(population[i+1]);

            if (elitism) {
                //this.population =this.elite.includeEliteRepWorst(this.population, this.eliteChromosomes);
                //this.eliteChromosomes.clear();
            }

            evaluate(population[i+1]);

            //para visualizar los datos
            getMetrics(i+1);
            graph.setData(generationBest, absoluteBest, generationAverage);
            //ya

            i++;

        }

        System.out.println(Arrays.toString(generationAverage));
        System.out.println(Arrays.toString(generationBest));
        System.out.println(Arrays.toString(absoluteBest));

    }


    protected abstract Chromosome[] initializePopulation(int p_size);
    protected abstract Chromosome[] crossover (Chromosome[] pop);
    protected abstract void evaluate(Chromosome[] pop);




    protected Chromosome[] selectParents(Chromosome[] pop) {

        Chromosome[] out = null;
        switch (selectionMethod)
        {
            case RULETA:
                out = ruletteSlection(pop);
                break;

            case TORNEOS:
                out = tournamentSelection(pop);
                break;

            default:
                break;
        }
        return out;
    }

    private Chromosome[] ruletteSlection(Chromosome[] pop)
    {
        int n = pop.length;
        Chromosome[] selected = new Chromosome[n];

        double totalFitness = 0.0;
        for (Chromosome c : pop) {
            totalFitness += c.aptitud;
        }

        // ahora seleccionamos
        for (int i = 0; i < n; i++) {

            double r = ThreadLocalRandom.current().nextDouble() * totalFitness;
            double cumulative = 0.0;

            for (Chromosome c : pop) {
                cumulative += c.aptitud;

                if (cumulative >= r) {
                    selected[i] = c.copy();
                    break;
                }
            }
        }

        return selected;
    }

    private Chromosome[] tournamentSelection(Chromosome[] pop) {

        int k = 7; //ESTO ES CUANTOS INDIVIDUOS SE pegan para ver cual es el mejor
        int n = pop.length;
        Chromosome[] selected = new Chromosome[n];

        for (int i = 0; i < n; i++) {

            Chromosome best = null;

            for (int j = 0; j < k; j++) {

                int randomIndex = ThreadLocalRandom.current().nextInt(n);

                if (best == null || pop[randomIndex].aptitud > best.aptitud) {
                    best = pop[randomIndex];
                }
            }

            selected[i] = best.copy();
        }

        return selected;
    }

    public void mutate (Chromosome[] pop)
    {
        for (Chromosome c : pop)
        {
            c.mutate(mutationType, MUTATION_PROBABILITY);
        }
    }

    private void getMetrics(int i)
    {
        //hacemos una vuelta por la population para conseguir estos dos
        double tot = 0;
        Chromosome best = population[i][0];
        for (int j = 0; j < population[i].length; j++)
        {
            if(population[i][j].aptitud > best.aptitud)
            {
                best = population[i][j];
            }

            tot+= population[i][j].aptitud;

        }
        generationAverage[i] = tot/ population[i].length;
        generationBest[i] = best.aptitud;

        if(i == 0 ||best.aptitud > absoluteBest[i-1])
        {
            absoluteBest[i]= best.aptitud;
            bestSolution = best;
        }
        else
        {
            absoluteBest[i] = absoluteBest[i-1];
        }

    }

    public void setElitismoPorcentaje(float p){
        Pelitismo = p;
        numCromosomasElite = (int)(p * population[0].length);
        eliteChromosomes = new Chromosome[numCromosomasElite];
    }

    public Chromosome getBestSolution()
    {
        return bestSolution;
    }
}
