package travellingbuddy;

import java.util.ArrayList;

public class Buddy_Boxes 
{
    ArrayList<PointsBuddy> buddies_in_box = new ArrayList<>();
        
    public Buddy_Boxes(ArrayList<PointsBuddy> bb)
    {
        buddies_in_box.addAll(bb);
    }
}
