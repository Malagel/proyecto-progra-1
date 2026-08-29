package avancecurricular.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EstudianteDAO {
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

    // faltan los demas metodos para insertar... eliminar... etc. DEBEN SER USADOS DENTRO DEL SERVIC por el UoW.
}