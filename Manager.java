import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.ArrayList;

public class Manager{
  
  private ArrayList<Server> servers;
  private Queue<Table> unseatedTables;
  private int abandonedCount;

  /**
   * Constructor initializes a list of Servers and a queue for unseated Tables
   * @param serverCount
   */
  public Manager(int serverCount){
        
    servers = new ArrayList<Server>();
    
    for(int i = 0; i < serverCount; i++){
      servers.add(new Server()); 
    }
    
    unseatedTables = new LinkedList<Table>();
  }
  
  /**
   * When a new party arrives, method generates a table object.
   * <p>
   * Tables are seated according to assignTables(int).
   * @param guests is the number of guests.
   * @param time   is the current time.
   * @param length is the length of time party will stay.
   */
  public void tableArrival(int guests, int time, int length){
    unseatedTables.add(new Table(guests, time, length));
  }

/**
 * Method is an event that removes and returns all the tables who are done.
 * @param time - current time
 * @return list of tables who have departed
 */ 
  public ArrayList<Table> checkDepartures(int time){
    
    ArrayList<Table> allDepartures = new ArrayList<Table>();
    
    for(int i = 0; i < servers.size(); i++){
      allDepartures.addAll(servers.get(i).checkDepartures(time));
    }
        
    return allDepartures;
  }
  
  /**
   * Assigns incoming parties to servers.
   * @param mode : Assignment Strategy [1-3]. <br>
   *               1. Assigns tables to the least busy server. <br>
   *               2. Assigns tables randomly. <br>
   *               3. Assigns tables in order, skipping overburdened servers <br>
   *               4. Assign tables instantly <br>
   *               5. covidStrategy
   * @param time - Current time
   */
  public void assignTables(int time, int mode) {
    switch (mode) {
      case 1:
        assignTablesLeastBusy(time);
        break;
      case 2:
        assignTablesRandom(time);
        break;
      case 3:
        assignTablesInOrder(time);
        break;
      case 4:
        assignTablesInstantly(time);
        break;
      case 5:
        assignTablesCovidStrategy(time);
        break;
      case 6:
        assignTablesMatchCapacity(time); //non FIFO
      default:
        break;
    }

  }
  
  /**
   * Happy Customer AND Happy Server Strategy <p>
   * Assigns tables to the least busy server that is not currently over capacity
   * @param time
   */
  public void assignTablesLeastBusy(int time) {
      while (unseatedTables.size() > 0 && servers.get(getLeastBusyServerIndex()).getAvailableCapacity() > 0)
        abandonedCount += servers.get(getLeastBusyServerIndex()).seatTable(unseatedTables.remove(), time);
  }
  
  /**
   * Happy Customer Strategy Assigns tables to the least busy server
   * @param time
   */
  public void assignTablesInstantly(int time) {
    while (unseatedTables.size() > 0)
      abandonedCount += servers.get(getLeastBusyServerIndex()).seatTable(unseatedTables.remove(), time);
  }
  
  /**
     * Random Assignment Strategy <p>
     * Assigns incoming parties randomly to servers. The method is called by Manager.assignTables(int time)
     * @param time - current time
     */
  public void assignTablesRandom(int time) {
    while (unseatedTables.size() > 0) {
      int randomServerIndex = (int) (Math.random() * (servers.size() - 0));
      abandonedCount += servers.get(randomServerIndex).seatTable(unseatedTables.remove(), time);
    }
  }
  
  /**
   * Busy Servers Strategy to assign tables to servers in order. 
   * The method is called by Manager.assignTable(int time).
   * @param time
   */
  public void assignTablesInOrder(int time) {
    int i = 0;
    while (!unseatedTables.isEmpty()) {
      // If on the last server cycle back to first
      i = (i >= servers.size() - 1) ? 0 : i + 1;
      abandonedCount += servers.get(i).seatTable(unseatedTables.remove(), time);
    }
  }
  
  /**
   * Covid Strategy <p>
   * Assigns tables to servers but servers cannot become overburdened. Parties/Tables larger than the server capacity
   * will be given the choice to be split or to leave. 
   * <p>
   * Tables will stay with a 50% chance if their party can be split between two servers. Else they abandon
   * @param time
   */
  public void assignTablesCovidStrategy(int time) {

    while (!unseatedTables.isEmpty()) 
    {
      int leastBusyServerCap = servers.get(getLeastBusyServerIndex()).getAvailableCapacity();
      int numGuest           = unseatedTables.peek().guests;

      // if the number of guest is larger than the least busy servers available capacity
      // Check if they can be split
      if (numGuest > leastBusyServerCap) 
      {
        int secondLeastCapacity = servers.get(getSecondLeastBusyServer()).getAvailableCapacity();

        // If the number of guests is more than two servers can handle
        if (numGuest > leastBusyServerCap + secondLeastCapacity) 
        {
          abandonedCount += numGuest;
          unseatedTables.remove();
          break;
        }

        // If the number of guest can't be split among two servers they abandon
        if (numGuest % (leastBusyServerCap + secondLeastCapacity) != 0) 
        {
          abandonedCount += numGuest;
          unseatedTables.remove();
        // else offer a split
        } else 
        {
         boolean abandon = new Random().nextBoolean();

          // They chose to leave
          if (abandon)
          {
            abandonedCount += numGuest;
            unseatedTables.remove();
            // They chose to stay
          } else 
          {
            numGuest -= leastBusyServerCap;
            Table table1 = new Table(leastBusyServerCap, time, Main.geometric(1.0 / leastBusyServerCap));
            Table table2 = new Table(numGuest, time, Main.geometric(1.0 / numGuest));
            abandonedCount += servers.get(getLeastBusyServerIndex()).seatTable(table1, time);
            abandonedCount += servers.get(getSecondLeastBusyServer()).seatTable(table2, time);
            unseatedTables.remove();
          }
        }
      } else if (leastBusyServerCap > 0)
      {
         abandonedCount += servers.get(getLeastBusyServerIndex()).seatTable(unseatedTables.remove(), time);
      }
     
    }
  }
  
  /**
     * Match tables strategy - skips large tables at front of line if capacity is available to serve smaller tables who arrived later
     * @param time - current time
     */
  public void assignTablesMatchCapacity(int time) {

    //First, seat tables in order while not overburdening any servers
    while (unseatedTables.size() > 0 && servers.get(getLeastBusyServerIndex()).getAvailableCapacity() > unseatedTables.peek().guests)
      abandonedCount += servers.get(getLeastBusyServerIndex()).seatTable(unseatedTables.remove(), time);
    
    //Second, if there are still unseated tables, iterate through all remaining tables and allow smaller tables to 'skip the line' in order to be seated immediately
    if(unseatedTables.size() > 0){ 
      Table[] unseatedTableArr = unseatedTables.toArray(new Table[unseatedTables.size()]);
      for(int i = 0; i < unseatedTableArr.length; i++){
        if(unseatedTableArr[i].guests <= servers.get(getLeastBusyServerIndex()).getAvailableCapacity()){
          Table t = unseatedTableArr[i];
          abandonedCount += servers.get(getLeastBusyServerIndex()).seatTable(t, time);
          unseatedTables.remove(t);
        }
      }
    }
    
    //Finally third, if there are still any unseated tables remaining and any servers who are not overburdened, go ahead and seat them. This step is identical to 'assignTablesLeastBusy'
    while (unseatedTables.size() > 0 && servers.get(getLeastBusyServerIndex()).getAvailableCapacity() > 0)
      abandonedCount += servers.get(getLeastBusyServerIndex()).seatTable(unseatedTables.remove(), time);
  }

  private int getSecondLeastBusyServer() {
    int leastBusyIndex      = getLeastBusyServerIndex();
    int secondLeastCapacity = Integer.MAX_VALUE;
    int secondLeastIndex    = 0;
    for (int i = 0; i < servers.size(); i++) 
    {
      // Skip if server is least busy
      if (i == leastBusyIndex) 
        i++;
        if (i >= servers.size()) 
          break;
      int currentServerCapacity = servers.get(i).getAvailableCapacity();

      if (currentServerCapacity < secondLeastCapacity) {
        secondLeastCapacity = currentServerCapacity;
        secondLeastIndex    = i;
      }
    }
    return secondLeastIndex;
  }
  /**
   * Utility method called by assignTables(int) and returns the index of the least burdened server.
   * @return (int) index for the least busy server
   */
  private int getLeastBusyServerIndex(){
    
    int leastBusy = 0;
    
    for (int i = 0; i < servers.size(); i++) {

      int currentServerCapacity   = servers.get(i).getAvailableCapacity();
      int leastBusyServerCapacity = servers.get(leastBusy).getAvailableCapacity();

      if(currentServerCapacity > leastBusyServerCapacity)
        leastBusy = i;
    } 
    return leastBusy;
  }
  
  // ===================================
    /**
    * Returns the time, number of unseated tables, and the server status.
    <p>
    * Method calls printServerStatus() to retrieve the status of all the servers.
    * @param time
    * @param mode
    * @return String
    */
  public String printRestaurantStatus(int time, int mode){
    
    String s = "";

    s += ("===================\nTime:" + time + "\n");
    s += ("Unseated tables: " + unseatedTables.size() + "\n\n");
    
    for(int i = 0; i < servers.size(); i++){
      
      s += "=====\n";
      s += "Server " + i + ":\n";
      s += (servers.get(i).printServerStatus(mode) + "\n");
    }

    return s;
  }
  
  /**
   * @return list of servers
   */
  public ArrayList<Server> getServers() {
    return servers;
  }
  
  /**
   * @return queue of unseatedTables
   */
  public Queue<Table> getUnseatedTables(){
    return unseatedTables;
  }
  
  public int getAbandonedCount(){
   
    return abandonedCount;
  }
}
