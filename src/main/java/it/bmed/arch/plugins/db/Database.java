package it.bmed.arch.plugins.db;

import it.bmed.arch.plugin.exception.DbReversePluginBusinessException;
import it.bmed.arch.plugin.exception.DbReversePluginSystemException;
import it.bmed.arch.plugins.model.OggettoDB;
import java.util.ArrayList;

/**
 * Created by davide on 27/09/16.
 */
public interface Database {

    /**
     * Interfaccia Database per ottenere una lista dei DDL presenti nel DB al
     * quale il plugin risulta connesso
     * 
     * @return Una lista di OggettiDb che rispecchia il contenuto del DB.
     * @throws DbReversePluginSystemException
     * @throws DbReversePluginBusinessException
     */
    ArrayList<OggettoDB> getListaOggetti() throws DbReversePluginSystemException, DbReversePluginBusinessException;

}
