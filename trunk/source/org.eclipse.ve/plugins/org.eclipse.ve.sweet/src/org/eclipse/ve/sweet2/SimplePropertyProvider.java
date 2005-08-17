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
		setSourceBinder((ObjectBinder) source);
	}

	public Object getValue() {
		if(fGetMethod == null){
			initializeGetMethod();
		}
		try {
			if(objectBinder.getValue() != null){
				return fGetMethod.invoke(objectBinder.getValue(),null);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;		
	}
	
	private void initializeGetMethod(){
		try{
			String getMethodName = getMethodName(fPropertyName); 
			fGetMethod = fReceiverClass.getMethod(getMethodName,null);
			if(fPropertyType == null){
				fPropertyType = fGetMethod.getReturnType();
			}
		} catch (NoSuchMethodException exc){
			exc.toString();
		}		
	}

	public void setValue(Object value) {
		if(fPropertyType == null){
			initializeGetMethod();
		}
		if(fSetMethod == null){
			try{
				String setMethodName = setMethodName(fPropertyName); 
				fSetMethod = fReceiverClass.getMethod(setMethodName,new Class[] {fPropertyType}); 
			} catch (NoSuchMethodException exc){
			}
		}
		try {
			isSettingValue = true;
			if(objectBinder != null){
				fSetMethod.invoke(objectBinder.getValue(),new Object[] {value});
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
		return objectBinder.getValue() != null;
	}

	public Class getType() {
		if(fGetMethod == null){
			initializeGetMethod();
		}
		return fGetMethod.getReturnType();
	}

	public String toString() {
		StringBuffer buffer = new StringBuffer(super.toString());
		buffer.append('(');
		buffer.append(fPropertyName);
		buffer.append('-');
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
