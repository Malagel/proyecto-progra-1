package avancecurricular.ui.controller;

import avancecurricular.ui.view.VistaPrincipal;
import avancecurricular.ui.view.VistaCurso;
import avancecurricular.ui.view.VistaCarrera;
import avancecurricular.ui.view.VistaProfesor;
import avancecurricular.ui.view.VistaEstudiante;

public class ControladorPrincipal {
    private final VistaPrincipal vista;
    private final VistaCurso vistaCurso;
    private final VistaCarrera vistaCarrera;
    private final VistaProfesor vistaProfesor;
    private final VistaEstudiante vistaEstudiante;

    public ControladorPrincipal(VistaPrincipal vista, VistaCurso vistaCurso, VistaCarrera vistaCarrera, VistaProfesor vistaProfesor, VistaEstudiante vistaEstudiante) {
        this.vista = vista;
        this.vistaCurso = vistaCurso;
        this.vistaCarrera = vistaCarrera;
        this.vistaProfesor = vistaProfesor;
        this.vistaEstudiante = vistaEstudiante;
        this.vista.setControlador(this);
    }

    public void onNavegarACursos() {
        vistaCurso.iniciar();
    }

    public void onNavegarACarreras() {
        vistaCarrera.iniciar();
    }

    public void onNavegarAProfesores() {
        vistaProfesor.iniciar();
    }

    public void onNavegarAEstudiantes() {
        vistaEstudiante.iniciar();
    }
}
