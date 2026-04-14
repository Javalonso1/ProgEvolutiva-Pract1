public class NodoAccion extends NodoAST{
    public enum Accion {AVANZAR, GIRAR_IZQ, GIRAR_DER};

    Accion tipoAccion;

    NodoAccion(){
    }
    @Override
    public void randomize(){
        tipoAccion = Accion.values()[(int)(Math.random()*3)];
    }

    @Override
    public NodoAST getNodoAtPos(int[]pos, int prof){
        return this;
    }

    @Override
    public boolean setNodoAtPos(int[]pos, int prof, NodoAST n){
        return false;
    }

    @Override
    public void changeNodoAtPos(int[]pos, int prof, NodoAST n){
        //No deberia llegar aqui en primer lugar ._.
    }

    @Override
    public int ranomizeBranch(){
        return -1;
    }

    @Override
    public void randomizeNodoFuncional(){
        randomize();
    }
    @Override
    public boolean randomizeNodoTerminal(){
        return false;
    }
}
