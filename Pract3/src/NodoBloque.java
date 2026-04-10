import java.util.List;

public class NodoBloque extends NodoAST{

    List<NodoAST> listaNodos;

    public  NodoBloque(){
        //TO DO
    }

    @Override
    public  void ejecutar(){
        for (NodoAST n: listaNodos)
        {
            n.ejecutar();
        }
    }
}
