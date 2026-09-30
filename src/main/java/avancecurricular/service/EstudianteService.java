package avancecurricular.service;

import avancecurricular.model.Curso;
import avancecurricular.model.Estudiante;
import avancecurricular.model.RegistroAcademico;
import avancecurricular.model.Carrera;
import avancecurricular.repository.EstudianteDAO;
import avancecurricular.repository.EstudianteDAO.FilaEstudiante;
import avancecurricular.repository.EstudianteDAO.FilaNota;
import avancecurricular.repository.UnitOfWork;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * Servicio centralizado para gestionar las operaciones académicas de los estudiantes,
 * como sus inscripciones, desinscripciones y calificaciones.
 */
public class EstudianteService {
    private final Map<String, Estudiante> estudiantes;
    
    private final EstudianteDAO estudianteDAO;
    private final UnitOfWork unitOfWork;

    public EstudianteService(EstudianteDAO dao, UnitOfWork unitOfWork) {
        this.estudiantes = new HashMap<>();
        this.estudianteDAO = dao;
        this.unitOfWork = unitOfWork;
    }

    /**
     * Hidrata el estado de la aplicación cargando los registros desde la base de datos
     * y resolviendo las relaciones en memoria (mapeo ORM manual).
     *
     * @param conn Conexión activa a la base de datos.
     * @param cursoService Servicio inyectado para buscar en memoria y asignar las referencias reales de los cursos.
     * @param carreraService Servicio con la misma utilidad que cursoService. 
     * @throws SQLException si ocurre un error de lectura durante la extracción de datos.
     */
    public void inicializar(Connection conn, CarreraService carreraService, CursoService cursoService) throws SQLException {
        List<FilaEstudiante> filasEst = estudianteDAO.extraerEstudiantes(conn);

        for (FilaEstudiante fila : filasEst) {
            Carrera carreraReal = carreraService.buscarPorId(fila.getIdCarrera());
            
            Estudiante estudiante = new Estudiante(fila.getRut(), fila.getNombre(), carreraReal, new HashSet<>());
            this.estudiantes.put(estudiante.getRut(), estudiante);
        }

        List<FilaNota> filasNotas = estudianteDAO.extraerNotas(conn);

        for (FilaNota fila : filasNotas) {
            Estudiante estudiante = this.estudiantes.get(fila.getRutEstudiante());
            Curso cursoReal = cursoService.buscarPorId(fila.getIdCurso());

            if (estudiante != null && cursoReal != null) {
                RegistroAcademico registro = new RegistroAcademico(cursoReal, fila.getNota(), fila.getEstado());
                estudiante.addRegistroAcademico(registro);
            }
        }
    }

    public java.util.Collection<Estudiante> obtenerTodos() {
        return java.util.Collections.unmodifiableCollection(this.estudiantes.values());
    }

    public void registrarEstudiante(Estudiante estudiante) {
        if (this.estudiantes.containsKey(estudiante.getRut()))
            throw new IllegalArgumentException("El RUT del estudiante que se intenta agregar ya existe.");

        this.estudiantes.put(estudiante.getRut(), estudiante);

        this.unitOfWork.registrarAccion(conn -> this.estudianteDAO.insertarEstudiante(estudiante, conn));
    }   

    /**
     * Elimina a un estudiante del sistema mediante su RUT.
     * <p>
     * Nota sobre integridad: Debido a las reglas de cascada en la infraestructura 
     * de la base de datos (ON DELETE CASCADE), eliminar al estudiante purgará 
     * automáticamente todo su historial académico (registros y calificaciones) 
     * asociado a su RUT.
     *
     * @param rut El RUT del estudiante a eliminar.
     */
    public void eliminarEstudiante(String rut) {
        this.estudiantes.remove(rut);

        this.unitOfWork.registrarAccion(conn -> this.estudianteDAO.eliminarEstudiante(rut, conn));
    }

    /**
     * Inscribe un curso al estudiante tras validar los prerrequisitos, registrando la acción
     * en la base de datos de manera transaccional.
     *
     * @param rut   RUT del estudiante.
     * @param curso Curso a inscribir.
     * @throws IllegalStateException si el estudiante ya se encuentra cursando la asignatura.
     */
    public void agregarRegistro(String rut, Curso curso) {
        Estudiante est = buscarPorRut(rut);
        int tamañoPrevio = est.getRegistrosAcademicos().size();
        RegistroAcademico nuevo = est.inscribirCurso(curso);
        
        if (est.getRegistrosAcademicos().size() == tamañoPrevio) {
            throw new IllegalStateException("El estudiante ya tiene inscrito el curso.");
        }
        this.unitOfWork.registrarAccion(conn -> this.estudianteDAO.insertarRegistro(rut, nuevo, conn));
    }

    public Estudiante buscarPorRut(String rut) {
        if (rut == null || rut.trim().isEmpty()) {
            throw new IllegalArgumentException("El RUT no puede ser nulo ni estar vacío.");
        }

        Estudiante est = this.estudiantes.get(rut);
        if (est == null) {
            throw new IllegalArgumentException("No se encontró ningún estudiante con el RUT: " + rut);
        }

        return est;
    }

    /**
     * Sustituye un registro académico existente (ej. cambio de estado o calificación) por uno nuevo actualizado.
     *
     * @param rut           RUT del estudiante.
     * @param nuevoRegistro El nuevo estado del registro académico.
     * @throws IllegalArgumentException si el estudiante no posee un registro previo de dicho curso.
     */
    public void actualizarRegistro(String rut, RegistroAcademico nuevoRegistro) {
        Estudiante est = buscarPorRut(rut);
        
        RegistroAcademico antiguo = est.getRegistrosAcademicos().stream()
            .filter(r -> r.getCurso().equals(nuevoRegistro.getCurso()))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("El estudiante no cursa esta asignatura."));
            
        
        est.removeRegistroAcademico(antiguo);
        est.addRegistroAcademico(nuevoRegistro);
        
        
        this.unitOfWork.registrarAccion(conn -> this.estudianteDAO.actualizarRegistro(rut, nuevoRegistro, conn));
    }

    public void desinscribirCurso(String rut, Curso curso) {
        Estudiante est = buscarPorRut(rut);
        
        RegistroAcademico registro = est.getRegistrosAcademicos().stream()
            .filter(r -> r.getCurso().equals(curso))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("El estudiante no tiene inscrito este curso."));
            
        est.removeRegistroAcademico(registro);
        
        this.unitOfWork.registrarAccion(conn -> 
            this.estudianteDAO.eliminarRegistro(rut, curso.getId(), conn)
        );
    }
}