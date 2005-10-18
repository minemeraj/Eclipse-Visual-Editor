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
 *  $Revision: 1.8 $  $Date: 2005-10-18 17:38:36 $ 
 */

package org.eclipse.jface.tests.binding.scenarios;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jface.binding.BindingException;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.ui.examples.rcp.adventure.AdventureFactory;
import org.eclipse.ui.examples.rcp.adventure.Catalog;
import org.eclipse.ui.examples.rcp.adventure.Lodging;
import org.eclipse.ui.examples.rcp.binding.scenarios.SampleData;

public class ReadOnlyComboScenarios extends ScenariosTestCase {

	protected ComboViewer cviewer = null;

	protected Combo combo = null;

	ILabelProvider lodgingLabelProvider = new LabelProvider() {
		public String getText(Object element) {
			return ((Lodging) element).getName();
		}
	};

	protected void setUp() throws Exception {
		super.setUp();
		getComposite().setLayout(new FillLayout());

		combo = new Combo(getComposite(), SWT.READ_ONLY | SWT.DROP_DOWN);
		cviewer = new ComboViewer(combo);

	}

	protected void tearDown() throws Exception {
		combo.dispose();
		combo = null;
		cviewer = null;
		super.tearDown();
	}

	protected Object getViewerSelection() {
		return ((IStructuredSelection) cviewer.getSelection())
				.getFirstElement();
	}

	/**
	 * @return the ComboViewer's domain object list
	 */
	protected List getViewerContent() {
		Object[] elements = ((IStructuredContentProvider) cviewer
				.getContentProvider()).getElements(null);
		if (elements != null)
			return Arrays.asList(elements);
		return null;
	}

	/**
	 * 
	 * @return the combo's items (String[]), which is the same thing as the
	 *         Viewer's labels
	 * 
	 */
	protected List getComboContent() {
		String[] elements = combo.getItems();
		if (elements != null)
			return Arrays.asList(elements);
		return null;
	}

	protected List getColumn(EList list, String feature) {
		List result = new ArrayList();
		if (list == null || list.size() == 0)
			return result;
		EStructuralFeature sf = ((EObject) list.get(0)).eClass()
				.getEStructuralFeature(feature);
		for (Iterator iter = list.iterator(); iter.hasNext();) {
			EObject o = (EObject) iter.next();
			result.add(o.eGet(sf));
		}
		return result;
	}

	/**
	 * This test case deal with the 3rd scenario, using vanilla bindings: Ensure
	 * a valid content and selection are bounded correctly Bind a collection of
	 * Lodgings to a ComboViewer Bind the ComboViewer's selection to the
	 * defaultLodging of an Adventure
	 * 
	 * This test does not deal with null values, empty content, changed content,
	 * property change of content elements, etc.
	 * 
	 */
	// public void testScenario03_original() throws BindingException {
	//
	// //TODO: use this baseline to improve the API
	//		
	//			
	//		
	// Adventure skiAdventure = SampleData.WINTER_HOLIDAY; // selection will
	// change its defaultLodging
	// Catalog catalog = SampleData.CATALOG_2005; // Lodging source
	//		
	// // need to be disposed
	// EMFUpdatableTable updatableTable = new EMFUpdatableTable(catalog,
	// "lodgings", new String[] {"description"});
	//		
	// getDbs().bindTable(
	// getDbs().createUpdatableTable(cviewer, "contents"),
	// updatableTable
	// );
	//		
	//		
	//		
	// // Ensure that cv's content now has the catalog's lodgings
	// assertEquals(catalog.getLodgings(), getViewerContent());
	//		
	// // Ensure that the cv's labels are the same as the lodging descriptions
	// assertEquals(getColumn(catalog.getLodgings(), "description"),
	// getComboContent());
	//		
	//		
	// // Bind the ComboViewer's selection to the Adventure's default lodging.
	// getDbs().bindValue(
	// cviewer, "selection", skiAdventure ,"defaultLodging",
	// new IdentityConverter(Object.class,Lodging.class),
	// new IdentityConverter(Lodging.class,Object.class));
	//		
	// // Check to see that the initial selection is the currentDefault Lodging
	// assertEquals(getViewerSelection(), skiAdventure.getDefaultLodging());
	//		
	// // Change the selection of the ComboViewer to all possible lodgings, and
	// // verify that skiAdventure's default lodging was changed accordingly
	// for (Iterator iter = catalog.getLodgings().iterator(); iter.hasNext();) {
	// Object selection = iter.next();
	// cviewer.setSelection(new StructuredSelection(selection));
	// assertEquals(selection, skiAdventure.getDefaultLodging());
	// assertEquals(getViewerSelection(), skiAdventure.getDefaultLodging());
	// }
	//		
	// updatableTable.dispose();
	//					
	// }

	/**
	 * This test case deal with the 3rd scenario, and focuses on the collection
	 * binding to the combo. It will bind a collection, add/remove/change
	 * elements in the collection, and change element's properties to ensure
	 * that the combo's labels were updated appropriatly.
	 * 
	 * it also induce null values in properties, and elments.
	 * 
	 * This test does not deal with the combo's selection.
	 */
	public void test_ROCombo_Scenario03_collectionBindings()
			throws BindingException {

		Catalog catalog = SampleData.CATALOG_2005; // Lodging source

		cviewer.setLabelProvider(lodgingLabelProvider); // TODO: need to resolve
														// column binding
		// Bind the ComboViewer's content to the available lodging
		getDbs().bind(cviewer, "contents", catalog, "lodgings");

		// Ensure that cv's content now has the catalog's lodgings
		assertEquals(catalog.getLodgings(), getViewerContent());

		// Ensure that the cv's labels are the same as the lodging descriptions
		assertEquals(getColumn(catalog.getLodgings(), "name"),
				getComboContent());

		EList lodgings = catalog.getLodgings();

		// Add a lodging in the middle
		Lodging lodging = AdventureFactory.eINSTANCE.createLodging();
		lodging.setName("Middle Lodging");
		lodgings.add(2, lodging);
		assertEquals(getViewerContent().get(2), lodging);

		// Add a lodging at the end
		lodging = AdventureFactory.eINSTANCE.createLodging();
		lodging.setName("End Lodging");
		lodgings.add(lodging);
		int index = getComboContent().size() - 1;
		assertEquals(getViewerContent().get(index), lodging);

		// Delete the first Lodging
		lodgings.remove(lodgings.get(0));
		// Ensure that the cv's labels are the same as the lodging descriptions
		assertEquals(getColumn(catalog.getLodgings(), "name"),
				getComboContent());

		// Delete middle Lodging
		lodgings.remove(lodgings.get(2));
		// Ensure that the cv's labels are the same as the lodging descriptions
		assertEquals(getColumn(catalog.getLodgings(), "name"),
				getComboContent());

		// Change the names of all Lodging
		for (Iterator iter = lodgings.iterator(); iter.hasNext();) {
			Lodging l = (Lodging) iter.next();
			l.setName("Changed: " + l.getName());
		}
		assertEquals(getColumn(catalog.getLodgings(), "name"),
				getComboContent());

		// TODO: set name to null

		// TODO: set to empty list

	}

}
