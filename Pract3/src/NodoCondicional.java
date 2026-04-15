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
                if(!hijoIzquierdo.setNodoAtPos(pos, prof + 1, n)){
                    hijoIzquierdo = n;
                }
            }
            else {
                if(!hijoDerecho.setNodoAtPos(pos, prof + 1, n)){
                    hijoDerecho = n;
                }
            }
            return true;
        }
    }

    @Override
    public void changeNodoAtPos(int[]pos, int prof, NodoAST n){
        if(prof >= pos.length -1){
            if(pos[prof] == 0) hijoIzquierdo = n;
            else hijoDerecho = n;
        }
        else {
            if(pos[prof] == 0) hijoIzquierdo.changeNodoAtPos(pos, prof + 1, n);
            else hijoDerecho.changeNodoAtPos(pos, prof + 1, n);
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
    public NODETYPE getType() {
        return NODETYPE.CONDITIONAL;
    }

    public void setHijoD(NodoAST n){
        hijoDerecho = n;
    }
    public NodoAST getHijoD() {return  hijoDerecho;}
    public void setHijoI(NodoAST n){
        hijoIzquierdo = n;
    }
    public NodoAST getHijoI() {return  hijoIzquierdo;}

    public  int getUmbral() {return Umbral;}
    public Sensores getSensor() {return sensor;}
    public Operdor getOperador() {return operador;}
}
