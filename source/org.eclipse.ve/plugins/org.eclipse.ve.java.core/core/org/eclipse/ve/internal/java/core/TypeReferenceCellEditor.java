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
 *  $RCSfile: TypeReferenceCellEditor.java,v $
 *  $Revision: 1.7 $  $Date: 2004-03-26 19:31:52 $ 
 */
package org.eclipse.ve.internal.java.core;

import java.text.MessageFormat;
import java.util.*;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.gef.commands.Command;
import org.eclipse.jface.viewers.DialogCellEditor;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.window.Window;
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

import org.eclipse.ve.internal.jcm.*;

import org.eclipse.ve.internal.java.choosebean.*;

import org.eclipse.ve.internal.propertysheet.INeedData;
import org.eclipse.ve.internal.propertysheet.ISourced;
 
/**
 * Cell editor to show base components (i.e. ones that are members of the BeanSubclassComposition and not members
 * of Methods) that are instances of the java type for this cell editor. Also allows construction of base news ones
 * if one wanted is not on the BeanComposition.
 * @since 1.0.0
 */
public class TypeReferenceCellEditor extends DialogCellEditor implements INeedData , ISourced , IExecutableExtension {

	CCombo combo;
	private EditDomain editDomain;
	private JavaClass javaClass;
	protected BeanComposition beanComposition; // The top level object ( the free form )
	protected List javaObjects; // Store the java objects on the free form that match the search class
	protected List javaObjectLabels;
	protected int selection = -1;
	private String[] items;
	private boolean allowCreation = true; // By default a ... button is there, although this can be overridden with initialiation data	
	
	public TypeReferenceCellEditor(Composite parent){
		super(parent);
	}
	public TypeReferenceCellEditor(JavaHelpers javaType, Composite parent){
		this(parent);
		this.javaClass = (JavaClass) javaType;
		
	}
	
	protected Object openDialogBox(Control cellEditorWindow) {
		ChooseBeanDialog chooseBean = new ChooseBeanDialog(
				cellEditorWindow.getShell(),
				editDomain,
				new IChooseBeanContributor[] {new NamedTypeChooseBeanContributor(javaClass.getQualifiedName(),javaClass.getJavaPackage().getPackageName(), javaClass.getName())},
				-1,
				false);				
		chooseBean.setFilter("*");
		if(chooseBean.open()==Window.OK){
			Object[] results = chooseBean.getResult();
			IJavaObjectInstance newJavaInstance = (IJavaObjectInstance)results[0];
			// Use the JavaContainer policy to get all the right command generated
			JavaContainerPolicy editPolicy = new JavaContainerPolicy(
					JCMFactory.eINSTANCE.getJCMPackage().getBeanComposition_Components(),
					editDomain);
			editPolicy.setContainer(beanComposition);
			Command createCommand = editPolicy.getCreateCommand(newJavaInstance,null);
			editDomain.getCommandStack().execute(createCommand);
			
			return newJavaInstance;
		}
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
			combo.select(javaObjects.indexOf(value));
		}
	}
	public void setSources(Object[] sources, IPropertySource[] propertySources, IPropertyDescriptor[] descriptors){
		// Find the Composition ( the free form surface top level object )
		EObject firstSource = (EObject)sources[0];
		beanComposition = (BeanSubclassComposition) EcoreUtil.getObjectByType(firstSource.eResource().getContents(), JCMPackage.eINSTANCE.getBeanSubclassComposition());		
		// Having got the root object we can now ask it for all of its child objects that match the desired type we are supposed to list
		javaObjects = new ArrayList();
		javaObjectLabels = new ArrayList();
		Iterator components = beanComposition.getMembers().iterator();
		while(components.hasNext()){
			Object component = components.next();
			if ( component instanceof IJavaObjectInstance ) { // We might have non java components, nor are we interested in primitives.	 
				// Test the class
				IJavaInstance javaComponent = (IJavaObjectInstance) component;
				JavaHelpers componentType= javaComponent.getJavaType();
				if ( javaClass.isAssignableFrom(componentType)) {
					javaObjects.add(component);	
					// The label used for the icon is the same one used by the JavaBeans tree view
					ILabelProvider labelProvider = ClassDescriptorDecoratorPolicy.getPolicy(editDomain).getLabelProvider(componentType);
					if ( labelProvider != null ) {
						javaObjectLabels.add(labelProvider.getText(component));		
					} else { 
						// If no label provider exists use the toString of the target VM JavaBean itself
						javaObjectLabels.add(BeanProxyUtilities.getBeanProxy(javaComponent).toBeanString()); 
					}
				}
			}
		}
		// Now we know the children set the items
		items = new String[javaObjectLabels.size()];
		System.arraycopy(javaObjectLabels.toArray(),0,items,0,items.length);
		// The list items is the stuff to get added to the list
		combo.setItems(items);
		
	}
	
	public void setData(Object anObject){
		editDomain = (EditDomain) anObject;
	}
	public void setInitializationData(IConfigurationElement element, String data, Object object){
		// The string passed in determines whether or not the editor creates the ... button allowing new instances 
		// to be created, or whether it just allows selection of existing ones
		// As an example, nextFocusableComponent on JComponent lets you point to another component, but not add one, whereas
		// model on JTable would let you add new models as well as attach to existing ones
		if ( object instanceof String ) {
			// TODO - not yet coded.  Superclass makes this difficult, need to think about it - JRW
			allowCreation = Boolean.getBoolean((String)object);
		}
	}	
	
}
