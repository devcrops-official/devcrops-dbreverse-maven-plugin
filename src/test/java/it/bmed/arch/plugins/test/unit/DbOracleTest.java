package it.bmed.arch.plugins.test.unit;

import it.bmed.arch.plugin.exception.DbReversePluginBusinessException;
import it.bmed.arch.plugin.exception.DbReversePluginSystemException;
import it.bmed.arch.plugins.db.DatabaseOracle;
import it.bmed.arch.plugins.model.OggettoDB;
import it.bmed.arch.plugins.test.AbstractTest;
import it.bmed.arch.plugins.utility.DbUtils;
import org.apache.log4j.Logger;
import org.h2.jdbc.JdbcSQLException;
import org.hibernate.engine.jdbc.internal.DDLFormatterImpl;
import org.junit.*;

import java.io.IOException;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ArrayList;

/**
 * @author pierluigi
 */
public class DbOracleTest extends AbstractTest {

    private static final Logger LOG = Logger.getLogger(DbOracleTest.class);

    private DbUtils dbUtils;
    private Connection dbConnection;

    @BeforeClass
    public static void BeforeClass() throws SQLException, IOException {
        LOG.debug("Start beforeClass DbOracleTest");
        LOG.debug("End beforeClass DbOracleTest");
    }

    @Before
    public void before() throws SQLException, IOException {
        LOG.debug(MessageFormat.format("Start @Before Test {0}", testName.getMethodName()));
        dbConnection = DriverManager.getConnection(DB_CONNECTION, DB_USER, DB_PASSWORD);
        String sql = new String(Files.readAllBytes(INIT_DB_SQL_FILE.toPath()));
        dbConnection.prepareStatement(sql).executeUpdate();
        dbUtils = new DbUtils(DB_DRIVER, DB_CONNECTION, DB_USER, DB_PASSWORD);
        LOG.debug(MessageFormat.format("End @Before Test {0}", testName.getMethodName()));
    }

    /**
     * Il test deve restituire un fault poichè non riesce a stabilire una
     * connessione con il DB.
     */
    @Test
    public void dbConnectionFaultTest() {
        LOG.info(MessageFormat.format("Start Test {0}", testName.getMethodName()));
        DbUtils dbUtilsFault = new DbUtils(DB_DRIVER, "jdbc/faultDb", DB_USER, DB_PASSWORD);
        DatabaseOracle test = new DatabaseOracle(dbUtilsFault, DB_SCHEMA);
        try {
            test.getListaOggetti();
            Assert.fail("La connessione va a buon fine nonostante l'url non sia valido");
        }
        catch (DbReversePluginSystemException e) {
            Assert.assertEquals("L'eccezione non è quella attesa", SQLException.class, e.getCause().getClass());
        }
        catch (DbReversePluginBusinessException e) {
            e.printStackTrace();
        }
        LOG.info(MessageFormat.format("Test {0} completato con successo", testName.getMethodName()));
    }

    /**
     * Il test deve restituire un fault poichè nella tabella user_objects
     * esistono oggetti per cui non c'è un ddl, in sostanza la function
     * DBMS_METADATA.GET_DDL fallisce e quindi l'intera query fallisce. Es.
     * restituisce : ORA-31600: valore di input DATABASE_LINK non valido per il
     * parametro OBJECT_TYPE della funzione GET_DDL ORA-06512: a
     * "SYS.DBMS_METADATA", line 5805 ORA-06512: a "SYS.DBMS_METADATA", line
     * 8344 ORA-06512: a line 1
     */
    @Test
    public void getListaOggettiFaultTest() {
        LOG.info(MessageFormat.format("Start Test {0}", testName.getMethodName()));

        DatabaseOracle databaseOracle = new DatabaseOracle(dbUtils, "s");
        try {
            ArrayList<OggettoDB> list = databaseOracle.getListaOggetti();
            Assert.assertEquals("La dimensione della lista non corrisponde a quella attesa", 0, list.size());
        }
        catch (DbReversePluginSystemException e) {
            LOG.info(e.getCause().getMessage());
            LOG.info("Restituisce errore di tipo system exception");
        }
        catch (DbReversePluginBusinessException e) {
            Assert.assertEquals("L'eccezione lanciata non è quello attesa", JdbcSQLException.class, e.getCause().getClass());
        }
        LOG.info(MessageFormat.format("Test {0} completato con successo", testName.getMethodName()));

    }

    /**
     * Il test deve restituire una lista di OggettoDB pari al numero di oggetti
     * presenti nel DB in memory, il loro DDL deve corrispondere esattamente a
     * quello specificato.
     */
    @Test
    public void getListaOggettiTest() throws SQLException, IOException, DbReversePluginBusinessException, DbReversePluginSystemException {
        LOG.info(MessageFormat.format("Start Test {0}", testName.getMethodName()));

        DatabaseOracle databaseOracle = new DatabaseOracle(dbUtils, DB_SCHEMA);
        ArrayList<OggettoDB> list = databaseOracle.getListaOggetti();

        Assert.assertEquals("La dimensione della lista non corrisponde a quella attesa", 3, list.size());
        Assert.assertEquals("Il tipo dell'oggetto non corrisponde", "TABLE", list.get(0).getTipo());
        Assert.assertEquals("Il nome dell'oggetto non corrisponde", "schema_version", list.get(0).getNome());
        Assert.assertEquals("Il ddl dell'oggetto non corrisponde", new String(Files.readAllBytes(EXPECTED_TABLE_SCHEMA_VERSION_FILE.toPath())),
                new DDLFormatterImpl().format(list.get(0).getDdl()));
        Assert.assertEquals("Il tipo dell'oggetto non corrisponde", "INDEX", list.get(1).getTipo());
        Assert.assertEquals("Il nome dell'oggetto non corrisponde", "schema_version_pk", list.get(1).getNome());
        Assert.assertEquals("Il ddl dell'oggetto non corrisponde", new String(Files.readAllBytes(EXPECTED_INDEX_SCHEMA_VERSION_PK_FILE.toPath())),
                new DDLFormatterImpl().format(list.get(1).getDdl()));
        Assert.assertEquals("Il tipo dell'oggetto non corrisponde", "INDEX", list.get(2).getTipo());
        Assert.assertEquals("Il nome dell'oggetto non corrisponde", "schema_version_s_idx", list.get(2).getNome());
        Assert.assertEquals("Il ddl dell'oggetto non corrisponde", new String(Files.readAllBytes(EXPECTED_TABLE_SCHEMA_VERSION_S_IDX_FILE.toPath())),
                new DDLFormatterImpl().format(list.get(2).getDdl()));

        LOG.info(MessageFormat.format("Test {0} completato con successo", testName.getMethodName()));
    }

    @After
    public void after() throws SQLException {
        LOG.debug(MessageFormat.format("@After Test {0}", testName.getMethodName()));
        dbConnection.close();
        /*
         * Viene eliminata l'istanza di DB in memory
         */

        LOG.debug(MessageFormat.format("End @After Test {0}", testName.getMethodName()));
    }

}
