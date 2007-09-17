/*******************************************************************************
 * Copyright (c) 2003, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: AbstractEventSrcGenerator.java,v $
 *  $Revision: 1.8 $  $Date: 2007-09-17 14:21:53 $ 
 */
package org.eclipse.ve.internal.java.codegen.util;

import java.util.*;

import org.eclipse.jem.java.*;

import org.eclipse.ve.internal.jcm.*;

/**
 * @author Gili Mendel
 *
 */
public abstract class AbstractEventSrcGenerator implements IEventSrcGenerator {
	
   public static final String  JAVAJET_EXT = ".javajet" ; //$NON-NLS-1$
	
   AbstractEventInvocation  fEE ;
   Listener		    fListener ;
   String[]			fMethodComments ;
   String			fSeperator = System.getProperty("line.separator") ; //$NON-NLS-1$
   String			fIndent = "\t\t" ; //$NON-NLS-1$
   String			fReceiver ;
   String           fEventArgName = "e" ; //$NON-NLS-1$
   EventInfo		fInfo = null ;


   public class EventInfo {   	  
   	  public String		   fSeperator ;
   	  public String		   fIndent ;
   	  public String		   fReceiver ;   // Instance we are adding the event to
   	  public String		   fSelector ;   // Add method
	  public int			   fselectorArgCount ; 
   	  public String		   fAllocatedClass ; // The Interface/class we are overiding   	  
   	  public String		   fEventType ;      // Argument to methods   	  
   	  public String[]		   fMethods ;        // Methods to implements
   	  public String[]		   fMStubs ;         // Methods stubs (required by interfaces)   	  
   	  public String[]         fPropertyNames;    // For bound properties
   	  public boolean[]       fPropertyIfFlag;   // generate an if statement for a property
   	  public String		   fEventArgName ;
   	  
   	  
   	  public EventInfo () {
   	  	this.fSeperator = AbstractEventSrcGenerator.this.fSeperator ;
   	  	this.fIndent = AbstractEventSrcGenerator.this.fIndent ;
   	  	this.fReceiver = AbstractEventSrcGenerator.this.fReceiver ;
   	  	this.fEventArgName = AbstractEventSrcGenerator.this.fEventArgName ;
   	  	
		int  noMethods = fEE.getCallbacks().size() ;
		List  methodsToImplements = null ;
   	  	
   	  	if (fListener.getListenerType().getExtends() != null) {    	  	
   	  		fAllocatedClass = fListener.getListenerType().getExtends().getJavaName() ;
   	  	}
   	  	else {
   	  		JavaClass imp = fListener.getListenerType().getImplements().get(0) ;
   	  	    fAllocatedClass = imp.getJavaName() ;   	  	    
   	  	    // This is the list of method this interface requires, make sure we generate them
			methodsToImplements = imp.getMethods() ;
   	  	}   	  	  
   	  	   	  	
   	  	fSelector = getAddListenerMethod() ;
   	  	if (fEE instanceof PropertyChangeEventInvocation) {
   	  	   PropertyChangeEventInvocation pee = (PropertyChangeEventInvocation) fEE ;
   	  	   fPropertyNames = new String[pee.getProperties().size()] ;
   	  	   fPropertyIfFlag = new boolean[pee.getProperties().size()] ;
   	  	   
   	  	   // generate If statements for a single parameter method invocation
   	  	   boolean useIfExpr;
		   if (fEE instanceof PropertyChangeEventInvocation)
			   useIfExpr = ((PropertyChangeEventInvocation)fEE).getAddMethod().getParameters().size()==1 ;
		   else
			   useIfExpr = false ;
   	  	   for (int i = 0; i < pee.getProperties().size(); i++) {
			  fPropertyNames[i] = pee.getProperties().get(i).getPropertyName() ;
			  
			  fPropertyIfFlag[i] = useIfExpr;
   	  	   }
   	  	   fselectorArgCount = pee.getAddMethod().getParameters().size() ;		
   	  	}   	  	   
   	  	else {   	  	
   	  	   fPropertyNames = null ;
   	  	   fPropertyIfFlag = null ;
   	  	   fselectorArgCount = 1 ;
   	  	}
   	  	
   	  	
   	  	fMethods = new String[noMethods] ;
   	  	fEventType = null ;
   	  	// Determine the methods that should be generated
        for (int i = 0; i < fEE.getCallbacks().size(); i++) {
        	// This is the method that is requested
			Method m = fEE.getCallbacks().get(i).getMethod();
			fMethods[i] = m.getName() ;
			if (fEventType == null)
			   if(m.getParameters().size()==1)
			      fEventType = ((JavaParameter)m.getParameters().get(0)).getJavaType().getJavaName();
			   else if (m.getParameters().size()==2)
			      fEventType = ((JavaParameter)m.getParameters().get(1)).getJavaType().getJavaName();
			   
			  
		}
		
		// Determine methods that were not requested, but that are required by an interface
		List stubs = new ArrayList() ;
		if (methodsToImplements != null) {
		for (Iterator iter = methodsToImplements.iterator(); iter.hasNext();) {
			String name = ((Method)iter.next()).getName();
			boolean generateStub = true ;
			for (int i = 0; i < fMethods.length; i++) {
				if (fMethods[i].equals(name)) {
					generateStub = false ;
					break ;
				}
			}
			if (generateStub)
			   stubs.add(name) ;
		}
		}
		fMStubs = (String[]) stubs.toArray(new String[stubs.size()]) ;			
   	  } 
   	     	  
   	  
   	  
   	  public EventInfo (Callback[] callbacks) {
		this.fSeperator = AbstractEventSrcGenerator.this.fSeperator;
		this.fIndent = AbstractEventSrcGenerator.this.fIndent;
		this.fReceiver = null;
		this.fSelector = null;
		this.fAllocatedClass = null;
		this.fEventType = null;
		this.fMStubs = null;
		this.fMethods = new String[callbacks.length];
		this.fPropertyIfFlag=null ;
		this.fPropertyNames=null ;
		this.fEventArgName = AbstractEventSrcGenerator.this.fEventArgName ;

		for (int i = 0; i < callbacks.length; i++) {
			// This is the method that is requested
			Method m =  callbacks[i].getMethod();
			fMethods[i] = m.getName();
			if (fEventType == null && m.getParameters().size() == 1)
				fEventType = ((JavaParameter) m.getParameters().get(0)).getJavaType().getJavaName();
		}
   	  }
	  public EventInfo (PropertyEvent[] props) {
			this.fSeperator = AbstractEventSrcGenerator.this.fSeperator;
			this.fIndent = AbstractEventSrcGenerator.this.fIndent;
			this.fReceiver = null;
			this.fSelector = null;
			this.fAllocatedClass = null;
			this.fEventType = null;
			this.fMStubs = null;
			this.fMethods = null ;
			this.fPropertyNames = new String[props.length] ;
			this.fPropertyIfFlag = new boolean[props.length] ;
		    this.fEventArgName = AbstractEventSrcGenerator.this.fEventArgName ;

            // Generate If statements for a signle argument Property Change method invocation
            boolean useIfExpr;
            if (fEE instanceof PropertyChangeEventInvocation)
		        useIfExpr = ((PropertyChangeEventInvocation)fEE).getAddMethod().getParameters().size()==1 ;
		    else
		        useIfExpr = false ;
			for (int i = 0; i < props.length; i++) {
				fPropertyNames[i] = props[i].getPropertyName() ;
				fPropertyIfFlag[i] = useIfExpr ;
			}
		  }
   }

 public AbstractEventSrcGenerator (AbstractEventInvocation ee, Listener l, String rec) {
   	fEE = ee ;
   	fListener = l ;
   	fReceiver = rec ;   
 }  
 
 
 protected abstract String getAddListenerMethod() ;
 
/**
 * Returns the seperator.
 * @return String
 */
 public String getSeperator() {
	return fSeperator;
 }

/**
 * Sets the seperator.
 * @param seperator The seperator to set
 */
 public void setSeperator(String seperator) {
	fSeperator = seperator;
	if(fInfo!=null)
		fInfo.fSeperator = seperator;
}
 
 public abstract String generateEvent() ;


/**
 * Returns the indent.
 * @return String
 */
public String getIndent() {
	return fIndent;
}

/**
 * Sets the indent.
 * @param indent The indent to set
 */
public void setIndent(String indent) {
	fIndent = indent;
	if(fInfo!=null)
		fInfo.fIndent = indent;
}

public EventInfo getInfo() {
	if (fInfo != null) return fInfo ;
	fInfo = new EventInfo() ;
	return fInfo ;
}

/**
 * @param string
 */
public void setEventArgName(String string) {
	fEventArgName = string;
	if(fInfo!=null)
	  fInfo.fEventArgName = string ;
	
}

}
