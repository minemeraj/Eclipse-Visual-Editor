/*******************************************************************************
 * Copyright (c) 2001, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: NamedColorChooserPanel.java,v $
 *  $Revision: 1.6 $  $Date: 2005-08-24 23:38:11 $ 
 */
package org.eclipse.ve.internal.jfc.beaninfo;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

import javax.swing.*;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import javax.swing.event.*;

public class NamedColorChooserPanel extends AbstractColorChooserPanel {
	/**
	 * Comment for <code>serialVersionUID</code>
	 * 
	 * @since 1.1.0
	 */
	private static final long serialVersionUID = -6002127476932213193L;

	private static java.util.ResourceBundle resabtedit = java.util.ResourceBundle.getBundle("org.eclipse.ve.internal.jfc.beaninfo.vceedit");  //$NON-NLS-1$

	public static final Color[] basicColorValues =
		{Color.black, Color.blue, Color.cyan, Color.darkGray, Color.gray, Color.green, Color.lightGray, 
		Color.magenta, Color.orange, Color.pink, Color.red, Color.white, Color.yellow};

	public static final String[] basicColorNames =
		{resabtedit.getString("black"), resabtedit.getString("blue"), resabtedit.getString("cyan"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		resabtedit.getString("darkGray"), resabtedit.getString("gray"), resabtedit.getString("green"),  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		resabtedit.getString("lightGray"), resabtedit.getString("magenta"),  //$NON-NLS-1$ //$NON-NLS-2$
		resabtedit.getString("orange"), resabtedit.getString("pink"), resabtedit.getString("red"),  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		resabtedit.getString("white"), resabtedit.getString("yellow") }; //$NON-NLS-1$ //$NON-NLS-2$

    public static final String basicColorConstantPrefix = "java.awt.Color."; //$NON-NLS-1$
	public static final String[] basicColorConstants =
		{"black", "blue", "cyan", "darkGray", "gray", "green", "lightGray", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$
		"magenta", "orange", "pink", "red", "white", "yellow"}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
		
	public static final SystemColor[] systemColorValues =
		{SystemColor.activeCaption, SystemColor.activeCaptionBorder, SystemColor.activeCaptionText, 
		SystemColor.control, SystemColor.controlDkShadow, SystemColor.controlHighlight, 
		SystemColor.controlLtHighlight, SystemColor.controlShadow, SystemColor.controlText, 
		SystemColor.desktop, SystemColor.inactiveCaption, SystemColor.inactiveCaptionBorder,
		SystemColor.inactiveCaptionText, SystemColor.info, SystemColor.infoText, SystemColor.menu, 
		SystemColor.menuText, SystemColor.scrollbar, SystemColor.text, SystemColor.textHighlight, 
		SystemColor.textHighlightText, SystemColor.textInactiveText, SystemColor.textText, 
		SystemColor.window, SystemColor.windowBorder, SystemColor.windowText};

	public static final String[] systemColorNames = 
		{resabtedit.getString("activeCaption"), resabtedit.getString("activeCaptionBorder"), resabtedit.getString("activeCaptionText"),  //$NON-NLS-3$ //$NON-NLS-2$ //$NON-NLS-1$
		resabtedit.getString("control"), resabtedit.getString("controlDkShadow"), resabtedit.getString("controlHighlight"),  //$NON-NLS-3$ //$NON-NLS-2$ //$NON-NLS-1$
		resabtedit.getString("controlLtHighlight"), resabtedit.getString("controlShadow"), resabtedit.getString("controlText"),  //$NON-NLS-3$ //$NON-NLS-2$ //$NON-NLS-1$
		resabtedit.getString("desktop"), resabtedit.getString("inactiveCaption"), resabtedit.getString("inactiveCaptionBord"), //$NON-NLS-3$ //$NON-NLS-2$ //$NON-NLS-1$
		resabtedit.getString("inactiveCaptionText"), resabtedit.getString("info"), resabtedit.getString("infoText"), resabtedit.getString("menu"),  //$NON-NLS-4$ //$NON-NLS-3$ //$NON-NLS-2$ //$NON-NLS-1$
		resabtedit.getString("menuText"), resabtedit.getString("scrollbar"), resabtedit.getString("text"), resabtedit.getString("textHighlight"),  //$NON-NLS-4$ //$NON-NLS-3$ //$NON-NLS-2$ //$NON-NLS-1$
		resabtedit.getString("textHighlightText"), resabtedit.getString("textInactiveText"), resabtedit.getString("textText"),  //$NON-NLS-3$ //$NON-NLS-2$ //$NON-NLS-1$
		resabtedit.getString("window"), resabtedit.getString("windowBorder"), resabtedit.getString("windowText")}; //$NON-NLS-3$ //$NON-NLS-2$ //$NON-NLS-1$

    public static final String systemColorConstantPrefix = "java.awt.SystemColor."; //$NON-NLS-1$
	public static final String[] systemColorConstants =
		{"activeCaption", "activeCaptionBorder", "activeCaptionText", //$NON-NLS-3$//$NON-NLS-2$//$NON-NLS-1$
		"control", "controlDkShadow", "controlHighlight", //$NON-NLS-3$//$NON-NLS-2$//$NON-NLS-1$
		"controlLtHighlight", "controlShadow", "controlText", //$NON-NLS-3$//$NON-NLS-2$//$NON-NLS-1$
		"desktop", "inactiveCaption", "inactiveCaptionBorder",//$NON-NLS-3$//$NON-NLS-2$//$NON-NLS-1$
		"inactiveCaptionText", "info", "infoText", "menu", //$NON-NLS-4$//$NON-NLS-3$//$NON-NLS-2$//$NON-NLS-1$
		"menuText", "scrollbar", "text", "textHighlight", //$NON-NLS-4$//$NON-NLS-3$//$NON-NLS-2$//$NON-NLS-1$
		"textHighlightText", "textInactiveText", "textText", //$NON-NLS-3$//$NON-NLS-2$//$NON-NLS-1$
		"window", "windowBorder", "windowText"};//$NON-NLS-3$//$NON-NLS-2$//$NON-NLS-1$
		
	private static List basicColors = Arrays.asList(basicColorValues);
	private JList basicColorsList = null;
	private static List systemColors = Arrays.asList(systemColorValues);
	private JList systemColorsList = null;
	
	private static final char UNKNOWN_CHAR = '?';
	private static final int UNKNOWN_INDEX = -2;
	
	private static char mnemonic = UNKNOWN_CHAR;
	private static int mnemonicIndex = UNKNOWN_INDEX;
	
	private ChangeListener colorListener = new ChangeListener() {
		public void stateChanged(ChangeEvent e) {
			updateListSelection(getColorFromModel());
		}
	};
	
	public void installChooserPanel(JColorChooser cc) {
		cc.getSelectionModel().addChangeListener(colorListener);
		super.installChooserPanel(cc);
	}
	
	public void uninstallChooserPanel(JColorChooser cc) {
		cc.getSelectionModel().removeChangeListener(colorListener);
		super.uninstallChooserPanel(cc);
	}

	/**
	 * @see javax.swing.colorchooser.AbstractColorChooserPanel#updateChooser()
	 */
	public void updateChooser() {
		updateListSelection(getColorFromModel());
	}

	public void updateListSelection(Color newColor) {
		if (newColor == null) {
			return;
		}

		if (isBasicColor(newColor)) {
			Color oldColor = (Color) basicColorsList.getSelectedValue();
			if (oldColor == null || !oldColor.equals(newColor)) {
				basicColorsList.setSelectedValue(newColor, true);
			}
		} else {
			basicColorsList.clearSelection();
		}

		if (isSystemColor(newColor)) {
			SystemColor oldColor =
				(SystemColor) basicColorsList.getSelectedValue();
			if (oldColor == null || !oldColor.equals(newColor)) {
				systemColorsList.setSelectedValue(newColor, true);
				basicColorsList.clearSelection();
			}
		} else {
			systemColorsList.clearSelection();
		}
	}

	/**
	 * @see javax.swing.colorchooser.AbstractColorChooserPanel#buildChooser()
	 */
	protected void buildChooser() {
		basicColorsList = new JList(basicColorValues);
		basicColorsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		basicColorsList.setCellRenderer(
			new ColorCellRenderer(basicColorNames, basicColorValues));
		basicColorsList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				Color selected = (Color) basicColorsList.getSelectedValue();
				if (selected != null) {
					getColorSelectionModel().setSelectedColor(selected);
				}
			}
		});
		systemColorsList = new JList(systemColorValues);
		systemColorsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		systemColorsList.setCellRenderer(
			new ColorCellRenderer(systemColorNames, systemColorValues));
		systemColorsList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				SystemColor selected =
					(SystemColor) systemColorsList.getSelectedValue();
				if (selected != null) {
					getColorSelectionModel().setSelectedColor(selected);
				}
			}
		});
		this.setLayout(new GridLayout(1, 2, 5, 5));

		JPanel basicPane = new JPanel();
		basicPane.setLayout(new BorderLayout(5, 5));
		basicPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), VisualBeanInfoMessages.getString("NamedColorChooserPanel.BasicTitle"))); //$NON-NLS-1$
		basicPane.add(new JScrollPane(basicColorsList), BorderLayout.CENTER);
		this.add(basicPane);

		JPanel systemPane = new JPanel();
		systemPane.setLayout(new BorderLayout(5, 5));
		systemPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), VisualBeanInfoMessages.getString("NamedColorChooserPanel.SystemTitle"))); //$NON-NLS-1$
		systemPane.add(new JScrollPane(systemColorsList), BorderLayout.CENTER);
		this.add(systemPane);
	}

	public boolean isBasicColorSelected() {
		return basicColorsList.getSelectedIndex() != -1;
	}

	public static boolean isBasicColor(Color c) {
		return basicColors.contains(c);
	}

	public boolean isSystemColorSelected() {
		return systemColorsList.getSelectedIndex() != -1;
	}

	public static boolean isSystemColor(Color c) {
		return c instanceof SystemColor;
	}

	public String getColorName() {
		Color c = getColorFromModel();
		return getColorName(c);
	}

	public static String getColorName(Color c) {
		if (isBasicColor(c)) {
			return basicColorNames[basicColors.indexOf(c)];
		} else if (isSystemColor(c)) {
			return systemColorNames[systemColors.indexOf(c)];
		} else {
			return ""; //$NON-NLS-1$
		}
	}

	public String getConstant() {
		Color c = getColorFromModel();
		return getConstant(c);
	}

	public String getConstant(Color c) {
		if (isBasicColor(c)) {
			return basicColorConstantPrefix
				+ basicColorConstants[basicColors.indexOf(c)];
		} else if (isSystemColor(c)) {
			return systemColorConstantPrefix
				+ systemColorConstants[systemColors.indexOf(c)];
		} else {
			return ""; //$NON-NLS-1$
		}
	}

	/**
	 * @see javax.swing.colorchooser.AbstractColorChooserPanel#getDisplayName()
	 */
	public String getDisplayName() {
		return VisualBeanInfoMessages.getString("NamedColorChooserPanel.PanelName"); //$NON-NLS-1$
	}
	
	/**
	 * @see javax.swing.colorchooser.AbstractColorChooserPanel#getMnemonic()
	 */
	public int getMnemonic() {
		if ( mnemonic == UNKNOWN_CHAR ) {
			mnemonic = Character.toUpperCase(VisualBeanInfoMessages.getString("NamedColorChooserPanel.PanelName.Mnemonic").charAt(0)); //$NON-NLS-1$
		}
		return mnemonic;
	}
	
	/**
	 * @see javax.swing.colorchooser.AbstractColorChooserPanel#getDisplayedMnemonicIndex()
	 */
	public int getDisplayedMnemonicIndex() {
		if ( mnemonicIndex == UNKNOWN_INDEX ) {
			// Try to find the first occurance of the mnemonic character.
			// Search for an Upper Case first, then Lower Case if not found
			mnemonicIndex = getDisplayName().indexOf(getMnemonic()) == -1 ? 
					        getDisplayName().indexOf(Character.toLowerCase((char)getMnemonic())) :
					        getDisplayName().indexOf(getMnemonic());
		}
		return mnemonicIndex;
	}

	/**
	 * @see javax.swing.colorchooser.AbstractColorChooserPanel#getSmallDisplayIcon()
	 */
	public Icon getSmallDisplayIcon() {
		return null;
	}

	/**
	 * @see javax.swing.colorchooser.AbstractColorChooserPanel#getLargeDisplayIcon()
	 */
	public Icon getLargeDisplayIcon() {
		return null;
	}
}
