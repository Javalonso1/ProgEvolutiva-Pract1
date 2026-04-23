import java.util.ArrayList;
import java.util.List;

public abstract class NodoAST {

    public enum NODETYPE{ACTION, BLOCK, CONDITIONAL}
    public NodoAST() {}
    public abstract void randomize();
    public abstract void randomizeNodoFuncional();
    public abstract boolean randomizeNodoTerminal();
    public abstract ArrayList<NodoAST> getAllTerminalNodos();
    public abstract int pickRandomSon();
    public abstract NodoAST getSon(int i);
    public abstract void sustituteSon(int i, NodoAST n);
    public abstract boolean podar();
    public abstract int numNodos();
    public abstract String nodoToText();
    public abstract NODETYPE getType();
    public abstract NodoAST copy();
}
