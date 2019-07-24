import java.util.Random;

/**
 * Created by Vince on 12/6/2017.
 */
public class Checklist {
    boolean[] checklist = new boolean[City.NUM_DISTRICTS];


    /**
     * Returns true if all items are true. Else returns false.
     * @return
     */
    public boolean check() {
        for(int i = 0; i < City.NUM_DISTRICTS; i++) {
            if (!checklist[i])
                return false;
        }
        return true;
    }

    public boolean check(int district) {
        return checklist[district];
    }

    public int getRandom() {
        Random r = new Random();
        if (numAvail() == 0)
            return -1;
        int tgt = r.nextInt(numAvail());
        int counter = 0;
        for(int i = 0; i < checklist.length; i++) {
            if (!checklist[i])
                counter++;
            if (counter > tgt)
                return i;
        }
        return -1;  // Something went wrong
    }

    public int numAvail() {
        int counter = 0;
        for(int i = 0; i < checklist.length; i++) {
            if (!checklist[i])
                counter++;
        }
        return counter;
    }
}
