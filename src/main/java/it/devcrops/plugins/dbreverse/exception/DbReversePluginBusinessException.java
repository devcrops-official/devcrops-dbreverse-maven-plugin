package it.devcrops.plugins.dbreverse.exception;

/**
 * @author giacomo
 */
public class DbReversePluginBusinessException extends Exception {

    /**
     * Costruttore per eccezione Business custom senza parametri
     */
    public DbReversePluginBusinessException() {
        super();
    }

    /**
     * Costruttore per eccezione Business custom
     * 
     * @param cause eccezione tirata
     */
    public DbReversePluginBusinessException(Throwable cause) {
        super(cause);
    }

    /**
     * Costruttore per eccezione Business custom
     * 
     * @param message Messaggio eccezione
     */
    public DbReversePluginBusinessException(String message) {
        super(message);
    }

    /**
     * Costruttore per eccezione Business custom
     * 
     * @param message Messaggio eccezione
     * @param cause eccezione tirata
     */
    public DbReversePluginBusinessException(String message, Throwable cause) {
        super(message, cause);
    }
}
