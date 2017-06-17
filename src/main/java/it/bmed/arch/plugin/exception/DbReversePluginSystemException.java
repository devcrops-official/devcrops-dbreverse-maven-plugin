package it.bmed.arch.plugin.exception;

/**
 * @author giacomo
 */
public class DbReversePluginSystemException extends Exception {

    /**
     * Costruttore per eccezione System custom senza parametri
     */
    public DbReversePluginSystemException() {
        super();
    }

    /**
     * Costruttore per eccezione System custom
     * 
     * @param cause eccezione lanciata
     */
    public DbReversePluginSystemException(Throwable cause) {
        super(cause);
    }

    /**
     * Costruttore per eccezione System custom
     * 
     * @param message messaggio eccezione
     */
    public DbReversePluginSystemException(String message) {
        super(message);
    }

    /**
     * Costruttore per eccezione System custom
     * 
     * @param message messaggio eccezione
     * @param cause eccezione lanciata
     */
    public DbReversePluginSystemException(String message, Throwable cause) {
        super(message, cause);
    }
}
