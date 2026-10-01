package avancecurricular.repository;

import avancecurricular.model.AsignaturaMalla;
import avancecurricular.model.Carrera;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Objeto de acceso a datos (DAO) de las tablas {@code carreras}, {@code asignaturas_malla}
 * y {@code prerrequisitos_malla}. Cada tabla se lee por separado y {@code CarreraService}
 * reconstruye en memoria la malla de cada carrera con sus prerrequisitos.
 */
public class CarreraDAO {

    /**
     * Clase contenedora (DTO) de uso interno del repositorio. 
     * Su única responsabilidad es extraer y transportar temporalmente los datos crudos 
     * de una fila de la base de datos antes de reconstruir el grafo de objetos del dominio.
     */
    public static class FilaCarrera {
        private final String id;
        private final String nombre;
        private final int creditosTotales;
        
        public FilaCarrera(String id, String nombre, int creditosTotales) {
            this.id = id;
            this.nombre = nombre;
            this.creditosTotales = creditosTotales;
        }

        public String getId() { return id; }
        public String getNombre() { return nombre; }
        public int getCreditosTotales() { return creditosTotales; }
    }

    /**
     * Fila cruda de {@code asignaturas_malla}: qué curso pertenece a qué carrera
     * y en qué semestre se dicta.
     */
    public static class FilaAsignaturaMalla {
        private final String idCarrera;
        private final String idCurso;
        private final int numeroSemestre;

        public FilaAsignaturaMalla(String idCarrera, String idCurso, int numeroSemestre) {
            this.idCarrera = idCarrera;
            this.idCurso = idCurso;
            this.numeroSemestre = numeroSemestre;
        }

        public String getIdCarrera() { return idCarrera; }
        public String getIdCurso() { return idCurso; }
        public int getNumeroSemestre() { return numeroSemestre; }
    }

    /**
     * Fila cruda de {@code prerrequisitos_malla}: un curso de una carrera y uno de sus prerrequisitos.
     */
    public static class FilaPrerrequisito {
        private final String idCarrera;
        private final String idCurso;
        private final String idCursoPrerrequisito;

        public FilaPrerrequisito(String idCarrera, String idCurso, String idCursoPrerrequisito) {
            this.idCarrera = idCarrera;
            this.idCurso = idCurso;
            this.idCursoPrerrequisito = idCursoPrerrequisito;
        }

        public String getIdCarrera() { return idCarrera; }
        public String getIdCurso() { return idCurso; }
        public String getIdCursoPrerrequisito() { return idCursoPrerrequisito; }
    }

    /**
     * Lee todas las filas de la tabla {@code carreras}.
     *
     * @param conn Conexión abierta a la base de datos.
     * @return Lista de filas crudas; vacía si no hay carreras.
     * @throws SQLException si falla la consulta.
     */
    public List<FilaCarrera> extraerCarreras(Connection conn) throws SQLException {
        List<FilaCarrera> filas = new ArrayList<>();
        String sql = "SELECT id, nombre, creditos_totales FROM carreras";

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                filas.add(new FilaCarrera(
                    rs.getString("id"),
                    rs.getString("nombre"),
                    rs.getInt("creditos_totales")
                ));
            }
        }
        return filas;
    }

    /**
     * Lee todas las asignaturas de todas las mallas.
     *
     * @param conn Conexión abierta a la base de datos.
     * @return Lista de filas crudas de {@code asignaturas_malla}.
     * @throws SQLException si falla la consulta.
     */
    public List<FilaAsignaturaMalla> extraerAsignaturasMalla(Connection conn) throws SQLException {
        List<FilaAsignaturaMalla> filas = new ArrayList<>();
        String sql = "SELECT id_carrera, id_curso, numero_semestre FROM asignaturas_malla";

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                filas.add(new FilaAsignaturaMalla(
                    rs.getString("id_carrera"),
                    rs.getString("id_curso"),
                    rs.getInt("numero_semestre")
                ));
            }
        }
        return filas;
    }

    /**
     * Lee todos los prerrequisitos registrados en las mallas.
     *
     * @param conn Conexión abierta a la base de datos.
     * @return Lista de filas crudas de {@code prerrequisitos_malla}.
     * @throws SQLException si falla la consulta.
     */
    public List<FilaPrerrequisito> extraerPrerrequisitos(Connection conn) throws SQLException {
        List<FilaPrerrequisito> filas = new ArrayList<>();
        String sql = "SELECT id_carrera, id_curso, id_curso_prerrequisito FROM prerrequisitos_malla";

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                filas.add(new FilaPrerrequisito(
                    rs.getString("id_carrera"),
                    rs.getString("id_curso"),
                    rs.getString("id_curso_prerrequisito")
                ));
            }
        }
        return filas;
    }

    /**
     * Inserta una carrera nueva, sin asignaturas.
     *
     * @param carrera Carrera a guardar.
     * @param conn    Conexión con la transacción abierta por {@link UnitOfWork}.
     * @throws SQLException si falla la inserción (por ejemplo, si el ID ya existe).
     */
    public void insertarCarrera(Carrera carrera, Connection conn) throws SQLException {
        String sql = "INSERT INTO carreras (id, nombre, creditos_totales) VALUES (?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, carrera.getId());
            stmt.setString(2, carrera.getNombre());
            stmt.setInt(3, carrera.getCreditosTotales());
            stmt.executeUpdate();
        }
    }

    /**
     * Elimina una carrera. Su malla se borra en cascada ({@code ON DELETE CASCADE}),
     * pero la base de datos rechaza el borrado si tiene estudiantes ({@code ON DELETE RESTRICT}).
     *
     * @param idCarrera Identificador de la carrera.
     * @param conn      Conexión con la transacción abierta por {@link UnitOfWork}.
     * @throws SQLException si falla el borrado o la carrera tiene estudiantes.
     */
    public void eliminarCarrera(String idCarrera, Connection conn) throws SQLException {
        String sqlCarrera = "DELETE FROM carreras WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sqlCarrera)) {
            stmt.setString(1, idCarrera);
            stmt.executeUpdate();
        }
    }

    /**
     * Agrega un curso a la malla de una carrera en el semestre indicado por la asignatura.
     *
     * @param idCarrera  Carrera a la que se agrega el curso.
     * @param asignatura Asignatura con el curso y su número de semestre.
     * @param conn       Conexión con la transacción abierta por {@link UnitOfWork}.
     * @throws SQLException si falla la inserción.
     */
    public void insertarAsignaturaMalla(String idCarrera, AsignaturaMalla asignatura, Connection conn) throws SQLException {
        String sql = "INSERT INTO asignaturas_malla (id_carrera, id_curso, numero_semestre) VALUES (?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, idCarrera);
            stmt.setString(2, asignatura.getCurso().getId());
            stmt.setInt(3, asignatura.getNumeroSemestre());
            stmt.executeUpdate();
        }
    }

    /**
     * Quita un curso de la malla de una carrera. Sus prerrequisitos se borran en cascada.
     *
     * @param idCarrera Carrera dueña de la malla.
     * @param idCurso   Curso que se quita.
     * @param conn      Conexión con la transacción abierta por {@link UnitOfWork}.
     * @throws SQLException si falla el borrado.
     */
    public void eliminarAsignaturaMalla(String idCarrera, String idCurso, Connection conn) throws SQLException {
        String sqlMalla = "DELETE FROM asignaturas_malla WHERE id_carrera = ? AND id_curso = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sqlMalla)) {
            stmt.setString(1, idCarrera);
            stmt.setString(2, idCurso);
            stmt.executeUpdate();
        }
    }

    /**
     * Registra que un curso de la malla exige aprobar otro curso antes.
     *
     * @param idCarrera            Carrera dueña de la malla.
     * @param idCurso              Curso que tiene el prerrequisito.
     * @param idCursoPrerrequisito Curso que debe aprobarse antes.
     * @param conn                 Conexión con la transacción abierta por {@link UnitOfWork}.
     * @throws SQLException si falla la inserción.
     */
    public void insertarPrerrequisito(String idCarrera, String idCurso, String idCursoPrerrequisito, Connection conn) throws SQLException {
        String sql = "INSERT INTO prerrequisitos_malla (id_carrera, id_curso, id_curso_prerrequisito) VALUES (?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, idCarrera);
            stmt.setString(2, idCurso);
            stmt.setString(3, idCursoPrerrequisito);
            stmt.executeUpdate();
        }
    }

    /**
     * Elimina un prerrequisito de un curso dentro de la malla de una carrera.
     *
     * @param idCarrera            Carrera dueña de la malla.
     * @param idCurso              Curso que tenía el prerrequisito.
     * @param idCursoPrerrequisito Prerrequisito que se elimina.
     * @param conn                 Conexión con la transacción abierta por {@link UnitOfWork}.
     * @throws SQLException si falla el borrado.
     */
    public void eliminarPrerrequisito(String idCarrera, String idCurso, String idCursoPrerrequisito, Connection conn) throws SQLException {
        String sql = "DELETE FROM prerrequisitos_malla WHERE id_carrera = ? AND id_curso = ? AND id_curso_prerrequisito = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, idCarrera);
            stmt.setString(2, idCurso);
            stmt.setString(3, idCursoPrerrequisito);
            stmt.executeUpdate();
        }
    }
}