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
        //TO DO
    }
    @Override
    public void randomize(){
        sensor = Sensores.values()[(int)(Math.random()*4)];
        operador = Operdor.values()[(int)(Math.random()*3)];
        Umbral = UmbralValues[(int)(Math.random()*3)];
    }

    public void setHijoD(NodoAST n){
        hijoDerecho = n;
    }
    public void setHijoI(NodoAST n){
        hijoIzquierdo = n;
    }
}
