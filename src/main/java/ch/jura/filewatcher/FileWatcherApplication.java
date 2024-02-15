package ch.jura.filewatcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.*;
import java.util.Scanner;

@SpringBootApplication
public class FileWatcherApplication implements CommandLineRunner {

    @Value("${watch-folder}")
    private String folderPath;
    @Value("${dest-folder}")
    private String destPath;

    private static Logger LOG = LoggerFactory
            .getLogger(FileWatcherApplication.class);

    public static void main(String[] args) {
        LOG.info("STARTING THE APPLICATION");
        SpringApplication.run(FileWatcherApplication.class, args);
        LOG.info("APPLICATION FINISHED");
    }

    @Override
    public void run(String... args) throws IOException, InterruptedException {
        LOG.info("Starting watching folder: " + folderPath );

        WatchService watchService = FileSystems.getDefault().newWatchService();
        Path path = Paths.get(folderPath);
        path.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);
        WatchKey key;
        while ((key = watchService.take()) != null) {

            for (WatchEvent<?> event : key.pollEvents()) {

                LOG.info("File found...: " + event.context().toString() );

                Path destinationPath = Paths.get(destPath);

                String fullDestPath = destinationPath.toAbsolutePath().toString() +
                        "/" + event.context().toString();

                LOG.info("Copy file to dest folder: " + destPath );

                Path dir = (Path)key.watchable();
                Path fullPath = dir.resolve(event.context().toString());

                File inputFile = new File(String.valueOf(fullPath));
                Scanner input = new Scanner(inputFile);

                PrintWriter writer = new PrintWriter(fullDestPath, "UTF-8");

                String line;
                while (input.hasNextLine())
                {
                    line =input.nextLine();
                    writer.println(line);

                }
                input.close();
                writer.close();

                if (inputFile.delete()){
                    LOG.info("Successfully deleted file from input folder: " + inputFile.getAbsolutePath() );
                }else {
                    LOG.info("Problem deleted file from inout folder: " + inputFile.getAbsolutePath() );
                }



                LOG.info("Event kind:" + event.kind() + ". File affected: " + event.context() + ".");
            }
            key.reset();
        }

        watchService.close();

    }



}
