package avancecurricular.repository;

import avancecurricular.model.Curso;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CursoDAO {
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

    public void insertarCurso(Curso curso, Connection conn) throws SQLException {
        String sql = "INSERT INTO cursos (id, nombre, creditos) VALUES (?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, curso.getId());
            stmt.setString(2, curso.getNombre());
            stmt.setInt(3, curso.getCreditos());
            stmt.executeUpdate();
        }
    }

    public void eliminarCurso(String id, Connection conn) throws SQLException {
        String sql = "DELETE FROM cursos WHERE id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            stmt.executeUpdate();
        }
    }
}