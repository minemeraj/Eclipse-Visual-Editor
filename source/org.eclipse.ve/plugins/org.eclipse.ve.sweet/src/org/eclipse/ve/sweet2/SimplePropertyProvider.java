package org.eclipse.ve.sweet2;

import java.lang.reflect.Method;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Text;

public class SimplePropertyProvider implements IPropertyProvider {
	
	private Object fSource;
	private String fPropertyName;
	private Class fReceiverClass;
	private Method fGetMethod;
	private Method fSetMethod;
	protected Class fPropertyType;
	private Text fTextControl;
	private ObjectBinder objectBinder;
	private boolean isSettingValue;
	
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
				Object modelValue = getModelValue((String)value);
				fSetMethod.invoke(fSource,new Object[] {modelValue});
				// Refresh binders so they can update the new value
				objectBinder.propertyChanged(fPropertyName,modelValue);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			isSettingValue = false;
		}
	}

	private Object getModelValue(String value) {
		// Ensure the input is converted to the type of the property
		if(fPropertyType.isInstance(value)){
			return value;
		} else if(fPropertyType == Integer.class || fPropertyType == Integer.TYPE){
			if(value == null || value.length() == 0){
				return new Integer(0);
			} else {
				return new Integer(value);
			}
		} else {
			return value;
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
		TextEditor textViewer = (TextEditor)viewer;
		fTextControl = textViewer.getText();	//TODO - Deal with view changing
		textViewer.getText().setText(getStringValue());	
		isSettingValue = false;			
	}
	
	private Object fromStringValue(String string) {
		if (fPropertyType == Integer.class || fPropertyType == Integer.TYPE){
			return new Integer(string);
		} else {
			return string;
		}
	}
	private String getStringValue() {
		// Do conversion from the domain value to a string
		if(fSource != null){
			return getValue().toString();
		} else {
			return "";
		}
	}

	public void refreshUI() {
		if(!isSettingValue && fTextControl != null){
			fTextControl.setText(getStringValue());
		}
	}

	public void setObjectBinder(ObjectBinder binder) {
		objectBinder = binder;
	}

	public void refreshDomain() {
		if(fTextControl != null){
			setValue(fTextControl.getText());
		}
	}

	public void setSource(Object aSource) {
		fSource = aSource;
		if(fTextControl != null){
			fTextControl.setEnabled(aSource != null);
		}
		refreshUI();
	}

	public boolean isForProperty(String propertyName) {
		return propertyName.equals(fPropertyName);
	}	
}
