package org.eclipse.ve.tests.cde;
/*******************************************************************************
 * Copyright (c)  2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: InverseMaintenanceAdapterContainedBeforePropagate.java,v $
 *  $Revision: 1.2 $  $Date: 2005-02-15 23:59:07 $ 
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
 * This handles before propagation and following propagation. I.e. It tests that propagation
 * goes through and applies the settings.
 */
public class InverseMaintenanceAdapterContainedBeforePropagate extends TestCase {


	public InverseMaintenanceAdapterContainedBeforePropagate(String name) {
		super(name);
	}

	/**
	 * Test that adding an adapter and propagate without being in a resource does nothing.
	 * (B1) 
	 */
	public void testAddNoResourceWithExistingAdapter() {
		// Use an EReference for simplicity. Create it, add an and EAnnotation to it and see that it propagates.
		EReference ref1 = EcoreFactory.eINSTANCE.createEReference();
		EAnnotation ann1 = EcoreFactory.eINSTANCE.createEAnnotation();
		
		InverseMaintenanceAdapter ia = new InverseMaintenanceAdapter();
		ref1.eAdapters().add(ia);
		
		InverseMaintenanceAdapter ia2 = new InverseMaintenanceAdapter();
		ann1.eAdapters().add(ia2);		
		
		ref1.getEAnnotations().add(ann1);
				
		// Shouldn't do anything until propagate
		assertFalse(ia2.isPropagated());		
		
		ia.propagate();
		
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
		
		ref1.getEAnnotations().add(ann1);
				
		// Shouldn't do anything until propagate
		assertNull(EcoreUtil.getExistingAdapter(ann1, InverseMaintenanceAdapter.ADAPTER_KEY));		
		
		ia.propagate();
		
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
		
		InverseMaintenanceAdapter ia2 = new InverseMaintenanceAdapter();
		ann1.eAdapters().add(ia2);		
		
		ref1.getEAnnotations().add(ann1);
				
		// Shouldn't do anything until propagate
		assertFalse(ia2.isPropagated());		
		
		ia.propagate();
		
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
		
		ref1.getEAnnotations().add(ann1);
				
		// Shouldn't do anything until propagate
		assertNull(EcoreUtil.getExistingAdapter(ann1, InverseMaintenanceAdapter.ADAPTER_KEY));		
		
		ia.propagate();
		
		// Now the tests.
		assertTrue(ia.isPropagated());
		InverseMaintenanceAdapter ia2 = (InverseMaintenanceAdapter) EcoreUtil.getExistingAdapter(ann1, InverseMaintenanceAdapter.ADAPTER_KEY);
		assertNotNull(ia2);
		assertTrue(ia2.isPropagated());
	}
	
}
