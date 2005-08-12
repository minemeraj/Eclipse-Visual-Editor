package org.eclipse.ve.sweet2;

public class IntegerDomainProvider implements IDomainProvider {
	
	private static final Integer ZERO = new Integer(0);

	public Object getValue(String text) {
		if(text == null || text.length() == 0){
			return ZERO;
		}
		return new Integer(text);
	}

}
