package org.eclipse.ve.sweet2;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.eclipse.ve.sweet.test.Person;

public class Foo implements InvocationHandler{

	public static void main(String[] args) {
		
		Person p = new Person("John Doe",35);
		PersonInterface proxyPerson = null;
		
		try{
			proxyPerson = (PersonInterface) Proxy.newProxyInstance(ObjectBinder.class.getClassLoader(),
					new Class[] { IObjectBinder.class , PersonInterface.class }, 
					new Foo());
		} catch (Exception e){
			e.printStackTrace();
		}		
		
		p.setName("Foo");
		proxyPerson.setName("Foo");
		
	}

	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

		return null;
		
	}
	
}
