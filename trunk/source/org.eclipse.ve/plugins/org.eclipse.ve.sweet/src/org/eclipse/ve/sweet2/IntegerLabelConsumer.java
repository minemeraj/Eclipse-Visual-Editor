package org.eclipse.ve.sweet2;

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
