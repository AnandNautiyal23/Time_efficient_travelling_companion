package travellingbuddy;

import java.util.ArrayList;

public class Buddies 
{
    ArrayList<Integer> bu = new ArrayList<Integer>();
    int b_id;
    double b_center_x;
    double b_center_y;
    
    public Buddies(ArrayList array, int id, double x, double y)
    {
        bu.addAll(array);
        b_id = id;
        b_center_x = x;
        b_center_y = y;
    }
}
