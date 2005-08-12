package org.eclipse.ve.sweet2;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.*;

public class ObjectBinder implements IObjectBinder , InvocationHandler {

	private Object source;
	private Class receiverClass;
	private List binders = new ArrayList();
	private List listeners = new ArrayList();
	private boolean isSignallingChange;

	public static IObjectBinder createObjectBinder(Object source) {
		try{
			Object result = Proxy.newProxyInstance(ObjectBinder.class.getClassLoader(),
					new Class[] { IObjectBinder.class }, 
					new ObjectBinder(source));
          return (IObjectBinder) result;
		} catch (Exception e){
			e.printStackTrace();
			return null;
		}
	}

	public static IObjectBinder createObjectBinder(Class sourceClass) {
		try{
			Object result = Proxy.newProxyInstance(ObjectBinder.class.getClassLoader(),
					new Class[] { IObjectBinder.class }, 
					new ObjectBinder((Class)sourceClass));
          return (IObjectBinder) result;
		} catch (Exception e){
			e.printStackTrace();
			return null;
		}
	}	
	
	private ObjectBinder(Object aSource){
		source = aSource;
		receiverClass = aSource.getClass();
	}
	
	private ObjectBinder(Class aSourceClass){
		receiverClass = aSourceClass;
	}

	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
	
		// Invoke the method on the object we are proxying
		try{
			Method sourceMethod = receiverClass.getMethod(method.getName(),method.getParameterTypes());
			return sourceMethod.invoke(source,args);
		} catch (Exception exc){
			try{
				Method myMethod = getClass().getMethod(method.getName(),method.getParameterTypes());
				return myMethod.invoke(this,args);
			} catch (Exception exc_1){
				return null;				
			}	
		}
	}

	public IValueProvider getPropertyProvider(String string) {
		SimplePropertyProvider result = new SimplePropertyProvider(this,string);
		result.setObjectBinder(this);
		binders.add(result);
		return result;
	}
	
	public void setValue(Object aSource) {
		Object oldSource = source;
		source = aSource;
		if(isSignallingChange) return;		
		isSignallingChange = true;		
		Iterator iter = listeners.iterator();		
		while(iter.hasNext()){
			PropertyChangeListener listener = (PropertyChangeListener)iter.next();
			listener.propertyChange(new PropertyChangeEvent(this,null,oldSource,aSource));
		}		
		isSignallingChange = false;		
	}

	public Object getValue() {
		return source;
	}

	public Class getType() {
		return receiverClass == null ? source.getClass() : receiverClass;
	}

	public void addPropetyChangeListener(PropertyChangeListener listener) {
		listeners.add(listener);
	}

	public void addPropertyChangeListener(PropertyChangeListener l) {
		// TODO Auto-generated method stub
		
	}

	public void refresh(String propertyName) {
		// Refresh all binders
		if(isSignallingChange) return;
		isSignallingChange = true;
		Iterator iter = listeners.iterator();		
		while(iter.hasNext()){
			PropertyChangeListener listener = (PropertyChangeListener)iter.next();
			listener.propertyChange(new PropertyChangeEvent(this,propertyName,null,null));
		}			
		isSignallingChange = false;		
	}
}
