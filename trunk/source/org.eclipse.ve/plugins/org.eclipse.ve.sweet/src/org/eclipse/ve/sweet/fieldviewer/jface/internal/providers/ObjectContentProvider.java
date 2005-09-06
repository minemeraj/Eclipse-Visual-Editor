package org.eclipse.ve.sweet.fieldviewer.jface.internal.providers;


public class ObjectContentProvider extends AbstractObjectContentProvider  implements IElementContentProvider  {

	public ObjectContentProvider(String aPropertyName) {
		super(aPropertyName);
	}
	
	public Object getElement(Object input){
		return lookupPropertyValue(input);
	}
	
}
