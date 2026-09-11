package avancecurricular.ui.controller;

import java.util.List;
import java.util.stream.Collectors;

import avancecurricular.model.Carrera;
import avancecurricular.model.Curso;
import avancecurricular.model.Profesor;
import avancecurricular.service.CarreraService;
import avancecurricular.service.CursoService;
import avancecurricular.service.EstudianteService;
import avancecurricular.service.ProfesorService;
import avancecurricular.ui.view.VistaCurso;

public class ControladorCurso {
    private final VistaCurso vista;
    private final CursoService cursoService;
    private final CarreraService carreraService;
    private final EstudianteService estudianteService;
    private final ProfesorService profesorService;

    public ControladorCurso(VistaCurso vista, CursoService cursoService, CarreraService carreraService, EstudianteService estudianteService, ProfesorService profesorService) {
        this.vista = vista;
        this.cursoService = cursoService;
        this.carreraService = carreraService;
        this.estudianteService = estudianteService;
        this.profesorService = profesorService;
        this.vista.setControlador(this);
    }

    public void onSolicitarListaCursos() {
        vista.mostrarListaCursos(cursoService.obtenerTodos());
    }

    public void onAgregarCurso(String id, String nombre, int creditos) {
        try {
            Curso nuevo = new Curso(id, nombre, creditos);
            cursoService.registrarCurso(nuevo);
            vista.mostrarMensaje("Curso '" + nombre + "' registrado con éxito.");
            onSolicitarListaCursos();
        } catch (RuntimeException e) {
            vista.mostrarError(e.getMessage());
        }
    }

    public void onEliminarCurso(String id) {
        try {
            cursoService.eliminarCurso(id, carreraService, estudianteService, profesorService);
            vista.mostrarMensaje("Curso eliminado con éxito.");
            onSolicitarListaCursos();
        } catch (RuntimeException e) {
            vista.mostrarError(e.getMessage());
        }
    }

    public void onConsultarDetalleCurso(String id) {
        try {
            Curso curso = cursoService.buscarPorId(id);
            
            List<Carrera> carreras = carreraService.obtenerTodas().stream()
                .filter(c -> c.getPlanDeEstudio().stream().anyMatch(am -> am.getCurso().equals(curso)))
                .collect(Collectors.toList());
                
            List<Profesor> profesores = profesorService.obtenerTodos().stream()
                .filter(p -> p.getCursosDictados().contains(curso))
                .collect(Collectors.toList());
                
            vista.mostrarDetalleCurso(curso, carreras, profesores);
        } catch (RuntimeException e) {
            vista.mostrarError(e.getMessage());
        }
    }

    public void onModificarCurso(String id, String nuevoNombre, int nuevosCreditos) {
        try {
            cursoService.actualizarCurso(id, nuevoNombre, nuevosCreditos);
            vista.mostrarMensaje("Curso actualizado con éxito.");
            onConsultarDetalleCurso(id);
        } catch (RuntimeException e) {
            vista.mostrarError(e.getMessage());
        }
    }
}