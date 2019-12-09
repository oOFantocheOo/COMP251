import java.util.*;

class Assignment implements Comparator<Assignment> {
    int number;
    int weight;
    int deadline;


    protected Assignment() {
    }

    protected Assignment(int number, int weight, int deadline) {
        this.number = number;
        this.weight = weight;
        this.deadline = deadline;
    }


    /**
     * This method is used to sort to compare assignment objects for sorting.
     * Return -1 if a1 > a2
     * Return 1 if a1 < a2
     * Return 0 if a1 = a2
     */
    @Override
    public int compare(Assignment a1, Assignment a2) {
        // TODO Implement this
        if (a1.deadline < a2.deadline)
            return 1;
        if (a1.deadline > a2.deadline)
            return -1;
        return 0;
    }
}

public class HW_Sched {
    ArrayList<Assignment> Assignments = new ArrayList<Assignment>();
    int m;
    int lastDeadline = 0;

    protected HW_Sched(int[] weights, int[] deadlines, int size) {
        for (int i = 0; i < size; i++) {
            Assignment homework = new Assignment(i, weights[i], deadlines[i]);
            this.Assignments.add(homework);
            if (homework.deadline > lastDeadline) {
                lastDeadline = homework.deadline;
            }
        }
        m = size;
    }


    /**
     * @return Array where output[i] corresponds to the assignment
     * that will be done at time i.
     */
    public int[] SelectAssignments() {
        //TODO Implement this


        //Sort assignments
        //Order will depend on how compare function is implemented
        Collections.sort(Assignments, new Assignment());
        // If schedule[i] has a value -1, it indicates that the
        // i'th timeslot in the schedule is empty
        int[] homeworkPlan = new int[lastDeadline];
        for (int i = 0; i < homeworkPlan.length; ++i) {
            homeworkPlan[i] = -1;
        }
        for (int i = homeworkPlan.length - 1; i >= 0; i--) { //Select each hour's homework from the last hour to the first hour
            int startIdx = -1;
            int endIdx = -1;// [startIdx, endIdx] is the range to be selected from, since at the i-th hour, we can choose all homeworks whose due hour is greater than i
            int maxIdx = -1;
            int maxWeight = 0;
            for (int cur = 0; cur < Assignments.size(); cur++) {//update the startIdx endIdx
                if (cur == 0 && Assignments.get(cur).deadline < i + 1)//if cannot start -- nothing to select during that hour
                    break;
                else
                    startIdx = 0;
                if (Assignments.get(cur).deadline >= i + 1)
                    endIdx = cur;
            }
            if (startIdx == -1)//if cannot start -- startIdx not modified -- nothing to select during that hour
                continue;// continue to next hour
            for (int cur = startIdx; cur <= endIdx; cur++) {// in the selectable homeworks, choose the one with greatest weight
                if (!Assignments.isEmpty() && Assignments.get(cur).weight > maxWeight) {
                    maxIdx = cur;
                    maxWeight = Assignments.get(cur).weight;
                }
            }

            if (maxIdx != -1)//unless nothing is selected
            {
                homeworkPlan[i] = Assignments.get(maxIdx).number;//record the homework selected
                Assignments.remove(maxIdx);//delete it from all assignments
            }
        }

        return homeworkPlan;
    }
}




