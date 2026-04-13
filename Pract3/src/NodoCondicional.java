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
