package it.devcrops.plugins.dbreverse.utility;

import it.devcrops.plugins.dbreverse.exception.DbReversePluginSystemException;
import it.devcrops.plugins.dbreverse.model.OggettoDB;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;
import org.codehaus.plexus.util.FileUtils;
import org.hibernate.engine.jdbc.internal.DDLFormatterImpl;

/**
 * Created by davide on 27/09/16.
 */
public class WriterUtils {

    /**
     * Costruttore privato previsto dalle linee guida.
     */
    private WriterUtils() {
        throw new IllegalAccessError("Utility class accessed without permission");

    }

    /**
     * Metodo di utilità per la scrittura su file del DDL degli oggetti DB su
     * file
     * 
     * @param outputDirectory
     * @param oggettoDB
     * @throws it.devcrops.plugins.dbreverse.exception.DbReversePluginSystemException
     */
    public static void writeDDLOggettoDB(String outputDirectory, OggettoDB oggettoDB) throws DbReversePluginSystemException {

        try {

            Path oggettoDirectoryPath = Paths.get(MessageFormat.format("{0}{1}{2}", outputDirectory, File.separator, oggettoDB.getTipo()));
            if (!Files.exists(oggettoDirectoryPath)) {
                Files.createDirectory(oggettoDirectoryPath);
            }

            Path oggettoFilePath = Paths.get(MessageFormat.format("{0}{1}{2}.sql", oggettoDirectoryPath, File.separator, oggettoDB.getNome()));
            if (Files.exists(oggettoFilePath)) {
                Files.delete(oggettoFilePath);
            }
            String formattedSQL = oggettoDB.getDdl();
            formattedSQL = new DDLFormatterImpl().format(formattedSQL);
            Files.write(oggettoFilePath, formattedSQL.getBytes(StandardCharsets.UTF_8));

        }
        catch (IOException ex) {
            throw new DbReversePluginSystemException(ex.getMessage(), ex);
        }

    }

    /**
     * Metodo di utilità per il controllo sulla validità dell'outputDirectory
     * 
     * @param outputDirectory
     * @throws IOException
     */
    public static String checkOutputDirectory(String outputDirectory, String defaultDirectory) throws IOException {
        Path outputDirectoryPath = null;

        if ("default".equals(outputDirectory)) {
            outputDirectoryPath = Paths.get(defaultDirectory);
            if (!Files.exists(outputDirectoryPath)) {
                Files.createDirectories(outputDirectoryPath);
            }
        }
        else {
            outputDirectoryPath = Paths.get(outputDirectory);
            if (!Files.exists(outputDirectoryPath) || !Files.isDirectory(outputDirectoryPath)) {
                throw new IOException(MessageFormat.format("Attenzione! L''output directory ''{0}'' non esiste o non è una directory.", outputDirectory));
            }
            if (!Files.isWritable(outputDirectoryPath)) {
                throw new IOException(MessageFormat.format("Attenzione! Impossibile scrivere nella directory ''{0}'' indicata.", outputDirectory));
            }
        }
        FileUtils.cleanDirectory(new File(outputDirectoryPath.toString()));
        return outputDirectoryPath.toString();
    }

}
