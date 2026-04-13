public abstract class NodoAST {

    public NodoAST() {}
    public abstract void randomize();
    public abstract NodoAST getNodoAtPos(int[]pos, int prof);
    public abstract void changeNodoAtPos(int[]pos, int prof, NodoAST n);
}
