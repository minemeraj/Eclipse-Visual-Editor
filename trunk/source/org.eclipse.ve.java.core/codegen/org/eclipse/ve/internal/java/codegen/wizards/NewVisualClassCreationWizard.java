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
 *  $Revision: 1.41 $  $Date: 2006-02-15 16:11:47 $ 
 */

import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.*;
import java.util.logging.Level;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;
import org.eclipse.emf.common.util.URI;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.formatter.CodeFormatter;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.*;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.wizards.newresource.BasicNewResourceWizard;

import org.eclipse.jem.internal.proxy.core.ProxyPlugin;
import org.eclipse.jem.internal.proxy.core.IConfigurationContributionInfo.ContainerPaths;
import org.eclipse.jem.internal.proxy.core.ProxyPlugin.FoundIDs;

import org.eclipse.ve.internal.java.codegen.core.CodegenMessages;
import org.eclipse.ve.internal.java.codegen.util.CodeGenUtil;
import org.eclipse.ve.internal.java.codegen.util.DefaultClassGenerator;
import org.eclipse.ve.internal.java.core.JavaVEPlugin;
import org.eclipse.ve.internal.java.vce.templates.*;

import com.ibm.icu.util.StringTokenizer;

/**
 * @authors JoeWin, pmuldoon
 */
public class NewVisualClassCreationWizard extends Wizard implements INewWizard, IExecutableExtension{
	private IWorkbench fWorkbench;
	private IStructuredSelection fSelection;
	
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
		Set jdtClassPath = new LinkedHashSet(TemplateUtil.getPluginAndPreReqJarPath(JavaVEPlugin.getPlugin().getBundle().getSymbolicName()));
		try {
			jdtClassPath.addAll(TemplateUtil.getPlatformJREPath());
		} catch (TemplatesException  e) {
			JavaVEPlugin.log(e, Level.FINE);
		}
		jdtClassPath.addAll(TemplateUtil.getPluginAndPreReqJarPath(contributorBundleName)); 
		String[] jdtClassPaths = (String[]) jdtClassPath.toArray(new String[jdtClassPath.size()]);
		URI templateURI = URI.createURI(templateLocation.toString());
		String templateFile = templateURI.lastSegment();
		String templatePath = templateURI.trimSegments(1).toString();
		String[] templatePaths = {  templatePath };
		try {
			return (IVisualClassCreationSourceGenerator) TemplateObjectFactory.getClassInstance(jdtClassPaths, templatePaths, templateFile, contributor.getClass().getClassLoader(), null, null);
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
		// Check for a class path container or plugin id this extension needs in this source folder
		if (fPage.getSelectedElement() != null && (fPage.getSelectedElement().getContainer() != null || fPage.getSelectedElement().getPluginId() != null)) {
			verifyProjectClassPath(fPage.getSelectedElement(), new SubProgressMonitor(monitor, 100));
		} else 
			monitor.worked(100);

		fPage.createType(new SubProgressMonitor(monitor, 100)); // use the full progress monitor
		
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
	
	public static void updateProjectClassPath(String pluginId, String containerId, IJavaProject project, IProgressMonitor monitor) {
		if (project != null) {
			try {
				if (!ProxyPlugin.isPDEProject(project)) { // Don't do anything if this is a plugin project
					FoundIDs foundIds = ProxyPlugin.getPlugin().getIDsFound(project);
					// We only test container id (i.e. the first segment of the path. Since we can't add any
					// customized container path we can't test if a customized one is here.
					// We would need to use a new library wizard to allow customization, but even
					// then we would have problems because we couldn't tell the wizard what customization
					// we were interested in.
					// 
					// For example, SWT_CONTAINER. We have a customization for JFACE, but we don't know
					// how to create one because the format is "/SWT_CONTAINER/something/JFACE" We don't
					// know what to put in for "something".
					//
					// So we just check if we have any visible container paths for this id.
					ContainerPaths cntrpaths = (ContainerPaths) foundIds.containerIds.get(containerId);
					if (cntrpaths == null || cntrpaths.getVisibleContainerPaths().length == 0) {
						// TODO If we are a plugin project, we should add the plugin... not the container.
						// For now just add the container to the project so the class will compile correctly
						IClasspathEntry[] cp = project.getRawClasspath();
						IClasspathEntry[] newcp = new IClasspathEntry[cp.length + 1];
						System.arraycopy(cp, 0, newcp, 0, cp.length);
						newcp[cp.length] = JavaCore.newContainerEntry(new Path(containerId));
						project.setRawClasspath(newcp, new SubProgressMonitor(monitor, 100));
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
		boolean result = true;
		final IWorkspaceRunnable op = new IWorkspaceRunnable() {
			public void run(IProgressMonitor monitor) throws CoreException, OperationCanceledException {
				try {
					finishPage(monitor);
				} catch (InterruptedException e) {
					throw new OperationCanceledException(e.getMessage());
				}
			}
		};

		try {
			IRunnableWithProgress runnable = new IRunnableWithProgress() {
				public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
					try {
						JavaCore.run(op, monitor);
					} catch (CoreException e) {
						throw new InvocationTargetException(e);
					}
				}
			};
			getContainer().run(true, true, runnable);
		} catch (InvocationTargetException e) {
			JavaVEPlugin.log(e.getCause());
			result = false;
		} catch (InterruptedException e) {
			result = false;
		}
		if (result) {
			ICompilationUnit cu = fPage.getCreatedType().getCompilationUnit();
			if (cu.isWorkingCopy())
				cu = (ICompilationUnit) cu.getPrimaryElement();
			// pmuldoon: removed deprecated call with getPrimaryElement
			// pmuldoon: old was: cu = (ICompilationUnit) cu.getOriginal(cu);
			if (cu != null) {
				IResource resource = cu.getResource();
				selectAndReveal(resource);
				openResource(resource);
			}
		}
		return result;
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

	public void init(IWorkbench workbench, IStructuredSelection currentSelection) {
		fWorkbench= workbench;
		fSelection= currentSelection;
	}
	
	public IStructuredSelection getSelection() {
		return fSelection;
	}

	public IWorkbench getWorkbench() {
		return fWorkbench;
	}

	protected void selectAndReveal(IResource newResource) {
		BasicNewResourceWizard.selectAndReveal(newResource, fWorkbench.getActiveWorkbenchWindow());
	}
}
