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
package org.eclipse.jface.tests.binding.scenarios;

import java.util.*;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jface.databinding.BindingException;
import org.eclipse.jface.databinding.PropertyDescription;
import org.eclipse.jface.databinding.swt.SWTBindingConstants;
import org.eclipse.jface.viewers.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.ui.examples.rcp.adventure.*;
import org.eclipse.ui.examples.rcp.binding.scenarios.SampleData;

public class ReadOnlyComboScenarios extends ScenariosTestCase {

	protected ComboViewer cviewer = null;

	protected Combo combo = null;
	
	protected Catalog catalog = null;

	ILabelProvider lodgingLabelProvider = new LabelProvider() {
		public String getText(Object element) {
			return ((Lodging) element).getName();
		}
	};
	
	ILabelProvider accountLabelProvider = new LabelProvider() {
		public String getText(Object element) {
			return ((Account) element).getCountry();
		}
	};

	protected void setUp() throws Exception {
		super.setUp();
		getComposite().setLayout(new FillLayout());

		combo = new Combo(getComposite(), SWT.READ_ONLY | SWT.DROP_DOWN);
		cviewer = new ComboViewer(combo);
		
		catalog = SampleData.CATALOG_2005; // Lodging source

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
	protected List getViewerContent(ComboViewer cviewer) {
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
	 public void test_ROCombo_Scenario03_vanilla() throws BindingException {
	
			
		 Adventure skiAdventure = SampleData.WINTER_HOLIDAY; // selection will
		 // change its defaultLodging
		 
		 cviewer.setLabelProvider(lodgingLabelProvider);
	     // Bind the ComboViewer's content to the available lodging
	  	 getDbc().bind2(cviewer, new PropertyDescription(catalog, "lodgings"), null);
				
		 // Ensure that cv's content now has the catalog's lodgings
		 assertEquals(catalog.getLodgings(), getViewerContent(cviewer));
				
		 // Ensure that the cv's labels are the same as the lodging descriptions
		 assertEquals(getColumn(catalog.getLodgings(), "name"), getComboContent());
				
				
		 // Bind the ComboViewer's selection to the Adventure's default lodging.
		 getDbc().bind2(new PropertyDescription(cviewer, SWTBindingConstants.SELECTION), 
				       new PropertyDescription(skiAdventure ,"defaultLodging"), null);
				
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

		

		cviewer.setLabelProvider(lodgingLabelProvider); // TODO: need to resolve
														// column binding
		// Bind the ComboViewer's content to the available lodging
		getDbc().bind2(cviewer, new PropertyDescription(catalog, "lodgings"), null);

		// Ensure that cv's content now has the catalog's lodgings
		assertEquals(catalog.getLodgings(), getViewerContent(cviewer));

		// Ensure that the cv's labels are the same as the lodging descriptions
		assertEquals(getColumn(catalog.getLodgings(), "name"),
				getComboContent());

		EList lodgings = catalog.getLodgings();

		// Add a lodging in the middle
		Lodging lodging = AdventureFactory.eINSTANCE.createLodging();
		lodging.setName("Middle Lodging");
		lodgings.add(2, lodging);
		assertEquals(getViewerContent(cviewer).get(2), lodging);

		// Add a lodging at the end
		lodging = AdventureFactory.eINSTANCE.createLodging();
		lodging.setName("End Lodging");
		lodgings.add(lodging);
		int index = getComboContent().size() - 1;
		assertEquals(getViewerContent(cviewer).get(index), lodging);

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
		
		// Set to null value
		Lodging l = (Lodging) catalog.getLodgings().get(0);
		l.setName(null);
		assertEquals(combo.getItem(0),"");
		
		// set to empty list
		while (lodgings.size()>0)
			lodgings.remove(0);
		assertEquals(getColumn(catalog.getLodgings(), "name"),
					 getComboContent());
		

		
	}
	
	/**
	 * This scenario tests a simple SWT combo with a set item list where the selection is bouded to a 
	 * String property
	 */
	 public void test_ROCombo_Scenario01() throws BindingException {
		 
		 
         //	Read-Only Combo will not change its text property on a call to setText()
		 		 
		 String[] items = new String[] { "FairyLand", "TuneLand", "NoWereLand", "TinkerLand", "DreamLand" };
		 combo.setItems (items);
		 Account account = (Account) catalog.getAccounts().get(0);
		 
		 // simple Combo's selection bound to the Account's country property
		 getDbc().bind2(new PropertyDescription(combo, SWTBindingConstants.SELECTION), 
				        new PropertyDescription(account, "country"), null);
		 
		 // Drive the combo selection
		 int index = 3;		 
		 combo.setText(items[index]); // this should drive the selection 
		 assertEquals(account.getCountry(), items[index]);
		 
		 // Set the country, and ensure selection is set property
		 index = 1;
		 account.setCountry(items[index]);
		 assertEquals(index, combo.getSelectionIndex());
		 assertEquals(combo.getText(), items[index]);
		 
		 index = combo.getSelectionIndex();
		 String txt = combo.getText();
		 // Set the country to something that is not in the Combo's list
		 account.setCountry("FooBar");		 
		 // Combo's selection will not Change
		 assertEquals(combo.getSelectionIndex(), index);
		 assertEquals(combo.getText(), txt);
		 
		 
	 }

		/**
		 * This scenario tests a simple SWT combo that is bound to a list of 
		 * Country objects. The Country object's name property is listed in the Combo.
		 * 
		 * The Combo's selection is bounded to the Country property of an Account.
		 */
		 public void test_ROCombo_Scenario02_SWTCombo() throws BindingException {
			 
			 // Create a list of Strings for the countries
		    BasicEList list = new BasicEList();
		    for (Iterator iter = catalog.getAccounts().iterator(); iter.hasNext();) 
				list.add(((Account)iter.next()).getCountry());				
			
		    // Bind the combo's content to that of the String based list
		    getDbc().bind2(combo, new PropertyDescription(list, null), null);		    
		    assertEquals(Arrays.asList(combo.getItems()), list);
		    
		    Account account = (Account) catalog.getAccounts().get(0);
			 
		    // simple Combo's selection bound to the Account's country property
			 getDbc().bind2(new PropertyDescription(combo, SWTBindingConstants.SELECTION), 
					 		new PropertyDescription(account, "country"), null);
			 
			 // Drive the combo selection
			 String selection = (String)list.get(2); 			
			 combo.setText(selection); // this should drive the selection 
			 assertEquals(account.getCountry(), selection);
	
			 
		 }
		 
			/**
			 * This scenario tests a simple SWT combo that is bound to a list of 
			 * Country objects. The Country object's name property is listed in the Combo.
			 * 
			 * The Combo's selection is bounded to the Country property of an Account.
			 */
			 public void test_ROCombo_Scenario02_ComboViewer() throws BindingException {
				 
				 
				 // Account label provider will fill the combo with the country
				 cviewer.setLabelProvider(accountLabelProvider);
			     // Bind the ComboViewer's content to the available accounts
			  	 getDbc().bind2(cviewer, new PropertyDescription(catalog, "accounts"), null);
						
				 // Ensure that cv's content now has the catalog's accounts
				 assertEquals(catalog.getAccounts(), getViewerContent(cviewer));						
				 // Ensure that the cv's labels are the same as the account countries
				 assertEquals(getColumn(catalog.getAccounts(), "country"), getComboContent());
				 
				 Account account = AdventureFactory.eINSTANCE.createAccount();				 
				 
				 // Use the Viewers visual Combo (Strings) to set the account's country
				 getDbc().bind2(new PropertyDescription(combo, SWTBindingConstants.SELECTION), 
						       new PropertyDescription(account, "country"), null);
				 
				 // Change the selection of the ComboViewer to all possible accounts, and
				 // verify that the account's Country is being changed correctly.
				 for (Iterator iter = catalog.getAccounts().iterator(); iter.hasNext();) {
						Account selection = (Account)iter.next();
						cviewer.setSelection(new StructuredSelection(selection));
						assertEquals(selection.getCountry(), account.getCountry());						
				 }
						
			 }
			 
			 /**
			  * This test ensure that multiple combos can be bound to the same deomain model
			  */
			 public void test_ROCombo_multipleBindings() throws BindingException {
				 
				 Adventure skiAdventure = SampleData.WINTER_HOLIDAY; // for selection 
				 
				cviewer.setLabelProvider(lodgingLabelProvider); // TODO: need to resolve
				// column binding
				// Bind the ComboViewer's content to the available lodging
				getDbc().bind2(cviewer, new PropertyDescription(catalog, "lodgings"), null);
				
				// Ensure that cv's content now has the catalog's lodgings
				assertEquals(catalog.getLodgings(), getViewerContent(cviewer));
				
				// Ensure that the cv's labels are the same as the lodging descriptions
				assertEquals(getColumn(catalog.getLodgings(), "name"),
				getComboContent());
				
				
				 
								
				
				ComboViewer otherViewer = new ComboViewer(getComposite(), SWT.NONE);
				otherViewer.setLabelProvider(lodgingLabelProvider);
				getDbc().bind2(otherViewer, new PropertyDescription(catalog, "lodgings"), null);
				// Ensure that cv's content now has the catalog's lodgings
				assertEquals(catalog.getLodgings(), getViewerContent(otherViewer));
				
				// Bind both selections to the same thing
				getDbc().bind2(new PropertyDescription(cviewer,SWTBindingConstants.SELECTION),
						       new PropertyDescription(skiAdventure,"defaultLodging"), null);
				getDbc().bind2(new PropertyDescription(otherViewer,SWTBindingConstants.SELECTION),
							   new PropertyDescription(skiAdventure,"defaultLodging"), null);
												
				
				Lodging lodging = (Lodging) catalog.getLodgings().get(0);
				
				// Ensure that setting the selection is driven forward to the other combo
				cviewer.setSelection(new StructuredSelection(lodging));
				assertEquals(((IStructuredSelection)cviewer.getSelection()).getFirstElement(),
						     ((IStructuredSelection)otherViewer.getSelection()).getFirstElement());
				
				// Change the list of one combo, and ensure it updates the other combo
				catalog.getLodgings().remove(lodging);
				assertEquals(getViewerContent(cviewer), getViewerContent(otherViewer));								
				 
			 }
			 
			 
				/**
				 * This scenario tests a simple SWT CCombo that is bound to a list of 
				 * Country objects. The Country object's name property is listed in the Combo.
				 * 
				 * The Combo's selection is bounded to the Country property of an Account.
				 */
				 public void test_ROCombo_SWTCCombo() throws BindingException {
					 
					 // Create a list of Strings for the countries
				    BasicEList list = new BasicEList();
				    for (Iterator iter = catalog.getAccounts().iterator(); iter.hasNext();) 
						list.add(((Account)iter.next()).getCountry());
				    
				    CCombo ccombo = new CCombo(getComposite(), SWT.READ_ONLY | SWT.DROP_DOWN);
					
				    // Bind the combo's content to that of the String based list
				    getDbc().bind2(ccombo, new PropertyDescription(list, null), null);		    
				    assertEquals(Arrays.asList(ccombo.getItems()), list);
				    
				    Account account = (Account) catalog.getAccounts().get(0);
					 
				    // simple Combo's selection bound to the Account's country property
					 getDbc().bind2(new PropertyDescription(ccombo, SWTBindingConstants.SELECTION), 
							 		new PropertyDescription(account, "country"), null);
					 
					 // Drive the combo selection
					 String selection = (String)list.get(2); 			
					 ccombo.setText(selection); // this should drive the selection 
					 assertEquals(account.getCountry(), selection);
			
					 
				 }
				 
					/**
					 * This scenario tests a simple SWT CCombo that is bound to a list of 
					 * Country objects. The Country object's name property is listed in the Combo.
					 * 
					 * The Combo's selection is bounded to the Country property of an Account.
					 */
					 public void test_ROCombo_SWTList() throws BindingException {
						 
						 // Create a list of Strings for the countries
					    BasicEList list = new BasicEList();
					    for (Iterator iter = catalog.getAccounts().iterator(); iter.hasNext();) 
							list.add(((Account)iter.next()).getCountry());
					    
					    org.eclipse.swt.widgets.List swtlist = new org.eclipse.swt.widgets.List(getComposite(), SWT.READ_ONLY | SWT.SINGLE);
						
					    // Bind the combo's content to that of the String based list
					    getDbc().bind2(swtlist, new PropertyDescription(list, null), null);		    
					    assertEquals(Arrays.asList(swtlist.getItems()), list);
					    
					    Account account = (Account) catalog.getAccounts().get(0);
						 
					    // simple Combo's selection bound to the Account's country property
						 getDbc().bind2(new PropertyDescription(swtlist, SWTBindingConstants.SELECTION), 
								 		new PropertyDescription(account, "country"), null);
						 	
						
						 String selection = (String)list.get(2); 			
						 swtlist.select(2); // this should drive the selection
						 swtlist.notifyListeners(SWT.Selection, null);  // Force notification
						 assertEquals(account.getCountry(), selection);
				
						 
					 }				 
	 
}
