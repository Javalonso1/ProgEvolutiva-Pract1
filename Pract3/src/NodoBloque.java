import java.util.List;

public class NodoBloque extends NodoAST{

    List<NodoAST> listaNodos;

    public  NodoBloque(){
        //TO DO
    }
    @Override
    public void randomize(){
        //No hace nada
    }

    public void AddNodo(NodoAST n){
        listaNodos.add(n);
    }
}
