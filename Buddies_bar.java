package travellingbuddy;

import java.util.ArrayList;

public class Buddies_bar
{
    ArrayList<Integer> bu_bar = new ArrayList<Integer>();
    int b_id_bar;
    double b_center_bar_x;
    double b_center_bar_y;
   
    public Buddies_bar(ArrayList array1, int id_bar, double x1, double y1)
    {
        bu_bar.addAll(array1);
        b_id_bar = id_bar;
        b_center_bar_x = x1;
        b_center_bar_y = y1;
    }
}
