package ch.jura.filewatcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.Scanner;

@SpringBootApplication
public class FileWatcherApplication implements CommandLineRunner {

    @Value("${watch-folder}")
    private String folderPath;

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
        path.register(watchService, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE, StandardWatchEventKinds.ENTRY_MODIFY);
        WatchKey key;
        while ((key = watchService.take()) != null) {
            for (WatchEvent<?> event : key.pollEvents()) {

                Path dir = (Path)key.watchable();
                Path fullPath = dir.resolve(event.context().toString());
                Scanner input = new Scanner(new File(String.valueOf(fullPath)));


                while (input.hasNextLine())
                {
                    LOG.info(input.nextLine());
                }


                LOG.info("Event kind:" + event.kind() + ". File affected: " + event.context() + ".");
            }
            key.reset();
        }

        watchService.close();

    }



}
