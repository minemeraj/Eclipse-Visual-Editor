package org.eclipse.ve.internal.jfc.beaninfo;
/*******************************************************************************
 * Copyright (c) 2001, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: BorderPropertyEditor.java,v $
 *  $Revision: 1.3 $  $Date: 2005-06-16 17:46:03 $ 
 */

import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Vector;

import javax.swing.*;
import javax.swing.border.Border;

public class BorderPropertyEditor extends JPanel implements ItemListener {
	/**
	 * Comment for <code>serialVersionUID</code>
	 * 
	 * @since 1.1.0
	 */
	private static final long serialVersionUID = -5616560005627773606L;
	private boolean built = false;
	private Border fBorder = null;
	private JComboBox borderCombo = null;
	private JScrollPane borderPane = null;
	private JScrollPane detailPanel = null;
	private JScrollPane borderPreviewer = null;
	private JLabel displayLabel = null;
	private JLabel displayBorderName = null;
	private boolean displayButtonRemoved = false;
	private boolean chooseOptionRemoved = false;
	
	private AbstractBorderPropertyPage selectedPage = null;
	
	private Vector initializedPages = null;
	private Dimension pageSize = null;
	
		
	class BorderListModel extends Object{
		String listName;
		Class pageClass;
		Class editingClass;
		Object page;
		BorderListModel(String aListName,Class aPageClass,Class anEditingClass){
			listName = aListName;
			pageClass = aPageClass;
			editingClass = anEditingClass;
		}
		BorderListModel(String aListName,String aPageClassName,String anEditingClassName){
			listName = aListName;
			try {
				pageClass = Class.forName(aPageClassName);
				editingClass = Class.forName(anEditingClassName);
			} catch ( ClassNotFoundException exc ) {
				exc.printStackTrace();
			}
		}	
		
		String getListName(){
			return listName;
		}
		
		String getDisplayName(){
			if ( page == null ){
				return ""; //$NON-NLS-1$
			} else {
				return getPage().getDisplayName();
			}
		}
		
		boolean isEditorFor( Border aBorder ) {
		    if ( aBorder == null && editingClass == null ) {
		    	return true;
		    } else if ( aBorder != null && editingClass != null && editingClass.equals( aBorder.getClass() ) ) {
		    	return getPage().okToSetBorder(aBorder);
		    }
		    return false;
		}
			
		AbstractBorderPropertyPage getPage(){
			if ( page == null ) {
				try{
					page = pageClass.newInstance();
				}catch (Exception exc){
					exc.printStackTrace();
				}
			}
			return (AbstractBorderPropertyPage)page;
		}
		
		public boolean equals( Object o ) {
			boolean isEqual = false;
			if ( o instanceof BorderListModel ) {
				BorderListModel blm = (BorderListModel)o;
				isEqual = listName.equals( blm.getListName() );
			}
			return isEqual;
		}
		public String toString() {
			return getListName();
		}
	};
	
	private BorderListModel nullBorderListModel = new BorderListModel(VisualBeanInfoMessages.getString("PropertyPage.None"),NoBorderSelectedPropertyPage.class,null); //$NON-NLS-1$
	private BorderListModel[] fBorderModels = {
			nullBorderListModel,
			new BorderListModel(VisualBeanInfoMessages.getString("PropertyPage.Bevel"),BevelBorderPropertyPage.class,javax.swing.border.BevelBorder.class), //$NON-NLS-1$
			new BorderListModel(VisualBeanInfoMessages.getString("PropertyPage.Compound"), CompoundBorderPropertyPage.class,javax.swing.border.CompoundBorder.class), //$NON-NLS-1$
			new BorderListModel(VisualBeanInfoMessages.getString("PropertyPage.Empty"),EmptyBorderPropertyPage.class,javax.swing.border.EmptyBorder.class), //$NON-NLS-1$
			new BorderListModel(VisualBeanInfoMessages.getString("PropertyPage.Etched"),EtchedBorderPropertyPage.class,javax.swing.border.EtchedBorder.class), //$NON-NLS-1$
			new BorderListModel(VisualBeanInfoMessages.getString("PropertyPage.Line"),LineBorderPropertyPage.class,javax.swing.border.LineBorder.class), //$NON-NLS-1$
			new BorderListModel(VisualBeanInfoMessages.getString("PropertyPage.Matte"),MatteBorderPropertyPage.class,javax.swing.border.MatteBorder.class), //$NON-NLS-1$
			new BorderListModel(VisualBeanInfoMessages.getString("PropertyPage.Soft"),SoftBevelBorderPropertyPage.class,javax.swing.border.SoftBevelBorder.class), //$NON-NLS-1$
			new BorderListModel(VisualBeanInfoMessages.getString("PropertyPage.Titled"),TitledBorderPropertyPage.class,javax.swing.border.TitledBorder.class), //$NON-NLS-1$
	};
	
	private BorderPagePropertyListener borderPagePropertyListener = new BorderPagePropertyListener();
	
	// An instance of this class is used to listen to each page which should signal
	// an event of name "border" and the new value of the border
	class BorderPagePropertyListener implements PropertyChangeListener{
		public void propertyChange(PropertyChangeEvent anEvent){
			if ( "borderValueChanged".equals(anEvent.getPropertyName())){ //$NON-NLS-1$
				setPreviewBorder( (Border) anEvent.getNewValue());
			}
		}
	}
		
	
public BorderPropertyEditor() {
	super();
	this.initialize();
	setBorderValue(null);
}

public BorderPropertyEditor(Border aBorder) {
	super();
	this.initialize();
	setBorderValue(aBorder);
}

private AbstractBorderPropertyPage getPageForBorder( Border aBorder ) {
	AbstractBorderPropertyPage aPage = nullBorderListModel.getPage();

	if (aBorder != null) {
		for (int i = 0; i < fBorderModels.length; i++) {
			if (fBorderModels[i].isEditorFor(aBorder)) {
				aPage = fBorderModels[i].getPage();
				break;
			}
		}
    }
	return aPage;
}	

public Border getBorderValue() {
	return selectedPage.getBorderValue();
}

public String getBorderName(){
	if (selectedPage != null){
		return selectedPage.getDisplayName();
	}
	return ""; //$NON-NLS-1$
}

public BorderListModel[] getBorderModelList(){
	return fBorderModels; 
}

public JComboBox getBorderCombo(){
	if (borderCombo == null){
		borderCombo = new JComboBox(fBorderModels);
		borderCombo.setBackground( SystemColor.control );
		borderCombo.setMaximumRowCount( fBorderModels.length );	
	}
	return borderCombo;
}

public JScrollPane getBorderPane(){
	if (borderPane == null){
		borderPane = new JScrollPane(getBorderCombo());
		borderPane.setBackground(SystemColor.control);
		borderPane.getVerticalScrollBar().setBackground(SystemColor.control);
		borderPane.getHorizontalScrollBar().setBackground(SystemColor.control);
	}
	return borderPane;
}


public JScrollPane getBorderPreviewer(){
	if (borderPreviewer == null){
		borderPreviewer = new JScrollPane();
		displayLabel = new JLabel(VisualBeanInfoMessages.getString("BorderPropertyEditor.DisplayButton.Text")); //$NON-NLS-1$
		displayLabel.setHorizontalAlignment(SwingConstants.CENTER);
		displayLabel.setBackground(Color.white);
		displayLabel.setForeground(Color.black);
		displayLabel.setOpaque(true);
		borderPreviewer.setViewportView(displayLabel);
		borderPreviewer.setViewportBorder(BorderFactory.createLineBorder(SystemColor.control, 5));
	}
	return borderPreviewer;
}


public JScrollPane getDetailPanel(){
	if (detailPanel == null){
		detailPanel = new JScrollPane();
		detailPanel.setPreferredSize( getPageSize() );
		detailPanel.setViewportBorder(BorderFactory.createLineBorder(SystemColor.control, 5));
	}
	return detailPanel;
}

public Dimension getPageSize() {
	
	if ( pageSize == null ) {
        int maxWidth = 0;
        int maxHeight = 0;
        Dimension size;
     
        for ( int i = 0; i < fBorderModels.length; i++ ) {
         	size = fBorderModels[i].getPage().getMinimumSize();
     	    if ( size.getWidth() > maxWidth ) {
     		   maxWidth = (int)size.getWidth();
        	}
        	if ( size.getHeight() > maxHeight ) {
        		maxHeight = (int)size.getHeight();
     	    }
         }
         pageSize = new Dimension( maxWidth, maxHeight );
	}
	return pageSize;
}


private void initialize() {
	setName("BorderPropertyEditor"); //$NON-NLS-1$
	initializedPages = new Vector();
}

public void buildPropertyEditor() {
	if (!built) {
		GridBagLayout gb = new GridBagLayout();
		this.setLayout(gb);
		this.setBackground(SystemColor.control);

        JLabel borderLabel = new JLabel(VisualBeanInfoMessages.getString("BorderPropertyEditor.Label.Text")); //$NON-NLS-1$
		borderLabel.setBackground(SystemColor.control);

		GridBagConstraints constraintsBorderLabel = new GridBagConstraints();
		constraintsBorderLabel.gridx = 0;
		constraintsBorderLabel.gridy = 0;
		constraintsBorderLabel.insets = new Insets(2, 2, 0, 2);
		constraintsBorderLabel.anchor = GridBagConstraints.WEST;
		this.add(borderLabel, constraintsBorderLabel);

		GridBagConstraints constraintsBorderPane = new GridBagConstraints();
		constraintsBorderPane.gridx = 0;
		constraintsBorderPane.gridy = 1;
		constraintsBorderPane.fill = GridBagConstraints.HORIZONTAL;
		constraintsBorderPane.insets = new Insets(2, 2, 2, 2);
		constraintsBorderPane.weightx = 1.0;
		constraintsBorderPane.weighty = 0.0;
		constraintsBorderPane.anchor = GridBagConstraints.WEST;
        this.add( getBorderCombo(), constraintsBorderPane );

		GridBagConstraints constraintsDetailPanel = new GridBagConstraints();
		constraintsDetailPanel.gridx = 0;
		constraintsDetailPanel.gridy = 2;
		constraintsDetailPanel.fill = GridBagConstraints.BOTH;
		constraintsDetailPanel.insets = new Insets(2, 2, 2, 2);
		constraintsDetailPanel.weightx = 1.0;
		constraintsDetailPanel.weighty = 0.85;
		this.add(getDetailPanel(), constraintsDetailPanel);

		GridBagConstraints constraintsBorderPreviewer =
			new GridBagConstraints();
		constraintsBorderPreviewer.gridx = 0;
		constraintsBorderPreviewer.gridy = 3;
		constraintsBorderPreviewer.fill = GridBagConstraints.BOTH;
		constraintsBorderPreviewer.insets = new Insets(2, 2, 2, 2);
		constraintsBorderPreviewer.weightx = 1.0;
		constraintsBorderPreviewer.weighty = 0.15;
		this.add(getBorderPreviewer(),constraintsBorderPreviewer);

        displayBorderName = new JLabel(" ");  //$NON-NLS-1$
		GridBagConstraints constraintsDisplayBorderName =
			new GridBagConstraints();
		constraintsDisplayBorderName.gridx = 0;
		constraintsDisplayBorderName.gridy = 4;
		constraintsDisplayBorderName.gridwidth = GridBagConstraints.REMAINDER;
		constraintsDisplayBorderName.anchor = GridBagConstraints.WEST;
		constraintsDisplayBorderName.insets = new Insets(2, 2, 2, 2);
		displayBorderName.setForeground(Color.blue);
		this.add(displayBorderName,constraintsDisplayBorderName);

		getBorderCombo().addItemListener(this);
		setSelectedEditor();
		switchToPage(selectedPage);
	
		setPreviewBorder(getBorderValue());
		built = true;
	}
}

public void setSelectedEditor() {
	Border b = getBorderValue();
	
	if ( b == null ) {
		if (chooseOptionRemoved) {
			chooseOptionRemoved = false;
			getBorderCombo().insertItemAt(nullBorderListModel, 0);
		}
		getBorderCombo().setSelectedIndex(0);
		return;
	} 
	
	if ( ! chooseOptionRemoved ) {
		chooseOptionRemoved = true;
		getBorderCombo().removeItem(nullBorderListModel);
	}
	for ( int i = 0; i < fBorderModels.length; i++){
		if ( fBorderModels[i+1].isEditorFor(b) ) {
			getBorderCombo().setSelectedIndex(i);
			break;
		}
	}
}

public void setBorderValue(Border aBorder) {
	fBorder = aBorder;
    selectedPage = getPageForBorder(aBorder);
}

public String getBorderInitializationString(){
 	return selectedPage.getJavaInitializationString();
}

public void itemStateChanged(ItemEvent e){
	
	if (e.getSource() == getBorderCombo()){
		BorderListModel item = (BorderListModel)getBorderCombo().getSelectedItem();
		if ( !chooseOptionRemoved && item != nullBorderListModel ) {
			chooseOptionRemoved = true;
			getBorderCombo().removeItem(nullBorderListModel);
		}
		selectedPage = item.getPage();
		selectedPage.addPropertyChangeListener(borderPagePropertyListener);
		switchToPage(selectedPage);
		fBorder = selectedPage.getBorderValue();
		setPreviewBorder(fBorder);
	}
}

public void switchToPage(AbstractBorderPropertyPage aPage){
	if (! initializedPages.contains( aPage ) ) {
		aPage.buildPropertyPage();
        initializedPages.add( aPage );
	}
	getDetailPanel().setViewportView( aPage );
}

private void setPreviewBorder(Border aBorder) {
	if (displayButtonRemoved) {
		// reset in case this was removed from a previous ClassCastException
		borderPreviewer.add(displayLabel, BorderLayout.CENTER);
		displayButtonRemoved = false;
	}
	displayLabel.setBorder(aBorder);
	if ( aBorder == null ) {
		displayBorderName.setText( " " ); //$NON-NLS-1$
	} else {
	    displayBorderName.setText(aBorder.getClass().getName());
	}
}

/**
 * @see java.awt.Component#paint(Graphics)
 * Need to override to capture the ClassCastExceptions that are thrown
 * when the border is a special inner class border used by the component
 * type and cannot be applied as a border to the display button (a JButton).
 */
public void paint(Graphics g) {
	try {
		super.paint(g);
	} catch (ClassCastException e) {
		displayButtonRemoved = true;
		borderPreviewer.remove(displayLabel);
		borderPreviewer.setBackground(borderPane.getBackground());
		this.repaint();
	}
}

}