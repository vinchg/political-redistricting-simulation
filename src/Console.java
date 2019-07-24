/**
 * Console
 *
 * @author Vincent Cheong
 * Student ID: 004299384
 */

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;


public class Console {
    public Console() {
        Scanner s = new Scanner(System.in);
        menu();

        while(true) {
            String line = s.nextLine();
            line.replaceAll("\\s+","");

            if ("1".equals(line)) {
                City c = new City();
                System.out.print("Input Districts: ");
                String input = s.nextLine();
                c.printConnected(input);
            } else if ("2".equals(line)) {
                System.out.print("File name: ");
                String input = s.nextLine();
                ArrayList[] group_list = parse(input);
                State s1 = new State(new City(), group_list);
                s1.printState();
            } else if ("3".equals(line)) {
                Anneal.execute();
            } else if ("menu".equalsIgnoreCase(line)) {
                menu();
            } else if ("quit".equalsIgnoreCase(line)) {
                return;
            } else {
                System.out.println();
                menu();
            }
        }
    }

    private void menu() {
        System.out.println("----Applying Simulated Annealing to Political Redistricting - Assignment 4----");
        System.out.println(" 1 | Check District");
        System.out.println(" 2 | Check State");
        System.out.println(" 3 | Run Simulation");
        System.out.println(" menu | Print Menu");
        System.out.println(" quit | Quit");
        System.out.println("----------------------------------------------------------------");
    }


    private ArrayList[] parse(String filename) {
        ArrayList[] ret = new ArrayList[State.MEMBERS];
        Scanner s;
        String line;
        try {
            s = new Scanner(new FileReader(filename));

            int line_number = 0; // The line represents the district in the formatted file
            while(s.hasNextLine()) {
                line = s.nextLine();
                ret[line_number] = parseLine(line, line_number);
                line_number++;
                if (line_number >= State.MEMBERS)
                    break;
            }
            s.close();

            System.out.println("Read Successful: " + filename + " | Lines Parsed: " + line_number);
            return ret;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }


    public ArrayList<District> parseLine(String line, int line_number) {
        ArrayList<District> ret = new ArrayList<>();
        line = line.replaceAll(",", " ");
        Scanner s = new Scanner(line);
        int value;
        while(s.hasNextInt()) {
            ret.add(new District(s.nextInt(), line_number));
        }
        return ret;
    }
}
