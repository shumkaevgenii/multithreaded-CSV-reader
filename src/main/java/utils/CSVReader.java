package utils;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.HashSetValuedHashMap;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class CSVReader {
    /**
     * Returns read data from csv file
     * throwing an exception if the CSVFilePath not found.
     *
     * @param CSVFilePath file to read
     * @return values by header MultiMap Collection
     * @throws IOException if the CSVFilePath not found
     */
    public static MultiValuedMap<String, String> getHeaderToValuesByCSV(String CSVFilePath) throws IOException {
        // Read all lines
        List<String> rows = Files.readAllLines(Paths.get(CSVFilePath));

        // Remove header line from rows and put to List
        List<String> headers = Arrays.asList(rows.remove(0).split(";"));

        MultiValuedMap<String, String> headerToValues = new HashSetValuedHashMap<>();

        for (String row : rows) {
            // Read row
            List<String> rowValues = Arrays.asList(row.split(";"));

            // Add all read values by headers to MultiMap
            for (int columnIndex = 0; columnIndex < rowValues.size(); columnIndex++) {
                headerToValues.put(headers.get(columnIndex), rowValues.get(columnIndex));
            }
        }

        return headerToValues;
    }
}