public class NodoCondicional extends NodoAST{

    public enum Sensores{DIST_MUESTRA, DIST_ARENA, DIST_OBSTACULO, NIVEL_ENERGIA};
    public enum Operdor {IGUAL, MENOR_QUE, MAYOR_QUE};

    Sensores sensor;
    Operdor operador;
    int Umbral;
    NodoAST hijoIzquierdo;
    NodoAST hijoDerecho;

    public  NodoCondicional(){
        //TO DO
    }
}
