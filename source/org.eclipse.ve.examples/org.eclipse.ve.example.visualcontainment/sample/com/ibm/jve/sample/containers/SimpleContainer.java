/*
 * Created on Apr 14, 2005
 */
package com.ibm.jve.sample.containers;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import com.ibm.jve.sample.components.UIDropDownComboPart;
import com.ibm.jve.sample.components.UILabelTextPart;
import com.ibm.jve.sample.core.UIContainer;

/**
 * @author doconnor
 */
public class SimpleContainer extends UIContainer {
	private Composite parent = null;

	/**
	 * 
	 */
	public SimpleContainer() {
		super("My Container");
	}


	/* (non-Javadoc)
	 * @see com.ibm.jve.sample.core.UIContainer#create()
	 */
	protected void create() {
		parent = createBaseContainer(1, false);
		Composite c1 = createComposite(parent, 2);
		
		Label name = new Label(c1, SWT.NONE);
		name.setText("Complete Name:");
				
		UILabelTextPart firstName = new UILabelTextPart(c1, "First Name:");
		Label dummy = new Label(c1, SWT.NONE);		
		UILabelTextPart lastName = new UILabelTextPart(c1, "Last Name:");
		Label dummy2 = new Label(c1, SWT.NONE);
		UIDropDownComboPart prefix = new UIDropDownComboPart(c1, "Title:", new String[]{"Mr.", "Mrs.", "Ms.", "Miss.", "Dr."});
		

	}

}
