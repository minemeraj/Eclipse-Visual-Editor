import java.applet.Applet;
import java.awt.*;

/**
 * @author JoeWin
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 */
public class TestAppletParms extends Applet {
		
	public void init(){
	
		setSize(70,70);
		List list = new List();
		add(list);
		list.setBounds(10,10,50,50);
		// Add the parms "One" and "Two" to the list
		list.add("One = " + getParameter("One"));
		list.add("Two = " + getParameter("Two"));
		
	}

}