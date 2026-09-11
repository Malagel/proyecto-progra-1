package avancecurricular.ui.controller;

import avancecurricular.model.Carrera;
import avancecurricular.model.Curso;
import avancecurricular.model.Estudiante;
import avancecurricular.model.RegistroAcademico;
import avancecurricular.service.CarreraService;
import avancecurricular.service.CursoService;
import avancecurricular.service.EstudianteService;
import avancecurricular.ui.view.VistaEstudiante;

public class ControladorEstudiante {
    private final VistaEstudiante vista;
    private final EstudianteService estudianteService;
    private final CarreraService carreraService;
    private final CursoService cursoService;

    public ControladorEstudiante(VistaEstudiante vista, EstudianteService estudianteService, CarreraService carreraService, CursoService cursoService) {
        this.vista = vista;
        this.estudianteService = estudianteService;
        this.carreraService = carreraService;
        this.cursoService = cursoService;
        this.vista.setControlador(this);
    }

    public void onSolicitarListaEstudiantes() {
        vista.mostrarListaEstudiantes(estudianteService.obtenerTodos());
    }

    public void onAgregarEstudiante(String rut, String nombre, String idCarrera) {
        try {
            Carrera carrera = carreraService.buscarPorId(idCarrera);
            if (carrera == null) {
                throw new IllegalArgumentException("La carrera con ID " + idCarrera + " no existe.");
            }
            Estudiante nuevo = new Estudiante(rut, nombre, carrera);
            estudianteService.registrarEstudiante(nuevo);
            vista.mostrarMensaje("Estudiante registrado con éxito.");
            onSolicitarListaEstudiantes();
        } catch (RuntimeException e) {
            vista.mostrarError(e.getMessage());
        }
    }

    public void onEliminarEstudiante(String rut) {
        try {
            estudianteService.eliminarEstudiante(rut);
            vista.mostrarMensaje("Estudiante eliminado con éxito.");
            onSolicitarListaEstudiantes();
        } catch (RuntimeException e) {
            vista.mostrarError(e.getMessage());
        }
    }

    public void onInscribirCurso(String rut, String idCurso) {
        try {
            Curso curso = cursoService.buscarPorId(idCurso);
            estudianteService.agregarRegistro(rut, curso);
            vista.mostrarMensaje("Curso inscrito con éxito.");
            onSolicitarRegistros(rut);
        } catch (RuntimeException e) {
            vista.mostrarError(e.getMessage());
        }
    }

    public void onActualizarRegistro(String rut, String idCurso, double nota, String estado) {
        try {
            Curso curso = cursoService.buscarPorId(idCurso);
            RegistroAcademico nuevoRegistro = new RegistroAcademico(curso, nota, estado);
            estudianteService.actualizarRegistro(rut, nuevoRegistro);
            vista.mostrarMensaje("Registro actualizado con éxito.");
            onSolicitarRegistros(rut);
        } catch (RuntimeException e) {
            vista.mostrarError(e.getMessage());
        }
    }

    public void onSolicitarRegistros(String rut) {
        try {
            Estudiante estudiante = estudianteService.buscarPorRut(rut);
            vista.mostrarRegistrosAcademicos(estudiante);
        } catch (RuntimeException e) {
            vista.mostrarError(e.getMessage());
        }
    }

    public void onDesinscribirCurso(String rut, String idCurso) {
        try {
            Curso curso = cursoService.buscarPorId(idCurso);
            estudianteService.desinscribirCurso(rut, curso);
            vista.mostrarMensaje("Curso desinscrito exitosamente.");
            onSolicitarRegistros(rut);
        } catch (RuntimeException e) {
            vista.mostrarError(e.getMessage());
        }
    }
}