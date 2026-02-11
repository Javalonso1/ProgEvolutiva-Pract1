public class ChromosomeBinario extends Chromosome{
    public ChromosomeBinario(int size){
        data = new float[size];
    }

    public void initializeRandom(){
        for(int i = 0; i < data.length; i++){
            data[i] = (int)(Math.random() * 2);
        }
    }

    public float[] getData(){
        return data;
    }
}
