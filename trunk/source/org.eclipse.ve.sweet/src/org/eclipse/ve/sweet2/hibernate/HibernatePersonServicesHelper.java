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
 *  Created Aug 10, 2005 by Gili Mendel
 * 
 *  $RCSfile: HibernatePersonServicesHelper.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-23 15:59:01 $ 
 */
package org.eclipse.ve.sweet2.hibernate;

import java.util.*;

import org.hibernate.*;
import org.hibernate.cfg.Configuration;

 
/**
 * This is a hibernate helper to the Person Database
 */
public class HibernatePersonServicesHelper {

	 private Session session = null;
	 private Transaction transaction = null;
	 private static String HIBERNATE_CFG = "/org/eclipse/ve/sweet2/hibernate/hibernate.cfg.xml";
	 private static String LOG4J_CFG = "org/eclipse/ve/sweet2/hibernate/log4j.properties";
	 
	 private static HibernatePersonServicesHelper helper = null;
	 
	 private boolean fakeData = System.getProperty("fake") != null;
	 
	 
	
	public HibernatePersonServicesHelper() {
		super();
		// Hibernate uses log4j that needs a configuration file
		System.setProperty("log4j.configuration",LOG4J_CFG);
	}
	
	public static HibernatePersonServicesHelper getHelper() {
		if (helper==null)
			helper = new HibernatePersonServicesHelper();
		return helper;
	}
	
	 
	 /**
	  * @return Hibernate Session using the default configuration file hibernate.cfg.xml
	  */
	 public Session getSession() {		 
		 if (session==null) {
		        SessionFactory sessionFactory = new Configuration().configure(HIBERNATE_CFG).buildSessionFactory();
		        session =sessionFactory.openSession();
		 }
		 return session;
	 }
	 
	 public void endSession() {
		 if (session!=null) {
			 endTransaction();
			 session.flush();
		     session.close();
		     session = null;
		 }
	 }
	 
	 private String _transactionMsg = null;
	 /**
	  * Start an Hibernate transaction
	  * @param msg debug message to be use in begin/end/rollback calls
	  */
	 public void beginTransaction(String msg) {
		 if (fakeData) return;
		 
		 _transactionMsg = msg;
		 System.out.println("\nStarting Transaction: "+msg);
		 if (transaction!=null)
			 throw new IllegalStateException();
		 transaction = getSession().beginTransaction();
	 }
	 
	 public void endTransaction() {
		 if (fakeData) return;
		 
		 if (transaction!=null) {
			 System.out.println("Transaction Ended: "+_transactionMsg);
			 transaction.commit();
			 transaction=null;
			 _transactionMsg=null;
		 }
	 }
	 
	 protected void rollBackTransaction() {
		 if (fakeData) return;
		 
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
	 public List getAllPersons() {
		 if (fakeData) 
			 return createSampleEntries();
		 else 
			 return getSession().createQuery("from Person").list();
	 }
	 
	 /**
	  * A call to this method will delete all users/relationships from the Data base.
	  */
	 public void deleteAllPersonEntries() {
		 try {
			 beginTransaction("Delete all Entries");
			 Session sess = getSession();			 
			 List list = sess.createQuery("from Person").list();
			 // Need first to remove the one-to-one relasionship, as it is cyclic
			 for (int i = 0; i < list.size(); i++) {				
				Person p = (Person)list.get(i);
				p.setSpouse(null);
			 }
			 sess.flush();
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
	 
	 public void deleteAPerson(Person p) {
		 if (fakeData) return;
		 try {
			 beginTransaction("Delete "+p);
			 p.setSpouse(null);			 
			 getSession().flush();
			 getSession().delete(p);
			 System.out.println("\tDeleting: "+p);
		 }
		 catch (RuntimeException e) {
			 rollBackTransaction();	
			 System.err.println("Failed deleting "+p);
			 throw (e);
		 }
		 finally {		 
		   endTransaction();
		 }		 
	 }
	 
	 public void saveAPerson(Person p) {
		 if (fakeData) return;
		 
		 try {
			 beginTransaction("Save "+p);
			 getSession().save(p);
			 getSession().flush();
			 System.out.println("\tSaving: "+p);
		 }
		 catch (RuntimeException e) {
			 rollBackTransaction();	
			 System.err.println("Failed saving "+p);
			 throw (e);
		 }
		 finally {		 
		   endTransaction();
		 }		 
	 }
	 
	 public void savePersonList(List persons) {
		 if (fakeData) return;
		 
		 try {
			 beginTransaction("Save List ");
			 for (Iterator itr=persons.iterator(); itr.hasNext();) {				 				 
				 Person p = (Person)itr.next();
				 getSession().save(p);
				 System.out.println("\tSaving: "+p);
			 }
			 getSession().flush();
		 }
		 catch (RuntimeException e) {
			 rollBackTransaction();	
			 System.err.println("Failed List Save");
			 throw (e);
		 }
		 finally {		 
		   endTransaction();
		 }		 
	 }
	 
	 public void addAPerson (Person p) {
		 if (fakeData) return;
		 try {
			 beginTransaction("Add "+p);
			 getSession().save(p);
			 System.out.println("\tAdding: "+p);
		 }
		 catch (RuntimeException e) {
			 rollBackTransaction();	
			 System.err.println("Failed adding "+p);
			 throw (e);
		 }
		 finally {		 
		   endTransaction();
		 }		 
		 getSession().save(p);
	 }
	 
	 /**
	  * Create sample set of persons/relationships in the DB
	  */
	 public List createSampleEntries() {
		 
		 // Delete old entries, if there
		 if (!fakeData) 
		     deleteAllPersonEntries();
		 
		 Person[] persons = new Person[0];
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
			 Person dave = new Person("Dave","Orme");
			 Person john = new Person("John","Doe");
			 Person jill = new Person ("Jill", "Smith");
			 Person tooth = new Person ("Tooth", "Fairy");
			 Person chris = new Person ("Chris","Cringle");
			 
			 persons = new Person[] { gili, michelle, joe, sri, anu, peter, beth, rich, jon, dave, john, jill, tooth, chris }; 


			 if (!fakeData) {
				getSession().save(chris);
				getSession().save(tooth);
				getSession().save(dave);
				getSession().save(gili);
				getSession().save(michelle);
				getSession().save(joe);
				getSession().save(sri);
				getSession().save(anu);
				getSession().save(peter);
				getSession().save(beth);
				getSession().save(rich);
				getSession().save(jon);
				getSession().save(john);
				getSession().save(jill);
			}
			 
			 
			 // many-to-one relationship
			 joe.setManager(gili);
			 sri.setManager(gili);
			 jon.setManager(gili);
			 rich.setManager(gili);
			 peter.setManager(gili);
			 gili.setManager(michelle);
			 dave.setManager(john);
			 tooth.setManager(chris);
			 			 
			 // many-to-many relathionship
			 gili.addBackup(peter);
			 gili.addBackup(sri);
			 gili.addBackup(rich);
			 
			 tooth.addBackup(chris);
			 tooth.addBackup(jill);
			 			 
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
			 
			 dave.addBackup(gili);
			 
			 john.setSpouse(jill);
								 
			 // one-to-one relationship	- bi-direction is enforced by the APIs	 
			 gili.setSpouse(michelle);
			 sri.setSpouse(anu);
			 peter.setSpouse(beth);
		
			 if (!fakeData) {
				 // Save changes to DB
				 getSession().flush();
			 }
		 }
		 catch (Exception e) {
			 rollBackTransaction();			 
		 }
		 finally {
			 endTransaction();			 
		 }
		 
		 return Arrays.asList(persons);
		 
		 
	 }

}
