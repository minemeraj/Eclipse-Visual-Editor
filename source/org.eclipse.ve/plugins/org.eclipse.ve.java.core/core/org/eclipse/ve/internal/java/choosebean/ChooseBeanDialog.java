/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: ChooseBeanDialog.java,v $
 *  $Revision: 1.45 $  $Date: 2005-12-02 20:22:22 $ 
 */
package org.eclipse.ve.internal.java.choosebean;

import java.util.logging.Level;

import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.search.IJavaSearchScope;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.ui.IJavaElementSearchConstants;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.operation.IRunnableContext;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.dialogs.SelectionDialog;
import org.eclipse.ui.part.FileEditorInput;

import org.eclipse.jem.internal.beaninfo.core.Utilities;
import org.eclipse.jem.internal.instantiation.InstantiationFactory;
import org.eclipse.jem.internal.instantiation.PTAnonymousClassDeclaration;
import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.jem.java.JavaClass;

import org.eclipse.ve.internal.cde.core.CDEUtilities;
import org.eclipse.ve.internal.cde.core.EditDomain;

import org.eclipse.ve.internal.java.core.*;
 
/**
 * Class to take advantage of the extendable JDT Type selection dialog
 * 
 * @since 1.2.0
 */
public class ChooseBeanDialog {

	/**
	 * 
	 * @param editDomain
	 * @param contributors
	 * @param initialText
	 * @return
	 * 
	 * @since 1.2.0
	 */
	public static Object[] getChooseBeanResults(EditDomain editDomain, IChooseBeanContributor[] contributors, String initialText){
		Object[] results = null;
		try {
			// search scope
			ICompilationUnit compilationUnit = (ICompilationUnit) JavaCore.create(((FileEditorInput)editDomain.getEditorPart().getEditorInput()).getFile());
			if (compilationUnit.getParent() instanceof IPackageFragment) {
				IPackageFragment packageFragment = (IPackageFragment) compilationUnit.getParent();
				IJavaProject javaProject = packageFragment.getJavaProject();
				IJavaSearchScope searchScope = SearchEngine.createJavaSearchScope(new IJavaElement[]{javaProject});
				
				// context
				IRunnableContext context = editDomain.getEditorPart().getEditorSite().getWorkbenchWindow();
				
				// extension
				ChooseBeanTypeSelectionExtension extension = new ChooseBeanTypeSelectionExtension(contributors, packageFragment, editDomain, searchScope);
				
				SelectionDialog dialog = JavaUI.createTypeDialog(
						editDomain.getEditorPart().getSite().getShell(), 
						context, 
						searchScope, 
						IJavaElementSearchConstants.CONSIDER_CLASSES_AND_INTERFACES, 
						false, 
						initialText,
						extension);
				dialog.setTitle(ChooseBeanMessages.MainDialog_title);
				dialog.setMessage(ChooseBeanMessages.MainDialog_message);
				if(dialog.open()==Window.OK){
					return getEMFObjectAndType(dialog.getResult(), editDomain, extension.getBeanName());
				}
			}
		} catch (JavaModelException e) {
			JavaVEPlugin.log(e, Level.WARNING);
		}
		return results;
	}

	private static Object[] getEMFObjectAndType(Object[] results, EditDomain editDomain, String beanName) {
		ResourceSet resourceSet = JavaEditDomainHelper.getResourceSet(editDomain);
		if(resourceSet!=null){
			Object[] newResults = new Object[results.length*2];
			for(int i=0;i<results.length;i++){
				if (results[i] instanceof IType) {
					IType type = (IType) results[i];
					String realFQN = type.getFullyQualifiedName('$');
					JavaClass javaClass = Utilities.getJavaClass(realFQN, resourceSet);					
					IJavaInstance javaInstance = (IJavaInstance) javaClass.getEPackage().getEFactoryInstance().create(javaClass);
					if (javaClass.isInterface() || javaClass.isAbstract()) {
						ICompilationUnit icu = JavaCore.createCompilationUnitFrom(((IFileEditorInput) editDomain.getEditorPart().getEditorInput()).getFile()); 
						PTAnonymousClassDeclaration anon = ASTMethodUtil.createAnonymousDeclaration(javaClass, icu);
						if (anon != null) {
							// Create an anonymous allocation.
							javaInstance.setAllocation(InstantiationFactory.eINSTANCE.createParseTreeAllocation(anon));
						}
					}
					if(beanName==null || beanName.trim().length()<1){
						beanName = getDefaultBeanName(((IJavaObjectInstance) javaInstance).getJavaType().getJavaName());
					}
					ChooseBeanDialogUtilities.setBeanName(javaInstance, beanName, editDomain);
					newResults[(i*2)] = javaInstance;
					newResults[(i*2)+1] = javaClass;
				}
			}
			return newResults;
		}else{
			return results;
		}
	}
	
	protected static String getDefaultBeanName(String qualTypeName) {
		String defaultName = qualTypeName;
		if (defaultName.indexOf('.') > 0)
			defaultName = defaultName.substring(defaultName.lastIndexOf('.') + 1);
		defaultName = CDEUtilities.lowCaseFirstCharacter(defaultName);
		return defaultName;
	}

}
