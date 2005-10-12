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
 *  Created Oct 12, 2005 by Gili Mendel
 * 
 *  $RCSfile: ReadOnlyComboScenarios.java,v $
 *  $Revision: 1.3 $  $Date: 2005-10-12 18:02:34 $ 
 */
 
package org.eclipse.jface.tests.binding.scenarios;

import java.util.*;

import org.eclipse.jface.binding.BindingException;
import org.eclipse.jface.binding.IdentityConverter;
import org.eclipse.jface.examples.binding.emf.EMFUpdatableTable;
import org.eclipse.jface.viewers.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.ui.examples.rcp.adventure.*;
import org.eclipse.ui.examples.rcp.binding.scenarios.SampleData;
 

public class ReadOnlyComboScenarios extends ScenariosTestCase {
	
	protected ComboViewer cviewer = null;
	protected Combo       combo = null;

	protected void setUp() throws Exception {
		super.setUp();	
		combo = new Combo (getComposite(), SWT.READ_ONLY|SWT.DROP_DOWN);
		cviewer = new ComboViewer(combo);
		
		
	}

	protected void tearDown() throws Exception {
        combo.dispose();
        combo=null;
        cviewer=null;
		super.tearDown();
	}
	
	protected Object getViewerSelection() {
		return ((IStructuredSelection)cviewer.getSelection()).getFirstElement();
	}
	
	/**
	 * @return the ComboViewer's domain object list
	 */
	protected List getViewerContent() {
		Object[] elements = ((IStructuredContentProvider)cviewer.getContentProvider()).getElements(null);
		if (elements!=null)
		  return Arrays.asList(elements);
		return null;
	}

	/**
	 * 
	 * @return the combo's items (String[]), which is the same thing
	 *         as the Viewer's labels
	 * 
	 */
	protected List getComboContent() {
		String[] elements = combo.getItems();
		if (elements!=null)
			return Arrays.asList(elements);
		return null;
	}
	
	/**
	 * This test case deal with the 3rd scenario, using vanilla
	 * bindings: 
	 * 		Ensure a valid content and selection are bounded correctly
	 * 		Bind a collection of Lodgings to a ComboViewer
	 * 		Bind the ComboViewer's selection to the defaultLodging of an Adventure
	 * 
	 * This test does not deal with null values, empty content, changed content, 
	 * property change of content elements, etc. 
	 *  
	 */
	public void testScenario03_1() throws BindingException {

		//TODO: use this baseline to improve the API
		
			
		
		Adventure skiAdventure = SampleData.WINTER_HOLIDAY;  // selection will change its defaultLodging 
		Catalog catalog = SampleData.CATALOG_2005;		     // Lodging source
		
		
		// Bind the ComboViewer's content to the available lodging		
		getDbs().bindTable(
				getDbs().createUpdatableTable(cviewer, "contents"),
				new EMFUpdatableTable(catalog, "lodgings", new String[] {"description"})
		);
		
		// Ensure that cv's content now has the catalog's lodgings
		assertEquals(catalog.getLodgings(), getViewerContent());
		
		// Ensure that the cv's labels are the same as the lodging descriptions
		ArrayList descriptions = new ArrayList();
		for (Iterator iter = catalog.getLodgings().iterator(); iter.hasNext();) 
			descriptions.add(((Lodging)iter.next()).getDescription());
		assertEquals(descriptions, getComboContent());
		
		
		// Bind the ComboViewer's selection to the Adventure's default lodging.
		getDbs().bindValue(
				cviewer, "selection", skiAdventure ,"defaultLodging",
				new IdentityConverter(Object.class,Lodging.class),
				new IdentityConverter(Lodging.class,Object.class));
		
		// Check to see that the initial selection is the currentDefault Lodging
		assertEquals(getViewerSelection(), skiAdventure.getDefaultLodging());
		
		// Change the selection of the ComboViewer to all possible lodgings, and 
		// verify that skiAdventure's default lodging was changed accordingly
		for (Iterator iter = catalog.getLodgings().iterator(); iter.hasNext();) {
			Object selection = iter.next();
			cviewer.setSelection(new StructuredSelection(selection));
			assertEquals(selection, skiAdventure.getDefaultLodging());
			assertEquals(getViewerSelection(), skiAdventure.getDefaultLodging());
		}			
	}

}
