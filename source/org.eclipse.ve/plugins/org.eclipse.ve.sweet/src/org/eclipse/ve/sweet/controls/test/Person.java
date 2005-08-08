package org.eclipse.ve.sweet.controls.test;

public class Person {
	
	public String name = "";
	public String address = "";
	public String city = "Wheaton";
	public String state = "IL";
	
	public Person(String name, String address, String city, String state) {
		this.address = address;
		this.city = city;
		this.name = name;
		this.state = state;
	}
	
	public Person() {}
}
