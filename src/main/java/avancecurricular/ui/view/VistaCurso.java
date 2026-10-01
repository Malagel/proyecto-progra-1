package avancecurricular.ui.view;

import avancecurricular.model.Carrera;
import avancecurricular.model.Curso;
import avancecurricular.model.Profesor;
import avancecurricular.ui.controller.ControladorCurso;
import java.util.Collection;

/**
 * Contrato específico para la presentación de los datos y acciones del módulo de Cursos.
 */
public interface VistaCurso extends VistaBase<ControladorCurso> {
    /**
     * Muestra el listado de cursos registrados.
     *
     * @param cursos Cursos a mostrar.
     */
	void mostrarListaCursos(Collection<Curso> cursos);

    /**
     * Muestra el detalle de un curso: en qué carreras está y qué profesores lo dictan.
     *
     * @param curso                  Curso consultado.
     * @param carrerasDondeSeImparte Carreras cuya malla incluye el curso.
     * @param profesoresQueLoDictan  Profesores que tienen asignado el curso.
     */
	void mostrarDetalleCurso(Curso curso, Collection<Carrera> carrerasDondeSeImparte, Collection<Profesor> profesoresQueLoDictan);
}