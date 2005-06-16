package org.eclipse.ve.examples.java.beaninfo;
/*******************************************************************************
 * Copyright (c)  2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/


import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.Customizer;

import javax.swing.*;

import org.eclipse.ve.examples.java.vm.UsesJPanelForCustomizer;

public class SwingJPanelCustomizer extends JPanel implements Customizer {

	/**
	 * Comment for <code>serialVersionUID</code>
	 * 
	 * @since 1.1.0
	 */
	private static final long serialVersionUID = -6389798996458770035L;
	UsesJPanelForCustomizer bean;
	private JTextField text;
	/**
	 * This is the default constructor
	 */
	public SwingJPanelCustomizer() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private  void initialize() {
		setLayout(new GridBagLayout());
		// Add a label
		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.WEST;
		JLabel label = new JLabel("Title:");
		add(label,c);
		text = new JTextField();
		text.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				updateTitle();
			}			
		});
		c.gridx = 1;
		c.weightx = 1.0;
		c.fill = GridBagConstraints.HORIZONTAL;
		add(text,c);
		setSize(300,50);
	}
	
	

	protected void updateTitle() {
		bean.setTitle(text.getText());
		firePropertyChange("title",null,text.getText());
	}

	public void setObject(Object aBean) {
		bean = (UsesJPanelForCustomizer) aBean;
		if (text != null){
			text.setText(bean.getTitle());
		}
	}
}
