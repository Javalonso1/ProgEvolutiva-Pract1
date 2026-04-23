import java.util.ArrayList;
import java.util.List;

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
    public ArrayList<NodoAST> getAllTerminalNodos(){
        return new ArrayList<NodoAST>();
    }
    @Override
    public int pickRandomSon(){
        return -1;
    }
    @Override
    public NodoAST getSon(int i){
        return  null;
    }
    @Override
    public void sustituteSon(int i, NodoAST n){

    }
    @Override
    public int numNodos(){
        return 1;
    }

    @Override
    public void randomizeNodoFuncional(){
        randomize();
    }
    @Override
    public boolean randomizeNodoTerminal(){
        return false;
    }
    @Override
    public boolean podar() {
        return false;
    }
    @Override
    public String nodoToText() {
        switch (tipoAccion){
            case AVANZAR:
                return "AV";
            case GIRAR_DER:
                return "GIR_D";
            case GIRAR_IZQ:
                return "GIR_I";
            default:
                return "";
        }
    }
    @Override
    public NODETYPE getType() {
        return NODETYPE.ACTION;
    }

    private NodoAccion(NodoAccion other) {
        this.tipoAccion = other.tipoAccion;
    }
    @Override
    public NodoAST copy() {
        return new NodoAccion(this);
    }
}
