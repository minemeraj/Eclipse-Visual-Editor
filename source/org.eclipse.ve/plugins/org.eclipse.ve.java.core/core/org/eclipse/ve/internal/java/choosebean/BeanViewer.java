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

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.logging.Level;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.search.IJavaSearchConstants;
import org.eclipse.jdt.core.search.IJavaSearchScope;
import org.eclipse.jdt.internal.corext.util.TypeInfo;
import org.eclipse.jdt.internal.corext.util.TypeInfoFilter;
import org.eclipse.jdt.internal.ui.dialogs.TypeInfoViewer;
import org.eclipse.jdt.ui.JavaElementImageDescriptor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.PlatformUI;

import org.eclipse.ve.internal.cde.core.EditDomain;

import org.eclipse.ve.internal.java.core.JavaVEPlugin;


public class BeanViewer extends TypeInfoViewer{
	
	public class BeanLabelProvider extends TypeInfoLabelProvider{
		private HashMap imageToDescriptorMap = null;
		public BeanLabelProvider(){
			imageToDescriptorMap = new HashMap();
		}
		public ImageDescriptor getImageDescriptor(Object element) {
			Image image = ChooseBeanDialogUtilities.getContributorImage(selectedContributor);
			if(showOnlyBeans && image==null)
				image = JavaVEPlugin.getJavaBeanImage();
			ImageDescriptor imageDescriptor = null;
			if(image==null){
				imageDescriptor = super.getImageDescriptor(element);
			}else{
				if(imageToDescriptorMap.containsKey(image)){
					imageDescriptor = (ImageDescriptor) imageToDescriptorMap.get(image);
				}else{
					imageDescriptor = ImageDescriptor.createFromImage(image);
					imageToDescriptorMap.put(image, imageDescriptor);
				}
			}
			if (element instanceof TypeInfo && image!=null) {
				TypeInfo ti = (TypeInfo) element;
				if(ti.isInterface())
					imageDescriptor = new JavaElementImageDescriptor(
							imageDescriptor, 
							JavaElementImageDescriptor.OVERRIDES, 
							new Point(image.getBounds().width, image.getBounds().height));
				else if(Flags.isAbstract(ti.getModifiers()))
					imageDescriptor = new JavaElementImageDescriptor(
							imageDescriptor, 
							JavaElementImageDescriptor.ABSTRACT, 
							new Point(image.getBounds().width, image.getBounds().height));
			}
			return imageDescriptor;
		}
	}
	
	public class BeanTypeInfoFilter extends TypeInfoFilter{
		private IFilter customFilter = null;
		public BeanTypeInfoFilter(String text, IJavaSearchScope scope, int elementKind) {
			super(text, scope, elementKind);
		}
		
		public void setCustomFilter(IFilter filter) {
			this.customFilter = filter;
		}

		public boolean matchesCachedResult(TypeInfo type) {
			return super.matchesCachedResult(type) && filter(type);
		}

		public boolean matchesHistoryElement(TypeInfo type) {
			return super.matchesHistoryElement(type) && filter(type);
		}

		// TODO These internal APIs were removed in Eclipse 3.2M3. Need to use other APIs
		// See https://bugs.eclipse.org/bugs/show_bug.cgi?id=115567
		
//		public boolean matchesNameExact(TypeInfo type) {
//			return super.matchesNameExact(type) && filter(type);
//		}
//
//		public boolean matchesSearchResult(TypeInfo type) {
//			return super.matchesSearchResult(type) && filter(type);
//		}

		protected boolean filter(TypeInfo type) {
			boolean valid = true;
			if(customFilter!=null)
				valid = valid && customFilter.select(type);
			if(valid && showOnlyBeans)
				valid = valid && isValidBean(type);
			return valid;
		}

		public boolean isSubFilter(String text) {
			return false;
		}
	}
	
	private IPackageFragment pkg = null;
	private BeanTypeInfoFilter typeInfoFilter = null;
	private HashMap contributorFilterMap = null;
	private String currentText;
	private IChooseBeanContributor selectedContributor = null;
	private boolean showOnlyBeans = false;
	private ResourceSet resourceSet = null;
	private IJavaSearchScope scope = null;
	private EditDomain editDomain;

	private boolean isValidBean(TypeInfo type) {
		IStatus status = ChooseBeanDialogUtilities.getClassStatus(type, pkg.getElementName(), resourceSet, scope, editDomain);
		return status.isOK();
	}

	protected TypeInfoLabelProvider createLabelProvider() {
		return new BeanLabelProvider();
	}
	
	protected TypeInfoFilter createTypeInfoFilter(String text) {
		typeInfoFilter = new BeanTypeInfoFilter(text, fSearchScope, fElementKind);
		typeInfoFilter.setCustomFilter(getCustomFilter(selectedContributor));
		return typeInfoFilter;
	}
	
	public void updateContributor(IChooseBeanContributor contributor){
		this.selectedContributor = contributor;
		if(currentText!=null)
			setSearchPattern(currentText); // refresh table
	}
	
	private HashMap getContributorFilterMap(){
		if(contributorFilterMap==null)
			contributorFilterMap = new HashMap();
		return contributorFilterMap;
	}
	
	private IFilter getCustomFilter(final IChooseBeanContributor contributor){
		IFilter customFilter = null;
		if(getContributorFilterMap().containsKey(contributor))
			customFilter = (IFilter) getContributorFilterMap().get(contributor);
		else{
			final List filterList = new ArrayList(); // just made to get output of getFilter() inside IRunnable
			IRunnableWithProgress runnable = new IRunnableWithProgress(){
				public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
					filterList.add(contributor.getFilter(pkg, monitor));
				}
			};
			try {
				PlatformUI.getWorkbench().getProgressService().run(false, false, runnable);
				//PlatformUI.getWorkbench().getProgressService().busyCursorWhile(runnable);
				if(filterList.size()>0)
					customFilter = (IFilter) filterList.get(0);
			} catch (InvocationTargetException e) {
				JavaVEPlugin.log(e, Level.FINE);
				customFilter = null;
			} catch (InterruptedException e) {
				// not exception - just cancelled
				customFilter = null;
			}
			getContributorFilterMap().put(contributor, customFilter);
		}
		return customFilter;
	}

	public void setSearchPattern(String text) {
		currentText = text;
		super.setSearchPattern(text);
	}
	
	public BeanViewer(Composite parent, Label progressLabel, IJavaSearchScope scope, 
								IPackageFragment pkg, ResourceSet resourceSet, EditDomain editDomain) {
		super(parent, SWT.NONE, progressLabel, scope, IJavaSearchConstants.CLASS_AND_INTERFACE, null);
		this.resourceSet = resourceSet;
		this.scope = scope;
		this.pkg = pkg;
		this.editDomain = editDomain;
	}

	public void showOnlyBeans(boolean showOnlyBeans){
		this.showOnlyBeans = showOnlyBeans;
	}
}
