/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: YesNoListChooseBeanContributor.java,v $
 *  $Revision: 1.4 $  $Date: 2004-05-20 21:43:03 $ 
 */
package org.eclipse.ve.internal.java.choosebean;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jdt.core.*;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.FilteredList;
import org.eclipse.ui.dialogs.FilteredList.FilterMatcher;

import org.eclipse.ve.internal.java.core.JavaVEPlugin;
 
/**
 * 
 * @since 1.0.0
 */
public class YesNoListChooseBeanContributor implements IChooseBeanContributor {

	private String name = null;
	private String[] yesTypes = null;
	private String[] noTypes = null;
	private List yesTypeFQNs = null;
	private List noTypeFQNs = null;
	private IJavaProject javaProject = null;
	private FilteredList.FilterMatcher filter = null;
	
	public YesNoListChooseBeanContributor(String name, String[] yesTypes, String[] noTypes){
		this.name = name;
		this.yesTypes = yesTypes;
		this.noTypes = noTypes;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.choosebean.IChooseBeanContributor#getName()
	 */
	public String getName() {
		return name;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.choosebean.IChooseBeanContributor#getFilter(org.eclipse.jdt.core.search.IJavaSearchScope)
	 */
	public FilterMatcher getFilter(IJavaProject javaProject) {
		if(filter==null){
			setJavaProject(javaProject);
			setFilter(createFilterMatcher());
		}
		return filter;
	}
	
	protected FilterMatcher createFilterMatcher(){
		return new TypeFilterMatcher(getYesTypeFQNs(), getNoTypeFQNs());		
	}

	/**
	 * @return Returns the noClasses.
	 */
	public List getNoTypeFQNs() {
		if(noTypeFQNs==null){
			noTypeFQNs = getSubTypes(noTypes);
		}
		return noTypeFQNs;
	}

	/**
	 * @param noClasses The noClasses to set.
	 */
	public void setNoFQNs(List noTypeFQNs) {
		this.noTypeFQNs = noTypeFQNs;
	}

	/*
	 * Expects {"pkg1","class1", "pkg2", "class2"}
	 */
	protected List getSubTypes(final String[] types){
		if (types == null)
			return Collections.EMPTY_LIST;
		final List subTypesList = new ArrayList();
		try {
			PlatformUI.getWorkbench().getProgressService().busyCursorWhile(new IRunnableWithProgress(){
				public void run(IProgressMonitor pm) throws InterruptedException {
					pm.beginTask("Search types", 100*(types.length/2));
					for (int c = 0; c < types.length; c+=2) {
						if (pm.isCanceled())
							throw new InterruptedException();
						try {
							IType baseClass = getJavaProject().findType(types[c], types[c+1]);
							if(baseClass!=null){
								ITypeHierarchy th = baseClass.newTypeHierarchy(getJavaProject(), new SubProgressMonitor(pm, 100));
								IType[] subTypes = null;
								if(baseClass.isClass()){
									subTypes = th.getAllSubtypes(baseClass);
								} else if (baseClass.isInterface()){
									// Get all implementors
									subTypes = th.getAllSubtypes(baseClass);
									for (int stc = 0; stc < subTypes.length; stc++) {
										subTypesList.add(subTypes[stc].getFullyQualifiedName());
									}																
									// Some of these will be hierarchy roots themselves								
									// So collect all the classes that extend them
									for (int i = 0; i < subTypes.length; i++) {
										IType[] implementors = th.getAllSubtypes(subTypes[i]);								
										for (int stc = 0; stc < implementors.length; stc++) {
											subTypesList.add(implementors[stc].getFullyQualifiedName());
										}																	
									}
								}
								subTypesList.add(baseClass.getFullyQualifiedName());
								for (int stc = 0; stc < subTypes.length; stc++) {
									subTypesList.add(subTypes[stc].getFullyQualifiedName());
								}								
							}
						} catch (JavaModelException e) {
							JavaVEPlugin.log(e, Level.WARNING);
						}
					}
				}
			});
		} catch (InvocationTargetException e) {
			JavaVEPlugin.log(e.getCause(), Level.WARNING);
		} catch (InterruptedException e) {
			// Not an error, was canceled.
		}
		return subTypesList;
	}
	
	/**
	 * @return Returns the yesClasses.
	 */
	public List getYesTypeFQNs() {
		if(yesTypeFQNs==null){
			yesTypeFQNs = getSubTypes(yesTypes);
		}
		return yesTypeFQNs;
	}

	/**
	 * @param yesClasses The yesClasses to set.
	 */
	public void setYesTypeFQNs(List yesTypeFQNs) {
		this.yesTypeFQNs = yesTypeFQNs;
	}

	/**
	 * @return Returns the javaProject.
	 */
	public IJavaProject getJavaProject() {
		return javaProject;
	}

	/**
	 * @param javaProject The javaProject to set.
	 */
	public void setJavaProject(IJavaProject javaProject) {
		this.javaProject = javaProject;
	}

	/**
	 * @param filterMatcher The filterMatcher to set.
	 */
	public void setFilter(FilteredList.FilterMatcher filter) {
		this.filter = filter;
	}

}
