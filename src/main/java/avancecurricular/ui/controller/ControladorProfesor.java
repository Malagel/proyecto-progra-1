package avancecurricular.ui.controller;

import avancecurricular.model.Curso;
import avancecurricular.model.Profesor;
import avancecurricular.service.CursoService;
import avancecurricular.service.ProfesorService;
import avancecurricular.ui.view.VistaProfesor;

public class ControladorProfesor {
    private final VistaProfesor vista;
    private final ProfesorService profesorService;
    private final CursoService cursoService;

    public ControladorProfesor(VistaProfesor vista, ProfesorService profesorService, CursoService cursoService) {
        this.vista = vista;
        this.profesorService = profesorService;
        this.cursoService = cursoService;
        this.vista.setControlador(this);
    }

    public void onSolicitarListaProfesores() {
        vista.mostrarListaProfesores(profesorService.obtenerTodos());
    }

    public void onAgregarProfesor(String rut, String nombre) {
        try {
            Profesor nuevo = new Profesor(rut, nombre);
            profesorService.registrarProfesor(nuevo);
            vista.mostrarMensaje("Profesor registrado con éxito.");
            onSolicitarListaProfesores();
        } catch (RuntimeException e) {
            vista.mostrarError(e.getMessage());
        }
    }

    public void onEliminarProfesor(String rut) {
        try {
            profesorService.eliminarProfesor(rut);
            vista.mostrarMensaje("Profesor eliminado con éxito.");
            onSolicitarListaProfesores();
        } catch (RuntimeException e) {
            vista.mostrarError(e.getMessage());
        }
    }

    public void onAsignarCurso(String rut, String idCurso) {
        try {
            Curso curso = cursoService.buscarPorId(idCurso);
            profesorService.asignarCursoAProfesor(rut, curso);
            vista.mostrarMensaje("Curso asignado con éxito.");
            onSolicitarCursosProfesor(rut);
        } catch (RuntimeException e) {
            vista.mostrarError(e.getMessage());
        }
    }

    public void onRemoverCurso(String rut, String idCurso) {
        try {
            Curso curso = cursoService.buscarPorId(idCurso);
            profesorService.removerCursoDeProfesor(rut, curso);
            vista.mostrarMensaje("Curso removido con éxito.");
            onSolicitarCursosProfesor(rut);
        } catch (RuntimeException e) {
            vista.mostrarError(e.getMessage());
        }
    }

    public void onSolicitarCursosProfesor(String rut) {
        try {
            Profesor profesor = profesorService.buscarPorRut(rut);
            vista.mostrarCursosDelProfesor(profesor);
        } catch (RuntimeException e) {
            vista.mostrarError(e.getMessage());
        }
    }
}