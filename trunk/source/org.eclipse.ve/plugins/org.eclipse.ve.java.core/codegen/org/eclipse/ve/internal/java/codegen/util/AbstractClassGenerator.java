/*******************************************************************************
 * Copyright (c) 2001, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 * Created on Apr 30, 2004 by Gili Mendel
 */

/*
*  $RCSfile: AbstractClassGenerator.java,v $
*  $Revision: 1.8 $
*/

package org.eclipse.ve.internal.java.codegen.util;

import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.*;

import org.eclipse.ve.internal.java.vce.templates.*;

/**
 * @author Gili Mendel
 *
 * This class will provide all the plumbing of creating a .java file from a jet template 
 */
public abstract class AbstractClassGenerator {
	public final static String  JAVAJET_EXT = ".javajet" ; //$NON-NLS-1$	
	
	public interface IClassTemplate {
		public String generateClassContent(AbstractClassGenerator.ClassInfo info) ;
	};
	

	protected	String			fNL=System.getProperties().getProperty("line.separator"); //$NON-NLS-1$
	protected   String[]		fComments = null;
	protected   String		   	fPackageName=null;
	protected  	String		   	fClassName=null;
	protected	IJavaProject    fProject=null;
	protected	boolean 		fFormatTemplate = true ;  // use the JDT formatter on the template
	protected   Map				fProjectOptions = null;
		
	protected	IClassTemplate  fTemplate = null ;
			
	public class ClassInfo {
		public String		   fSeperator;
		public String		   fComments[] ;			// method's JavaDoc/Comments
		public String		   fPackageName;
		public String		   fClassName;
		
		public ClassInfo() {
			this.fSeperator = AbstractClassGenerator.this.fNL;
			this.fComments = AbstractClassGenerator.this.fComments;
			this.fPackageName =  AbstractClassGenerator.this.fPackageName;
			this.fClassName = AbstractClassGenerator.this.fClassName;
		}
	}
	
	public AbstractClassGenerator(String className, String pkgName) {
		fClassName=className;
		fPackageName=pkgName;
	}

	protected abstract IClassTemplate getClassTemplate() ;
	protected abstract AbstractClassGenerator.ClassInfo getTemplateInfo();	
	protected abstract String getBasePlugin() ;	
	protected abstract String getTemplateDirPath() ;

	public String toString() {
		return "ClassGenerator for: " + fClassName ; //$NON-NLS-1$
	}



	protected IClassTemplate getClassTemplate(String templateFileName, String className) {
		if (fTemplate != null) return fTemplate ;
		
		try {
			List list = TemplateUtil.getPluginAndPreReqJarPath(getBasePlugin());
			list.addAll(TemplateUtil.getPlatformJREPath());
			String[] classPath = (String[]) list.toArray(new String[list.size()]);
			String   templatePath = TemplateUtil.getPathForBundleFile(getBasePlugin(), getTemplateDirPath()) ;
			
			fTemplate = (IClassTemplate)
			TemplateObjectFactory.getClassInstance(classPath, new String[] {templatePath}, 
			templateFileName, TemplateUtil.getClassLoader(getBasePlugin()),
			className,null) ;
		}
		catch (TemplatesException e) {
			org.eclipse.ve.internal.java.core.JavaVEPlugin.log(e) ;
		} 	                                                            	
		return fTemplate ; 	
	}


	/**
	 * @param comments The fComments to set.
	 */
	public void setComments(String[] comments) {
		fComments = comments;
	}	
	
	protected IPackageFragment generatePackageIfNeeded(IProgressMonitor monitor) throws CodeGenException {
		if (fPackageName != null) {
			IPackageFragment pkg = null;
			try {
				IPackageFragment[] pkgs = fProject.getPackageFragments();
				for (int i = 0; i < pkgs.length; i++) {
					if (pkgs[i].getElementName().equals(fPackageName)) {
						pkg = pkgs[i];
						break;
					}
				}
				if (pkg == null) {
					// for now find the first src folder we can find
					// in the future we want to create a special JVE src
					// directory
					IPackageFragmentRoot root = null;
					IPackageFragmentRoot[] all = fProject
							.getAllPackageFragmentRoots();
					for (int i = 0; i < all.length; i++) {
						if (all[i].getKind() == IPackageFragmentRoot.K_SOURCE) {
							root = all[i];
							break;
						}
					}
					if (root != null) {
						pkg = root.createPackageFragment(fPackageName, true,
								monitor);
					}
				}
			} catch (JavaModelException e) {
				throw new CodeGenException(e.getMessage());
			}
			return pkg;
		}
		return null; 
	}

	
	/**
	 * @param project The fProject to set.
	 * @todo Generated comment
	 */
	public void setProject(IJavaProject project) {
		fProject = project;
		fProjectOptions = project.getOptions(true);
	}
	/**
	 * @param formatTemplate The fFormatTemplate to set.
	 * @todo Generated comment
	 */
	public void setFormatTemplate(boolean formatTemplate) {
		fFormatTemplate = formatTemplate;
	}
}
