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
 *  $RCSfile: TimeEntry.java,v $
 *  $Revision: 1.2 $  $Date: 2005-10-18 17:38:36 $ 
 */
package org.eclipse.ui.examples.rcp.binding.scenarios;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.text.ParseException;
import java.util.StringTokenizer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Spinner;

public class TimeEntry extends Composite {

	private Spinner spin_hours = null;

	private Spinner spin_minutes = null;

	private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(
			this);

	public TimeEntry(Composite parent, int style) {
		super(parent, style);
		initialize();
	}

	private void initialize() {
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		spin_hours = new Spinner(this, SWT.NONE);
		spin_hours.setMaximum(24);
		spin_hours
				.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
					public void widgetSelected(
							org.eclipse.swt.events.SelectionEvent e) {
						fireChanged();
					}
				});
		spin_minutes = new Spinner(this, SWT.NONE);
		spin_minutes.setMaximum(59);
		spin_minutes
				.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
					public void widgetSelected(
							org.eclipse.swt.events.SelectionEvent e) {
						fireChanged();
					}
				});
		this.setLayout(gridLayout);
		setSize(new Point(300, 200));
	}

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		propertyChangeSupport.addPropertyChangeListener(listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		propertyChangeSupport.removePropertyChangeListener(listener);
	}

	private void fireChanged() {
		propertyChangeSupport.firePropertyChange("time", null, getTime());
	}

	public String getTime() {
		StringBuffer result = new StringBuffer();
		result.append(spin_hours.getSelection());
		result.append(":");
		result.append(spin_minutes.getSelection());
		return result.toString();
	}

	public void setTime(String aTime) throws ParseException {
		StringTokenizer tokenizer = new StringTokenizer(aTime, ":");
		int hours = Integer.parseInt((String) tokenizer.nextElement());
		int minutes = Integer.parseInt((String) tokenizer.nextElement());
		spin_hours.setSelection(hours);
		spin_minutes.setSelection(minutes);
	}
}