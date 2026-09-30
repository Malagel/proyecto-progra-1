package avancecurricular.ui.view;

import avancecurricular.ui.controller.ControladorPrincipal;

/**
 * Contrato específico para la vista raíz o menú principal de la aplicación.
 * Aunque no define métodos adicionales a {@link VistaBase}, su existencia es estructural: 
 * tipa estrictamente el contenedor principal con el {@link ControladorPrincipal} 
 * y actúa como el punto de entrada para la navegación hacia los demás submódulos.
 */
public interface VistaPrincipal extends VistaBase<ControladorPrincipal> {

}