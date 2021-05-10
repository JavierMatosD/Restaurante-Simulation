public class Table{
 
  //TO DO: keep track of seating delay AND delay due to server overload

  //Best practice would be to have these be private and define get methods, but I don't think it matters in this instance.
  public int rArrival, sArrival, guests, seatingDelay, serverDelay, length;
  
  /**
   * Constructor 
   * @param numGuests    = the number of guest (job size)
   * @param arrivalTime  = arrival time at restaurant 
   * @param lengthOfStay = length the guests will stay at the restaurant after being seated, assuming no delay due to slow service
   */
  public Table(int numGuests, int arrivalTime, int lengthOfStay) {
    guests   = numGuests;
    rArrival = arrivalTime;
    length   = lengthOfStay;
  }

  /**
   * Method updates the tables arrival time, seating delay, server delay, and length of stay. 
   * @param serverMultiplier
   * @param time = current time
   */
  public void seated(double serverMultiplier, int time){
    this.sArrival     = time; //server arrival is the current time
    this.seatingDelay = sArrival-rArrival; //restaurant delay = delay between arrival at restaurant and being seated
    this.serverDelay  = (int)(length*serverMultiplier)-length; //server delay = difference between actual length with server delay and original length party would have stayed at restaurant
    this.length       = (int)(length * serverMultiplier); //update length to reflect server delay
  }

}