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
 *  $Revision: 1.3 $  $Date: 2004-03-15 19:11:50 $ 
 */
package org.eclipse.ve.internal.java.core;

import java.text.MessageFormat;
import java.util.*;
import java.util.List;

import org.eclipse.core.runtime.*;
import org.eclipse.emf.ecore.*;
import org.eclipse.emf.ecore.util.*;
import org.eclipse.gef.commands.Command;
import org.eclipse.jface.viewers.*;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.views.properties.*;
import org.eclipse.jem.internal.beaninfo.adapters.*;
import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.jem.java.JavaClass;

import org.eclipse.ve.internal.cdm.AnnotationEMF;
import org.eclipse.ve.internal.cdm.CDMFactory;

import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cde.emf.ClassDescriptorDecoratorPolicy;
import org.eclipse.ve.internal.cde.rules.IRuleRegistry;

import org.eclipse.ve.internal.jcm.*;

import org.eclipse.ve.internal.java.choosebean.*;
import org.eclipse.ve.internal.java.choosebean.ChooseBeanDialog;
import org.eclipse.ve.internal.java.choosebean.IChooseBeanContributor;
import org.eclipse.ve.internal.java.rules.IChildRule;
import org.eclipse.ve.internal.java.rules.RuledCommandBuilder;
import org.eclipse.ve.internal.java.vce.rules.*;
import org.eclipse.ve.internal.java.vce.rules.VCEChildRule;
import org.eclipse.ve.internal.java.vce.rules.VCEPreSetCommand;

import org.eclipse.ve.internal.propertysheet.*;
 
/**
 * 
 * @since 1.0.0
 */
public class TypeReferenceCellEditor extends DialogCellEditor implements INeedData , ISourced , IExecutableExtension {

	CCombo combo;
	private EditDomain editDomain;
	private String className;
	protected BeanSubclassComposition beanComposition; // The top level object ( the free form )
	protected List javaObjects; // Store the java objects on the free form that match the search class
	protected List javaObjectLabels;
	protected int selection = -1;
	private String[] items;	
	
	public TypeReferenceCellEditor(Composite parent){
		super(parent);
	}
	
	protected Object openDialogBox(Control cellEditorWindow) {
		// TODO Auto-generated method stub
		ChooseBeanDialog chooseBean = new ChooseBeanDialog(
				cellEditorWindow.getShell(),
				editDomain,
				new IChooseBeanContributor[] {new NamedTypeChooseBeanContributor("javax.swing.Action","javax.swing", "Action")},
				-1,
				false);				

		if(chooseBean.open()==Window.OK){
			Object[] results = chooseBean.getResult();
			IJavaObjectInstance newJavaInstance = (IJavaObjectInstance)results[0];
			// Add this to the BeanSubclassComposition
			Command addToBeanCompositionCmd = ((VCEChildRule)editDomain.getRuleRegistry().getRule(IChildRule.RULE_ID)).preCreateChild(
				editDomain,
				beanComposition,
				newJavaInstance,
				JCMFactory.eINSTANCE.getJCMPackage().getBeanComposition_Components());
			addToBeanCompositionCmd.execute();
			// Create an annotation for it right now so it gets onto the free form
			AnnotationEMF emfAnnotation = CDMFactory.eINSTANCE.createAnnotationEMF();
			emfAnnotation.setAnnotates(newJavaInstance);
			beanComposition.getAnnotations().add(emfAnnotation);
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
		// To do comparisons that allow for inheritance we need to find the EMF JavaClass that represents the class we are searching for
		JavaClass javaClass = (JavaClass) Utilities.getJavaClass(className,firstSource.eResource().getResourceSet());
		while(components.hasNext()){
			Object component = components.next();
			if ( component instanceof IJavaObjectInstance ) { // We might have non java components 
				// Test the class
				IJavaObjectInstance javaComponent = (IJavaObjectInstance) component;
				JavaClass componentClass = (JavaClass) javaComponent.getJavaType();
				if ( javaClass.isAssignableFrom(componentClass)) {
					javaObjects.add(component);	
					// The label used for the icon is the same one used by the JavaBeans tree view
					ILabelProvider labelProvider = ClassDescriptorDecoratorPolicy.getPolicy(editDomain).getLabelProvider(componentClass);
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
		// Use this to set the class name passed in
		if ( object instanceof String ) {
			className = (String)object;
		}
	}	
	
}
