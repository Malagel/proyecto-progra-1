package avancecurricular.ui.view;

import avancecurricular.model.Estudiante;
import avancecurricular.ui.controller.ControladorEstudiante;
import java.util.Collection;

/**
 * Contrato específico para la presentación de los datos y acciones del módulo de Estudiantes.
 */
public interface VistaEstudiante extends VistaBase<ControladorEstudiante> {
    /**
     * Muestra el listado de estudiantes registrados.
     *
     * @param estudiantes Estudiantes a mostrar.
     */
	void mostrarListaEstudiantes(Collection<Estudiante> estudiantes);
    /**
     * Muestra el expediente de un estudiante: sus registros, créditos aprobados y avance.
     *
     * @param estudiante Estudiante consultado.
     */
	void mostrarRegistrosAcademicos(Estudiante estudiante);
}