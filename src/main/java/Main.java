import utils.MultiThreadCSVReader;

public class Main {

    static final int numOfThreads = 3;

    static final String[] inputFilePaths = new String[]{
            "inputData/file1.csv",
            "inputData/file2.csv",
            "inputData/file3.csv"
    };

    static final String outputFolder = "outputData";

    public static void main(String[] args) throws InterruptedException {
        MultiThreadCSVReader multiThreadCSVReader = new MultiThreadCSVReader(inputFilePaths);

        multiThreadCSVReader.runMultiThreadReading(numOfThreads);

        multiThreadCSVReader.writeResultsToFiles(outputFolder);
    }
}
