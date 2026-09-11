package avancecurricular.ui.view;

import avancecurricular.model.Estudiante;
import avancecurricular.ui.controller.ControladorEstudiante;
import java.util.Collection;

public interface VistaEstudiante extends VistaBase<ControladorEstudiante> {
    void mostrarListaEstudiantes(Collection<Estudiante> estudiantes);
    void mostrarRegistrosAcademicos(Estudiante estudiante);
}