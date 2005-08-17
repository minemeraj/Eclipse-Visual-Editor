package org.eclipse.ve.sweet2;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Control;

public abstract class AbstractPropertyProvider implements IValueProvider {

	private List listeners;
	protected Class fPropertyType;
	protected IObjectBinder objectBinder;
	protected boolean isSettingValue;
	Editor editor;
	Control control;

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		if(listeners == null){
			listeners = new ArrayList();
		}
		listeners.add(listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		listeners.remove(listener);		
	}

	protected void signalListeners() {
		if(listeners != null){
			Iterator iter = listeners.iterator();
			while(iter.hasNext()){
				((PropertyChangeListener)iter.next()).propertyChange(new PropertyChangeEvent(this,getPropertyName(),null,null));
			}
		}
	}
	
	protected abstract String getPropertyName();
	
	protected String getMethodName(String propertyName){
		StringBuffer getMethodName = new StringBuffer();
		getMethodName.append("get");
		getMethodName.append(propertyName.substring(0,1).toUpperCase());
		getMethodName.append(propertyName.substring(1));
		return getMethodName.toString();
	}
	
	protected String setMethodName(String propertyName){
		StringBuffer getMethodName = new StringBuffer();
		getMethodName.append("set");
		getMethodName.append(propertyName.substring(0,1).toUpperCase());
		getMethodName.append(propertyName.substring(1));
		return getMethodName.toString();
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

	protected void setSourceBinder(IObjectBinder binder) {
		objectBinder = binder;
		// Once the object binder is set we listen to it and refresh ourself once its value changes
		objectBinder.addPropertyChangeListener(new PropertyChangeListener(){
			public void propertyChange(PropertyChangeEvent evt) {
				if(!isSettingValue){
					signalListeners();
				}
			}			
		});
	}

}
