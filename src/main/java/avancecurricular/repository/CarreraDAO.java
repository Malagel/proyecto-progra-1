package avancecurricular.repository;

import avancecurricular.model.AsignaturaMalla;
import avancecurricular.model.Carrera;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CarreraDAO {
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

    public void insertarCarrera(Carrera carrera, Connection conn) throws SQLException {
        String sql = "INSERT INTO carreras (id, nombre, creditos_totales) VALUES (?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, carrera.getId());
            stmt.setString(2, carrera.getNombre());
            stmt.setInt(3, carrera.getCreditosTotales());
            stmt.executeUpdate();
        }
    }

    public void eliminarCarrera(String idCarrera, Connection conn) throws SQLException {
        String sqlCarrera = "DELETE FROM carreras WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sqlCarrera)) {
            stmt.setString(1, idCarrera);
            stmt.executeUpdate();
        }
    }

    public void insertarAsignaturaMalla(String idCarrera, AsignaturaMalla asignatura, Connection conn) throws SQLException {
        String sql = "INSERT INTO asignaturas_malla (id_carrera, id_curso, numero_semestre) VALUES (?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, idCarrera);
            stmt.setString(2, asignatura.getCurso().getId());
            stmt.setInt(3, asignatura.getNumeroSemestre());
            stmt.executeUpdate();
        }
    }

    public void eliminarAsignaturaMalla(String idCarrera, String idCurso, Connection conn) throws SQLException {
        String sqlMalla = "DELETE FROM asignaturas_malla WHERE id_carrera = ? AND id_curso = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sqlMalla)) {
            stmt.setString(1, idCarrera);
            stmt.setString(2, idCurso);
            stmt.executeUpdate();
        }
    }

    public void insertarPrerrequisito(String idCarrera, String idCurso, String idCursoPrerrequisito, Connection conn) throws SQLException {
        String sql = "INSERT INTO prerrequisitos_malla (id_carrera, id_curso, id_curso_prerrequisito) VALUES (?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, idCarrera);
            stmt.setString(2, idCurso);
            stmt.setString(3, idCursoPrerrequisito);
            stmt.executeUpdate();
        }
    }

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