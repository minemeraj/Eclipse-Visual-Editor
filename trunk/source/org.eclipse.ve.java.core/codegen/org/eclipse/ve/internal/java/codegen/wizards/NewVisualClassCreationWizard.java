package org.eclipse.ve.internal.java.codegen.wizards;
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
/*
 *  $RCSfile: NewVisualClassCreationWizard.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 17:48:30 $ 
 */

import java.net.URL;
import java.util.*;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.*;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.internal.ui.wizards.NewElementWizard;
import org.eclipse.jdt.ui.wizards.NewClassWizardPage;
import org.eclipse.jem.internal.core.MsgLogger;
import org.eclipse.ui.*;

import org.eclipse.ve.internal.java.core.JavaVEPlugin;

import org.eclipse.ve.internal.java.codegen.core.CodegenMessages;
import org.eclipse.ve.internal.java.vce.templates.*;

/**
 * @author JoeWin
 */
public class NewVisualClassCreationWizard extends NewElementWizard implements IExecutableExtension{
	
	private NewClassWizardPage fPage;
	private String superClassName = null;
	private IVisualClassCreationSourceContributor contributor = null;
	private Plugin contributorPlugin = null;
	public static final String VISUAL_CLASS_WIZARD_SUPER_CLASS_KEY = "VISUAL_CLASS_WIZARD_SUPER_CLASS_KEY";
	private static String DEFAULT_SUPER_CLASS = "javax.swing.JFrame";
	public static String NEWLINE =  System.getProperty("line.separator") ; //$NON-NLS-1$

	public NewVisualClassCreationWizard() {
		super();
		setDefaultPageImageDescriptor(JavaVEPlugin.getWizardTitleImageDescriptor());
        setDialogSettings(JavaVEPlugin.getPlugin().getDialogSettings());		
		setWindowTitle(CodegenMessages.getString("NewVisualClassCreationWizard.title")); //$NON-NLS-1$
	}
	
	/**
	 * Sets the contributor who will be providing for the source of the input className
	 * @param className
	 * @param monitor
	 */
	protected void updateContributor(String className, IProgressMonitor monitor){
		IExtensionPoint exp = JavaVEPlugin.getPlugin().getDescriptor().getExtensionPoint("newsource"); //$NON-NLS-1$
		IExtension[] extensions = exp.getExtensions();
		IType superClass = null;
		try {
			superClass = fPage.getPackageFragmentRoot().getJavaProject().findType(className);
		} catch (JavaModelException e2) {
			JavaVEPlugin.log(e2, MsgLogger.LOG_FINEST);
		}
		if(extensions!=null && extensions.length>0 && superClass!=null){
			boolean contributorFound = false;
			for(int ec=0;ec<extensions.length && !contributorFound;ec++){
				IConfigurationElement[] configElms = extensions[ec].getConfigurationElements();
				for(int cc=0;cc<configElms.length && !contributorFound;cc++){
					IConfigurationElement celm = configElms[cc];
					try {
						String typeName = celm.getAttribute("type");
						if(superClass.getFullyQualifiedName().equals(typeName)){
							contributor = (IVisualClassCreationSourceContributor) celm.createExecutableExtension("class"); //$NON-NLS-1$
							if(contributor!=null){
								contributorPlugin = extensions[ec].getDeclaringPluginDescriptor().getPlugin();
							}
							contributorFound = true;
						}
					} catch (CoreException e) {
						JavaVEPlugin.log(e, MsgLogger.LOG_FINEST);
					}
				}
			}
		}
	}
	
	private IVisualClassCreationSourceGenerator getGeneratorInstance(URL templateLocation){
		java.util.List jdtClassPath = TemplateUtil.getPluginAndPreReqJarPath(JavaVEPlugin.getPlugin().getDescriptor().getUniqueIdentifier());
		try {
			jdtClassPath.add(TemplateUtil.getPlatformJREPath());
		} catch (TemplatesException  e) {
			JavaVEPlugin.log(e, MsgLogger.LOG_FINE);
		}
		List contributorsPaths = TemplateUtil.getPluginAndPreReqJarPath(contributorPlugin.getDescriptor().getUniqueIdentifier()); 
		for(int i=0;i<contributorsPaths.size();i++)
			if(!jdtClassPath.contains(contributorsPaths.get(i)))
				jdtClassPath.add(contributorsPaths.get(i));
		String[] jdtClassPaths = new String[jdtClassPath.size()];
		for(int i=0;i<jdtClassPath.size();i++)
			jdtClassPaths[i] = (String)jdtClassPath.get(i);
		String templatePath = templateLocation.getPath().substring(0,templateLocation.getPath().lastIndexOf('/'));
		String[] templatePaths = {  templatePath };
		try {
			return (IVisualClassCreationSourceGenerator) TemplateObjectFactory.getClassInstance(jdtClassPaths, templatePaths, templateLocation.getPath().substring(templateLocation.getPath().lastIndexOf('/')+1), contributor.getClass().getClassLoader(), null, null);
		} catch (TemplatesException e) {
			JavaVEPlugin.log(e, MsgLogger.LOG_WARNING);
		}
		return null;
	}
	
	protected String removeWhiteSpaces(String input){
		char[] chars = input.toCharArray();
		char[] noSpaces = new char[chars.length];
		int count = 0;
		for (int i = 0; i < chars.length; i++) {
			char c = chars[i];
			if(Character.isWhitespace(c))
				continue;
			noSpaces[count] = c;
			count++;
		}
		return new String(noSpaces,0,count);
	}
	
	protected void merge(IMethod to, IMethod from, ICodeFormatter formatter, IProgressMonitor monitor){
		try {
			StringBuffer finalSource = new StringBuffer();
			
			String toCUSource = to.getCompilationUnit().getSource();
			String fromCUSource = from.getCompilationUnit().getSource();
			int toStart = to.getSourceRange().getOffset();
			int toEnd = to.getSourceRange().getOffset()+to.getSourceRange().getLength();
			toStart = toCUSource.lastIndexOf('\n',toStart)+1;
			toEnd = toCUSource.indexOf('\n',toEnd);
			int fromStart = from.getSourceRange().getOffset();
			int fromEnd = from.getSourceRange().getOffset()+from.getSourceRange().getLength();
			fromStart = fromCUSource.lastIndexOf('\n', fromStart)+1;
			fromEnd = fromCUSource.indexOf('\n', fromEnd);
			
			int fromNameOffset = from.getNameRange().getOffset();
			int toNameOffset = to.getNameRange().getOffset();
			int fromBodyStart = fromCUSource.indexOf('{', fromNameOffset)+1;
			int fromBodyEnd = fromCUSource.lastIndexOf('}',fromEnd)-1;
			int toBodyStart = toCUSource.indexOf('{', toNameOffset)+1;
			int toBodyEnd = toCUSource.lastIndexOf('}', toEnd)-1;
			
			StringTokenizer toNLs = new StringTokenizer(toCUSource.substring(toBodyStart, toBodyEnd), "\r\n", false);
			StringTokenizer fromNLs = new StringTokenizer(fromCUSource.substring(fromBodyStart, fromBodyEnd), "\r\n", false);
			List toLines = new ArrayList();
			List fromLines = new ArrayList();
			int bestToLineIndex = 0;
			while(toNLs.hasMoreTokens()){
				String token = toNLs.nextToken();
				toLines.add(token);	
				//	We shouldnt be adding lines after the 'return something;' statement.
				// As it would result in errors.
				if(token.indexOf("return")>-1) 
					break;
				bestToLineIndex++;
			}
			while(fromNLs.hasMoreTokens())
				fromLines.add(fromNLs.nextToken());
			for(int fc=0;fc<fromLines.size();fc++){
				String fromLine = (String)fromLines.get(fc);
				String fromLineNoSpaces = removeWhiteSpaces(fromLine);
				boolean foundInToLines = false;
				boolean fromLineIsSuperConstructorCall = from.isConstructor() && fromLineNoSpaces.indexOf("super(")>-1; 
				for(int tc=0;tc<toLines.size();tc++){
					String toLine = (String) toLines.get(tc);
					String toLineNoSpaces = removeWhiteSpaces(toLine);
					boolean toLineIsSuperConstructorCall = to.isConstructor() && toLineNoSpaces.indexOf("super(")>-1;
					if(toLineNoSpaces.equals(fromLineNoSpaces) || (toLineIsSuperConstructorCall && fromLineIsSuperConstructorCall)){
						foundInToLines = true;
						break;
					}
				}
				if(!foundInToLines){
					toLines.add(bestToLineIndex, fromLine);
					bestToLineIndex++;
				}
			}
			
			finalSource.append(toCUSource.substring(toStart, toBodyStart)+NEWLINE);
			for(int i=0;i<toLines.size();i++)
				finalSource.append(toLines.get(i)+NEWLINE);
			finalSource.append(toCUSource.substring(toBodyEnd, toEnd));
			
			IType declaringType = to.getDeclaringType();
			IJavaElement sibling = null;
			for(int i=0;i<declaringType.getChildren().length;i++){
				if(declaringType.getChildren()[i].equals(to) && i<declaringType.getChildren().length-1){
					sibling = declaringType.getChildren()[i+1];
					break;
				}
			}
			to.delete(true, monitor);
			String source = finalSource.toString();
			if(formatter!=null)
				source = formatter.format(source, 1, null, NEWLINE);
			declaringType.createMethod(NEWLINE+source+NEWLINE, sibling, true, monitor);
		} catch (JavaModelException e) {
			JavaVEPlugin.log(e, MsgLogger.LOG_FINE);
		}
	}
	
	protected String getCompleteSource(ICompilationUnit cu, ISourceReference srcRef){
		try {
			 String cuSource = cu.getSource();
			 int from = srcRef.getSourceRange().getOffset();
			 int to = from + srcRef.getSourceRange().getLength();
			 int fromNL = cuSource.lastIndexOf('\n',from);
			 int toNL = cuSource.indexOf('\n',to);
			 if(fromNL>-1 && fromNL<cuSource.length())
			 	from = fromNL+1;
			 if(toNL>-1 && toNL<cuSource.length())
			 	to = toNL+1;
			 return cuSource.substring(from, to);
		} catch (JavaModelException e) {
			JavaVEPlugin.log(e, MsgLogger.LOG_FINE);
		}
		return null;
	}
	
	protected void merge(ICompilationUnit to, ICompilationUnit from, ICodeFormatter formatter, IProgressMonitor monitor){
		try {
			IType toType = to.getTypes()[0];
			IType fromType = from.getTypes()[0];
			for(int i=0;i<fromType.getChildren().length;i++){
				IJavaElement child = fromType.getChildren()[i];
				if (child instanceof IField) {
					IField field = (IField) child;
					String source = getCompleteSource(field.getCompilationUnit(), field);
					if(formatter!=null)
						source = formatter.format(source, 1, null, NEWLINE);
					toType.createField(source+NEWLINE, null, true, monitor);
				}
				if (child instanceof IMethod) {
					IMethod method = (IMethod) child;
					// There could be multiple target methods into which "method"s statements should go into.
					IMethod[] targetMethods = null;
					if(method.isConstructor()){
						List constructors = new ArrayList();
						IMethod[] allMethods = toType.getMethods();
						for(int mc=0;mc<allMethods.length;mc++)
							if(allMethods[mc].isConstructor())
								constructors.add(allMethods[mc]);
						targetMethods = new IMethod[constructors.size()];
						constructors.toArray(targetMethods);
					}else{
						targetMethods = toType.findMethods(method);
					}
					if(targetMethods!=null && targetMethods.length>0){
						for(int tmc=0;tmc<targetMethods.length;tmc++)
							merge(targetMethods[tmc], method, formatter, monitor);
					}else{
						String source = getCompleteSource(method.getCompilationUnit(), method);
						if(formatter!=null)
							source = formatter.format(source, 1, null, NEWLINE);
						toType.createMethod(source, null, true, monitor);
					}
				}
				if (child instanceof IType) {
					IType type = (IType) child;
					String source = getCompleteSource(type.getCompilationUnit(), type);
					if(formatter!=null)
						source = formatter.format(source, 0, null, NEWLINE);
					toType.createType(source, null, true, monitor);
				}
			}
		} catch (JavaModelException e) {
			JavaVEPlugin.log(e, MsgLogger.LOG_FINE);
		}
	}
	
	protected void applyContributor(IType type1, String superClassName, IProgressMonitor monitor){
		IVisualClassCreationSourceGenerator gen = getGeneratorInstance(contributor.getTemplateLocation());
		try {
			ICompilationUnit originalCU = type1.getCompilationUnit(); 
			if (originalCU.isWorkingCopy())
				originalCU = (ICompilationUnit) originalCU.getOriginal(originalCU);
			String src = gen.generateSource(originalCU.getTypes()[0].getElementName(), superClassName);
			ICompilationUnit workingCopy = (ICompilationUnit) originalCU.getWorkingCopy() ;
			workingCopy.getBuffer().setContents(src);
			workingCopy.reconcile();
			ICodeFormatter formatter = contributor.needsFormatting()?ToolFactory.createCodeFormatter():null;
			merge(originalCU, workingCopy, formatter, monitor);
			workingCopy.destroy();
		} catch (JavaModelException e) {
			JavaVEPlugin.log(e, MsgLogger.LOG_FINE);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jdt.internal.ui.wizards.NewElementWizard#finishPage(org.eclipse.core.runtime.IProgressMonitor)
	 */
	protected void finishPage(IProgressMonitor monitor) throws InterruptedException, CoreException {
		// Store the changed default super class name if one was not specified.
		if(superClassName==null && fPage.getSuperClass()!=null){
			String sc = fPage.getSuperClass();
			boolean found = false;
			NewVisualClassWizardPage page = (NewVisualClassWizardPage) fPage;
			for(int i=0;i<page.AWTButtonClasses.length && !found;i++)
			   if(page.AWTButtonClasses[i].equals(sc))
				   found = true;
			for(int i=0;i<page.swingButtonClasses.length && !found;i++)
			   if(page.swingButtonClasses[i].equals(sc))
				   found = true;
			if(found)
			JavaVEPlugin.getPlugin().getPluginPreferences().setValue(VISUAL_CLASS_WIZARD_SUPER_CLASS_KEY, fPage.getSuperClass());
			else
			JavaVEPlugin.getPlugin().getPluginPreferences().setValue(VISUAL_CLASS_WIZARD_SUPER_CLASS_KEY, "java.lang.Object");
		}
		fPage.createType(monitor); // use the full progress monitor
		
		ICompilationUnit cu= fPage.getCreatedType().getCompilationUnit();
		if (cu.isWorkingCopy())
			cu = (ICompilationUnit) cu.getOriginal(cu);
		if (cu != null) {
			IResource resource= cu.getResource();
			selectAndReveal(resource);
			openResource(resource);
		}	

		updateContributor(fPage.getSuperClass(), monitor);
		if(contributor!=null){
			applyContributor(fPage.getCreatedType(), fPage.getSuperClass(), monitor);
		}
	}
	
	public void addPages() {
		fPage = new NewVisualClassWizardPage();
		addPage(fPage);
		fPage.init(getSelection());
		if(superClassName!=null){
			fPage.setSuperClass(superClassName, false);
		}else{
			// TODO Set the defaults in the JavaVEPlugin instead of overhere - Eclipse style
			// Set the selection to what was used previously OR default if no previous 
			Preferences preferences = JavaVEPlugin.getPlugin().getPluginPreferences();
			if(!preferences.getDefaultString(VISUAL_CLASS_WIZARD_SUPER_CLASS_KEY).equals(DEFAULT_SUPER_CLASS))
				preferences.setDefault(VISUAL_CLASS_WIZARD_SUPER_CLASS_KEY, DEFAULT_SUPER_CLASS);
			fPage.setSuperClass(preferences.getString(VISUAL_CLASS_WIZARD_SUPER_CLASS_KEY), true);
		}
	}
	
	protected void openResource(IResource resource) {
		openResourceJVE(resource);
	}
		
	public static void openResourceJVE(final IResource resource) {
		if (resource.getType() == IResource.FILE) {
			IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
			if (window != null) {
				final IWorkbenchPage activePage = window.getActivePage();
				if (activePage != null) {
					window.getShell().getDisplay().asyncExec(new Runnable() {
						public void run() {
							try {
								activePage.openEditor((IFile) resource, "org.eclipse.ve.internal.java.codegen.editorpart.JavaVisualEditor"); //$NON-NLS-1$
							} catch (PartInitException e) {
								JavaVEPlugin.log(e);
							}
						}
					});
				}
			}
		}
	}
	/* (non-Javadoc)
	 * @see org.eclipse.core.runtime.IExecutableExtension#setInitializationData(org.eclipse.core.runtime.IConfigurationElement, java.lang.String, java.lang.Object)
	 */
	public void setInitializationData(
		IConfigurationElement config,
		String propertyName,
		Object data)
		throws CoreException {
		if(data instanceof String){
			superClassName = (String)data;		
		}
	}

}
