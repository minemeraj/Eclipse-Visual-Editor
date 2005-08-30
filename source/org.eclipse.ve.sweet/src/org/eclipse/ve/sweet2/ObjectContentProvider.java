package org.eclipse.ve.sweet2;

public class ObjectContentProvider extends AbstractObjectContentProvider  implements IElementContentProvider  {

	public ObjectContentProvider(String aPropertyName) {
		super(aPropertyName);
	}
	
	public Object getElement(Object input){
		return lookupPropertyValue(input);
	}
	
}
