package travellingbuddy;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Interpolation 
{
    static String arr;
    static double temp_arr[] = new double[3];
    public static void main(String args[])
    {
        Interpolation t = new Interpolation();
        Interpolation.FileRead();
    }
    
    public static void FileRead()
    {
        int file_no = 0;
        List<String> temps = new ArrayList<String>();
        
        FileWriter f1 = null;
        BufferedWriter b1 = null;
        
        while(file_no++ <=10000)
        {            
            try
            {
               
            double g = 36.0;
            String arr_prev = "";
            arr = "";
            Scanner inFile1 = new Scanner(new File("/home/anand/Desktop/T_drive_taxi_dataset/Trajectory/"+file_no+".txt"));
           
            f1 = new FileWriter ("/home/anand/Desktop/anand/"+file_no+".txt", true);
            b1 = new BufferedWriter (f1);
        
            
            int count = 0;
            boolean flag = true;
            int xw = 0;
            double d = 0.0, e = 0.0;
            int break_flag = 0;
            int day = 0;
            String[] str3_prev = new String[3];
            String[] str1_prev = new String[3];
            
            while(inFile1.hasNext())
            {
                
                if(xw > 25)
                {
                    break;
                }
                
                break_flag = 0;
                
                arr_prev = arr;
                arr = "";
                
                arr = inFile1.nextLine();
                
                String[] str = arr.split(",");
                String[] str_prev = arr_prev.split(",");
                
                String[] str1 = str[1].split(" ");
                
                if(count > 0)
                {
                                       
                    str1_prev = str_prev[1].split(" ");
                
                    String[] str3 = str1[0].split("-");
                    str3_prev = str1[0].split("-");
                    String[] str2 = str1[1].split(":");
                    String[] str2_prev = str1_prev[1].split(":");

                    temp_arr[1] = Double.parseDouble(str[2]);
                    
                    temp_arr[2] = Double.parseDouble(str[3]);
                    
                    int hour = Integer.parseInt(str2[0]);
                    temp_arr[0] = hour;

                    day = Integer.parseInt(str3[2]);

                    if(day > 02)
                    {
                        break_flag = 1;
                        break;
                    }
                
                
                if(day == 02)
                {
                    if((hour == 15 || (xw >= 2 && hour > 15)) &&  xw < 25)
                    {
                        count++;
                        int min = Integer.parseInt(str2[1]);
                        
                        if(min >= g || hour > 15)
                        {
                            xw++;
                            
                            double g1 = Double.parseDouble(str2_prev[1]);
                            double g2 = Double.parseDouble(str2[1]);
                            double d1 = Double.parseDouble(str_prev[2]);
                            double d2 = Double.parseDouble(str[2]);
                            double e1 = Double.parseDouble(str_prev[3]);
                            double e2 = Double.parseDouble(str[3]);
			    double g_diff1 = g-g1;
                            double g_diff2 = g2-g1;
                            double d_diff = d2-d1;
                            double e_diff = e2-e1;
                            double we = (g_diff1*d_diff)/g_diff2; 

                            if(we == 0.0)
                            {
                                d = d1 + we;
                            }
                            else 
                            {
                                d = Double.parseDouble(str[2]);
                            }
                                                        
                            double we1 = (g_diff1*e_diff)/g_diff2;

                            if(we1 == 0.0)
                            {
                                e = e1 + we1;
                            }
                            else
                            {
                                e = Double.parseDouble(str[3]);
                            }
                            
                            g = g + 10;
                            
                            if(g > 60)
                            {
                                g = g - 60;
                            }
                            
                            
                            b1.write(d+"");
                            b1.write(",");
                            b1.write(e+"");
                            b1.newLine();
                            
                        }
                      }
                                                

                    
                    else
                    if((hour > 15 && flag) && xw < 25)
                    {
                        double tmp = 0, tmp1=0;
                        double d2 = Double.parseDouble(str[2]);
                        double e2 = Double.parseDouble(str[3]);
                        double d1 = Double.parseDouble(str_prev[2]);
                        double e1 = Double.parseDouble(str_prev[3]);
                                                
                        tmp = (d2-d1)/6;
                        tmp1 = (e2-e1)/6;
                        
                            for(int g1=1+xw;g1<=25;g1++)
                            {
                                xw++;
                                
                                d1 = d1+tmp;
                                e1 = e1+tmp1;
                                
                                d = d1;
                                e = e1;
                                
                                
                                b1.write(d+"");
                                b1.write(",");
                                b1.write(e+"");
                                b1.newLine();
                            
                                System.out.println(d1 + " " + e1);
                            }
                            flag = false;
                    }
                                        
                    temps.add(arr);
                    }
                }
                count++;
                
            }
            
            if(temp_arr[0] < 15 && xw < 25)
            {
            	for(int g1=xw;g1<=25;g1++)
                {
                    
                    d = temp_arr[1];
                    e = temp_arr[2];
                    
                    b1.write(d+"");
                    b1.write(",");
                    b1.write(e+"");
                    b1.newLine();
                            
                    System.out.println(temp_arr[1] + " " + temp_arr[2]);
                }
            }
            
            if(temp_arr[0] >= 15 && xw < 25)
            {
                for(int g1=xw;g1<=25;g1++)
                {
                    
                    d = temp_arr[1];
                    e = temp_arr[2];
                    
                    b1.write(d+"");
                    b1.write(",");
                    b1.write(e+"");
                    b1.newLine();
                            
                    System.out.println(temp_arr[1] + " " + temp_arr[2]);
                }
          }
            
            b1.close();
            f1.close();
        }
        catch(IOException e)
        {
            System.out.println(e);
        }
     }
    }
}
