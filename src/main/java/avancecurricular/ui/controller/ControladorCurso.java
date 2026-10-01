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

/**
 * Intermediario (Controlador) que orquesta el flujo de información entre la capa de presentación (Vista)
 * y la lógica de negocio (Servicios) para el módulo correspondiente.
 */
public class ControladorCurso {
    private final VistaCurso vista;
    private final CursoService cursoService;
    private final CarreraService carreraService;
    private final EstudianteService estudianteService;
    private final ProfesorService profesorService;

    
    /**
     * Crea el controlador del módulo de Cursos y se registra en su vista.
     *
     * @param vista             Vista (consola o ventana) que muestra los resultados.
     * @param cursoService      Servicio principal del módulo.
     * @param carreraService    Se usa para validar mallas al eliminar y para el detalle del curso.
     * @param estudianteService Se usa para validar registros académicos al eliminar.
     * @param profesorService   Se usa para el detalle y para desasignar el curso al eliminarlo.
     */
    public ControladorCurso(VistaCurso vista, CursoService cursoService, CarreraService carreraService, EstudianteService estudianteService, ProfesorService profesorService) {
        this.vista = vista;
        this.cursoService = cursoService;
        this.carreraService = carreraService;
        this.estudianteService = estudianteService;
        this.profesorService = profesorService;
        this.vista.setControlador(this);
    }

    /**
     * Pide a la vista que muestre todos los cursos registrados.
     */
    public void onSolicitarListaCursos() {
        vista.mostrarListaCursos(cursoService.obtenerTodos());
    }

    /**
     * Registra un curso nuevo y refresca el listado. Si los datos son inválidos
     * o el ID ya existe, muestra el error en la vista.
     *
     * @param id       Identificador del curso.
     * @param nombre   Nombre del curso.
     * @param creditos Créditos del curso (mayor que cero).
     */
    public void onAgregarCurso(String id, String nombre, int creditos) {
        try {
            cursoService.registrarCurso(id, nombre, creditos);
            vista.mostrarMensaje("Curso '" + nombre + "' registrado con éxito.");
            onSolicitarListaCursos();
        } catch (RuntimeException e) {
            vista.mostrarError(e.getMessage());
        }
    }

    /**
     * Elimina un curso si no está en ninguna malla, no es prerrequisito y no tiene registros.
     * Si no se puede eliminar, muestra el motivo en la vista.
     *
     * @param id Identificador del curso.
     */
    public void onEliminarCurso(String id) {
        try {
            cursoService.eliminarCurso(id, carreraService, estudianteService, profesorService);
            vista.mostrarMensaje("Curso eliminado con éxito.");
            onSolicitarListaCursos();
        } catch (RuntimeException e) {
            vista.mostrarError(e.getMessage());
        }
    }

    /**
     * Muestra un curso junto con las carreras que lo incluyen y los profesores que lo dictan.
     *
     * @param id Identificador del curso.
     */
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

    /**
     * Cambia el nombre y los créditos de un curso y muestra su detalle actualizado.
     *
     * @param id             Identificador del curso a modificar.
     * @param nuevoNombre    Nombre nuevo.
     * @param nuevosCreditos Créditos nuevos (mayor que cero).
     */
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