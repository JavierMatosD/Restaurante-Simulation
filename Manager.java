import java.util.LinkedList;
import java.util.Queue;
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
   *               3. Assigns tables in order, skipping overburdened servers
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
      default:
        break;
    }

  }
  
  /**
   * Assigns tables to the least busy server
   * @param time
   */
  public void assignTablesLeastBusy(int time) {
      while (unseatedTables.size() > 0 && servers.get(getLeastBusyServerIndex()).getAvailableCapacity() > 0)
      abandonedCount += servers.get(getLeastBusyServerIndex()).seatTable(unseatedTables.remove(), time);
  }
   
  /**
     * Assigns incoming parties to servers. The method is called by Manager.assignTables(int time)
     * <p>
     * Assigns tables to servers randomly.
     * @param time - current time
     */
  public void assignTablesRandom(int time) {
    while (unseatedTables.size() > 0) {
      int randomServerIndex = (int) (Math.random() * (servers.size() - 0));
      abandonedCount += servers.get(randomServerIndex).seatTable(unseatedTables.remove(), time);
    }
  }
  
  /**
   * Strategy to assign tables to servers in order. 
   * The method is called by Manager.assignTable(int time)
   * <p>
   * Skips over servers that are overburdened.
   * @param time
   */
  public void assignTablesInOrder(int time) {
    for (int i = 0; i < servers.size(); i++) {
        
        // Skip if server is at capacity
        if (servers.get(i).getAvailableCapacity() < 0) {
          i++;
        }
        
        // Cycle back to first server
        if (i + 1 > servers.size()) {
          i = 0;
        }

        // If there are still unseated tables then seat them
        if (unseatedTables.size() > 0){
          abandonedCount += servers.get(i).seatTable(unseatedTables.remove(), time);
        } else {
          break;
        }
    }
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
