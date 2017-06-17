package it.bmed.arch.plugins.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import org.junit.Rule;
import org.junit.rules.TestName;

import java.io.IOException;
import java.net.URL;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.BeforeClass;

/**
 * Created by pierluigi on 12/10/15.
 */
public abstract class AbstractTest {

    // private static final Logger LOG =
    // Logger.getLogger(AbstractTest.class);

    protected static final String DB_DRIVER = "org.h2.Driver";
    protected static final String DB_CONNECTION = "jdbc:h2:mem:dbtest";
    protected static final String DB_USER = "sa";
    protected static final String DB_PASSWORD = "";
    protected static final String DB_SCHEMA = "sa";

    protected static final String INIT_DB_SQL = "init.sql";
    protected static File INIT_DB_SQL_FILE;
    protected static final String EXPECTED_TABLE_SCHEMA_VERSION = "ddlExpected/table_schema_version.sql";
    protected static File EXPECTED_TABLE_SCHEMA_VERSION_FILE;
    protected static final String EXPECTED_INDEX_SCHEMA_VERSION_PK = "ddlExpected/index_schema_version_pk.sql";
    protected static File EXPECTED_INDEX_SCHEMA_VERSION_PK_FILE;
    protected static final String EXPECTED_INDEX_SCHEMA_VERSION_S_IDX = "ddlExpected/index_schema_version_s_idx.sql";
    protected static File EXPECTED_TABLE_SCHEMA_VERSION_S_IDX_FILE;

    @Rule
    public final TestName testName = new TestName();

    @BeforeClass
    public static void beforeClass() throws Exception {
        INIT_DB_SQL_FILE = new File(AbstractTest.class.getClassLoader().getResource(INIT_DB_SQL).getFile());
        EXPECTED_TABLE_SCHEMA_VERSION_FILE = new File(AbstractTest.class.getClassLoader().getResource(EXPECTED_TABLE_SCHEMA_VERSION).getFile());
        EXPECTED_INDEX_SCHEMA_VERSION_PK_FILE = new File(AbstractTest.class.getClassLoader().getResource(EXPECTED_INDEX_SCHEMA_VERSION_PK).getFile());
        EXPECTED_TABLE_SCHEMA_VERSION_S_IDX_FILE = new File(AbstractTest.class.getClassLoader().getResource(EXPECTED_INDEX_SCHEMA_VERSION_S_IDX).getFile());

    }

    public static String generateSHA256(File file) throws FileNotFoundException, IOException {
        FileInputStream fileInputStream = new FileInputStream(file);
        String digest = DigestUtils.sha256Hex(fileInputStream);
        return digest;
    }
}
