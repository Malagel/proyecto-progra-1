package avancecurricular.ui.view;

import avancecurricular.model.Profesor;
import avancecurricular.ui.controller.ControladorProfesor;
import java.util.Collection;

/**
 * Contrato específico para la presentación de los datos y acciones del módulo de Profesores.
 */
public interface VistaProfesor extends VistaBase<ControladorProfesor> {
    
    /**
     * Muestra el listado de profesores registrados.
     *
     * @param profesores Profesores a mostrar.
     */
	void mostrarListaProfesores(Collection<Profesor> profesores);
    
    /**
     * Muestra la carga académica (cursos dictados) de un profesor.
     *
     * @param profesor Profesor consultado.
     */
	void mostrarCursosDelProfesor(Profesor profesor);
}
