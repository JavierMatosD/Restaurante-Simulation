import java.util.ArrayList;

public class Server{
 
  private final int serverCapacity = 15; //hard coded server capacity before service efficiency begins decreasing
  private ArrayList<Table> tables;
  private boolean busy;
  private int jobCounter, nextDeparture;
  

  /**
   * Constructor initializes an empty server with no jobs yet sent.
   * After this constructor is called, jobArrival may be called to simulate a job arrival.
   */
  public Server() {
    tables        = new ArrayList<Table>();
    busy          = false;
    jobCounter    = 0;
    nextDeparture = -1;
  }
  
    /**
   * Method is an event that removes and returns all the tables who are done eating within the server.
   * @param time = current time
   * @return list of tables departed from server
   */
  public ArrayList<Table> checkDepartures(int time){
    
    ArrayList<Table> departures = new ArrayList<Table>();
    
    for(int i = 0; i < tables.size(); i++){
      
      if(tables.get(i).sArrival + tables.get(i).length == time){
        departures.add(departTable(i));
        i--;
      }
    }
    
    return departures;
  }
  
  // ===================================
  // this method is called when a table is seated by a server
  // returns the number of abandoned guests. 0 if table seated successfully
  public int seatTable(Table t, int time){
    
    double serverMultiplier = (1.0*t.guests + this.getGuestTotal())/serverCapacity;
    
    if(serverMultiplier < 1.0)
      serverMultiplier = 1.0;
    
    if(t.seated(serverMultiplier,time)){
      tables.add(t);
      jobCounter++; //Increment job counter
      return 0;
    }
    else{
      return t.guests; //return abandoned guest count if seating was unsuccessful
    }
  }
  

  /**
   * Method handles a tables departure
   * @param index of the departing table
   * @return The newly departed table
   */
  public Table departTable(int index){
    
   Table t = tables.remove(index);
    
   /*
    if(tables.size() == 0){ //If the queue is empty, the server is not busy and there is no next departure scheduled.
      busy = false;
      nextDeparture = -1;
    }
    else{
      busy = true;
      nextDeparture = nextDeparture + jobs.peek().size; //add the new job's size to the old next departure time (i.e. the current time) to get the next departure time
    }
    */
   
    return t;
  }
  
  //                                              Getters
  // =================================================================================================
  public String printServerStatus(int mode){
   
    if(mode == 0){
      return "Tables: " + getTableTotal() + " | Guests: " + getGuestTotal() + " / " + serverCapacity;
    }
    else if (mode == 1){
      String s = ""; 
      
      s += "Tables: " + getTableTotal() + " | Guests: " + getGuestTotal() + " / " + serverCapacity + "\n\n";
      
      for(int i = 0; i < tables.size(); i++){
       
        s += i + " | Guests: " + tables.get(i).guests + " | Server Arrival: " + tables.get(i).sArrival + " | Length: " + tables.get(i).length + "\n";
      }
      
      return s;
    }
    else{
      return "error"; 
    }
  }
  
  /**
   * @return Total number of tables
   */
  public int getTableTotal(){
    return tables.size();
  }
  
  /**
   * @return Total guests being served across all tables (i.e. sum and return all job sizes)
   */
  public int getGuestTotal(){
    int size = 0;
    
    for(int i = 0; i < tables.size(); i++){
        size += tables.get(i).guests;
    }
    
    return size;
  }
  
  /**
   * Method calls Server.getGuestTotal()
   * @return The servers' available capacity
   */
  public int getAvailableCapacity() {
    return serverCapacity - getGuestTotal();
  }
  
  /**
   * @return The jobCounter
   */
  public int getJobCounter(){
   return jobCounter; 
  }
  
  /**
   * @return True if the server is busy
   */
  public boolean isBusy() {
    return busy;
  }
  
  /**
   * @return The number of tables
   */
  public int tableCount(){
   return tables.size(); 
  }
}