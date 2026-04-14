public abstract class NodoAST {

    public NodoAST() {}
    public abstract void randomize();
    public abstract NodoAST getNodoAtPos(int[]pos, int prof);
    public abstract boolean setNodoAtPos(int[]pos, int prof, NodoAST n);
    public abstract void changeNodoAtPos(int[]pos, int prof, NodoAST n);
    public abstract int ranomizeBranch();
    public abstract void randomizeNodoFuncional();
    public abstract boolean randomizeNodoTerminal();
}
