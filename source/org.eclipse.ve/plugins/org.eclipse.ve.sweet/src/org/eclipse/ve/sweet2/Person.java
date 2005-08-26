package org.eclipse.ve.sweet2;

import java.util.*;

public class Person implements PersonInterface {

	private String firstName;
	private String lastName;
	private int age;
	private Person manager;
	private static List sampleData;
	
	public static List getSampleData(){
		if(sampleData == null){
			sampleData = new ArrayList();
			Person john = new Person("Doe","John",37);
			Person jill = new Person("Smoth","Jill",25);
			Person chris = new Person("Cringle","Chris",75);
			Person manager = new Person("Cheese","Big",52);
			john.setManager(manager);
			jill.setManager(manager);
			sampleData.add(john);
			sampleData.add(jill);
			sampleData.add(chris);
			sampleData.add(manager);
		}
		return sampleData;
	}

	public Person(String aFirstName, String aLastName, int anAge) {
		firstName = aFirstName;
		lastName = aLastName;
		age = anAge;
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
