package utils;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.HashSetValuedHashMap;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MultiThreadCSVReader {
    // Contains files for reading
    private final Queue<String> filePaths;
    // Contains header by values
    private final MultiValuedMap<String, String> headerToValues;

    public MultiThreadCSVReader(Queue<String> filePaths) {
        this.filePaths = filePaths;
        headerToValues = new HashSetValuedHashMap<>();
    }

    public MultiThreadCSVReader(String[] filePaths) {
        this(new LinkedList<>(Arrays.asList(filePaths)));
    }


    /**
     * Starts threads to read csv files,
     * and waits for threads to finish
     *
     * @param numOfThreads number of threads
     * @throws InterruptedException if interrupted while waiting
     */
    public void runMultiThreadReading(int numOfThreads) throws InterruptedException {
        ExecutorService service = Executors.newFixedThreadPool(numOfThreads);

        // Running threads
        for (int i = 0; i < numOfThreads; i++) {
            service.submit(new ReaderThread());
        }

        // Waiting for threads to finish
        service.shutdown();
        service.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
    }


    /**
     * For each header, writes all values to a files
     *
     * @param outputFolder path to writing
     */
    public void writeResultsToFiles(String outputFolder) {
        try {
            for (String header : headerToValues.keySet()) {
                try (BufferedWriter bf = new BufferedWriter(new FileWriter(Paths.get(outputFolder, header).toFile()))) {
                    // Writing the header
                    bf.write(header + ':' + '\n');

                    // Writing all values for header
                    for (String value : headerToValues.get(header)) {
                        bf.write(value + ';');
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private class ReaderThread extends Thread {
        @Override
        public void run() {
            String filePath;

            while (true) {
                synchronized (filePaths) {
                    // Thread finished if there are no files
                    if (filePaths.isEmpty()) {
                        break;
                    }
                    // Get file for reading and remove from pool
                    filePath = filePaths.remove();
                }

                try {
                    // Read file
                    headerToValues.putAll(CSVReader.getHeaderToValuesByCSV(filePath));
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }
}