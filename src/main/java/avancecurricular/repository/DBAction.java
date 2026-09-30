package avancecurricular.repository;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Interfaz funcional que define una operación de acceso a datos a ser ejecutada
 * bajo el contexto de una conexión transaccional administrada.
 */
@FunctionalInterface
public interface DBAction {
    void ejecutar(Connection conn) throws SQLException;
}