import java.util.ArrayList;
import java.util.Random;

public class NodoCondicional extends NodoAST{

    public enum Sensores{DIST_MUESTRA, DIST_ARENA, DIST_OBSTACULO, NIVEL_ENERGIA};
    public enum Operdor {IGUAL, MENOR_QUE, MAYOR_QUE};
    protected int[] UmbralValues = {10, 50, 100};

    Sensores sensor;
    Operdor operador;
    int Umbral;
    NodoAST hijoIzquierdo;
    NodoAST hijoDerecho;

    public  NodoCondicional(){
    }
    @Override
    public void randomize(){
        sensor = Sensores.values()[(int)(Math.random()*4)];
        operador = Operdor.values()[(int)(Math.random()*3)];
        Umbral = UmbralValues[(int)(Math.random()*3)];
    }

    @Override
    public NodoAST getNodoAtPos(int[]pos, int prof){
        if(prof == pos.length) return this;
        else {
            if(pos[prof] == 0) return hijoIzquierdo.getNodoAtPos(pos, prof + 1);
            else  return hijoDerecho.getNodoAtPos(pos, prof + 1);
        }
    }

    @Override
    public boolean setNodoAtPos(int[]pos, int prof, NodoAST n){
        if(prof == pos.length) return false;
        else {
            if(pos[prof] == 0) {
                if(!hijoIzquierdo.setNodoAtPos(pos, prof + 1, n.copy())){
                    hijoIzquierdo = n.copy();
                }
            }
            else {
                if(!hijoDerecho.setNodoAtPos(pos, prof + 1, n.copy())){
                    hijoDerecho = n.copy();
                }
            }
            return true;
        }
    }

    @Override
    public void changeNodoAtPos(int[]pos, int prof, NodoAST n){
        if(prof >= pos.length -1){
            if(pos[prof] == 0) hijoIzquierdo = n.copy();
            else hijoDerecho = n.copy();
        }
        else {
            if(pos[prof] == 0) hijoIzquierdo.changeNodoAtPos(pos, prof + 1, n.copy());
            else hijoDerecho.changeNodoAtPos(pos, prof + 1, n.copy());
        }
    }

    @Override
    public int ranomizeBranch(){
        return (int)(Math.random() * 2);
    }

    @Override
    public void randomizeNodoFuncional(){
        if((int)(Math.random() * 2) == 0) hijoIzquierdo.randomizeNodoFuncional();
        else hijoDerecho.randomizeNodoFuncional();
    }

    @Override
    public boolean randomizeNodoTerminal(){
        if((int)(Math.random() * 2) == 0){
            randomize();
            return true;
        }
        else {
            int i = (int)(Math.random() * 2);
            if(i == 0){
                boolean a = hijoIzquierdo.randomizeNodoTerminal();
                if(!a) a = hijoDerecho.randomizeNodoTerminal();
                if(!a) randomize();
                return true;
            }
            else {
                boolean a = hijoDerecho.randomizeNodoTerminal();
                if(!a) a = hijoIzquierdo.randomizeNodoTerminal();
                if(!a) randomize();
                return true;
            }
        }
    }

    @Override
    public boolean podar() {
        if ((int) (Math.random() * 2) == 0) {
            if ((int) (Math.random() * 2) == 0) {
                hijoIzquierdo = new NodoAccion();
                hijoIzquierdo.randomize();
            } else {
                hijoDerecho = new NodoAccion();
                hijoDerecho.randomize();
            }
            return true;
        } else {
            int i = (int) (Math.random() * 2);
            if (i == 0) {
                if (!hijoIzquierdo.podar()) {
                    hijoIzquierdo = new NodoAccion();
                    hijoIzquierdo.randomize();
                }
                return true;
            } else {
                if (!hijoDerecho.podar()) {
                    hijoDerecho = new NodoAccion();
                    hijoDerecho.randomize();
                }
                return true;
            }
        }
    }

    @Override
    public int numNodos(){
        int a = 1;
        a += hijoDerecho.numNodos();
        a += hijoIzquierdo.numNodos();
        return a;
    }
    @Override
    public String nodoToText() {
        String s1 = "";
        switch (sensor){
            case DIST_MUESTRA:
                s1 = "D_M";
                break;
            case DIST_ARENA:
                s1 = "D_A";
                break;
            case NIVEL_ENERGIA:
                s1 = "N_E";
                break;
            case DIST_OBSTACULO:
                s1 = "D_O";
                break;
            default:
                s1 = "";
        }
        String s2 = "";
        switch (operador){
            case IGUAL:
                s2 = "=";
                break;
            case MENOR_QUE:
                s2 = "<";
                break;
            case MAYOR_QUE:
                s2 = ">";
                break;
            default:
                s2 = "";
                break;
        }
        return ("If-"+ s1 + s2 + Umbral + "(" + hijoIzquierdo.nodoToText() + ")else(" + hijoDerecho.nodoToText() + ")");
    }
    @Override
    public NODETYPE getType() {
        return NODETYPE.CONDITIONAL;
    }

    public void setHijoD(NodoAST n){
        hijoDerecho = n.copy();
    }
    public NodoAST getHijoD() {return  hijoDerecho;}
    public void setHijoI(NodoAST n){
        hijoIzquierdo = n.copy();
    }
    public NodoAST getHijoI() {return  hijoIzquierdo;}

    public  int getUmbral() {return Umbral;}
    public Sensores getSensor() {return sensor;}
    public Operdor getOperador() {return operador;}

    private NodoCondicional(NodoCondicional other) {
        this.Umbral = other.Umbral;
        this.operador = other.operador;
        this.sensor = other.sensor;
        this.hijoDerecho = other.hijoDerecho.copy();
        this.hijoIzquierdo = other.hijoIzquierdo.copy();
    }
    @Override
    public NodoAST copy() {
        return new NodoCondicional(this);
    }
}
