import java.util.Arrays;

public class GreedyTester {
    public static void main(String[] args) {

        //This is the typical kind of input you will be tested with. The format will always be the same
        //Each index represents a single homework. For example, homework zero has weight 23 and deadline t=3.
        int[] weights = new int[]{23, 60, 14, 25, 7, 1};
        int[] deadlines = new int[]{3, 1, 2, 1, 3, 9};
        int m = weights.length;

        //This is the declaration of a schedule of the appropriate size
        HW_Sched schedule = new HW_Sched(weights, deadlines, m);

        //This call organizes the assignments and outputs homeworkPlan
        int[] res = schedule.SelectAssignments();
        System.out.println(Arrays.toString(res));

        weights = new int[]{23, 60, 14, 25, 7, 1, 5};
        deadlines = new int[]{3, 4, 1, 6, 7, 2, 5};
        m = weights.length;

        //This is the declaration of a schedule of the appropriate size
        schedule = new HW_Sched(weights, deadlines, m);

        //This call organizes the assignments and outputs homeworkPlan
        res = schedule.SelectAssignments();
        System.out.println(Arrays.toString(res));
    }

}
