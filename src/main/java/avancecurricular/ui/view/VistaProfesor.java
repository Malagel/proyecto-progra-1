package avancecurricular.ui.view;

import avancecurricular.model.Profesor;
import avancecurricular.ui.controller.ControladorProfesor;
import java.util.Collection;

/**
 * Contrato específico para la presentación de los datos y acciones del módulo de Profesores.
 */
public interface VistaProfesor extends VistaBase<ControladorProfesor> {
    void mostrarListaProfesores(Collection<Profesor> profesores);
    void mostrarCursosDelProfesor(Profesor profesor);
}
