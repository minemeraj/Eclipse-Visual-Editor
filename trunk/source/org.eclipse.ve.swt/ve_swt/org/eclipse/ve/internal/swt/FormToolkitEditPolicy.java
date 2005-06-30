/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $$RCSfile: FormToolkitEditPolicy.java,v $$
 *  $$Revision: 1.4 $$  $$Date: 2005-06-30 10:05:48 $$ 
 */

package org.eclipse.ve.internal.swt;

import java.util.*;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ContainerEditPolicy;
import org.eclipse.gef.requests.CreateRequest;

import org.eclipse.jem.internal.instantiation.*;
import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;

import org.eclipse.ve.internal.cde.core.EditDomain;

import org.eclipse.ve.internal.jcm.BeanComposition;
import org.eclipse.ve.internal.jcm.JCMPackage;

import org.eclipse.ve.internal.java.core.BeanUtilities;
import org.eclipse.ve.internal.java.rules.RuledCommandBuilder;

import org.eclipse.ve.internal.propertysheet.common.commands.AbstractCommand;

public class FormToolkitEditPolicy extends ContainerEditPolicy {
	
	
	private EditDomain editDomain;
	private RuledCommandBuilder builder;
	public class EnsureFormToolkitExistsCommand extends AbstractCommand{
		private IJavaObjectInstance javaChild;
		private boolean changedAllocation;
		private IJavaObjectInstance formToolkit;
		public EnsureFormToolkitExistsCommand(IJavaObjectInstance instance) {
			javaChild = instance;
		}
		public void execute() {
			// 1) Make sure there is a FormToolkit object and create one if required
			// 2) Substitute any {formToolkit} PTNames in the allocation with this instance
			if(javaChild.getAllocation() instanceof ParseTreeAllocation){
				editDomain = EditDomain.getEditDomain(getHost());
				PTExpression expression = ((ParseTreeAllocation)javaChild.getAllocation()).getExpression();					
				if(expression instanceof PTMethodInvocation){
					visitMethodInvocation((PTMethodInvocation)expression);
				} 
				if(changedAllocation){
					// ReCreate the allocation feature so that CodeGen will reGenerate the constructor
					ParseTreeAllocation newAlloc = InstantiationFactory.eINSTANCE.createParseTreeAllocation();
					newAlloc.setExpression(expression);								
					javaChild.setAllocation(newAlloc);					
				}					
			}				
		}
		private void visitMethodInvocation(PTMethodInvocation methodInvocation) {
			if(methodInvocation.getReceiver() instanceof PTName){
				PTName name = (PTName) methodInvocation.getReceiver();
				if(name.getName().equals(SwtPlugin.FORM_TOOLKIT_TOKEN)){
					// Get a form tookit instance
					formToolkit = ensureFormToolkitExists();					
					// Change the reference in the parse tree to point to the toolkit
					PTInstanceReference formToolkitRef = InstantiationFactory.eINSTANCE.createPTInstanceReference();
					formToolkitRef.setObject(formToolkit);
					methodInvocation.setReceiver(formToolkitRef);
					changedAllocation = true;
				}
			}
		}		
		
		private IJavaObjectInstance ensureFormToolkitExists() {
			// See if there is an existing form toolkit as a component of the composition
			BeanComposition beanComposition = (BeanComposition) editDomain.getDiagramData();
			// Iterate over the components looking for a form toolkit
			Iterator
			iter = beanComposition.getComponents().iterator();
			while(iter.hasNext()){
				Object component = iter.next();
				if(component instanceof IJavaObjectInstance){
					IJavaObjectInstance javaComponent = (IJavaObjectInstance)component;
					if (javaComponent.getJavaType().getQualifiedName().equals(SwtPlugin.FORM_TOOLKIT_CLASSNAME)){
						return javaComponent;
					}
				}
			}
			// We do not have a form toolkit so create one and add it as a component to the BeanComposition
			// The format for creating a form toolkit is "new FormToolkit(display)";
			List args = new ArrayList(1);
			// Create an expression for "Display.getCurrent()";
			PTMethodInvocation getDisplayExpression = InstantiationFactory.eINSTANCE.createPTMethodInvocation(
					InstantiationFactory.eINSTANCE.createPTName(SwtPlugin.DISPLAY_CLASSNAME),"getCurrent",null);
			args.add(getDisplayExpression);
			PTClassInstanceCreation formToolkitExpression= InstantiationFactory.eINSTANCE.createPTClassInstanceCreation("org.eclipse.ui.forms.widgets.FormToolkit",args);
			ParseTreeAllocation formToolkitAllocation = InstantiationFactory.eINSTANCE.createParseTreeAllocation(formToolkitExpression);
			IJavaObjectInstance newFormToolkit = (IJavaObjectInstance) BeanUtilities.createJavaObject(SwtPlugin.FORM_TOOLKIT_CLASSNAME,((IJavaObjectInstance)getHost().getModel()).eResource().getResourceSet(),formToolkitAllocation);
			// add the form toolkit to the "components" relationship of the BeanComposition
			builder = new RuledCommandBuilder(editDomain);
			builder.applyAttributeSetting(beanComposition,JCMPackage.eINSTANCE.getBeanComposition_Components(),newFormToolkit);
			// Also add a command to be executed that will create a visual info on the annotation that has a keyed value of type "cdm:KeyedBoolean",  
			// a key of "org.eclipse.ve.internal.cdm.model.visualconstraintkey" and value of true
			builder.append(BeanUtilities.getSetEmptyVisualContraintsCommand(newFormToolkit,false,editDomain));
			
			builder.getCommand().execute();
			return newFormToolkit;
		}		
		protected boolean prepare() {
			return true;
		}		
	}	
	
protected Command getCreateCommand(CreateRequest request) {

	final IJavaObjectInstance javaChild = (IJavaObjectInstance)request.getNewObject();	
	if(javaChild.getAllocation() != null){
		return new EnsureFormToolkitExistsCommand(javaChild);
	} else {
		return null;
	}

}

}
