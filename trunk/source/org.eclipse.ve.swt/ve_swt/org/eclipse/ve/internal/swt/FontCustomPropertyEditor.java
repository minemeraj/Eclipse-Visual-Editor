/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: FontCustomPropertyEditor.java,v $
 *  $Revision: 1.3 $  $Date: 2005-03-28 19:00:28 $ 
 */
package org.eclipse.ve.internal.swt;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.*;

import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.widgets.List;

import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.jem.internal.proxy.core.*;

import org.eclipse.ve.internal.java.core.BeanProxyUtilities;

public class FontCustomPropertyEditor extends Composite {

	protected final static String[] styleNames = {
			FontPropertyEditorMessages.getString("normalStyle"), FontPropertyEditorMessages.getString("boldStyle"), FontPropertyEditorMessages.getString("italicStyle"), FontPropertyEditorMessages.getString("boldItalicStyle")}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
	protected final static int[] styleValues = { SWT.NORMAL, SWT.BOLD, SWT.ITALIC, SWT.BOLD | SWT.ITALIC};
	protected final static int[] sizeValues = { 8, 10, 12, 14, 18, 24, 36, 48, 72};

	protected final static String[] jfaceFontNames = { "Banner", "Default", "Dialog", "Header", "Text"};
	protected final static String[] jfaceNameConstants = { "org.eclipse.jface.bannerfont", "org.eclipse.jface.defaultfont", //$NON-NLS-1$ //$NON-NLS-2$
			"org.eclipse.jface.dialogfont", "org.eclipse.jface.headerfont", "org.eclipse.jface.textfont"}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	protected final static String[] jfaceStyleNames = {
			FontPropertyEditorMessages.getString("normalStyle"), FontPropertyEditorMessages.getString("boldStyle"), FontPropertyEditorMessages.getString("italicStyle")}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	protected final static int JFACE_NORMAL = 0;	// index into jfaceStyleNames
	protected final static int JFACE_BOLD = 1;		// index into jfaceStyleNames
	protected final static int JFACE_ITALIC = 2;	// index into jfaceStyleNames

	protected static java.util.Set fontNames;
	protected static java.util.Set lowerCaseFontNames;

	private java.util.List fPropertyChangeListeners;

	private IJavaObjectInstance fExistingValue;
	private Text nameField;
	private Text styleField;
	private Text sizeField;

	private List namesList;
	private List stylesList;
	private List sizesList;

	private Text previewText;

	private boolean isUpdating = false;
	private boolean isJFace = false;
	private JFaceFontInfo jfaceFontInfo;
	
	protected Font value;
	private TabFolder tabFolder = null;
	private Composite composite = null;
	private Composite jfaceFontsTab = null;
	private Label jfaceNameLabel = null;
	private Label jfaceStyleLabel = null;
	private Text jfaceNameField = null;
	private Text jfaceStyleField = null;
	private List jfaceNamesList = null;
	private List jfaceStylesList = null;
	// Used to perform comparisons on strings ignoring case.
	public class StringIgnoreCaseComparator implements Comparator {

		public int compare(Object o1, Object o2) {
			if (o1 instanceof String && o2 instanceof String) {
				return ((String) o1).compareToIgnoreCase((String) o2);
			} else {
				return -1;
			}
		}

		public boolean equals(Object o1, Object o2) {
			if (o1 instanceof String && o2 instanceof String) {
				return ((String) o1).equalsIgnoreCase((String) o2);
			} else {
				return false;
			}
		}
	}

	/*
	 * Store JFace font information need later for the initialization string
	 * By default create the default font, normal style
	 */
	public class JFaceFontInfo {
		String name = JFaceResources.DEFAULT_FONT;
		int style = JFACE_NORMAL;
	}
	
	public FontCustomPropertyEditor(Composite parent, int style, Font value, IJavaObjectInstance existingValue) {
		super(parent, style);
		this.value = value;
		fExistingValue = existingValue;
		initialize();
	}

	private void initialize() {
		setSize(new org.eclipse.swt.graphics.Point(322,245));
		GridLayout grid = new GridLayout();
		grid.horizontalSpacing = 1;
		grid.verticalSpacing = 1;
		this.setLayout(grid);

		createTabFolder();
		initializeLists();

		previewText = new Text(this, SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.H_SCROLL | SWT.V_SCROLL);
		previewText.setText(FontPropertyEditorMessages.getString("previewText")); //$NON-NLS-1$
		GridData gd03 = new GridData();
		gd03.horizontalAlignment = GridData.FILL;
		gd03.verticalAlignment = GridData.FILL;
		gd03.grabExcessHorizontalSpace = true;
		gd03.grabExcessVerticalSpace = true;
		gd03.heightHint = 50;
		gd03.widthHint = 500;
		previewText.setLayoutData(gd03);

		grid.numColumns = 3;
		createFontFromProxy(this);
		updateSelections();
		nameField.selectAll();
	}

	private void initializeLists() {
		namesList.removeAll();
		Iterator iter = getFontNames().iterator();
		while (iter.hasNext()) {
			namesList.add((String) iter.next());
		}
		stylesList.setItems(styleNames);
		sizesList.removeAll();
		for (int i = 0; i < sizeValues.length; i++) {
			sizesList.add(String.valueOf(sizeValues[i]));
		}

		jfaceNamesList.setItems(jfaceFontNames);
		jfaceStylesList.setItems(jfaceStyleNames);
	}

	private void createFontFromProxy(Control control) {
		if (this.fExistingValue == null) { return; }
		try {
			// type: Font
			IBeanProxy fontProxy = BeanProxyUtilities.getBeanProxy(this.fExistingValue);
			IBeanTypeProxy fontType = fontProxy.getTypeProxy();
			IMethodProxy getFontData = fontType.getMethodProxy("getFontData");
			// type: FontData[]
			IArrayBeanProxy fontDataArrayProxy = (IArrayBeanProxy) getFontData.invoke(fontProxy);
			int len = fontDataArrayProxy.getLength();
			FontData[] fontData = new FontData[len];
			for (int i = 0; i < len; i++) {
				// type: FontData
				IBeanProxy fontDataProxy = fontDataArrayProxy.get(i);
				IBeanTypeProxy fontDataType = fontDataProxy.getTypeProxy();

				IMethodProxy getHeight = fontDataType.getMethodProxy("getHeight");
				IMethodProxy getStyle = fontDataType.getMethodProxy("getStyle");
				IMethodProxy getName = fontDataType.getMethodProxy("getName");

				int height = ((IIntegerBeanProxy) getHeight.invoke(fontDataProxy)).intValue();
				int style = ((IIntegerBeanProxy) getStyle.invoke(fontDataProxy)).intValue();
				String name = ((IStringBeanProxy) getName.invoke(fontDataProxy)).stringValue();

				fontData[i] = new FontData(name, height, style);
			}
			Font newValue = new Font(control.getDisplay(), fontData);
			this.value = newValue;
		} catch (ThrowableProxy t) {
			// failsafe
		}
	}

	protected java.util.Set getFontNames() {
		if (fontNames == null) {
			fontNames = new TreeSet(new StringIgnoreCaseComparator());
			FontData[] fd = this.getDisplay().getFontList(null, true);
			for (int i = 0; i < fd.length; i++) {
				fontNames.add(fd[i].getName());
			}
		}
		return fontNames;
	}

	public void setValue(Object v) {
		if (v != null && v instanceof Font) {
			this.value = (Font) v;
		}
		// Fire the font change
		if (fPropertyChangeListeners != null) {
			Iterator iter = fPropertyChangeListeners.iterator();
			while (iter.hasNext()) {
				((PropertyChangeListener) iter.next()).propertyChange(new PropertyChangeEvent(this, "value", v, null)); //$NON-NLS-1$
			}
		}
	}

	public String getJavaInitializationString() {
		String SWT_PREFIX = "org.eclipse.swt.SWT"; //$NON-NLS-1$
		if (value == null)
			return "null"; //$NON-NLS-1$
		if (!isJFace) {
			FontData fd = value.getFontData()[0];
			String style;
			switch (fd.getStyle()) {
				case SWT.NORMAL:
					style = SWT_PREFIX + ".NORMAL";break; //$NON-NLS-1$
				case SWT.BOLD:
					style = SWT_PREFIX + ".BOLD";break; //$NON-NLS-1$
				case SWT.ITALIC:
					style = SWT_PREFIX + ".ITALIC";break; //$NON-NLS-1$
				case SWT.BOLD | SWT.ITALIC:
					style = SWT_PREFIX + ".BOLD | " + SWT_PREFIX + ".ITALIC";break; //$NON-NLS-1$ //$NON-NLS-2$
				default:
					style = SWT_PREFIX + ".NORMAL"; //$NON-NLS-1$
			}
			return "new org.eclipse.swt.graphics.Font(org.eclipse.swt.widgets.Display.getDefault(), \"" + fd.getName() + "\", " + String.valueOf(fd.getHeight()) + ", " + style + ")"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		} else {
			String results = "";
			switch (jfaceFontInfo.style) {
				case JFACE_NORMAL:
					results = "org.eclipse.jface.resource.JFaceResources.getFontRegistry().get(\"" + jfaceFontInfo.name + "\")"; //$NON-NLS-1$ //$NON-NLS-2$
					break;
				case JFACE_BOLD:
					results = "org.eclipse.jface.resource.JFaceResources.getFontRegistry().getBold(\"" + jfaceFontInfo.name + "\")"; //$NON-NLS-1$ //$NON-NLS-2$
					break;
				case JFACE_ITALIC:
					results = "org.eclipse.jface.resource.JFaceResources.getFontRegistry().getItalic(\"" + jfaceFontInfo.name + "\")"; //$NON-NLS-1$ //$NON-NLS-2$
					break;

				default:
					results = "org.eclipse.jface.resource.JFaceResources.getFontRegistry().get(\"" + jfaceFontInfo.name + "\")"; //$NON-NLS-1$ //$NON-NLS-2$
					break;
			}
			return results;
		}
	}

	public Font getValue() {
		return value;
	}

	protected java.util.Set getLowerCaseFontNames() {
		if (lowerCaseFontNames == null) {
			lowerCaseFontNames = new TreeSet();
			Iterator iter = getFontNames().iterator();
			while (iter.hasNext()) {
				lowerCaseFontNames.add(((String) iter.next()).toLowerCase());
			}
		}
		return lowerCaseFontNames;
	}

	private int searchFontNames(String search) {
		int index = 0;

		if (search.length() > 0) {
			search = search.toLowerCase();

			Set lcfn = getLowerCaseFontNames();
			// Search through the font list for the first element greater than or equal to the search
			// string
			Iterator iter = lcfn.iterator();
			while (iter.hasNext()) {
				if (((String) iter.next()).compareTo(search) >= 0)
					break;
				index++;
			}
			if (index >= lcfn.size()) {
				index = lcfn.size();
			}
		}
		return index;
	}

	private void updateFont() {
		isJFace = false;
		if (namesList.getSelectionCount() == 0 || stylesList.getSelectionCount() == 0) { return; }
		Font f;
		int newSize = 1;
		if (sizeField.getText().length() > 0) {
			newSize = Integer.parseInt(sizeField.getText());
		} else {
			// Default to the smallest size available in the list
			if (sizesList.getItemCount() > 0) {
				sizesList.setSelection(0);
				String smallestSize = sizesList.getItem(0);
				newSize = Integer.parseInt(smallestSize);
				sizeField.setText(smallestSize);
			}
		}
		try {
			f = new Font(this.getDisplay(), namesList.getSelection()[0], newSize, styleValues[stylesList.getSelectionIndex()]);
		} catch (SWTError e) {
			// badly formed font
			return;
		}
		previewText.setFont(f);
		if (value != null && !value.isDisposed()) {
			value.dispose();
		}
		setValue(f);
	}

	private void updateJFaceFont() {
		if (jfaceNamesList.getSelectionCount() == 0 || jfaceStylesList.getSelectionCount() == 0) { return; }
		isJFace = true;
		jfaceFontInfo = new JFaceFontInfo();
		Font f;
		jfaceFontInfo.name = jfaceNameConstants[jfaceNamesList.getSelectionIndex()];
		switch (jfaceStylesList.getSelectionIndex()) {
			case JFACE_NORMAL:
				jfaceFontInfo.style = JFACE_NORMAL;
				f = JFaceResources.getFontRegistry().get(jfaceFontInfo.name);
				break;
			case JFACE_BOLD:
				jfaceFontInfo.style = JFACE_BOLD;
				f = JFaceResources.getFontRegistry().getBold(jfaceFontInfo.name);
				break;
			case JFACE_ITALIC:
				jfaceFontInfo.style = JFACE_ITALIC;
				f = JFaceResources.getFontRegistry().getItalic(jfaceFontInfo.name);
				break;

			default:
				jfaceFontInfo.style = JFACE_NORMAL;
				f = JFaceResources.getFontRegistry().get(jfaceFontInfo.name);
				break;
		}
		f = new Font(this.getDisplay(), f.getFontData());
		previewText.setFont(f);
		if (value != null && !value.isDisposed()) {
			value.dispose();
		}
		setValue(f);
	}
	private void updateSelections() {
		if (value == null) { return; }
		isUpdating = true;
		FontData fd = value.getFontData()[0];
		namesList.setSelection(new String[] { fd.getName()});
		nameField.setText(fd.getName());
		for (int i = 0; i < styleValues.length; i++) {
			if (fd.getStyle() == styleValues[i]) {
				stylesList.setSelection(i);
				styleField.setText(styleNames[i]);
				break;
			}
		}
		for (int i = 0; i < sizeValues.length; i++) {
			if (fd.getHeight() == sizeValues[i]) {
				sizesList.setSelection(i);
				break;
			}
		}
		sizeField.setText(String.valueOf(fd.getHeight()));
		previewText.setFont(value);
		isUpdating = false;
	}

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		if (fPropertyChangeListeners == null) {
			fPropertyChangeListeners = new ArrayList(1);
		}
		fPropertyChangeListeners.add(listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		if (fPropertyChangeListeners != null) {
			fPropertyChangeListeners.remove(listener);
		}
	}

	public void setExistingValue(IJavaObjectInstance existingValue) {
		fExistingValue = existingValue;
		// force getting font when creating dialog
		this.value = null;
	}

	/**
	 * This method initializes tabFolder	
	 *
	 */    
	private void createTabFolder() {
		GridData gridData4 = new org.eclipse.swt.layout.GridData();
		tabFolder = new TabFolder(this, SWT.NONE);		   
		createComposite();
		createComposite1();
		TabItem tabItem1 = new TabItem(tabFolder, SWT.NONE);
		TabItem tabItem2 = new TabItem(tabFolder, SWT.NONE);
		tabItem1.setControl(composite);
		tabItem1.setText("Named Fonts");
		tabItem2.setControl(jfaceFontsTab);
		tabItem2.setText("JFace Fonts");
		gridData4.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData4.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData4.grabExcessHorizontalSpace = true;
		gridData4.grabExcessVerticalSpace = true;
		gridData4.horizontalSpan = 3;
		tabFolder.setLayoutData(gridData4);
	}

	/**
	 * This method initializes composite	
	 *
	 */    
	private void createComposite() {
		GridLayout gridLayout1 = new GridLayout();
		composite = new Composite(tabFolder, SWT.NONE);
		composite.setLayout(gridLayout1);
		Label nameLabel = new Label(composite, SWT.NONE);
		nameLabel.setText(FontPropertyEditorMessages.getString("nameLabel")); //$NON-NLS-1$

		Label styleLabel = new Label(composite, SWT.NONE);
		styleLabel.setText(FontPropertyEditorMessages.getString("styleLabel")); //$NON-NLS-1$
		
		Label sizeLabel = new Label(composite, SWT.NONE);
		sizeLabel.setText(FontPropertyEditorMessages.getString("sizeLabel")); //$NON-NLS-1$
		
		nameField = new Text(composite, SWT.SINGLE | SWT.BORDER);
		GridData gd01 = new GridData();
		gd01.horizontalAlignment = GridData.FILL;
		gd01.grabExcessHorizontalSpace = true;
		nameField.setLayoutData(gd01);
		
		styleField = new Text(composite, SWT.SINGLE | SWT.BORDER | SWT.READ_ONLY);
		GridData gd11 = new GridData();
		gd11.horizontalAlignment = GridData.FILL;
		styleField.setLayoutData(gd11);
		
		sizeField = new Text(composite, SWT.SINGLE | SWT.BORDER);
		GridData gd21 = new GridData();
		gd21.horizontalAlignment = GridData.FILL;
		sizeField.setLayoutData(gd21);
		
		namesList = new List(composite, SWT.SINGLE | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		GridData gd02 = new GridData();
		gd02.horizontalAlignment = GridData.FILL;
		gd02.verticalAlignment = GridData.FILL;
		gd02.grabExcessHorizontalSpace = true;
		gd02.grabExcessVerticalSpace = true;
		gd02.heightHint = 100;
		gd02.widthHint = 200;
		namesList.setLayoutData(gd02);
		
		stylesList = new List(composite, SWT.SINGLE | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		GridData gd12 = new GridData();
		gd12.horizontalAlignment = GridData.FILL;
		gd12.verticalAlignment = GridData.FILL;
		gd12.grabExcessVerticalSpace = true;
		stylesList.setLayoutData(gd12);

		sizesList = new List(composite, SWT.SINGLE | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		GridData gd22 = new GridData();
		gd22.horizontalAlignment = GridData.FILL;
		gd22.verticalAlignment = GridData.FILL;
		gd22.grabExcessVerticalSpace = true;
		gd22.heightHint = 100;
		gd22.widthHint = 40;
		sizesList.setLayoutData(gd22);
		gridLayout1.numColumns = 3;

		namesList.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				if (!isUpdating) {
					String newSelection = namesList.getSelection()[0];
					isUpdating = true;
					nameField.setText(newSelection);
					isUpdating = false;
				}
				updateFont();
			}
		});

		nameField.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				if (!isUpdating) {
					int newIndex = searchFontNames(nameField.getText());
					if (namesList.getSelectionIndex() != newIndex) {
						isUpdating = true;
						namesList.setSelection(newIndex);
						isUpdating = false;
						updateFont();
					}
				}
			}
		});

		nameField.addMouseListener(new MouseAdapter() {

			public void mouseUp(MouseEvent e) {
				nameField.selectAll();
			}
		});

		stylesList.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				String newSelection = stylesList.getSelection()[0];
				styleField.setText(newSelection);
				updateFont();
			}
		});

		sizesList.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				String newSelection = sizesList.getSelection()[0];
				sizeField.setText(newSelection);
				updateFont();
			}
		});

		sizeField.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				String newText = sizeField.getText();
				int newSize = 1;
				if (newText.length() > 0) {
					try {
						newSize = Integer.parseInt(newText);
						if (newSize < 1) { throw new NumberFormatException(); }
					} catch (NumberFormatException e1) {
						sizeField.setText(String.valueOf(value.getFontData()[0].getHeight()));
						return;
					}
				}
				updateFont();
				sizesList.deselectAll();
				for (int i = 0; i < sizeValues.length; i++) {
					if (newSize == sizeValues[i]) {
						sizesList.setSelection(i);
						break;
					}
				}
			}
		});

		sizeField.addMouseListener(new MouseAdapter() {

			public void mouseUp(MouseEvent e) {
				sizeField.selectAll();
			}
		});

	}

	/**
	 * This method initializes composite1	
	 *
	 */    
	private void createComposite1() {
		GridData gridData7 = new org.eclipse.swt.layout.GridData();
		GridData gridData6 = new org.eclipse.swt.layout.GridData();
		GridData gridData41 = new org.eclipse.swt.layout.GridData();
		GridData gridData3 = new org.eclipse.swt.layout.GridData();
		GridLayout gridLayout11 = new GridLayout();
		jfaceFontsTab = new Composite(tabFolder, SWT.NONE);		   
		jfaceNameLabel = new Label(jfaceFontsTab, SWT.NONE);
		jfaceStyleLabel = new Label(jfaceFontsTab, SWT.NONE);
		jfaceNameField = new Text(jfaceFontsTab, SWT.BORDER);
		jfaceStyleField = new Text(jfaceFontsTab, SWT.BORDER | SWT.READ_ONLY);
		jfaceNamesList = new List(jfaceFontsTab, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		jfaceStylesList = new List(jfaceFontsTab, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		gridData3.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData3.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		gridData3.grabExcessHorizontalSpace = true;
		jfaceNameField.setLayoutData(gridData3);
		gridData41.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData41.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		jfaceStyleField.setLayoutData(gridData41);
		gridData6.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData6.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData6.grabExcessHorizontalSpace = true;
		gridData6.grabExcessVerticalSpace = true;
		gridData6.heightHint = 100;
		gridData6.widthHint = 200;
		jfaceNamesList.setLayoutData(gridData6);
		gridData7.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData7.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData7.grabExcessVerticalSpace = true;
		jfaceStylesList.setLayoutData(gridData7);
		jfaceFontsTab.setLayout(gridLayout11);
		gridLayout11.numColumns = 2;
		jfaceNameLabel.setText("Name");
		jfaceStyleLabel.setText("style");
		jfaceNamesList.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() { 
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {    
				if (!isUpdating) {
					String newSelection = jfaceNamesList.getSelection()[0];
					isUpdating = true;
					jfaceNameField.setText(newSelection);
					isUpdating = false;
				}
				updateJFaceFont();
			}
		});
		jfaceStylesList.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				String newSelection = jfaceStylesList.getSelection()[0];
				jfaceStyleField.setText(newSelection);
				updateJFaceFont();
			}
		});
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
