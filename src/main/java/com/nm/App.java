package com.nm;

import com.nm.db.OperData;
import com.nm.db.OperDataImpl;
import com.nm.db.WagData;
import com.nm.db.WagDataImpl;
import com.nm.parser.Parser2790;
import com.nm.parser.Parser2790Impl;
import com.nm.pojo.Wagon;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

public class App {

    private final Logger logger = LogManager.getLogger(App.class.getName());
    private final String MY_FOLDER = "c:\\demo\\";

    public static void main(String[] args) {
        App app = new App();
        app.startApp();
    }

    private void startApp() {
        try {
            WatchService watchService
                    = FileSystems.getDefault().newWatchService();

            Path path = Paths.get(MY_FOLDER);

            path.register(
                    watchService,
                    StandardWatchEventKinds.ENTRY_CREATE);

            WatchKey key;
            while ((key = watchService.take()) != null) {
                for (WatchEvent<?> event : key.pollEvents()) {
                    System.out.println(
                            "Event kind:" + event.kind()
                                    + ". File affected: " + event.context() + ".");

                    ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor();
                    ses.schedule(() -> readFile(MY_FOLDER, "" + event.context()), 1000, TimeUnit.MILLISECONDS);
                    ses.shutdown();
                }
                key.reset();
            }
        } catch (IOException e) {
            logger.warn("watchService IOE: " + e.getMessage());
        } catch (InterruptedException e) {
            logger.warn("watchService interruptE: " + e.getMessage());
        }
    }

    private synchronized void readFile(String path, String fName) {
        OperData operData = new OperDataImpl();
        WagData wagData = new WagDataImpl();
        String uRL = path + fName;
        System.out.println(uRL);
        StringBuilder sb = new StringBuilder();
        String s = "";
        try (Stream<String> stream = Files.lines(Paths.get(uRL), Charset.forName("CP866"))) {
//            stream.forEach(System.out::println);
            stream.forEach(sb::append);
            s += stream;
        } catch (IOException e) {
            logger.warn("readFile IOE: " + e.getMessage());
        }
        if (sb.length() > 0) {
            Parser2790 parser = new Parser2790Impl();
            Wagon wagon = parser.parsing(sb.toString());

            int idWag = wagData.persistData(wagon);
            if (idWag != 0) {
                operData.persistOpers(wagon.getList(), idWag);
            }
        }
//        if (sb.length() > 0) {
//            try {
//                BufferedWriter writer = new BufferedWriter(new FileWriter("c:\\temp\\" + fName));
//                writer.write(sb.toString());
//                writer.close();
//            } catch (IOException e) {
//                logger.warn("writeFile IOE: " + e.getMessage());
//            }
//        }
    }
}


