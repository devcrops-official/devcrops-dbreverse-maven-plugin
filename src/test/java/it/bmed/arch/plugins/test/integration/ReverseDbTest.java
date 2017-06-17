package it.bmed.arch.plugins.test.integration;

import it.bmed.arch.plugins.test.AbstractTest;
import org.apache.log4j.Logger;
import org.apache.maven.shared.invoker.*;
import org.junit.*;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.Properties;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 * @author pierluigi
 */
public class ReverseDbTest extends AbstractTest {

    private static final Logger LOG = Logger.getLogger(ReverseDbTest.class);
    private static Invoker invoker = new DefaultInvoker();
    private static String version;
    private static final String TABLE_SCHEMA_VERSION_FILE_GENERATED = "target/integration-test/TABLE/schema_version.sql";
    private static final String INDEX_SCHEMA_VERSION_FILE_PK_GENERATED = "target/integration-test/INDEX/schema_version_pk.sql";
    private static final String INDEX_SCHEMA_VERSION_FILE_S_IDX_GENERATED = "target/integration-test/INDEX/schema_version_s_idx.sql";
    private static final String EXPECTED_UNSUPPORTED_DRIVER_MESSAGE = " Attenzione! Il plugin non supporta il driver indicato 'driver.unsupported'";
    private static final String OUTPUT_DIRECTORY = "../../../../target/integration-test/";

    @BeforeClass
    public static void BeforeClass() throws ParserConfigurationException, SAXException, IOException, XPathExpressionException, MavenInvocationException {

        File pomFile = new File("pom.xml");
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document pom = dBuilder.parse(pomFile);
        XPathFactory xPathFactory = XPathFactory.newInstance();
        XPath xPath = xPathFactory.newXPath();
        Node versionNode = (Node) xPath.evaluate("/project/version", pom, XPathConstants.NODE);
        version = versionNode.getTextContent();
        LOG.info(MessageFormat.format("Preparazione installazione della versione {0} del plugin devops-data-dbreverse-maven-plugin ... ", version));

        Properties properties = new Properties();
        properties.setProperty("maven.test.skip", "true");
        properties.setProperty("bmed.check.skip-pmd", "true");
        InvocationRequest request = new DefaultInvocationRequest();
        request.setPomFile(new File("pom.xml"));
        request.setGoals(Collections.singletonList("install"));
        request.setProperties(properties);
        invoker = new DefaultInvoker();
        invoker.execute(request);

    }

    @Before
    public void Before() throws SQLException, IOException {
        LOG.debug("Start before ReverseDbTest");
        LOG.debug("End before ReverseDbTest");
    }

    @Test
    public void reverseDbSuccessTest() throws MavenInvocationException, IOException {
        LOG.info(MessageFormat.format("Start Test {0}", testName.getMethodName()));

        InvocationRequest request = new DefaultInvocationRequest();
        request.setPomFile(new File("src/test/resources/integration-test/pom.xml"));
        Properties properties = new Properties();
        properties.setProperty("plugin.version", version);
        properties.setProperty("dbPassword", DB_PASSWORD);
        properties.setProperty("dbUser", DB_USER);
        properties.setProperty("dbDriver", DB_DRIVER);
        properties.setProperty("dbSchema", DB_SCHEMA);
        properties.setProperty("dbOutputDirectory", OUTPUT_DIRECTORY);
        request.setGoals(Collections.singletonList("test "));
        request.setProperties(properties);
        InvocationResult result = invoker.execute(request);

        Assert.assertEquals(0, result.getExitCode());
        Assert.assertNull(result.getExecutionException());
        Assert.assertEquals("Il ddl della tabella schema_version in output è diverso da quello in input", generateSHA256(EXPECTED_TABLE_SCHEMA_VERSION_FILE), generateSHA256(new File(
                TABLE_SCHEMA_VERSION_FILE_GENERATED)));
        Assert.assertEquals("Il ddl dell'indice schema_version_pk in output è diverso da quello in input", generateSHA256(EXPECTED_INDEX_SCHEMA_VERSION_PK_FILE), generateSHA256(new File(
                INDEX_SCHEMA_VERSION_FILE_PK_GENERATED)));
        Assert.assertEquals("Il ddl dell'indice schema_version_s_idx in output è diverso da quello in input", generateSHA256(EXPECTED_TABLE_SCHEMA_VERSION_S_IDX_FILE), generateSHA256(new File(
                INDEX_SCHEMA_VERSION_FILE_S_IDX_GENERATED)));

        LOG.info(MessageFormat.format("Test {0} completato con successo", testName.getMethodName()));

    }

    @Test
    public void reverseDbUnsupportedDriver() throws MavenInvocationException, IOException {
        LOG.info(MessageFormat.format("Start Test {0}", testName.getMethodName()));

        InvocationRequest request = new DefaultInvocationRequest();
        request.setPomFile(new File("src/test/resources/integration-test/pom.xml"));
        Properties properties = new Properties();
        properties.setProperty("plugin.version", version);
        properties.setProperty("dbPassword", DB_PASSWORD);
        properties.setProperty("dbUser", DB_USER);
        properties.setProperty("dbDriver", "driver.unsupported");
        properties.setProperty("dbSchema", DB_SCHEMA);
        properties.setProperty("dbOutputDirectory", OUTPUT_DIRECTORY);
        request.setGoals(Collections.singletonList("test "));
        request.setProperties(properties);
        InvocationResult result = invoker.execute(request);
        Assert.assertEquals(1, result.getExitCode());
        Assert.assertNull(result.getExecutionException());

        LOG.info(MessageFormat.format("Test {0} completato con successo", testName.getMethodName()));
    }

    @After
    public void After() throws SQLException {
    }

}
