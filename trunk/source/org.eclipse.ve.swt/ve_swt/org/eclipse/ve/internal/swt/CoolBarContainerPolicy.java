package org.eclipse.ve.internal.swt;
/*******************************************************************************
 * Copyright (c) 2001, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: CoolBarContainerPolicy.java,v $
 *  $Revision: 1.1 $  $Date: 2004-08-20 22:39:14 $ 
 */


import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.gef.commands.Command;
import org.eclipse.jem.internal.instantiation.InstantiationFactory;
import org.eclipse.jem.internal.instantiation.JavaAllocation;
import org.eclipse.jem.internal.instantiation.PTClassInstanceCreation;
import org.eclipse.jem.internal.instantiation.PTFieldAccess;
import org.eclipse.jem.internal.instantiation.PTInstanceReference;
import org.eclipse.jem.internal.instantiation.PTName;
import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.jem.internal.instantiation.base.JavaInstantiation;
import org.eclipse.jem.java.JavaClass;
import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.java.core.JavaEditDomainHelper;
import org.eclipse.ve.internal.java.rules.RuledCommandBuilder;
import org.eclipse.ve.internal.propertysheet.common.commands.CommandWrapper;
/**
 * Container Edit Policy for CoolBar/CoolItems.
 */
public class CoolBarContainerPolicy extends CompositeContainerPolicy {
	private EReference sf_coolbarItems, sf_coolItemControl, sf_compositeControls;
	protected EClass classControl;
	protected EClass classCoolItem;
	protected EFactory visualsFact;
	protected JavaClass classComponent;

	public CoolBarContainerPolicy(EditDomain domain) {
		super(domain);
		ResourceSet rset = JavaEditDomainHelper.getResourceSet(domain);
		sf_coolbarItems = JavaInstantiation.getReference(rset, SWTConstants.SF_COOLBAR_ITEMS);
		sf_compositeControls = JavaInstantiation.getReference(rset, SWTConstants.SF_COMPOSITE_CONTROLS);
		sf_coolItemControl = JavaInstantiation.getReference(rset, SWTConstants.SF_COOLITEM_CONTROL);
		classControl = (EClass) sf_compositeControls.getEType();
		classCoolItem = (EClass) sf_coolbarItems.getEType();
		visualsFact = classCoolItem.getEPackage().getEFactoryInstance();
		
		// Override the containment feature from CompositeContainer
		//containmentSF = JavaInstantiation.getSFeature(JavaEditDomainHelper.getResourceSet(domain), SWTConstants.SF_COOLBAR_ITEMS);
	}
	
	/*
	 * The child in this case is the Control and not the CoolItem. The isValidChild
	 * in this case is called by super classes and they are passed the Control at that
	 * point in time. We will later wrapper it into a TabItem after it has gone through
	 * this test.
	 */
	protected boolean isValidChild(Object child, EStructuralFeature containmentSF) {
		return classControl.isInstance(child);
	}

	
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.cde.core.ContainerPolicy#getCreateCommand(java.lang.Object, java.lang.Object)
	 */
	public Command getCreateCommand(Object child, Object positionBeforeChild) {
		return super.getCreateCommand(child, positionBeforeChild).chain(getCreateCoolItemCommand(child));
	}
	/*
	 * Create the command to create the parse tree allocation for the CoolItem, 
	 * the command to set the child as the 'control' property setting of the CoolItem,
	 * and the command to set the CoolItem as a child of the CoolBar. 
	 */
	private Command getCreateCoolItemCommand (final Object child){
		Command setCoolItemCommand = new CommandWrapper(){
			
			protected boolean prepare() {
				return true;
			}		
			
			public void execute(){
				IJavaObjectInstance coolItem = (IJavaObjectInstance)visualsFact.create(classCoolItem);
				PTClassInstanceCreation ic = InstantiationFactory.eINSTANCE.createPTClassInstanceCreation() ;
				ic.setType(coolItem.getJavaType().getJavaName()) ;
				
				// set the arguments
				PTInstanceReference ir = InstantiationFactory.eINSTANCE.createPTInstanceReference() ;
				ir.setObject((IJavaObjectInstance)getContainer()) ;	
				PTFieldAccess fa = InstantiationFactory.eINSTANCE.createPTFieldAccess();	
				PTName name = InstantiationFactory.eINSTANCE.createPTName("org.eclipse.swt.SWT") ;
				fa.setField("NONE");
				fa.setReceiver(name) ;
				ic.getArguments().add(ir);
				ic.getArguments().add(fa) ;
				
				JavaAllocation alloc = InstantiationFactory.eINSTANCE.createParseTreeAllocation(ic);
				coolItem.setAllocation(alloc);
				RuledCommandBuilder cb = new RuledCommandBuilder(domain);
				cb.applyAttributeSetting((EObject) coolItem, sf_coolItemControl, child);
				cb.applyAttributeSetting((EObject) getContainer(), sf_coolbarItems, coolItem);
				command = cb.getCommand();
				command.execute();
			}
		};
		return setCoolItemCommand;
	}


}
