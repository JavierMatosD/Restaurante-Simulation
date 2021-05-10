public class Table{
   
  public int rArrival, sArrival, guests, seatingDelay, serverDelay, length; //Best practice would be to have these be private and define get methods, but I don't think it matters in this instance.
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
    
    return true;
  }
  // ===================================
}