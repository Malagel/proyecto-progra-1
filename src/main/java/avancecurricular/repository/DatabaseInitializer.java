package avancecurricular.repository;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {
    private static final String[] TABLAS_SQL = {
        
        // Entidades independientes
        "CREATE TABLE IF NOT EXISTS carreras ("
            + "id VARCHAR(10) PRIMARY KEY, "
            + "nombre VARCHAR(100) NOT NULL, "
            + "creditos_totales INT NOT NULL"
            + ");",

        "CREATE TABLE IF NOT EXISTS cursos ("
            + "id VARCHAR(10) PRIMARY KEY, "
            + "nombre VARCHAR(100) NOT NULL, "
            + "creditos INT NOT NULL"
            + ");",

        "CREATE TABLE IF NOT EXISTS profesores ("
            + "rut VARCHAR(12) PRIMARY KEY, "
            + "nombre VARCHAR(100) NOT NULL"
            + ");",

        // Entidades dependientes
        "CREATE TABLE IF NOT EXISTS estudiantes ("
            + "rut VARCHAR(12) PRIMARY KEY, "
            + "nombre VARCHAR(100) NOT NULL, "
            + "id_carrera VARCHAR(10) NOT NULL, "
            + "FOREIGN KEY (id_carrera) REFERENCES carreras(id) ON DELETE RESTRICT"
            + ");",

        // Tablas de relación
        "CREATE TABLE IF NOT EXISTS profesor_cursos ("
            + "rut_profesor VARCHAR(12) NOT NULL, "
            + "id_curso VARCHAR(10) NOT NULL, "
            + "PRIMARY KEY (rut_profesor, id_curso), "
            + "FOREIGN KEY (rut_profesor) REFERENCES profesores(rut) ON DELETE CASCADE, "
            + "FOREIGN KEY (id_curso) REFERENCES cursos(id) ON DELETE CASCADE"
            + ");",

        "CREATE TABLE IF NOT EXISTS asignaturas_malla ("
            + "id_carrera VARCHAR(10) NOT NULL, "
            + "id_curso VARCHAR(10) NOT NULL, "
            + "numero_semestre INT NOT NULL, "
            + "PRIMARY KEY (id_carrera, id_curso), "
            + "FOREIGN KEY (id_carrera) REFERENCES carreras(id) ON DELETE CASCADE, "
            + "FOREIGN KEY (id_curso) REFERENCES cursos(id) ON DELETE RESTRICT"
            + ");",

        "CREATE TABLE IF NOT EXISTS prerrequisitos_malla ("
            + "id_carrera VARCHAR(10) NOT NULL, "
            + "id_curso VARCHAR(10) NOT NULL, "
            + "id_curso_prerrequisito VARCHAR(10) NOT NULL, "
            + "PRIMARY KEY (id_carrera, id_curso, id_curso_prerrequisito), "
            + "FOREIGN KEY (id_carrera, id_curso) REFERENCES asignaturas_malla(id_carrera, id_curso) ON DELETE CASCADE, "
            + "FOREIGN KEY (id_curso_prerrequisito) REFERENCES cursos(id) ON DELETE RESTRICT"
            + ");",

        "CREATE TABLE IF NOT EXISTS registros_academicos ("
            + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
            + "rut_estudiante VARCHAR(12) NOT NULL, "
            + "id_curso VARCHAR(10) NOT NULL, "
            + "nota DECIMAL(3, 1), "
            + "estado VARCHAR(15) NOT NULL, "
            + "FOREIGN KEY (rut_estudiante) REFERENCES estudiantes(rut) ON DELETE CASCADE, "
            + "FOREIGN KEY (id_curso) REFERENCES cursos(id) ON DELETE RESTRICT"
            + ");"
    };

    public static void crearTablasSiNoExisten() throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            for (String sql : TABLAS_SQL) {
                stmt.executeUpdate(sql);
            }
        }
    }
}