package avancecurricular.service;

import avancecurricular.model.Curso;
import avancecurricular.model.Profesor;
import avancecurricular.repository.ProfesorDAO;
import avancecurricular.repository.ProfesorDAO.FilaProfesor;
import avancecurricular.repository.ProfesorDAO.FilaProfesorCurso;
import avancecurricular.repository.UnitOfWork;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Servicio encargado de la administración de la plana docente y sus asignaturas dictadas.
 */
public class ProfesorService {

    private final Map<String, Profesor> profesores;
    private final ProfesorDAO profesorDAO;
    private final UnitOfWork unitOfWork;

    public ProfesorService(ProfesorDAO dao, UnitOfWork unitOfWork) {
        this.profesores = new HashMap<>();
        this.profesorDAO = dao;
        this.unitOfWork = unitOfWork;
    }
    /**
     * Hidrata el estado de la aplicación cargando los registros desde la base de datos
     * y resolviendo las relaciones en memoria (mapeo ORM manual).
     *
     * @param conn         Conexión activa a la base de datos.
     * @param cursoService Servicio inyectado para buscar en memoria y asignar las referencias reales de los cursos.
     * @throws SQLException si ocurre un error de lectura durante la extracción de datos.
     */
    public void inicializar(Connection conn, CursoService cursoService) throws SQLException {
        this.profesores.clear();

        List<FilaProfesor> filasProfesores = profesorDAO.extraerProfesores(conn);
        for (FilaProfesor fila : filasProfesores) {
            Profesor profesor = new Profesor(fila.getRut(), fila.getNombre());
            this.profesores.put(profesor.getRut(), profesor);
        }

        List<FilaProfesorCurso> filasCursos = profesorDAO.extraerCursosDictados(conn);
        for (FilaProfesorCurso fila : filasCursos) {
            Profesor profesor = this.profesores.get(fila.getRutProfesor());
            Curso cursoReal = cursoService.buscarPorId(fila.getIdCurso());

            if (profesor != null && cursoReal != null) {
                profesor.asignarCurso(cursoReal);
            }
        }
    }

    public Profesor buscarPorRut(String rut) {
        if (rut == null || rut.trim().isEmpty()) {
            throw new IllegalArgumentException("El RUT no puede estar vacío.");
        }
        
        Profesor profesor = this.profesores.get(rut);
        if (profesor == null) {
            throw new IllegalArgumentException("No se encontró ningún profesor con el RUT: " + rut);
        }
        return profesor;
    }

    public Collection<Profesor> obtenerTodos() {
        return Collections.unmodifiableCollection(this.profesores.values());
    }

    public void registrarProfesor(Profesor profesor) {
        if (this.profesores.containsKey(profesor.getRut())) {
            throw new IllegalArgumentException("El profesor con RUT " + profesor.getRut() + " ya existe.");
        }

        this.profesores.put(profesor.getRut(), profesor);
        this.unitOfWork.registrarAccion(conn -> this.profesorDAO.insertarProfesor(profesor, conn));
    }

    /**
     * Método de conveniencia que instancia la entidad internamente y delega su registro 
     * en el flujo principal, reduciendo el acoplamiento desde los controladores.
     *
     * @param rut       El identificador único a asignar.
     * @param nombre    El nombre personal  .
     */
    public void registrarProfesor(String rut, String nombre) {
        Profesor nuevoProfesor = new Profesor(rut, nombre);
        this.registrarProfesor(nuevoProfesor);
    }

    public void eliminarProfesor(String rut) {
        Profesor profesor = buscarPorRut(rut);
        
        this.profesores.remove(profesor.getRut());
        this.unitOfWork.registrarAccion(conn -> this.profesorDAO.eliminarProfesor(rut, conn));
    }

    public void asignarCursoAProfesor(String rut, Curso curso) {
        Profesor profesor = buscarPorRut(rut);

        if (profesor.getCursosDictados().contains(curso)) {
            throw new IllegalStateException("El profesor ya dicta el curso: " + curso.getNombre());
        }

        profesor.asignarCurso(curso);
        this.unitOfWork.registrarAccion(conn -> this.profesorDAO.asignarCurso(rut, curso.getId(), conn));
    }

    public void removerCursoDeProfesor(String rut, Curso curso) {
        Profesor profesor = buscarPorRut(rut);

        if (!profesor.getCursosDictados().contains(curso)) {
            throw new IllegalStateException("El profesor no dicta el curso: " + curso.getNombre());
        }

        profesor.removerCurso(curso);
        this.unitOfWork.registrarAccion(conn -> this.profesorDAO.removerCurso(rut, curso.getId(), conn));
    }
}