
package travellingbuddy;

import java.util.ArrayList;

public class Cluster 
{
    ArrayList<Integer> obj_in_c = new ArrayList<>();
    ArrayList<Buddies> buddies_in_c = new ArrayList<>();
        
    public Cluster(ArrayList<Buddies> bb, ArrayList<Integer> ooo)
    {
        buddies_in_c.addAll(bb);
        obj_in_c.addAll(ooo);
    }
}
