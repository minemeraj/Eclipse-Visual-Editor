package org.eclipse.ve.sweet2;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.Method;

public class NestedPropertyProvider extends AbstractPropertyProvider {
	
	private String[] fNestedProperties;
	private Class fType;
	private Method[] fGetMethods;
	private Method fSetMethods;
	private IObjectBinder[] fBinders;
	private IObjectBinder sourceBinder;
	private Method fSetMethod;

	public NestedPropertyProvider(IObjectBinder aSourceBinder, String[] nestedProperties){
		//TODO: <gm> need to support subscripts ...e.g. manager.backup[2].firstName</gm>
		
		// aType is the type of object that is our source, e.g. Person
		// The nested properties are the properties we should get from it, e.g. manager.firstName
		fNestedProperties = nestedProperties;
		fBinders = new IObjectBinder[nestedProperties.length];
		fBinders[0] = aSourceBinder;
		objectBinder = aSourceBinder;
		initialize();
	}
		
	public boolean canSetValue(){
		IObjectBinder valBinder = fBinders[fNestedProperties.length-1];
		return valBinder!=null && 
		       valBinder.getValue()!=null && 
		       getSetMethod()!=null; 	       
	}

	public Object getValue() {
		try{
			boolean deadEnd=false;
			for (int i = 0; i < fGetMethods.length; i++) {
				Object binderValue = null;
				if (!deadEnd) {				  				  
				  binderValue = fBinders[i].getValue();
				  deadEnd = binderValue == null;
				}
				Object binderResult  = deadEnd ? null : fGetMethods[i].invoke(binderValue,null);				
				// If we are the last person in the array we return the result
				if(i == fGetMethods.length -1){
					return binderResult;
				} else {
					// Otherwise set the value into the next binder so the get methods can become chained
					fBinders[i+1].setValue(binderResult);
					// Listen to the binder so that when its value changes we can update ourself					
				}				
			}
		} catch (Exception exc){
			exc.printStackTrace();
		}
		return null;
	}
	
	private Method getSetMethod() {
		if (fSetMethod!=null) return fSetMethod;
		try {
			
			int index = fNestedProperties.length-1;
			String setMethodName = setMethodName(fNestedProperties[index]);	 
			fSetMethod = fBinders[index].getType().getMethod(
				setMethodName,
				new Class[] {fGetMethods[index].getReturnType()});			
		} 		
		catch (NoSuchMethodException e) {			
			// A read only property will not have a set value
		}
		return fSetMethod;
	}
	

	public void setValue(Object aValue) {
		int index = fNestedProperties.length-1;
		Object target = fBinders[index].getValue();
		if (target==null)  
			return; // No value to set
		
		try{
			isSettingValue = true;
			Method setMethod = getSetMethod(); 			
			if(setMethod == null){
				throw new Error ("Invalid Set Method:"+fBinders[0].getType().getName()+", property:"+getProperty());
			}			
			setMethod.invoke(target,new Object[] {aValue});
			// Refresh binders so they can update the new value
			fBinders[index].refresh(fNestedProperties[index]);			
		} catch (Exception exc){
			throw new Error (exc);
		} finally {
			isSettingValue = false;			
		}
	}

	public Class getType() {
		return fBinders[fNestedProperties.length-1].getType();
	}
	private void initialize(){
		fGetMethods = new Method[fNestedProperties.length];
		try{
			// fGetMethods is an array of methods that can be used to traverse each binder
			// 	fSources is an array of binders
			// We start with our aType that is the class of the object that is giving us the first property
			for (int i = 0; i < fNestedProperties.length; i++) {
				String getMethodName = getMethodName(fNestedProperties[i]);
				Method getMethod = fBinders[i].getType().getMethod(getMethodName,null);
				fGetMethods[i] = getMethod;
				// Alongside each getMethod is a binder for the source on which it will be invoked
				if(i < fNestedProperties.length - 1){
					IObjectBinder binder = ObjectBinder.createObjectBinder(getMethod.getReturnType());
					fBinders[i+1] = binder;
				}
			}
		} catch (Exception exc){
			throw new Error("Class:"+fBinders[0].getType().getName()+", property:"+getProperty(),exc);
		}	
		// Listen to the source binder so when its value changes we can notify listeners
		fBinders[0].addPropertyChangeListener(new PropertyChangeListener(){
			public void propertyChange(PropertyChangeEvent evt) {
				// If the object that changed is the one we are display
				if(!isSettingValue){
					signalListeners();
				}
			}
		});
	}

	protected String getPropertyName() {
		return fNestedProperties[fNestedProperties.length-1]; 
	}
	
	private String getProperty() {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < fNestedProperties.length; i++) {
			if (i>0)
				sb.append('.');
			sb.append(fNestedProperties[i]);			
		}
		return sb.toString();
	}
	public String toString() {		
		return fBinders[0].getType().getName()+"\n"+getProperty();
	}

}
