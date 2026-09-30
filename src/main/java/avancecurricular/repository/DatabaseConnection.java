package avancecurricular.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:sqlite:sistema_academico.db?foreign_keys=on";  

    private DatabaseConnection() {}

    /**
     * Establece la conexión principal con la base de datos SQLite.
     * <p>
     * NOTA CRÍTICA: Se ejecuta {@code PRAGMA foreign_keys = ON} obligatoriamente, 
     * ya que SQLite deshabilita la integridad referencial por defecto. Sin esta instrucción, 
     * los borrados en cascada (ON DELETE CASCADE/RESTRICT) fallarán silenciosamente.
     *
     * @return Una conexión transaccional lista para operar.
     * @throws SQLException si el archivo de la base de datos no es accesible.
     */
    public static Connection getConnection() throws SQLException {
        Connection conn = DriverManager.getConnection(URL);
        
        try (java.sql.Statement stmt = conn.createStatement()) {
            stmt.execute("PRAGMA foreign_keys = ON;");
        }
        return conn;
    }
}