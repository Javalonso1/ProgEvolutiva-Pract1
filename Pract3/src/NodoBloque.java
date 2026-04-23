import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class NodoBloque extends NodoAST{

    ArrayList<NodoAST> listaNodos;

    public  NodoBloque(){
        listaNodos = new ArrayList<>();
    }
    @Override
    public void randomize(){
        //No hace nada
    }

    @Override
    public void randomizeNodoFuncional(){
        listaNodos.get((int)(Math.random() * listaNodos.size())).randomizeNodoFuncional();
    }
    @Override
    public boolean randomizeNodoTerminal(){
        int i = (int)(Math.random() * listaNodos.size());
        return listaNodos.get(i).randomizeNodoTerminal();
    }
    @Override
    public ArrayList<NodoAST> getAllTerminalNodos(){
        ArrayList<NodoAST> arr = new ArrayList<NodoAST>();
        arr.add(this);
        for(int j = 0; j < listaNodos.size(); j++){
            ArrayList<NodoAST> aux = listaNodos.get(j).getAllTerminalNodos();
            for (int i = 0; i < aux.size(); i++) arr.add(aux.get(i));
        }
        return arr;
    }
    @Override
    public int pickRandomSon(){
        return (int)(Math.random()*listaNodos.size());
    }
    @Override
    public NodoAST getSon(int i){
        return listaNodos.get(i);
    }
    @Override
    public void sustituteSon(int i, NodoAST n){
        listaNodos.set(i, n.copy());
    }

    @Override
    public boolean podar(){
        if((int)(Math.random() * 2) == 0){
            NodoAccion n = new NodoAccion();
            n.randomize();
            listaNodos.set((int)(Math.random() * listaNodos.size()), n);
            return true;
        }
        else {
            int i = (int)(Math.random() * listaNodos.size());
            if(!listaNodos.get(i).podar()) {
                NodoAccion n = new NodoAccion();
                n.randomize();
                listaNodos.set(i, n);
            }
            return true;
        }
    }

    @Override
    public int numNodos(){
        int a = 1;
        for(int i = 0; i < listaNodos.size(); i++){
            a += listaNodos.get(i).numNodos();
        }
        return a;
    }

    @Override
    public String nodoToText() {
        String s = "NodBloq";
        for(int i = 0; i < listaNodos.size(); i++){
            s += "(" + listaNodos.get(i).nodoToText() + ")";
        }
        return s;
    }
    @Override
    public NODETYPE getType() {
        return NODETYPE.BLOCK;
    }

    public void AddNodo(NodoAST n){
        listaNodos.add(n.copy());
    }

    public ArrayList<NodoAST> GetNodos() {return  listaNodos;}

    private NodoBloque(NodoBloque other) {
        this.listaNodos = new ArrayList<>();
        for (NodoAST n : other.listaNodos)
        {
            this.listaNodos.add(n.copy());
        }
    }
    @Override
    public NodoAST copy() {
        return new NodoBloque(this);
    }
}
