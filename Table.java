public class Table{
   
  public int rArrival, sArrival, guests, seatingDelay, serverDelay, length; //Best practice would be to have these be private and define get methods, but I don't think it matters in this instance.
  private final int seatingAbandonment = 2; //customers willing to wait up to 2 hours to be seated
  //private final serverAbandonmentMultiplier = 1.5;
  
  // ===================================
  // g = number of guests ie job size
  // a = arrival time at restaurant
  // l = length the guests will stay at the restaurant after being seated, assuming no delay due to slow service
  public Table(int g, int a, int l){
    guests = g;
    rArrival = a; //restaurant arrival is the given time
    length = l;
    
    //System.out.println(length);
  }
  
  // ===================================
  
  public boolean seated(double serverMultiplier, int time){
    sArrival = time; //server/seating arrival is the current time
    seatingDelay = sArrival-rArrival; //restaurant delay = delay between arrival at restaurant and being seated
    
    if(seatingDelay > seatingAbandonment){
      
      return false;
    }
    
    serverDelay = (int)(length*serverMultiplier)-length; //server delay = difference between actual length with server delay and original length party would have stayed at restaurant
    length = (int)(length * serverMultiplier); //update length to reflect server delay
    
    return true;
  }
  // ===================================
}