//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

public class DataSetGeneratorClass {
    public DataSetGeneratorClass() {
    }
    public static void main( String[] args) {
    }


    public static void cal(int numDataPoints, int g) {
        Random random = new Random();
        String filePath = "/Users/vennilasooben/IFT2015_TP1/sample.txt";

        try {
            FileWriter fileWriter = new FileWriter(filePath);
            PrintWriter printWriter = new PrintWriter(fileWriter);
            printWriter.println("300 300");

            for(int i = 0; i < numDataPoints; ++i) {
                int value = random.nextInt(100);
                double latitude = 45.0 + g  + random.nextDouble();
                double longitude = -73.0 + g + random.nextDouble();
                String line = String.format("%d (%f,%f)", value, latitude, longitude);
                printWriter.println(line);
            }

            printWriter.close();
            fileWriter.close();
            System.out.println("Data generation completed. File saved at " + filePath);
        } catch (IOException var13) {
            var13.printStackTrace();
        }

    }
}

