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
 *  $Revision: 1.1 $  $Date: 2005-03-23 22:44:48 $ 
 */
package org.eclipse.ve.internal.swt;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.*;

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

	protected Font value;

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

	public FontCustomPropertyEditor(Composite parent, int style, Font value, IJavaObjectInstance existingValue) {
		super(parent, style);
		this.value = value;
		fExistingValue = existingValue;
		initialize();
	}

	private void initialize() {
		setSize(new org.eclipse.swt.graphics.Point(300, 200));
		GridLayout grid = new GridLayout();
		grid.numColumns = 3;
		grid.marginHeight = 5;
		grid.marginWidth = 5;
		grid.horizontalSpacing = 5;
		grid.verticalSpacing = 5;
		this.setLayout(grid);

		Label nameLabel = new Label(this, SWT.NONE);
		nameLabel.setText(FontPropertyEditorMessages.getString("nameLabel")); //$NON-NLS-1$

		Label styleLabel = new Label(this, SWT.NONE);
		styleLabel.setText(FontPropertyEditorMessages.getString("styleLabel")); //$NON-NLS-1$

		Label sizeLabel = new Label(this, SWT.NONE);
		sizeLabel.setText(FontPropertyEditorMessages.getString("sizeLabel")); //$NON-NLS-1$

		nameField = new Text(this, SWT.SINGLE | SWT.BORDER);
		GridData gd01 = new GridData();
		gd01.horizontalAlignment = GridData.FILL;
		gd01.grabExcessHorizontalSpace = true;
		nameField.setLayoutData(gd01);

		styleField = new Text(this, SWT.SINGLE | SWT.BORDER | SWT.READ_ONLY);
		GridData gd11 = new GridData();
		gd11.horizontalAlignment = GridData.FILL;
		styleField.setLayoutData(gd11);

		sizeField = new Text(this, SWT.SINGLE | SWT.BORDER);
		GridData gd21 = new GridData();
		gd21.horizontalAlignment = GridData.FILL;
		sizeField.setLayoutData(gd21);

		namesList = new List(this, SWT.SINGLE | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		GridData gd02 = new GridData();
		gd02.horizontalAlignment = GridData.FILL;
		gd02.verticalAlignment = GridData.FILL;
		gd02.grabExcessHorizontalSpace = true;
		gd02.grabExcessVerticalSpace = true;
		gd02.heightHint = 100;
		gd02.widthHint = 200;
		namesList.setLayoutData(gd02);

		stylesList = new List(this, SWT.SINGLE | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		GridData gd12 = new GridData();
		gd12.horizontalAlignment = GridData.FILL;
		gd12.verticalAlignment = GridData.FILL;
		gd12.grabExcessVerticalSpace = true;
		stylesList.setLayoutData(gd12);

		sizesList = new List(this, SWT.SINGLE | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		GridData gd22 = new GridData();
		gd22.horizontalAlignment = GridData.FILL;
		gd22.verticalAlignment = GridData.FILL;
		gd22.grabExcessVerticalSpace = true;
		gd22.heightHint = 100;
		gd22.widthHint = 40;
		sizesList.setLayoutData(gd22);

		previewText = new Text(this, SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.H_SCROLL | SWT.V_SCROLL);
		previewText.setText(FontPropertyEditorMessages.getString("previewText")); //$NON-NLS-1$
		GridData gd03 = new GridData();
		gd03.horizontalSpan = 3;
		gd03.horizontalAlignment = GridData.FILL;
		gd03.verticalAlignment = GridData.FILL;
		gd03.grabExcessHorizontalSpace = true;
		gd03.grabExcessVerticalSpace = true;
		gd03.heightHint = 50;
		gd03.widthHint = 500;
		previewText.setLayoutData(gd03);

		initializeLists();

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

}
