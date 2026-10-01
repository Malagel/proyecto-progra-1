package avancecurricular.ui.controller;

import avancecurricular.model.Carrera;
import avancecurricular.model.Curso;
import avancecurricular.model.Estudiante;
import avancecurricular.model.RegistroAcademico;
import avancecurricular.service.CarreraService;
import avancecurricular.service.CursoService;
import avancecurricular.service.EstudianteService;
import avancecurricular.ui.view.VistaEstudiante;

/**
 * Intermediario (Controlador) que orquesta el flujo de información entre la capa de presentación (Vista)
 * y la lógica de negocio (Servicios) para el módulo correspondiente.
 */
public class ControladorEstudiante {
    private final VistaEstudiante vista;
    private final EstudianteService estudianteService;
    private final CarreraService carreraService;
    private final CursoService cursoService;

    /**
     * Crea el controlador del módulo de Estudiantes y se registra en su vista.
     *
     * @param vista             Vista (consola o ventana) que muestra los resultados.
     * @param estudianteService Servicio principal del módulo.
     * @param carreraService    Se usa para buscar la carrera al registrar un estudiante.
     * @param cursoService      Se usa para buscar los cursos al inscribir o actualizar registros.
     */
    public ControladorEstudiante(VistaEstudiante vista, EstudianteService estudianteService, CarreraService carreraService, CursoService cursoService) {
        this.vista = vista;
        this.estudianteService = estudianteService;
        this.carreraService = carreraService;
        this.cursoService = cursoService;
        this.vista.setControlador(this);
    }

    /**
     * Pide a la vista que muestre todos los estudiantes registrados.
     */
    public void onSolicitarListaEstudiantes() {
        vista.mostrarListaEstudiantes(estudianteService.obtenerTodos());
    }

    /**
     * Registra un estudiante nuevo en una carrera existente y refresca el listado.
     *
     * @param rut       RUT del estudiante (máximo 10 caracteres).
     * @param nombre    Nombre del estudiante.
     * @param idCarrera Carrera en la que se matricula.
     */
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

    /**
     * Elimina un estudiante y refresca el listado.
     *
     * @param rut RUT del estudiante.
     */
    public void onEliminarEstudiante(String rut) {
        try {
            estudianteService.eliminarEstudiante(rut);
            vista.mostrarMensaje("Estudiante eliminado con éxito.");
            onSolicitarListaEstudiantes();
        } catch (RuntimeException e) {
            vista.mostrarError(e.getMessage());
        }
    }

    /**
     * Inscribe un curso en estado CURSANDO, solo si el estudiante aprobó sus prerrequisitos.
     *
     * @param rut     RUT del estudiante.
     * @param idCurso Curso a inscribir.
     */
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

    /**
     * Cambia la nota y el estado de un curso ya inscrito por el estudiante.
     *
     * @param rut     RUT del estudiante.
     * @param idCurso Curso del registro.
     * @param nota    Nota nueva (1.0 a 7.0, salvo en estado CURSANDO).
     * @param estado  APROBADO, REPROBADO o CURSANDO.
     */
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

    /**
     * Muestra el expediente académico de un estudiante.
     *
     * @param rut RUT del estudiante.
     */
    public void onSolicitarRegistros(String rut) {
        try {
            Estudiante estudiante = estudianteService.buscarPorRut(rut);
            vista.mostrarRegistrosAcademicos(estudiante);
        } catch (RuntimeException e) {
            vista.mostrarError(e.getMessage());
        }
    }

    /**
     * Quita un curso del expediente del estudiante.
     *
     * @param rut     RUT del estudiante.
     * @param idCurso Curso a desinscribir.
     */
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