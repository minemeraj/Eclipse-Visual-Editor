package org.eclipse.ve.sweet2;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.lang.reflect.Method;
import java.util.StringTokenizer;

public class ObjectContentConsumer implements IContentConsumer {

	private IObjectDelegate[] fBinders; //e.g. {ObjectBinder(Person.class),ObjectBinder(Person.class)}
	private String[] fPropertyNames; // e.g.  {"manager","firstName"}
	private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
	private Method fSetMethod; //e.g. setFirstName(String aString)
	private Method[] fGetMethods; //e.g. {setManager(Manager),setFirstName(String)}
	private Class fPropertyType;
	private boolean isSettingValue;
	private PropertyChangeListener propertyChangeListener;
	
	public ObjectContentConsumer(String aPropertyName){
		int indexOfPeriod = aPropertyName.indexOf('.');
		if(indexOfPeriod == -1){
			fPropertyNames = new String[] {aPropertyName};
			fBinders = new IObjectDelegate[1];
		} else {
			StringTokenizer tk = new StringTokenizer(aPropertyName,".");
			// If the property is nested we bind to a property provider that can give this to us
			fPropertyNames = new String[tk.countTokens()];
			fBinders = new IObjectDelegate[fPropertyNames.length];
			for (int i = 0; tk.hasMoreTokens(); i++) {
				fPropertyNames[i] = tk.nextToken();				
			}
		}		
	}

	public ObjectContentConsumer(ObjectDelegate binder, String propertyName) {
		this(propertyName);
		ouputChanged(binder);
	}

	public Object getValue() {
		if(fGetMethods == null) return null;
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

	public void setValue(Object aValue) {
		getValue(); // TODO - Need better way to ensure that the binder's contents are initialized
		// Invoke the set method on the binder's value
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
			fSetMethod = fBinders[fBinders.length-1].getType().getMethod(setMethodName,new Class[] {getPropertyType()});
		} 		
		catch (NoSuchMethodException e) {			
		}
		return fSetMethod;
	}	
	
	private Class getPropertyType() {
		if (fPropertyType!=null) return fPropertyType;
		Method get = fGetMethods[fGetMethods.length-1];
		fPropertyType = get.getReturnType();
		return fPropertyType;
	}	

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		propertyChangeSupport.addPropertyChangeListener(listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		propertyChangeSupport.removePropertyChangeListener(listener);
	}

	public Class getType() {
		// TODO
		return null;
	}
	
	private void initialize(){
		fGetMethods = new Method[fPropertyNames.length];
		try{
			// fGetMethods is an array of methods that can be used to traverse each binder
			// 	fSources is an array of binders
			// We start with our aType that is the class of the object that is giving us the first property
			for (int i = 0; i < fPropertyNames.length; i++) {
				String getMethodName = EditorUtilities.getMethodName(fPropertyNames[i]);
				Method getMethod = fBinders[i].getType().getMethod(getMethodName,null);
				fGetMethods[i] = getMethod;
				// Alongside each getMethod is a binder for the source on which it will be invoked
				if(i < fPropertyNames.length - 1){
					IObjectDelegate binder = ObjectDelegate.createObjectBinder(getMethod.getReturnType());
					fBinders[i+1] = binder;
				}
			}
			// fBinders[0] contains our source
			// To populate the remaining binders we need to get the value
			if(fBinders.length > 1){
				getValue();
			}
		} catch (Exception exc){
//			throw new Error("Class:"+fBinders[0].getType().getName()+", property:"+getProperty(),exc);
		}		
	}

	public void ouputChanged(Object anObjectDelegate) {
		if(fBinders[0] != null){
			fBinders[0].removePropertyChangeListener(propertyChangeListener);
		}
		if(propertyChangeListener == null){
			propertyChangeListener = new PropertyChangeListener(){
				public void propertyChange(PropertyChangeEvent event) {
					if(fPropertyNames[fPropertyNames.length-1].equals(event.getPropertyName()) || event.getPropertyName() == null){
						propertyChangeSupport.firePropertyChange(event);
					}
				}
			};		
		};
		if(anObjectDelegate instanceof IObjectDelegate){
			fBinders[0] = (IObjectDelegate)anObjectDelegate;
		} else {
			fBinders[0] = ObjectDelegate.createObjectBinder(anObjectDelegate.getClass());
			fBinders[0].setValue(anObjectDelegate);
		}
		fBinders[0].addPropertyChangeListener(propertyChangeListener);		
		initialize();
	}
}
