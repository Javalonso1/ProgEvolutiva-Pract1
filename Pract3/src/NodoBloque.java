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
    public NodoAST getNodoAtPos(int[]pos, int prof){
        if(prof == pos.length) return this;
        else {
            return listaNodos.get(pos[prof]).getNodoAtPos(pos, prof + 1);
        }
    }
    @Override
    public boolean setNodoAtPos(int[]pos, int prof, NodoAST n){
        if(prof == pos.length) return false;
        else {
            if(!listaNodos.get(pos[prof]).setNodoAtPos(pos, prof + 1, n.copy())) {
                listaNodos.set(pos[prof], n.copy());
            }
            return true;
        }
    }
    @Override
    public void changeNodoAtPos(int[]pos, int prof, NodoAST n){
        if(prof >= pos.length -1){
            listaNodos.set(pos[prof], n);
        }
        else {
            listaNodos.get(pos[prof]).changeNodoAtPos(pos, prof+1, n.copy());
        }
    }

    @Override
    public int ranomizeBranch(){
        return (int)(Math.random() * listaNodos.size());
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
    public NODETYPE getType() {
        return NODETYPE.BLOCK;
    }

    public void AddNodo(NodoAST n){
        listaNodos.add(n);
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
