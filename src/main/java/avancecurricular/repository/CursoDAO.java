package avancecurricular.repository;

import avancecurricular.model.Curso;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
/**
 * Objeto de acceso a datos (DAO) de la tabla {@code cursos}.
 * Lee y escribe cursos en SQLite usando la conexión que le entrega
 * {@code CursoService} al iniciar o el {@link UnitOfWork} al guardar.
 */
public class CursoDAO {

    /**
     * Clase contenedora (DTO) de uso interno del repositorio. 
     * Su única responsabilidad es extraer y transportar temporalmente los datos crudos 
     * de una fila de la base de datos antes de reconstruir el grafo de objetos del dominio.
     */
    public static class FilaCurso {
        private final String id;
        private final String nombre;
        private final int creditos;


        public FilaCurso(String id, String nombre, int creditos) {
            this.id = id;
            this.nombre = nombre;
            this.creditos = creditos;
        }

        public String getId() { return id; }
        public String getNombre() { return nombre; }
        public int getCreditos() { return creditos; }
    }

    /**
     * Lee todas las filas de la tabla {@code cursos}.
     *
     * @param conn Conexión abierta a la base de datos.
     * @return Lista de filas crudas; vacía si la tabla no tiene registros.
     * @throws SQLException si falla la consulta.
     */
    public List<FilaCurso> extraerCursos(Connection conn) throws SQLException {
        List<FilaCurso> filas = new ArrayList<>();
        String sql = "SELECT id, nombre, creditos FROM cursos";

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                filas.add(new FilaCurso(
                    rs.getString("id"),
                    rs.getString("nombre"),
                    rs.getInt("creditos")
                ));
            }
        }
        return filas;
    }

    /**
     * Inserta un curso nuevo en la tabla {@code cursos}.
     *
     * @param curso Curso a guardar.
     * @param conn  Conexión con la transacción abierta por {@link UnitOfWork}.
     * @throws SQLException si falla la inserción (por ejemplo, si el ID ya existe).
     */
    public void insertarCurso(Curso curso, Connection conn) throws SQLException {
        String sql = "INSERT INTO cursos (id, nombre, creditos) VALUES (?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, curso.getId());
            stmt.setString(2, curso.getNombre());
            stmt.setInt(3, curso.getCreditos());
            stmt.executeUpdate();
        }
    }

    /**
     * Elimina el curso con el ID indicado. Las reglas de integridad (mallas,
     * prerrequisitos y registros) se validan antes en {@code CursoService}.
     *
     * @param id   Identificador del curso a eliminar.
     * @param conn Conexión con la transacción abierta por {@link UnitOfWork}.
     * @throws SQLException si falla el borrado.
     */
    public void eliminarCurso(String id, Connection conn) throws SQLException {
        String sql = "DELETE FROM cursos WHERE id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            stmt.executeUpdate();
        }
    }

    /**
     * Actualiza el nombre y los créditos de un curso existente, buscándolo por su ID.
     *
     * @param curso Curso con los datos ya modificados en memoria.
     * @param conn  Conexión con la transacción abierta por {@link UnitOfWork}.
     * @throws SQLException si falla la actualización.
     */
    public void actualizarCurso(Curso curso, Connection conn) throws SQLException {
        String sql = "UPDATE cursos SET nombre = ?, creditos = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, curso.getNombre());
            stmt.setInt(2, curso.getCreditos());
            stmt.setString(3, curso.getId());
            stmt.executeUpdate();
        }
    }
}