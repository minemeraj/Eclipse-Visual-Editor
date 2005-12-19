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
package org.eclipse.ui.examples.rcp.binding.scenarios;

import java.beans.PropertyChangeSupport;
import java.util.Date;

import org.eclipse.jface.databinding.DataBinding;
import org.eclipse.jface.databinding.IDataBindingContext;
import org.eclipse.jface.databinding.Property;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class NullBinding extends Composite {

	private static class Person {
		private String name;

		private Integer age;

		private Date born;

		private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(
				Person.this);

		public Person(String name, Integer age, Date born) {
			this.name = name;
			this.age = age;
			this.born = born;
		}

		public Integer getAge() {
			return age;
		}

		public void setAge(Integer age) {
			Integer oldValue = this.age;
			this.age = age;
			propertyChangeSupport.firePropertyChange("age", oldValue, age);
		}

		public Date getBorn() {
			return born;
		}

		public void setBorn(Date born) {
			Date oldValue = this.born;
			this.born = born;
			propertyChangeSupport.firePropertyChange("born", oldValue, born);
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			String oldValue = this.name;
			this.name = name;
			propertyChangeSupport.firePropertyChange("name", oldValue, name);
		}
	}

	private IDataBindingContext dbc;

	private Group group = null;

	public NullBinding(Composite parent, int style) {
		super(parent, style);
		initialize();
	}

	/**
	 * This method initializes sShell
	 * 
	 */
	private void initialize() {
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		setLayout(gridLayout);
		dbc = DataBinding.createContext(this);
		createGroup();
		this.setSize(new org.eclipse.swt.graphics.Point(444, 215));
	}

	/**
	 * This method initializes group
	 * 
	 */
	private void createGroup() {
		GridLayout gridLayout1 = new GridLayout();
		gridLayout1.numColumns = 3;
		GridData gridData2 = new org.eclipse.swt.layout.GridData();
		gridData2.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData2.horizontalSpan = 2;
		gridData2.grabExcessVerticalSpace = true;
		gridData2.grabExcessHorizontalSpace = true;
		gridData2.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		group = new Group(this, SWT.NONE);
		group.setText("Winter holiday");
		group.setLayout(gridLayout1);
		group.setLayoutData(gridData2);

		Person person = new Person(null, null, null);
		createLabelAndText(person, "name");
		createLabelAndText(person, "age");
		createLabelAndText(person, "born");
	}

	private void createLabelAndText(Object object, String attributeName) {
		Label label = new Label(group, SWT.NONE);
		label.setText(attributeName.substring(0, 1).toUpperCase() + attributeName.substring(1) + ":");
		Text text = new Text(group, SWT.BORDER);
		GridData gridData = new org.eclipse.swt.layout.GridData();
		gridData.grabExcessHorizontalSpace = true;
		gridData.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		gridData.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		text.setLayoutData(gridData);
		Label verifyLabel = new Label(group, SWT.NONE);
		GridData gridData2 = new GridData(100,-1);
		dbc.bind(text, new Property(object, attributeName), null);
		dbc.bind(verifyLabel, new Property(object, attributeName), null);
	}
}
