package avancecurricular.service;

import avancecurricular.exception.EntidadNoEncontradaException;
import avancecurricular.model.AsignaturaMalla;
import avancecurricular.model.Carrera;
import avancecurricular.model.Curso;
import avancecurricular.model.Estudiante;
import avancecurricular.model.Profesor;
import avancecurricular.model.RegistroAcademico;
import avancecurricular.repository.CursoDAO;
import avancecurricular.repository.CursoDAO.FilaCurso;
import avancecurricular.repository.UnitOfWork;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Servicio encargado de gestionar el ciclo de vida de los cursos y salvaguardar su
 * integridad respecto a carreras, profesores y estudiantes.
 */
public class CursoService {
    private final Map<String, Curso> cursos;
    private final CursoDAO cursoDAO;
    private final UnitOfWork unitOfWork;

    public CursoService(CursoDAO dao, UnitOfWork unitOfWork) {
        this.cursos = new HashMap<>();
        this.cursoDAO = dao;
        this.unitOfWork = unitOfWork;
    }

    public void inicializar(Connection conn) throws SQLException {
        this.cursos.clear();
        
        List<FilaCurso> filas = cursoDAO.extraerCursos(conn);
        
        for (FilaCurso fila : filas) {
            Curso curso = new Curso(fila.getId(), fila.getNombre(), fila.getCreditos());
            this.cursos.put(curso.getId(), curso);
        }
    }

    public void registrarCurso(Curso curso) {
        if (this.cursos.containsKey(curso.getId())) {
            throw new IllegalArgumentException("El curso con ID " + curso.getId() + " ya existe.");
        }

        this.cursos.put(curso.getId(), curso);
        this.unitOfWork.registrarAccion(conn -> this.cursoDAO.insertarCurso(curso, conn));
    }

    /**
     * Método de conveniencia que instancia la entidad internamente y delega su registro 
     * en el flujo principal, reduciendo el acoplamiento desde los controladores.
     *
     * @param id       El identificador único a asignar.
     * @param nombre   El nombre comercial o descriptivo.
     * @param creditos El peso académico de la entidad.
     */
    public void registrarCurso(String id, String nombre, int creditos) {
        Curso nuevoCurso = new Curso(id, nombre, creditos);
        this.registrarCurso(nuevoCurso);
    }

    /**
     * Elimina un curso del sistema de forma segura, garantizando que no esté integrado en mallas curriculares,
     * no sea prerrequisito, y no tenga estudiantes con registros en él. Si el curso es dictado por profesores,
     * se les remueve de su carga académica.
     *
     * @param id                Identificador del curso.
     * @param carreraService    Servicio para validación en mallas.
     * @param estudianteService Servicio para validación en registros académicos.
     * @param profesorService   Servicio para desasignación en cargas docentes.
     * @throws IllegalStateException si el curso incumple alguna regla de integridad que impida su eliminación.
     */
    public void eliminarCurso(String id, CarreraService carreraService, EstudianteService estudianteService, ProfesorService profesorService) {
        if (!this.cursos.containsKey(id)) {
            throw new IllegalArgumentException("El curso no existe.");
        }
        
        Curso cursoObjetivo = this.cursos.get(id);

        for (Carrera carrera : carreraService.obtenerTodas()) {
            for (AsignaturaMalla am : carrera.getPlanDeEstudio()) {
                if (am.getCurso().equals(cursoObjetivo)) {
                    throw new IllegalStateException("Violación de integridad: El curso pertenece a la malla de " + carrera.getNombre());
                }
                if (am.getPrerrequisitos().contains(cursoObjetivo)) {
                    throw new IllegalStateException("Violación de integridad: El curso es prerrequisito en la carrera " + carrera.getNombre());
                }
            }
        }

        for (Estudiante estudiante : estudianteService.obtenerTodos()) {
            for (RegistroAcademico registro : estudiante.getRegistrosAcademicos()) {
                if (registro.getCurso().equals(cursoObjetivo)) {
                    throw new IllegalStateException("Violación de integridad: El estudiante " + estudiante.getRut() + " tiene registros asociados a este curso.");
                }
            }
        }

        for (Profesor profesor : profesorService.obtenerTodos()) {
            if (profesor.getCursosDictados().contains(cursoObjetivo)) {
                profesor.removerCurso(cursoObjetivo);
            }
        }

        this.cursos.remove(id);
        this.unitOfWork.registrarAccion(conn -> this.cursoDAO.eliminarCurso(id, conn));
    }

    public void actualizarCurso(String id, String nuevoNombre, int nuevosCreditos) {
        Curso curso = buscarPorId(id);
        curso.setNombre(nuevoNombre);
        curso.setCreditos(nuevosCreditos);
        
        this.unitOfWork.registrarAccion(conn -> this.cursoDAO.actualizarCurso(curso, conn));
    }

    public Curso buscarPorId(String id) {
        Curso curso = this.cursos.get(id);
        if (curso == null) {
            throw new EntidadNoEncontradaException("No se encontró ningún curso con el ID: " + id);
        }
        return curso;
    }

    public Collection<Curso> obtenerTodos() {
        return Collections.unmodifiableCollection(this.cursos.values());
    }
}