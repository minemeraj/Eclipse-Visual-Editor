package org.eclipse.ve.sweet2;

import java.beans.Introspector;
import java.lang.reflect.Method;

import org.eclipse.jface.viewers.Viewer;

public class SimplePropertyProvider implements IPropertyProvider {
	
	private Object fSource;
	private String fPropertyName;
	private Class fReceiverClass;
	private Method fGetMethod;
	private Method fSetMethod;
	private Class fPropertyType;
	
	public SimplePropertyProvider(Object source, String propertyName){
		fSource = source;
		fPropertyName = propertyName;
		fReceiverClass = source.getClass();
	}

	public Object getValue() {
		if(fGetMethod == null){
			initializeGetMethod();
		}
		try {
			return fGetMethod.invoke(fSource,null);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private void initializeGetMethod(){
		try{
			StringBuffer getMethodName = new StringBuffer();
			getMethodName.append("get");
			getMethodName.append(fPropertyName.substring(0,1).toUpperCase());
			getMethodName.append(fPropertyName.substring(1));
			fGetMethod = fReceiverClass.getMethod(getMethodName.toString(),null);
			if(fPropertyType == null){
				fPropertyType = fGetMethod.getReturnType();
			}
		} catch (NoSuchMethodException exc){
		}		
	}

	public void setValue(Object value) {
		if(fPropertyType == null){
			initializeGetMethod();
		}
		if(fSetMethod == null){
			try{
				StringBuffer setMethodName = new StringBuffer();
				setMethodName.append("set");
				setMethodName.append(fPropertyName.substring(0,1).toUpperCase());
				setMethodName.append(fPropertyName.substring(1));
				fSetMethod = fReceiverClass.getMethod(setMethodName.toString(),new Class[] {fPropertyType});
			} catch (NoSuchMethodException exc){
			}
		}
		try {
			fSetMethod.invoke(fSource,new Object[] {value});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Object getSource() {
		return fSource;
	}

	public void dispose() {		
	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		TextViewer textViewer = (TextViewer)viewer;
		textViewer.getText().setText(getStringValue());		
	}
	private String getStringValue() {
		return getValue().toString();
	}	
}
