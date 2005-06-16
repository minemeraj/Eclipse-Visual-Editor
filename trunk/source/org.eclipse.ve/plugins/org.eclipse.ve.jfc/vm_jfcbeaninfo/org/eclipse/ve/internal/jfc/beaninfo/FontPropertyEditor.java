/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: FontPropertyEditor.java,v $
 *  $Revision: 1.5 $  $Date: 2005-06-16 17:46:03 $ 
 */
package org.eclipse.ve.internal.jfc.beaninfo;

import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;

public class FontPropertyEditor extends JPanel {
	/**
	 * Comment for <code>serialVersionUID</code>
	 * 
	 * @since 1.1.0
	 */
	private static final long serialVersionUID = -1100929504872834780L;
	private static java.util.ResourceBundle resabtedit = java.util.ResourceBundle.getBundle("org.eclipse.ve.internal.jfc.beaninfo.vceedit");  //$NON-NLS-1$
	private Font fontValue;

	private final static Font DEFAULT_VALUE = new Font("Dialog", Font.PLAIN, 12); //$NON-NLS-1$
		
	private final static String[] styleNames = { resabtedit.getString("plain"), resabtedit.getString("bold"), resabtedit.getString("italic"), resabtedit.getString("bold_italic") }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
	private final static Integer[] sizeArray = { new Integer(8), new Integer(10), new Integer(12), new Integer(14), new Integer(18), new Integer(24), new Integer(36), new Integer(48), new Integer(72) };
	
	private static Vector fontNames = null;
	private static Vector lowerCaseFontNames = null;
	private static Vector pointSizes = null;
	
    private JTextField namesText = null;
    private JList namesList = null;
    private JList stylesList = null;
    private JTextField stylesText = null;
    private WholeNumberField sizeText = null;
    private JList sizeList = null;
    private JTextArea previewText = null;

    private boolean searchSelect = false;
    private boolean nameSelect = false;
    
	/**
	 * This method initializes 
	 * 
	 */
	public FontPropertyEditor() {
        this(null);
	}
	
	public FontPropertyEditor(Font startFont) {
		super();
		getPointSizes();
		initialize();
		setFontValue(startFont);
	}
	
	public Vector getPointSizes() {
		if ( pointSizes == null ) {
			pointSizes = new Vector( Arrays.asList( sizeArray ) );
		}
		return pointSizes;
	}
	
	public Vector getFontNames() {
		if ( fontNames == null ) {
			fontNames = new Vector( Arrays.asList( GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames() ) );
			Collections.sort( fontNames, new StringIgnoreCaseComparator() );
		}
		return fontNames;
	}
	
	public Vector getLowerCaseFontNames() {
		if ( lowerCaseFontNames == null ) {
            lowerCaseFontNames = new Vector();
            Iterator itr = getFontNames().iterator();
            while ( itr.hasNext() ) {
        	     lowerCaseFontNames.add( ((String)itr.next()).toLowerCase() );
             }
		}
		return lowerCaseFontNames;
	}	
	
	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
        this.setPreferredSize(new Dimension(430, 250));
        this.setLayout(new BorderLayout());
        this.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
    	
    	JSplitPane editPreviewSplit = new JSplitPane( JSplitPane.VERTICAL_SPLIT );
    	editPreviewSplit.setOneTouchExpandable(true);
    	
		JPanel optionsPanel = new JPanel();
		GridBagLayout gb = new GridBagLayout();
		optionsPanel.setLayout(gb);
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets( 0, 0, 5, 5 );
		c.gridy = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weighty = 0.0;
		
		c.gridx = 0;
		c.weightx = 1.0;
        optionsPanel.add(new JLabel(resabtedit.getString("Name")), c); //$NON-NLS-1$
			
		c.gridx = 1;
		c.weightx = 0.0;
		optionsPanel.add(new JLabel(resabtedit.getString("Style")), c); //$NON-NLS-1$
		
		c.gridx = 2;
		c.weightx = 0.0;
		optionsPanel.add(new JLabel(resabtedit.getString("Size")), c); //$NON-NLS-1$
			
   		c.gridy = 1;
   		c.weighty = 1.0;
   		c.fill = GridBagConstraints.BOTH;
  		    
   		c.gridx = 0;
   		c.weightx = 1.0;
		optionsPanel.add(getNamesPanel(), c);

		c.gridx = 1;
		c.weightx = 0.0;
		optionsPanel.add(getStylesPanel(), c);

		c.insets = new Insets( 0, 0, 5, 0 );
		c.gridx = 2;
   		c.weightx = 0.0;
		optionsPanel.add(getSizePanel(), c);
		
		optionsPanel.setMinimumSize(new Dimension(400, 150));
		optionsPanel.setBorder(BorderFactory.createEmptyBorder(0,5,0,5));

        editPreviewSplit.setTopComponent( optionsPanel );
		//this.add(optionsPanel, BorderLayout.CENTER);
		
		JScrollPane previewPane = new JScrollPane();
 	    previewPane.setViewportView(getPreviewText());
		previewPane.setMinimumSize(new Dimension(3,75));
        editPreviewSplit.setBottomComponent( previewPane );
		//this.add(previewPane, BorderLayout.SOUTH);
		
		editPreviewSplit.setResizeWeight(0.75);
		this.add( editPreviewSplit, BorderLayout.CENTER );
		
		recursiveSetBackground( this, java.awt.SystemColor.control );
		
		initializeOptions();
	}
	
	/**
	 * This method initializes previewText
	 * 
	 * @return javax.swing.JTextArea
	 */
	private JTextArea getPreviewText() {
		if(previewText == null) {
			previewText = new javax.swing.JTextArea();
			previewText.setRows(1);
			previewText.setText(resabtedit.getString("font.previewtext")); //$NON-NLS-1$
			previewText.setMargin(new java.awt.Insets(5,5,5,5));
		}
		return previewText;
	}

	/**
	 * This method initializes namesPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getNamesPanel() {
		JPanel namesPanel = new JPanel();
		GridBagLayout gb = new GridBagLayout();
		namesPanel.setLayout(gb);
		GridBagConstraints c = new GridBagConstraints();	
		c.gridx = 0;
	    c.weightx = 1.0;

		c.insets = new Insets( 0, 0, 5, 0 );
        c.gridy = 0;
		c.weighty = 0.0;
		c.fill = GridBagConstraints.HORIZONTAL;
		namesPanel.add(getNamesText(), c);

		c.insets = new Insets( 0, 0, 0, 0 );			
		c.gridy = 1;
		c.weighty = 1.0;
		c.fill = GridBagConstraints.BOTH;
		JScrollPane namesScrollPane = new JScrollPane();
		namesScrollPane.setViewportView(getNamesList());
		namesPanel.add(namesScrollPane, c);
		
		namesPanel.setPreferredSize(new Dimension(150,100));
		namesPanel.setMinimumSize(new Dimension(150,100));		
		return namesPanel;
	}
	
	private JTextField getNamesText() {
		if ( namesText == null ) {
			namesText = new JTextField();
			namesText.getDocument().addDocumentListener( new DocumentListener() {
                public void updateSelection() {
					if (!nameSelect) {
						int newIndex =
							searchFontNames(getNamesText().getText());

						if (getNamesList().getSelectedIndex() != newIndex) {
							searchSelect = true;
							getNamesList().setSelectedIndex(newIndex);
							getNamesList().ensureIndexIsVisible(newIndex);
							searchSelect = false;
						}
                	}
                }
                public void changedUpdate(DocumentEvent e) {
                    // Do nothing
                }
                public void insertUpdate(DocumentEvent e) {
 	                updateSelection();
                }
                public void removeUpdate(DocumentEvent e) {
  	                updateSelection();
                }
	       	});
	       	namesText.addFocusListener(new FocusAdapter() {
	       		public void focusGained(FocusEvent e) {
	       			namesText.selectAll();
	       		}
	       	});
    	}			
		return namesText;
	}


	/**
	 * This method initializes namesList
	 * 
	 * @return javax.swing.JList
	 */
	private JList getNamesList() {
		if(namesList == null) {
			namesList = new JList();
			namesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			namesList.addListSelectionListener( new ListSelectionListener() {
                public void valueChanged(ListSelectionEvent e) {
                	String newName = (String)getNamesList().getSelectedValue();
                	if ( newName != null ) {
                		checkNull();
                	    fontValue = new Font( newName, fontValue.getStyle(), fontValue.getSize() );
                	    if ( ! searchSelect ) {
                	    	nameSelect = true;
                	    	getNamesText().setText( newName );
                	    	nameSelect = false;
                	    }
                	    updatePreview();
                	}
                }
    	    });
		}
		return namesList;
	}

	/**
	 * This method initializes stylesPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getStylesPanel() {
		JPanel stylesPanel = new JPanel();
		GridBagLayout gb = new GridBagLayout();
		stylesPanel.setLayout(gb);
		GridBagConstraints c = new GridBagConstraints();	
		c.gridx = 0;
	    c.weightx = 1.0;

		c.insets = new Insets( 0, 0, 5, 0 );
        c.gridy = 0;
		c.weighty = 0.0;
		c.fill = GridBagConstraints.HORIZONTAL;
		stylesPanel.add(getStylesText(), c);

		c.insets = new Insets( 0, 0, 0, 0 );			
		c.gridy = 1;
		c.weighty = 1.0;
		c.fill = GridBagConstraints.BOTH;
		JScrollPane stylesScrollPane = new JScrollPane();
		stylesScrollPane.setViewportView(getStylesList());
		stylesPanel.add(stylesScrollPane, c);
		
		stylesPanel.setPreferredSize(new Dimension(110,100));
		stylesPanel.setMinimumSize(new Dimension(110,100));		
		return stylesPanel;
	}
	
	private JTextField getStylesText() {
		if ( stylesText == null ) {
			stylesText = new JTextField();
			stylesText.setEnabled(false);
    	}			
		return stylesText;
	}

	/**
	 * This method initializes stylesList
	 * 
	 * @return javax.swing.JList
	 */
	private JList getStylesList() {
		if(stylesList == null) {
			stylesList = new javax.swing.JList();
			stylesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			stylesList.addListSelectionListener( new ListSelectionListener() {
                public void valueChanged(ListSelectionEvent e) {
                	String newStyle = (String)getStylesList().getSelectedValue();
                	if ( newStyle != null ) {
                		checkNull();
                	    fontValue = new Font( fontValue.getName(), getStyleFromName( newStyle ), fontValue.getSize() );
                	    updatePreview();
                	    getStylesText().setText( newStyle );
                	}
                }
    	    });
		}
		return stylesList;
	}
	/**
	 * This method initializes jPanel2
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getSizePanel() {
		JPanel sizePanel = new JPanel();
		GridBagLayout gb = new GridBagLayout();
		sizePanel.setLayout(gb);
		GridBagConstraints c = new GridBagConstraints();	
		c.gridx = 0;
	    c.weightx = 1.0;

		c.insets = new Insets( 0, 0, 5, 0 );
        c.gridy = 0;
		c.weighty = 0.0;
		c.fill = GridBagConstraints.HORIZONTAL;
		sizePanel.add(getSizeText(), c);

		c.insets = new Insets( 0, 0, 0, 0 );			
		c.gridy = 1;
		c.weighty = 1.0;
		c.fill = GridBagConstraints.BOTH;
		JScrollPane sizeScrollPane = new JScrollPane();
		sizeScrollPane.setViewportView(getSizeList());
		sizePanel.add(sizeScrollPane, c);
		
		sizePanel.setPreferredSize(new Dimension(110,100));
		sizePanel.setMinimumSize(new Dimension(110,100));		
		return sizePanel;
	}
	
	/**
	 * This method initializes sizeText
	 * 
	 * @return javax.swing.JTextField
	 */
	private WholeNumberField getSizeText() {
		if(sizeText == null) {
			sizeText = new WholeNumberField();
			sizeText.getDocument().addDocumentListener( new DocumentListener() {
                public void updateFont() {
					Integer newSize = new Integer( getSizeText().getValue() );
					checkNull();
					fontValue = new Font( fontValue.getName(), fontValue.getStyle(), newSize.intValue() );
                    Integer selectedSize = (Integer)getSizeList().getSelectedValue();
            	    if ( selectedSize == null || ! selectedSize.equals( newSize ) ) {            	    	
            	    	if ( getPointSizes().contains(newSize) ) {
            	    	    getSizeList().setSelectedValue( newSize, true );
            	    	} else {
            	    		getSizeList().clearSelection();
            	    	}
            	    }
               	    updatePreview();
                }
                public void changedUpdate(DocumentEvent e) {
                    // Do nothing
                }
                public void insertUpdate(DocumentEvent e) {
 	                updateFont();
                }
                public void removeUpdate(DocumentEvent e) {
  	                updateFont();
                }
	       	});
			sizeText.addFocusListener(new FocusAdapter() {
				public void focusGained(FocusEvent e) {
					sizeText.selectAll();
				}
			});
	
    	}
		return sizeText;
	}
	/**
	 * This method initializes sizeList
	 * 
	 * @return javax.swing.JList
	 */
	private JList getSizeList() {
		if(sizeList == null) {
			sizeList = new JList();
			sizeList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			sizeList.addListSelectionListener( new ListSelectionListener() {
                public void valueChanged(ListSelectionEvent e) {
                	Integer newSize = (Integer)getSizeList().getSelectedValue();
                	if ( newSize != null ) {
                		checkNull();
                	    fontValue = new Font( fontValue.getName(), fontValue.getStyle(), newSize.intValue() );
                	    if ( newSize.intValue() != getSizeText().getValue() ) {
                	    	getSizeText().setValue( newSize.intValue() );
                	    }
                	    updatePreview();
                	}
                }
    	    });
		}
		return sizeList;
	}

	public int getStyleFromName( String name ) {
		int style = Font.PLAIN;
	
		if (name.equalsIgnoreCase(resabtedit.getString("plain"))) { //$NON-NLS-1$
			style = Font.PLAIN;
		} else if (name.equalsIgnoreCase(resabtedit.getString("bold"))) { //$NON-NLS-1$
			style = Font.BOLD;
		} else if (name.equalsIgnoreCase(resabtedit.getString("italic"))) { //$NON-NLS-1$
			style = Font.ITALIC;
		} else if (name.equalsIgnoreCase(resabtedit.getString("bold_italic"))) { //$NON-NLS-1$
			style = Font.BOLD | Font.ITALIC;
		}
		return style;
	}
	
	public String getNameFromStyle( int style ) {
		String name = resabtedit.getString("plain"); //$NON-NLS-1$

		switch( style ) {
			case Font.PLAIN               : name = resabtedit.getString("plain"); break; //$NON-NLS-1$
			case Font.BOLD                : name = resabtedit.getString("bold"); break; //$NON-NLS-1$
			case Font.ITALIC              : name = resabtedit.getString("italic"); break; //$NON-NLS-1$
			case Font.BOLD | Font.ITALIC  : name = resabtedit.getString("bold_italic"); break; //$NON-NLS-1$
		}
		return name;
	}
	
	private void initializeOptions() {
		getNamesList().setListData( getFontNames().toArray() );
        getStylesList().setListData(styleNames);
        getSizeList().setListData(getPointSizes());
   	}
   	
   	private int searchFontNames( String search ) {
   		int index = 0;
   		
   		if ( search.length() > 0 ) {
   			search = search.toLowerCase();

            Vector lcfn = getLowerCaseFontNames();
            // Search through the font list for the first element greater than or equal to the search
            // string
   			for ( index = 0; 
   			      index < lcfn.size() && 
   			      ((String)lcfn.elementAt(index)).compareTo( search ) < 0; 
   			      index++ );
   			if ( index >= lcfn.size() ) {
   				index = lcfn.size();
   			}
   		}	                 
   		return index;	
   	}
	
	/**
	 * setValue method comment.
	 */
	public void setFontValue(Font newFont) {
		fontValue = newFont;
		if (fontValue != null) {
			if (newFont.getSize() < 1) {
				newFont = newFont.deriveFont((float)1.0);
			}
			initializeFields();
		} else {
			getNamesList().clearSelection();
			getStylesList().clearSelection();
			getSizeList().clearSelection();
		}
		updatePreview();
    }
	
	private void initializeFields() {
		getNamesList().clearSelection();
		selectFontName(fontValue.getName());
	    getStylesList().setSelectedValue(getNameFromStyle(fontValue.getStyle()), true);
		getSizeText().setValue(fontValue.getSize());
	}
    
    /**
     * Select a given font name on the list (ignoring case)
	 * @param name   the font name to select
	 */
	private void selectFontName(String name) {
		ListModel lm = getNamesList().getModel();
		String temp;
		for (int i = 0; i < lm.getSize(); i++) {
			temp = (String)lm.getElementAt(i);
			if (name.equalsIgnoreCase(temp)) {
				getNamesList().setSelectedValue(temp, true);
				break;
			}
		}
	}
	
	private void checkNull() {
		if (fontValue == null) {
			fontValue = DEFAULT_VALUE;
			initializeFields();
		}
	}

	public Font getFontValue() {
    	return fontValue;
    }
    
    private void updatePreview() {
        getPreviewText().setFont(fontValue);
    }
    
    /**
     * Recursively set this container and every child of the container's
     * background color to the given color.
     * Note: ignores setting background of JList components
     * @param parent the starting container to set colors
     * @param bgColor the background color to set to
     */
	private void recursiveSetBackground( Container parent, Color bgColor ) {
		if ( parent instanceof JList || parent instanceof JTextComponent ) {
			return;
		}
		
		parent.setBackground( bgColor );
		
		Component[] children = parent.getComponents();
		for ( int i = 0; i < children.length; i++ ) {
			if ( children[i] instanceof Container ) {
				recursiveSetBackground( (Container)children[i], bgColor );
			} else {
				children[i].setBackground( bgColor );
			}
		}
	}

    // Used to perform comparisons on strings ignoring case.
	protected class StringIgnoreCaseComparator implements Comparator {
		public int compare( Object o1, Object o2 ) {
			if ( o1 instanceof String && o2 instanceof String ) {
				return ((String)o1).compareToIgnoreCase((String)o2);
			} else {
				return -1;
			}
		}
		
		public boolean equals( Object o1, Object o2 ) {
			if ( o1 instanceof String && o2 instanceof String ) {
				return ((String)o1).equalsIgnoreCase((String)o2);
			} else {
	 			return false;
			}
		}
    }
    
    // Used to restrict entry in the JTextField to only digits.
	protected class WholeNumberField extends JTextField {
	
		/**
		 * Comment for <code>serialVersionUID</code>
		 * 
		 * @since 1.1.0
		 */
		private static final long serialVersionUID = 5053060184460827306L;
		private NumberFormat integerFormatter;

		public WholeNumberField() {
			super();
			integerFormatter = NumberFormat.getNumberInstance();
			integerFormatter.setParseIntegerOnly(true);
		}

		public int getValue() {
			int retVal = 1;
			try {
				retVal = integerFormatter.parse(getText()).intValue();
				// Switch to 1 if they enter 0, as size 0 breaks things
				if (retVal == 0) {
					retVal = 1;
				}
			} catch (ParseException e) {
				// This can only happen if the string is empty
				// Default to the first item on the list in this case
				retVal = sizeArray[0].intValue();
			}
			return retVal;
		}

		public void setValue(int value) {
			setText(integerFormatter.format(value));
		}

		protected Document createDefaultModel() {
			return new WholeNumberDocument();
		}

		protected class WholeNumberDocument extends PlainDocument {
	        
			/**
			 * Comment for <code>serialVersionUID</code>
			 * 
			 * @since 1.1.0
			 */
			private static final long serialVersionUID = 209899837559693967L;

			public void insertString(int offs, String str, AttributeSet a)
				throws BadLocationException {
				char[] source = str.toCharArray();
				char[] result = new char[source.length];
				int j = 0;

				for (int i = 0; i < result.length; i++) {
					if (Character.isDigit(source[i])) {
						result[j++] = source[i];
					}
				}
				super.insertString(offs, new String(result, 0, j), a);
			}
		}
	}
}
