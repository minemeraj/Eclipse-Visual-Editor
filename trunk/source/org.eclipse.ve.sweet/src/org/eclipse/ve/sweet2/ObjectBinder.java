package org.eclipse.ve.sweet2;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class ObjectBinder implements IObjectBinder , InvocationHandler {

	private Object source;
	private Class receiverClass;

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
	
	private ObjectBinder(Object aSource){
		source = aSource;
		receiverClass = aSource.getClass();
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

	public IPropertyProvider getPropertyProvider(String string) {
		return new SimplePropertyProvider(source,string);
	}
	

}
