package org.eclipse.ve.sweet2;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.*;
import java.util.*;

public class ObjectDelegate implements IObjectDelegate , InvocationHandler {

	private Object source;
	private Class receiverClass;
	private List binders = new ArrayList();
	private List listeners = new ArrayList();
	private static Map typeListeners;
	private boolean isSignallingChange;
	
	void setSource(Object aSource){
		source = aSource;
	}

	public static IObjectDelegate createObjectBinder(Class sourceClass) {
		try{
			Object result = Proxy.newProxyInstance(ObjectDelegate.class.getClassLoader(),
					new Class[] { IObjectDelegate.class }, 
					new ObjectDelegate(sourceClass));
          return (IObjectDelegate) result;
		} catch (Exception e){
			e.printStackTrace();
			return null;
		}
	}	
	
	
	public interface ChangeListener{
		void objectAdded(Object newObject);
		void objectRemove(Object oldObject);
		void objectChanged(Object object, String propertyName);
	}
	
	public static void addListener(Class aType, ChangeListener aListener){
		if(typeListeners == null){
			// TODO: <gm> I expect that this does not do what you intended to do,
			//            a weak link is on the key, so only when aType is disposed 
			//            will the array be removed from the list.
			//            We need a WeakList like so that when the listener is not
			//            there anymore, we can remove it from the list.
			//       </gm>
			typeListeners = new WeakHashMap();
		}
		List listeners = (List)typeListeners.get(aType);
		if(listeners == null){
			listeners = new ArrayList();
			typeListeners.put(aType,listeners);			
		}
		listeners.add(aListener);
	}
	
	private ObjectDelegate(Class aSourceClass){
		receiverClass = aSourceClass;
		ObjectDelegate.addListener(receiverClass,new ChangeListener(){
			public void objectAdded(Object newObject) {
				
			}
			public void objectRemove(Object oldObject) {
				
			}
			public void objectChanged(Object object, String propertyName) {
				if(object == source){
					Iterator iter = listeners.iterator();		
					while(iter.hasNext()){
						PropertyChangeListener listener = (PropertyChangeListener)iter.next();
						listener.propertyChange(new PropertyChangeEvent(this,propertyName,null,source));
					}		
				}
			}
		});		
	}

	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
	
		// Invoke the method on the object we are proxying
		try{
			if (method.getName().equals("equals") && args.length==1)
				if (args[0]==proxy)
					return Boolean.TRUE;
				else
					return Boolean.FALSE;
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
	
	private void fireValueChanged (Object oldVal, Object newVal) {
		if (!isSignallingChange && listeners.size()>0) {
			isSignallingChange = true;		
			try {
				PropertyChangeEvent event = new PropertyChangeEvent(this, null, oldVal, newVal);
				for (Iterator iter = listeners.iterator(); iter.hasNext();)
					((PropertyChangeListener) iter.next()).propertyChange(event);
			} finally {
				isSignallingChange = false;
			}
		}			
	}
	
	public void setValue(Object aSource) {
		if (aSource!=null && !receiverClass.isAssignableFrom(aSource.getClass()))
			throw new Error("Invalide value:"+aSource);
		Object oldSource = source;
		source = aSource;
		fireValueChanged(oldSource, aSource);
	}

	public Object getValue() {
		return source;
	}

	public Class getType() {
		return receiverClass == null ? source.getClass() : receiverClass;
	}

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		listeners.add(listener);
	}

	public void refresh(String propertyName) {
		// Refresh all binders
		if(isSignallingChange) return;		
		// Signal change
		if(typeListeners == null) return;		
		List listeners = (List)typeListeners.get(getType());
		Iterator iter = listeners.iterator();
		while(iter.hasNext()){
			((ChangeListener)iter.next()).objectChanged(source,propertyName);
		}
		isSignallingChange = false;		
	}
	
	public static void refresh(Object anInstance, String propertyName){
		List listeners = (List)typeListeners.get(anInstance.getClass());
		Iterator iter = listeners.iterator();
		while(iter.hasNext()){
			((ChangeListener)iter.next()).objectChanged(anInstance,propertyName);
		}		
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		// TODO Auto-generated method stub
		
	}

	public void refresh() {
		refresh(null);
	}
	
	public String toString() {
		return "Class:"+receiverClass.getName()+"\n"+source.toString();
	}

	public static void removeListener(Class aType, ChangeListener changeListener) {
		List listeners = (List)typeListeners.get(aType);
		if(listeners instanceof List){
			((List)listeners).remove(changeListener);			
		}		
	}

	public void setObjectBinder(IObjectDelegate anObjectBinder) {
		
	}
}
