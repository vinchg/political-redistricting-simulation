import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Vince on 12/6/2017.
 */
public class State {
    City c;
    ArrayList[] group_list; // State is determined by grouping
    double score;
    final static int MEMBERS = 28;
    static Random r = new Random();


    // Create an initial state and computes the score for it
    public State(City input_city) {
        c = input_city;
        // Create initial state with only 28 members
        initialize();
        score = computeScore();
    }


    //
    public State(City input_city, ArrayList[] input_state) {
        c = input_city;
        group_list = input_state;
        score = computeScore();
    }


    // Generate a random initial state
    private void initialize() {
        // Create initial state with only 28 members
        group_list = new ArrayList[MEMBERS];
        Checklist cl = new Checklist();
        for(int i = 0; i < MEMBERS; i++) {
            group_list[i] = new ArrayList<District>();
            int tgt = cl.getRandom();
            group_list[i].add(new District((tgt + 1), i)); // Add a random district to each group
            cl.checklist[tgt] = true;
        }

        List<District> temp;
        while(true) {
            if (cl.check())
                break;

            for(int i = 0; i < MEMBERS; i++) {
                temp = group_list[i];  // Cycle through each group

                int[] district_neighbors = c.neighbors(temp);
                int[] unpicked_neighbors = unpickedNeighbors(cl, district_neighbors);
                if (unpicked_neighbors == null)
                    continue;

                int picked_district = unpicked_neighbors[r.nextInt(unpicked_neighbors.length)];  // Pick a random unpicked neighbor from that element

                temp.add(new District(picked_district, i));
                cl.checklist[picked_district - 1] = true;
            }
        }
    }





    public ArrayList[] getState() {
        return group_list;
    }

    /**
     * Computes score for the state
     * @return
     */
    private double computeScore() {
        int[] scoreTable = new int[group_list.length];
        for(int i = 0; i < group_list.length; i++) {  // Calculate totals for each group
            int counter = 0;
            List<District> temp = group_list[i];
            for(int j = 0; j < temp.size(); j++) {
                counter += c.POPULATION[temp.get(j).number - 1];  // Get district number (adjust it to index) -> Get Population and add to Counter
            }
            scoreTable[i] = counter;
        }

        // Compute score from scoreTable

        // Calculate mean first
        int counter = 0;
        for(int i = 0; i < scoreTable.length; i++) {
            counter += scoreTable[i];
        }
        double mean = counter / scoreTable.length;

        // Calculate average deviation
        counter = 0;
        for(int i = 0; i < scoreTable.length; i++) {
            counter += Math.pow((mean - scoreTable[i]), 2);
        }
        return counter;
        //double avg_dev = counter / (scoreTable.length + 1);
        //return avg_dev;
    }

    public double computeMean() {
        int[] scoreTable = new int[group_list.length];
        for(int i = 0; i < group_list.length; i++) {  // Calculate totals for each group
            int counter = 0;
            List<District> temp = group_list[i];
            for(int j = 0; j < temp.size(); j++) {
                counter += c.POPULATION[temp.get(j).number - 1];  // Get district number (adjust it to index) -> Get Population and add to Counter
            }
            scoreTable[i] = counter;
        }

        // Compute score from scoreTable

        // Calculate mean first
        int counter = 0;
        for(int i = 0; i < scoreTable.length; i++) {
            counter += scoreTable[i];
        }
        double mean = counter / scoreTable.length;
        return mean;
    }


    /**
     * Generate and return a random state change from a given state
     */
    public ArrayList[] generateState(ArrayList[] input_state) {
        List<District> group = input_state[r.nextInt(MEMBERS)];
        if (group.size() <= 1)
            return generateState(input_state);
        int index = r.nextInt(group.size());
        District tgt = group.get(index);
        District tgt_hc = new District(tgt);

        // Target is chosen
        // Hard copy the old group
        ArrayList[] new_state = hardCopy(input_state);
        // Remove it from the group now
        new_state[tgt.group].remove(index);

        // Add it to another group
        // Choose from possible groups
        int[] neighbors = c.neighbors(tgt_hc.number);
        List<Integer> n_list = new ArrayList<>();
        for(int i = 0; i < neighbors.length; i++) {
            n_list.add(neighbors[i]);
        }
        while (!n_list.isEmpty()) {
            int list_index = r.nextInt(n_list.size());
            int join_district = n_list.get(list_index);
            n_list.remove(list_index);
            int join_group = search(join_district, input_state);
            if (join_group == tgt.group)
                continue;
            else {
                tgt_hc.group = join_group;
                new_state[join_group].add(tgt_hc);
                return new_state;
            }
        }

        return input_state; // failed

        /*int new_group = r.nextInt(MEMBERS);
        while(new_group == tgt.group) {  // If the new chosen group is the same as old, keep rolling
            new_group = r.nextInt(MEMBERS);
        }

        tgt_hc.group = new_group;
        new_state[new_group].add(tgt_hc);*/

    }

    public int search(int district, ArrayList[] input_state) {
        List<District> temp;
        for(int i = 0; i < input_state.length; i++) {
            temp = input_state[i];
            for(int j = 0; j < temp.size(); j++) {
                if (temp.get(j).number == district)
                    return i;
            }
        }
        return -1;
    }


    /**
     * Displays state
     */
    public void printState() {
        int counter = 0;
        System.out.println("Score: " + score);
        for(int i = 0; i < group_list.length; i++) {
            List<District> temp = group_list[i];
            if (temp.size() > 0)
                System.out.print("Group " + i + ": ");
            int pop_counter = 0;
            for(int j = 0; j < temp.size(); j++) {
                System.out.print(temp.get(j).number + " ");
                pop_counter += City.POPULATION[temp.get(j).number - 1];
                counter++;
            }
            System.out.print("Population: " + pop_counter);
            if (temp.size() > 0)
                System.out.println();
        }
        System.out.println("Districts (Check): " + counter);
    }


    public static int[] unpickedNeighbors(Checklist cl, int[] neighbors) {
        int[] buffer = new int[City.NUM_DISTRICTS];
        int counter = 0;
        for(int i = 0; i < neighbors.length; i++) {
            if (!cl.check(neighbors[i] - 1)) {
                buffer[counter] = neighbors[i];
                counter++;
            }
        }
        if (counter == 0)
            return null;

        int[] ret_buf = new int[counter];
        for(int i = 0; i < counter; i++) {
            ret_buf[i] = buffer[i];
        }
        return ret_buf;
    }


    public static ArrayList[] hardCopy(ArrayList[] src) {
        ArrayList[] tgt = new ArrayList[src.length];
        for(int i = 0; i < tgt.length; i++) {
            tgt[i] = new ArrayList<District>();
            List<District> temp = src[i];
            for(int j = 0; j < temp.size(); j++) {
                tgt[i].add(new District(temp.get(j)));
            }
        }
        return tgt;
    }
}
