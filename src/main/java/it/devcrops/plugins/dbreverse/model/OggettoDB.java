package it.devcrops.plugins.dbreverse.model;

/**
 * Created by davide on 27/09/16.
 */
public class OggettoDB {
    private String nome = null;
    private String tipo = null;
    private String ddl = null;

    /**
     * Costruttore OggettoDB che rappresenta un'oggetto del db. L'oggetto
     * contiene: il tipo dell'oggetto il nome dell'oggetto il ddl dell'oggetto
     * 
     * @param tipo
     * @param nome
     * @param ddl
     */
    public OggettoDB(String tipo, String nome, String ddl) {
        this.tipo = tipo;
        this.nome = nome;
        this.ddl = ddl;
    }

    /**
     * Costruttore vuoto.
     */
    public OggettoDB() {
        /**
         * Costruttore vuoto necessario per poter creare un oggetto di tipo
         * OggettoDB vuoto.
         */
    }

    /**
     * @return nome dell'oggetto
     */
    public String getNome() {
        return nome;
    }

    /**
     * Permette di impostare la variabile Nome con il valore di input
     * 
     * @param nome
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * @return tipo dell'oggetto
     */
    public String getTipo() {
        return tipo;
    }

    /**
     * Permette di impostare la variabile Tipo con il valore di input
     * 
     * @param tipo
     */
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    /**
     * @return ddl dell'oggetto
     */
    public String getDdl() {
        return ddl;
    }

    /**
     * Permette di impostare la variabile dll con il valore di input
     * 
     * @param ddl
     */
    public void setDdl(String ddl) {
        this.ddl = ddl;
    }

}
