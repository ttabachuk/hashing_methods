/*
 * Author: Ammaad Denmark, Tim Tabachuk
 * Date: 12/10/22
 * Filename: HashTesting.java
 * 
 * Analysis of three different hashing methods
 * 
*/
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class HashTesting {
    public static void main(String[] args) {
        final int ARRAY_SIZE = 241;
        final int NAMES = 200;
        // create a 3 tables for 3 different hashing methods
        Table<Integer, String> linearTable = new Table<Integer, String>(ARRAY_SIZE);
        TableDoubleHash<Integer, String> doubleTable = new TableDoubleHash<Integer, String>(ARRAY_SIZE);
        TableChainHash<Integer, String> chainTable = new TableChainHash<Integer, String>(ARRAY_SIZE);

        // get input from names file
        File inFile = new File("names.txt");
        Scanner scanner = null;
        try {
            scanner = new Scanner(inFile);
        }
        catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
        
        // scan all lines in file and add to Hash Table
        String[] tokens;
        String name;
        int number;
        int attempt = 1;
        double linearTotal = 0.0;
        double doubleTotal = 0.0;
        double chainTotal = 0.0;
        
        System.out.println();
        System.out.println("Collisions per Attempted placement in Tables: ");
        System.out.println("Attempt \t Linear \t Double \t Chain");
        while(scanner.hasNextLine()) {
            tokens = scanner.nextLine().split(" ");
            name = tokens[0];
            number = Integer.parseInt(tokens[1]);
            try {
                linearTable.put(number, name);
                doubleTable.put(number, name);
                chainTable.put(number, name);
            } 
            catch (IllegalStateException e) {
                System.out.println(e.getMessage());
                break;
            }
            System.out.print("\t" + attempt + "\t\t");
            System.out.print("\t" + linearTable.getCollisions() + "\t\t");
            System.out.print("\t" + doubleTable.getCollisions() + "\t\t");
            System.out.println("\t" + chainTable.getCollisions() + "\t\t");
            linearTotal += (double) linearTable.getCollisions() / NAMES;
            doubleTotal += (double) doubleTable.getCollisions() / NAMES;
            chainTotal += ((double) chainTable.getCollisions()) / NAMES;
            attempt++;
        }

        System.out.println("---------------");
        System.out.printf("%.3f \n", linearTotal);
        System.out.printf("%.3f \n", doubleTotal);
        System.out.printf("%.3f \n", chainTotal);
    }
}