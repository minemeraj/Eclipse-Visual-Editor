/*******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: FormLayoutLayoutComposite.java,v $
 *  $Revision: 1.2 $  $Date: 2006-05-17 20:15:53 $ 
 */
package org.eclipse.ve.internal.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Label;

public class FormLayoutLayoutComposite extends Composite {

	private FormLayoutLayoutPage parentPage;

	private Spinner spacingSpinner;
	private Spinner heightSpinner;
	private Spinner widthSpinner;

	private boolean initialized = false;

	protected ModifyListener spinnerModify = new ModifyListener() {

		public void modifyText(ModifyEvent e) {
			if (initialized) {
				Spinner spinner = (Spinner) e.widget;
				if (spinner == spacingSpinner)
					parentPage.propertyChanged(FormLayoutLayoutPage.SPACING_CHANGED, spacingSpinner.getSelection());
				else if (spinner == heightSpinner)
					parentPage.propertyChanged(FormLayoutLayoutPage.MARGIN_HEIGHT_CHANGED, heightSpinner.getSelection());
				else if (spinner == widthSpinner)
					parentPage.propertyChanged(FormLayoutLayoutPage.MARGIN_WIDTH_CHANGED, widthSpinner.getSelection());
			}
		}
	};

	public FormLayoutLayoutComposite(FormLayoutLayoutPage parentPage, Composite parent, int style) {
		super(parent, style);
		this.parentPage = parentPage;
		initialize();
	}

	private void initialize() {

		this.setLayout(new RowLayout());
		Group spaceGroup = new Group(this, SWT.NONE);
		GridLayout g3 = new GridLayout();
		g3.numColumns = 2;
		spaceGroup.setLayout(g3);
		spaceGroup.setText(SWTMessages.FormLayoutLayoutComposite_spacingTitle); 

		Label l2 = new Label(spaceGroup, SWT.NONE);
		l2.setText(SWTMessages.FormLayoutLayoutComposite_spacing); 
		spacingSpinner = new Spinner(spaceGroup, SWT.BORDER);
		spacingSpinner.addModifyListener(spinnerModify);
		GridData gd3 = new GridData();
		gd3.grabExcessHorizontalSpace = true;
		l2.setLayoutData(gd3);

		Label l4 = new Label(spaceGroup, SWT.NONE);
		l4.setText(SWTMessages.FormLayoutLayoutComposite_marginWidth); 
		widthSpinner = new Spinner(spaceGroup, SWT.BORDER);
		widthSpinner.addModifyListener(spinnerModify);

		Label l5 = new Label(spaceGroup, SWT.NONE);
		l5.setText(SWTMessages.FormLayoutLayoutComposite_marginHeight); 
		heightSpinner = new Spinner(spaceGroup, SWT.BORDER);
		heightSpinner.addModifyListener(spinnerModify);
		this.setSize(new Point(188, 124));
	}

	/*
	 * Initial values from the parent page. Order is
	 * 1. Spacing
	 * 2. Margin height
	 * 3. Margin width
	 */
	protected void setInitialValues (Object [] values) {
		if (values == null || values.length != 3)
			return;
		spacingSpinner.setSelection(((Integer)values[0]).intValue());
		heightSpinner.setSelection(((Integer)values[1]).intValue());
		widthSpinner.setSelection(((Integer)values[2]).intValue());
		initialized = true;
	}


} // @jve:decl-index=0:visual-constraint="10,10"
