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
 *  $RCSfile: ShellPropertySourceAdapter.java,v $
 *  $Revision: 1.9 $  $Date: 2006-02-06 17:14:41 $ 
 */
package org.eclipse.ve.internal.swt;

import org.eclipse.jem.internal.instantiation.*;
import org.eclipse.jem.internal.instantiation.PTClassInstanceCreation;
import org.eclipse.jem.internal.instantiation.PTExpression;

/**
 * SWT Shell property source adapter.
 * @since 1.0.0
 */
public class ShellPropertySourceAdapter extends CompositePropertySourceAdapter {

	private final String[] TRIM_STRINGS = new String[] {SWT_TYPE_ID, "SHELL_TRIM"}; //$NON-NLS-1$
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.swt.WidgetPropertySourceAdapter#getChangedAllocation(org.eclipse.jem.internal.instantiation.PTExpression, org.eclipse.ve.internal.swt.WidgetPropertySourceAdapter.StyleBitPropertyID, int)
	 */
	protected PTExpression getChangedAllocation(PTExpression allocationExpression, StyleBitPropertyID propertyID, int styleBit) {
		if (allocationExpression instanceof PTClassInstanceCreation) {
			PTClassInstanceCreation classInstanceCreation = (PTClassInstanceCreation) allocationExpression;
			PTExpression newArg = null;
			PTExpression currentArg = null;
			switch (classInstanceCreation.getArguments().size()) {
				case 0:
					// No args. So get changed arg. Tricky here, but if changed arg does not contain SHELL_TRIM, we need
					// to add that in or it will not have trim (which is usually added by default within default ctor).
					newArg = getChangedStyleExpression(null, propertyID, styleBit);
					if (newArg != null) {
						// See if trim family set, if not, add it in. Only if we are not adjusting trim already.
						// (Actually since we are going from nothing to something, really shouldn't need to test if
						// trim in here, we should know it isn't).
						if (!propertyID.getFamilyName().equals("trim")) { //$NON-NLS-1$
							StyleBitPropertyID trimFamily = findFamily("trim"); //$NON-NLS-1$
							if (!isSameStyleFamilySet(newArg, trimFamily)) {
								// Add it in.
								newArg = addStyleBit(newArg, trimFamily, trimFamily.getStyle(TRIM_STRINGS[0], TRIM_STRINGS[1]));
							}
						} 
						// If it is only trim, then trim was explicitly added, so we leave it alone.
						classInstanceCreation.getArguments().add(newArg);
						return classInstanceCreation;
					} else
						return null;	// Not changed
				case 1:
					// We are either style or Shell or Display. Let it try to update it.
					// Since we have a setting we don't want to test for trim family. It has explicitly been handled.
					// Try to do a little, if it is not a field access or a infix w/OR, then create second arg to handle it.
					// This will cause factory to have a problem (i.e. you have a method that returns a style). We won't
					// catch that and will create bad code. Or if field access to a shell or display, then it will try
					// to or it in and will again get bad code. But maybe better than ignoring it.
					newArg = (PTExpression) classInstanceCreation.getArguments().get(0);
					if (newArg instanceof PTFieldAccess || newArg instanceof PTInfixExpression) {
						currentArg = (PTExpression) classInstanceCreation.getArguments().set(0, InstantiationFactory.eINSTANCE.createPTNullLiteral());
						newArg = getChangedStyleExpression(currentArg, propertyID, styleBit);
						if (newArg != null)
							classInstanceCreation.getArguments().set(0, newArg);
						else
							classInstanceCreation.getArguments().clear(); // We went to no setting, so go to no args.
					} else {
						// Let's expand to two args.
						newArg = getChangedStyleExpression(null, propertyID, styleBit);
						if (newArg != null) {
							// See if trim family set, if not, add it in. Only if we are not adjusting trim already.
							// (Actually since we are going from nothing to something, really shouldn't need to test if
							// trim in here, we should know it isn't).
							if (!propertyID.getFamilyName().equals("trim")) { //$NON-NLS-1$
								StyleBitPropertyID trimFamily = findFamily("trim"); //$NON-NLS-1$
								if (!isSameStyleFamilySet(newArg, trimFamily)) {
									// Add it in.
									newArg = addStyleBit(newArg, trimFamily, trimFamily.getStyle(TRIM_STRINGS[0], TRIM_STRINGS[1]));
								}
							} 
							// If it is only trim, then trim was explicitly added, so we leave it alone.							
							classInstanceCreation.getArguments().add(newArg);
						} else
							return null; // We went to no setting, so go back to one arg, i.e. no change.						
					}
					return classInstanceCreation;
				case 2:
					// If 2, then style is definitely the second arg. We will not add in trim if we have an arg
					// because trim has already been taken care of (i.e. they added it or wasn't there, don't force it).
					currentArg = (PTExpression) classInstanceCreation.getArguments().set(1, InstantiationFactory.eINSTANCE.createPTNullLiteral());
					newArg = getChangedStyleExpression(currentArg, propertyID, styleBit);
					if (newArg != null)
						classInstanceCreation.getArguments().set(1, newArg);
					else {
						// We are getting rid of the style entirely, so we can just go down to one arg for shell.
						classInstanceCreation.getArguments().remove(1);
					}
					return classInstanceCreation;
				default:
					// If >2, then style is definitely the second arg. We will not add in trim if we have an arg
					// because trim has already been taken care of (i.e. they added it or wasn't there, don't force it).
					currentArg = (PTExpression) classInstanceCreation.getArguments().set(1, InstantiationFactory.eINSTANCE.createPTNullLiteral());
					newArg = getChangedStyleExpression(currentArg, propertyID, styleBit);
					if (newArg != null)
						classInstanceCreation.getArguments().set(1, newArg);
					else {
						// We are getting rid of the style entirely, but we can't get rid of the arg because there
						// are more after it. So we need to put in a trim style.
						classInstanceCreation.getArguments().set(1, updateField(null, TRIM_STRINGS));
					}
					return classInstanceCreation;
			}
		}
		return null;	// Don't change it. We can't just return the same one because no way of knowing something under it had changed.
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.swt.WidgetPropertySourceAdapter#getStyleExpression(org.eclipse.jem.internal.instantiation.PTExpression)
	 */
	protected PTExpression getStyleExpression(PTExpression allocationExp) {
		if (allocationExp instanceof PTClassInstanceCreation) {
			PTClassInstanceCreation classInstanceCreation = (PTClassInstanceCreation) allocationExp;
			switch (classInstanceCreation.getArguments().size()) {
				case 0:
					return null;	// No style expression
				case 1:
					// We are assuming if one arg, then it is style. Not really valid for Shell. It could be Display or Shell, but we don't know.
					// However the usage of this method will try to instantiate it and it won't be an integer if not style. So ok.
					return (PTExpression) classInstanceCreation.getArguments().get(0);
				default:
					// Any more than one is ok. They all have style as second arg.
					return (PTExpression) classInstanceCreation.getArguments().get(1);
			}
		} else return super.getStyleExpression(allocationExp);
	}
}
