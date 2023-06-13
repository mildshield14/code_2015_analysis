import java.io.*;
import java.util.ArrayList;
import java.util.Queue;
import java.util.Scanner;
import java.util.regex.*;

import static java.lang.Double.parseDouble;

public class ManipulationFichier {

    private static ArrayList<Integer> arrayBox;

    private static ArrayList<Coordinates> arrayCoord;

    public static void setArrayBox(ArrayList<Integer> arrayBox) {
        ManipulationFichier.arrayBox = arrayBox;
    }

    public static void setArrayCoord( ArrayList<Coordinates> arrayCoord) {
        ManipulationFichier.arrayCoord = arrayCoord;
    }

    public static  ArrayList<Coordinates> getArrayCoord() {
        return arrayCoord;
    }

    public static  ArrayList<Integer> getArrayBox() {
        return arrayBox;
    }


    // Sorts the informations from the input file to put the strings as arguments in the program
    public static void setData(String writefilestr, String s, GestionCamion c1, String input1){

        ManipulationFichier inputData = new ManipulationFichier();

        try {
            BufferedWriter deletef = new BufferedWriter(new FileWriter(writefilestr, false));
            deletef.close();
        } catch (IOException e) {
            System.out.println("Erreur dans l'ecriture du fichier");
        }

        Pattern pattern = Pattern.compile("(\\d+) \\((-?\\d+\\.\\d+),(-?\\d+\\.\\d+)\\)");
        Matcher matcher = pattern.matcher(s);

        ArrayList<Integer> box = new ArrayList();
        ArrayList<Coordinates> positions = new ArrayList();
        while (matcher.find()) {
            int number = Integer.parseInt(matcher.group(1));
            double latitude = parseDouble(matcher.group(2));
            double longitude = parseDouble(matcher.group(3));

            box.add(number);

            Coordinates ob1 = new Coordinates(longitude,latitude);

            positions.add(ob1);


        }

        String[] numbers = input1.split("\\s+");
        int firstNumber = Integer.parseInt(numbers[0]);
        int secondNumber = Integer.parseInt(numbers[1]);
        
        if (firstNumber>secondNumber){
            c1.setContentTruck(secondNumber);
        }else{
            c1.setContentTruck(firstNumber);
        }

        inputData.setArrayBox(box);
        inputData.setArrayCoord(positions);

    }


    // Retrieves the file with the name entered in the first argument and converts it into strings
    public static void readFile(String nameFile, String writefilename, GestionCamion c1){
        
        try {
            //Fetches the file and reads it
            File file = new File(nameFile);
            Scanner FileReader = new Scanner(file);
            String lines = "";
            String input1 = FileReader.nextLine();

            //Converts the lines into strings
            while (FileReader.hasNextLine()) {
                String input = FileReader.nextLine();
                String aLine = input + "/n";
                lines = lines + aLine;
            }
            FileReader.close();
            setData(writefilename,lines, c1, input1);
        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
        }

    }


    // Writes the output on the new file
    public static void writeFile(String writefilename, String out) {


        try {
            BufferedWriter outputs = new BufferedWriter(new FileWriter(writefilename, true));
            outputs.write(out+"\n");
            outputs.close();
        } catch (IOException e) {
            System.out.println("Erreur dans l'ecriture du fichier");
        }
    }


}
