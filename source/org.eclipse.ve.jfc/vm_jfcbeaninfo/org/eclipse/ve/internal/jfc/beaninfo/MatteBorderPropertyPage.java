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
 *  $RCSfile: MatteBorderPropertyPage.java,v $
 *  $Revision: 1.3 $  $Date: 2005-06-16 17:46:03 $ 
 */

import java.awt.*;
import java.text.MessageFormat;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;
import javax.swing.event.*;

public class MatteBorderPropertyPage extends AbstractBorderPropertyPage implements DocumentListener, ListSelectionListener {
	/**
	 * Comment for <code>serialVersionUID</code>
	 * 
	 * @since 1.1.0
	 */
	private static final long serialVersionUID = -4850147951715863878L;
	private static java.util.ResourceBundle resabtedit = java.util.ResourceBundle.getBundle("org.eclipse.ve.internal.jfc.beaninfo.vceedit");  //$NON-NLS-1$
	private boolean built = false;
	public static String[] colorNames =
		{resabtedit.getString("black"), resabtedit.getString("blue"), resabtedit.getString("cyan"), resabtedit.getString("darkGray"), resabtedit.getString("gray"), resabtedit.getString("green"), resabtedit.getString("lightGray"), //$NON-NLS-7$ //$NON-NLS-6$ //$NON-NLS-5$ //$NON-NLS-4$ //$NON-NLS-3$ //$NON-NLS-2$ //$NON-NLS-1$
		resabtedit.getString("magenta"), resabtedit.getString("orange"), resabtedit.getString("pink"), resabtedit.getString("red"), resabtedit.getString("white"), resabtedit.getString("yellow")}; //$NON-NLS-6$ //$NON-NLS-5$ //$NON-NLS-4$ //$NON-NLS-3$ //$NON-NLS-2$ //$NON-NLS-1$
	public static Color[] colorValues =
		{Color.black, Color.blue, Color.cyan, Color.darkGray, Color.gray, Color.green, Color.lightGray, 
		Color.magenta, Color.orange, Color.pink, Color.red, Color.white, Color.yellow};
	public static String[] initStrings =
		{"black", "blue", "cyan", "darkGray", "gray", "green", "lightGray",//$NON-NLS-7$//$NON-NLS-6$//$NON-NLS-5$//$NON-NLS-4$//$NON-NLS-3$//$NON-NLS-2$//$NON-NLS-1$
		"magenta", "orange", "pink", "red", "white", "yellow"};//$NON-NLS-6$//$NON-NLS-5$//$NON-NLS-4$//$NON-NLS-3$//$NON-NLS-2$//$NON-NLS-1$
		
	private int top = 5;
	private int left = 5;
	private int bottom = 5;
	private int right = 5;
	private Color color = colorValues[0];

	private JTextField topField = null;
	private JTextField leftField = null;
	private JTextField bottomField = null;
	private JTextField rightField = null;
	
	private JList colorList = null;
	private JScrollPane colorPane = null;

	                                             
public MatteBorderPropertyPage(){
	super();  
	initialize();
}

public String getName(){
	return "MatteBorderPropertyPage"; //$NON-NLS-1$
}

public String getDisplayName(){
	
	String colorString = colorNames[getIndex(color)];	
	return MessageFormat.format(VisualBeanInfoMessages.getString("MatteBorder.Matte(Color,top,left,bottom,right)"),  //$NON-NLS-1$
								new Object[]{colorString,
											 new Integer(getTop()),
											 new Integer(getLeft()),
											 new Integer(getBottom()),
											 new Integer(getRight())});
}

public int getIndex(Color c) {
	int index = -1;
	for (int i = 0; i < colorValues.length; i++) {
		if (colorValues[i].equals(c)) {
            index = i;
            break;
		}
	}
	return index;
}

public JList getColorList(){
	if(colorList == null){
		try{
			colorList = new JList( colorValues);
			colorList.setCellRenderer(new ColorCellRenderer( colorNames,  colorValues));
		}catch(Throwable e){
			e.printStackTrace();
		}
	}
	return colorList;
}

public JScrollPane getColorPane(){
	if (colorPane == null){
		try{
			colorPane = new JScrollPane(getColorList());
		}catch(Throwable e){
			e.printStackTrace();
		}
	}
	return colorPane;
}

public void initialize(){
	this.setName("MatteBorderPropertyPage"); //$NON-NLS-1$
}

public void buildPropertyPage() {
	if (!built) {
		this.setBackground(SystemColor.control);
		this.setLayout(new BorderLayout());

		JPanel p1 = new JPanel();
		p1.setBackground(SystemColor.control);
		p1.setLayout(new GridLayout(4, 2, 0, 0));

		topField = new JTextField(String.valueOf(top));
		leftField = new JTextField(String.valueOf(left));
		bottomField = new JTextField(String.valueOf(bottom));
		rightField = new JTextField(String.valueOf(right));

		p1.add(new JLabel(VisualBeanInfoMessages.getString("Top"))); //$NON-NLS-1$
		p1.add(topField);
		p1.add(new JLabel(VisualBeanInfoMessages.getString("Left"))); //$NON-NLS-1$
		p1.add(leftField);
		p1.add(new JLabel(VisualBeanInfoMessages.getString("Bottom"))); //$NON-NLS-1$
		p1.add(bottomField);
		p1.add(new JLabel(VisualBeanInfoMessages.getString("Right"))); //$NON-NLS-1$
		p1.add(rightField);
		this.add(p1, BorderLayout.NORTH);

		topField.getDocument().addDocumentListener(this);
		leftField.getDocument().addDocumentListener(this);
		bottomField.getDocument().addDocumentListener(this);
		rightField.getDocument().addDocumentListener(this);

		JPanel p2 = new JPanel();
		p2.setLayout(new BorderLayout());
		p2.setBackground(SystemColor.control);
		p2.add(new JLabel(VisualBeanInfoMessages.getString("MatteBorder.BorderColor")), BorderLayout.NORTH); //$NON-NLS-1$
		p2.add(getColorPane(), BorderLayout.CENTER);
		this.add(p2, BorderLayout.CENTER);
		getColorList().setSelectedIndex(getIndex(color));
		getColorList().addListSelectionListener(this);
		built = true;
	}
}

protected int getIntValue(JTextField aTextField){
	try {
		return Integer.parseInt(aTextField.getText());
	} catch ( NumberFormatException nfexc ) {
		return 0;
	}	
}

public int getTop(){
	return top;
}

public int getLeft(){
	return left;
}

public int getBottom(){
	return bottom;
}

public int getRight(){
	return right;
}

public Color getColor(){
    return color;
}

public Border getBorderValue(){
	Border aBorder = BorderFactory.createMatteBorder(getTop(), getLeft(), getBottom(), getRight(), getColor());
	return aBorder;
}

public void valueChanged(ListSelectionEvent e){
	if (e.getSource() == getColorList()) {
		int i = getColorList().getSelectedIndex();
		if (i >= 0 && i < colorNames.length) {
			color = colorValues[i];
		} else {
			color = colorValues[0];
		}
		firePropertyChange("borderValueChanged", null, getBorderValue()); //$NON-NLS-1$
	}
}

public void updateHandle(DocumentEvent e){
	if (e.getDocument() == topField.getDocument()) { 
		top = getIntValue(topField);
	} else if ( e.getDocument() == leftField.getDocument() ) {
		left = getIntValue(leftField);
	} else if (e.getDocument() == bottomField.getDocument()) {
		bottom = getIntValue(bottomField);
	} else if (e.getDocument() == rightField.getDocument()){
		right = getIntValue(rightField);
	} else {
		return;
	}
	firePropertyChange("borderValueChanged", null, getBorderValue()); //$NON-NLS-1$
}

public void insertUpdate(DocumentEvent e){
	updateHandle(e);
}

public void removeUpdate(DocumentEvent e){
	updateHandle(e);
}

public void changedUpdate(DocumentEvent e){
	updateHandle(e);
}

public boolean okToSetBorder(Border aBorder){
	if (aBorder instanceof MatteBorder){
		MatteBorder fBorder = (MatteBorder) aBorder;
		Insets fInsets = fBorder.getBorderInsets();
		top = fInsets.top;
		left = fInsets.left;
		bottom = fInsets.bottom;
		right = fInsets.right;
	
	    color = fBorder.getMatteColor();
		return true;
	}
	return false;

}

protected String getBorderColorJavaInitializationString(){
	int i = getIndex(color);
	if (i != -1){
		return "java.awt.Color." +  initStrings[i]; //$NON-NLS-1$
	}
	else return "java.awt.Color." +  initStrings[0]; //$NON-NLS-1$
}

public String getJavaInitializationString(){
	return "javax.swing.BorderFactory.createMatteBorder(" + getTop() + "," + getLeft() + "," + getBottom() + //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			"," + getRight() + "," + getBorderColorJavaInitializationString() + ")"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
}


}