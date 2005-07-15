package org.eclipse.ve.internal.java.codegen.wizards;
/*******************************************************************************
 * Copyright (c) 2004 Red Hat, Inc. and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Red Hat, Inc. - 1.0 implementation
 *******************************************************************************/

/*
 *  $RCSfile: NewVisualClassCreationWizard.java,v $
 *  $Revision: 1.36 $  $Date: 2005-07-15 23:47:32 $ 
 */

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.logging.Level;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.*;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.formatter.CodeFormatter;
import org.eclipse.jdt.internal.ui.wizards.NewElementWizard;
import org.eclipse.ui.*;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.part.FileEditorInput;

import org.eclipse.jem.internal.proxy.core.ProxyPlugin;

import org.eclipse.ve.internal.java.codegen.core.CodegenMessages;
import org.eclipse.ve.internal.java.codegen.util.CodeGenUtil;
import org.eclipse.ve.internal.java.codegen.util.DefaultClassGenerator;
import org.eclipse.ve.internal.java.core.JavaVEPlugin;
import org.eclipse.ve.internal.java.vce.templates.*;

/**
 * @authors JoeWin, pmuldoon
 */
public class NewVisualClassCreationWizard extends NewElementWizard implements IExecutableExtension{
	
	private NewVisualClassWizardPage fPage;
	private String superClassName = null;
	private IVisualClassCreationSourceContributor contributor = null;
	private String contributorBundleName = null;
	public static final String VISUAL_CLASS_WIZARD_SELECTED_ELEMENT_KEY = "VISUAL_CLASS_WIZARD_SELECTED_ELEMENT_KEY"; //$NON-NLS-1$
	public static String NEWLINE =  System.getProperty("line.separator") ; //$NON-NLS-1$

	public NewVisualClassCreationWizard() {
		super();
		setDefaultPageImageDescriptor(JavaVEPlugin.getWizardTitleImageDescriptor());
        setDialogSettings(JavaVEPlugin.getPlugin().getDialogSettings());		
		setWindowTitle(CodegenMessages.NewVisualClassCreationWizard_title); 
	}
		
	/**
	 * Sets the contributor who will be providing for the source of the input className
	 * 
	 * @param className
	 * @param monitor
	 */
	protected void updateContributor(VisualElementModel elementModel) {
		if (elementModel != null && elementModel.getConfigElement() != null) {
			IConfigurationElement celm = elementModel.getConfigElement();
			if ((celm.getAttribute("contributor") != null || celm.getChildren("contributor").length != 0)) { //$NON-NLS-1$ //$NON-NLS-2$
				try {
					contributor = (IVisualClassCreationSourceContributor) celm.createExecutableExtension("contributor"); //$NON-NLS-1$
					if (contributor != null) {
						contributorBundleName = elementModel.getContributorBundleName();
					}
				} catch (CoreException e) {
					JavaVEPlugin.log(e, Level.FINEST);
				}
			}
		}
	}
	
	private IVisualClassCreationSourceGenerator getGeneratorInstance(URL templateLocation){
		java.util.List jdtClassPath = TemplateUtil.getPluginAndPreReqJarPath(JavaVEPlugin.getPlugin().getBundle().getSymbolicName());
		try {
			jdtClassPath.addAll(TemplateUtil.getPlatformJREPath());
		} catch (TemplatesException  e) {
			JavaVEPlugin.log(e, Level.FINE);
		}
		List contributorsPaths = TemplateUtil.getPluginAndPreReqJarPath(contributorBundleName); 
		for(int i=0;i<contributorsPaths.size();i++)
			if(!jdtClassPath.contains(contributorsPaths.get(i)))
				jdtClassPath.add(contributorsPaths.get(i));
		String[] jdtClassPaths = new String[jdtClassPath.size()];
		for(int i=0;i<jdtClassPath.size();i++)
			jdtClassPaths[i] = (String)jdtClassPath.get(i);
		String templatePath = null;
		try {
			templatePath = Platform.asLocalURL(templateLocation).getPath();
			templatePath = templatePath.substring(0,templatePath.lastIndexOf('/'));
		} catch (IOException e1) {
			JavaVEPlugin.log(e1, Level.WARNING);
		}
		String[] templatePaths = {  templatePath };
		try {
			return (IVisualClassCreationSourceGenerator) TemplateObjectFactory.getClassInstance(jdtClassPaths, templatePaths, templateLocation.getPath().substring(templateLocation.getPath().lastIndexOf('/')+1), contributor.getClass().getClassLoader(), null, null);
		} catch (TemplatesException e) {
			JavaVEPlugin.log(e, Level.WARNING);
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
	
	protected void merge(IMethod to, IMethod from, CodeFormatter formatter, IProgressMonitor monitor){
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
			
			StringTokenizer toNLs = new StringTokenizer(toCUSource.substring(toBodyStart, toBodyEnd), "\r\n", false); //$NON-NLS-1$
			StringTokenizer fromNLs = new StringTokenizer(fromCUSource.substring(fromBodyStart, fromBodyEnd), "\r\n", false); //$NON-NLS-1$
			List toLines = new ArrayList();
			List fromLines = new ArrayList();
			int bestToLineIndex = 0;
			while(toNLs.hasMoreTokens()){
				String token = toNLs.nextToken();
				toLines.add(token);	
				//	We shouldnt be adding lines after the 'return something;' statement.
				// As it would result in errors.
				if(token.indexOf("return")>-1)  //$NON-NLS-1$
					break;
				bestToLineIndex++;
			}
			while(fromNLs.hasMoreTokens())
				fromLines.add(fromNLs.nextToken());
			for(int fc=0;fc<fromLines.size();fc++){
				String fromLine = (String)fromLines.get(fc);
				String fromLineNoSpaces = removeWhiteSpaces(fromLine);
				boolean foundInToLines = false;
				boolean fromLineIsSuperConstructorCall = from.isConstructor() && fromLineNoSpaces.indexOf("super(")>-1;  //$NON-NLS-1$
				for(int tc=0;tc<toLines.size();tc++){
					String toLine = (String) toLines.get(tc);
					String toLineNoSpaces = removeWhiteSpaces(toLine);
					boolean toLineIsSuperConstructorCall = to.isConstructor() && toLineNoSpaces.indexOf("super(")>-1; //$NON-NLS-1$
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
//			if(formatter!=null)
//				source = formatter.format(CodeFormatter.K_COMPILATION_UNIT,source,0, source.length(), 1, NEWLINE).toString();
			declaringType.createMethod(NEWLINE+source+NEWLINE, sibling, true, monitor);
		} catch (JavaModelException e) {
			JavaVEPlugin.log(e, Level.FINE);
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
			JavaVEPlugin.log(e, Level.FINE);
		}
		return null;
	}
	
	protected void merge(ICompilationUnit to, ICompilationUnit from, CodeFormatter formatter, IProgressMonitor monitor){
		try {
			// Merge imports
			IImportDeclaration[] fromImports = from.getImports();
			for (int i = 0; fromImports!=null && i < fromImports.length; i++) {
				IImportContainer importContainer = to.getImportContainer();
				if(importContainer==null || !importContainer.exists() || !importContainer.hasChildren()){
					// there are no imports hence put it at the top
					to.createImport(fromImports[i].getElementName(), null, monitor);
				}else{
					// we need to put new imports before existing imports - 
					// formatter at the end will take care of cleaning up ordering etc.
					IJavaElement[] javaElements = importContainer.getChildren();
					to.createImport(fromImports[i].getElementName(), javaElements[0], monitor);
				}
			}
			
			// Merge types
			IType toType = CodeGenUtil.getMainType(to);
			IType fromType = CodeGenUtil.getMainType(from);
			merge(toType, fromType, formatter, monitor);
		} catch (JavaModelException e) {
			JavaVEPlugin.log(e, Level.FINE);
		}
	}

	protected void merge(IType toType, IType fromType, CodeFormatter formatter, IProgressMonitor monitor) throws JavaModelException {
		for(int i=0;i<fromType.getChildren().length;i++){
			IJavaElement child = fromType.getChildren()[i];
			if (child instanceof IField) {
				IField field = (IField) child;
				String source = getCompleteSource(field.getCompilationUnit(), field);
				if(formatter!=null){
//						TextEdit te = formatter.format(CodeFormatter.K_CLASS_BODY_DECLARATIONS,source,0, source.length(), 1, NEWLINE);
//						source = te.toString();
				}
				toType.createField(source, null, true, monitor);
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
//						if(formatter!=null)
//							source = formatter.format(CodeFormatter.K_COMPILATION_UNIT,source,0, source.length(), 1, NEWLINE).toString();
					toType.createMethod(source, null, true, monitor);
				}
			}
			if (child instanceof IType) {
				IType type = (IType) child;
				String source = getCompleteSource(type.getCompilationUnit(), type);
//					if(formatter!=null)
//						source = formatter.format(CodeFormatter.K_COMPILATION_UNIT,source,0, source.length(), 1, NEWLINE).toString();
				toType.createType(source, null, true, monitor);
			}
		}
	}
	
	protected void applyContributor(IType type1, String superClassName, IProgressMonitor monitor){
		IVisualClassCreationSourceGenerator gen = getGeneratorInstance(contributor.getTemplateLocation());
		try {
			ICompilationUnit originalCU = type1.getCompilationUnit(); 
			
			HashMap argMatrix = fPage.getArgumentMatrix();
			// send the target type as template should generate correct names (104006)
			if(argMatrix!=null)
				argMatrix.put(IVisualClassCreationSourceGenerator.TARGET_TYPE, type1);  
			String src = gen.generateSource(CodeGenUtil.getMainType(originalCU).getElementName(), superClassName, argMatrix);
			ICompilationUnit workingCopy = originalCU.getWorkingCopy(null) ;
			workingCopy.getBuffer().setContents(src);
			workingCopy.reconcile(ICompilationUnit.NO_AST, true, null, new NullProgressMonitor());
			
			CodeFormatter formatter = contributor.needsFormatting()?ToolFactory.createCodeFormatter(null):null;
			merge(originalCU, workingCopy, formatter, monitor);
			workingCopy.discardWorkingCopy();
			
			// Format the source 
			String content = originalCU.getSource();
			content = DefaultClassGenerator.format(content, CodeFormatter.K_COMPILATION_UNIT, originalCU.getJavaProject().getOptions(true), System.getProperties().getProperty("line.separator")); //$NON-NLS-1$
			originalCU.getBuffer().setContents(content);
			originalCU.getBuffer().save(monitor, true);
			
		} catch (JavaModelException e) {
			JavaVEPlugin.log(e, Level.FINE);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jdt.internal.ui.wizards.NewElementWizard#finishPage(org.eclipse.core.runtime.IProgressMonitor)
	 */
	protected void finishPage(IProgressMonitor monitor) throws InterruptedException, CoreException {
		// Store the selected VisualElementModel if one is selected.
		if (fPage.getSelectedElement() != null) {
			JavaVEPlugin.getPlugin().getPluginPreferences().setValue(VISUAL_CLASS_WIZARD_SELECTED_ELEMENT_KEY, getSelectedElementStringValue(fPage.getSelectedElement()));
		} else {
			JavaVEPlugin.getPlugin().getPluginPreferences().setValue(VISUAL_CLASS_WIZARD_SELECTED_ELEMENT_KEY, "");			 //$NON-NLS-1$
		}
		monitor.beginTask("",300); //$NON-NLS-1$
		fPage.createType(new SubProgressMonitor(monitor, 100)); // use the full progress monitor
		// Check for a class path container or plugin id this extension needs in this source folder
		if (fPage.getSelectedElement() != null && (fPage.getSelectedElement().getContainer() != null || fPage.getSelectedElement().getPluginId() != null)) {
			verifyProjectClassPath(fPage.getSelectedElement(), new SubProgressMonitor(monitor, 100));
		} else 
			monitor.worked(100);
		updateContributor(fPage.getSelectedElement());
		if(contributor!=null){
			applyContributor(fPage.getCreatedType(), fPage.getSuperClass(), new SubProgressMonitor(monitor, 100));
		} else 
			monitor.worked(100);
		monitor.done();
	}
	/**
	 * Determine if the classpath container or plugin id for this style extension needs
	 * to be in the source folder for this class.
	 * 
	 * @param elementModel
	 * @param monitor
	 * 
	 * @since 1.0.0
	 */
	protected void verifyProjectClassPath(VisualElementModel elementModel, IProgressMonitor monitor) {
		monitor.beginTask("", 100); //$NON-NLS-1$
		try {
			if (elementModel == null)
				return;
			String pluginId = elementModel.getPluginId();
			String container = elementModel.getContainer();
			IJavaProject project = fPage.getPackageFragment().getJavaProject();
			updateProjectClassPath(pluginId, container, project, monitor);
		} finally {
			monitor.done();
		}
	}
	
	public static void updateProjectClassPath(String pluginId, String container, IJavaProject project, IProgressMonitor monitor) {
		if (project != null) {
			try {
				if (!ProxyPlugin.isPDEProject(project)) { // Don't do anything if this is a plugin project
					Map containers = new HashMap(), plugins = new HashMap();
					ProxyPlugin.getPlugin().getIDsFound(project, containers, new HashMap(), plugins, new HashMap());
					if (!((container != null && containers.get(container) == Boolean.TRUE))) {
						if (container != null && plugins.isEmpty()) {
							// TODO If we are a plugin project, we should add the plugin... not the container.
							// For now just add the container to the project so the class will compile correctly
							IClasspathEntry[] cp = project.getRawClasspath();
							IClasspathEntry[] newcp = new IClasspathEntry[cp.length + 1];
							System.arraycopy(cp, 0, newcp, 0, cp.length);
							newcp[cp.length] = JavaCore.newContainerEntry(new Path(container));
							project.setRawClasspath(newcp, new SubProgressMonitor(monitor, 100));
						}
					}
				}
			} catch (JavaModelException e) {
			} catch (CoreException e) {
			}
		}
	}
	
	public void addPages() {
		fPage = new NewVisualClassWizardPage();
		addPage(fPage);
		fPage.init(getSelection());
		if(superClassName != null)
			fPage.setSuperClass(superClassName);
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
								String editorID = "org.eclipse.ve.internal.java.codegen.editorpart.JavaVisualEditor"; //$NON-NLS-1$
								activePage.openEditor(new FileEditorInput((IFile) resource), editorID); 
								IDE.setDefaultEditor((IFile)resource, editorID);
							} catch (PartInitException e) {
								JavaVEPlugin.log(e);
							}
						}
					});
				}
			}
		}
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.IExecutableExtension#setInitializationData(org.eclipse.core.runtime.IConfigurationElement, java.lang.String,
	 *      java.lang.Object)
	 */
	public void setInitializationData(IConfigurationElement config, String propertyName, Object data) throws CoreException {
		if (data instanceof String) {
			superClassName = (String) data;
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.IWizard#performFinish()
	 */
	public boolean performFinish() {
		boolean res =super.performFinish();
		if(res){
			ICompilationUnit cu= fPage.getCreatedType().getCompilationUnit();
			if (cu.isWorkingCopy())
			    cu = (ICompilationUnit) cu.getPrimaryElement();
				//pmuldoon: removed deprecated call with getPrimaryElement
				//pmuldoon: old was: cu = (ICompilationUnit) cu.getOriginal(cu);
			if (cu != null) {
				IResource resource= cu.getResource();
				selectAndReveal(resource);
				openResource(resource);
			}
		}
		return res;
	}
	protected String getSelectedElementStringValue (VisualElementModel vem) {
		if (vem != null) {
			return vem.getCategory() + "-" + vem.getName() + "-" + fPage.getSuperClass();  //$NON-NLS-1$ //$NON-NLS-2$
		}
		return ""; //$NON-NLS-1$
	}

	public IJavaElement getCreatedElement() {
		return fPage.getCreatedType();
	}
}
