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
/*
 *  $RCSfile: TypeReferenceCellEditor.java,v $
 *  $Revision: 1.20 $  $Date: 2005-12-02 16:31:22 $ 
 */
package org.eclipse.ve.internal.java.core;

import java.text.MessageFormat;
import java.util.*;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.DialogCellEditor;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;

import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.jem.java.JavaClass;
import org.eclipse.jem.java.JavaHelpers;

import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cde.emf.ClassDescriptorDecoratorPolicy;
import org.eclipse.ve.internal.cde.emf.InverseMaintenanceAdapter;

import org.eclipse.ve.internal.jcm.*;

import org.eclipse.ve.internal.java.choosebean.*;

import org.eclipse.ve.internal.propertysheet.INeedData;
import org.eclipse.ve.internal.propertysheet.ISourced;
 
/**
 * Cell editor to show base components (i.e. ones that are members of the BeanSubclassComposition and not members
 * of Methods) that are instances of the java type for this cell editor.
 * @since 1.0.0
 */
public class TypeReferenceCellEditor extends DialogCellEditor implements INeedData, ISourced {

	CCombo combo;
	private EditDomain editDomain;
	private JavaClass javaClass;
	protected List javaObjects; // Store the java objects on the free form or local to the method of the source that match the search class
	protected int selection = -1;
	private String[] items;
	
	public TypeReferenceCellEditor(Composite parent){
		super(parent);
	}
	public TypeReferenceCellEditor(JavaHelpers javaType, Composite parent){
		this(parent);
		this.javaClass = (JavaClass) javaType;
		
	}
	
	protected Object openDialogBox(Control cellEditorWindow) {
		Object[] results = ChooseBeanDialog.getChooseBeanResults(
				editDomain,
				new IChooseBeanContributor[] {new NamedTypeChooseBeanContributor(javaClass.getQualifiedName(),javaClass.getJavaPackage().getPackageName(), javaClass.getName())},
				"**");
		if(results!=null && results.length>0)
			return (IJavaObjectInstance)results[0];
		return null;
	}
	
	protected Control createContents(Composite cell) {
		combo = new CCombo(cell,SWT.NONE);
		combo.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				int newSelection = combo.getSelectionIndex();
				if (newSelection == selection)
					return;
				selection = newSelection;
				Object newValue = javaObjects.get(selection);
				boolean oldValidState = isValueValid();
				boolean newValidState = isCorrect(newValue);
				
				if (!newValidState) {
					// try to insert the current value into the error message.
					setErrorMessage(
						MessageFormat.format(getErrorMessage(), new Object[] {items[selection]}));
				}
				valueChanged(oldValidState, newValidState, newValue);
				fireApplyEditorValue();
			}
		});
		return combo;
	}
	public boolean isActivated() {
		return combo.isVisible();
	}
	
	protected void valueChanged(boolean oldValidState, boolean newValidState, Object newValue) {
		super.valueChanged(oldValidState, newValidState);
		if (newValidState)
			doSetValue(newValue);
	}	
		
	protected void updateContents(Object value) {
		if(combo != null && javaObjects != null){
			combo.select(selection = javaObjects.indexOf(value));
		}
	}

	
	public void setData(Object anObject){
		editDomain = (EditDomain) anObject;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.propertysheet.ISourced#setSources(java.lang.Object[], org.eclipse.ui.views.properties.IPropertySource[], org.eclipse.ui.views.properties.IPropertyDescriptor[])
	 */
	public void setSources(Object[] sources, IPropertySource[] propertySources, IPropertyDescriptor[] descriptors) {
		BeanComposition beanComposition = (BeanComposition) editDomain.getDiagramData();		
		// Having got the root object we can now ask it for all of its child objects that match the desired type we are supposed to list
		SortedMap javaObjectLabels = new TreeMap();
		Iterator components = beanComposition.getMembers().iterator();
		while(components.hasNext()){
			addComponentToList(javaObjectLabels, components.next());
		}
		
		// Now we also need to walk through the one member container that is common to all of the sources.
		// Some of the sources may be global with an initialize method. That is the member we must be in.
		// If not global, then we need to see what method it is declared in. That will be the member. All of the sources must share this
		// same member or we can't add them in. That is because if they came from different memberContainers we wouldn't be able to apply
		// the same value to all of the sources because they couldn't cross membership for locals.
		MemberContainer member = null;
		for (int i = 0; i < sources.length; i++) {
			Object source = sources[i];
			if (source instanceof IJavaInstance) {
				IJavaInstance javasource = (IJavaInstance) source;
				JCMMethod initMethod = (JCMMethod) InverseMaintenanceAdapter.getFirstReferencedBy(javasource, JCMPackage.eINSTANCE.getJCMMethod_Initializes());
				if (initMethod != null)
					if (member == null)
						member = initMethod;
					else {
						member = null;
						break;	// We have a different init method. 
					}
				else {
					EObject container = javasource.eContainer();
					// If contained by BeanComposition and no init method, then it is a global, and already handled globals.
					// OR if by some chance the container is NOT a MemberContainer, then it should be ignored.
					if (!(container instanceof BeanComposition) && container instanceof MemberContainer) {
						if (member != container)
							if (member == null)
								member = (MemberContainer) container;
							else {
								member = null;
								break;	// We have a different member
							}
					}
				}
			}
		}
		
		// Now walk through member and get all of its members. (Properties and implicits at this time are not valid).
		if (member != null) {
			Iterator members = member.getMembers().iterator();
			while(members.hasNext()){
				addComponentToList(javaObjectLabels, members.next());
			}

		}
		
		// Now take the sorted map and produce two arrays, one the labels (which becomes items) and one the real objects.
		javaObjects = new ArrayList(javaObjectLabels.size());
		items = new String[javaObjectLabels.size()];
		for (Iterator itr = javaObjectLabels.entrySet().iterator(); itr.hasNext();) {
			Map.Entry entry = (Map.Entry) itr.next();
			items[javaObjects.size()] = (String) entry.getKey();
			javaObjects.add(entry.getValue());
		}
		combo.setItems(items);
	}
	/**
	 * @param javaObjectLabels
	 * @param component
	 * 
	 * @since 1.2.0
	 */
	private void addComponentToList(SortedMap javaObjectLabels, Object component) {
		if ( component instanceof IJavaObjectInstance ) { // We might have non java components, nor are we interested in primitives.	 
			// Test the class
			IJavaInstance javaComponent = (IJavaInstance) component;
			JavaHelpers componentType= javaComponent.getJavaType();
			if ( javaClass.isAssignableFrom(componentType)) {
				// The label used for the icon is the same one used by the JavaBeans tree view
				ILabelProvider labelProvider = ClassDescriptorDecoratorPolicy.getPolicy(editDomain).getLabelProvider(componentType);
				if ( labelProvider != null ) {
					javaObjectLabels.put(labelProvider.getText(component), component);		
				} else { 
					// If no label provider exists use the toString of the target VM JavaBean itself
					javaObjectLabels.put(BeanProxyUtilities.getBeanProxy(javaComponent).toBeanString(), component); 
				}
			}
		}
	}	
	
}
