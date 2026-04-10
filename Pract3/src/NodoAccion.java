public class NodoAccion extends NodoAST{
    public enum Accion {AVANZAR, GIRAR_IZQ, GIRAR_DER};

    Accion tipoAccion;

    NodoAccion(){
        //TO DO
    }
    @Override
    public void randomize(){
        tipoAccion = Accion.values()[(int)(Math.random()*3)];
    }
}
