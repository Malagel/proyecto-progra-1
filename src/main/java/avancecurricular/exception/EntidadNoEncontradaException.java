package avancecurricular.exception;

/**
 * Excepción lanzada cuando se intenta operar sobre un identificador que no existe
 * en los registros del sistema.
 */
public class EntidadNoEncontradaException extends RuntimeException {
	/**
     * Crea la excepción con un mensaje que indica qué entidad no se encontró.
     *
     * @param mensaje Descripción del error, que se muestra al usuario.
     */
    public EntidadNoEncontradaException(String mensaje) {
        super(mensaje);
    }
}