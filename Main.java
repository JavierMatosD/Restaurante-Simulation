import java.util.Random;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args){
              
      int timeCap = 10000;
      int serverCount = 8;
      double p = .5;
      double q = .5;
      
      simulation(timeCap, serverCount, p, q);
      System.out.println("Theoretical expected guest hours rate: " + getExpectedGuestHoursRate(p,q));
      
      // The expected number of guest hours generated per hour = (1/p)*((2-q/q^2). In other words, E[X] * E[Y^2] where X ~ Geometric(p) and Y ~ Geometric(q) 
      // Crucially, however, if servers become overburdened during the course of the simulation they might slow down and no longer be able to keep up with the expected incoming load
    }
    
    // ===================================
    // timeCap is number of hours to run simulation for
    // serverCount is number of servers in the restaurant
    // p is used with our geometric function to determine the number of jobs arriving per hour. As p decreases, the number of jobs arriving per hour increases
    // q is used with our geometric function to determine the number of guests in each table (aka job). As q decreases, the number of guests arriving in each table increases
    public static void simulation(int timeCap, int serverCount, double p, double q){
      
      int seatingDelay = 0;
      int serverDelay = 0;
      int departureCount = 0;
      int guestHours = 0;
      
      Manager m = new Manager(serverCount); //initialize new restaurant
      int time = 0;
      int arrivalCount;

      while (time < timeCap){ //Stop simulation once enough minutes have passed
        
        ArrayList<Table> departures = m.tick(time);
        
        for(int i = 0; i < departures.size(); i++){
          seatingDelay += departures.get(i).seatingDelay; //restaurant delay = delay in being seated upon arriving
          serverDelay += departures.get(i).serverDelay; //server delay = delay in being served due to over capacity server
          departureCount++;
        }
        
        //System.out.println(m.printRestaurantStatus(time));
        //System.out.println(time + " | Departures: " + departures);
        
        arrivalCount = geometric(p); // we could try subtracting one here so that it's possible that 0 tables arrive in a given hour
        
        //System.out.println("\n\n========\n" + time + "   " + arrivalCount + "\n");
        
        for(int i = 0; i < arrivalCount; i++){
         
          int guestCount = geometric(q);
          int length = geometric(1.0/guestCount); //The length is also determined geometrically. The expected value, however, is directly tied to q. The expected value of the guestcount = 1/q = expected hours the party will stay.
          guestHours += guestCount*length;
          
          //System.out.println(guestCount + "  " + length);
          m.tableArrival(guestCount, time, length); //generate a new party (aka table aka job).
        }     
        
        m.assignTables(time);
        
        time++;        
      }
      
      System.out.println(m.printRestaurantStatus(time));
      
      System.out.println("Seating delay: " + (1.0*seatingDelay/departureCount));
      System.out.println("Server delay: " + (1.0*serverDelay/departureCount));
      System.out.println("Guest hours average: " + (1.0*guestHours/timeCap));
    }
    
    // ===================================
    
    public static int geometric (double prob){
      Random r = new Random();
      
      double a = r.nextDouble(); //our double to compare to the cdf
      
      int i = 0;
      while(true){ //now we just find the value of i at which our random variable is smaller than the cdf, which is what we return
        i++;
        double cdf = 1.0-Math.pow((1.0-prob),i+0.0);
        if(a < cdf){
          return i;
        } 
      }
      //return -1;
    }
    
    public static double getExpectedGuestHoursRate(double p, double q){
     
      return ((2.0-q)/Math.pow(q,2.0))*(1.0/p);
    }
}