package avancecurricular.ui.view;

public interface VistaBase<C> {
    void setControlador(C controlador);
    
    void iniciar();
    
    void mostrarMensaje(String mensaje);
    void mostrarError(String error);
}