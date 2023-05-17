import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class AWTExample extends Frame {
   
  ButtonHandler bHandler = new ButtonHandler();
  JButton btn = new JButton("Close");

  public AWTExample() {
	setSize(400, 100);
	setLocation(600, 300);
	
    btn.setBounds(50, 50, 50, 50);
    btn.addActionListener(bHandler);
    add(btn);
    setTitle("This is my First AWT example");
    setLayout(new FlowLayout());
    setVisible(true);
    
    addWindowListener(new WindowAdapter() {
        public void windowClosing(WindowEvent we) {
            dispose();
        }
    });
  }

   class ButtonHandler implements ActionListener {

	  // handle button events
	  public void actionPerformed(ActionEvent event) {

		 if (event.getSource() == btn) 
			System.exit(0);;
	  } 
  }

  public static void main(String args[]){
    new AWTExample();
  } 
}
