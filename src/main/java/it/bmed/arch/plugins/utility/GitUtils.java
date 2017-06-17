package it.bmed.arch.plugins.utility;

import it.bmed.arch.plugin.exception.DbReversePluginSystemException;
import org.apache.log4j.Logger;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import java.io.*;
import java.text.MessageFormat;

/**
 * Created by davide on 30/09/16.
 */
public class GitUtils {

    private static final String REMOTE = "remote";
    private static final String ORIGIN = "origin";
    private static final String URL = "url";
    private static final String HTTP = "http";
    private static final String ASK_USERNAME_MESSAGE = "Inserisci nome utente git : ";
    private static final String ASK_CREDENTIAL_MESSAGE = "Inseririsci password git : ";
    private static final Logger LOG = Logger.getLogger(GitUtils.class);

    /**
     * Costruttore privato previsto dalle linee guida.
     */
    private GitUtils() {
        throw new IllegalAccessError("Utility class accessed without permission");
    }

    /**
     * Il metodo esegue la add dell'outputDirectory designata, seguita dal
     * commit e dalla push. Inoltre richiede le credenziali in base alla
     * tipologia di connessione al repository che si utilizza
     * 
     * @param outputDirectory property directory root nella quale inserire gli
     *            oggetti ottenuti dal db
     * @param gitPushMessage property messaggio di commit
     * @throws it.bmed.arch.plugin.exception.DbReversePluginSystemException
     */
    public static void gitPush(String outputDirectory, String gitPushMessage) throws DbReversePluginSystemException {

        FileRepositoryBuilder builder = new FileRepositoryBuilder();
        Git git = null;

        try {
            Repository repository = builder.readEnvironment().findGitDir().build();
            git = new Git(repository);

            if (!git.diff().call().isEmpty()) {
                for (DiffEntry diff : git.diff().call()) {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug(MessageFormat.format("git add {0}", diff.getPath(DiffEntry.Side.NEW)));
                    }
                    if (diff.getChangeType().equals(diff.getChangeType().ADD)) {
                        git.add().addFilepattern(diff.getPath(DiffEntry.Side.NEW)).call();
                    }
                }
                if (LOG.isDebugEnabled()) {
                    LOG.debug(MessageFormat.format("git commit -a -m ''{0}''", gitPushMessage));
                }
                String apply = "--ignore-whitespace";
                InputStream inputStream = new ByteArrayInputStream(apply.getBytes());
                git.apply().setPatch(inputStream).call();
                git.commit().setAll(true).setMessage(gitPushMessage).call();

                if (LOG.isDebugEnabled()) {
                    LOG.debug("git push origin --all");
                }
                String url = repository.getConfig().getString(REMOTE, ORIGIN, URL);
                if (url.startsWith(HTTP)) {
                    git.push().setRemote(ORIGIN).setPushAll().setCredentialsProvider(getCredential()).call();
                } else {
                    git.push().setRemote(ORIGIN).setPushAll().call();
                }
                LOG.info("Le modifiche al DDL sullo schema interrogato sono state versionate sul repository remoto.");
            } else {
                LOG.info("Non sono state rilevate modifiche DDL sullo schema interrogato.");
            }
        } catch (GitAPIException | IOException e) {
            throw new DbReversePluginSystemException(e.getMessage(), e);
        } catch (Exception e) {
            throw new DbReversePluginSystemException(e.getMessage(), e);
        } finally {
            if (git != null) {
                git.close();
            }
        }
    }

    /**
     * @return Ritorna un oggetto contenente le credenziali d'accesso ad un
     *         repository git
     */
    private static UsernamePasswordCredentialsProvider getCredential() {
        String user = readLine(ASK_USERNAME_MESSAGE);
        String password = readLine(ASK_CREDENTIAL_MESSAGE);
        return new UsernamePasswordCredentialsProvider(user, password);
    }

    /**
     * Il metodo viene utilizzato per ottenere le credenziali d'accesso dalla
     * console.
     * 
     * @param label Messaggio visualizzato a console
     * @return risposta scritta su console.
     */
    private static String readLine(String label) {
        Console console = System.console();
        return console.readLine(label);
    }
}
