package org.eclipse.ve.sweet2;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Control;

public class SimplePropertyProvider implements IValueProvider {
	
	private IValue fSource;
	private String fPropertyName;
	private Class fReceiverClass;
	private Method fGetMethod;
	private Method fSetMethod;
	protected Class fPropertyType;
	private ObjectBinder objectBinder;
	private boolean isSettingValue;
	private Editor editor;
	private Control control;
	
	public SimplePropertyProvider(IValue source, String propertyName){
		fPropertyName = propertyName;
		fSource = source;		
		fReceiverClass = source.getType();
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
			if(fSource.getValue() != null){
				return fGetMethod.invoke(fSource.getValue(),null);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;		
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
			exc.toString();
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
				fSetMethod.invoke(fSource.getValue(),new Object[] {value});
				// Refresh binders so they can update the new value
				objectBinder.refresh(fPropertyName);
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
		objectBinder.addPropetyChangeListener(new PropertyChangeListener(){
			public void propertyChange(PropertyChangeEvent evt) {
				if(!isSettingValue){
					((Viewer)editor).refresh();
				}
			}			
		});
	}

	public boolean canSetValue() {
		return fSource.getValue() != null;
	}

	public Class getType() {
		if(fGetMethod == null){
			initializeGetMethod();
		}
		return fGetMethod.getReturnType();
	}

}
