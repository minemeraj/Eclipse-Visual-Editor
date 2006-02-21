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
 *  $Revision: 1.9 $  $Date: 2006-02-21 17:16:35 $ 
 */
package org.eclipse.ve.internal.java.vce.templates;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Hashtable;

import org.eclipse.core.runtime.*;
import org.eclipse.emf.codegen.jet.JETCompiler;
import org.eclipse.emf.codegen.jet.JETException;

import org.eclipse.jem.internal.proxy.core.ProxyPlugin;

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
			   e = new TemplateObjectEmitter(templateURIPath, template, ts, JavaVEPlugin.VE_GENERATED_OBJECTs_DESTINATION,classname);
		    else
		       e = new TemplateObjectEmitter(templateURIPath, template, ts, JavaVEPlugin.VE_GENERATED_OBJECTs_DESTINATION);
			fEmitters.put(tPath, e);
		}
		return e;
	}

	/**
	 * @return int denoting the template last modified timestampe if the template is readable. If the template is inside
	 * a jar, then the file for the jar will be used for the timestamp instead. "-1" if invalid, not a file.
	 */
	protected static long getTemplateTimeStamp(String[] templateURIPath, String template) {

		try {
			String templateAbsoluteURI = JETCompiler.find(templateURIPath, template);
			if (templateAbsoluteURI != null) {
				URL url;
				try {
					url = new URL(templateAbsoluteURI);
					url = ProxyPlugin.getFilePath(FileLocator.resolve(url));
				} catch (MalformedURLException exception) {
					url = new URL("file:" + templateAbsoluteURI); //$NON-NLS-1$
				}
				if ("file".equals(url.getProtocol())) { //$NON-NLS-1$
					File tf = new File(url.getFile());
					if (tf.canRead())
						return tf.lastModified();
				}
			}
		} catch (MalformedURLException e) {
		} catch (IOException e) {
		}
		return -1;
	}
	/**
	 * Given a template, parse and generate the source, if needed.
	 */
	public static String getClassSource(String[] templateURIPath, String template, String className, IProgressMonitor pm) throws TemplatesException {

		try {
			long ts = getTemplateTimeStamp(templateURIPath, template);
			if (ts == -1)
				throw new JETException("Template not found: " + template); //$NON-NLS-1$

			TemplateObjectEmitter e = getEmitter(templateURIPath, template, ts, null);
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
			long ts = getTemplateTimeStamp(templateURIPath, template);
			if (ts == -1)
				throw new JETException("Template not found: " + template); //$NON-NLS-1$

            TemplateObjectEmitter e ;            
            e = getEmitter(templateURIPath, template, ts,className);

			return e.getObject(jdtCompileClassPath, e.getClassName(pm), cl, pm);
		}
		catch (JETException e) {
			throw new TemplatesException(e);
		}

	}
}
