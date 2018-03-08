package travellingbuddy;

import java.util.ArrayList;

public class R_Candidate1
{
    ArrayList<Integer> r = new ArrayList<Integer>();
    int r_duration;
    int can_id;
    
    public R_Candidate1(ArrayList array1, int d, int c_i)
    {
        r.addAll(array1);
        r_duration = d;
        can_id = c_i;
    }
}
