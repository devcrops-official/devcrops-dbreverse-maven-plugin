package it.bmed.arch.plugins.test.unit;

import it.bmed.arch.plugin.exception.DbReversePluginSystemException;
import it.bmed.arch.plugins.model.OggettoDB;
import it.bmed.arch.plugins.test.AbstractTest;
import it.bmed.arch.plugins.utility.WriterUtils;
import org.apache.log4j.Logger;
import org.hibernate.engine.jdbc.internal.DDLFormatterImpl;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;

import static org.junit.Assert.assertEquals;

/**
 * @author pierluigi
 */
public class WriterUtilsTest extends AbstractTest {

    private static final Logger LOG = Logger.getLogger(WriterUtilsTest.class);
    private static final String BASEPATH_NOT_FOUND_MSG_FAIL = "Il path inserito non esiste, il test doveva andare in errore.";
    private static final String BASEPATH_NOT_WRITABLE_MSG_FAIL = "Il path inserito non risulta accessibile da parte del plugin, dovrebbe andare in errore.";
    private static final String EXCEPTION_MSG_FAIL = "L'eccezione lanciata non è quello attesa";

    private OggettoDB oggettoDB = new OggettoDB();

    @Before
    public void before() {
        LOG.debug(MessageFormat.format("@Before Test {0}", testName.getMethodName()));
        oggettoDB.setNome("Persons");
        oggettoDB.setTipo("TABLE");
        oggettoDB.setDdl("CREATE TABLE Persons ( PersonID int," + "LastName varchar(255)," + "FirstName varchar(255)," + "Address varchar(255)," + "City varchar(255)" + ");");
    }

    /**
     * Il test restituisce un fault poichè non trova il basePath della directory
     * su cui scrivere gli oggetti DB.
     */
    @Test
    public void basePathNotFoundFaultTest() {
        String path = "faultTest";
        try {
            WriterUtils.writeDDLOggettoDB(path, oggettoDB);
            Assert.fail(BASEPATH_NOT_FOUND_MSG_FAIL);
        }
        catch (DbReversePluginSystemException e) {
            assertEquals(EXCEPTION_MSG_FAIL, java.nio.file.NoSuchFileException.class, e.getCause().getClass());
        }
        LOG.info(MessageFormat.format("Test {0} completato con successo", testName.getMethodName()));
    }

    /**
     * Il test restituisce un fault poichè la jvm non hai i diritti di scrittura
     * su quel basePath.
     */
    @Test
    public void writePermissionDeniedFaultTest() {
        File dir = new File("target/readonly");
        dir.mkdirs();
        dir.setReadOnly();
        try {
            WriterUtils.writeDDLOggettoDB(dir.getPath(), oggettoDB);
            Assert.fail(BASEPATH_NOT_WRITABLE_MSG_FAIL);
        }
        catch (DbReversePluginSystemException e) {
            assertEquals(EXCEPTION_MSG_FAIL, java.nio.file.AccessDeniedException.class, e.getCause().getClass());
        }
        dir.setWritable(true);
        LOG.info(MessageFormat.format("Test {0} completato con successo", testName.getMethodName()));
    }

    /**
     * Il test verifica la corretta scrittura di un oggetto DB passato in input.
     */
    @Test
    public void writeDDLOggettoDBTest() {
        try {
            File dir = new File("target/outputDirectory");
            dir.mkdirs();
            WriterUtils.writeDDLOggettoDB(dir.getPath(), oggettoDB);
            Path oggettoDirectoryPath = Paths.get(MessageFormat.format("{0}{1}{2}", "target/outputDirectory", File.separator, oggettoDB.getTipo()));
            if (!Files.exists(oggettoDirectoryPath)) {
                Assert.fail("Il test va in errore. La cartella tipo non viene creata correttamente");
            }

            Path oggettoFilePath = Paths.get(MessageFormat.format("{0}{1}{2}.sql", oggettoDirectoryPath, File.separator, oggettoDB.getNome()));
            if (!Files.exists(oggettoFilePath)) {
                Assert.fail("Il test va in errore. Il file name non viene creato correttamente");
            }
            try {
                String file = new String(Files.readAllBytes(oggettoFilePath));
                Assert.assertEquals("Il file contiene informazioni diverse rispetto a quelle previste", new DDLFormatterImpl().format(oggettoDB.getDdl()), file);
            }
            catch (IOException e) {
                Assert.fail("Impossibile leggere il file generato.");
            }
        }
        catch (DbReversePluginSystemException e) {
            Assert.fail("Errore in fase di scrittura");
            LOG.debug(e.getCause().getMessage());
        }
        LOG.info(MessageFormat.format("Test {0} completato con successo", testName.getMethodName()));
    }

    @After
    public void after() {
        LOG.debug(MessageFormat.format("@After Test {0}", testName.getMethodName()));
    }

}
