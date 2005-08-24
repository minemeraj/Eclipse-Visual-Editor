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
package org.eclipse.ve.tests.cde;
/*
 *  $RCSfile: InverseMaintenanceAdapterContainedAfterPropagate.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:54:15 $ 
 */
import org.eclipse.emf.ecore.*;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;

import org.eclipse.ve.internal.cde.emf.InverseMaintenanceAdapter;

import junit.framework.TestCase;

/**
 * Tests for the Inverse Maintenance Adapter for Type (B) with (C) from InverseMaintenanceAdapter
 * comments on algorithm. Type (B) is handling contained references.
 * 
 * This goes after propagation. Makes sure it handles the notifications correctly.
 */
public class InverseMaintenanceAdapterContainedAfterPropagate extends TestCase {


	public InverseMaintenanceAdapterContainedAfterPropagate(String name) {
		super(name);
	}

	/**
	 * Test that adding an adapter after propagate without being in a resource does nothing.
	 * (B1) 
	 */
	public void testAddNoResourceWithExistingAdapter() {
		// Use an EReference for simplicity. Create it, add an and EAnnotation to it and see that it propagates.
		EReference ref1 = EcoreFactory.eINSTANCE.createEReference();
		EAnnotation ann1 = EcoreFactory.eINSTANCE.createEAnnotation();
		
		InverseMaintenanceAdapter ia = new InverseMaintenanceAdapter();
		ref1.eAdapters().add(ia);
		ia.propagate();		
		
		InverseMaintenanceAdapter ia2 = new InverseMaintenanceAdapter();
		ann1.eAdapters().add(ia2);		
		
		ref1.getEAnnotations().add(ann1);
				
		// Now the tests.
		assertFalse(ia2.isPropagated());
	}

	
	/**
	 * Test that adding an adapter and propagate w/o being in a resource does nothing.
	 * (B2) 
	 */
	public void testAddNoResource() {
		// Use an EReference for simplicity. Create it, add an EAnnotation to it and see that it doesn't propagate.
		EReference ref1 = EcoreFactory.eINSTANCE.createEReference();
		EAnnotation ann1 = EcoreFactory.eINSTANCE.createEAnnotation();

		InverseMaintenanceAdapter ia = new InverseMaintenanceAdapter();
		ref1.eAdapters().add(ia);
		ia.propagate();		
		
		ref1.getEAnnotations().add(ann1);
						
		// Now the tests.
		assertFalse(ia.isPropagated());
		assertNull(EcoreUtil.getExistingAdapter(ann1, InverseMaintenanceAdapter.ADAPTER_KEY));
	}

	/**
	 * Test that adding an adapter and propagate with being in a resource works. The adapter of the
	 * target will be added immediately. Test will make sure it gets propagated correctly.
	 * (B1) 
	 */
	public void testAddResourceWithExistingAdapter() {
		// Use an EReference for simplicity. Create it, add an and EAnnotation to it and see that it propagates.
		EReference ref1 = EcoreFactory.eINSTANCE.createEReference();
		EAnnotation ann1 = EcoreFactory.eINSTANCE.createEAnnotation();
		Resource res = new ResourceImpl();
		res.getContents().add(ref1);
		
		InverseMaintenanceAdapter ia = new InverseMaintenanceAdapter();
		ref1.eAdapters().add(ia);
		ia.propagate();		
		
		InverseMaintenanceAdapter ia2 = new InverseMaintenanceAdapter();
		ann1.eAdapters().add(ia2);		
		
		ref1.getEAnnotations().add(ann1);
				
		// Now the tests.
		assertTrue(ia.isPropagated());
		assertTrue(ia2.isPropagated());
	}

	/**
	 * Test that adding an adapter and propagate with being in a resource works. It will
	 * add an adapter during propagation.
	 * (B2) 
	 */
	public void testAddResource() {
		// Use an EReference for simplicity. Create it, add an and EAnnotation to it and see that it propagates.
		EReference ref1 = EcoreFactory.eINSTANCE.createEReference();
		EAnnotation ann1 = EcoreFactory.eINSTANCE.createEAnnotation();
		Resource res = new ResourceImpl();
		res.getContents().add(ref1);
		
		InverseMaintenanceAdapter ia = new InverseMaintenanceAdapter();
		ref1.eAdapters().add(ia);
		ia.propagate();		
		
		ref1.getEAnnotations().add(ann1);
				
		// Now the tests.
		assertTrue(ia.isPropagated());
		InverseMaintenanceAdapter ia2 = (InverseMaintenanceAdapter) EcoreUtil.getExistingAdapter(ann1, InverseMaintenanceAdapter.ADAPTER_KEY);
		assertNotNull(ia2);
		assertTrue(ia2.isPropagated());
	}
	
}
