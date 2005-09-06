package org.eclipse.ve.sweet.fieldviewer.jface.internal.providers;

public class IntegerLabelConsumer implements ILabelConsumer {
	
	private static Integer ZERO = new Integer(0);

	public Object getObject(String aString) {
		if(aString == null){
			return ZERO;
		} else {
			return new Integer(aString);
		}
	}

}
