package org.eclipse.ve.sweet2;

public class Person implements PersonInterface {

	private String firstName;
	private String lastName;
	private int age;
	private Person manager;

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
