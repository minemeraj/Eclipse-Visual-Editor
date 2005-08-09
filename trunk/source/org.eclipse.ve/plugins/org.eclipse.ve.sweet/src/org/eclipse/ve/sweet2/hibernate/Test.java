package org.eclipse.ve.sweet2.hibernate;
import java.util.Iterator;
import java.util.List;

import org.hibernate.*;
import org.hibernate.cfg.Configuration;


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
 *  $Revision: 1.1 $  $Date: 2005-08-09 20:23:23 $ 
 */
/**
 * This class is a test that will delete all entries from the DataBase, Re-Create the
 * entries, and list the content of the DB.
 */
public class Test {
	
	 private Session session = null;
	 private Transaction transaction = null;
	 private static String HIBERNATE_CFG = "/org/eclipse/ve/sweet2/hibernate/hibernate.cfg.xml";
	 
	 /**
	  * @return Hibernate Session using the default configuration file hibernate.cfg.xml
	  */
	 protected Session getSession() {
		 if (session==null) {
		        SessionFactory sessionFactory = new Configuration().configure(HIBERNATE_CFG).buildSessionFactory();
		        session =sessionFactory.openSession();
		 }
		 return session;
	 }
	 
	 private String _transactionMsg = null;
	 /**
	  * Start an Hibernate transaction
	  * @param msg debug message to be use in begin/end/rollback calls
	  */
	 protected void beginTransaction(String msg) {
		 _transactionMsg = msg;
		 System.out.println("Starting Transaction: "+msg);
		 if (transaction!=null)
			 throw new IllegalStateException();
		 transaction = getSession().beginTransaction();
	 }
	 
	 protected void endTransaction() {
		 System.out.println("Transaction Ended: "+_transactionMsg);
		 transaction.commit();
		 transaction=null;
		 _transactionMsg=null;
	 }
	 
	 protected void rollBackTransaction() {
		 System.out.println("Rolling Back: "+_transactionMsg);
		 if (transaction!=null) 
			 transaction.rollback();
		 _transactionMsg=null;
	 }

	 /**
	  * Use an Hibernate query to return a list of all Person objects from
	  * the Db.
	  * @return Person Set read from the DB
	  */
	 protected List primGetPersonList() {
		 return getSession().createQuery("from Person").list();
	 }
	 
	 /**
	  * A call to this method will delete all users/relationships from the Data base.
	  */
	 private void cleanOldEntries() {
		 try {
			 beginTransaction("Delete old Entries");
			 Session sess = getSession();			 
			 List list = sess.createQuery("from Person").list();
			 // Need first to remove the one-to-one relasionship, as it is cyclic
			 for (int i = 0; i < list.size(); i++) {				
				Person p = (Person)list.get(i);
				p.setSpouse(null);
			 }
			 session.flush();
			 for (int i = 0; i < list.size(); i++) {
				Person p = (Person)list.get(i);
			    sess.delete(p);
				System.out.println("\tDeleting: "+p);
			}
		 }
		 catch (RuntimeException e) {
			 rollBackTransaction();	
			 System.err.println("Failed cleaning previous DB entries");
			 throw (e);
		 }
		 finally {		 
		   endTransaction();
		 }
	 }
	 
	 /**
	  * Create sample set of persons/relationships in the DB
	  */
	 private void createDBEntries() {
		 
		 // Delete old entries, if there
		 cleanOldEntries();
		 
		 beginTransaction("Create New Entries:");		 
		 try {
			 Person gili = new Person("Gili", "Mendel");
			 Person michelle = new Person("Michelle", "Mendel");
			 Person joe = new Person("Joe", "Winchester");
			 Person sri = new Person ("Sri", "Gunturi");
			 Person anu = new Person ("Anu","Gunturi");
			 Person peter = new Person("Peter","Walker");
			 Person beth = new Person ("Beth","Walker");
			 Person rich = new Person("Rich","Kulp");
			 Person jon = new Person ("Jon","Stinton");
			 			 
			 getSession().save(gili);
			 getSession().save(michelle);
			 getSession().save(joe);
			 getSession().save(sri);
			 getSession().save(anu);
			 getSession().save(peter);
			 getSession().save(beth);
			 getSession().save(rich);
			 getSession().save(jon);
			 
			 // many-to-one relationship
			 joe.setManager(gili);
			 sri.setManager(gili);
			 jon.setManager(gili);
			 rich.setManager(gili);
			 peter.setManager(gili);
			 gili.setManager(michelle);
			 			 
			 // many-to-many relathionship
			 gili.addBackup(peter);
			 gili.addBackup(sri);
			 gili.addBackup(rich);
			 			 
			 rich.addBackup(joe);
			 rich.addBackup(peter);
			 rich.addBackup(gili);
			 
			 sri.addBackup(gili);
			 sri.addBackup(rich);
			 
			 peter.addBackup(gili);
			 peter.addBackup(joe);
			 peter.addBackup(rich);
			 			 
			 joe.addBackup(rich);
			 joe.addBackup(peter);
			 
			 jon.addBackup(peter);
								 
			 // one-to-one relationship	- bi-direction is enforced by the APIs	 
			 gili.setSpouse(michelle);
			 sri.setSpouse(anu);
			 peter.setSpouse(beth);
		
			 // Save changes to DB
			 getSession().flush();
		 }
		 catch (Exception e) {
			 rollBackTransaction();			 
		 }
		 finally {
			 endTransaction();			 
		 }
		 
		 
	 }

	 public static void main(String[] args) {
		    
		    System.setProperty("commons-loggin.properties","/org/eclipse/ve/sweet2/hibernate/log4j.properties");
		    Test t = new Test();
		    try{
		    	// This will delete/reCreate the person entries in the DB
		    	// comment this line if you do not want to reCreate.
		    	t.createDBEntries();
		    	
		    	List pl = t.primGetPersonList();
		    	for (Iterator iter = pl.iterator(); iter.hasNext();) {
					Person p = (Person) iter.next();
					System.out.println("\t"+p);
					System.out.println("\t\tManager: "+p.getManager());
					Person s = p.getSpouse();
					System.out.println("\t\tSpouse:" + s);
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
		     Session session = t.getSession();
		     if (session!=null) {
		        session.flush();
		        session.close();		        
		     }
		     System.exit(0);
		    }
	 }

}
