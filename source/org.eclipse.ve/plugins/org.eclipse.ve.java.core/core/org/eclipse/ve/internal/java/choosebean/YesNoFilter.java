/*******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.java.choosebean;

import java.util.*;
import java.util.logging.Level;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.internal.corext.util.TypeInfo;
import org.eclipse.jface.viewers.IFilter;

import org.eclipse.ve.internal.java.core.JavaVEPlugin;


public class YesNoFilter implements IFilter {

	private String[] yesTypes = null;
	private String[] noTypes = null;
	private List yesTypeFQNs = null;
	private List noTypeFQNs = null;
	private IPackageFragment pkg = null;
	private IProgressMonitor monitor = null;
	
	public YesNoFilter(String[] yesTypes, String[] noTypes, IPackageFragment pkg, IProgressMonitor monitor){
		this.yesTypes = yesTypes;
		this.noTypes = noTypes;
		this.pkg = pkg;
		this.monitor = monitor;
		int num = (yesTypes==null ? 0 : yesTypes.length/2) + (noTypes==null ? 0 : noTypes.length/2);
		getMonitor().beginTask(ChooseBeanMessages.YesNoFilter_DependenciesMonitor_Message, num); 
		getYesTypeFQNs();
		getNoTypeFQNs();
		getMonitor().worked(num);
	}
	
	public boolean select(Object toTest) {
		TypeInfo type = (TypeInfo) toTest;
		String fqn = type.getFullyQualifiedName();
		if(		!getNoTypeFQNs().contains(fqn) && 
				getYesTypeFQNs().contains(fqn))
			return true;
		return false;
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
		for (int c = 0; c < types.length; c+=2) {
			try {
				IType baseClass = getJavaProject().findType(types[c], types[c+1]);
				getMonitor().subTask(baseClass.getFullyQualifiedName());
				getMonitor().worked(1);
				if(baseClass!=null){
					ITypeHierarchy th = baseClass.newTypeHierarchy(getJavaProject(), new NullProgressMonitor());
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
		return subTypesList;
	}
	
	protected IJavaProject getJavaProject() {
		return pkg.getJavaProject();
	}
	
	protected IProgressMonitor getMonitor(){
		if(monitor==null)
			monitor = new NullProgressMonitor();
		return monitor;
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

	
	public void setMonitor(IProgressMonitor monitor) {
		this.monitor = monitor;
	}
}
