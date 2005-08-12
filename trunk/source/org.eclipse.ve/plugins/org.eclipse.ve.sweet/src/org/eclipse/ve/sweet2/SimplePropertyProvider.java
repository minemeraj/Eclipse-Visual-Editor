package org.eclipse.ve.sweet2;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Control;

public class SimplePropertyProvider implements IValueProvider {
	
	private Object fSource;
	private String fPropertyName;
	private Class fReceiverClass;
	private Method fGetMethod;
	private Method fSetMethod;
	protected Class fPropertyType;
	private ObjectBinder objectBinder;
	private boolean isSettingValue;
	private Editor editor;
	private Control control;
	
	public SimplePropertyProvider(Object source, String propertyName){
		fSource = source;
		fPropertyName = propertyName;
		fReceiverClass = source.getClass();
	}
	
	public SimplePropertyProvider(Class aReceiverClass, String propertyName){
		fReceiverClass = aReceiverClass;
		fPropertyName = propertyName;
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
			isSettingValue = true;
			if(fSource != null){
				fSetMethod.invoke(fSource,new Object[] {value});
				// Refresh binders so they can update the new value
				objectBinder.propertyChanged(fPropertyName,value);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			isSettingValue = false;
		}
	}

	public Object getSource() {
		return fSource;
	}

	public void dispose() {		
	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		if(isSettingValue) return;
		isSettingValue = true;		
		editor = (Editor)viewer;
		control = viewer.getControl();	//TODO - Deal with view changing
		viewer.refresh();
		isSettingValue = false;			
	}
	
	public void setObjectBinder(ObjectBinder binder) {
		objectBinder = binder;
	}

	public boolean canSetValue() {
		return fSource != null;
	}

	public void firePropertyChanged() {
		if(!isSettingValue){
			((Viewer)editor).refresh();
		}
	}

	public void setSource(Object aSource) {
		fSource = aSource;
	}
}
