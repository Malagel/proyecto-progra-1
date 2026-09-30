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
    void mostrarListaCursos(Collection<Curso> cursos);

    void mostrarDetalleCurso(Curso curso, Collection<Carrera> carrerasDondeSeImparte, Collection<Profesor> profesoresQueLoDictan);
}