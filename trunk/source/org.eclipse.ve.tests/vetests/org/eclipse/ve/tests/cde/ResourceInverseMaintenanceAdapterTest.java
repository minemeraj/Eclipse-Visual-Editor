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
 *  $RCSfile: ResourceInverseMaintenanceAdapterTest.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:54:15 $ 
 */
import java.util.ArrayList;

import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;

import org.eclipse.ve.internal.cde.emf.InverseMaintenanceAdapter;
import org.eclipse.ve.internal.cde.emf.ResourceInverseMaintenanceAdapter;

import junit.framework.TestCase;

/**
 * This tests the ResourceInverseMaintenanceAdapter.
 */
public class ResourceInverseMaintenanceAdapterTest extends TestCase {

	public ResourceInverseMaintenanceAdapterTest(String name) {
		super(name);
	}
	
	/**
	 * Test propagating after resource already loaded.
	 */
	public void testPropagateThruResource() {
		Resource res = new ResourceImpl();
		EReference ref1 = EcoreFactory.eINSTANCE.createEReference();
		
		res.getContents().add(ref1);
		
		// Do the tests.
		InverseMaintenanceAdapter ia = (InverseMaintenanceAdapter) EcoreUtil.getExistingAdapter(ref1, InverseMaintenanceAdapter.ADAPTER_KEY);
		assertNull(ia);
		
		ResourceInverseMaintenanceAdapter ria = new ResourceInverseMaintenanceAdapter(); 
		res.eAdapters().add(ria);
		ria.propagate();
		ia = (InverseMaintenanceAdapter) EcoreUtil.getExistingAdapter(ref1, InverseMaintenanceAdapter.ADAPTER_KEY);		
		assertNotNull(ia);
		assertTrue(ia.isPropagated());		
	}
	
	/**
	 * Test adding a single to resource
	 */
	public void testAddToResource() {
		Resource res = new ResourceImpl();
		res.eAdapters().add(new ResourceInverseMaintenanceAdapter());
		
		EReference ref1 = EcoreFactory.eINSTANCE.createEReference();
		
		res.getContents().add(ref1);
		
		// Do the tests.
		InverseMaintenanceAdapter ia = (InverseMaintenanceAdapter) EcoreUtil.getExistingAdapter(ref1, InverseMaintenanceAdapter.ADAPTER_KEY);
		assertNotNull(ia);
		assertTrue(ia.isPropagated());
	}

	/**
	 * Test adding many to resource
	 */
	public void testAddManyToResource() {
		Resource res = new ResourceImpl();
		res.eAdapters().add(new ResourceInverseMaintenanceAdapter());
		
		EReference ref1 = EcoreFactory.eINSTANCE.createEReference();
		EReference ref2 = EcoreFactory.eINSTANCE.createEReference();
		ArrayList list = new ArrayList(2);
		list.add(ref1);
		list.add(ref2);		
		
		res.getContents().addAll(list);
		
		// Do the tests.
		InverseMaintenanceAdapter ia = (InverseMaintenanceAdapter) EcoreUtil.getExistingAdapter(ref1, InverseMaintenanceAdapter.ADAPTER_KEY);
		assertNotNull(ia);
		assertTrue(ia.isPropagated());
		InverseMaintenanceAdapter ia2 = (InverseMaintenanceAdapter) EcoreUtil.getExistingAdapter(ref2, InverseMaintenanceAdapter.ADAPTER_KEY);
		assertNotNull(ia2);
		assertTrue(ia2.isPropagated());
	}
	
	/**
	 * Test set (i.e. replace one already added) a single to resource
	 */
	public void testSetToResource() {
		Resource res = new ResourceImpl();
		res.eAdapters().add(new ResourceInverseMaintenanceAdapter());
		
		EReference ref1 = EcoreFactory.eINSTANCE.createEReference();
		
		res.getContents().add(ref1);
		
		EReference ref2 = EcoreFactory.eINSTANCE.createEReference();
		res.getContents().set(0, ref2);		
		
		// Do the tests.
		InverseMaintenanceAdapter ia = (InverseMaintenanceAdapter) EcoreUtil.getExistingAdapter(ref2, InverseMaintenanceAdapter.ADAPTER_KEY);
		assertNotNull(ia);
		assertTrue(ia.isPropagated());
	}	

}
