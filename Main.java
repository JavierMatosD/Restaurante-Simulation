import java.util.Random;
import java.io.FileWriter;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args){
              
      int mode        = 1;
      int timeCap     = 10000;
      int serverCount = 10;
      double p        = 0.45;
      double q        = 0.50;
      
      ArrayList<Double[]> strat1Data = new ArrayList<>();
      ArrayList<Double[]> strat2Data = new ArrayList<>();
      ArrayList<Double[]> strat3Data = new ArrayList<>();
      ArrayList<Double[]> strat4Data = new ArrayList<>();
      ArrayList<Double[]> strat5Data = new ArrayList<>();

      int pIndex            = 0;
      int seatingDelayIndex = 1;
      int serverDelayIndex  = 2;
      int abandonRateIndex  = 3;
      int modeIndex         = 4;
      FileWriter fWriter_1 = null;
      FileWriter fWriter_2 = null;
      FileWriter fWriter_3 = null;
      FileWriter fWriter_4 = null;
      FileWriter fWriter_5 = null;

      int numSimulations = 5;
      // Run simulation for all 4 strategies
      while (mode <= 5) {
        p = 0.45;
        // Maximum capacity for restaurant is 15 guests * 8 servers = 120
        // Max expected guest hours occur when q = 0.5 and p = 0.05
        while (p > 0.05) {
          double averageSeatingDelay = 0;
          double averageServerDelay = 0;
          double averageAbandonmentRate = 0;

          // Run simulation 100 times
          int i = 1;
          while (i <= numSimulations) {

            // run 
            String[] results = simulation(mode, timeCap, serverCount, p, q);

            averageSeatingDelay    += Double.parseDouble(results[seatingDelayIndex]);
            averageServerDelay     += Double.parseDouble(results[serverDelayIndex]);
            averageAbandonmentRate += Double.parseDouble(results[abandonRateIndex]);
            i++;
          }

          // Average out the results
          averageServerDelay     = averageServerDelay     / numSimulations;
          averageSeatingDelay    = averageSeatingDelay    / numSimulations;
          averageAbandonmentRate = averageAbandonmentRate / numSimulations;

          // Add results to our data
          Double[] meanResults = new Double[] { p, averageSeatingDelay, averageServerDelay, averageAbandonmentRate, (double) mode};

          switch (mode) {
            case 1:
              strat1Data.add(meanResults);
              break;
            case 2:
              strat2Data.add(meanResults);
              break;
            case 3:
              strat3Data.add(meanResults);
              break;
            case 4:
              strat4Data.add(meanResults);
              break;
            case 5:
              strat5Data.add(meanResults);
              break;
          }

          // Adjust p
          p -= 0.05;
        }

        // switch modes
        mode++;
      }
      // Write to fWriter
      try 
      {
        String strat1FileName = "assignmentStrategy_1.csv";
        String strat2FileName = "assignmentStrategy_2.csv";
        String strat3FileName = "assignmentStrategy_3.csv";
        String strat4FileName = "assignmentStrategy_4.csv";
        String strat5FileName = "assignmentStrategy_5.csv";

        fWriter_1 = new FileWriter(strat1FileName);
        fWriter_2 = new FileWriter(strat2FileName);
        fWriter_3 = new FileWriter(strat3FileName);
        fWriter_4 = new FileWriter(strat4FileName);
        fWriter_5 = new FileWriter(strat5FileName);

        for (Double[] doubles : strat1Data) 
        {
          fWriter_1.append(String.valueOf(doubles[pIndex]));
          fWriter_1.append(",");
          fWriter_1.append(String.valueOf(doubles[seatingDelayIndex]));
          fWriter_1.append(",");
          fWriter_1.append(String.valueOf(doubles[serverDelayIndex]));
          fWriter_1.append(",");
          fWriter_1.append(String.valueOf(doubles[abandonRateIndex]));
          fWriter_1.append(",");
          fWriter_1.append(String.valueOf(doubles[modeIndex]));
          fWriter_1.append("\n");
        }
        
        for (Double[] doubles : strat2Data) 
        {
          fWriter_2.append(String.valueOf(doubles[pIndex]));
          fWriter_2.append(",");
          fWriter_2.append(String.valueOf(doubles[seatingDelayIndex]));
          fWriter_2.append(",");
          fWriter_2.append(String.valueOf(doubles[serverDelayIndex]));
          fWriter_2.append(",");
          fWriter_2.append(String.valueOf(doubles[abandonRateIndex]));
          fWriter_2.append(",");
          fWriter_2.append(String.valueOf(doubles[modeIndex]));
          fWriter_2.append("\n");
        }
        for (Double[] doubles : strat3Data) 
        {
          fWriter_3.append(String.valueOf(doubles[pIndex]));
          fWriter_3.append(",");
          fWriter_3.append(String.valueOf(doubles[seatingDelayIndex]));
          fWriter_3.append(",");
          fWriter_3.append(String.valueOf(doubles[serverDelayIndex]));
          fWriter_3.append(",");
          fWriter_3.append(String.valueOf(doubles[abandonRateIndex]));
          fWriter_3.append(",");
          fWriter_3.append(String.valueOf(doubles[modeIndex]));
          fWriter_3.append("\n");
        }
        for (Double[] doubles : strat4Data) 
        {
          fWriter_4.append(String.valueOf(doubles[pIndex]));
          fWriter_4.append(",");
          fWriter_4.append(String.valueOf(doubles[seatingDelayIndex]));
          fWriter_4.append(",");
          fWriter_4.append(String.valueOf(doubles[serverDelayIndex]));
          fWriter_4.append(",");
          fWriter_4.append(String.valueOf(doubles[abandonRateIndex]));
          fWriter_4.append(",");
          fWriter_4.append(String.valueOf(doubles[modeIndex]));
          fWriter_4.append("\n");
        }
        for (Double[] doubles : strat5Data) 
        {
          fWriter_5.append(String.valueOf(doubles[pIndex]));
          fWriter_5.append(",");
          fWriter_5.append(String.valueOf(doubles[seatingDelayIndex]));
          fWriter_5.append(",");
          fWriter_5.append(String.valueOf(doubles[serverDelayIndex]));
          fWriter_5.append(",");
          fWriter_5.append(String.valueOf(doubles[abandonRateIndex]));
          fWriter_5.append(",");
          fWriter_5.append(String.valueOf(doubles[modeIndex]));
          fWriter_5.append("\n");
        }
      } 
      catch (Exception ex) { System.out.println("something went wrong");}
       // flush and close 
      finally 
      {
        try 
        {
          fWriter_1.flush();
          fWriter_1.close();
          fWriter_2.flush();
          fWriter_2.close();
          fWriter_3.flush();
          fWriter_3.close();
          fWriter_4.flush();
          fWriter_4.close();
          fWriter_5.flush();
          fWriter_5.close();
        } catch (Exception e){System.out.println("something went wrong");}
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
          seatingDelay += departures.get(i).seatingDelay;
          serverDelay += departures.get(i).serverDelay;
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
          String.valueOf(abandonedRate)
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