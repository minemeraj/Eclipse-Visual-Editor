import java.awt.Button;

/**
 * @author JoeWin
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class CannotApplyButtonLabel extends Button {
	
	public CannotApplyButtonLabel(){
	}
	
	public void setLabel(String aLabel){
		if ( aLabel != null && aLabel.length() > 1 ) {
			throw new RuntimeException("Label length cannot be larger than one");
		}
		super.setLabel(aLabel);
		
	}

}

