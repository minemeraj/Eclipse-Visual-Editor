package org.eclipse.ve.sweet2;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.lang.reflect.Method;

public class ObjectConsumerProvider extends AbstractObjectContentProvider  implements IElementContentProvider , IContentConsumer {

	private boolean isSettingValue;
	private Method fSetMethod;
	private Class fPropertyType;
	private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);	

	public ObjectConsumerProvider(String aPropertyName) {
		super(aPropertyName);
	}
	
	public Object getElement(Object input){
		return lookupPropertyValue(input);
	}

	public Object getValue() {
		return lookupPropertyValue(null);
	}

	public void setValue(Object aValue) {
		if(isSettingValue) return;
		try{
			isSettingValue = true;
			getSetMethod().invoke(fBinders[fBinders.length-1].getValue(),new Object[] {aValue});
			fBinders[fBinders.length-1].refresh(fPropertyNames[fPropertyNames.length-1]);
		} catch (Exception e){
			e.printStackTrace();
		} finally {
			isSettingValue = false;
		} 
	}
	
	private Method getSetMethod() {
		if (fSetMethod!=null) return fSetMethod;
		try {
			String setMethodName = EditorUtilities.setMethodName(fPropertyNames[fPropertyNames.length-1]); 
			fSetMethod = fBinders[fBinders.length-1].getType().getMethod(setMethodName,new Class[] {getType()});
		} 		
		catch (NoSuchMethodException e) {			
		}
		return fSetMethod;
	}		


	public void addPropertyChangeListener(PropertyChangeListener listener) {
		propertyChangeSupport.addPropertyChangeListener(listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		propertyChangeSupport.removePropertyChangeListener(listener);
	}

	public void ouputChanged(Object anObjectBinder) {
		// Ignore as the input and output are the same
	}

	public Class getType() {
		if (fPropertyType!=null) return fPropertyType;
		Method get = fGetMethods[fGetMethods.length-1];
		fPropertyType = get.getReturnType();
		return fPropertyType;			
	}
}
