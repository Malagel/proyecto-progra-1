package avancecurricular.ui.view;

/**
 * Contrato base para el patrón Vista (MVC/MVP). Define el comportamiento estructural común
 * para la inyección del controlador y el despliegue de retroalimentación al usuario,
 * garantizando que la lógica de negocio se mantenga desacoplada de la tecnología gráfica empleada.
 *
 * @param <C> El tipo de controlador (Controller) que gestionará y escuchará a esta vista.
 */
public interface VistaBase<C> {
    /**
     * Asocia el controlador que recibirá las acciones del usuario en esta vista.
     *
     * @param controlador Controlador del módulo.
     */
	
	void setControlador(C controlador);
    
    /**
     * Inicia y hace visible la vista actual. Puede desencadenar la carga inicial de datos.
     */
	
    void iniciar();
    
    /**
     * Muestra al usuario un mensaje de confirmación o información.
     *
     * @param mensaje Texto a mostrar.
     */
    void mostrarMensaje(String mensaje);
    
    /**
     * Muestra al usuario un mensaje de error.
     *
     * @param error Descripción del error.
     */
    void mostrarError(String error);
}