package avancecurricular.repository;

import avancecurricular.model.Estudiante;
import avancecurricular.model.RegistroAcademico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Objeto de acceso a datos (DAO) de las tablas {@code estudiantes} y {@code registros_academicos}.
 */
public class EstudianteDAO {

    /**
     * Clase contenedora (DTO) de uso interno del repositorio. 
     * Su única responsabilidad es extraer y transportar temporalmente los datos crudos 
     * de una fila de la base de datos antes de reconstruir el grafo de objetos del dominio.
     */
    public static class FilaEstudiante {
        private final String rut;
        private final String nombre;
        private final String idCarrera;

        public FilaEstudiante(String rut, String nombre, String idCarrera) {
            this.rut = rut;
            this.nombre = nombre;
            this.idCarrera = idCarrera;
        }

        public String getRut() { return rut; }
        public String getNombre() { return nombre; }
        public String getIdCarrera() { return idCarrera; }
    }

    /**
     * Fila cruda de {@code registros_academicos}: nota y estado de un estudiante en un curso.
     */
    public static class FilaNota {
        private final String rutEstudiante;
        private final String idCurso;
        private final double nota;
        private final String estado;

        public FilaNota(String rutEstudiante, String idCurso, double nota, String estado) {
            this.rutEstudiante = rutEstudiante;
            this.idCurso = idCurso;
            this.nota = nota;
            this.estado = estado;
        }

        public String getRutEstudiante() { return rutEstudiante; }
        public String getIdCurso() { return idCurso; }
        public double getNota() { return nota; }
        public String getEstado() { return estado; }
    }

    /**
     * Lee todas las filas de la tabla {@code estudiantes}.
     *
     * @param conn Conexión abierta a la base de datos.
     * @return Lista de filas crudas; vacía si no hay estudiantes.
     * @throws SQLException si falla la consulta.
     */
    public List<FilaEstudiante> extraerEstudiantes(Connection conn) throws SQLException {
        List<FilaEstudiante> filas = new ArrayList<>();
        String sql = "SELECT rut, nombre, id_carrera FROM estudiantes";

        try (PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                filas.add(new FilaEstudiante(
                    rs.getString("rut"),
                    rs.getString("nombre"),
                    rs.getString("id_carrera")
                ));
            }
        }
        return filas;
    }

    /**
     * Lee todos los registros académicos de todos los estudiantes.
     *
     * @param conn Conexión abierta a la base de datos.
     * @return Lista de filas crudas de {@code registros_academicos}.
     * @throws SQLException si falla la consulta.
     */
    public List<FilaNota> extraerNotas(Connection conn) throws SQLException {
        List<FilaNota> filas = new ArrayList<>();
        String sql = "SELECT rut_estudiante, id_curso, nota, estado FROM registros_academicos";

        try (PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                filas.add(new FilaNota(
                    rs.getString("rut_estudiante"),
                    rs.getString("id_curso"),
                    rs.getDouble("nota"),
                    rs.getString("estado")
                ));
            }
        }
        return filas;
    }

    /**
     * Inserta un estudiante nuevo asociado a su carrera.
     *
     * @param estudiante Estudiante a guardar.
     * @param conn       Conexión con la transacción abierta por {@link UnitOfWork}.
     * @throws SQLException si falla la inserción (por ejemplo, si el RUT ya existe).
     */
    public void insertarEstudiante(Estudiante estudiante, Connection conn) throws SQLException {
        String sql = "INSERT INTO estudiantes (rut, nombre, id_carrera) VALUES (?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, estudiante.getRut());
            stmt.setString(2, estudiante.getNombre());
            stmt.setString(3, estudiante.getCarrera().getId());
            stmt.executeUpdate();
        }
    }

    /**
     * Actualiza el nombre y la carrera de un estudiante, buscándolo por su RUT.
     *
     * @param estudiante Estudiante con los datos ya modificados en memoria.
     * @param conn       Conexión con la transacción abierta por {@link UnitOfWork}.
     * @throws SQLException si falla la actualización.
     */
    public void actualizarEstudiante(Estudiante estudiante, Connection conn) throws SQLException {
        String sql = "UPDATE estudiantes SET nombre = ?, id_carrera = ? WHERE rut = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, estudiante.getNombre());
            stmt.setString(2, estudiante.getCarrera().getId());
            stmt.setString(3, estudiante.getRut());
            stmt.executeUpdate();
        }
    }

    /**
     * Elimina un estudiante. Sus registros académicos se borran en cascada.
     *
     * @param rut  RUT del estudiante.
     * @param conn Conexión con la transacción abierta por {@link UnitOfWork}.
     * @throws SQLException si falla el borrado.
     */
    public void eliminarEstudiante(String rut, Connection conn) throws SQLException {
        String sqlEstudiante = "DELETE FROM estudiantes WHERE rut = ?";
        try (PreparedStatement stmtEst = conn.prepareStatement(sqlEstudiante)) {
            stmtEst.setString(1, rut);
            stmtEst.executeUpdate();
        }
    }

    /**
     * Guarda un registro académico nuevo (curso, nota y estado) de un estudiante.
     *
     * @param rutEstudiante RUT del estudiante.
     * @param registro      Registro a guardar.
     * @param conn          Conexión con la transacción abierta por {@link UnitOfWork}.
     * @throws SQLException si falla la inserción.
     */
    public void insertarRegistro(String rutEstudiante, RegistroAcademico registro, Connection conn) throws SQLException {
        String sql = "INSERT INTO registros_academicos (rut_estudiante, id_curso, nota, estado) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, rutEstudiante);
            stmt.setString(2, registro.getCurso().getId());
            stmt.setDouble(3, registro.getNota());
            stmt.setString(4, registro.getEstado());
            stmt.executeUpdate();
        }
    }

    /**
     * Actualiza la nota y el estado de un registro, buscándolo por RUT del estudiante e ID del curso.
     *
     * @param rutEstudiante RUT del estudiante.
     * @param registro      Registro con la nota y el estado nuevos.
     * @param conn          Conexión con la transacción abierta por {@link UnitOfWork}.
     * @throws SQLException si falla la actualización.
     */
    public void actualizarRegistro(String rutEstudiante, RegistroAcademico registro, Connection conn) throws SQLException {
        String sql = "UPDATE registros_academicos SET nota = ?, estado = ? WHERE rut_estudiante = ? AND id_curso = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDouble(1, registro.getNota());
            stmt.setString(2, registro.getEstado());
            stmt.setString(3, rutEstudiante);
            stmt.setString(4, registro.getCurso().getId());
            stmt.executeUpdate();
        }
    }


    /**
     * Elimina el registro de un estudiante en un curso (desinscripción).
     *
     * @param rutEstudiante RUT del estudiante.
     * @param idCurso       Curso del registro.
     * @param conn          Conexión con la transacción abierta por {@link UnitOfWork}.
     * @throws SQLException si falla el borrado.
     */
    public void eliminarRegistro(String rutEstudiante, String idCurso, Connection conn) throws SQLException {
        String sql = "DELETE FROM registros_academicos WHERE rut_estudiante = ? AND id_curso = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, rutEstudiante);
            stmt.setString(2, idCurso);
            stmt.executeUpdate();
        }
    }
}