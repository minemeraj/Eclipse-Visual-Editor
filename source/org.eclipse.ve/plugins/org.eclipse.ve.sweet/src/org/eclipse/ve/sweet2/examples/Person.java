package org.eclipse.ve.sweet2.examples;

import java.util.*;

public class Person implements PersonInterface {

	private String firstName;
	private String lastName;
	private int age;
	private Person manager;
	private List backups = new ArrayList();
	private static List sampleData = new ArrayList();
	public static Person CHRIS_CHRINGLE;
	public static Person TOOTH_FAIRY;
	public static Person JILL;
	public static Person JOHN;
	public static Person BIG_CHEESE;
	static{
		JOHN = new Person("John","Doe",37);
		JILL = new Person("Jill","Smith",25);
		CHRIS_CHRINGLE = new Person("Chris","Chringle",75);
		TOOTH_FAIRY = new Person("Tooth","Fairy",30);	
		TOOTH_FAIRY.setManager(CHRIS_CHRINGLE);
		BIG_CHEESE = new Person("Big","Cheese",52);
		JOHN.setManager(TOOTH_FAIRY);
		JILL.setManager(BIG_CHEESE);
		BIG_CHEESE.addBackup(CHRIS_CHRINGLE);
		BIG_CHEESE.addBackup(TOOTH_FAIRY);
		
	}
	
	public static List getSampleData(){
		return sampleData;
	}

	public void addBackup(Person backup) {
		backups.add(backup);
	}
	public List getBackups(){
		return backups;
	}
	public void removeBackup(Person backup){
		backups.remove(backup);
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
