package it.devcrops.plugins.dbreverse.db;

import it.devcrops.plugins.dbreverse.exception.DbReversePluginBusinessException;
import it.devcrops.plugins.dbreverse.exception.DbReversePluginSystemException;
import it.devcrops.plugins.dbreverse.model.OggettoDB;
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
