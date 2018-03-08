
package travellingbuddy;

import java.util.Comparator;


class MyComparatorY implements Comparator<Points> 
{
    
    @Override
    public int compare(Points p1, Points p2) 
    {
    if (p1.get_y_cor() < p2.get_y_cor()) {
        return -1;
    } 
    return 0;
}

    
}
    
