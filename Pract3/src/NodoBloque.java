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
    public boolean setNodoAtPos(int[]pos, int prof, NodoAST n){
        if(prof == pos.length) return false;
        else {
            if(!listaNodos.get(pos[prof]).setNodoAtPos(pos, prof + 1, n)) {
                listaNodos.set(pos[prof], n);
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
            listaNodos.get(pos[prof]).changeNodoAtPos(pos, prof+1, n);
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
        int j = 0;
        boolean found = false;
        while (j < listaNodos.size() && !found){
            found = listaNodos.get(i+j- (listaNodos.size()*(int)((i+j/listaNodos.size())))).randomizeNodoTerminal();
        }
        return found;
    }

    public void AddNodo(NodoAST n){
        listaNodos.add(n);
    }

    public List<NodoAST> GetNodos() {return  listaNodos;}
}
