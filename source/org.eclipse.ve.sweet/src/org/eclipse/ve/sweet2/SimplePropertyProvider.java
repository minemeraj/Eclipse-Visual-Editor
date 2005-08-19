package org.eclipse.ve.sweet2;

import java.lang.reflect.Method;


public class SimplePropertyProvider extends AbstractPropertyProvider {
	
	String fPropertyName;
	private Class fReceiverClass;
	private Method fGetMethod;
	private Method fSetMethod;
	public SimplePropertyProvider(IObjectBinder source, String propertyName){
		fPropertyName = propertyName;		
		fReceiverClass = source.getType();
		setSourceBinder(source);
	}

	public Object getValue() {
		try {
			if(objectBinder.getValue() != null){
				return getGetMethod().invoke(objectBinder.getValue(),null);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;		
	}
		
	private Method getGetMethod() {
		if (fGetMethod!=null) return fGetMethod;
		try {
			String getMethodName = getMethodName(fPropertyName); 
			fGetMethod = fReceiverClass.getMethod(getMethodName,null);
		} 		
		catch (NoSuchMethodException e) {
			throw new Error("property '"+fPropertyName+"' is not valid for Class "+fReceiverClass.getName(),e);
		}
		return fGetMethod;
	}
	
	private Method getSetMethod() {
		if (fSetMethod!=null) return fSetMethod;
		try {
			String setMethodName = setMethodName(fPropertyName); 
			fSetMethod = fReceiverClass.getMethod(setMethodName,new Class[] {getPropertyType()});
		} 		
		catch (NoSuchMethodException e) {			
			// A read only property will not have a set value
		}
		return fSetMethod;
	}
	
	private Class getPropertyType() {
		if (fPropertyType!=null) return fPropertyType;
		Method get = getGetMethod();
		fPropertyType = get.getReturnType();
		return fPropertyType;
	}

	public void setValue(Object value) {
		try {
			isSettingValue = true;
			if(canSetValue()){
				getSetMethod().invoke(objectBinder.getValue(),new Object[] {value});
				// Refresh binders so they can update the new value
				objectBinder.refresh(fPropertyName);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			isSettingValue = false;
		}
	}

	public boolean canSetValue() {
		return objectBinder != null && 
		       objectBinder.getValue() != null && 
		       getSetMethod()!=null;
	}

	public Class getType() {
		return getPropertyType();
	}

	public String toString() {
		StringBuffer buffer = new StringBuffer(super.toString());
		buffer.append("\n(");
		buffer.append(fPropertyName);
		buffer.append(" - ");
		buffer.append(objectBinder);
		buffer.append(')');		
		return buffer.toString();
	}

	public void refresh(){
		signalListeners();
	}

	protected String getPropertyName() {
		return fPropertyName;
	}

}
