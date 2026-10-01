package avancecurricular.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import avancecurricular.model.Profesor;

/**
 * Objeto de acceso a datos (DAO) de las tablas {@code profesores} y {@code profesor_cursos}.
 */
public class ProfesorDAO {

    /**
     * Clase contenedora (DTO) de uso interno del repositorio. 
     * Su única responsabilidad es extraer y transportar temporalmente los datos crudos 
     * de una fila de la base de datos antes de reconstruir el grafo de objetos del dominio.
     */
    public static class FilaProfesor {
        private final String rut;
        private final String nombre;

        public FilaProfesor(String rut, String nombre) {
            this.rut = rut;
            this.nombre = nombre;
        }

        public String getRut() { return rut; }
        public String getNombre() { return nombre; }
    }

    /**
     * Fila cruda de {@code profesor_cursos}: un curso que dicta un profesor.
     */
    public static class FilaProfesorCurso {
        private final String rutProfesor;
        private final String idCurso;

        public FilaProfesorCurso(String rutProfesor, String idCurso) {
            this.rutProfesor = rutProfesor;
            this.idCurso = idCurso;
        }

        public String getRutProfesor() { return rutProfesor; }
        public String getIdCurso() { return idCurso; }
    }

    /**
     * Lee todas las filas de la tabla {@code profesores}.
     *
     * @param conn Conexión abierta a la base de datos.
     * @return Lista de filas crudas; vacía si no hay profesores.
     * @throws SQLException si falla la consulta.
     */
    public List<FilaProfesor> extraerProfesores(Connection conn) throws SQLException {
        List<FilaProfesor> filas = new ArrayList<>();
        String sql = "SELECT rut, nombre FROM profesores";

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                filas.add(new FilaProfesor(
                    rs.getString("rut"),
                    rs.getString("nombre")
                ));
            }
        }
        return filas;
    }

    /**
     * Lee qué cursos dicta cada profesor.
     *
     * @param conn Conexión abierta a la base de datos.
     * @return Lista de filas crudas de {@code profesor_cursos}.
     * @throws SQLException si falla la consulta.
     */
    public List<FilaProfesorCurso> extraerCursosDictados(Connection conn) throws SQLException {
        List<FilaProfesorCurso> filas = new ArrayList<>();
        String sql = "SELECT rut_profesor, id_curso FROM profesor_cursos";

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                filas.add(new FilaProfesorCurso(
                    rs.getString("rut_profesor"),
                    rs.getString("id_curso")
                ));
            }
        }
        return filas;
    }

    /**
     * Inserta un profesor nuevo, sin cursos asignados.
     *
     * @param profesor Profesor a guardar.
     * @param conn     Conexión con la transacción abierta por {@link UnitOfWork}.
     * @throws SQLException si falla la inserción (por ejemplo, si el RUT ya existe).
     */
    public void insertarProfesor(Profesor profesor, Connection conn) throws SQLException {
        String sql = "INSERT INTO profesores (rut, nombre) VALUES (?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, profesor.getRut());
            stmt.setString(2, profesor.getNombre());
            stmt.executeUpdate();
        }
    }

    /**
     * Elimina un profesor. Sus asignaciones de cursos se borran en cascada.
     *
     * @param rut  RUT del profesor.
     * @param conn Conexión con la transacción abierta por {@link UnitOfWork}.
     * @throws SQLException si falla el borrado.
     */
    public void eliminarProfesor(String rut, Connection conn) throws SQLException {
        String sqlProfesor = "DELETE FROM profesores WHERE rut = ?";
        try (PreparedStatement stmtProfesor = conn.prepareStatement(sqlProfesor)) {
            stmtProfesor.setString(1, rut);
            stmtProfesor.executeUpdate();
        }
    }

    /**
     * Asigna un curso a la carga académica de un profesor.
     *
     * @param rutProfesor RUT del profesor.
     * @param idCurso     Curso que pasa a dictar.
     * @param conn        Conexión con la transacción abierta por {@link UnitOfWork}.
     * @throws SQLException si falla la inserción.
     */
    public void asignarCurso(String rutProfesor, String idCurso, Connection conn) throws SQLException {
        String sql = "INSERT INTO profesor_cursos (rut_profesor, id_curso) VALUES (?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, rutProfesor);
            stmt.setString(2, idCurso);
            stmt.executeUpdate();
        }
    }

    /**
     * Quita un curso de la carga académica de un profesor.
     *
     * @param rutProfesor RUT del profesor.
     * @param idCurso     Curso que deja de dictar.
     * @param conn        Conexión con la transacción abierta por {@link UnitOfWork}.
     * @throws SQLException si falla el borrado.
     */
    public void removerCurso(String rutProfesor, String idCurso, Connection conn) throws SQLException {
        String sql = "DELETE FROM profesor_cursos WHERE rut_profesor = ? AND id_curso = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, rutProfesor);
            stmt.setString(2, idCurso);
            stmt.executeUpdate();
        }
    }
}