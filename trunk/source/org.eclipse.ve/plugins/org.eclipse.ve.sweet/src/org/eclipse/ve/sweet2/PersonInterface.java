package org.eclipse.ve.sweet2;

public interface PersonInterface {
	
	String getFirstName();
	void setFirstName(String aFirstName);
	String getLastName();
	void setLastName(String aLastName);
	int getAge();
	void setAge(int anAge);
	void setManager(Person aManager);
	Person getManager();

}
