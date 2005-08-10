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
 *  $RCSfile: HibernateHelper.java,v $
 *  $Revision: 1.1 $  $Date: 2005-08-10 18:39:22 $ 
 */
package org.eclipse.ve.sweet2.hibernate;

import java.util.List;

import org.hibernate.*;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
 
/**
 * This is a hibernate helper to the Person Database
 */
public class HibernateHelper {

	 private Session session = null;
	 private Transaction transaction = null;
	 private static String HIBERNATE_CFG = "/org/eclipse/ve/sweet2/hibernate/hibernate.cfg.xml";
	 private static String LOG4J_CFG = "org/eclipse/ve/sweet2/hibernate/log4j.properties";
	 
	 private static HibernateHelper helper = null;
	 
	 
	
	public HibernateHelper() {
		super();
		// Hibernate uses log4j that needs a configuration file
		System.setProperty("log4j.configuration",LOG4J_CFG);
	}
	
	public static HibernateHelper getHelper() {
		if (helper==null)
			helper = new HibernateHelper();
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
		 _transactionMsg = msg;
		 System.out.println("\nStarting Transaction: "+msg);
		 if (transaction!=null)
			 throw new IllegalStateException();
		 transaction = getSession().beginTransaction();
	 }
	 
	 public void endTransaction() {
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
	 public List primGetPersonList() {
		 return getSession().createQuery("from Person").list();
	 }
	 
	 /**
	  * A call to this method will delete all users/relationships from the Data base.
	  */
	 public void deleteCurrentEntries() {
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
	 public void createSampleEntries() {
		 
		 // Delete old entries, if there
		 deleteCurrentEntries();
		 
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

}
