package it.devcrops.plugins.dbreverse.utility;

import it.devcrops.plugins.dbreverse.exception.DbReversePluginBusinessException;
import it.devcrops.plugins.dbreverse.exception.DbReversePluginSystemException;
import it.devcrops.plugins.dbreverse.model.OggettoDB;

import java.sql.*;
import java.util.ArrayList;
import it.devcrops.plugins.dbreverse.db.mapper.RowMapper;
import java.text.MessageFormat;
import org.apache.log4j.Logger;

/**
 * Created by davide on 27/09/16.
 */
public class DbUtils {

    private final String dbdriver;
    private final String dbconnection;
    private final String dbuser;
    private final String dbpassword;
    private static final Logger LOG = Logger.getLogger(DbUtils.class);

    /**
     * Costruttre oggetto di tipo DbUtils.
     * 
     * @param dbDriver
     * @param dbConnection
     * @param dbUser
     * @param dbPassword
     */
    public DbUtils(String dbDriver, String dbConnection, String dbUser, String dbPassword) {

        LOG.debug(MessageFormat.format("Configuro DbDriver ''{0}'' .", dbDriver));
        this.dbdriver = dbDriver;
        LOG.debug(MessageFormat.format("Configuro DbConnection ''{0}'' .", dbConnection));
        this.dbconnection = dbConnection;
        LOG.debug(MessageFormat.format("Configuro DbUser ''{0}'' .", dbUser));
        this.dbuser = dbUser;
        LOG.debug(MessageFormat.format("Configuro DbPassword ''{0}'' .", dbPassword));
        this.dbpassword = dbPassword;

    }

    /**
     * Il metodo, ad uso privato, restituisce una connessione al db.
     * 
     * @return Connection
     * @throws DbReversePluginSystemException
     */
    private Connection getDBConnection() throws DbReversePluginSystemException {

        Connection dbConnection;
        try {
            Class.forName(dbdriver);
            dbConnection = DriverManager.getConnection(dbconnection, dbuser, dbpassword);
            return dbConnection;
        } catch (ClassNotFoundException | SQLException e) {
            throw new DbReversePluginSystemException(e.getMessage(), e);
        }
    }

    /**
     * Il metodo esegue una query passata in input e, tramite un RowMappe, crea
     * degli oggetti appropriati con il Result Set ottenuto
     * 
     * @param query Query d'input, varia da tipologia di db
     * @param rowMapp Il rowMapper puo` variare in base alla necessita` di
     *            manipolare i dati ottenuti
     * @return Una lista di OggettiDb che rispecchia il contenuto del DB.
     * @throws SQLException
     * @throws DbReversePluginSystemException
     * @throws DbReversePluginBusinessException
     */
    public ArrayList<OggettoDB> executeQuery(String query, RowMapper rowMapp) throws SQLException, DbReversePluginSystemException, DbReversePluginBusinessException {
        LOG.debug(MessageFormat.format("Esecuzione query : \n {0} .", query));
        ArrayList<OggettoDB> listaOggetti = new ArrayList<>();
        try (Connection connection = getDBConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        listaOggetti.add(rowMapp.mapRow(resultSet));
                    }
                    return listaOggetti;
                }
            } catch (SQLException e) {
                throw new DbReversePluginBusinessException(e.getMessage(), e);
            }
        }
    }
}
