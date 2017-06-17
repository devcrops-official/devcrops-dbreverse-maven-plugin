package it.bmed.arch.plugins.db;

import it.bmed.arch.plugin.exception.DbReversePluginBusinessException;
import it.bmed.arch.plugin.exception.DbReversePluginSystemException;
import it.bmed.arch.plugins.db.mapper.OracleRowMapper;
import it.bmed.arch.plugins.model.OggettoDB;
import it.bmed.arch.plugins.utility.DbUtils;

import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ArrayList;

/**
 * Created by davide on 27/09/16.
 */
public class DatabaseOracle implements Database {

    private final DbUtils dbUtils;
    private final String dbSchema;
    private static final String SELECT_QUERY = "SELECT object_type, object_name, DBMS_METADATA.GET_DDL(replace(object_type, '' '', ''_'') ,object_name,''{0}'') as object_ddl FROM dba_objects WHERE owner=''{0}'' AND NOT REGEXP_LIKE (object_type, ''{1}'') AND NOT REGEXP_LIKE (object_name, ''{2}'')";
    private static final String OBJECT_TYPE_EXCLUSION = "^(.*PARTITION|LOB|QUEUE|DATABASE.*)";
    private static final String OBJECT_NAME_EXCLUSION = "^(SYS_.*)";

    /**
     * Costruttore della classe DatabaseOracle
     * 
     * @param dbParam
     * @param dbSchema
     */
    public DatabaseOracle(DbUtils dbParam, String dbSchema) {
        this.dbUtils = dbParam;
        this.dbSchema = dbSchema;
    }

    /**
     * Il metodo implementa l'interfaccia Database utilizzando l'oggetto DbUtils
     * per interagire con un Db Oracle
     * 
     * @return Una lista di OggettiDb che rispecchia il contenuto del DB.
     * @throws DbReversePluginSystemException
     * @throws DbReversePluginBusinessException
     */
    @Override
    public ArrayList<OggettoDB> getListaOggetti() throws DbReversePluginSystemException, DbReversePluginBusinessException {
        String query = MessageFormat.format(SELECT_QUERY, dbSchema, OBJECT_TYPE_EXCLUSION, OBJECT_NAME_EXCLUSION);
        OracleRowMapper oracleRowMapper = new OracleRowMapper();
        try {
            return dbUtils.executeQuery(query, oracleRowMapper);
        }
        catch (SQLException e) {
            throw new DbReversePluginSystemException(e.getMessage(), e);
        }
    }

}