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
 *  $RCSfile: WidgetContainmentHandler.java,v $
 *  $Revision: 1.2 $  $Date: 2005-11-04 17:30:52 $ 
 */
package org.eclipse.ve.internal.swt;

import org.eclipse.jem.internal.instantiation.*;
import org.eclipse.jem.internal.instantiation.base.*;

import org.eclipse.ve.internal.cde.commands.CommandBuilder;
import org.eclipse.ve.internal.cde.core.EditDomain;
 

/**
 * Containment Handler for a widget.
 * @since 1.2.0
 */
public class WidgetContainmentHandler extends NoFFModelAdapter {

	public WidgetContainmentHandler(Object model) {
		super(model);
	}

	public Object contributeToDropRequest(Object parent, Object child, CommandBuilder preCmds, CommandBuilder postCmds, boolean creation, EditDomain domain) throws StopRequestException {
		child = super.contributeToDropRequest(parent, child, preCmds, postCmds, creation, domain);	// Let super handle is not on FF.
		processAllocation(parent, child, preCmds);
		return child;		
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.swt.NoFFModelAdapter#contributeToRemoveRequest(java.lang.Object, java.lang.Object, org.eclipse.ve.internal.cde.commands.CommandBuilder, org.eclipse.ve.internal.cde.commands.CommandBuilder, boolean, org.eclipse.ve.internal.cde.core.EditDomain)
	 */
	public Object contributeToRemoveRequest(Object parent, Object child, CommandBuilder preCmds, CommandBuilder postCmds, boolean orphan, EditDomain domain) throws StopRequestException {
		if (orphan)
			postCmds.append(new EnsureOrphanFromParentCommand((IJavaObjectInstance) child, (IJavaObjectInstance) parent));
		return super.contributeToRemoveRequest(parent, child, preCmds, postCmds, orphan, domain);
	}

	/**
	 * Process the allocation. All widget containment handlers that don't subclass WidgetContainmentHandler and call
	 * super.contributeToDropRequest must call this to handle the allocation.
	 * <p>
	 * This method makes sure that there if no allocation it will create one of the form {@link org.eclipse.swt.widgets.Widget#Widget(org.eclipse.swt.widgets.Widget, int)}
	 * where the widget is replaced by the parent and the int becomes <code>SWT.NONE</code>.
	 * <p>
	 * And if there is an allocation it will make sure that {parentComposite} is replaced with a reference to the parent. 
	 * @param parent
	 * @param child
	 * @param preCmds
	 * 
	 * @since 1.2.0
	 */
	public static void processAllocation(Object parent, Object child, CommandBuilder preCmds) {
		if (child instanceof IJavaObjectInstance) {
			IJavaObjectInstance jo = (IJavaObjectInstance) child;
			if (!jo.isSetAllocation()) {
				// Needs an allocation. Use default of new Widget(parent, SWT.NONE);
				ParseTreeAllocation parseTreeAllocation = InstantiationFactory.eINSTANCE.createParseTreeAllocation();
				PTClassInstanceCreation classInstanceCreation = InstantiationFactory.eINSTANCE.createPTClassInstanceCreation(jo.getJavaType().getQualifiedNameForReflection(), null);
				PTInstanceReference parentReference = InstantiationFactory.eINSTANCE.createPTInstanceReference((IJavaInstance) parent);
				classInstanceCreation.getArguments().add(parentReference);
				PTName SWTType = InstantiationFactory.eINSTANCE.createPTName("org.eclipse.swt.SWT"); 
				PTFieldAccess fieldAccess = InstantiationFactory.eINSTANCE.createPTFieldAccess(SWTType, "NONE");
				classInstanceCreation.getArguments().add(fieldAccess);
				parseTreeAllocation.setExpression(classInstanceCreation);
				
				preCmds.applyAttributeSetting(jo, JavaInstantiation.getAllocationFeature(jo), parseTreeAllocation);
			} else if (parent instanceof IJavaObjectInstance && jo.isParseTreeAllocation())
				preCmds.append(new EnsureCorrectParentCommand(jo, (IJavaObjectInstance) parent));
		}
	}
}
