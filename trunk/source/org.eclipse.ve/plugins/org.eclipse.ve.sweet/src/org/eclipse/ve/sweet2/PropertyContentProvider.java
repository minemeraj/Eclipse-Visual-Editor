package org.eclipse.ve.sweet2;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.Method;

import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.ve.sweet2.ObjectBinder.ChangeListener;

public class PropertyContentProvider implements IElementContentProvider {
	
	private String fPropertyName;
	private Object viewerInput;
	private Method fGetMethod;
	private ChangeListener changeListener;

	public PropertyContentProvider(String aPropertyName){
		fPropertyName = aPropertyName;
	}

	public void dispose() {
		
	}

	public void inputChanged(final Viewer viewer, Object oldInput, Object newInput) {
		// Listen to the input and refresh the viewer whenever the registered property changes
		if(newInput == null) return;
		if(newInput instanceof IObjectBinder){
			IObjectBinder binder = (IObjectBinder)newInput;
			viewerInput = ((IObjectBinder)newInput).getValue();
			// If the object changes we must refresh the viewer
			binder.addPropertyChangeListener(new PropertyChangeListener(){
				public void propertyChange(PropertyChangeEvent event) {
					if(fPropertyName.equals(event.getPropertyName()) || event.getPropertyName() == null){
						viewer.refresh();
					}
				}
			});
		}
		if(changeListener != null && oldInput != null){
			ObjectBinder.removeListener(oldInput.getClass(),changeListener);
		}
		
		if(changeListener == null){
			changeListener = new ChangeListener(){
				public void objectAdded(Object newObject) {				
				}
				public void objectRemove(Object oldObject) {				
				}
				public void objectChanged(Object object, String propertyName) {		
					if(object == viewerInput && fPropertyName.equals(propertyName)){
						viewer.refresh();
					}
				}
			};
		}
		
		ObjectBinder.addListener(newInput.getClass(),changeListener);		
		
	}

	public Object getElement(Object input) {
		if(input instanceof IObjectBinder){
			input = ((IObjectBinder)input).getValue();
		}		
		if(input == null) return null;
		try{
			if(fGetMethod == null){
				fGetMethod = input.getClass().getMethod(EditorUtilities.getMethodName(fPropertyName),null);
			}
			// TODO Deal with the fact that the method might be from a previous input of different type
			return fGetMethod.invoke(input,null);
		} catch (Exception e){
			e.printStackTrace();
			return null;
		}
	}
}
