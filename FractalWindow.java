import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;

public class FractalWindow extends JFrame implements ActionListener, ChangeListener {
	
	public static JButton reset, draw;
	public static Label coordinates;
	private Label threshholdLabel;
	private JPanel threshholdPanel;
	private JSlider threshholdSlider;
	public static BoardArea gameBoard;
	public int height, width;
	private int minThresh = 0;
	private int maxThresh = 30;
	private int avgThresh = 10;
	private boolean mShown;
	
	public FractalWindow( int H, int W ){
		
		height = H;  width = W;
		gameBoard = new BoardArea( this, height, width );
		ButtonHandler bHandler = new ButtonHandler();
		
		
		//create  GUI elements
		JPanel boardPanel = new JPanel();
		boardPanel.setLayout( new BorderLayout() );
			boardPanel.add( gameBoard, BorderLayout.NORTH );
			coordinates = new Label( " " );
			boardPanel.add( coordinates, BorderLayout.SOUTH );
			BoardArea.coordinates = coordinates;
			
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout( new GridLayout(1,4) );
		
			reset = new JButton( "Reset" );
			reset.addActionListener( bHandler );
			buttonPanel.add( reset );
			
			draw = new JButton( "Draw" );
			draw.addActionListener( bHandler );
			buttonPanel.add( draw );
		
		threshholdPanel = new JPanel();
		threshholdPanel.setLayout( new GridLayout(2,1) );
				
			threshholdSlider = new JSlider(SwingConstants.HORIZONTAL,
                                  minThresh, maxThresh, avgThresh);
		    threshholdSlider.addChangeListener(this);
		    threshholdSlider.setMajorTickSpacing(10);
		    threshholdSlider.setMinorTickSpacing(5);
		    threshholdSlider.setPaintTicks(true);
		    threshholdSlider.setPaintLabels(true);
		    
		    threshholdLabel = new Label( "               Threshhold: " + "10" );
			threshholdPanel.add( threshholdLabel);
			threshholdPanel.add( threshholdSlider );
		
		buttonPanel.add( threshholdPanel );
			
		getContentPane().setLayout( new BorderLayout() );
		getContentPane().add( boardPanel, BorderLayout.CENTER );
		getContentPane().add( buttonPanel, BorderLayout.NORTH );
			
		addWindowListener( new WindowAdapter()
        {
            public void windowClosing( WindowEvent e ) {
                //file_exit_action();
            }
        } );
        
		pack();
		
		
	}
        
	
	public void addNotify() 
	{
		super.addNotify();
		
		if (mShown)
			return;
		
		mShown = true;
	}

	// Close the window when the close box is clicked
	void thisWindowClosing(java.awt.event.WindowEvent e)	{
		setVisible(false);
		dispose();
		System.exit(0);
	}
		
	class ButtonHandler implements ActionListener  {
		
	  public void actionPerformed( ActionEvent event ) {
	  			
		if( event.getSource() == reset ) {
			
			gameBoard.reset();

		} else if( event.getSource() == draw ) {
			
			gameBoard.draw();

		}
	  }
	}
	
   /**
   *  Processes threshhold slider events.
   */
  public void stateChanged(ChangeEvent e)  {
  	
    gameBoard.setThreshhold( threshholdSlider.getValue() );
    threshholdLabel.setText( "               Threshhold: " + threshholdSlider.getValue() );
	gameBoard.repaint();
  }
  
  /**
   * not implemented
   */
  public void actionPerformed(ActionEvent e)
  {  }

}
