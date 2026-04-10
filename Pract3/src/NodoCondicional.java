public class NodoCondicional extends NodoAST{
    public enum Sensores{DIST_MUESTRA, DIST_ARENA, DIST_OBSTACULO, NIVEL_ENERGIA};

    Sensores sensor;
    int Umbral;
    NodoAST hijoIzquierdo;
    NodoAST hijoDerecho;

    public  NodoCondicional(){
        //TO DO
    }

    @Override
    public  void ejecutar(){
        //TO DO
    }
}
