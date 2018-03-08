package travellingbuddy;

import java.util.ArrayList;

public class Buddy_Index1
{
     int buddy_id;
     ArrayList<Points> Obj_Set = new ArrayList<Points>();
     ArrayList<Integer> CanIDs = new ArrayList<Integer>();
     
    public Buddy_Index1(int b_id, ArrayList <Points> Ob_st, ArrayList Ca_id)
    {
        buddy_id = b_id;
        Obj_Set.addAll(Ob_st);
        CanIDs.addAll(Ca_id);
        
    }
}

