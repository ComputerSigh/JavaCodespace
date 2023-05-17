public interface FiniteStateMachine {
   
  public static final int DRAWING		= 1;
  public static final int SETTING_POINTS = 0;
  
  public void setState( int s );
  
  public int getState();

}
