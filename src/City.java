import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * City
 *
 * @author Vincent Cheong
 * Student ID: 004299384
 */

public class City {

    final static int NUM_DISTRICTS = 66;
    final static int[] POPULATION = {
            2992, 2032, 3021, 1973, 2020, 2977, 2003, 3004, 2985, 3024,
            3032, 3004, 3020, 1980, 2026, 2008, 1980, 1984, 1978, 2008,
            2008, 1991, 2987, 2031, 2969, 3028, 2991, 3001, 1993, 2010,
            2995, 2979, 2008, 2010, 3027, 2991, 1974, 2018, 1979, 3028,
            2020, 2983, 3004, 1970, 3023, 2973, 2024, 5976, 1978, 1975,
            1969, 2978, 2030, 2002, 2971, 1991, 2997, 2024, 2990, 1993,
            2009, 3004, 3026, 2984, 3011, 2993
    };
    boolean[][] BORDERS;
    boolean[][] b_ref;  // Since passing primitives by references is a pain in Java

    /**
     * Default Constructor
     */
    public City() {
        parseFile("default.txt");
        BORDERS = hardCopy(b_ref);
        //printBorders();  // Debugging
    }


    /**
     * parseFile: Generates an adjacency matrix from a formatted file
     * @param filename
     */
    public void parseFile(String filename) {
        Scanner s;
        String line;
        b_ref = new boolean[NUM_DISTRICTS][NUM_DISTRICTS];  // Initialize b_ref
        try {
            s = new Scanner(new FileReader(filename));

            int line_number = 0; // The line represents the district in the formatted file
            while(s.hasNextLine()) {
                line = s.nextLine();
                parseLine(line, line_number);
                line_number++;
                if (line_number >= NUM_DISTRICTS)
                    break;
            }
            s.close();

            System.out.println("Read Successful: " + filename + " | Lines Parsed: " + line_number);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    /**
     * parseLine: Given a single line, parse it and modify the boolean matrix appropriately
     * @param line
     * @param line_number
     */
    public void parseLine(String line, int line_number) {
        line = line.replaceAll(",", " ");
        Scanner s = new Scanner(line);
        int value;
        while(s.hasNextInt()) {
            value = s.nextInt() - 1;
            b_ref[line_number][value] = true;
            b_ref[value][line_number] = true;
        }
    }


    public void printBorders() {
        System.out.print("    ");
        for(int i = 0; i < BORDERS.length; i++) {
            System.out.print((i+1));
            int spaces = (i+1) < 10 ? 2 : 1;
            if (spaces > 1)
                System.out.print("  ");
            else
                System.out.print(" ");
        }
        System.out.println();
        for(int i = 0; i < BORDERS.length; i++) {
            System.out.print((i+1));
            int spaces = (i+1) < 10 ? 2 : 1;
            if (spaces > 1)
                System.out.print("   ");
            else
                System.out.print("  ");
            for(int j = 0; j < BORDERS.length; j++) {
                System.out.print(BORDERS[i][j] ? "1" : "0");
                System.out.print("  ");
            }
            System.out.println();
        }
    }


    /**
     * hardCopy: Create a hard copy of a square boolean matrix from src and returns it
     * @param src
     */
    public boolean[][] hardCopy(boolean[][] src) {
        boolean[][] tgt = new boolean[src.length][src.length];
        for(int i = 0; i < src.length; i++) {
            for(int j = 0; j < src.length; j++) {
                tgt[i][j] = src[i][j];
            }
        }
        return tgt;
    }


    /**
     * printConnected: (Part 1) Prints connected components of the subgraph formed by districts and their adjacencies
     * @param line
     */
    public void printConnected(String line) {
        line = line.replaceAll(",", " ");
        Scanner s = new Scanner(line);

        // Parse elements from line into a list array
        List<District> list = new ArrayList<>();
        while(s.hasNextInt())
            list.add(new District(s.nextInt(), -1));


        // Assign groups to each element based on their grouping
        int groups = 0;  // Number of groups
        for(int i = 0; i < list.size(); i++) {
            District d1 = list.get(i);
            if (d1.group > -1)  // If already assigned a group, skip this one
                continue;
            else {
                List<District> d_list  = new ArrayList<>();

                boolean existingGroup = false;
                int group_number = 0;
                for(int j = 0; j < list.size(); j++) {
                    if (i == j)  // Skip when comparing with oneself on the list
                        continue;

                    District d2 = list.get(j);
                    // If they border each other, add them to the same group if there is one
                    if (BORDERS[d1.number - 1][d2.number - 1]) {
                        if (d2.group > -1) {
                            existingGroup = true;
                            if (d2.group > group_number)
                                group_number = d2.group;
                        }
                        d_list.add(d2);
                    }
                }


                // d_list now contains a list of given groups that are connected to it
                if (existingGroup) {
                    // Set all elements of the group to that group number
                    for(int j = 0; j < d_list.size(); j++) {
                        d_list.get(j).group = group_number;
                    }
                    d1.group = group_number;
                } else {
                    // Create a new group and set the groups to that group number
                    for(int j = 0; j < d_list.size(); j++) {
                        d_list.get(j).group = groups;
                    }
                    d1.group = groups;
                    groups++;
                }
            }
        }


        // The original list now has some group number assigned to each district
        // Order the districts into group lists and print out each group at a time
        ArrayList[] group_list = new ArrayList[groups];
        for(int i = 0; i < groups; i++) {
            group_list[i] = new ArrayList<District>();
        }

        for(int i = 0; i < list.size(); i++) {
            District d = list.get(i);
            group_list[d.group].add(d);
        }

        boolean firstGroup = true;
        for(int i = 0; i < groups; i++) {
            List<District> temp = group_list[i];
            if (temp.isEmpty())
                continue;
            else {
                // Print out the group
                if (!firstGroup)
                    System.out.print(",");
                System.out.print("{");
                for(int j = 0; j < temp.size(); j++) {
                    if (j != 0)
                        System.out.print(",");
                    System.out.print(temp.get(j).number);
                }
                System.out.print("}");
                if (firstGroup)
                    firstGroup = false;
            }
        }
    }


    /**
     * Returns an array of neighbors to a district
     */
    public int[] neighbors(int district) {
        int[] buffer = new int[NUM_DISTRICTS];
        int counter = 0;
        for(int i = 0; i < BORDERS.length; i++) {
            if (BORDERS[district - 1][i]) {
                buffer[counter] = (i + 1);
                counter++;
            }
        }

        int[] ret_arr = new int[counter];
        for(int i = 0; i < counter; i++) {
            ret_arr[i] = buffer[i];
        }
        return ret_arr;
    }

    /**
     * Returns an array of neighbors to a group
     */
    public int[] neighbors(List<District> group) {
        Checklist cl = new Checklist();
        int[] temp;
        for(int i = 0; i < group.size(); i++) {
            temp = neighbors(group.get(i).number);
            for(int j = 0; j < temp.length; j++) {
                if ((temp[j] - 1) < 0)
                    break;
                cl.checklist[temp[j] - 1] = true;
            }
        }

        int[] buffer = new int[NUM_DISTRICTS];
        int counter = 0;
        for(int i = 0; i < cl.checklist.length; i++) {
            if (cl.check(i)) {
                buffer[counter] = i + 1;
                counter++;
            }
        }

        int[] ret_arr = new int[counter];
        for(int i = 0; i < counter; i++) {
            ret_arr[i] = buffer[i];
        }
        return ret_arr;
    }
}