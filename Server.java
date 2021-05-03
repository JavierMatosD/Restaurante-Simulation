import java.util.ArrayList;

public class Server{
 
  private final int serverCapacity = 15; //hard coded server capacity before service efficiency begins decreasing
  private ArrayList<Table> tables;
  private boolean busy;
  private int jobCounter;
  private int nextDeparture;
  
  // ===================================
  // Constructor which initializes an empty server with no jobs yet sent. After this constructor is called, jobArrival may be called to simulate a job arrival.
  public Server(){
    tables = new ArrayList<Table>();
    busy = false;
    jobCounter = 0;
    nextDeparture = -1;
  }
  
  public ArrayList<Table> tick(int time){
    
    ArrayList<Table> departures = new ArrayList<Table>();
    
    for(int i = 0; i < tables.size(); i++){
      
      if(tables.get(i).sArrival+tables.get(i).length == time){
        departures.add(departTable(i));
      }
    }
    
    return departures;
  }
  
  // ===================================
  // this method is called when a table is seated by a server
  public void seatTable(Table t, int time){
    
    double serverMultiplier = (1.0*t.guests + this.getGuestTotal())/serverCapacity;
    
    if(serverMultiplier < 1.0)
      serverMultiplier = 1.0;
    
    t.seated(serverMultiplier,time);
    tables.add(t);
    jobCounter++; //Increment job counter
  }
  
  // ===================================
  //departure
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
  
  // ===================================
  // All get methods
  
  public String printServerStatus(){
   
    return "Tables: " + getTableTotal() + " | Guests: " + getGuestTotal() + " / " + serverCapacity;
  }
  
  // =================================== 
  
  public int getTableTotal(){
    
    return tables.size();
  }
  
  // Calculates total guests being served across all tables (i.e. sum and return all job sizes)
  public int getGuestTotal(){
    int size = 0;
    
    for(int i = 0; i < tables.size(); i++){

        size += tables.get(i).guests;
    }
    
    return size;
  }
  
  // ===================================
  
  public int getAvailableCapacity(){
    
    return serverCapacity-getGuestTotal();
  }
  
  // ===================================
  
  public int getJobCounter(){
   return jobCounter; 
  }
  
  // ===================================
  
  public boolean isBusy(){
    return busy;
  }
  
  // ===================================
  
  public int tableCount(){
   return tables.size(); 
  }
}