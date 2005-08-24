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
 *  $RCSfile: TemplateObjectEmitter.java,v $
 *  $Revision: 1.7 $  $Date: 2005-08-24 23:30:48 $ 
 */
package org.eclipse.ve.internal.java.vce.templates;

import java.io.*;
import java.util.logging.Level;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.codegen.jet.JETCompiler;
import org.eclipse.emf.codegen.jet.JETException;

import org.eclipse.ve.internal.java.core.JavaVEPlugin;


/**
 * @author Gili Mendel
 * 
 * This class will be used to compile/generate an object from a given JavaJet template.
 * It is expected that this class will be cached, and re-used for a given JavaJet template.
 * 
 * This class is not concerend with classes time stamps .. it is just a workerbee. If the template
 * time stamp has changed after the creation of this Emitter, it is the user responsibility
 * to create a new Emitter.  Use the TemplateObjectFactory to get a current emitter
 */
public class TemplateObjectEmitter {
	
	public final static String GENERATE_METHOD="generate" ; //$NON-NLS-1$
	
//	String					fEncodeing = System.getProperty("file.encoding", "Cp1252" );
	String					fEncodeing = System.getProperty("Cp1252" );	 //$NON-NLS-1$
	String					fTemplateURIPath[]=null ;
	String					fTemplate=null ;
	long					fTemplateTimeStamp = -1 ;
	String  				fClassName=null ;
	String					fPackageName=null ;
	IPath					fDestinationPath ;
	JETCompiler				fJetCompiler = null ;
	boolean				parsed=false ;
	StringBuffer			fGeneratedSource = null ;
	JavaObjectEmiter        fObjectEmitter = null ;
	
			
	/**
	 * @param templateURIPath  classpath to search the template
	 * @param template         template's file name
	 * @param destinationPath  If not existed already, where to create the template's object 
	 */	
	TemplateObjectEmitter (String[] templateURIPath, String template, IPath destinationPath) {
		fTemplateURIPath = templateURIPath ;
		fTemplate = template ;
		fDestinationPath = destinationPath ;
		File t = new File(JETCompiler.find(templateURIPath,template)) ;
		if (t.canRead())
		  fTemplateTimeStamp = t.lastModified() ;
	}
	/**
	 * @param templateURIPath  classpath to search the template
	 * @param template         template's file name
	 * @param destinationPath  If not existed already, where to create the template's object 
	 * @param className        The class this template is to generate.  If a name is given, 
	 *                          parsing the template is not needed if the class is already generated
	 *                          and compiled.
	 */	
	
	TemplateObjectEmitter (String[] templateURIPath, String template, IPath destinationPath, String className) {
		this (templateURIPath,template, destinationPath) ;
		fClassName = className ;
	}	
	
	   /**
	    * This class is a wrapper to the JETCompiler class.
	    */
	
	
	
	protected void tick(int i, IProgressMonitor pm) {
		if (pm != null) 
		   pm.worked(i) ;
	}
	protected void tick(IProgressMonitor pm) {
		tick (1,pm) ;
	}
	
	/**
	 * Returns the className, parse the template if the name is not known in advance
	 * @return String
	 */
	public String getClassName(IProgressMonitor pm) {
		if (fClassName != null)
		   return fClassName;    
		      
       try {
       	parseTemplate(pm) ;
       	tick(pm) ;
		fClassName = getJetCompiler().getSkeleton().getClassName() ;		
	   }
	   catch (JETException e) {	   	
	   	org.eclipse.ve.internal.java.core.JavaVEPlugin.log(e) ;
	   }       		   		   
	   return fClassName ;
		   
	}
	/**
	 * Returns the className, parse the template if the name is not known in advance
	 * @return String
	 */
	public String getPackageName(IProgressMonitor pm) {
	   if (fPackageName != null)  return fPackageName ;
	   try {
       	parseTemplate(pm) ;
       	tick(pm) ;
		fPackageName = getJetCompiler().getSkeleton().getPackageName() ;
	   }
	   catch (JETException e) {	   	
	   	org.eclipse.ve.internal.java.core.JavaVEPlugin.log(e) ;
	   }       		   		   
	   return fPackageName ;
		
	}
	/**
	 * @return package/ClassName
	 */
	public String getFullClassName (IProgressMonitor pm) {
		String name = getClassName(pm) ;
		tick(pm) ;
		String result = null ;
		try {
			result = getJetCompiler().getSkeleton().getPackageName()+"."+name ; //$NON-NLS-1$
		}
		catch (JETException e) {}
        return result ;
	}

	/**
	 * Returns the jetCompiler 
	 * @return JETCompiler
	 */
	protected JETCompiler getJetCompiler() throws JETException {
		if (fJetCompiler!=null) return fJetCompiler ;
		
		fJetCompiler = new JETCompiler(fTemplateURIPath,fTemplate) ;
		return fJetCompiler;
	}
	protected void parseTemplate(IProgressMonitor pm) throws JETException {
		if (!parsed) {
			if (JavaVEPlugin.isLoggingLevel(Level.FINEST))
				JavaVEPlugin.log("TemplateObjectEmitter: parsing: "+fTemplate,Level.FINEST) ; //$NON-NLS-1$
			// Make sure ClassPath Var. are initialized
			org.eclipse.jdt.launching.JavaRuntime.getDefaultVMInstall() ;
			tick(pm) ;
			getJetCompiler().parse() ;
			tick(pm) ;
			parsed=true ;
		}		
	}
	/**
	 * Parse and Generate the template
	 */
	protected void generateTemplate(IProgressMonitor pm) throws JETException {
		if (fGeneratedSource==null) {
			parseTemplate(pm) ;
			ByteArrayOutputStream generatedTemplate = new ByteArrayOutputStream() ; ;
			tick(pm) ;
			getJetCompiler().generate(generatedTemplate) ;
			if (JavaVEPlugin.isLoggingLevel(Level.FINEST))
				JavaVEPlugin.log("TemplateObjectEmitter: generating"+fTemplate,Level.FINEST) ; //$NON-NLS-1$
			
			// convert the output String into a StringBuffer
			InputStream contents = new ByteArrayInputStream(generatedTemplate.toByteArray());						
			InputStreamReader reader = new InputStreamReader(contents);
			tick(pm) ;
			char[] buf = new char[4096];
			fGeneratedSource = new StringBuffer() ;
			try {
				int len=0 ;
				while ((len = reader.read(buf, 0, 4096)) != -1) {
					fGeneratedSource.append(buf,0,len) ;
				}
			}
			catch (IOException e) {
				fGeneratedSource=null ;
				throw new JETException(e.getMessage()) ;
			}
		}
	}
	public String getObjectSource(IProgressMonitor pm) throws JETException {
		if (fGeneratedSource==null) {
		  parseTemplate(pm) ;
		  generateTemplate(pm) ;
		}
		return fGeneratedSource.toString() ;				
	}
		

	protected Object getObject(String[] classPath, String newClassName, ClassLoader cl, IProgressMonitor pm) throws JETException, TemplatesException{
		if (fObjectEmitter == null) {
			// Do not try to compile the JET at this point, we may be able to find 
			// an existing object with a good time stamp.
			if (fTemplateTimeStamp<0) throw new TemplatesException("Invalid Template Time Stamp") ; //$NON-NLS-1$
			// Create an emitter with NO source first
			fObjectEmitter = new JavaObjectEmiter(null,getClassName(pm),
			                                      getPackageName(pm),
			                                      fDestinationPath, fTemplateTimeStamp) ;	
            fObjectEmitter.setEncodeing(fEncodeing) ;
		}
		Object o = fObjectEmitter.generateObjectFromExisting(cl,pm) ;
		if (o == null) {
			// Now compile the JET template and get the Java source
		    fObjectEmitter.setSrc(getObjectSource(pm).toCharArray()) ;
		    o = fObjectEmitter.generateObject(classPath,cl,pm) ;
		}
		return o ;
	}
	
	/**
	 * Returns the encodeing.
	 * @return String
	 */
	public String getEncodeing() {
		return fEncodeing;
	}

	/**
	 * Sets the encodeing.
	 * @param encodeing The encodeing to set
	 */
	public void setEncodeing(String encodeing) {
		fEncodeing = encodeing;
		if (fObjectEmitter!=null)
		  fObjectEmitter.setEncodeing(encodeing) ;
	}
	
	public String toString() {
		return "TemplateObjectEmiter("+fTemplate+","+fTemplateTimeStamp+")" ; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}


	/**
	 * Returns the templateTimeStamp.
	 * @return long
	 */
	public long getTemplateTimeStamp() {
		return fTemplateTimeStamp;
	}

}
