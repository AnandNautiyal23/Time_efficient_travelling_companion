package travellingbuddy;

import java.util.ArrayList;

public class R_Candidate_bar1
{
   ArrayList<Integer> r_bar = new ArrayList<Integer>();
    int r_bar_duration;
    int can_id_bar;
    
    public R_Candidate_bar1(ArrayList array2, int r_d, int c_i_b)
    {
        r_bar.addAll(array2);
        r_bar_duration = r_d;
        can_id_bar = c_i_b;
    }
}
