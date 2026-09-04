package avancecurricular.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import avancecurricular.model.Profesor;

public class ProfesorDAO {
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

    public void insertarProfesor(Profesor profesor, Connection conn) throws SQLException {
        String sql = "INSERT INTO profesores (rut, nombre) VALUES (?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, profesor.getRut());
            stmt.setString(2, profesor.getNombre());
            stmt.executeUpdate();
        }
    }

    public void eliminarProfesor(String rut, Connection conn) throws SQLException {
        String sqlProfesor = "DELETE FROM profesores WHERE rut = ?";
        try (PreparedStatement stmtProfesor = conn.prepareStatement(sqlProfesor)) {
            stmtProfesor.setString(1, rut);
            stmtProfesor.executeUpdate();
        }
    }

    public void asignarCurso(String rutProfesor, String idCurso, Connection conn) throws SQLException {
        String sql = "INSERT INTO profesor_cursos (rut_profesor, id_curso) VALUES (?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, rutProfesor);
            stmt.setString(2, idCurso);
            stmt.executeUpdate();
        }
    }

    public void removerCurso(String rutProfesor, String idCurso, Connection conn) throws SQLException {
        String sql = "DELETE FROM profesor_cursos WHERE rut_profesor = ? AND id_curso = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, rutProfesor);
            stmt.setString(2, idCurso);
            stmt.executeUpdate();
        }
    }
}