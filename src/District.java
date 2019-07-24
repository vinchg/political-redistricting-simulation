/**
 * Created by Vince on 12/4/2017.
 */
public class District {
    int number;
    int group;

    public District(int input_number, int input_group) {
        this.number = input_number;
        this.group = input_group;  // -1 if unassigned and > 0 if assigned
    }

    public District(District copy) {
        this.number = copy.number;
        this.group = copy.group;
    }
}
