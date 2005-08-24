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
package org.eclipse.ve.internal.jfc.beaninfo;
/*
 *  $RCSfile: TitledBorderPropertyPage.java,v $
 *  $Revision: 1.4 $  $Date: 2005-08-24 23:38:12 $ 
 */

import java.awt.*;
import java.awt.event.*;
import java.text.MessageFormat;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;

public class TitledBorderPropertyPage
	extends AbstractBorderPropertyPage
	implements DocumentListener, ActionListener, ItemListener {

    /**
	 * Comment for <code>serialVersionUID</code>
	 * 
	 * @since 1.1.0
	 */
	private static final long serialVersionUID = 3133841974545596757L;

	private static final String NULL = "null"; //$NON-NLS-1$

	private boolean built = false;

	private String titleTitle = ""; //$NON-NLS-1$
	private int titleJustification = TitledBorder.DEFAULT_JUSTIFICATION;
	private int titlePosition = TitledBorder.DEFAULT_POSITION;

	private JTextField titleField = null;
	private JComboBox justificationCombo = null;
	private JComboBox positionCombo = null;
	private int[] justificationValues;
	private int[] positionValues;

	private Color titleColor = null;
	private Font titleFont = null;
	private Border titleBorder = null;

	private ColorEditor colorEditor = null;
	private FontEditor fontEditor = null;
	private BorderEditor borderEditor = null;

	private JPanel colorEditPanel = null;
	private JPanel fontEditPanel = null;
	private JPanel borderEditPanel = null;

	private JTextField colorField = null;
	private JTextField fontField = null;
	private JTextField borderField = null;

	private JButton colorButton = null;
	private JButton fontButton = null;
	private JButton borderButton = null;

	public TitledBorderPropertyPage() {
		super();
		initialize();
	}

	private void initialize() {
		this.setName("TitledBorderPropertyPage"); //$NON-NLS-1$
		Dimension size = new Dimension(200, 395);
		this.setMinimumSize(size);
	}

	public void buildPropertyPage() {
		if (!built) {
			this.setBackground(SystemColor.control);
			Insets labelInsets = new Insets(0, 5, 5, 5);
			Insets compInsets = new Insets(0, 5, 10, 5);

			GridBagLayout gb = new GridBagLayout();
			GridBagConstraints c = new GridBagConstraints();
			c.weightx = 1.0;
			c.fill = GridBagConstraints.HORIZONTAL;
			c.anchor = GridBagConstraints.NORTHWEST;
			c.gridx = 0;

			this.setLayout(gb);

			c.gridy = 0;
			c.insets = labelInsets;
	        JLabel titleLabel = new JLabel(VisualBeanInfoMessages.getString("TitleBorder.Title")); //$NON-NLS-1$
			this.add(titleLabel, c);

			c.gridy = 1;
			c.insets = compInsets;
			this.add(getTitleField(), c);

			c.gridy = 2;
			c.insets = labelInsets;
	        JLabel titleJustificationLabel = new JLabel(VisualBeanInfoMessages.getString("TitledBorder.TitleJustification")); //$NON-NLS-1$
			this.add(titleJustificationLabel, c);

			c.gridy = 3;
			c.insets = compInsets;
			this.add(getJustificationCombo(), c);

			c.gridy = 4;
			c.insets = labelInsets;
	        JLabel titlePositionLabel = new JLabel(VisualBeanInfoMessages.getString("TitledBorder.TitlePosition")); //$NON-NLS-1$
			this.add(titlePositionLabel, c);

			c.gridy = 5;
			c.insets = compInsets;
			this.add(getPositionCombo(), c);

			c.gridy = 6;
			c.insets = labelInsets;
	        JLabel fontLabel = new JLabel(VisualBeanInfoMessages.getString("TitledBorder.Font")); //$NON-NLS-1$
			this.add(fontLabel, c);

			c.gridy = 7;
			c.insets = compInsets;
			this.add(getFontEditPanel(), c);

			c.gridy = 8;
			c.insets = labelInsets;
	        JLabel colorLabel = new JLabel(VisualBeanInfoMessages.getString("TitledBorder.Color")); //$NON-NLS-1$
			this.add(colorLabel, c);

			c.gridy = 9;
			c.insets = compInsets;
			this.add(getColorEditPanel(), c);

			c.gridy = 10;
			c.insets = labelInsets;
	        JLabel borderLabel = new JLabel(VisualBeanInfoMessages.getString("TitledBorder.Border")); //$NON-NLS-1$
			this.add(borderLabel, c);

			c.gridy = 11;
			c.insets = compInsets;
			this.add(getBorderEditPanel(), c);
			
			updateFields();
			built = true;
		}
	}

	private JTextField getTitleField() {
		if (titleField == null) {
			titleField = new JTextField();
			titleField.getDocument().addDocumentListener(this);
		}
		return titleField;
	}

    public String getTitleJustificationLabel( int constant ) {
    	String label = constant + ""; //$NON-NLS-1$
    	switch ( constant ) {
    		case TitledBorder.DEFAULT_JUSTIFICATION : label = VisualBeanInfoMessages.getString("TitledBorder.DisplayName.DefaultJustification"); break; //$NON-NLS-1$
	    	case TitledBorder.LEFT                  : label = VisualBeanInfoMessages.getString("TitledBorder.DisplayName.Left"); break; //$NON-NLS-1$
		    case TitledBorder.CENTER                : label = VisualBeanInfoMessages.getString("TitledBorder.DisplayName.Center"); break; //$NON-NLS-1$
    		case TitledBorder.RIGHT                 : label = VisualBeanInfoMessages.getString("TitledBorder.DisplayName.Right"); break; //$NON-NLS-1$
	    	case TitledBorder.LEADING               : label = VisualBeanInfoMessages.getString("TitledBorder.DisplayName.Leading"); break; //$NON-NLS-1$
		    case TitledBorder.TRAILING              : label = VisualBeanInfoMessages.getString("TitledBorder.DisplayName.Trailing"); break; //$NON-NLS-1$
    	}
	    return label;
    }

    public String getTitleJustificationNamedConstant( int constant ) {
	    String name = constant + ""; //$NON-NLS-1$
    	switch ( constant ) {
	    	case TitledBorder.DEFAULT_JUSTIFICATION : name = "javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION"; break; //$NON-NLS-1$
		    case TitledBorder.LEFT                  : name = "javax.swing.border.TitledBorder.LEFT"; break; //$NON-NLS-1$
    		case TitledBorder.CENTER                : name = "javax.swing.border.TitledBorder.CENTER"; break; //$NON-NLS-1$
	    	case TitledBorder.RIGHT                 : name = "javax.swing.border.TitledBorder.RIGHT"; break; //$NON-NLS-1$
		    case TitledBorder.LEADING               : name = "javax.swing.border.TitledBorder.LEADING"; break; //$NON-NLS-1$
    		case TitledBorder.TRAILING              : name = "javax.swing.border.TitledBorder.TRAILING"; break; //$NON-NLS-1$
	    }
    	return name;
    }

    public String getTitlePositionLabel( int constant ) {
	    String label = constant + ""; //$NON-NLS-1$
    	switch ( constant ) {
	    	case TitledBorder.DEFAULT_POSITION : label = VisualBeanInfoMessages.getString("TitledBorder.DisplayName.DefaultPosition"); break; //$NON-NLS-1$
		    case TitledBorder.ABOVE_TOP        : label = VisualBeanInfoMessages.getString("TitledBorder.DisplayName.AboveTop"); break; //$NON-NLS-1$
    		case TitledBorder.TOP              : label = VisualBeanInfoMessages.getString("TitledBorder.DisplayName.Top"); break; //$NON-NLS-1$
	    	case TitledBorder.BELOW_TOP        : label = VisualBeanInfoMessages.getString("TitledBorder.DisplayName.BelowTop"); break; //$NON-NLS-1$
		    case TitledBorder.ABOVE_BOTTOM     : label = VisualBeanInfoMessages.getString("TitledBorder.DisplayName.AboveBottom"); break; //$NON-NLS-1$
    		case TitledBorder.BOTTOM           : label = VisualBeanInfoMessages.getString("TitledBorder.DisplayName.Bottom"); break; //$NON-NLS-1$
	    	case TitledBorder.BELOW_BOTTOM     : label = VisualBeanInfoMessages.getString("TitledBorder.DisplayName.BelowBottom"); break; //$NON-NLS-1$
    	}    
        return label;
    }

    public String getTitlePositionNamedConstant( int constant ) {
	    String name = constant + ""; //$NON-NLS-1$
    	switch ( constant ) {
	    	case TitledBorder.DEFAULT_POSITION : name = "javax.swing.border.TitledBorder.DEFAULT_POSITION"; break; //$NON-NLS-1$
		    case TitledBorder.ABOVE_TOP        : name = "javax.swing.border.TitledBorder.ABOVE_TOP"; break; //$NON-NLS-1$
    		case TitledBorder.TOP              : name = "javax.swing.border.TitledBorder.TOP"; break; //$NON-NLS-1$
	    	case TitledBorder.BELOW_TOP        : name = "javax.swing.border.TitledBorder.BELOW_TOP"; break; //$NON-NLS-1$
		    case TitledBorder.ABOVE_BOTTOM     : name = "javax.swing.border.TitledBorder.ABOVE_BOTTOM"; break; //$NON-NLS-1$
    		case TitledBorder.BOTTOM           : name = "javax.swing.border.TitledBorder.BOTTOM"; break; //$NON-NLS-1$
	       	case TitledBorder.BELOW_BOTTOM     : name = "javax.swing.border.TitledBorder.BELOW_BOTTOM"; break; //$NON-NLS-1$
    	}
        return name;
    }
				
	private JComboBox getJustificationCombo() {
		if (justificationCombo == null) {
			justificationCombo = new JComboBox();
			justificationCombo.setBackground(SystemColor.control);
			justificationCombo.addItem(
				getTitleJustificationLabel(TitledBorder.DEFAULT_JUSTIFICATION));
			justificationCombo.addItem(
				getTitleJustificationLabel(TitledBorder.LEFT));
			justificationCombo.addItem(
				getTitleJustificationLabel(TitledBorder.CENTER));
			justificationCombo.addItem(
				getTitleJustificationLabel(TitledBorder.RIGHT));
			justificationCombo.addItem(
				getTitleJustificationLabel(TitledBorder.LEADING));
			justificationCombo.addItem(
				getTitleJustificationLabel(TitledBorder.TRAILING));

			justificationValues = new int[justificationCombo.getItemCount()];
			justificationValues[0] = TitledBorder.DEFAULT_JUSTIFICATION;
			justificationValues[1] = TitledBorder.LEFT;
			justificationValues[2] = TitledBorder.CENTER;
			justificationValues[3] = TitledBorder.RIGHT;
			justificationValues[4] = TitledBorder.LEADING;
			justificationValues[5] = TitledBorder.TRAILING;

			justificationCombo.addItemListener(this);
		}
		return justificationCombo;
	}

	private JComboBox getPositionCombo() {
		if (positionCombo == null) {
			positionCombo = new JComboBox();
			positionCombo.setBackground(SystemColor.control);
			positionCombo.addItem(
				getTitlePositionLabel(TitledBorder.DEFAULT_POSITION));
			positionCombo.addItem(
				getTitlePositionLabel(TitledBorder.ABOVE_TOP));
			positionCombo.addItem(getTitlePositionLabel(TitledBorder.TOP));
			positionCombo.addItem(
				getTitlePositionLabel(TitledBorder.BELOW_TOP));
			positionCombo.addItem(
				getTitlePositionLabel(TitledBorder.ABOVE_BOTTOM));
			positionCombo.addItem(getTitlePositionLabel(TitledBorder.BOTTOM));
			positionCombo.addItem(
				getTitlePositionLabel(TitledBorder.BELOW_BOTTOM));

			positionValues = new int[positionCombo.getItemCount()];
			positionValues[0] = TitledBorder.DEFAULT_POSITION;
			positionValues[1] = TitledBorder.ABOVE_TOP;
			positionValues[2] = TitledBorder.TOP;
			positionValues[3] = TitledBorder.BELOW_TOP;
			positionValues[4] = TitledBorder.ABOVE_BOTTOM;
			positionValues[5] = TitledBorder.BOTTOM;
			positionValues[6] = TitledBorder.BELOW_BOTTOM;

			positionCombo.addItemListener(this);
		}
		return positionCombo;
	}

	private FontEditor getFontEditor() {
		if (fontEditor == null) {
			fontEditor = new FontEditor();
			fontEditor.setValue(titleFont);
		}
		return fontEditor;
	}

	private JPanel getFontEditPanel() {
		if (fontEditPanel == null) {
			fontEditPanel = new JPanel();
			fontEditPanel.setBackground(SystemColor.control);
			GridBagLayout gb = new GridBagLayout();
			GridBagConstraints c = new GridBagConstraints();
			c.fill = GridBagConstraints.HORIZONTAL;
			c.anchor = GridBagConstraints.NORTHWEST;
			c.gridy = 0;
			fontEditPanel.setLayout(gb);

			c.insets = new Insets(0, 0, 0, 5);
			c.weightx = 1.0;
			c.gridx = 0;
			fontField = new JTextField();
			fontField.setText(getFontEditor().getAsText());
			fontField.setEditable(false);
			fontField.setBackground(SystemColor.control);
			gb.setConstraints(fontField, c);
			fontEditPanel.add(fontField);

			c.insets = new Insets(0, 0, 0, 0);
			c.weightx = 0;
			c.gridx = 1;
			fontButton = new JButton();
			fontButton.setText(VisualBeanInfoMessages.getString("TitledBorder.ChooseButton")); //$NON-NLS-1$
			fontButton.setMargin(new java.awt.Insets(0, 5, 0, 5));
			fontButton.setBackground(SystemColor.control);
			fontButton.addActionListener(this);
			gb.setConstraints(fontButton, c);
			fontEditPanel.add(fontButton);
		}
		return fontEditPanel;
	}

	private ColorEditor getColorEditor() {
		if (colorEditor == null) {
			colorEditor = new ColorEditor();
			colorEditor.setValue(titleColor);
		}
		return colorEditor;
	}

	private JPanel getColorEditPanel() {
		if (colorEditPanel == null) {
			colorEditPanel = new JPanel();
			colorEditPanel.setBackground(SystemColor.control);
			GridBagLayout gb = new GridBagLayout();
			GridBagConstraints c = new GridBagConstraints();
			c.fill = GridBagConstraints.HORIZONTAL;
			c.anchor = GridBagConstraints.NORTHWEST;
			c.gridy = 0;
			colorEditPanel.setLayout(gb);

			c.insets = new Insets(0, 0, 0, 5);
			c.weightx = 1.0;
			c.gridx = 0;
			colorField = new JTextField();
			colorField.setText(getColorEditor().getAsText());
			colorField.setEditable(false);
			colorField.setBackground(SystemColor.control);
			gb.setConstraints(colorField, c);
			colorEditPanel.add(colorField);

			c.insets = new Insets(0, 0, 0, 0);
			c.weightx = 0;
			c.gridx = 1;
			colorButton = new JButton();
			colorButton.setText(VisualBeanInfoMessages.getString("TitledBorder.ChooseButton")); //$NON-NLS-1$
			colorButton.setMargin(new java.awt.Insets(0, 5, 0, 5));
			colorButton.setBackground(SystemColor.control);
			colorButton.addActionListener(this);
			gb.setConstraints(colorButton, c);
			colorEditPanel.add(colorButton);
		}

		return colorEditPanel;
	}

	private BorderEditor getBorderEditor() {
		if (borderEditor == null) {
			borderEditor = new BorderEditor();
			borderEditor.setValue(titleBorder);
		}
		return borderEditor;
	}

	private JPanel getBorderEditPanel() {
		if (borderEditPanel == null) {
			borderEditPanel = new JPanel();
			borderEditPanel.setBackground(SystemColor.control);
			GridBagLayout gb = new GridBagLayout();
			GridBagConstraints c = new GridBagConstraints();
			c.fill = GridBagConstraints.HORIZONTAL;
			c.anchor = GridBagConstraints.NORTHWEST;
			c.gridy = 0;
			borderEditPanel.setLayout(gb);

			c.insets = new Insets(0, 0, 0, 5);
			c.weightx = 1.0;
			c.gridx = 0;
			borderField = new JTextField();
			borderField.setEditable(false);
			borderField.setBackground(SystemColor.control);
			borderEditPanel.add(borderField, c);

			c.insets = new Insets(0, 0, 0, 0);
			c.weightx = 0;
			c.gridx = 1;
			borderButton = new JButton();
			borderButton.setText(VisualBeanInfoMessages.getString("TitledBorder.ChooseButton")); //$NON-NLS-1$
			borderButton.setMargin(new java.awt.Insets(0, 5, 0, 5));
			borderButton.setBackground(SystemColor.control);
			borderButton.addActionListener(this);
			borderEditPanel.add(borderButton, c);
		}

		return borderEditPanel;
	}

	private void updateFields() {
		titleField.setText(titleTitle);
		getJustificationCombo().setSelectedItem(
			getTitleJustificationLabel(titleJustification));
		getPositionCombo().setSelectedItem(
			getTitlePositionLabel(titlePosition));
		fontField.setText(getFontEditor().getAsText());
		colorField.setText(getColorEditor().getAsText());
		borderField.setText(getBorderEditor().getAsText());
	}

	private void updateFont() {
		titleFont = (Font) getFontEditor().getValue();
		fontField.setText(getFontEditor().getAsText());
		firePropertyChange("borderValueChanged", null, getBorderValue()); //$NON-NLS-1$
	}

	private void updateColor() {
		titleColor = (Color) getColorEditor().getValue();
		colorField.setText(getColorEditor().getAsText());
		firePropertyChange("borderValueChanged", null, getBorderValue()); //$NON-NLS-1$
	}

	private void updateBorder() {
		titleBorder = (Border) getBorderEditor().getValue();
		borderField.setText(getBorderEditor().getAsText());
		firePropertyChange("borderValueChanged", null, getBorderValue()); //$NON-NLS-1$
	}
	
	public String escapeString( String str ) {
		StringBuffer s = new StringBuffer( str );
		char c;
		for ( int i = 0; i < s.length(); i++ ) {
			c = s.charAt(i);
			if ( c == '"' || c == '\\' )  {
			    s.insert( i, '\\' );
			    i++;
			}
		}
		return s.toString();
	}

	// Event handling for border changes

	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		int state;

		if (source == fontButton) {
			state =
				SubEditorJDialog.showSubEditorJDialog(
					getTopLevelAncestor(),
					getFontEditor().getCustomEditor());
			if (state == SubEditorJDialog.OK) {
				updateFont();
			}
		} else if (source == colorButton) {
			state =
				SubEditorJDialog.showSubEditorJDialog(
					getTopLevelAncestor(),
					getColorEditor().getCustomEditor());
			if (state == SubEditorJDialog.OK) {
				updateColor();
			}
		} else if (source == borderButton) {
			state =
				SubEditorJDialog.showSubEditorJDialog(
					getTopLevelAncestor(),
					getBorderEditor().getCustomEditor());
			if (state == SubEditorJDialog.OK) {
				updateBorder();
			}

		}
	}

	public void itemStateChanged(ItemEvent e) {
		if (e.getSource() == getJustificationCombo()) {
			int index = getJustificationCombo().getSelectedIndex();
			if (index >= 0 && index <= justificationValues.length) {
				titleJustification = justificationValues[index];
			}
		} else if (e.getSource() == getPositionCombo()) {
			int index = getPositionCombo().getSelectedIndex();
			if (index >= 0 && index <= positionValues.length) {
				titlePosition = positionValues[index];
			}
		}

		firePropertyChange("borderValueChanged", null, getBorderValue()); //$NON-NLS-1$
	}

	private void updateHandle(DocumentEvent e) {
		if (e.getDocument() == titleField.getDocument()) {
			titleTitle = titleField.getText();
			firePropertyChange("borderValueChanged", null, getBorderValue()); //$NON-NLS-1$
		}
	}

	public void insertUpdate(DocumentEvent e) {
		updateHandle(e);
	}

	public void removeUpdate(DocumentEvent e) {
		updateHandle(e);
	}

	public void changedUpdate(DocumentEvent e) {
		updateHandle(e);
	}

	// Implementing abstract methods from AbstractBorderPropertyPage

	public boolean okToSetBorder(Border aBorder) {
		if (aBorder instanceof TitledBorder) {
			TitledBorder fBorder = (TitledBorder) aBorder;
			titleTitle = fBorder.getTitle();
			titleJustification = fBorder.getTitleJustification();
			titlePosition = fBorder.getTitlePosition();
			titleColor = fBorder.getTitleColor();
			titleFont = fBorder.getTitleFont();
			titleBorder = fBorder.getBorder();

			if (titleColor != null) {
				getColorEditor().setValue(titleColor);
			}
			if (titleFont != null) {
				getFontEditor().setValue(titleFont);
			}
			if (titleBorder != null) {
				getBorderEditor().setValue(titleBorder);
			}
			return true;
		}
		return false;
	}

	public Border getBorderValue() {
		Border aBorder =
			BorderFactory.createTitledBorder(
				titleBorder,
				titleTitle,
				titleJustification,
				titlePosition,
				titleFont,
				titleColor);
		return aBorder;
	}

	public String getDisplayName() {
		return MessageFormat.format(VisualBeanInfoMessages.getString("TitledBorder.DisplayName.Title(TitleString,JustificationString,TitlePositionString,TitleFont,TitleColor)"), //$NON-NLS-1$
		new Object[] {
			titleTitle,
			getTitleJustificationLabel(titleJustification),
			getTitlePositionLabel(titlePosition),
			getFontEditor().getAsText(),
			getColorEditor().getAsText()});
	}

	public String getJavaInitializationString() {
		StringBuffer sb = new StringBuffer( "javax.swing.BorderFactory.createTitledBorder(" ); //$NON-NLS-1$
		
		// Sub border param
		sb.append( (getBorderEditor().getValue() == null ? NULL : getBorderEditor().getJavaInitializationString()) ); //$NON-NLS-1$
		
		// Title param
		sb.append( ", \"" ); //$NON-NLS-1$
		sb.append( escapeString( titleTitle ) );
		sb.append( "\", " ); //$NON-NLS-1$
		
		sb.append( getTitleJustificationNamedConstant(titleJustification) );
		sb.append( ", " ); //$NON-NLS-1$
		sb.append( getTitlePositionNamedConstant(titlePosition) );
		sb.append( ", " ); //$NON-NLS-1$
		sb.append( (getFontEditor().getValue() == null ? NULL : getFontEditor().getJavaInitializationString()) ); //$NON-NLS-1$
        sb.append( ", " ); //$NON-NLS-1$
		sb.append( (getColorEditor().getValue() == null ? NULL : getColorEditor().getJavaInitializationString()) ); //$NON-NLS-1$
		sb.append( ")" ); //$NON-NLS-1$
		
		return sb.toString();
	}
}
