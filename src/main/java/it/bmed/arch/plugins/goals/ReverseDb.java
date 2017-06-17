package it.bmed.arch.plugins.goals;

import it.bmed.arch.plugin.exception.DbReversePluginBusinessException;
import it.bmed.arch.plugin.exception.DbReversePluginSystemException;
import it.bmed.arch.plugins.db.Database;
import it.bmed.arch.plugins.db.DatabaseOracle;
import it.bmed.arch.plugins.model.OggettoDB;
import it.bmed.arch.plugins.utility.DbUtils;
import it.bmed.arch.plugins.utility.GitUtils;
import it.bmed.arch.plugins.utility.WriterUtils;
import java.text.MessageFormat;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;

import java.util.ArrayList;
import org.apache.log4j.Logger;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

/**
 * Goal per recuperare tutti i DDL di un DB.<br/>
 * <b>Usage examples</b>:
 * <p>
 * <i>Con configurazioni di customizzazione di default (dal classpath del
 * plugin)</i>:
 * </p>
 * <blockquote> <code>
 * mvn devops-data-dbreverse-maven-plugin:reverse-db \<br/>
 * </code> </blockquote>
 * 
 * @author Imola Informatica
 * @see org.apache.maven.plugin.Mojo
 * @since 1.0
 */
@Mojo(name = "reverse-db", requiresProject = true, defaultPhase = LifecyclePhase.GENERATE_RESOURCES)
public class ReverseDb extends AbstractGoal {

    private static final Logger LOG = Logger.getLogger(ReverseDb.class);
    private static final String ORACLE_DRIVER = "oracle.jdbc.driver.OracleDriver";
    private static final String H2_DRIVER = "org.h2.Driver";

    /**
     * Costruttore vuoto.
     */
    public ReverseDb() {
        /**
         * Costruttore vuoto della classe rappresentante il Goal maven
         */
    }

    /**
     * Esegue il goal reverse-db. Il goal recupera i ddl di tutte le cartelle di
     * cui l'utenza db assegnata ha visibilita. Nel caso in cui la property sia
     * correttamente inizializzata il goal si occupa anche di effettuare una
     * push delle modifiche sul git del progetto.
     * 
     * @throws org.apache.maven.plugin.MojoExecutionException
     * @throws org.apache.maven.plugin.MojoFailureException
     */
    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {

        Database db;
        DbUtils dbParam = new DbUtils(getDbDriver(), getDbConnection(), getDbUser(), getDbPassword());

        try {
            switch (getDbDriver()) {
                case ORACLE_DRIVER:
                case H2_DRIVER:
                    db = new DatabaseOracle(dbParam, getDbSchema());
                    break;
                default:
                    throw new IllegalArgumentException(MessageFormat.format("Attenzione! Il plugin non supporta il driver indicato ''{0}'' .", getDbDriver()));
            }
            
            LOG.info(MessageFormat.format("Estrazioni degli oggetti dallo schema {0} ...", getDbSchema()));
            
            ArrayList<OggettoDB> listaOggettiDb = db.getListaOggetti();
            if (!listaOggettiDb.isEmpty()) {
                
                LOG.info(MessageFormat.format("Estrazione completata, oggetti estratti {0}.", listaOggettiDb.size()));
                LOG.info("Scrittura su file degli oggetti estratti ...");
                
                String outputDirectory = WriterUtils.checkOutputDirectory(getOutputDirectory(), getDbSchema());
                for (OggettoDB oggettoDb : listaOggettiDb) {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug(MessageFormat.format("Scrittura dell''oggetto {0} di tipo {1}", oggettoDb.getNome(), oggettoDb.getTipo()));
                    }
                    WriterUtils.writeDDLOggettoDB(outputDirectory, oggettoDb);
                }
                LOG.info("Scrittura terminata.");
                
                if (isGitPush()) {
                    LOG.info("Push sul repository remoto di eventuali modifiche effettuate al DDL degli oggetti db ...");
                    GitUtils.gitPush(outputDirectory, getGitPushMessage());
                }
            } else {
                LOG.info(MessageFormat.format("Non risultano essere presenti oggetti all''interno dello schema {0} ...", getDbSchema()));
            }
        } catch (DbReversePluginSystemException ex) {
            throw new MojoExecutionException(ex.getMessage(), ex);
        } catch (IllegalArgumentException | DbReversePluginBusinessException e) {
            throw new MojoFailureException(e.getMessage(), e);
        } catch (Exception e) {
            throw new MojoExecutionException(e.getMessage(), e);
        }
    }
}
