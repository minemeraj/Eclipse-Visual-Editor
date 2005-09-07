package org.eclipse.ve.sweet2;

import org.eclipse.jface.viewers.LabelProvider;

public class IntegerLabelConsumerProvider extends LabelProvider implements ILabelConsumer {
	
	private static Integer ZERO = new Integer(0);

	public Object getObject(String aString) {
		if(aString == null){
			return ZERO;
		} else {
			return new Integer(aString);
		}
	}

}
