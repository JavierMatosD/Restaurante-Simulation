import java.util.Random;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args){
              
      int timeCap     = 10000;
      int serverCount = 10;
      double p        = 0.05;
      double q        = 0.50;
      
      // Maximum capacity for restaurant is 15 guests * 8 servers = 120
      // Max expected guest hours occur when q = 0.5 and p = 0.05
      // while (p > .05) {
        
      // }
      simulation(timeCap, serverCount, p, q);

      /**
       * The expected number of guest hours generated per hour = (1/p)*((2-q/q^2). In other words, E[X] * E[Y^2] where X ~ Geometric(p) and Y ~ Geometric(q).
       * Crucially, however, if servers become overburdened during the course of the simulation they might slow down and no longer be able
       * to keep up with the expected incoming load.
       */
      System.out.println("Theoretical expected guest hours rate: " + getExpectedGuestHoursRate(p,q));
    }
    

    /**
     * Called by main to run the restaurant simulation
     * @param timeCap is number of hours to run simulation for
     * @param serverCount is number of servers in the restaurant
     * @param p : determines the job arrival rate. <br>
     *            As 'p' decreases, the number of jobs arriving per hour increases.
     * @param q : determines the number of guests per table (aka job size). <br>
     *            As 'q' decreases, the number of guests arriving in each table increases
     * @return void
     */
    public static void simulation(int timeCap, int serverCount, double p, double q){
      
      int seatingDelay    = 0;
      int serverDelay     = 0;
      int departureCount  = 0;
      int guestHours      = 0;
      int time            = 0;
      int totalGuestCount = 0;
      int arrivalCount;

      //initialize new restaurant
      Manager m = new Manager(serverCount);

      // Simulation stops when enough minutes have passed
      while (time < timeCap) {

        ArrayList<Table> departures = m.checkDepartures(time);

        for (int i = 0; i < departures.size(); i++) {

          //restaurant delay = delay in being seated upon arriving
          //server delay = delay in being served due to over capacity server
          seatingDelay += departures.get(i).seatingDelay;
          serverDelay  += departures.get(i).serverDelay;
          departureCount++;
        }

        // We could try subtracting one here so that it's possible that 0 tables arrive in a given hour
        arrivalCount = geometric(p);


        /**
         * length is how long the guests will stay at the restaurant after being seated.
         * guestCount is the number of guests in the party. 
         * length is determined geometrically. However, the expected value is directly tied to q.
         * The expected value of the guestCount = 1/q = expected hours the party will stay.
         */
        for (int i = 0; i < arrivalCount; i++) {
          int guestCount = geometric(q);
          /*if(guestCount > 30){
            System.out.println("Big one + " + guestCount); 
          }*/
          int length       = geometric(1.0 / guestCount);
          guestHours      += guestCount * length;
          totalGuestCount += guestCount;

          // Generate a new party (aka table aka job).
          m.tableArrival(guestCount, time, length);
        }

        m.assignTables(time, 1);

        time++;
      }
      
      // Print Data
      System.out.println(m.printRestaurantStatus(time, 0));
      System.out.println("Abandoned rate: " + (1.0*m.getAbandonedCount()/totalGuestCount));
      System.out.println("Seating delay: "       + (1.0*seatingDelay/departureCount));
      System.out.println("Server delay: "        + (1.0*serverDelay/departureCount));
      System.out.println("Guest hours average: " + (1.0*guestHours/timeCap));
    }
    

    /**
     * Generates and returns geometrically distributed random variable (RV). Method is called by Main.simulation(int, int, double, double)
     * to determine the number of incoming jobs/hour and the number of guests/job
     * @param prob : probability
     * @return int : RV
     */
    public static int geometric(double prob) {

      Random r = new Random();
      double a = r.nextDouble(); // Double to compare to the cdf
      int i    = 0;

      // Find and return the value of i at which our random variable is smaller than the cdf
      while (true) {
        i++;
        double cdf = 1.0 - Math.pow((1.0 - prob), i + 0.0);
        if (a < cdf) 
          return i;
      }
    }
    
    /**
     * The expected number of guest hours generated per hour = (1/p)*((2-q/q^2).
     * In other words, E[X] * E[Y^2] where X ~ Geometric(p) and Y ~ Geometric(q) 
     * @param p 
     * @param q
     * @return (double) the expected rate of arrival for incoming guests
     */
    public static double getExpectedGuestHoursRate(double p, double q){
      return ((2.0-q)/Math.pow(q,2.0))*(1.0/p);
    }
}