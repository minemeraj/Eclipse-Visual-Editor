package org.eclipse.ve.sweet2;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.lang.reflect.Method;

public class PropertyContentConsumer implements IContentConsumer {

	private IObjectBinder fBinder;
	private String fPropertyName;
	private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
	private Method fGetMethod;
	private Method fSetMethod;
	private Class fPropertyType;
	private boolean isSettingValue;
	private PropertyChangeListener propertyChangeListener;
	
	public PropertyContentConsumer(String propertyName){
		fPropertyName = propertyName;
	}

	public PropertyContentConsumer(ObjectBinder binder, String propertyName) {
		fPropertyName = propertyName;
		setObjectBinder(binder);
	}

	public Object getValue() {
		try{
			return getGetMethod().invoke(fBinder.getValue(),null);
		} catch (Exception e){
			e.printStackTrace();
			return null;
		}
	}

	public void setValue(Object aValue) {
		// Invoke the set method on the binder's value
		if(isSettingValue) return;
		try{
			isSettingValue = true;
			getSetMethod().invoke(fBinder.getValue(),new Object[] {aValue});
			fBinder.refresh(fPropertyName);
		} catch (Exception e){
			e.printStackTrace();
		} finally {
			isSettingValue = false;
		}
	}
	
	private Method getSetMethod() {
		if (fSetMethod!=null) return fSetMethod;
		try {
			String setMethodName = EditorUtilities.setMethodName(fPropertyName); 
			fSetMethod = fBinder.getType().getMethod(setMethodName,new Class[] {getPropertyType()});
		} 		
		catch (NoSuchMethodException e) {			
		}
		return fSetMethod;
	}	
	
	private Class getPropertyType() {
		if (fPropertyType!=null) return fPropertyType;
		Method get = getGetMethod();
		fPropertyType = get.getReturnType();
		return fPropertyType;
	}	
	
	private Method getGetMethod() {
		if (fGetMethod!=null) return fGetMethod;
		try {
			String getMethodName = EditorUtilities.getMethodName(fPropertyName); 
			fGetMethod = fBinder.getType().getMethod(getMethodName,null);
		} 		
		catch (NoSuchMethodException e) {
			throw new Error("property '"+fPropertyName+"' is not valid for Class "+fBinder.getType().getName(),e);
		}
		return fGetMethod;
	}	

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		propertyChangeSupport.addPropertyChangeListener(listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		propertyChangeSupport.removePropertyChangeListener(listener);
	}

	public Class getType() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setObjectBinder(IObjectBinder anObjectBinder) {
		if(fBinder != null){
			fBinder.removePropertyChangeListener(propertyChangeListener);
		}
		if(propertyChangeListener == null){
			propertyChangeListener = new PropertyChangeListener(){
				public void propertyChange(PropertyChangeEvent event) {
					if(fPropertyName.equals(event.getPropertyName()) || event.getPropertyName() == null){
						propertyChangeSupport.firePropertyChange(event);
					}
				}
			};		
		};
		
		fBinder = anObjectBinder;		
		fBinder.addPropertyChangeListener(propertyChangeListener);		
	}
}
