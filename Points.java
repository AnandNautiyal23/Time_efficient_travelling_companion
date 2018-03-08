package travellingbuddy;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class Points 
{
    double x_cor, y_cor;
    int p_id;
    String tag;
    
    public Points(double p_x, double p_y, int pd, String tt)
    {
        x_cor = p_x;
        y_cor = p_y;
        p_id = pd;
        tag = tt;
    }
    
    public double get_x_cor() 
    {
    	return x_cor;
    }
    
    public double get_y_cor() 
    {
    	return y_cor;
    }
}
