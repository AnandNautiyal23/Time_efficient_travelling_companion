
package travellingbuddy;

import java.util.Comparator;


class MyComparator implements Comparator<Points> 
{
    
    @Override
    public int compare(Points p1, Points p2) 
    {
    if (p1.get_x_cor() < p2.get_x_cor()) {
        return -1;
    } 
    return 0;
}

    
}
    
