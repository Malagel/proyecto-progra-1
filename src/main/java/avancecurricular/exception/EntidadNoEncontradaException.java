package avancecurricular.exception;

/**
 * Excepción lanzada cuando se intenta operar sobre un identificador que no existe
 * en los registros del sistema.
 */
public class EntidadNoEncontradaException extends RuntimeException {
    public EntidadNoEncontradaException(String mensaje) {
        super(mensaje);
    }
}