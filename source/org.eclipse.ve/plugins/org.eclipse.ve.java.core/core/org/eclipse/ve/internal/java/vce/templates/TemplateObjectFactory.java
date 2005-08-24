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
 *  $RCSfile: TemplateObjectFactory.java,v $
 *  $Revision: 1.6 $  $Date: 2005-08-24 23:30:48 $ 
 */
package org.eclipse.ve.internal.java.vce.templates;

import java.io.File;
import java.util.Hashtable;

import org.eclipse.core.runtime.*;
import org.eclipse.emf.codegen.jet.JETCompiler;
import org.eclipse.emf.codegen.jet.JETException;

import org.eclipse.ve.internal.java.core.JavaVEPlugin;

/**
 * @author Gili Mendel
 * 
 * This factory class provides simple mechanism for one generate objects associated with JavaJet templates.
 * It deals with time stamps and caches Object emitters accordingly.  
 * 
 */
public class TemplateObjectFactory {
	
	static Hashtable fEmitters = new Hashtable();

	protected static TemplateObjectEmitter getEmitter(String[] templateURIPath, String template, long ts, String classname) {
		String tPath = JETCompiler.find(templateURIPath, template) ;
		TemplateObjectEmitter e = (TemplateObjectEmitter) fEmitters.get(tPath);
		// only reUse this Emitter if it's time stamp is valid
		if (e == null || e.getTemplateTimeStamp() < ts) {
			if (classname != null)
			   e = new TemplateObjectEmitter(templateURIPath, template, JavaVEPlugin.VE_GENERATED_OBJECTs_DESTINATION,classname);
		    else
		       e = new TemplateObjectEmitter(templateURIPath, template, JavaVEPlugin.VE_GENERATED_OBJECTs_DESTINATION);
			fEmitters.put(tPath, e);
		}
		return e;
	}

	/**
	 * @return File denoting the template if the template is readable.
	 */
	protected static File getTemplate(String[] templateURIPath, String template) {

		try {
//// Work around for JET path resolution on unix
//for (int i = 0; i < templateURIPath.length; i++) {
//	if (templateURIPath[i].startsWith("/") && !templateURIPath[i].startsWith("//"))	 //$NON-NLS-1$ //$NON-NLS-2$
//	     templateURIPath[i]="/"+ templateURIPath[i];	 //$NON-NLS-1$
//}			
			String templateAbsoluteURI = JETCompiler.find(templateURIPath, template);
			File tf = new File(templateAbsoluteURI);
			if (!tf.canRead()) {
				return null;
			}
			return tf;
		}
		catch (Exception e) {
			return null;
		}
	}
	/**
	 * Given a template, parse and generate the source, if needed.
	 */
	public static String getClassSource(String[] templateURIPath, String template, String className, IProgressMonitor pm) throws TemplatesException {

		try {
			File tf = getTemplate(templateURIPath, template);
			if (tf == null)
				throw new JETException("Template not found: " + template); //$NON-NLS-1$

			TemplateObjectEmitter e = getEmitter(templateURIPath, template, tf.lastModified(),null);
			return e.getObjectSource(pm);
		}
		catch (JETException e) {
			throw new TemplatesException(e);
		}
	}

	/**
	 * The method takes a template, and will compile it, if needed in order to return
	 * an instance Object.
	 * 
	 * @param classPath to be used to compile the .java file associated with a template
	 * @param templateURIPath is the class path to search for the template
	 * @param template the name of the template file
	 * @param The default classloader to use for loading the class
	 * @param optional (the to be generated) class's name. If given (not null) and a valid class already exists
	 *         than the template will not be parsed.
	 * @param optional progress bar (use null if none) 
	 * 
	 */
	public static Object getClassInstance(String[] jdtCompileClassPath, String[] templateURIPath, String template, ClassLoader cl, String className, IProgressMonitor pm) throws TemplatesException {

		try {
			File tf = getTemplate(templateURIPath, template);
			if (tf == null)
				throw new JETException("Template not found: " + template); //$NON-NLS-1$

            TemplateObjectEmitter e ;            
            e = getEmitter(templateURIPath, template, tf.lastModified(),className);

			return e.getObject(jdtCompileClassPath, e.getClassName(pm), cl, pm);
		}
		catch (JETException e) {
			throw new TemplatesException(e);
		}

	}
}
