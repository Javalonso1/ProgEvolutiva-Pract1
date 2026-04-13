import java.util.List;

public class NodoBloque extends NodoAST{

    List<NodoAST> listaNodos;

    public  NodoBloque(){
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
    public void changeNodoAtPos(int[]pos, int prof, NodoAST n){
        if(prof >= pos.length -1){
            listaNodos.set(pos[prof], n);
        }
        else {
            listaNodos.get(pos[prof]).changeNodoAtPos(pos, prof+1, n);
        }
    }

    public void AddNodo(NodoAST n){
        listaNodos.add(n);
    }

    public List<NodoAST> GetNodos() {return  listaNodos;}
}
