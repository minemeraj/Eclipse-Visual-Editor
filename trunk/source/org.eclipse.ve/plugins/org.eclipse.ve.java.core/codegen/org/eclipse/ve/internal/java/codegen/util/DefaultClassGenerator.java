/*
 * Created on Apr 30, 2004 by Gili Mendel
 */
/*******************************************************************************
 * Copyright (c) 2001, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.java.codegen.util;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.formatter.CodeFormatter;
import org.eclipse.jdt.core.internal.dom.rewrite.CodeFormatterUtil;
import org.eclipse.jdt.internal.formatter.DefaultCodeFormatter;
import org.eclipse.jdt.ui.CodeGeneration;
import org.eclipse.jdt.ui.PreferenceConstants;
import org.eclipse.text.edits.TextEdit;


/**
 * @author Gili Mendel
 *
 *  The default generator assume that the template's name is the same to its file name
 *  with the .javajet extension
 */
public class DefaultClassGenerator extends AbstractClassGenerator {
	
	String ftmplDir;
	String ftmplName;
	String fpluginName;
	
	public DefaultClassGenerator(String className, String pkgName, String pluginName, String tmplDirectory, String tmplName) {
		super(className, pkgName);	
		ftmplName=tmplName;
		ftmplDir=tmplDirectory;
		fpluginName=pluginName;
	}	
	
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.util.AbstractClassGenerator#getClassTemplate()
	 */
	protected IClassTemplate getClassTemplate() {
		return getClassTemplate(ftmplName+JAVAJET_EXT,ftmplName) ;
	}
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.util.AbstractClassGenerator#getTemplateInfo()
	 */
	protected ClassInfo getTemplateInfo() {
		return new ClassInfo();
	}
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.util.AbstractClassGenerator#getBasePlugin()
	 */
	protected String getBasePlugin() {
		return fpluginName;
	}
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.util.AbstractClassGenerator#getTemplatePath()
	 */
	protected String getTemplateDirPath() {		
		return ftmplDir;
	}
	
	public String getTemplteContent() {
		return getClassTemplate().generateClassContent(getTemplateInfo());
	}
	
	public void generateClass(IProgressMonitor monitor) throws CodeGenException {
	    if (fProject == null) throw new CodeGenException ("Project is not set") ;
		IPackageFragment pkg = generatePackageIfNeeded(monitor);
		try {
			ICompilationUnit cu = pkg.createCompilationUnit(fClassName+".java",getTemplteContent(),true,monitor);
			if (fFormatTemplate) {
				ICompilationUnit wc = cu.getWorkingCopy(monitor);
				if (PreferenceConstants.getPreferenceStore().getBoolean(
						PreferenceConstants.CODEGEN_ADD_COMMENTS)) {
					// Add Type default comments
					try {
						String comment = CodeGeneration.getTypeComment(cu, cu
								.getAllTypes()[0].getElementName(), fNL);
						wc.getBuffer().replace(0, 0, comment);
					} catch (CoreException e1) {
					}
				}
				DefaultCodeFormatter formatter = new DefaultCodeFormatter();
				String content = wc.getSource();
				TextEdit te = formatter.format(
						CodeFormatter.K_COMPILATION_UNIT, content, 0, content.length(), 0, fNL);
				content = CodeFormatterUtil.evaluateFormatterEdit(content, te,
						null);
				wc.getBuffer().setContents(content);
				wc.commitWorkingCopy(true, monitor);
			}
		} catch (JavaModelException e) {
			throw new CodeGenException(e.getMessage()) ;
		}
	}


}
