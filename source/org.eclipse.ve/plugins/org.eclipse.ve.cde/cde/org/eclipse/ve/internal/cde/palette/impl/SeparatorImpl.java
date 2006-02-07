/**
 * <copyright>
 * </copyright>
 *
 * $Id: SeparatorImpl.java,v 1.3 2006-02-07 17:21:33 rkulp Exp $
 */
package org.eclipse.ve.internal.cde.palette.impl;


import org.eclipse.emf.ecore.EClass;
import org.eclipse.gef.palette.PaletteEntry;
import org.eclipse.gef.palette.PaletteSeparator;

import org.eclipse.ve.internal.cde.palette.PalettePackage;
import org.eclipse.ve.internal.cde.palette.Separator;


/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Separator</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * </p>
 *
 * @generated
 */
public class SeparatorImpl extends EntryImpl implements Separator {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected SeparatorImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EClass eStaticClass() {
		return PalettePackage.Literals.SEPARATOR;
	}

	protected PaletteEntry createPaletteEntry() {
		return new PaletteSeparator();
	}

} //SeparatorImpl
