import java.util.Random;

public class ChromosomeDron extends Chromosome<Float, Integer>{

    protected int MAX_INSERCIONES = 3;
    protected int MAX_PERMUTACIONES_HEURISTICA = 4;

    protected  DronEvolver DronEv;
    public ChromosomeDron(int NumCams, int numDrones, DronEvolver mDronEv){
        fenotipo = new Integer[NumCams + (numDrones-1)];
        DronEv = mDronEv;
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
        //El Cromosoma de drones no usa el genotipo
    }

    public void initializeRandom(){
        //Primero se colocan todos los numeros
        for(int i = 0; i < fenotipo.length; i++){
            fenotipo[i] = i + 1;
        }
        //Luego se barajan
        for(int i = 0; i <fenotipo.length; i++){
            int rnd1 = (int)(Math.random() * fenotipo.length);
            int rnd2 = (int)(Math.random() * fenotipo.length);
            if(rnd1 != rnd2){
                int aux = fenotipo[rnd1];
                fenotipo[rnd1] = fenotipo[rnd2];
                fenotipo[rnd2] = aux;
            }
        }
    }

    void calculateFenotipo(){
        //Solo se usan los fenotipos, por lo que no hace falta calcular nada en base del genotipo
    }

    @Override
    void mutate(GeneticManager.MUTATION_TYPE t, double mutationP) {
        switch (t){
            case INSERCION:
                double r = Math.random() * 100;
                if (r < mutationP){
                    r = (Math.random() * MAX_INSERCIONES)+1;
                    for (int i = 0; i < r; i++){
                        int pos1 = (int)(Math.random() * fenotipo.length);
                        int pos2 = (int)(Math.random() * fenotipo.length);
                        if(pos1 != pos2){
                            int aux = fenotipo[pos1];
                            int j = pos1;
                            if(pos1 > pos2){
                                while(j > pos2){
                                    fenotipo[j] = fenotipo[j-1];
                                    j--;
                                }
                                fenotipo[pos2] = aux;
                            }
                            else {
                                while(j < pos2){
                                    fenotipo[j] = fenotipo[j+1];
                                    j++;
                                }
                                fenotipo[pos2] = aux;
                            }
                        }
                    }
                }
                break;
            case INTERCAMBIO:
                r = Math.random() * 100;
                if (r < mutationP){
                    int pos1 = (int)(Math.random() * fenotipo.length);
                    int pos2 = (int)(Math.random() * fenotipo.length);
                    if(pos1 != pos2){
                        int aux = fenotipo[pos1];
                        fenotipo[pos1] = fenotipo[pos2];
                        fenotipo[pos2] = aux;
                    }
                }
                break;
            case INVERSION:
                r = Math.random() * 100;
                if (r < mutationP){
                    int pos1 = (int)(Math.random() * fenotipo.length);
                    int pos2 = (int)(Math.random() * fenotipo.length);
                    if(pos1 != pos2){
                        if(pos1 > pos2){
                            int aux = pos1;
                            pos1 = pos2;
                            pos2 = aux;
                        }
                        while (pos1 < pos2){
                            int aux = fenotipo[pos1];
                            fenotipo[pos1] = fenotipo[pos2];
                            fenotipo[pos2] = aux;
                            pos2--;
                            pos1++;
                        }
                    }
                }
                break;
            case HEURISTICA:
                r = Math.random() * 100;
                if (r < mutationP){
                    int n = (int)(Math.random() * (MAX_PERMUTACIONES_HEURISTICA - 1)) + 2;

                    int[] pos = new int[n];
                    for(int i = 0; i < n; i++){
                        pos[i] = (int)(Math.random() *fenotipo.length);
                        boolean stop = false;
                        int j = i - 1;
                        while (j >= 0 && !stop){
                            if(pos[i] == pos[j]){
                                i--;
                                stop = true;
                            }
                            j--;
                        }
                    }
                    int realval[] = new int[n];
                    for(int i = 0; i < n; i++) realval[i] = fenotipo[pos[i]];

                    int[][]permutaciones;
                    permutaciones = createPermutaciones(realval);

                    //Ahora que tenemos las permutaciones tocara evaluar todas las posibilidades

                    Chromosome[] drons = new ChromosomeDron[permutaciones.length];
                    for(int i = 0; i < permutaciones.length; i++){
                        drons[i] = copy();
                        for(int j = 0; j < permutaciones[0].length; j++){
                            drons[i].fenotipo[pos[j]] = permutaciones[i][j];
                        }
                    }
                    DronEv.evaluate(drons);
                    int sol = 0;
                    for(int i = 1; i < drons.length; i++){
                        if(drons[i].aptitud < drons[sol].aptitud) sol = i;
                    }
                    fenotipo = (Integer[]) drons[sol].fenotipo.clone();
                }
                break;
            case CUSTOM:
                int n = (int)(Math.random() * fenotipo.length);
                for(int i = 0; i < n; i++){
                    int p1 = (int)(Math.random() * fenotipo.length);
                    int p2 = (int)(Math.random() * fenotipo.length);
                    if(p1 != p2){
                        int aux = fenotipo[p1];
                        fenotipo[p1] = fenotipo[p2];
                        fenotipo[p2] = aux;
                    }
                }
                break;
            default:
                break;
        }
    }


    private int[][] createPermutaciones(int[] perm){
        int size = 1;
        for(int i = 2; i <= perm.length; i++) size = size * i;
        int[][] sol = new  int[size][perm.length];
        if(size == 2){
            sol[0][0] = perm[0];
            sol[0][1] = perm[1];

            sol[1][0] = perm[1];
            sol[1][1] = perm[0];

        }
        else {
            int[] perm2 = new  int[perm.length -1];
            for(int i = 1; i < perm.length; i++) perm2[i-1] = perm[i];
            int[][] aux2 = createPermutaciones(perm2);
            int l = 0;
            for(int pos = 0; pos < perm.length; pos++){
                for(int i = 0; i < aux2.length; i++){
                    for(int k = 0; k < perm.length; k++){
                        if(k == pos) sol[l][k] = perm[0];
                        else{
                            if(k > pos){
                                sol[l][k] = aux2[i][k-1];
                            }
                            else {
                                sol[l][k] = aux2[i][k];
                            }
                        }
                    }
                    l++;
                }
            }
        }

        return sol;
    }
    @Override
    void crucePMX(Chromosome c1, Chromosome c2, int corte1, int corte2)
    {
        for(int i = 0; i < c1.fenotipo.length; i++){
            if(i>= corte1 && i<=corte2){
                fenotipo[i] = (Integer) c2.fenotipo[i];
            }
            else {
                int sol = (int)c1.fenotipo[i];
                int j = corte1;
                while (j <= corte2){
                    if(sol == (int)c2.fenotipo[j]){
                        sol = (int)c1.fenotipo[j];
                        j = corte1;
                    }
                    else j++;
                }
                fenotipo[i] = sol;
            }
        }
        int j = 0;
    };
    @Override
    void cruceOX(Chromosome c1, Chromosome c2, int corte1, int corte2)
    {
        for(int i = corte1; i <= corte2; i++){
            fenotipo[i] = (Integer)c2.getFenotipo()[i];
        }
        int k = corte2+1;
        if(k == fenotipo.length) k =0;
        for(int i = corte2+1; i < fenotipo.length; i++){
            int l = corte1;
            boolean stop = false;
            while (l <= corte2 &&!stop){
                if((int)c1.getFenotipo()[k] == (int)c2.getFenotipo()[l]) stop = true;
                l++;
            }
            if(stop) i--;
            else fenotipo[i] = (int)c1.getFenotipo()[k];
            k++;
            if(k>= fenotipo.length) k = 0;
        }
        for(int i = 0; i < corte1; i++){
            int l = corte1;
            boolean stop = false;
            while (l <= corte2 &&!stop){
                if((int)c1.getFenotipo()[k] == (int)c2.getFenotipo()[l]) stop = true;
                l++;
            }
            if(stop) i--;
            else fenotipo[i] = (int)c1.getFenotipo()[k];
            k++;
            if(k>= fenotipo.length) k = 0;
        }
    };
    @Override
    void cruceOXPP(Chromosome c1, Chromosome c2, int[]pos)
    {
        int k = 0;
        for(int i = 0; i < pos.length; i++){
            fenotipo[i] = (Integer)c2.getFenotipo()[pos[i]];
            if(pos[i]+1 > k) k = pos[i]+1;
        }
        int iniPos = k;
        if(k == fenotipo.length) k =0;
        for(int i = iniPos; i < fenotipo.length; i++){
            int l = 0;
            boolean stop = false;
            while (l < pos.length &&!stop){
                if((int)c1.getFenotipo()[k] == (int)c2.getFenotipo()[pos[l]]) stop = true;
                l++;
            }
            if(stop) i--;
            else fenotipo[i] = (int)c1.getFenotipo()[k];
            k++;
            if(k>= fenotipo.length) k = 0;
        }
        for(int i = 0; i < iniPos; i++){
            if(fenotipo[i] == null){
                int l = 0;
                boolean stop = false;
                while (l < pos.length &&!stop){
                    if((int)c1.getFenotipo()[k] == (int)c2.getFenotipo()[pos[l]]) stop = true;
                    l++;
                }
                if(stop) i--;
                else fenotipo[i] = (int)c1.getFenotipo()[k];
                k++;
                if(k>= fenotipo.length) k = 0;
            }
        }
    }
    @Override
    void cruceCX(Chromosome c1, Chromosome c2)
    {
        int ini = (int)c1.fenotipo[0];
        fenotipo[0] = (int)c1.fenotipo[0];
        int a = (int)c2.fenotipo[0];
        while (a != ini){
            int k = 0;
            while ((int)c1.fenotipo[k] != a) k++;
            fenotipo[k] = (int)c1.fenotipo[k];
            a = (int)c2.fenotipo[k];
        }
        for(int i = 0; i < fenotipo.length; i++) if(fenotipo[i] == null) fenotipo[i] = (int)c2.fenotipo[i];
    }
    @Override
    void cruceCO(Chromosome c1, Chromosome c2, int corte)
    {
        int[] I1 =new int[fenotipo.length];
        int[] aux =new int[fenotipo.length];
        for(int i = 0; i < aux.length; i++) aux[i] = i+1;

        for(int i = 0; i < fenotipo.length; i++){
            int k = 0;
            boolean found = false;
            while (!found){
                if(aux[k] == (int)c1.fenotipo[i]) found = true;
                else k++;
            }
            I1[i] = k + 1;
            int[] aux2 = new int[aux.length -1];
            for(int j = 0; j < aux.length; j++){
                if(j < k) aux2[j] = aux[j];
                else if(j>k) aux2[j-1] = aux[j];
            }
            aux = aux2;
        }

        int[] I2 =new int[fenotipo.length];
        aux =new int[fenotipo.length];
        for(int i = 0; i < aux.length; i++) aux[i] = i+1;

        for(int i = 0; i < fenotipo.length; i++){
            int k = 0;
            boolean found = false;
            while (!found){
                if(aux[k] == (int)c2.fenotipo[i]) found = true;
                else k++;
            }
            I2[i] = k + 1;
            int[] aux2 = new int[aux.length -1];
            for(int j = 0; j < aux.length; j++){
                if(j < k) aux2[j] = aux[j];
                else if(j>k) aux2[j-1] = aux[j];
            }
            aux = aux2;
        }

        aux = new int[fenotipo.length];
        for(int i =0; i< fenotipo.length; i++) {
            aux[i] = (int)c1.fenotipo[i];
            if(i >= corte) I1[i] = I2[i];
        }

        for(int i = 0; i < fenotipo.length; i++){
            fenotipo[i] = aux[I1[i] -1];

            int[] aux2 = new int[aux.length -1];
            for(int j = 0; j < aux.length; j++){
                if(j < I1[i] -1) aux2[j] = aux[j];
                else if(j>I1[i] -1) aux2[j-1] = aux[j];
            }
            aux = aux2;
        }
    }
    @Override
    void cruceERX(Chromosome c1, Chromosome c2)
    {
        int[][] rutas = new int[fenotipo.length][4];
        for (int i = 0; i < fenotipo.length; i++) {
            for (int j = 0; j < 4; j++) {
                rutas[i][j] = -1;
            }
        }

        for (int i = 0; i < fenotipo.length; i++) {
            int gen = (int) c1.fenotipo[i];

            int izq1 = (int) c1.fenotipo[(i - 1 + fenotipo.length) % fenotipo.length];
            int der1 = (int) c1.fenotipo[(i + 1) % fenotipo.length];

            int pos2 = 0;
            while ((int) c2.fenotipo[pos2] != gen) pos2++;

            int izq2 = (int) c2.fenotipo[(pos2 - 1 + fenotipo.length) % fenotipo.length];
            int der2 = (int) c2.fenotipo[(pos2 + 1) % fenotipo.length];

            int idx = 0;
            int[] vecinos = {izq1, der1, izq2, der2};
            for (int v : vecinos) {
                boolean existe = false;
                for (int k = 0; k < idx; k++) {
                    if (rutas[gen - 1][k] == v) {
                        existe = true;
                        break;
                    }
                }
                if (!existe) {
                    rutas[gen - 1][idx++] = v;
                }
            }
        }
        boolean[] usados = new boolean[fenotipo.length];
        int actual = (int) c2.fenotipo[0];
        fenotipo[0] = actual;
        usados[actual - 1] = true;

        for (int pos = 1; pos < fenotipo.length; pos++) {
            for (int i = 0; i < fenotipo.length; i++) {
                for (int j = 0; j < 4; j++) {
                    if (rutas[i][j] == actual) {
                        rutas[i][j] = -1;
                    }
                }
            }

            int[] vecinos = rutas[actual - 1];
            int mejor = -1;
            int minGrado = Integer.MAX_VALUE;

            for (int v : vecinos) {
                if (v != -1 && !usados[v - 1]) {

                    int grado = 0;
                    for (int x : rutas[v - 1]) {
                        if (x != -1) grado++;
                    }

                    if (grado < minGrado) {
                        minGrado = grado;
                        mejor = v;
                    }
                }
            }
            
            if (mejor == -1) {
                int rnd;
                do {
                    rnd = (int) (Math.random() * fenotipo.length) + 1;
                } while (usados[rnd - 1]);
                mejor = rnd;
            }

            fenotipo[pos] = mejor;
            usados[mejor - 1] = true;
            actual = mejor;
        }
    }
    @Override
    void cruceCustom(Chromosome c1, Chromosome c2){
        boolean[] escogidos = new boolean[fenotipo.length];
        for(int i = 0; i < fenotipo.length; i++){
            if(i%2 == 0){
                fenotipo[i] = (int)c1.fenotipo[i];
                escogidos[fenotipo[i] -1] = true;
            }
            else {
                fenotipo[i] = (int)c2.fenotipo[i];
                escogidos[fenotipo[i] -1] = true;
            }
        }
        for(int i = 0; i < fenotipo.length; i++){
            int j = 0;
            boolean error = false;
            while (!error && j < i){
                if(fenotipo[i] == fenotipo[j]) error = true;
                j++;
            }
            if(error){
                j = 0;
                while (error){
                    if(!escogidos[j]) {
                        escogidos[j] = true;
                        fenotipo[i] = j+1;
                        error = false;
                    }
                    j++;
                }
            }
        }
    }
    private ChromosomeDron(ChromosomeDron other) {
        this.aptitud = other.aptitud;
        this.puntuacion = other.puntuacion;
        this.punt_acumulada = other.punt_acumulada;
        this.fenotipo = other.fenotipo.clone();
        this.DronEv = other.DronEv;
    }
    @Override
    public Chromosome copy() {
        return new ChromosomeDron(this);
    }

}
