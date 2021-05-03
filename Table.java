public class Table{
 
  //TO DO: keep track of seating delay AND delay due to server overload
  
  public int rArrival, sArrival, guests, seatingDelay, serverDelay, length; //Best practice would be to have these be private and define get methods, but I don't think it matters in this instance.

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
  
  public void seated(double serverMultiplier, int time){
    sArrival = time; //server arrival is the current time
    seatingDelay = sArrival-rArrival; //restaurant delay = delay between arrival at restaurant and being seated
    serverDelay = (int)(length*serverMultiplier)-length; //server delay = difference between actual length with server delay and original length party would have stayed at restaurant
    length = (int)(length * serverMultiplier); //update length to reflect server delay
  }
  
  // ===================================
}