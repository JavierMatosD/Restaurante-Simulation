import java.util.Random;
import java.io.FileWriter;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args){
            
      //System.out.println(getExpectedGuestHoursRate(.05,.5));

      int mode        = 1;
      int modeCount = 5;
      int timeCap     = 10000;
      int serverCount = 10;
      double p        = 0.15;
      double q        = 0.50;
      
      ArrayList<ArrayList<Double[]>> stratData = new ArrayList<ArrayList<Double[]>>();
      for(int i = 0; i < modeCount; i++){
        stratData.add(new ArrayList<Double[]>());
      }
     
      int pIndex            = 0;
      int seatingDelayIndex = 1;
      int serverDelayIndex  = 2;
      int abandonRateIndex  = 3;
      int departureCountIndex = 4;
      int modeIndex         = 5;
      
      FileWriter fWriter = null;
      int numSimulations = 10;
      
      // Run simulation for all strategies
      while (mode <= modeCount) {
        p = 0.15;
        // Maximum capacity for restaurant is 10 guests * 10 servers = 100
        // Max expected guest hours occur when q = 0.5 and p = 0.05
        while (p >= 0.05) {
          double averageSeatingDelay = 0;
          double averageServerDelay = 0;
          double averageAbandonmentRate = 0;
          double averageDepartureCount = 0;

          // Run simulation 100 times
          int i = 1;
          while (i <= numSimulations) {

            //System.out.println(i);
            
            // run 
            String[] results = simulation(mode, timeCap, serverCount, p, q);

            averageSeatingDelay    += Double.parseDouble(results[seatingDelayIndex]);
            averageServerDelay     += Double.parseDouble(results[serverDelayIndex]);
            averageAbandonmentRate += Double.parseDouble(results[abandonRateIndex]);
            averageDepartureCount += Double.parseDouble(results[departureCountIndex]);
            i++;
          }

          // Average out the results
          averageServerDelay     = averageServerDelay     / numSimulations;
          averageSeatingDelay    = averageSeatingDelay    / numSimulations;
          averageAbandonmentRate = averageAbandonmentRate / numSimulations;
          averageDepartureCount = averageDepartureCount / numSimulations;

          // Add results to our data
          Double[] meanResults = new Double[] { p, averageSeatingDelay, averageServerDelay, averageAbandonmentRate, averageDepartureCount, (double) mode};

          stratData.get(mode-1).add(meanResults);

          // Adjust p
          p -= 0.01;
        }

        // switch modes
        mode++;
      }
      // Write to fWriter
      try 
      {

        for(int i = 0; i < stratData.size(); i++){
          
          fWriter = new FileWriter("assignmentStrategy_" + (i+1) + ".csv");
          
          for (Double[] doubles : stratData.get(i)){
            fWriter.append(String.valueOf(doubles[pIndex]));
            fWriter.append(",");
            fWriter.append(String.valueOf(doubles[seatingDelayIndex]));
            fWriter.append(",");
            fWriter.append(String.valueOf(doubles[serverDelayIndex]));
            fWriter.append(",");
            fWriter.append(String.valueOf(doubles[abandonRateIndex]));
            fWriter.append(",");
            fWriter.append(String.valueOf(doubles[departureCountIndex]));
            fWriter.append(",");
            fWriter.append(String.valueOf(doubles[modeIndex]));
            fWriter.append("\n");
          }
          fWriter.flush();
          fWriter.close();
        }
      } 
      catch (Exception ex) { System.out.println(ex.toString());}
       // flush and close 
      finally 
      {
        System.out.println("Finished :)");
      }

      /**
       * The expected number of guest hours generated per hour = (1/p)*((2-q/q^2). In other words, E[X] * E[Y^2] where X ~ Geometric(p) and Y ~ Geometric(q).
       * Crucially, however, if servers become overburdened during the course of the simulation they might slow down and no longer be able
       * to keep up with the expected incoming load.
       */
      //System.out.println("Theoretical expected guest hours rate: " + getExpectedGuestHoursRate(p,q));
      
    }
    

    /**
     * Called by main to run the restaurant simulation
     * @param mode is the table assignment strategy the manager uses
     * @param timeCap is number of hours to run simulation for
     * @param serverCount is number of servers in the restaurant
     * @param p : determines the job arrival rate. <br>
     *            As 'p' decreases, the number of jobs arriving per hour increases.
     * @param q : determines the number of guests per table (aka job size). <br>
     *            As 'q' decreases, the number of guests arriving in each table increases
     * @return void
     */
    public static String[] simulation(int mode, int timeCap, int serverCount, double p, double q) {

      int seatingDelay = 0;
      int serverDelay = 0;
      int departureCount = 0;
      int guestHours = 0;
      int time = 0;
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
          seatingDelay += departures.get(i).seatingDelay * departures.get(i).guests;
          serverDelay += departures.get(i).serverDelay * departures.get(i).guests;
          departureCount += departures.get(i).guests;
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
          int length = geometric(1.0 / guestCount);
          guestHours += guestCount * length;
          totalGuestCount += guestCount;

          // Generate a new party (aka table aka job).
          m.tableArrival(guestCount, time, length);
        }

        m.assignTables(time, mode);

        time++;
      }

      // Print Data
      double abandonedRate = 1.0 * m.getAbandonedCount() / totalGuestCount;
      double seatDelay     = 1.0 * seatingDelay / departureCount;
      double servDelay     = 1.0 * serverDelay / departureCount;
      // System.out.println("\n");
      // System.out.println("");
      // System.out.println("Mode: " + mode);
      // System.out.println("P = " + (float)p);
      // System.out.println("Q = " + q + "\n");
      // System.out.println(m.printRestaurantStatus(time, 0));
      // System.out.println("Abandoned rate: " + (1.0 * m.getAbandonedCount() / totalGuestCount));
      // System.out.println("Seating delay: " + (1.0 * seatingDelay / departureCount));
      // System.out.println("Server delay: " + (1.0 * serverDelay / departureCount));
      // System.out.println("Guest hours average: " + (1.0 * guestHours / timeCap));

      String[] results = new String[]{
        String.valueOf(p),
          String.valueOf(seatDelay),
          String.valueOf(servDelay),
          String.valueOf(abandonedRate),
          String.valueOf(departureCount)
      };

      return results;
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