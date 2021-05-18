public class Table{
   
  public int rArrival, sArrival, guests, seatingDelay, serverDelay, length; //Since this is a helper class we will let these variables be public rather than writing get methods for them
  private final int seatingAbandonment = 2; //customers willing to wait up to 2 hours to be seated
  //private final serverAbandonmentMultiplier = 1.5;
  
  // ===================================
  /**
   * Constructor 
   * @param g = number of guest
   * @param a = arrival time
   * @param l = length of time the party will stay
   */
  public Table(int g, int a, int l){
    guests   = g;
    rArrival = a; //restaurant arrival is the given time
    length   = l;
  }
  
  // ===================================
  /**
   * Updates the tables' arrival time, seating delay, server delay, length of service
   * @param serverMultiplier 
   * @param time = current time
   * @return 
   */
  public boolean seated(double serverMultiplier, int time){
    sArrival = time;
    seatingDelay = sArrival-rArrival; //restaurant delay = delay between arrival at restaurant and being seated
    
    if(seatingDelay > seatingAbandonment)
      return false;
    
    serverDelay = (int)(length*serverMultiplier)-length; //server delay = difference between actual length with server delay and original length party would have stayed at restaurant
    length      = (int)(length * serverMultiplier); //update length to reflect server delay
    
    /*if(serverDelay > seatingAbandonment)
     return false;
    */
    
    return true;
  }
  // ===================================
}