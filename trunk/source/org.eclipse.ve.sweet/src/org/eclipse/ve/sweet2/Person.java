package org.eclipse.ve.sweet2;

import java.util.*;

public class Person implements PersonInterface {

	private String firstName;
	private String lastName;
	private int age;
	private Person manager;
	private static List sampleData = new ArrayList();
	public static Person CHRIS_CHRINGLE;
	public static Person TOOTH_FAIRY;
	static{
		Person john = new Person("Doe","John",37);
		Person jill = new Person("Smoth","Jill",25);
		CHRIS_CHRINGLE = new Person("Cringle","Chris",75);
		TOOTH_FAIRY = new Person("Tooth","Fairy",30);	
		TOOTH_FAIRY.setManager(CHRIS_CHRINGLE);
		Person manager = new Person("Cheese","Big",52);
		john.setManager(manager);
		jill.setManager(manager);		
	}
	
	public static List getSampleData(){
		return sampleData;
	}

	public Person(String aFirstName, String aLastName, int anAge) {
		firstName = aFirstName;
		lastName = aLastName;
		age = anAge;
		sampleData.add(this);
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String aFirstName) {
		firstName = aFirstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String aLastName) {
		lastName = aLastName;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int anAge) {
		age = anAge;
	}
	public void setManager(Person aManager) {
		manager = aManager;
	}
	public Person getManager() {
		return manager;
	}

	

}
