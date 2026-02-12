import java.util.ArrayList;

//el manager para
public abstract class GeneticManager {

    private Chromosome[][] population;

     private double[] generationAverage;
     private Chromosome[] generationBest;
     private Chromosome absoluteBest;

     public GeneticManager()
     {

     }
    //esto ahora mismo es super guarro, en un futuro será chulo
    public void evolve(int n_gen, int p_size, boolean elitism)
    {
        int i = 0; //i es la generación actual
        population = new Chromosome[n_gen][p_size];
        generationAverage = new double[n_gen];
        generationBest = new Chromosome[n_gen];

        population[0] = initializePopulation(p_size);


        evaluate(population[i]);
        while (i < n_gen)   //por si queremos meter otra condición de ruptura
        {
            if (elitism) {
                //eliteChromosomes.addAll(0, this.elite.getElite(population));
            }

            Chromosome[] parents = selectParents(population[i]);
            population[i+1] = crossover(parents);
            population[i+1] = mutate(population[i+1]);

            if (elitism) {
                //this.population =this.elite.includeEliteRepWorst(this.population, this.eliteChromosomes);
                //this.eliteChromosomes.clear();
            }

            evaluate(population[i]);

            //para visualizar los datos
            getMetrics(i);
            //ya

            i++;

        }

    }


    protected abstract Chromosome[] initializePopulation(int p_size);
    protected abstract Chromosome[] crossover (Chromosome[] pop);
    protected abstract void evaluate(Chromosome[] pop);




    public Chromosome[] selectParents(Chromosome[] pop) {

        return null;
    }


    public Chromosome[] mutate (Chromosome[] pop)
    {
        return null;
    }

    private void getMetrics(int i)
    {
        //hacemos una vuelta por la population para conseguir estos dos
        double tot = 0;
        Chromosome best = population[i][0];
        for (int j = 0; j < population[i].length; i++)
        {
            if(population[i][j].aptitud > best.aptitud)
            {
                best = population[i][j];
            }

            tot+= population[i][j].aptitud;

        }
        generationAverage[i] = tot/ population[i].length;
        generationBest[i] = best;

        if(best.aptitud > absoluteBest.aptitud)
        {
            absoluteBest= best;
        }

    }
}
