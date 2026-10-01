package avancecurricular.repository;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Interfaz funcional que define una operación de acceso a datos a ser ejecutada
 * bajo el contexto de una conexión transaccional administrada.
 */
@FunctionalInterface
public interface DBAction {
    /**
     * Ejecuta la operación usando la conexión entregada.
     *
     * @param conn Conexión con la transacción abierta por {@link UnitOfWork}.
     * @throws SQLException si la operación falla; en ese caso {@link UnitOfWork} hace rollback.
     */
	void ejecutar(Connection conn) throws SQLException;
}