/*
 * Created on Apr 14, 2005 
 */
package com.ibm.jve.sample.components;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import com.ibm.jve.sample.core.UIPartBase;

/**
 * @author doconnor 
 */
public class UIDropDownComboPart extends UIPartBase {	
	private String[] comboItems = null;
	/**
	 * @param parent
	 * @param title
	 */
	public UIDropDownComboPart(Composite parent, String title, String[] comboItems) {
		super(parent, title);		
		this.comboItems = comboItems;
		createContents();
	}

	/**
	 * 
	 */
	private void createContents() {
		initializeContainer(2);
		createLabel();
		Combo combo = new Combo(getContainer(), SWT.READ_ONLY | SWT.DROP_DOWN);
		
		if(comboItems != null &&comboItems.length > 0){
			combo.setItems(comboItems);
		}
		
		setControls(new Control[]{combo});		
	}

}