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
		// aType is the type of object that is our source, e.g. Person
		// The nested properties are the properties we should get from it, e.g. manager.firstName
		fNestedProperties = nestedProperties;
		fBinders = new IObjectBinder[nestedProperties.length];
		fBinders[0] = aSourceBinder;
		objectBinder = aSourceBinder;
		initialize();
	}
		
	public boolean canSetValue(){
		return true;
	}

	public Object getValue() {
		try{
			for (int i = 0; i < fGetMethods.length; i++) {
				Method getMethod = fGetMethods[i];
				IObjectBinder binder = fBinders[i];
				Object binderValue = binder.getValue();
				if(binderValue != null){
					Object binderResult = getMethod.invoke(binderValue,null);
					// If we are the last person in the array we return the result
					if(i == fGetMethods.length -1){
						return binderResult;
					} else {
						// Otherwise set the value into the next binder so the get methods can become chained
						fBinders[i+1].setValue(binderResult);
						// Listen to the binder so that when its value changes we can update ourself
					}
				}
			}
		} catch (Exception exc){
			exc.printStackTrace();
		}
		return null;
	}
	

	public void setValue(Object aValue) {
		try{
			isSettingValue = true;			
			if(fSetMethod == null){
				// 	There is one set method that is fired on the immediate binder
				int binderNumbers = fNestedProperties.length;
				String setMethodName = setMethodName(fNestedProperties[binderNumbers-1]);	 
				fSetMethod = fBinders[binderNumbers-1].getType().getMethod(
					setMethodName,
					new Class[] {fGetMethods[binderNumbers-1].getReturnType()});
			}
			fSetMethod.invoke(fBinders[fBinders.length-1].getValue(),new Object[] {aValue});
			// Refresh binders so they can update the new value
			fBinders[fBinders.length-1].refresh(fNestedProperties[fNestedProperties.length-1]);			
		} catch (Exception exc){
			exc.printStackTrace();
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
			exc.toString();
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

}
