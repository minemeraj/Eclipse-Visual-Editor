package org.eclipse.ve.sweet2.hibernate;
import java.util.Iterator;
import java.util.List;


/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  Created Aug 2, 2005 by Gili Mendel
 * 
 *  $RCSfile: Test.java,v $
 *  $Revision: 1.6 $  $Date: 2005-08-19 22:24:15 $ 
 */
/**
 * This class is a test that will delete all entries from the DataBase, Re-Create the
 * entries, and list the content of the DB.
 */
public class Test {
	


	 public static void main(String[] args) {
		    
		   HibernatePersonServicesHelper helper = HibernatePersonServicesHelper.getHelper();
		    try{
		    	// This will delete/reCreate the person entries in the DB
		    	// comment this line if you do not want to reCreate.
		    	helper.createSampleEntries();
		    	System.out.println("\nRead all DataBase Entries:");
		    	List pl = helper.getAllPersons();
		    	for (Iterator iter = pl.iterator(); iter.hasNext();) {
					Person p = (Person) iter.next();
					System.out.println("\t"+p);
					System.out.println("\t\tManager: "+p.getManager());
					Person s = p.getSpouse();
					System.out.println("\t\tSpouse: " + s);
					System.out.println("\t\tBackups:");
					if (p.getBackups()!=null)
					   for (Iterator i = p.getBackups().iterator(); i.hasNext();) {
						  System.out.println("\t\t\t"+i.next());
					   }
					else
						System.out.println("\t\t\tNone");
				}
		    }
		    finally{
		     helper.endSession();
		     System.exit(0);
		    }
	 }

}
