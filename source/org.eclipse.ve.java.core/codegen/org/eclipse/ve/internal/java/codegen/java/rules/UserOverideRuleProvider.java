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
 *  $RCSfile: UserOverideRuleProvider.java,v $
 *  $Revision: 1.10 $  $Date: 2006-02-21 17:16:35 $ 
 */
package org.eclipse.ve.internal.java.codegen.java.rules;

import java.io.*;
import java.util.List;
import java.util.logging.Level;

import org.eclipse.core.runtime.*;
import org.eclipse.ve.internal.cde.rules.IRule;
import org.eclipse.ve.internal.cde.rules.IRuleRegistry;
import org.eclipse.ve.internal.java.vce.rules.IRuleProvider;

import org.eclipse.ve.internal.java.core.JavaVEPlugin;
import org.eclipse.ve.internal.java.vce.templates.JavaObjectEmiter;
import org.eclipse.ve.internal.java.vce.templates.TemplateUtil;

/**
 * @author Gili Mendel
 * 
 * A user overide rule, is a rule whose source is expected to be imported to DEFAULT_SCRATCH_PATH, and
 * whos class is compiled into DEFAULT_SCRATCH_PATH. 
 */
public class UserOverideRuleProvider implements IRuleProvider {
	
	public static final IPath DEFAULT_SCRATCH_PATH = JavaVEPlugin.getPlugin().getStateLocation().append("Rules")  ; //$NON-NLS-1$ //$NON-NLS-2$
    public static final IPath DEFAULT_SCRATCH_SRC_PATH = DEFAULT_SCRATCH_PATH.append("src") ; //$NON-NLS-1$


    IRule	fRule = null ;
    Class	fRuleClass = null ;
    String	fID = null ;
    String  fClassname = null ;
    String  fStyle = null ;
    File	fsrcFile = null ;
    long	fTimeStamp = -1 ;  // Current Object's Time Stamp
    JavaObjectEmiter fEmiter = null ;
    String  classPath[] = null ;
    IRuleRegistry ruleRegistry;

	public UserOverideRuleProvider(String id, String className, String style, IRuleRegistry ruleRegistry) {
		fClassname = className ;
		fStyle = style ;
		fID = id ;
		Path p = new Path (fClassname.replace('.','/')) ;
		// The Rules directory is flat		
		fsrcFile = DEFAULT_SCRATCH_SRC_PATH.append(p.lastSegment()+".java").toFile() ;				 //$NON-NLS-1$
		this.ruleRegistry = ruleRegistry;
	}
	protected String[] getClassPath() {
		if (classPath != null) return classPath ;
		
		List list = TemplateUtil.getPluginAndPreReqJarPath("org.eclipse.ve.java.core"); //$NON-NLS-1$
		try {
			list.addAll(TemplateUtil.getPlatformJREPath());
		}
		catch (CoreException e) {}
		classPath = new String[list.size()];
		for (int i = 0; i < list.size(); i++) {
			classPath[i] = (String) list.get(i);
		}
		return  classPath ;
	}
	/**
	 * @see org.eclipse.ve.internal.java.codegen.java.rules.IRuleProvider#getRule()
	 */
	public IRule getRule() {
		
		JavaObjectEmiter e = getEmiter (getTimeStamp()) ;
		if (e == fEmiter) return fRule ;
		
		synchronized (this) {
			FileReader in = null;
			try {
				fRule = (IRule) e.generateObjectFromExisting(this.getClass().getClassLoader(), null);
				if (fRule == null) {
					// Need to compile
					if (!fsrcFile.canRead()) {
						if (JavaVEPlugin.isLoggingLevel(Level.WARNING))
							JavaVEPlugin.log("UserOverideRuleProvider: Can not read source file: " + fsrcFile.getAbsolutePath(), //$NON-NLS-1$
								Level.WARNING);
						return null;
					}
					in = new FileReader(fsrcFile);
					char[] buf = new char[4096];
					StringBuffer sBuf = new StringBuffer();
					while (true) {
						int len = in.read(buf, 0, buf.length);
						if (len <= 0)
							break;
						sBuf.append(buf, 0, len);
					}
					e.setSrc(sBuf.toString().toCharArray());
					fRule = (IRule) e.generateObject(getClassPath(), this.getClass().getClassLoader(), null);
				}

				fRule.setRegistry(ruleRegistry);
			} catch (Exception ex) {
				if (JavaVEPlugin.isLoggingLevel(Level.WARNING))
					JavaVEPlugin.log("UserOverideRuleProvider.getRule(): " + ex.getMessage(), //$NON-NLS-1$
						Level.WARNING);
			} finally {
				if (in != null)
					try {
						in.close();
					} catch (IOException ex) {
					}
			}
			if (fRule != null) {
				fEmiter = e;
				fTimeStamp = fsrcFile.lastModified();
			}
		}
		return fRule;
	}
	
	protected JavaObjectEmiter  getEmiter(long ts) {
		if (fEmiter == null || fEmiter.getSrcTimeStamp() < ts) {
			Path p = new Path (fClassname.replace('.','/')) ; 
			return  new JavaObjectEmiter(null,p.lastSegment(),
		                               p.removeLastSegments(1).toString().replace(IPath.SEPARATOR,'.'),
		                               DEFAULT_SCRATCH_PATH,getTimeStamp()) ;
	
		}
		return fEmiter ;
	}

	/**
	 * @see org.eclipse.ve.internal.java.codegen.java.rules.IRuleProvider#getTimeStamp()
	 */
	public long getTimeStamp() {
		if (fsrcFile.canRead())
		   return fsrcFile.lastModified() ;
		else
		   return -1 ;
	}

	/**
	 * @see org.eclipse.ve.internal.java.codegen.java.rules.IRuleProvider#getSourceLocation()
	 */
	public String getSourceLocation() {
		return fsrcFile.getAbsolutePath();
	}

	/**
	 * @see org.eclipse.ve.internal.java.codegen.java.rules.IRuleProvider#getStyle()
	 */
	public String getStyle() {
		return fStyle ;
	}
	
	public String toString() {
		return "User Overide Rule(id="+getRuleID()+", source="+getSourceLocation()+")"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}

	/**
	 * @see org.eclipse.ve.internal.java.codegen.java.rules.IRuleProvider#getID()
	 */
	public String getRuleID() {
		return fID;
	}

	/**
	 * @see org.eclipse.ve.internal.java.codegen.java.rules.IRuleProvider#getProviderID()
	 */
	public String getProviderID() {
		return getSourceLocation() ;
	}

}
