import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Random;
import java.util.Scanner;

public class Driver {
    
    public static void main(String[] args) throws FileNotFoundException {
        File input = makeInputFile(100, 40);
        Point p1 = new Point();
        Point p2 = new Point();
        getPoundKeyLocations(p1, p2, input);
        System.out.println(p1 + " " + p2);
        getOutputFile(p1, p2, input);
    }
    
    //pre: the product of x and y must be postive throws IAE if violated
    //post: prints a grid to input.txt grid contains all periods with two pound sings
    public static File makeInputFile(int x, int y) throws FileNotFoundException {
        //if input is smaller than a 2 by 2 grid no path can be generated
        if(x * y < 4) {
            throw new IllegalArgumentException();
        }
        Random random = new Random();
        Point first = new Point(random.nextInt(x), random.nextInt(y));
        Point second = new Point(random.nextInt(x), random.nextInt(y));
        while(second.equals(first)) {
            second = new Point(random.nextInt(x), random.nextInt(y));
        }
        PrintStream inputFile = new PrintStream("input.txt");
        for(int i = 0; i < y; i++) {
            for(int j = 0; j < x; j++) {
                if((first.getX() == j && first.getY() == i) || (second.getX() == j && second.getY() == i)) {
                    inputFile.print("#");
                } else {
                    inputFile.print(".");
                }
            }
            inputFile.println();
        }
        return new File("input.txt");
    }
    
    //pre: given file must exist if violated throws FNFE input File must contain two pound key signs
    //     if violated throws IAE
    //post: changes first and second to be the location of both pound keys contained in given input file
    public static int getPoundKeyLocations(Point first, Point second, File input) throws FileNotFoundException {
        Scanner fileScanner = new Scanner(input);
        int poundKeysFound = 0;
        int row = 0; 
        while(fileScanner.hasNextLine()) {
            String currLine = fileScanner.nextLine();
            for(int i = 0; i < currLine.length(); i++) {
                if(currLine.charAt(i) == '#') {
                    if(poundKeysFound == 0) {
                        first.x = i;
                        first.y = row;
                        poundKeysFound++;
                    } else if(poundKeysFound == 1) {
                        second.x = i;
                        second.y = row;
                        poundKeysFound++;
                    } else {
                        throw new IllegalArgumentException();
                    }
                }
            }
            row++;
        }
        //if firstFound has not been incremented twice that mean the file does not contain
        //excetly two pound keys
        if(poundKeysFound != 2) {
            throw new IllegalArgumentException();
        }
        return poundKeysFound;
    }
    
    //pre: given inputFile must be an existing file that contains dots and hash signs in stanard format
    //post: returns an output file that includes all the information in the given input file however
    //      a path is drawn between the two hash signs by replacing all dots with stars
    public static File getOutputFile(Point first, Point second, File inputFile) throws FileNotFoundException {
        Scanner fileScanner = new Scanner(inputFile);
        PrintStream output = new PrintStream("output.txt");
        int y = 0;
        while(fileScanner.hasNextLine()) {
            String currLine = fileScanner.nextLine();
            for(int i = 0; i < currLine.length(); i++) {
                if(first.getX() == i && (y > first.getY() && (y < second.getY() || (y <= second.getY() && i != second.getX())))) {
                    output.print("*");
                } else if(second.getY() == y && (i > second.getX() && i <= first.getX()) || (i < second.getX() && i >= first.getX())) {
                    output.print("*");
                } else {
                    output.print(currLine.charAt(i));
                }

            }
            output.println();
            y++;
        }
        return new File("output.txt");
    }
}