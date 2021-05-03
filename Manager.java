import java.util.LinkedList;
import java.util.Queue;
import java.util.ArrayList;

public class Manager{
  
  private ArrayList<Server> servers;
  private Queue<Table> unseatedTables;
  
  // ===================================
  
  public Manager(int serverCount){
        
    servers = new ArrayList<Server>();
    
    for(int i = 0; i < serverCount; i++){
      servers.add(new Server()); 
    }
    
    unseatedTables = new LinkedList<Table>();
  }
  
  // ===================================
  
  public void tableArrival(int guests, int time, int length){ //a new party arrives. We generate a table object for them but do not necessarily seat them yet
    //System.out.println("New table arrival: " + guests);
    unseatedTables.add(new Table(guests, time, length));
  }

  // ===================================
  
  public ArrayList<Table> tick(int time){
    
    ArrayList<Table> allDepartures = new ArrayList<Table>();
    
    for(int i = 0; i < servers.size(); i++){
      allDepartures.addAll(servers.get(i).tick(time));
    }
        
    return allDepartures;
  }
  
  // ===================================
  //Set table assignment strategy to test in this function. Basic greedy function currently which immediately assigns table to least busy server.
  
  public void assignTables(int time){
      
    while(unseatedTables.size() > 0 && servers.get(getLeastBusyServerIndex()).getAvailableCapacity()>0)
      servers.get(getLeastBusyServerIndex()).seatTable(unseatedTables.remove(),time);   
    
    /*
    //This is another possible strategy where no server is ever overburdened, but it is vulnerable to massive backlog if new jobs are coming in too quickly 
    int i = 0;
    
    while(i < servers.size() && unseatedTables.size() > 0){
      
      System.out.println("Unseated tables: " + unseatedTables.size());
      
      if(servers.get(i).getAvailableCapacity() > unseatedTables.peek().guests){
        System.out.println("Seating table with " + unseatedTables.peek().guests + " guests");
        servers.get(i).seatTable(unseatedTables.remove(), time);
        assignTables(time);
        return;
      }
      
      i++;
    }
    */
  }
  
  // ===================================
  // Helper method returning index of least burdened server
  
  private int getLeastBusyServerIndex(){
    
    int leastBusy = 0;
    
    for(int i = 0; i < servers.size(); i++){
      
      if(servers.get(i).getAvailableCapacity() > servers.get(leastBusy).getAvailableCapacity()){
        leastBusy = i;
      }
    } 
    return leastBusy;
  }
  
  // ===================================
  
  public String printRestaurantStatus(int time){
    
    String s = "";
    
    s += ("===================\nTime:" + time + "\n");
    s += ("Unseated tables: " + unseatedTables.size() + "\n\n");
    
    for(int i = 0; i < servers.size(); i++){
     
      s += (servers.get(i).printServerStatus() + "\n");
    }
    
    return s;
  }
  
  // ===================================
  
  public ArrayList<Server> getServers(){
   
    return servers;
  }
  
  // ===================================
  
  public Queue<Table> getUnseatedTables(){
   
    return unseatedTables;
  }
}