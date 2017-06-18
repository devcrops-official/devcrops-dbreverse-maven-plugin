package it.devcrops.plugins.dbreverse.goals;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

/**
 * Classe astratta che comprende tutte le properties necessarie per l'utilizzo
 * del Goal reverse-db
 */
public abstract class AbstractGoal extends AbstractMojo {

    /**
     * Cartella del progetto maven corrente.
     */
    @Parameter(defaultValue = "${project}", required = true, readonly = true)
    private MavenProject project;

    /**
     * La directory target sul quale verranno scritti i DDL ottenuti dal DB.
     */
    @Parameter(property = "dbOutputDirectory", defaultValue = "default", required = true)
    private String outputDirectory;

    /**
     * gitPush Nel caso in cui questa property sia abilitata verra` eseguita una
     * git add di tutto il contenuto della directory dbOutputDirectory seguita
     * da una commit e dalla push. Per poter utilizzare questa funzione e`
     * necessario che il progetto sia versionato su git.
     */
    @Parameter(property = "gitPush", defaultValue = "false")
    private Boolean gitPush;

    /**
     * gitPush Messaggio del commit Git
     */
    @Parameter(property = "gitPushMessage", defaultValue = "Commit modifiche DDL oggetti DB")
    private String gitPushMessage;

    /**
     * dbSchema Schema del db di riferimento.
     */
    @Parameter(property = "dbSchema", required = true)
    private String dbSchema;

    /**
     * dbDriver Driver del db. ( esempio: oracle, h2, ...)
     */
    @Parameter(property = "dbDriver", required = true)
    private String dbDriver;

    /**
     * dbConnection Url di connessione al db. ( esempio:
     * jdbc:oracle:thin:@localhost:1521:xe )
     */
    @Parameter(property = "dbConnection", required = true)
    private String dbConnection;

    /**
     * dbUser Username utenza db
     */
    @Parameter(property = "dbUser", required = true)
    private String dbUser;

    /**
     * dbPassword Password utenza db
     */
    @Parameter(property = "dbPassword", required = true)
    private String dbPassword;

    protected AbstractGoal() {
    }

    /**
     * @return {@link #project}
     */
    public MavenProject getProject() {
        return project;
    }

    /**
     * @return {@link #outputDirectory}
     */
    public String getOutputDirectory() {
        return outputDirectory;
    }

    /**
     * @return {@link #dbDriver}
     */
    public String getDbDriver() {
        return dbDriver;
    }

    /**
     * @return {@link #dbConnection}
     */
    public String getDbConnection() {
        return dbConnection;
    }

    /**
     * @return {@link #dbUser}
     */
    public String getDbUser() {
        return dbUser;
    }

    /**
     * @return {@link #dbPassword}
     */
    public String getDbPassword() {
        return dbPassword;
    }

    /**
     * @return {@link #dbSchema}
     */
    public String getDbSchema() {
        return dbSchema;
    }

    /**
     * @return {@link #gitPush}
     */
    public Boolean isGitPush() {
        return gitPush;
    }

    /**
     * @return {@link #getGitPushMessage}
     */
    public String getGitPushMessage() {
        return gitPushMessage;
    }

}
