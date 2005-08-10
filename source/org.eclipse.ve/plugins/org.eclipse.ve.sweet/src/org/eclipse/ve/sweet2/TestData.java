package org.eclipse.ve.sweet2;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.ve.sweet.test.Person;

public class TestData {
	
	public static void createTestData(){
		
		Map people = new HashMap();
		
		people.put("1",new Person("John Doe",35));
		people.put("2",new Person("Jill Smith",25));
		people.put("3",new Person("Chris Cringle",75));		
		people.put("3",new Person("Tooth Fairy",39));		
		
		
		
	}

}
