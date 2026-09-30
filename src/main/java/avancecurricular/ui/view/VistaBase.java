package avancecurricular.ui.view;

/**
 * Contrato base para el patrón Vista (MVC/MVP). Define el comportamiento estructural común
 * para la inyección del controlador y el despliegue de retroalimentación al usuario,
 * garantizando que la lógica de negocio se mantenga desacoplada de la tecnología gráfica empleada.
 *
 * @param <C> El tipo de controlador (Controller) que gestionará y escuchará a esta vista.
 */
public interface VistaBase<C> {
    void setControlador(C controlador);
    
    /**
     * Inicia y hace visible la vista actual. Puede desencadenar la carga inicial de datos.
     */
    void iniciar();
    
    void mostrarMensaje(String mensaje);
    void mostrarError(String error);
}