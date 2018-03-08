package travellingbuddy;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Companion_discovery 
{
    //declaration and initialization of variables used
    	
    static String arr;
    static int fname = 1; 
    static int n, density_threshold, size_threshold, duration_threshold;
    static double distance_threshold, radius_threshold;
    static double eucd[][] = new double[500][500];
    static int neighbour[][] = new int[500][500];
    static String distance[][]= new String[500][2];
    static String prev_distance[][]= new String[500][2];
    static int snapshot = 1;
    static ArrayList<Buddies> buddy = new ArrayList<>();
    static ArrayList<R_Candidate> R_c = new ArrayList<>();
    static ArrayList<Cluster> C = new ArrayList<>();
    static int buddy_id_in_cluster = 0;
    static int bid_in_cluster = 0;
    static ArrayList<Buddy_Index> BI = new ArrayList<>();
    
    
    public static void input()
    {
        // Input the threshold values from user
        
        System.out.println("Enter the no of objects in the object set");
        Scanner in = new Scanner(System.in);
        n = in.nextInt();
        System.out.println("Enter the distance threshold(epsilon)");
        Scanner di_th = new Scanner(System.in);
        distance_threshold = di_th.nextDouble();
        System.out.println("Enter the density threshold(min_pts)");
        Scanner den_th = new Scanner(System.in);
        density_threshold = den_th.nextInt();
        System.out.println("Enter the size threshold(delta_s)");
        Scanner size_th = new Scanner(System.in);
        size_threshold = size_th.nextInt();
        System.out.println("Enter the duration threshold(delta_s)");
        Scanner duration_th = new Scanner(System.in);
        duration_threshold = duration_th.nextInt();
        System.out.println("Enter the radius threshold");
        Scanner rad_th = new Scanner(System.in);
        radius_threshold = rad_th.nextDouble();
    }
    
   
  //to initialize the arrays by 0 each time   
  
  public static void initialize()
  {
      for(int i=0; i<n; i++)
      {
         for(int j= 0; j<n; j++)
         {
            eucd[i][j] = 0;
            neighbour[i][j] = 0;
         }
      }
  }
    
    //Structure of companion discovery
    
    public static void perform()
    {
       // to store start time of the code
       long startTime = System.currentTimeMillis(); 
        
       for(snapshot = 1; snapshot <= 25; snapshot = snapshot +1)  
       {
           int snap_duration = 10; // to initialize duration of a snapshot
           int candidate_id = 0; // to store the companion candidate ids
           
           //to store the intersected candidates
           ArrayList<R_Candidate_bar> R_bar = new ArrayList<>(); 
           
           //to store the clusters
           ArrayList<Cluster> buddy_clusters = new ArrayList<>();
           
           // to store buddy ids instead of detailed objects 
           ArrayList<ArrayList<Integer>> buddy_clusters_id = new ArrayList<>();
          
            //to take inputs in the first snapshot
            if(snapshot == 1)
            {
               input();
            }
            
            System.out.println();
            System.out.println("SNAPSHOT # "+snapshot);
            
             euclidean(); // to calculate euclidean distance between all pair of objects
             
             buddy_id_in_cluster = 0; // to increment buddy ids
             
             clustering(); // to form clusters of buddies
    
             //copies clusters in C to buddy_clusters
             for(int i = 0; i<C.size(); i++)
             {
                    buddy_clusters.add(C.get(i));
             }
            
             //empties C
             C.clear();
                       
            fname = 2; // to increment filename
            
            initialize(); // initializes various constructs to 0 for different snapshots
           
            
           //to update buddy index for first snapshot
           if(snapshot == 1)
           {
               // to store canIDs in Buddy Index
               ArrayList<Integer> can_list = new ArrayList<>();
               can_list.add(candidate_id);
               
               for(int i = 0; i < buddy_clusters.size(); i++)
               {
                   for(int j = 0; j < buddy_clusters.get(i).buddies_in_c.size(); j++)
                   {
                       BI.add(new Buddy_Index(bid_in_cluster++, buddy_clusters.get(i).buddies_in_c.get(j).bu, can_list));
                   }
               }
               
            }
           
                      
           if(snapshot > 1)
           {
                //to check if the buddy is valid
                for(int i = 0; i < BI.size(); i++)
                {
                    //to store the object set of the i(th) buddy
                    ArrayList<Integer> cluster_valid1 = new ArrayList<>();
                    
                    cluster_valid1 = BI.get(i).Obj_Set;
               
                    int validity_check = 0;
                    int flag_check = 0;
               
                    abcd:
                    for(int j = 0; j < buddy_clusters.size(); j++)
                    {
                        // to break the loop if a buddy is found valid
                        if(validity_check > 0)
                            break;
                   
                        for(int k = 0; k < buddy_clusters.get(j).buddies_in_c.size(); k++)
                        {
                            flag_check = 0;
                    
                            ArrayList<Integer> cluster_valid2 = new ArrayList<>();
                        
                            cluster_valid2 = buddy_clusters.get(j).buddies_in_c.get(k).bu;
                         
                            //both conditions are checked since one can contain same elements as the other and a few more
                            if(cluster_valid1.containsAll(cluster_valid2) && cluster_valid2.containsAll(cluster_valid1))
                            {
                                validity_check++;
                                break abcd;
                            }
                            
                            //flag is set if the buddy is no more valid
                            flag_check = 1;
                        }
                    }
                                
                    //if buddy is no longer valid
                    if(flag_check == 1)
                    {
                        int temp_bid = 0;
                        temp_bid = BI.get(i).buddy_id;
                        
                        //to store canIds of the invalid buddy in a temporary variable
                        ArrayList<Integer> temp_can_id = new ArrayList<>();
                        temp_can_id.addAll(BI.get(i).CanIDs);
                        
                        //to store ObjSet of the invalid buddy in a temporary variable
                        ArrayList<Integer> temp_obj_set = new ArrayList<>();
                        temp_obj_set.addAll(BI.get(i).Obj_Set);
                        
                        //to store the objects of invalid cluster
                        ArrayList<Integer> obj_of_inv_cluster = new ArrayList<>();
                        
                        for(int p = 0; p < temp_obj_set.size(); p++)
                        {
                            for(int q = 0; q < buddy_clusters.size(); q++)
                            {
                                for(int q1 = 0; q1 < buddy_clusters.get(q).obj_in_c.size(); q1++)
                                { 
                                    if(temp_obj_set.get(p) == buddy_clusters.get(q).obj_in_c.get(q1))
                                    {
                                        obj_of_inv_cluster.add(temp_obj_set.get(p));
                                    }
                                }
                            }
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
                                      
                                       if(R_c.get(s).obj_in_candidate.size() > 0)
                                       {
                                        for(int lk = 0; lk < R_c.get(s).obj_in_candidate.size(); lk++)
                                        {
                                           for(int nm = 0; nm < obj_of_inv_cluster.size(); nm++)
                                            {
                                            	if(!(R_c.get(s).obj_in_candidate.contains(obj_of_inv_cluster.get(nm)))) 
                                                {
                                                     R_c.get(s).obj_in_candidate.add(obj_of_inv_cluster.get(nm));
                                                }
                                            }
                                        }
                                       }
                                       else
                                       {
                                           R_c.get(s).obj_in_candidate.addAll(obj_of_inv_cluster);
                                       }
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
                    for(int j = 0; j < buddy_clusters.get(i).buddies_in_c.size(); j++)
                    {
                        int new_buddy_check = 0;
                        for(int k = 0; k < BI.size(); k++)
                        {
                             if(BI.get(k).Obj_Set.containsAll(buddy_clusters.get(i).buddies_in_c.get(j).bu))
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
                            BI.add(new Buddy_Index(bid_in_cluster++, buddy_clusters.get(i).buddies_in_c.get(j).bu, new_can_id));
                        }
                    }
                }
           }
                     
                      
           //to update buddy ids in buddy_cluster for carrying out intersection
           for(int i = 0; i < buddy_clusters.size(); i++)
           {
               //to store the bids in the form of clusters so that detailed objects need not be accessed
               ArrayList <Integer> bids_as_cluster = new ArrayList<>();
               for(int j = 0; j < buddy_clusters.get(i).buddies_in_c.size(); j++)
               {
                   for(int k = 0; k < BI.size(); k++)
                   {
                       //checks if buddies in the new clusters & in Buddy Index are the same
                       //If so, update buddy ids in the new clusters
                       if(buddy_clusters.get(i).buddies_in_c.get(j).bu.containsAll(BI.get(k).Obj_Set))
                       {
                           buddy_clusters.get(i).buddies_in_c.get(j).b_id = BI.get(k).buddy_id;
                           
                           //add the buddy ids so that they form a cluster
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
               //to hold the buddies in companion candidates for intersection
               ArrayList<Integer> temp1 = new ArrayList<>();
               //to hold the objects in companion candidates for intersection
               ArrayList<Integer> obj_intr1 = new ArrayList<>();
               
               for(int cc = 0; cc < R_c.get(i).r.size(); cc++)
               {
                    temp1.add(R_c.get(i).r.get(cc));
               }
               
               for(int cc = 0; cc < R_c.get(i).obj_in_candidate.size(); cc++)
               {
                    obj_intr1.add(R_c.get(i).obj_in_candidate.get(cc));
               }
               
               //checks if size threshold condition is satisfied
               if((temp1.size() + obj_intr1.size()) < size_threshold)
               {
                    break;
               }
              
               
               for(int j=0; j<buddy_clusters.size(); j++)
               {
                    //to sore the intersected buddies
                    ArrayList<Integer> new_candidate_buddies = new ArrayList<>();
                    //to store the intersected objects
                    ArrayList<Integer> new_candidate_objects = new ArrayList<>();

                    //to hold the buddies in buddy_clusters for intersection
                    ArrayList<Integer> temp2 = new ArrayList<>();
                    //to hold the objects in buddy_clusters for intersection
                    ArrayList<Integer> obj_intr2 = new ArrayList<>();

                    for(int cc = 0; cc < buddy_clusters.get(j).buddies_in_c.size(); cc++)
                    {
                        temp2.add(buddy_clusters.get(j).buddies_in_c.get(cc).b_id);
                    }

                    for(int cc = 0; cc < buddy_clusters.get(j).obj_in_c.size(); cc++)
                    {
                        obj_intr2.add(buddy_clusters.get(j).obj_in_c.get(cc));
                    }

                                             
                   //intersecting objects of a cluster
                   res1.addAll(obj_intr1);
                   obj_intr1.retainAll(obj_intr2);
                   
                   //intersecting buddies of a cluster
                   res.addAll(temp1);
                   temp1.retainAll(temp2);
                   
                   for(int k = 0; k < temp1.size(); k++)
                   {
                        new_candidate_buddies.add(temp1.get(k));
                   }
                  
                   for(int k = 0; k < obj_intr1.size(); k++)
                   {
                       new_candidate_objects.add(obj_intr1.get(k));
                   }
                   
                   //to store the original content of obj_intr1
                   obj_intr1.clear();
                   obj_intr1.addAll(res1);
                   res1.clear();
                   
                   //to store the original content of temp1
                   temp1.clear();
                   temp1.addAll(res);
                   res.clear();
                    
                  
                   //to store the incremented time
                   int dur = R_c.get(i).r_duration + snap_duration;
                  
                   //to find out the qualified companion
                   if((new_candidate_buddies.size() + new_candidate_objects.size()) >= size_threshold)
                   {
                       //to store the new candidate as an object in R_bar
                       R_bar.add(new R_Candidate_bar (new_candidate_buddies, dur, candidate_id++, new_candidate_objects));
                      
                       if(dur >= duration_threshold )
                       {
                           System.out.println("The qualified companion is found out and it is");
                            
                            for(int l=0; l<new_candidate_buddies.size(); l++)
                            {
                                for(int l1 = 0; l1 < BI.size(); l1++)
                                {
                                    //if duration is satisfied, buddy ids in new_candidate_buddies
                                    //are compared with bids in Buddy Index  
                                    if(new_candidate_buddies.get(l) == BI.get(l1).buddy_id)
                                    {
                                        //prints the objects of the qualified buddies in clusters
                                        for(int l2 = 0; l2 < BI.get(l1).Obj_Set.size(); l2++)
                                        {    
                                            System.out.println(BI.get(l1).Obj_Set.get(l2));
                                        }
                                    }
                                }
                            }
                            
                            //prints the qualified objects in clusters
                            for(int l=0; l < new_candidate_objects.size(); l++)
                            {
                                System.out.println(new_candidate_objects.get(l));
                            }
                        }
                   }
                }
            }
             
           
            //code for closed candidate
            ArrayList<R_Candidate_bar> new_candidate1 = new ArrayList<>();
             
            if(snapshot > 1)
            {
               for(int i=0; i<buddy_clusters.size(); i++)
               {
                  //to store bids of buddies in buddy_cluster 
                  ArrayList<Integer> check1_buddies = new ArrayList<>();
                  //to store objects of buddy_clusters
                  ArrayList<Integer> check1_objects = new ArrayList<>();
                  
                  //storing bids of buddies in check1_buddies
                  for(int rr = 0; rr < buddy_clusters.get(i).buddies_in_c.size(); rr++)
                  {   
                     check1_buddies.add(buddy_clusters.get(i).buddies_in_c.get(rr).b_id);
                  }
                  
                  //storing objectss of buddy_clusters in check1_objects
                  for(int rr = 0; rr < buddy_clusters.get(i).obj_in_c.size(); rr++)
                  {   
                     check1_objects.add(buddy_clusters.get(i).obj_in_c.get(rr));
                  }
                      
                    int flag1 = 0, flag2 = 0;
                    
                    for(int j=0; j<R_bar.size(); j++)
                    {
                        //to store bids of buddies inside a cluster in R_bar
                        ArrayList<Integer> check2_buddies = new ArrayList<>();
                        //to store objects in a cluster in R_bar 
                        ArrayList<Integer> check2_objects = new ArrayList<>();

                        //storing buddies inside a cluster in check2_buddies
                        for(int rr = 0; rr < R_bar.get(j).r_bar.size(); rr++)
                        {   
                             check2_buddies.add(R_bar.get(j).r_bar.get(rr));
                        }

                        //storing objects inside a cluster in check2_objects
                        for(int rr = 0; rr < R_bar.get(j).obj_in_candidate1.size(); rr++)
                        {   
                             check2_objects.add(R_bar.get(j).obj_in_candidate1.get(rr));
                        }

                        //to check for closed buddies
                        for(int k=0; k<check1_buddies.size(); k++)
                        {
                             if(check2_buddies.indexOf(check1_buddies.get(k))==-1)
                             {
                                 flag1++;
                                 break;
                             }
                        }

                        //to check for closed objects
                        for(int k=0; k<check1_objects.size(); k++)
                        {
                             if(check2_objects.indexOf(check1_objects.get(k))==-1)
                             {
                                 flag2++;
                                 break;
                             }
                        }
                    }

                       if((check1_buddies.size() + check1_objects.size()) >= size_threshold && flag1==R_bar.size() && flag2==R_bar.size())
                        {
                            new_candidate1.add(new R_Candidate_bar(check1_buddies, snap_duration, candidate_id++, check1_objects));
                        }
                        flag1=0;
                        flag2=0;
                }
            }//end of closed candidate
             
            
            
            
            
             //add closed intersected candidate to R_bar
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
                   
                  for(int rr = 0; rr < buddy_clusters.get(i).buddies_in_c.size(); rr++)
                  {   
                     first_buddies.add(buddy_clusters.get(i).buddies_in_c.get(rr).b_id);
                  }
                  
                  for(int rr = 0; rr < buddy_clusters.get(i).obj_in_c.size(); rr++)
                  {   
                     first_objects.add(buddy_clusters.get(i).obj_in_c.get(rr));
                  }
               
                  R_bar.add(new R_Candidate_bar(first_buddies, snap_duration, candidate_id, first_objects));
                    candidate_id++;
                }
            }
             
            R_c.clear();
                      
            // adds R_bar elements to R_c so that R_c can take part in the further processing with the upcoming snapshots
            for(int x =0; x<R_bar.size(); x++)
            {
                R_c.add(new R_Candidate(R_bar.get(x).r_bar, R_bar.get(x).r_bar_duration, R_bar.get(x).can_id_bar, R_bar.get(x).obj_in_candidate1));
            }
             
            
            //to update can_ids in buddy index
            for(int i = 0; i < buddy_clusters.size(); i++)
            {
                for(int j = 0; j < buddy_clusters.get(i).buddies_in_c.size(); j++)
                {
                   int temp_id = buddy_clusters.get(i).buddies_in_c.get(j).b_id;
                                       
                   ArrayList<Integer> can_list = new ArrayList<>();
                    
                   for(int k = 0; k < R_c.size(); k++)
                   {
                       for(int l = 0; l < R_c.get(k).r.size(); l++)
                       {
                            if(R_c.get(k).r.get(l) == temp_id)
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
        }//end of snapshot
    
        long endTime   = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        System.out.println("The time is = "+totalTime);
    
    }
       
    
    //calculates radius of a passed Buddy object
    public static double radius_calc(Buddies b)
    {
    	// stores the x co-ordinate of the center of the buddy object
        double cen_x = b.b_center_x;
        
        // stores the y co-ordinate of the center of the buddy object
        double cen_y = b.b_center_y;
        
        ArrayList<Double> pres_buddy_obj_dist = new ArrayList<>();
        
        for(int i = 0; i<b.bu.size(); i++)
        {
            int b_i = b.bu.get(i);
            
            //calculation of the object's distance from buddy center         
            double b_i_x1 = Math.pow((cen_x - Double.parseDouble(distance[b_i][0])),2);
            double b_i_y1 = Math.pow((cen_y - Double.parseDouble(distance[b_i][1])),2);
            double eucd_b1 = Math.sqrt(b_i_x1 + b_i_y1);
            
            pres_buddy_obj_dist.add(eucd_b1);
        }
        
        //Radius is the distance between the center of the farthest object to the center of the buddy 
        Collections.sort(pres_buddy_obj_dist, Collections.reverseOrder());
        double max ;
        
        max = pres_buddy_obj_dist.get(0);
        
        return max;   //radius is returned as the maximum distance
    }
    
    //calculate radius of a passed Buddy_bar object 
    public static double radius_calc_bar(Buddies_bar b_bar)
    {
        // stores the x co-ordinate of the center of the buddy bar object
        double cen_x = b_bar.b_center_bar_x;
        
        // stores the y co-ordinate of the center of the buddy bar object
        double cen_y = b_bar.b_center_bar_y;
        
        double pres_buddy_obj_dist[] = new double[b_bar.bu_bar.size()];
        
        for(int i = 0; i<b_bar.bu_bar.size(); i++)
        {
            int b_i = b_bar.bu_bar.get(i);
            
            //calculation of the object's distance from buddy bar center
            double b_i_x1 = Math.pow(cen_x - Double.parseDouble(distance[b_i][0]),2);
            double b_i_y1 = Math.pow((cen_y - Double.parseDouble(distance[b_i][1])),2);
            double eucd_b1 = Math.sqrt(b_i_x1 + b_i_y1);
            
            pres_buddy_obj_dist[i] = eucd_b1;
        }
        
        //Radius is the distance between the center of the farthest object to the center of the buddy bar
        double max = pres_buddy_obj_dist[0];
        
        for(int i =0; i < b_bar.bu_bar.size(); i++)
        {
            if(max < pres_buddy_obj_dist[i])
            {
                max = pres_buddy_obj_dist[i];
            }
        }
        return max; //radius is returned as the maximum distance
    }
    
    
    //calculates distance between the center of two Buddies
    public static double center_dist_calc(double x1, double y1, double x2, double y2)
    {
        double diff_bud_x = Math.pow(Math.abs(x2 - x1),2);
        double diff_bud_y = Math.pow(Math.abs(y2 - y1),2);
        double eucd_cen = Math.sqrt(diff_bud_x + diff_bud_y);
              
        return eucd_cen;
    }
    
    //calculates distance between two buddies
    public static double dist_calc(int o_k, int o_l)
    {
        double diff_x = Math.pow(Math.abs(Double.parseDouble(distance[o_k][0]) - Double.parseDouble(distance[o_l][0])),2);
        double diff_y = Math.pow(Math.abs(Double.parseDouble(distance[o_k][1]) - Double.parseDouble(distance[o_l][1])),2);
        double dist = Math.sqrt(diff_x + diff_y);
              
        return dist;
    }
    
    //updates the center of a buddy with upcoming snapshots
    public static void update_center(Buddies b)
    {
        double shift_x = 0;
        double shift_y = 0;
        
        for(int j=0; j<b.bu.size(); j++)
        {
            int x = b.bu.get(j);
            
            //object's x co-ordinate in the previous snapshot
            double x_prev = Double.parseDouble(prev_distance[x][0]);
            
            //object's y co-ordinate in the previous snapshot
            double y_prev = Double.parseDouble(prev_distance[x][1]);
            
            //object's x co-ordinate in the present snapshot
            double x_pres = Double.parseDouble(distance[x][0]);
            
            //object's x co-ordinate in the present snapshot
            double y_pres = Double.parseDouble(distance[x][1]);
            
            //calculating object's shift in x co-ordinate
            shift_x = shift_x + Math.abs((x_prev - x_pres));
            
            //calculating object's shift in y co-ordinate
            shift_y = shift_y + Math.abs((y_prev - y_pres));
        }
	
	//shift in object's center in x co-ordinate
        b.b_center_x = b.b_center_x + shift_x;
        
        //shift in object's center in y co-ordinate
        b.b_center_y = b.b_center_y + shift_y;
    }
    
    
    //clusters the buddies in a snapshot
    public static void clustering()
    {
        //stores the buddies of a snapshot
        ArrayList<Buddies> buddy_set_B = new ArrayList<>();
        
        //to calculate if a buddy splits or merges
        split_and_merge();
    
        //adding buddy to buddy_set_B
        for(int i = 0; i<buddy.size(); i++)
        {
            buddy_set_B.add(new Buddies(buddy.get(i).bu, buddy.get(i).b_id, buddy.get(i).b_center_x, buddy.get(i).b_center_y)); 
        }
        
        while(buddy_set_B.size() > 0)
        {
            //to store buddies of a cluster
            ArrayList <Buddies> c = new ArrayList<>();
            //to store the objects of a cluster
            ArrayList <Integer> ob = new ArrayList<>();
            
            //add a buddy to c
            c.add(new Buddies(buddy_set_B.get(0).bu, buddy_id_in_cluster, buddy_set_B.get(0).b_center_x, buddy_set_B.get(0).b_center_y));
            
            //increment the buddy id in a cluster
            buddy_id_in_cluster++;
            
            //removed the buddy added to c from buddy_set_B
            buddy_set_B.remove(0);
            
            //to remove b_j's which are evaluated upon from buddy_set_B 
            ArrayList<Integer> temp_add = new ArrayList<>();         
            
            for(int i = 0; i < c.size(); i++)
            {
                if(temp_add.size() > 0)
                {
                    //sort temp_add to remove from start of buddy_set_B
                    Collections.sort(temp_add);
                    
                    int size = temp_add.size();
                    
                    //to remove b_js from buddy_set_B
                    while(size > 0)
                    {
                       buddy_set_B.remove((int)temp_add.get(size-1));
                       size--;
                    }
                }
                
                temp_add.clear();
                               
                int density_connected_i = 0;
                 
                //to calculate buddy radius threshold
                double buddy_radius_i = 0.0;
                
                if(c.get(i).bu.size() > 0)
                {
                    buddy_radius_i = radius_calc(c.get(i));
                }
                
                //check for density_connectedness of b_i
                if(c.get(i).bu.size() > (density_threshold+1) && buddy_radius_i <= (distance_threshold/2))
                {
                    density_connected_i = 1;
                }
                
                for(int j = 0; j < buddy_set_B.size(); j++)
                {
                   int density_connected_j = 0;
                    
                    double dist_center = center_dist_calc(c.get(i).b_center_x, c.get(i).b_center_y, buddy_set_B.get(j).b_center_x, buddy_set_B.get(j).b_center_y);
                    
                    //to calculate  buddy radius_threshold
                    double buddy_radius_j = 0.0;
                    if(buddy_set_B.get(j).bu.size() > 0)
                    {
                        buddy_radius_j = radius_calc(buddy_set_B.get(j));
                    }
                    
                    //check for density connectedness of b_j
                    if(buddy_set_B.get(j).bu.size() > (density_threshold+1) && buddy_radius_j <= (distance_threshold/2))
                    {
                        density_connected_j = 1;
                    }
                    
                    //if this satisfies, buddies are not density connected
                    if((dist_center - buddy_radius_i - buddy_radius_j) > distance_threshold)
                    {
                        continue;
                    }
                
                    
                    xyz:
                    for(int k = 0; k < c.get(i).bu.size(); k++)
                    {
                        int o_i = c.get(i).bu.get(k);
                        
                        for(int l = 0; l < buddy_set_B.get(j).bu.size(); l++)
                        {
                            int o_j = buddy_set_B.get(j).bu.get(l);
                           
                            //calculate distance between two objects of two buddies of c and buddy_set_B as dist_obj
                            double dist_obj = dist_calc(c.get(i).bu.get(k), buddy_set_B.get(j).bu.get(l));
                            
                            //if dist_obj is less than or equal to distance_threshold
                            if(dist_obj <= distance_threshold)
                            {
                                //if both b_i and b_j are density connected
                                if(density_connected_i == 1 && density_connected_j == 1)
                                { 
                                    //add b_j to c
                                    c.add(new Buddies(buddy_set_B.get(j).bu, buddy_id_in_cluster, buddy_set_B.get(j).b_center_x, buddy_set_B.get(j).b_center_y));
                                    buddy_id_in_cluster++;
                                    
                                    //adds the buddy positions which have been evaluated upon
                                    temp_add.add(j);
                                    
                                    k = -1;
                                    break xyz;
                                }
                                    
                                //if objects o_i and o_j are neighbours
                                else if(neighbour[o_i][o_j] == 1)
                                {
                                    int count2 = 0;
                                    
                                    //to check the no of neighbours of object o_i
                                    for(int x = 0; x<n; x++)
                                    {
                                        if(neighbour[o_i][x] == 1)
                                        {
                                            count2++;
                                        }
                                    }
                                        
                                    //if the object satisfies the density threshold criteria
                                    if(count2 > density_threshold)
                                    {
                                       ob.add(o_j);
                                       
                                       int re = buddy_set_B.get(j).bu.get(l);
                                       
                                       //remove the objects from buddy_set_B
                                       buddy_set_B.get(j).bu.remove(buddy_set_B.get(j).bu.indexOf(re));
                                       
                                       l--;
                                       
                                       //if the buddy has zero elements, remove it from buddy_set_B  
                                       if(buddy_set_B.get(j).bu.size() == 0)
                                           {
                                               buddy_set_B.remove(j);
                                               j--;
                                               break xyz;
                                           }
                                    }
                                }
                           }
                        }
                    }
                }
            }
            //add the clusters of buddy ids to C
            C.add(new Cluster(c, ob));
        }
    }    
    
    
    
    public static void split_and_merge()
    {
        ArrayList<Buddies_bar> buddy_bar = new ArrayList<>();
        
        //for 1st snapshot, mark all objects as buddies
        if(snapshot == 1)
        {
            for(int i = 0; i < n; i++)
            {
                ArrayList <Integer> temp11 = new ArrayList<>();
                temp11.add(i);
                
                //add buddy, buddy id, latitude and longitude information to buddy ArrayList 
                buddy.add(new Buddies(temp11, i, Double.parseDouble(distance[i][0]), Double.parseDouble(distance[i][1])));
            }
        }
    
        for(int i=0; i<buddy.size(); i++)
        {
            //updates the center of a buddy
            update_center(buddy.get(i));
                       
            //stores object's center in x co-ordinate
            double b = buddy.get(i).b_center_x;
            
            //stores object's center in y co-ordinate
            double c = buddy.get(i).b_center_y;
            
            int j = 0;
            
                while(j < buddy.get(i).bu.size())
                {
                    //
                    int a = buddy.get(i).bu.get(j);
                    
                    //checking distance of o_i to buddy centre (in x co-ordinate)
                    double diff_x = Math.pow(Math.abs(b - Double.parseDouble(distance[a][0])),2);
                    
                    //checking distance of o_i to buddy centre (in y co-ordinate)
                    double diff_y = Math.pow(Math.abs(c - Double.parseDouble(distance[a][1])),2);
                    
                    //calculate the change in centres
                    double dist = Math.sqrt((diff_x + diff_y));
                       
                        //split the buddy if objects have moved apart (more than the radius threshold)
                        if(dist > radius_threshold)
                        {
                            //stores the x co-ordinate of the object
                            double a_x = Double.parseDouble(distance[a][0]);
   
   			    //stores the y co-ordinate of the object
                            double a_y = Double.parseDouble(distance[a][1]);

                            //split o_j out as a new buddy b_j
                            ArrayList<Integer> temp = new ArrayList<>();
                            temp.add(a);  

                            //add b_j to B'
                            buddy_bar.add(new Buddies_bar(temp, j, a_x, a_y));

                            //remove o_i from buddy_bar
                            buddy.get(i).bu.remove(buddy.get(i).bu.indexOf(a));

                            //update cen(b_i)
                            update_center(buddy.get(i));
                        }
                        
                        else
                        {
                            j++;
                        }
                }
            
                //add b_i to B'
                buddy_bar.add(new Buddies_bar(buddy.get(i).bu, i, buddy.get(i).b_center_x, buddy.get(i).b_center_y));
        }
        
        // to remove empty arraylists from buddy_bar   
        for(int z = 0; z < buddy_bar.size();z++)
        {
              if(buddy_bar.get(z).bu_bar.size() == 0)
                {
                    buddy_bar.remove(z);
                }
        }

        buddy.clear();
    
       
       //Merge Operation
       ArrayList<ArrayList<Integer>> b_k = new ArrayList<>();
       int i = 0;
       int x = 0;
       double cen_b_l_x;
       double cen_b_l_y;
       
       while(x != buddy_bar.size())
       {
           int flag = 0;
           
            //to store buddy radius_threshold
            double buddy_radius_i = 0.0;
            
            	//calculate buddy radius_threshold
                if(buddy_bar.get(i).bu_bar.size() !=0)
                {    
                    buddy_radius_i = radius_calc_bar(buddy_bar.get(i));
                }
                
                //b_l stores the ids of buddies which are merged together                
                ArrayList<Integer> b_l = new ArrayList<>();
     
                    for(int k = 0; k < buddy_bar.get(i).bu_bar.size(); k++)
                    {
                        b_l.add(buddy_bar.get(i).bu_bar.get(k));
                    }
             
                        cen_b_l_x = 0;
                        cen_b_l_y = 0;
                    
                        for(int j = i+1; j<buddy_bar.size(); j++)
                        {
                            double dist_center = center_dist_calc(buddy_bar.get(i).b_center_bar_x, buddy_bar.get(i).b_center_bar_y, buddy_bar.get(j).b_center_bar_x, buddy_bar.get(j).b_center_bar_y);
                            
                                //to calculate  buddy radius_threshold
                                double buddy_radius_j = 0.0;
                                if(buddy_bar.get(j).bu_bar.size() !=0)
                                {                
                                    buddy_radius_j = radius_calc_bar(buddy_bar.get(j));
                                }
                          
                                
                                //checking the condition for merging two objects into a single buddy
                                if((dist_center + buddy_radius_i + buddy_radius_j) <= (2*radius_threshold))
                                {
                                   flag++;
                                   
                                    //add b_j to b_l
                                    for(int k = 0; k < buddy_bar.get(j).bu_bar.size(); k++)
                                    { 
                                        b_l.add(buddy_bar.get(j).bu_bar.get(k));
                                    }
                                  
                                  
                                       //calculation of the center of the buddy b_l (formed by merging of buddies b_i and b_j)
                                       int m1 = buddy_bar.get(i).bu_bar.size();
                                       int m2 = buddy_bar.get(j).bu_bar.size();
                                       double cen_b_i_x = m1 * buddy_bar.get(i).b_center_bar_x;
                                       double cen_b_i_y = m1 * buddy_bar.get(i).b_center_bar_y;
                                       double cen_b_j_x = m2 * buddy_bar.get(j).b_center_bar_x;
                                       double cen_b_j_y = m2 * buddy_bar.get(j).b_center_bar_y;


                                       if((m1 + m2) > 0)
                                       {
                                           cen_b_l_x = (cen_b_i_x + cen_b_j_x)/(m1+m2);
                                           cen_b_l_y = (cen_b_i_y + cen_b_j_y)/(m1+m2);
                                       }

                                       else
                                       {
                                           cen_b_l_x = cen_b_i_x + cen_b_j_x;
                                           cen_b_l_y = cen_b_i_y + cen_b_j_y;
                                       }
                                       
                                       	    // To remove the merged buddies from buddy_bar 
                                            for(int q = 0; q < b_l.size(); q++)
                                            {
                                                for(int q1 = 0; q1 < buddy_bar.size(); q1++)
                                                {
                                                    if(buddy_bar.get(q1).bu_bar.contains(b_l.get(q)))
                                                    {
                                                        buddy_bar.remove(buddy_bar.get(q1));
                                                    }
                                                }
                                            }        
                                       
                                       	    //Adding the Merging of the buddies (b_l) to buddy_bar
                                            buddy_bar.add(0, new Buddies_bar(b_l, (i+j)/2, cen_b_l_x, cen_b_l_y));
                                   
                                            i = 0;
                                            
                                            break;
                                }
                        }
                        
                        //If merging condition is not met
                        if(flag == 0)
                        {
                            double xx = buddy_bar.get(i).b_center_bar_x;
                            double yy = buddy_bar.get(i).b_center_bar_y;
                           
                            buddy.add(new Buddies(b_l, i, xx, yy));
      
                            for(int q = 0; q < b_l.size(); q++)
                            {
                                 for(int q1 = 0; q1 < buddy_bar.size(); q1++)
                                 {
                                    if(buddy_bar.get(q1).bu_bar.contains(b_l.get(q)))
                                    {
                                       buddy_bar.remove(buddy_bar.get(q1));
                                    }
                                    break;
                                }
                            }
                            if(b_l.size() > 0)
                            {
                                b_k.add(b_l);
                            }
                        }
             
                        if(buddy_bar.size() == 0)
                            break;
                        if(buddy_bar.get(i).bu_bar.isEmpty())
                        {
                            buddy_bar.remove(i);
                        }
        }            
       
        //Allocating ids to buddy elements
        for(int i2 = 0; i2 < buddy.size(); i2++)
        {
            buddy.get(i2).b_id = i2;
        }

	//Allocating the present distance in prev_distance[][] to be used in the next snapshot
        for(int p=0; p<n; p++)
        {
            for(int q=0; q<2; q++)
            {
                prev_distance[p][q] = distance[p][q];
            }
        }
                    
    }
 
    static int line_to_read = 1;
    
    public static String OpenFile() throws IOException
    {
        //provide the input directory for the TAXI dataset
        Scanner inFile1 = new Scanner(new File("/home/anand/Desktop/M_Tech_Project/Extracted_dataset/T_drive_trajectory_500_objects/"+fname+".txt"));
        
            //reads one line at a time from each object file (for Euclidean distance calculation)
            for(int df = 0; df < line_to_read; df++)
            {
                arr = inFile1.nextLine();
            }
        
            inFile1.close();
       return arr;
    }
    
    public static void euclidean()
    {
        double diff_x, diff_y ;
        int k, i , j, s;
           
        try
        {
            for(j = 0; j<n; j++)
            {
                String arr1 = OpenFile();
                                                
                //splits the read line on the basis of comma to be stored as elements of temp_arr1[] array
                String[] temp_arr1 = arr1.split(",");
                
                //s is set to 0 each time so as to read the line of a new object file from the start
                s=0;
                
                //to read latitude and longitude in distance[][] array (field 0 and field 1 of the read line of the object file)
                for(k=0; k<2; k++)
                {
                    //to break the loop if s becomes equal to the number of fields in the line of the read object file
                    if(s == temp_arr1.length)
                       break;
                  
                    distance[j][k] = temp_arr1[s]; // to store the latitude and longitude as 2D array
                    s++;
                }
                
                fname++;
            }
            
            line_to_read++;
            
            
            //copy distance to prev_distance if snapshot == 1, update_center() and split_and_merge()
            if(snapshot==1)
            {
                for(int p=0; p<n; p++)
                {
                    for(int q=0; q<2; q++)
                    {
                        prev_distance[p][q] = distance[p][q];
                    }
                }
            }
                   
                       
            //calculation of Euclidean distance between objects
            for(i=0; i< n; i++)
            {
		       for(j =0; j<n; j++)
		       {
		                   
		       // to calculate x2-x1
		       diff_x = Math.pow((Double.parseDouble(distance[i][0]) - Double.parseDouble(distance[j][0])),2);
		                   
		       // to calculate y2-y1
		       diff_y = Math.pow((Double.parseDouble(distance[i][1]) - Double.parseDouble(distance[j][1])),2);
		                   
		       eucd[i][j] = Math.sqrt((diff_x + diff_y));
		             
		       //neighbours are the objects who are within the distance threshold of each other
		       if(eucd[i][j] <= distance_threshold)
		       {
		            neighbour[i][j] = 1;
		       }
              }
            }
        }
        
        catch(IOException e)
        {
            System.out.println(e);
        }
        
    }
    
    public static void main(String args[])
    {
        Companion_discovery w = new Companion_discovery();
        w.perform();
    }
}

