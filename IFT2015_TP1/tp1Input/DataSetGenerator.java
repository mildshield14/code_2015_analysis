import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

public class DataSetGenerator {

    public static void main(String[] args) {
        // Random number generator
        Random random = new Random();

        // File path to write the data
        String filePath = "/Users/vennilasooben/IFT2015_TP1/tp1Input/dataset.txt";

        // Number of data points
        int numDataPoints = 100;

        try {
            // Create FileWriter and PrintWriter
            FileWriter fileWriter = new FileWriter(filePath);
            PrintWriter printWriter = new PrintWriter(fileWriter);

            // Write the two numbers at the beginning
            printWriter.println("10000 10000");

            // Generate and write the data points
            for (int i = 0; i < numDataPoints; i++) {
                int value = random.nextInt(100);
                double latitude = 45.0 + random.nextDouble();
                double longitude = -73.0 + random.nextDouble();

                String line = String.format("%d (%f,%f)", value, latitude, longitude);
                printWriter.println(line);
            }

            // Close the PrintWriter and FileWriter
            printWriter.close();
            fileWriter.close();

            System.out.println("Data generation completed. File saved at " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
