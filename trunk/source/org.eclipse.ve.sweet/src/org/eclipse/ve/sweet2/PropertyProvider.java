package org.eclipse.ve.sweet2;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.ve.sweet.objectviewer.IPropertyEditor;

public class PropertyProvider implements InvocationHandler {
	
	private Object source;
	private String propertyName;
	private Class receiverClass;

	public static IValueProvider getPropertyProvider(Object source, String propertyName){
		 try {
	            Object result = Proxy.newProxyInstance(PropertyProvider.class.getClassLoader(),
	                    new Class[] { IValueProvider.class }, new PropertyProvider(
	                            source, propertyName));
	            return (IValueProvider) result;
	        } catch (Exception e) {
	        	e.printStackTrace();
	        }		
	       return null;
	}
	
	private PropertyProvider(Object aSource, String aPropertyName){
		source = aSource;
		propertyName = aPropertyName;
        receiverClass = aSource.getClass();		
	}

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    	
    	try{ 
    		Method myMethod = this.getClass().getMethod(method.getName(),method.getParameterTypes());
    		return myMethod.invoke(this,args);
    	} catch (NoSuchMethodException exc){
        	Method realMethod;
    	    try {
    	    	realMethod = receiverClass.getMethod(method.getName(), method.getParameterTypes() );
    	    } catch (Exception e) {
    	    	return null;
    	    }
    	    return realMethod.invoke(source, args);				 
    	}
    	
	}

	public Object getValue() {
		try{
			StringBuffer getMethodName = new StringBuffer();
			getMethodName.append("get");
			getMethodName.append(propertyName.substring(0,1).toUpperCase());
			getMethodName.append(propertyName.substring(1));
			Method getMethod = receiverClass.getMethod(getMethodName.toString(),null);
			return getMethod.invoke(source,null);
		} catch (Exception e){
			return null;
		}
	}

	public void setValue(Object value) {
	}

	public void dispose() {		
	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		TextEditor textViewer = (TextEditor)viewer;
		textViewer.getText().setText(getStringValue());
	}

	private String getStringValue() {
		return getValue().toString();
	}
	
	public Object getSource(){
		return source;
	}

}
