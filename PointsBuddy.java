package travellingbuddy;

import java.util.ArrayList;

public class PointsBuddy 
{
    ArrayList<Points> p_bu = new ArrayList<Points>();
    int b_id;
    
    public PointsBuddy(ArrayList array, int id)
    {
      	p_bu.addAll(array);
        b_id = id;
    }
}
