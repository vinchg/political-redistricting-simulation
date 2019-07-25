import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Vince on 12/6/2017.
 */
public class Anneal {

    public Anneal() {
        execute();
    }

    public static void execute() {
        // exp (( solution - neighbor) / temperature)
        Random r = new Random();
        double temperature = 200000000;
        double cooling_rate = 0.000003;
        City c = new City();
        State s = new State(c);
        State s_new;
        State best = new State(c, State.hardCopy(s.group_list));
        ArrayList[] best_list = State.hardCopy(s.group_list);
        double best_score = s.score;


        // Set temperature
        // create random initial solution
        // loop until reaches certain temperature or good enough solution
        // Make small/random change to current solution
        // Decide whether to move to solution
        // Decrease temperature and continue looping
        // Group a solution
        //

        int counter = 0;
        while (temperature > 1) {
            s_new = new State(c, s.generateState(s.group_list));

            if (Math.random() <= acceptanceProbability(s.score, s_new.score, temperature)) {
                s = s_new;
            }

            // Keep track of best solution
            if (s.score < best_score) {
                best_list = State.hardCopy(s.group_list);
                best_score = s.score;
            }
            //if (s.score < best.score)
            //    best = new State(c, State.hardCopy(s.group_list));

            temperature *= (1 - cooling_rate);
            counter++;

            if (s.score < 750000.0)
                break;
        }

        System.out.println("Iterations: " + counter);
        System.out.println("<--Best solution-->");
        best = new State(c, best_list);
        best.printState();
        System.out.println("Mean: " + best.computeMean());
    }

    private static double acceptanceProbability(double score, double new_score, double temperature) {
        if (new_score > score)
            return 1;

        return Math.exp((score - new_score) / temperature);
    }
}
