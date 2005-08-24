/*******************************************************************************
 * Copyright (c) 2003, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: ObjectEventDecoder.java,v $
 *  $Revision: 1.6 $  $Date: 2005-08-24 23:30:45 $ 
 */
package org.eclipse.ve.internal.java.codegen.java;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.core.dom.*;

import org.eclipse.ve.internal.jcm.EventInvocation;
import org.eclipse.ve.internal.jcm.PropertyChangeEventInvocation;
import org.eclipse.ve.internal.java.codegen.model.BeanPart;

/**
 * @author Gili Mendel
 *
 */
public class ObjectEventDecoder extends AbstractEventDecoder {
	/**
	 * @see org.eclipse.ve.internal.java.codegen.java.AbstractEventDecoder#initialDecoderHelper()
	 */
	public IEventDecoderHelper createDecoderHelper(Statement exp) {

		IEventDecoderHelper helper = null;
		if (exp != null) {
			if (exp instanceof ExpressionStatement &&
				((ExpressionStatement)exp).getExpression() instanceof MethodInvocation ) {
				MethodInvocation mi = (MethodInvocation)((ExpressionStatement)exp).getExpression();
				if (mi.arguments().size()== 1) {

					// regular VCE pattern
					boolean innerType = mi.arguments().get(0) instanceof SimpleName;
					// jBuilder pattern
					if (mi.arguments().get(0) instanceof ClassInstanceCreation) {
						ClassInstanceCreation ae = (ClassInstanceCreation) mi.arguments().get(0);
						if (ae.arguments().size() == 1 && (ae.arguments().get(0) instanceof ThisExpression))
							innerType = true;
					}

					if (innerType) {
						// addXXListener(sharedListener)
						if (getEventInvocation() instanceof EventInvocation)
							helper = new InnerClassStyleHelper(fbeanPart, exp, this);
						else
							helper = new PropertyChangedInnerStyleHelper(fbeanPart, exp, this);
					}
					else {
						if (getEventInvocation() instanceof EventInvocation) {
							// Vanilla Event	      
							helper = new AllocationStyleHelper(fbeanPart, exp, this);
						}
						else {
							// Property Changed event style
							helper = new PropertyChangedAllocationStyleHellper(fbeanPart, exp, this);
						}
					}
				}
				else if (mi.arguments().size() == 2 && getEventInvocation() instanceof PropertyChangeEventInvocation) {
					// "property",Listener signiture
					helper = new PropertyChangedAllocationStyleHellper(fbeanPart, exp, this);
				}
			}
		}
		else {
			// Generating a brand new one
			if (fEventInvocation instanceof EventInvocation)
				helper = new AllocationStyleHelper(fbeanPart, exp, this);
			else
				helper = new PropertyChangedAllocationStyleHellper(fbeanPart, exp, this);
		}
		return helper;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.java.IJVEDecoder#createCodeGenInstanceAdapter()
	 */
	public ICodeGenAdapter createCodeGenInstanceAdapter(BeanPart bp) {		
		return new BeanDecoderAdapter(bp) ;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.java.IJVEDecoder#createThisCodeGenInstanceAdapter(org.eclipse.ve.internal.java.codegen.model.BeanPart)
	 */
	public ICodeGenAdapter createThisCodeGenInstanceAdapter(BeanPart bp) {
		EObject bean = bp.getEObject();
		return new ThisBeanDecoderAdapter(bean, bp);
	}

}
