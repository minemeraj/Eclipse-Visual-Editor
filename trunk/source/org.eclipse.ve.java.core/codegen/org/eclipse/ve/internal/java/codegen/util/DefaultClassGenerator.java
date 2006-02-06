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
*  $RCSfile: DefaultClassGenerator.java,v $
*  $Revision: 1.11 $
*/


package org.eclipse.ve.internal.java.codegen.util;

import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.formatter.CodeFormatter;
import org.eclipse.jdt.ui.CodeGeneration;
import org.eclipse.jdt.ui.PreferenceConstants;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.text.edits.MalformedTreeException;
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
	
	
	/**
	 * 
	 * @param className		name of class to generate  e.g., 			"MyGeneratedClass"
	 * @param pkgName		pkg. to place the generated class in e.g.,	"my.package"
	 * @param pluginName	plugin where the JJet template is located	"torg.eclipse.ve.jfc"
	 * @param tmplDirectory	directory where the template is located		"templates/com/ibm/jjet"
	 * @param tmplName		template file name (assume .javajet extension)	"MyGeneratedClassTemplate"
	 */
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

	/**
	 * 
	 * @param src  Text to format 
	 * @param kind  see <code>CodeFormatter</code>
	 * @param options project option, or null for default options
	 * @param lineSeperator
	 * @return formated text
	 * 
	 * @since 1.1.0
	 */
	public static String format(String src, int kind, Map options, String lineSeperator) {
		CodeFormatter formatter = ToolFactory.createCodeFormatter(options);
		TextEdit te = formatter.format(kind, src, 0, src.length(), 0, lineSeperator);
		
		if (te != null) {
			Document d = new Document (src);
			try {
				te.apply(d);
			} catch (MalformedTreeException e) {
			} catch (BadLocationException e) {
			}
			return d.get();		
		}		
		return src;	// If we got here then formatting failed, so just return the src string unchanged and let it show as syntax error.
	}
	
	public void generateClass(IProgressMonitor monitor) throws CodeGenException {
	    if (fProject == null) throw new CodeGenException ("Project is not set") ; //$NON-NLS-1$
		IPackageFragment pkg = generatePackageIfNeeded(monitor);
		try {
			ICompilationUnit cu = pkg.createCompilationUnit(fClassName+".java",getTemplteContent(),true,monitor); //$NON-NLS-1$
			if (fFormatTemplate) {
				ICompilationUnit wc = cu.getWorkingCopy(monitor);
				if (PreferenceConstants.getPreferenceStore().getBoolean(PreferenceConstants.CODEGEN_ADD_COMMENTS)) {
					// Add Type default comments
					try {
						String comment = CodeGeneration.getTypeComment(cu, cu
								.getAllTypes()[0].getElementName(), fNL);

						if (wc.getPackageDeclarations().length>0) {
						 // The type template will generate the package header
						 ISourceRange sr = wc.getPackageDeclarations()[0].getSourceRange() ;
						 wc.getBuffer().replace(sr.getOffset(),sr.getLength(),""); //$NON-NLS-1$
						}						
						String content = CodeGeneration.getCompilationUnitContent(wc,comment,wc.getSource(), fNL );						
						wc.getBuffer().setContents(content);
						
					} catch (CoreException e1) {
					}
				}				
				String content = format (wc.getSource(), CodeFormatter.K_COMPILATION_UNIT, fProjectOptions,  fNL);
				wc.getBuffer().setContents(content);
				wc.commitWorkingCopy(true, monitor);
			}
		} catch (JavaModelException e) {
			throw new CodeGenException(e.getMessage()) ;
		}
	}


}
