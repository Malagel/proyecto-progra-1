package avancecurricular.service;

import avancecurricular.model.AsignaturaMalla;
import avancecurricular.model.Carrera;
import avancecurricular.model.Curso;
import avancecurricular.model.Estudiante;
import avancecurricular.repository.CarreraDAO;
import avancecurricular.repository.CarreraDAO.FilaCarrera;
import avancecurricular.repository.CarreraDAO.FilaAsignaturaMalla;
import avancecurricular.repository.CarreraDAO.FilaPrerrequisito;
import avancecurricular.repository.UnitOfWork;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Servicio encargado de gestionar la lógica de negocio relacionada con los programas académicos
 * y la estructuración de sus mallas curriculares.
 */
public class CarreraService {
    private final Map<String, Carrera> carreras;
    private final CarreraDAO carreraDAO;
    private final UnitOfWork unitOfWork;

    public CarreraService(CarreraDAO dao, UnitOfWork unitOfWork) {
        this.carreras = new HashMap<>();
        this.carreraDAO = dao;
        this.unitOfWork = unitOfWork;
    }

    /**
     * Hidrata el estado de la aplicación cargando los registros desde la base de datos
     * y resolviendo las relaciones en memoria (mapeo ORM manual).
     *
     * @param conn Conexión activa a la base de datos.
     * @param cursoService Servicio inyectado para buscar en memoria y asignar las referencias reales de los cursos.
     * @throws SQLException si ocurre un error de lectura durante la extracción de datos.
     */
    public void inicializar(Connection conn, CursoService cursoService) throws SQLException {
        List<FilaCarrera> filasCarrera = carreraDAO.extraerCarreras(conn);
        for (FilaCarrera fila : filasCarrera) {
            Carrera carrera = new Carrera(fila.getId(), fila.getNombre(), fila.getCreditosTotales());
            this.carreras.put(carrera.getId(), carrera);
        }

        List<FilaAsignaturaMalla> filasMalla = carreraDAO.extraerAsignaturasMalla(conn);
        for (FilaAsignaturaMalla fila : filasMalla) {
            Carrera carrera = this.carreras.get(fila.getIdCarrera());
            Curso cursoReal = cursoService.buscarPorId(fila.getIdCurso());

            if (carrera != null && cursoReal != null) {
                AsignaturaMalla asignatura = new AsignaturaMalla(cursoReal, fila.getNumeroSemestre());
                carrera.addAsignatura(asignatura);
            }
        }

        List<FilaPrerrequisito> filasPre = carreraDAO.extraerPrerrequisitos(conn);
        for (FilaPrerrequisito fila : filasPre) {
            Carrera carrera = this.carreras.get(fila.getIdCarrera());
            Curso cursoPreReal = cursoService.buscarPorId(fila.getIdCursoPrerrequisito());

            if (carrera != null && cursoPreReal != null) {
                for (AsignaturaMalla asignatura : carrera.getPlanDeEstudio()) {
                    if (asignatura.getCurso().getId().equals(fila.getIdCurso())) {
                        asignatura.addPrerrequisito(cursoPreReal);
                        break;
                    }
                }
            }
        }
    }

    /**
     * Integra un curso existente al plan de estudios de una carrera en un semestre determinado.
     *
     * @param idCarrera Identificador de la carrera.
     * @param curso     Entidad del curso a agregar.
     * @param semestre  Número del semestre en la malla.
     * @throws IllegalStateException si el curso ya forma parte de la malla de esta carrera.
     */
    public void agregarAsignaturaMalla(String idCarrera, Curso curso, int semestre) {
        Carrera carrera = buscarPorId(idCarrera);
        if (carrera == null) {
            throw new IllegalArgumentException("La carrera no existe.");
        }
        
        boolean yaExiste = carrera.getPlanDeEstudio().stream()
                .anyMatch(am -> am.getCurso().equals(curso));
        if (yaExiste) {
            throw new IllegalStateException("El curso ya pertenece a la malla de esta carrera.");
        }

        AsignaturaMalla nuevaAsignatura = new AsignaturaMalla(curso, semestre);
        carrera.addAsignatura(nuevaAsignatura);

        this.unitOfWork.registrarAccion(conn -> 
            this.carreraDAO.insertarAsignaturaMalla(idCarrera, nuevaAsignatura, conn)
        );
    }

    public void agregarPrerrequisito(String idCarrera, String idCursoDestino, Curso cursoPre) {
        Carrera carrera = buscarPorId(idCarrera);
        if (carrera == null) {
            throw new IllegalArgumentException("La carrera no existe.");
        }

        AsignaturaMalla asignaturaMalla = carrera.getPlanDeEstudio().stream()
                .filter(am -> am.getCurso().getId().equals(idCursoDestino))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("El curso destino no está en la malla de esta carrera."));

        asignaturaMalla.addPrerrequisito(cursoPre);

        this.unitOfWork.registrarAccion(conn -> 
            this.carreraDAO.insertarPrerrequisito(idCarrera, idCursoDestino, cursoPre.getId(), conn)
        );
    }

    public java.util.Collection<Carrera> obtenerTodas() {
        return java.util.Collections.unmodifiableCollection(this.carreras.values());
    }

    public Carrera buscarPorId(String id) {
        return this.carreras.get(id);
    }

    public void registrarCarrera(Carrera carrera) {
        if (this.carreras.containsKey(carrera.getId())) {
            throw new IllegalArgumentException("La carrera con ID " + carrera.getId() + " ya existe.");
        }
        
        this.carreras.put(carrera.getId(), carrera);
        this.unitOfWork.registrarAccion(conn -> this.carreraDAO.insertarCarrera(carrera, conn));
    }

    /**
     * Elimina una carrera del sistema, verificando previamente que no existan estudiantes
     * matriculados en ella para mantener la integridad referencial.
     *
     * @param id                Identificador de la carrera a eliminar.
     * @param estudianteService Servicio de estudiantes para validar la matrícula.
     * @throws IllegalStateException si existen estudiantes activos en la carrera.
     */
    public void eliminarCarrera(String id, EstudianteService estudianteService) {
        if (!this.carreras.containsKey(id)) throw new IllegalArgumentException("...");
        
        for (Estudiante est : estudianteService.obtenerTodos()) {
            if (est.getCarrera().getId().equals(id)) {
                throw new IllegalStateException("No se puede eliminar: tiene estudiantes matriculados.");
            }
        }
        this.carreras.remove(id);
        this.unitOfWork.registrarAccion(conn -> this.carreraDAO.eliminarCarrera(id, conn));
    }
}