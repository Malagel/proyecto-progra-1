package avancecurricular.ui.controller;

import avancecurricular.model.Carrera;
import avancecurricular.model.Curso;
import avancecurricular.service.CarreraService;
import avancecurricular.service.CursoService;
import avancecurricular.service.EstudianteService;
import avancecurricular.ui.view.VistaCarrera;

public class ControladorCarrera {
    private final VistaCarrera vista;
    private final CarreraService carreraService;
    private final EstudianteService estudianteService;
    private final CursoService cursoService;

    public ControladorCarrera(VistaCarrera vista, CarreraService carreraService, EstudianteService estudianteService, CursoService cursoService) {
        this.vista = vista;
        this.carreraService = carreraService;
        this.estudianteService = estudianteService;
        this.cursoService = cursoService;
        this.vista.setControlador(this);
    }

    public void onSolicitarListaCarreras() {
        vista.mostrarListaCarreras(carreraService.obtenerTodas());
    }

    public void onAgregarAsignaturaMalla(String idCarrera, String idCurso, int semestre) {
        try {
            Curso curso = cursoService.buscarPorId(idCurso);
            carreraService.agregarAsignaturaMalla(idCarrera, curso, semestre);
            vista.mostrarMensaje("Asignatura agregada a la malla exitosamente.");
            onVerDetalleMalla(idCarrera);
        } catch (RuntimeException e) {
            vista.mostrarError(e.getMessage());
        }
    }

    public void onAgregarPrerrequisito(String idCarrera, String idCursoDestino, String idCursoPre) {
        try {
            Curso cursoPre = cursoService.buscarPorId(idCursoPre);
            carreraService.agregarPrerrequisito(idCarrera, idCursoDestino, cursoPre);
            vista.mostrarMensaje("Prerrequisito agregado exitosamente.");
            onVerDetalleMalla(idCarrera);
        } catch (RuntimeException e) {
            vista.mostrarError(e.getMessage());
        }
    }

    public void onAgregarCarrera(String id, String nombre, int creditosTotales) {
        try {
            Carrera nueva = new Carrera(id, nombre, creditosTotales);
            carreraService.registrarCarrera(nueva);
            vista.mostrarMensaje("Carrera '" + nombre + "' registrada con éxito.");
            onSolicitarListaCarreras();
        } catch (RuntimeException e) {
            vista.mostrarError(e.getMessage());
        }
    }

    public void onEliminarCarrera(String id) {
        try {
            carreraService.eliminarCarrera(id, estudianteService);
            vista.mostrarMensaje("Carrera eliminada con éxito.");
            onSolicitarListaCarreras();
        } catch (RuntimeException e) {
            vista.mostrarError(e.getMessage());
        }
    }

    public void onVerDetalleMalla(String idCarrera) {
        try {
            Carrera carrera = carreraService.buscarPorId(idCarrera);
            if (carrera == null) {
                throw new IllegalArgumentException("La carrera no existe.");
            }
            vista.mostrarDetalleMalla(carrera);
        } catch (RuntimeException e) {
            vista.mostrarError(e.getMessage());
        }
    }
}