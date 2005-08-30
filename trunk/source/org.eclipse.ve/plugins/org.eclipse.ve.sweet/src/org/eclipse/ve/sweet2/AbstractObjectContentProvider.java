package org.eclipse.ve.sweet2;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.Method;
import java.util.StringTokenizer;

import org.eclipse.jface.viewers.Viewer;

import org.eclipse.ve.sweet2.ObjectDelegate.ChangeListener;

public abstract class AbstractObjectContentProvider {
	
	private String[] fPropertyNames;
	private Object viewerInput;
	protected Method[] fGetMethods;
	private ChangeListener changeListener;
	protected IObjectDelegate[] fBinders;
	private PropertyChangeListener[] fPropertyChangeListeners;
	private Viewer fViewer;

	public AbstractObjectContentProvider(String aPropertyName){
		if(aPropertyName != null){
			int indexOfPeriod = aPropertyName.indexOf('.');
			if(indexOfPeriod == -1){
				fPropertyNames = new String[] {aPropertyName};
				fBinders = new IObjectDelegate[1];
				} else {
				StringTokenizer tk = new StringTokenizer(aPropertyName,".");
			// 	If the property is nested we bind to a property provider that can give this to us
				fPropertyNames = new String[tk.countTokens()];
				fBinders = new IObjectDelegate[fPropertyNames.length];
				for (int i = 0; tk.hasMoreTokens(); i++) {
					fPropertyNames[i] = tk.nextToken();				
				}
			}
		} else {
			fBinders = new IObjectDelegate[1];			
		}
	}

	public void dispose() {
		
	}
	
	private void initialize(){
		if(fPropertyNames == null) return;
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
		} catch (Exception exc){
//			throw new Error("Class:"+fBinders[0].getType().getName()+", property:"+getProperty(),exc);
		}	
	}
	
	protected Object lookupPropertyValue(Object anInput){		
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
	
	class ConsumerListener implements PropertyChangeListener{
		String fPropertyName;
		ConsumerListener(String propertyName){
			fPropertyName = propertyName;
		}
		public void propertyChange(PropertyChangeEvent event) {
			// 	If the object that changed is the one we are display
			if(fPropertyName.equals(event.getPropertyName()) || event.getPropertyName() == null){
				fViewer.refresh();
			}
		}
	}	
	
	public void inputChanged(final Viewer viewer, Object oldInput, Object newInput) {
		// Listen to the input and refresh the viewer whenever the registered property changes
		if(newInput == null) return;
		fViewer = viewer;
		if(newInput instanceof IObjectDelegate){				
			fBinders[0] = (IObjectDelegate)newInput;
		} else if (newInput != null) {
			fBinders[0] = ObjectDelegate.createObjectBinder(newInput.getClass());		
			fBinders[0].setValue(newInput);
		}
		viewerInput = fBinders[0].getValue();
		initialize();
		// 	Listen to the source binder so when its value changes we can notify listeners
		if(fPropertyChangeListeners == null){
			fPropertyChangeListeners = new PropertyChangeListener[fBinders.length];
			for (int i = 0; i < fBinders.length; i++) {
				fPropertyChangeListeners[i] = new ConsumerListener(fPropertyNames[i]);
				fBinders[i].addPropertyChangeListener(fPropertyChangeListeners[i]);					
			}					
		}
		if(changeListener != null && oldInput != null){
			ObjectDelegate.removeListener(oldInput.getClass(),changeListener);
		}
		
		if(changeListener == null){
			changeListener = new ChangeListener(){
				public void objectAdded(Object newObject) {				
				}
				public void objectRemove(Object oldObject) {				
				}
				public void objectChanged(Object object, String propertyName) {		
					if(object == viewerInput && fPropertyNames[fPropertyNames.length-1].equals(propertyName)){
						viewer.refresh();
					}
				}
			};
		}
		
		ObjectDelegate.addListener(newInput.getClass(),changeListener);		
		
	}

}
