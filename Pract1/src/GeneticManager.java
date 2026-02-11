import java.util.ArrayList;

//el manager para
public class GeneticManager {

    private Chromosome[][] population;

     private int[] generationAverage;
     private Chromosome[] generationBest;
     private Chromosome[] absoluteBest;

     public GeneticManager()
     {

     }
    //esto ahora mismo es super guarro, en un futuro será chulo
    public void evolve(int n_gen, int p_size, boolean elitism)
    {
        int i = 0; //i es la generación actual
        population = new Chromosome[n_gen][p_size];
        generationAverage = new int[n_gen];
        generationBest = new Chromosome[n_gen];
        absoluteBest = new Chromosome[n_gen];

        population[0] = initializePopulation(p_size);


        evaluateBinaryCameraPopulation(population[i]);
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

            evaluateBinaryCameraPopulation(population[i]);

            //para visualizar los datos. Igual sustituir por getMetrics.
            this.generationAverage[i] = getGenerationAvg();
            this.generationBest[i] = getGenerationBest();
            this.absoluteBest[i] = getAbsoluteBest();
            //ya

            i++;

        }

    }


    Chromosome[] initializePopulation(int p_size)
    {
        return null;
    }

    void evaluateBinaryCameraPopulation(Chromosome[] pop)
    {

    }

    public Chromosome[] selectParents(Chromosome[] pop) {

        return null;
    }

    public Chromosome[] crossover (Chromosome[] pop)
    {

        return null;
    }

    public Chromosome[] mutate (Chromosome[] pop)
    {

        return null;
    }

    int getGenerationAvg()
    {
        return 0;
    }

    Chromosome getGenerationBest()
    {
        return null;
    }

    Chromosome getAbsoluteBest()
    {
        return null;

    }
}
