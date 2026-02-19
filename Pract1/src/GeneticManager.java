import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
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
    protected double MUTATION_PROBABILITY = 5;

    //estas para la grafica
     private double[] generationAverage;
     private double[] generationBest;
     private double[] absoluteBest;
     private Chromosome bestSolution;

     //Porcentaje cruce
     public float Pcruce = 60;

     //Elitismo
     public float Pelitismo;
     private int numCromosomasElite = 1;
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
                AddElitismo(i);
            }

            Chromosome[] parents = selectParents(population[i]);
            population[i+1] = crossover(parents);
            mutate(population[i+1]);

            evaluate(population[i+1]);

            if (elitism) {
                MixElite(i+1);
                eliteChromosomes = new Chromosome[numCromosomasElite];
            }

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

            case ESTOCASTICO:
                out = estocasticSlection(pop);
                break;

            case TRUNCAMIENTO:
                out = truncamentSelection(pop);
                break;

            case RESTOS:
                out = restSelection(pop);
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

        //cogemos el minimo
        double aptitudMin = 0;
        for (Chromosome c : pop)
        {
            if (c.aptitud < aptitudMin)
                aptitudMin = c.aptitud;
        }

        //ajustamos y sumamos fitness
        double[] aptitudDesplazada = new double[pop.length];
        double totalFitness = 0.0;
        for (int i = 0; i < pop.length; i++) {
            aptitudDesplazada[i] = pop[i].aptitud - aptitudMin;
            totalFitness += aptitudDesplazada[i];
        }

        // ahora seleccionamos
        for (int i = 0; i < n; i++) {

            double r = ThreadLocalRandom.current().nextDouble() * totalFitness;
            double cumulative = 0.0;

            for (int j = 0; j < pop.length; j++) {
                cumulative += aptitudDesplazada[j];

                if (cumulative >= r) {
                    selected[i] = pop[j].copy();
                    break;
                }
            }
        }

        return selected;
    }

    private Chromosome[] tournamentSelection(Chromosome[] pop) {

        int k = 2; //ESTO ES CUANTOS INDIVIDUOS SE pegan para ver cual es el mejor
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
    private Chromosome[] estocasticSlection(Chromosome[] pop)
    {
        int n = pop.length;
        Chromosome[] selected = new Chromosome[n];

        //cogemos el minimo
        double aptitudMin = 0;
        for (Chromosome c : pop)
        {
            if (c.aptitud < aptitudMin)
                aptitudMin = c.aptitud;
        }

        //ajustamos y sumamos fitness
        double[] aptitudDesplazada = new double[pop.length];
        double totalFitness = 0.0;
        for (int i = 0; i < pop.length; i++) {
            aptitudDesplazada[i] = pop[i].aptitud - aptitudMin;
            totalFitness += aptitudDesplazada[i];
        }

        // ahora seleccionamos
        double r = totalFitness / pop.length;
        double cumulative = ThreadLocalRandom.current().nextDouble() * r;
        for (int i = 0; i < n; i++) {
            boolean  stop = false;
            int j =0;
            double sum = 0.0;
            while (!stop){
                sum += aptitudDesplazada[j];
                if(sum >= cumulative) {
                    selected[i] = pop[j].copy();
                    stop = true;
                }
                j++;
            }
            cumulative += r;
        }
        return selected;
    }

    private Chromosome[] truncamentSelection(Chromosome[] pop)
    {
        int n = pop.length;
        double trunc = 0.25;
        Chromosome[] selected = new Chromosome[n];

        //organizamos los cromosomas de mayor a menor valor
        int[] organizados = new int[pop.length];
        for (int pos = 0; pos < pop.length; pos++)
        {
            int aux = pos;
            boolean stop = false;
            int j = 0;
            while (!stop){
                if(j >= aux){
                    stop = true;
                    organizados[j] = aux;
                }
                else{
                    if(pop[j].aptitud < pop[aux].aptitud){
                        int aux2 = organizados[j];
                        organizados[j] = aux;
                        aux = aux2;
                    }
                }
                j++;
            }
        }
        // ahora seleccionamos
        int numSelec = (int) (pop.length * trunc);
        int numDuplics = (int) (1/trunc);

        int k = 0;
        for(int j = 0; j < numSelec; j++){
            for(int l = 0; l < numDuplics; l++){
                selected[k] = pop[organizados[j]].copy();
                k++;
            }
        }
        //Esto para el caso raro en el que no se haya completado "selected", para evitar que haya cromosomas "null"
        while (k < selected.length){
            selected[k] = pop[0].copy();
            k++;
        }

        return selected;
    }

    private Chromosome[] restSelection(Chromosome[] pop)
    {
        int n = pop.length;
        Chromosome[] selected = new Chromosome[n];

        //cogemos el minimo
        double aptitudMin = 0;
        for (Chromosome c : pop)
        {
            if (c.aptitud < aptitudMin)
                aptitudMin = c.aptitud;
        }

        //ajustamos y sumamos fitness
        double[] aptitudDesplazada = new double[pop.length];
        double totalFitness = 0.0;
        for (int i = 0; i < pop.length; i++) {
            aptitudDesplazada[i] = pop[i].aptitud - aptitudMin;
            totalFitness += aptitudDesplazada[i];
        }

        //Metemos en "selected" aquellos cuya probabilidad multiplicada por su numero de copias sea mayor a 1
        int k = 0;
        for(int i = 0; i < n; i++){
            if((aptitudDesplazada[i]/ totalFitness) * n >= 1){
                selected[k] = pop[i].copy();
                k++;
            }
        }

        //El resto de cromosomas deben ser elegidos con otro metodo
        //No se especifica cual en la practica, asi que elegi hacerlo por ruleta
        while (k < n){
            double r = ThreadLocalRandom.current().nextDouble() * totalFitness;
            double cumulative = 0.0;

            for (int j = 0; j < pop.length; j++) {
                cumulative += aptitudDesplazada[j];

                if (cumulative >= r) {
                    selected[k] = pop[j].copy();
                    k++;
                    break;
                }
            }
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

    private void AddElitismo(int pos){
        //Primero se buscan los cromosomas de mayor valor
        int[] sustituciones = new int[numCromosomasElite];
        for(int i = 0; i < population[pos].length; i++){
            if(i < numCromosomasElite) sustituciones[i] = i;
            else {
                int aux = i;
                for (int j = 0; j < numCromosomasElite; j++){
                    if(population[pos][aux].aptitud > population[pos][sustituciones[j]].aptitud){
                        int k = sustituciones[j];
                        sustituciones[j] = aux;
                        aux = k;
                    }
                }
            }
        }
        //Luego se sustituyen
        for(int i = 0; i < numCromosomasElite; i++){
            eliteChromosomes[i] = population[pos][sustituciones[i]].copy();
        }
    }
    private void MixElite(int pos){
        //Primero se buscan los cromosomas de menor valor
        int[] sustituciones = new int[numCromosomasElite];
        for(int i = 0; i < population[pos].length; i++){
            if(i < numCromosomasElite) sustituciones[i] = i;
            else {
                int aux = i;
                for (int j = 0; j < numCromosomasElite; j++){
                    if(population[pos][aux].aptitud < population[pos][sustituciones[j]].aptitud){
                        int k = sustituciones[j];
                        sustituciones[j] = aux;
                        aux = k;
                    }
                }
            }
        }
        //Luego se sustituyen
        for(int i = 0; i < numCromosomasElite; i++){
            population[pos][sustituciones[i]] = eliteChromosomes[i].copy();
        }
    }
}
