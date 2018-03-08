package travellingbuddy;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import static travellingbuddy.Companion_discovery.buddy_id_in_cluster;
import static travellingbuddy.Companion_discovery.snapshot;

public class Grid1 
{
    static double distance_thres, buddy_radius, duration_threshold;
    static int size_threshold;
    static int n;
    static int snapshot = 1;
    static int minPts;
    static int nRows, nCols;
    static int fname = 1;
    static String arr; 
    static String distance[][]= new String[10000][2];
    static int line_to_read = 1;
    static double cell_Width;
    static ArrayList<Points> P = new ArrayList<>();
    static ArrayList<ArrayList<Points>> G = new ArrayList<>();
    static ArrayList<Buddy_Boxes> cluster = new ArrayList<>();
    static ArrayList<Buddy_Boxes> Grid_buddy = new ArrayList<>();  
    static int buddy_id_in_cluster = 0;
    static ArrayList<Buddy_Index1> BI = new ArrayList<>();
    static int bid_in_cluster = 0;
    static ArrayList<R_Candidate1> R_c = new ArrayList<>();

    public static void main(String args[])
    {
        Grid1 a = new Grid1();
        
        a.perform();
    }
    
    public static void input1()
    {
        //System.out.println("Enter the no of points P");
        /*Scanner in = new Scanner(System.in);
        n = in.nextInt();*/
        n = 500;
        /*System.out.println("Enter the distance threshold(epsilon)");
        Scanner di_th1 = new Scanner(System.in);
        distance_thres = di_th1.nextDouble();*/
        distance_thres = 1;
        /*System.out.println("Enter the minPts");
        Scanner m_pt = new Scanner(System.in);
        minPts = m_pt.nextInt();*/
        minPts = 10;
        /*System.out.println("Enter the size_threshold");
        Scanner s_th = new Scanner(System.in);
        size_threshold = s_th.nextInt();*/
        size_threshold = 10;
        /*System.out.println("Enter the duration_threshold");
        Scanner du_th = new Scanner(System.in);
        duration_threshold = du_th.nextDouble();*/
        duration_threshold = 50;
        /*System.out.println("Enter the Buddy radius threshold");
        Scanner bud_rad = new Scanner(System.in);
        buddy_radius = bud_rad.nextDouble();*/
        buddy_radius = 0.2;
    }
    
    public static void initialize()
    {
        fname = 1;
        G.clear();
    }
    public static void perform()
    {
        double sum = 0, avg = 0;
        long startTime = 0, endTime = 0, totalTime = 0;
        
        startTime = System.currentTimeMillis();
        
        for(int snapshot = 1; snapshot <= 25; snapshot++)
        {
            
            
           int snap_duration = 10;
           int candidate_id = 0;
           
           ArrayList<R_Candidate_bar1> R_bar = new ArrayList<>();
           ArrayList<Buddy_Boxes> buddy_clusters = new ArrayList<>();
           ArrayList<ArrayList<Integer>> buddy_clusters_id = new ArrayList<>();
            
           initialize();
           
            if(snapshot == 1)
            {
               input1();
            }
            
            System.out.println();
            System.out.println("SNAPSHOT # "+snapshot);
            
            buddy_id_in_cluster = 0;
            
            constructBoxes();
                        
            DetermineCorePoints();
            
            PointBuddy_constr();
                       
            cluster.clear();
            
            merge();
           
            int cluster_no = 0;
                
            Grid_buddy.clear();
            
            for(int i = 0; i < cluster.size(); i++)
            {
                 buddy_clusters.add(cluster.get(i));
            }
            
            //to update buddy index for first snapshot
            
            if(snapshot == 1)
           {
               for(int i = 0; i < buddy_clusters.size(); i++)
               {
                   ArrayList<Integer> can_list = new ArrayList<>();
                   can_list.add(candidate_id);
                   for(int j = 0; j < buddy_clusters.get(i).buddies_in_box.size(); j++)
                   {
                       BI.add(new Buddy_Index1(bid_in_cluster++, buddy_clusters.get(i).buddies_in_box.get(j).p_bu, can_list));
                   }
               }
           }
           
           if(snapshot > 1)
           {
               
               //to check if the buddy is valid
                for(int i = 0; i < BI.size(); i++)
                {
                    ArrayList<Integer> cluster_valid1 = new ArrayList<>();
                    
                    for(int i1 = 0; i1 < BI.get(i).Obj_Set.size(); i1++)
                    {
                        cluster_valid1.add(BI.get(i).Obj_Set.get(i1).p_id);
                    }
                    
                    int validity_check = 0;
                    int flag_check = 0;
               
                    abcd:
                    for(int j = 0; j < buddy_clusters.size(); j++)
                    {
                        if(validity_check > 0)
                            break;
                   
                        for(int k = 0; k < buddy_clusters.get(j).buddies_in_box.size(); k++)
                        {
                            flag_check = 0;
                    
                            ArrayList<Integer> cluster_valid2 = new ArrayList<>();
                        
                            for(int i1 = 0; i1 < buddy_clusters.get(j).buddies_in_box.get(k).p_bu.size(); i1++)
                            {
                                cluster_valid2.add(buddy_clusters.get(j).buddies_in_box.get(k).p_bu.get(i1).p_id);
                            }
                            
                            if(cluster_valid1.containsAll(cluster_valid2) && cluster_valid2.containsAll(cluster_valid1))
                            {
                                validity_check++;
                                break abcd;
                            }
                            flag_check = 1;
                        }
                    }
                    
                    //if buddy is no longer valid
                    if(flag_check == 1)
                    {
                        int temp_bid = 0;
                        temp_bid = BI.get(i).buddy_id;
                        
                        ArrayList<Integer> temp_can_id = new ArrayList<>();
                        temp_can_id.addAll(BI.get(i).CanIDs);
                        ArrayList<Integer> temp_obj_set = new ArrayList<>();
                        
                        for(int rt = 0; rt < BI.get(i).Obj_Set.size(); rt++)
                        {
                            temp_obj_set.add(BI.get(i).Obj_Set.get(rt).p_id);
                        }
                       
                        
                        for(int r1 = 0; r1 < temp_can_id.size(); r1++)
                        {
                            
                        for(int s = 0; s < R_c.size(); s++)
                        {
                            if(temp_can_id.get(r1) == R_c.get(s).can_id)
                            {
                                for(int t = 0; t < R_c.get(s).r.size(); t++)
                                {
                                   if(R_c.get(s).r.get(t) == temp_bid)
                                   {
                                      R_c.get(s).r.remove(R_c.get(s).r.get(t));
                                   }    
                                }
                            }
                        }
                    }
                    BI.remove(i);
                    i--;    
                    }
                }                
                
                //to add new buddies to Buddy Index

                for(int i = 0; i < buddy_clusters.size(); i++)
                {
                    for(int j = 0; j < buddy_clusters.get(i).buddies_in_box.size(); j++)
                    {
                        ArrayList<Integer> temp_add = new ArrayList<>();
                        
                        for(int rt = 0; rt < buddy_clusters.get(i).buddies_in_box.get(j).p_bu.size(); rt++)
                        {
                            temp_add.add(buddy_clusters.get(i).buddies_in_box.get(j).p_bu.get(rt).p_id);
                        }
                                               
                        int new_buddy_check = 0;
                        for(int k = 0; k < BI.size(); k++)
                        {
                            ArrayList<Integer> temp_add1 = new ArrayList<>();
                            for(int rt = 0; rt < BI.get(k).Obj_Set.size(); rt++)
                            {
                                temp_add1.add(BI.get(k).Obj_Set.get(rt).p_id);
                            }
                            
                            if(temp_add1.containsAll(temp_add))
                             {
                                 new_buddy_check = 0;
                                 break ;
                             }
                             else
                             {
                                 new_buddy_check = 1;
                             }
                        }

                        if(new_buddy_check == 1)
                        {
                            ArrayList<Integer> new_can_id = new ArrayList<>();
                            BI.add(new Buddy_Index1(bid_in_cluster++, buddy_clusters.get(i).buddies_in_box.get(j).p_bu, new_can_id));
                        }
                    }
                }
            }
            
                  
           //to update buddy ids in buddy_cluster for carrying out intersection
           for(int i = 0; i < buddy_clusters.size(); i++)
           {
               ArrayList <Integer> bids_as_cluster = new ArrayList<>();
               
               for(int j = 0; j < buddy_clusters.get(i).buddies_in_box.size(); j++)
               {
                   ArrayList<Integer> temp_upd = new ArrayList<>();
                        
                    for(int rt = 0; rt < buddy_clusters.get(i).buddies_in_box.get(j).p_bu.size(); rt++)
                    {
                        temp_upd.add(buddy_clusters.get(i).buddies_in_box.get(j).p_bu.get(rt).p_id);
                    }
                   
                   for(int k = 0; k < BI.size(); k++)
                   {
                       ArrayList<Integer> temp_upd1 = new ArrayList<>();
                        
                       for(int rt = 0; rt < BI.get(k).Obj_Set.size(); rt++)
                       {
                           temp_upd1.add(BI.get(k).Obj_Set.get(rt).p_id);
                       }
                       
                       if(temp_upd.containsAll(temp_upd1))
                       {
                           buddy_clusters.get(i).buddies_in_box.get(j).b_id = BI.get(k).buddy_id;
                           bids_as_cluster.add(BI.get(k).buddy_id);
                       }
                   }
               }
               buddy_clusters_id.add(bids_as_cluster);
           }
                                
                      
            //intersection code using retainAll
            
            ArrayList <Integer> res = new ArrayList<>();
            ArrayList <Integer> res1 = new ArrayList<>();
            
            for(int i=0; i<R_c.size(); i++)
            {
               ArrayList<Integer> temp1 = new ArrayList<>();
               
               for(int cc = 0; cc < R_c.get(i).r.size(); cc++)
               {
                    temp1.add(R_c.get(i).r.get(cc));
               }
               
               
               if(temp1.size()  < size_threshold)
               {
                    break;
               }
              
               for(int j=0; j<buddy_clusters.size(); j++)
               {
                    ArrayList<Integer> new_candidate_buddies = new ArrayList<>();
                   
                    ArrayList<Integer> temp2 = new ArrayList<>();
                    
                    for(int cc = 0; cc < buddy_clusters.get(j).buddies_in_box.size(); cc++)
                    {
                        temp2.add(buddy_clusters.get(j).buddies_in_box.get(cc).b_id);
                    }

                   //intersecting buddies of a cluster
                   res.addAll(temp1);
                   temp1.retainAll(temp2);
                   
                   for(int k = 0; k < temp1.size(); k++)
                   {
                        new_candidate_buddies.add(temp1.get(k));
                   }
                  
                   res1.clear();
                   
                   temp1.clear();
                   temp1.addAll(res);
                   res.clear();
                    
                   int dur = R_c.get(i).r_duration + snap_duration;
                  
                   //to find out the qualified companion
                   if(new_candidate_buddies.size() >= size_threshold)
                   {
                       R_bar.add(new R_Candidate_bar1 (new_candidate_buddies, dur, candidate_id++));
                      
                       if(dur >= duration_threshold)
                       {
                           System.out.println("The qualified companion is found out and it is");
                            
                            for(int l=0; l<new_candidate_buddies.size(); l++)
                            {
                                for(int l1 = 0; l1 < BI.size(); l1++)
                                {
                                    if(new_candidate_buddies.get(l) == BI.get(l1).buddy_id)
                                    {
                                        for(int l2 = 0; l2 < BI.get(l1).Obj_Set.size(); l2++)
                                        {    
                                            System.out.println(BI.get(l1).Obj_Set.get(l2).p_id);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
             
            //code for closed candidate
                        
            ArrayList<R_Candidate_bar1> new_candidate1 = new ArrayList<>();
             
            if(snapshot > 1)
            {
               for(int i=0; i<buddy_clusters.size(); i++)
               {
                  ArrayList<Integer> check1_buddies = new ArrayList<>();
                  
                  for(int rr = 0; rr < buddy_clusters.get(i).buddies_in_box.size(); rr++)
                  {   
                     check1_buddies.add(buddy_clusters.get(i).buddies_in_box.get(rr).b_id);
                  }
                  
                  int flag1 = 0;
                    
                    for(int j=0; j<R_bar.size(); j++)
                    {
                        ArrayList<Integer> check2_buddies = new ArrayList<>();
                      
                        for(int rr = 0; rr < R_bar.get(j).r_bar.size(); rr++)
                        {   
                             check2_buddies.add(R_bar.get(j).r_bar.get(rr));
                        }

                        for(int k=0; k<check1_buddies.size(); k++)
                        {
                             if(check2_buddies.indexOf(check1_buddies.get(k))==-1)
                             {
                                 flag1++;
                                 break;
                             }
                        }

                    }

                       if(check1_buddies.size()  >= size_threshold && flag1==R_bar.size())
                        {
                            new_candidate1.add(new R_Candidate_bar1(check1_buddies, snap_duration, candidate_id++));
                        }
                        flag1=0;
                }
            }//end of closed candidate
             
            
             for(int v=0; v<new_candidate1.size(); v++)
             {
                R_bar.add(new_candidate1.get(v));
             }
             
             new_candidate1.clear();
             
                                  
            //to add the initial clusters as candidates
            
            if(snapshot == 1)  
            {
               for(int i=0; i<buddy_clusters.size(); i++)
               {
                  ArrayList<Integer> first_buddies = new ArrayList<>();
                  ArrayList<Integer> first_objects = new ArrayList<>();
                   
                  for(int rr = 0; rr < buddy_clusters.get(i).buddies_in_box.size(); rr++)
                  {   
                     first_buddies.add(buddy_clusters.get(i).buddies_in_box.get(rr).b_id);
                  }
                  
                  R_bar.add(new R_Candidate_bar1(first_buddies, snap_duration, candidate_id));
                    candidate_id++;
                }
            }
             
            R_c.clear();
                      
            for(int x =0; x<R_bar.size(); x++)
            {
                R_c.add(new R_Candidate1(R_bar.get(x).r_bar, R_bar.get(x).r_bar_duration, R_bar.get(x).can_id_bar));
            }
             
            
            //to update can_ids in buddy index
             
            for(int i = 0; i < buddy_clusters.size(); i++)
            {
                for(int j = 0; j < buddy_clusters.get(i).buddies_in_box.size(); j++)
                {
                   int temp_id = buddy_clusters.get(i).buddies_in_box.get(j).b_id;
                                       
                   ArrayList<Integer> can_list = new ArrayList<>();
                    
                   for(int k = 0; k < R_c.size(); k++)
                   {
                       for(int l = 0; l < R_c.get(k).r.size(); l++)
                       {
                           int idd = R_c.get(k).r.get(l);
                           if(idd == temp_id)
                            {
                                 can_list.add(R_c.get(k).can_id);
                            }
                        }
                    }
                    
                    for(int s = 0; s < BI.size(); s++)
                    {
                        if(BI.get(s).buddy_id == temp_id)
                        {
                            BI.get(s).CanIDs.clear();
                            
                            for(int s1 = 0; s1 < can_list.size(); s1++)
                            {
                                BI.get(s).CanIDs.add(can_list.get(s1));
                            }
                        }
                    }
                }
            }
            
            R_bar.clear();
            
        }
        endTime   = System.currentTimeMillis();
        totalTime = endTime - startTime;
        System.out.println("Total time is = "+totalTime);
    }
    
     
    public static void constructBoxes()
    {
        cell_Width = (distance_thres/Math.sqrt(2));
               
        euclidean();
        
        ArrayList<Points> P = new ArrayList<>(); 
        
        for(int y = 0; y < n; y++)
        {
            P.add(new Points(Double.parseDouble(distance[y][0]), Double.parseDouble(distance[y][1]), y, "no tag"));
        }
       
        //sort P in x co-ordinates
        Collections.sort(P, new MyComparator());
        
        ArrayList<Points> strip = new ArrayList<>();
       
        int strip_count = 0;
        int j_count = 1;
        
       for(int i = 0; i < P.size(); i++)
       {
           strip.add(new Points(P.get(i).x_cor, P.get(i).y_cor, P.get(i).p_id, P.get(i).tag));
           
           int j_flag = 0;
           for(int j = i+1; j<P.size(); j++)
           {
               j_count++;
               j_flag = 1;
                              
               if(P.get(j).x_cor > (P.get(i).x_cor + cell_Width))
                {
                    ArrayList<Points> strip_temp = new ArrayList<>();
                    strip_temp.addAll(strip);
                    
                    strip_count+= strip_temp.size();
                   
                    AddStripToGrid(G, strip_temp, cell_Width);
                    strip.clear();
                    break;
                }
                
                strip.add(new Points(P.get(j).x_cor, P.get(j).y_cor, P.get(j).p_id, P.get(j).tag));
                
                i=j;
           }
           
           if(j_flag == 0 || j_count == P.size())
           {
                ArrayList<Points> strip_temp = new ArrayList<>();
                strip_temp.addAll(strip);

                strip_count+= strip_temp.size();
                
                AddStripToGrid(G, strip_temp, cell_Width);
                
                strip.clear();
           }
       }
    }

    
    public static void AddStripToGrid(ArrayList<ArrayList<Points>> G, ArrayList<Points> strip_temp, double cell_width)
    {

        //Sort strip in y-coordinate
        Collections.sort(strip_temp, new MyComparatorY());
        
        ArrayList<Points> Box = new ArrayList<>(); 
        
        for(int i = 0; i < strip_temp.size(); i++)
        {
            ArrayList<Points> q = new ArrayList<>();
           
            q.add(new Points(strip_temp.get(i).x_cor, strip_temp.get(i).y_cor, strip_temp.get(i).p_id, "no tag"));
         
            Box.add(new Points(q.get(0).x_cor, q.get(0).y_cor, q.get(0).p_id, q.get(0).tag));
           
           
           int break_flag = 0;
           
           bnm:
           for(int j = i+1; j < strip_temp.size(); j++)
           {
               ArrayList<Points> r = new ArrayList<>();
           
               r.add(new Points(strip_temp.get(j).x_cor, strip_temp.get(j).y_cor, strip_temp.get(j).p_id, "no tag"));
         
               double box_dist = (q.get(0).y_cor + cell_Width);
               
               if(r.get(0).y_cor > (q.get(0).y_cor + cell_Width))
                {
                    ArrayList<Points> box_temp = new ArrayList<>();
                    
                    box_temp.addAll(Box);
                   
                    G.add(box_temp);
                 
                    break_flag = 1;
                    Box.clear();
                    
                    break bnm;
                }
                
                Box.add(new Points(r.get(0).x_cor, r.get(0).y_cor, r.get(0).p_id, r.get(0).tag));
           
                i=j;
           }
           
           if(break_flag == 0)
           {
           
                    ArrayList<Points> box_temp = new ArrayList<>();
                    box_temp.addAll(Box);

                    Box.clear();
                    G.add(box_temp);
           }        
        }
    }
    
      
    
    
    public static void DetermineCorePoints()
    {
        for(int a = 0; a < G.size(); a++)
        {
           if(G.get(a).size() > minPts)
           {
               for(int b = 0; b < G.get(a).size(); b++)
               {
                    G.get(a).get(b).tag = "core";
               }
           }
           
           else
           {
               ArrayList<Points> dist_check1 = new ArrayList<>();
               dist_check1 = G.get(a);
               
               for(int b = 0; b < G.size(); b++)
               { 
                   nc :
                   if(a!=b)
                   {
                       int nPoints = 0;
                       ArrayList<Points> dist_check2 = new ArrayList<>();
                       dist_check2 = G.get(b);
                       
                       for(int i = 0; i < dist_check1.size(); i++)
                       {
                           for(int j = 0; j < dist_check2.size(); j++)
                           {
                               double eucd_dist = Distance(dist_check1.get(i), dist_check2.get(j));
                               
                               if(eucd_dist > distance_thres)
                               {
                                   nPoints++;
                                   if(nPoints >= minPts)
                                   {
                                       G.get(a).get(i).tag = "core";
                                       break nc;
                                   }
                               }
                            }
                            if(nPoints >= minPts)
                                break;
                        }
                   }
               }
           }
        }
    }
   
    public static double Distance(Points p1, Points p2)
    {
        double distX = p2.x_cor - p1.x_cor;
        double distY = p2.y_cor - p1.y_cor;

        double euc_dist = Math.sqrt((Math.pow(distX,2) + Math.pow(distY,2)));

        return euc_dist;
    }

    
    public static void PointBuddy_constr()
    {
        for(int i = 0; i < G.size(); i++)
        {
            int visited1[] = new int[G.get(i).size()];
                
            for(int kl = 0; kl < G.get(i).size(); kl++)
            {
                visited1[kl] = 0;
            }
            
            ArrayList<PointsBuddy> buddies_in_box = new ArrayList<>();
                        
            for(int j = 0; j < G.get(i).size(); j++)
            {
                ArrayList<Points> buddy = new ArrayList<>(); 
                           
                if(visited1[j] != 1)
                {
                    visited1[j] = 1;
                    buddy.add(new Points(G.get(i).get(j).x_cor, G.get(i).get(j).y_cor, G.get(i).get(j).p_id, G.get(i).get(j).tag));
                
                    int k_flag = 0;
                               
                    for(int k = j+1; k < G.get(i).size(); k++)
                    {
                        k_flag = 1;

                        if(visited1[k] != 1)
                        {
                            double eucd_dist_p = Distance(G.get(i).get(j), G.get(i).get(k));

                            if(eucd_dist_p < buddy_radius)
                            {
                                buddy.add(new Points(G.get(i).get(k).x_cor, G.get(i).get(k).y_cor, G.get(i).get(k).p_id, G.get(i).get(k).tag));
                                visited1[k] = 1;
                            }
                        }
                    }
                }
                
                if(buddy.size() > 0)
                {
                    buddies_in_box.add(new PointsBuddy(buddy,0));
                }
            }
            
            Grid_buddy.add(new Buddy_Boxes(buddies_in_box));
        }
    }
    
          
    static int cluster_count = 0;
    
    public static void merge()
    {
        int [] visited = new int[Grid_buddy.size()];
            
        for(int kl = 0; kl < Grid_buddy.size(); kl++)
        {
            visited[kl] = 0;
        }
    
        for(int i = 0; i < Grid_buddy.size(); i++)
        {
            if(visited[i] == 0)
            {
                ArrayList<PointsBuddy> merged_boxes = new ArrayList<>();

                merged_boxes.addAll(Grid_buddy.get(i).buddies_in_box);

                cluster_count+= merged_boxes.size();

                visited[i] = 1;

                cvb:
                for(int j = i+1; j < Grid_buddy.size();j++)
                {
                  if(visited[j] == 0)
                  {
                    for(int k = 0; k < Grid_buddy.get(i).buddies_in_box.size(); k++)
                    {
                        for(int k1 = 0; k1 < Grid_buddy.get(i).buddies_in_box.get(k).p_bu.size(); k1++)
                        {
                            if(Grid_buddy.get(i).buddies_in_box.get(k).p_bu.get(k1).tag.equals("core"))
                            {
                                for(int l = 0; l < Grid_buddy.get(j).buddies_in_box.size(); l++)
                                {
                                    for(int l1 = 0; l1 < Grid_buddy.get(j).buddies_in_box.get(l).p_bu.size(); l1++)
                                    {
                                        if(Grid_buddy.get(j).buddies_in_box.get(l).p_bu.get(l1).tag.equals("core"))
                                        {
                                            int count_x = 0, count_y = 0;
                                            for(int xc = 0; xc < Grid_buddy.get(i).buddies_in_box.get(k).p_bu.size(); xc++)
                                            {
                                                count_x++;
                                            }
                                            
                                            for(int xc = 0; xc < Grid_buddy.get(j).buddies_in_box.get(l).p_bu.size(); xc++)
                                            {
                                                count_y++;
                                            }
                                            double point_dist = Distance(Grid_buddy.get(i).buddies_in_box.get(k).p_bu.get(k1), Grid_buddy.get(j).buddies_in_box.get(l).p_bu.get(l1));

                                            if(point_dist <= distance_thres)
                                            {
                                                merged_boxes.addAll(Grid_buddy.get(j).buddies_in_box);

                                                cluster_count+= merged_boxes.size();

                                                visited[j] = 1;
                                                break cvb;
                                            }
                                        }  
                                    }
                                }
                            }
                        }
                    }
                  }
                }
                cluster.add(new Buddy_Boxes(merged_boxes));
            }
        }
    }
      

     
   public static String OpenFile() throws IOException
   {
        Scanner inFile1 = new Scanner(new File("/home/anand/Desktop/anand/"+fname+".txt"));
        
            for(int df = 0; df < line_to_read; df++)
            {
                arr = inFile1.nextLine();
            }
        
            inFile1.close();
        
         return arr;
    }
    
    public static void euclidean()
    {
        String arr[];
        int k, j, s;
           
        try
        {
            for(j = 0; j<n; j++)
            {
                String arr1 = OpenFile(); 
                
                String[] temp_arr1 = arr1.split(",");
                s=0;
                   
                for(k=0; k<2; k++)
                {
                    if(s == temp_arr1.length)
                       break;
                  
                    distance[j][k] = temp_arr1[s]; // to store the data as 2D array
                    s++;
                }
                fname++;
            }
            
            line_to_read++;
        }
       
        catch(IOException e)
        {
            System.out.println("The exception is "+e);
        }
    }
}
