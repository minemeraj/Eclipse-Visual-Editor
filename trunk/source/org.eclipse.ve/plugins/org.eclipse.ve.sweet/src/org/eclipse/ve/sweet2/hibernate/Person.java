package org.eclipse.ve.sweet2.hibernate;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.CallbackException;
import org.hibernate.Session;
import org.hibernate.classic.*;

import sun.security.validator.ValidatorException;

import EDU.oswego.cs.dl.util.concurrent.NullSync;

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
 *  $RCSfile: Person.java,v $
 *  $Revision: 1.1 $  $Date: 2005-08-09 20:23:23 $ 
 */
/**
 *  The Person class is a simple example of a single table/object with the following
 *  properties:
 *  
 *  firstName, lastName are simple properties/columns
 *  manager, is a many-to-one relationship
 *  db_spouse, is a one-to-one relationship
 *  backups, is a many-to-many relathionship
 *  
 */
public class Person implements Serializable, Validatable, Lifecycle {

	private static final long serialVersionUID = 1L;
	private static Log log = LogFactory.getLog(Person.class);	
	private Session session;

    private String firstName;
    private String lastName;
    private long id;  				// used by Hibernate as a primary key 
    private Person manager;
    private Spouse db_spouse;       // Represents the extra Spouse DB table indirection 
    private Set backups;
  
  /**
   * The Spouse class represents the DB SPOUSE table.  A seperate SPOUSE table is needed
   * because Spouse requirements are as following:
   * 
   * Person/Spouse is a one-to-one, nullable, as well as a unique relationship. i.e.,
   * o Spouse is another person (same table)
   * o A person is not required to have a Spouse, and may have at most one Spouse.
   * o A Spouse is unique
   * o The Spouse relationship is bi-directional
   * 
   * Spouse is inner class, to hide it (as much as possible) from the Person API.
   */
  public static class Spouse implements Serializable {
	 
	private static final long serialVersionUID = 1L;
	private long id;
	
	private Person person;
	private Person spouse;    // the db_spouse of person
	
	public Spouse() {
		super();		
	}	
	public Spouse (Person spouse, Person person) {
		this();
		this.spouse = spouse;		
		this.person = person;
	}

	/**
	 * @return the db_spouse of the person associated with the person of this class.
	 */
	public Person getSpouse() {
		return spouse;
	}

	/**
	 * @param db_spouse associated with the person of this entry. 
	 */
	public void setSpouse(Person spouse) {
		this.spouse = spouse;
	}

	/**
	 * @return Hibernate managed primary key.
	 */
	public long getId() {
		return id;
	}
	
	/*
	 * Hibernate will use the same primary key here, as that of the person's ID.
	 */
	private void setId(long id) {
		this.id = id;
	}

	public String toString() {
		return "Spouse[Person: "+person+" Spouse:"+spouse+"]";
	}

	/**
	 * @return the person associated with this entry
	 */
	public Person getPerson() {
		return person;
	}

	/**
	 * @param person associated with this entry.
	 */
	public void setPerson(Person person) {
		this.person = person;
	}
	  
  }
 
  public Person(String firstName, String lastName) {
	super();
	this.firstName= firstName;
	this.lastName=lastName;
  }
  
  public Person() {
	  super();
  }
  

  /**
   * @return First Name
   */
  public String getFirstName() {
    return firstName;
  }

  /** 
   * @return Last name
   */
  public String getLastName() {
    return lastName;
  }

  /**
   * @param string Sets the First Name
   */
  public void setFirstName(String string) {
    firstName = string;
  }

  /**
   * @param string sets the Last Name
   */
  public void setLastName(String string) {
    lastName = string;
  }

  /**
   * @return the primary key for Person - managed by Hibernate
   */
  public long getId() {
    return id;
  }

  /*
   * Sets the primary key for Person - managed by Hibernate
   */
  private void setId(long l) {
    id = l;
  }

/**
 * @return the many-to-one manager relathionship
 */
public Person getManager() {
	return manager;
}

/**
 * @param manager many-to-one relathionship
 */
public void setManager(Person manager) {
	this.manager = manager;
}

/**
 * @return the one-to-one relathionship
 */
private Spouse getDb_spouse() {
	return this.db_spouse;
}

/**
 * @param spouse entry
 */
private void setDb_spouse(Spouse spouse) {
	this.db_spouse = spouse;
}

/**
 * This API wrapper the seperate db_spouse entry in the DB, and is here
 * only as a user API.  It is not used by hibernate
 * 
 * @param spouse
 */
public void setSpouse(Person spouse) {
	    if (spouse!=null) {
	      if (session!=null&&!session.contains(this)) {
	    	  // Make sure that both person/spouse are in the DB first
	    	  session.save(this);
	    	  session.save(spouse);
	      }
	      setDb_spouse(new Spouse(spouse, this));
	      Spouse ssp = spouse.getDb_spouse();
		  if (ssp==null || ssp.getPerson()!=spouse)
			spouse.setSpouse(this);
		  if (session!=null&&!session.contains(getDb_spouse()))
		     session.save(getDb_spouse());
	    }
	    else {
	    	Spouse sp = getDb_spouse();  // Spouse table entry
	    	Person curSpouse = getSpouse();
	    	setDb_spouse(null);
	    	if (sp!=null) {
	    		// force the bi-direction
	    		curSpouse.setSpouse(null);
	    		if (session!=null&&session.contains(sp)) {	    		
		    		session.delete(sp);		    		
	    		}
	    	}
	    	
	    }
}
public Person getSpouse() {
	if (getDb_spouse()!=null)
		return getDb_spouse().getSpouse();
	else
		return null;
}
	    

public String toString() {
	return firstName+" "+lastName;
}


/**
 * @return many-to-many relationship
 */
public Set getBackups() {
	return backups;
}

/*
 * @param backups many-to-many
 */
private void setBackups(Set backups) {
	this.backups = backups;
}

/**
 * This is an API, and is not used by Hibernate
 */
public void addBackup(Person backup) {
	if (backups==null)
		backups = new HashSet();
    backups.add(backup);
}
/**
 * This is an API, and is not used by Hibernate
 */
public void removeBackup(Person backup) {
	backups.remove(backup);
}

public void validate() throws ValidationFailure {
	log.trace("\tValidate was Called on: "+this);
	Person spouse = getSpouse();
	if(spouse!=null)
		if (spouse.getSpouse()!=this)
			throw new ValidationFailure("Spouse relationshiop is not bi-directional");
}

public boolean onSave(Session s) throws CallbackException {
	log.trace("\tonSaveCalled on: "+this);
	session=s;
	if (db_spouse!=null)		 
		s.save(db_spouse);
	return NO_VETO;
}

public boolean onUpdate(Session s) throws CallbackException {
	log.trace("\tonUpdate on: "+this);
	session=s;
	if (db_spouse!=null)		
		s.save(db_spouse);
	return NO_VETO;
}

public boolean onDelete(Session s) throws CallbackException {
	log.trace("\tonDelete on: "+this);
	session=s;
	Person spouse = getSpouse();
	if (spouse!=null) {
		 setSpouse(null);
	}
	return NO_VETO;
}

public void onLoad(Session s, Serializable id) {
	log.trace("\tonLoad on: "+this);
	session=s;
}

}