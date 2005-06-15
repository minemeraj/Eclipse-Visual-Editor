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
 *  $RCSfile: ColorCustomPropertyEditor.java,v $
 *  $Revision: 1.7 $  $Date: 2005-06-15 20:19:21 $ 
 */
package org.eclipse.ve.internal.swt;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.jface.preference.JFacePreferences;
import org.eclipse.jface.resource.ColorRegistry;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

import org.eclipse.jem.internal.instantiation.*;
import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.jem.internal.proxy.core.*;

import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.java.core.BeanProxyUtilities;

public class ColorCustomPropertyEditor extends Composite {
	
	private java.util.List fPropertyChangeListeners;
	
	private static final String COLOR_PREFIX = "org.eclipse.swt.SWT.COLOR_"; //$NON-NLS-1$
	
	private static final String[] basicColorNames = { ColorPropertyEditorMessages.getString("black"), ColorPropertyEditorMessages.getString("blue"), ColorPropertyEditorMessages.getString("cyan"), ColorPropertyEditorMessages.getString("gray"), ColorPropertyEditorMessages.getString("green"), ColorPropertyEditorMessages.getString("magenta"), ColorPropertyEditorMessages.getString("red"), ColorPropertyEditorMessages.getString("white"), ColorPropertyEditorMessages.getString("yellow"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$
		ColorPropertyEditorMessages.getString("darkBlue"), ColorPropertyEditorMessages.getString("darkCyan"), ColorPropertyEditorMessages.getString("darkGray"), ColorPropertyEditorMessages.getString("darkGreen"), ColorPropertyEditorMessages.getString("darkMagenta"), ColorPropertyEditorMessages.getString("darkRed"), ColorPropertyEditorMessages.getString("darkYellow") }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$
	
	private static final String[] basicColorConstants = { "BLACK", "BLUE", "CYAN", "GRAY", "GREEN", "MAGENTA", "RED", "WHITE", "YELLOW", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$
		"DARK_BLUE", "DARK_CYAN", "DARK_GRAY", "DARK_GREEN", "DARK_MAGENTA", "DARK_RED", "DARK_YELLOW" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$
	
	private static final int[] basicColorConstantValues = { SWT.COLOR_BLACK, SWT.COLOR_BLUE, SWT.COLOR_CYAN, SWT.COLOR_GRAY, SWT.COLOR_GREEN,
		SWT.COLOR_MAGENTA, SWT.COLOR_RED, SWT.COLOR_WHITE, SWT.COLOR_YELLOW,
		SWT.COLOR_DARK_BLUE, SWT.COLOR_DARK_CYAN, SWT.COLOR_DARK_GRAY, SWT.COLOR_DARK_GREEN, SWT.COLOR_DARK_MAGENTA, SWT.COLOR_DARK_RED, 
		SWT.COLOR_DARK_YELLOW };
		
	private static Color[] basicColorValues = new Color[basicColorConstantValues.length];
	private Image basicColorImages[] = new Image[basicColorValues.length];
		
	private static final String[] systemColorNames = { ColorPropertyEditorMessages.getString("infoBackground"), ColorPropertyEditorMessages.getString("infoForeground"),  //$NON-NLS-1$ //$NON-NLS-2$
		ColorPropertyEditorMessages.getString("listBackground"), ColorPropertyEditorMessages.getString("listForeground"), ColorPropertyEditorMessages.getString("listSeletion"), ColorPropertyEditorMessages.getString("listSelectionText"),  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		ColorPropertyEditorMessages.getString("titleBackground"), ColorPropertyEditorMessages.getString("titleBackgroundGradient"), ColorPropertyEditorMessages.getString("titleForeground"), ColorPropertyEditorMessages.getString("titleInactiveBackground"), ColorPropertyEditorMessages.getString("titleInactiveBackgroundGradient"),  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
		ColorPropertyEditorMessages.getString("titleInactiveForeground"), //$NON-NLS-1$
		ColorPropertyEditorMessages.getString("widgetBackground"), ColorPropertyEditorMessages.getString("widgetBorder"), ColorPropertyEditorMessages.getString("widgetDarkShadow"), ColorPropertyEditorMessages.getString("widgetForeground"), ColorPropertyEditorMessages.getString("widgetHighlightShadow"), ColorPropertyEditorMessages.getString("widgetLightShadow"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
		ColorPropertyEditorMessages.getString("widgetNormalShadow") }; //$NON-NLS-1$

	private static final String[] systemColorConstants = { "INFO_BACKGROUND", "INFO_FOREGROUND",  //$NON-NLS-1$ //$NON-NLS-2$
		"LIST_BACKGROUND", "LIST_FOREGROUND", "LIST_SELECTION", "LIST_SELECTION_TEXT",  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		"TITLE_BACKGROUND", "TITLE_BACKGROUND_GRADIENT", "TITLE_FOREGROUND", "TITLE_INACTIVE_BACKGROUND",  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		"TITLE_INACTIVE_BACKGROUND_GRADIENT", "TITLE_INACTIVE_FOREGROUND",  //$NON-NLS-1$ //$NON-NLS-2$
		"WIDGET_BACKGROUND", "WIDGET_BORDER", "WIDGET_DARK_SHADOW", "WIDGET_FOREGROUND", "WIDGET_HIGHLIGHT_SHADOW", "WIDGET_LIGHT_SHADOW",  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
		"WIDGET_NORMAL_SHADOW" }; //$NON-NLS-1$
		
	private static final int[] systemColorConstantValues = { SWT.COLOR_INFO_BACKGROUND, SWT.COLOR_INFO_FOREGROUND, 
		SWT.COLOR_LIST_BACKGROUND, SWT.COLOR_LIST_FOREGROUND, SWT.COLOR_LIST_SELECTION, SWT.COLOR_LIST_SELECTION_TEXT, 
		SWT.COLOR_TITLE_BACKGROUND, SWT.COLOR_TITLE_BACKGROUND_GRADIENT, SWT.COLOR_TITLE_FOREGROUND,
		SWT.COLOR_TITLE_INACTIVE_BACKGROUND, SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT, SWT.COLOR_TITLE_INACTIVE_FOREGROUND, 
		SWT.COLOR_WIDGET_BACKGROUND, SWT.COLOR_WIDGET_BORDER, SWT.COLOR_WIDGET_DARK_SHADOW, SWT.COLOR_WIDGET_FOREGROUND, 
		SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW, SWT.COLOR_WIDGET_LIGHT_SHADOW, SWT.COLOR_WIDGET_NORMAL_SHADOW };
		
	private static Color[] systemColorValues = new Color[systemColorConstantValues.length];
	private Image systemColorImages[] = new Image[systemColorValues.length]; 

	// JFace colors from JFacePreferences
	private static final String[] jfaceColorNames = { ColorPropertyEditorMessages.getString("hyperlink"), ColorPropertyEditorMessages.getString("active_hyperlink"), ColorPropertyEditorMessages.getString("error")}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ 
	
	private static final String[] jfaceColorConstantValues = {JFacePreferences.HYPERLINK_COLOR, JFacePreferences.ACTIVE_HYPERLINK_COLOR, JFacePreferences.ERROR_COLOR};
	protected final static String[] jfaceColorInitStrings = { 
		"org.eclipse.jface.preference.JFacePreferences.HYPERLINK_COLOR", //$NON-NLS-1$ 
		"org.eclipse.jface.preference.JFacePreferences.ACTIVE_HYPERLINK_COLOR", //$NON-NLS-1$ 
		"org.eclipse.jface.preference.JFacePreferences.ERROR_COLOR"}; //$NON-NLS-1$ 
		
	private static Color[] jfaceColorValues = new Color[jfaceColorConstantValues.length];
	private Image jfaceColorImages[] = new Image[jfaceColorValues.length];

	
	private IJavaObjectInstance fExistingValue;
	
	private TabFolder tabFolder = null;
	private Composite namedValueComposite = null;
	private Composite rgbComposite = null;
	private Composite rgbPanel = null;
	private Composite preview = null;	
	
	private Table basicTable;
	private Table systemTable;
	
	private Spinner redSpinner;
	private Spinner greenSpinner;
	private Spinner blueSpinner;
	
	private Scale redScale;
	private Scale greenScale;
	private Scale blueScale;
	
	private Label blueL;
	private Label greenL;
	private Label redL;
	
	private Button externalChooser;
	
	private Group basicGroup;
	private Group systemGroup;
	
	private Color black;
	private Color value;
	
	private boolean isNamed = false;
	private boolean isBasic = false;
	private boolean isSystem = false;
	private boolean changeInProcess = false;
	private boolean isJFace = false;
	private int basicColorSelection, systemColorSelection, jfaceColorSelection;
	
	private static final int NAMED_SWATCH_SIZE = 10;
	private static final int NAMED_LIST_HEIGHT = 175;
	private static final int NAMED_LIST_WIDTH = 175;
	

	private Composite jfaceColorComposite = null;

	private Table jfaceColorTable = null;
	private Label initStringLabel = null;
	private EditDomain fEditDomain;

	// preview pane fields and constants
	private Group previewGroup;
	private Canvas previewCanvas;
	private Color initialColor;
	private Color white;
	private static final int spacing = 5;
	private static final int bigRect = 25;
	private static final int medRect = 15;
	private static final int smRect = 5;
	private static final int swatchWidth  = 50;
	private static final int swatchHeight = 25;
	private final String previewText = ColorPropertyEditorMessages.getString("previewText"); //$NON-NLS-1$

	private boolean lookupIsJFaceProject = true; // lookup JFace plugin only once
	private boolean isJFaceProject = false;

	public ColorCustomPropertyEditor(Composite parent, int style, Color value, IJavaObjectInstance existingValue, EditDomain editDomain) {
		super(parent, style);
		this.value = value;
		fEditDomain = editDomain;
		fExistingValue = existingValue;
		initialize();
	}

	private void initialize() {
		GridData gridData3 = new org.eclipse.swt.layout.GridData();
		setSize(new org.eclipse.swt.graphics.Point(433,340));
		GridLayout grid = new GridLayout();
		grid.numColumns = 1;
		grid.verticalSpacing = 5;
		this.setLayout(grid);
		
		createColorFromProxy(this);
		
		black = this.getDisplay().getSystemColor(SWT.COLOR_BLACK);
		
		// Setup default initial color if it's not set yet
		if (value == null) {
			setColor(this.getDisplay().getSystemColor(SWT.COLOR_DARK_GREEN), true);
			isBasic = true;
		}
		
		createTabFolder();
		
		if ( ! isNamed ) {
			tabFolder.setSelection(1);
		}
	
		createPreviewPanel();
		GridData previewGD = new GridData();
		previewGD.verticalAlignment = GridData.CENTER;
		previewGD.horizontalAlignment = GridData.FILL;
		previewGD.grabExcessHorizontalSpace = true;
		previewGD.grabExcessVerticalSpace = false;
		gridData3.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData3.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		preview.setLayoutData(previewGD);

		initStringLabel = new Label(this, SWT.NONE);
		initStringLabel.setText(""); //$NON-NLS-1$
		initStringLabel.setLayoutData(gridData3);
		initStringLabel.setForeground(org.eclipse.swt.widgets.Display.getCurrent().getSystemColor(org.eclipse.swt.SWT.COLOR_BLUE));
		
		updateSelections();
	}
	

	/*
	 * Based on the current existing color value, select the appropriate tab and table selection. 
	 */
	private void updateSelections() {
		if (fExistingValue != null && fExistingValue.getAllocation() instanceof ParseTreeAllocation) {
			ParseTreeAllocation ptAlloc = (ParseTreeAllocation) fExistingValue.getAllocation();
			PTExpression exp = ptAlloc.getExpression();
			if (exp instanceof PTMethodInvocation && ((PTMethodInvocation) exp).getReceiver() instanceof PTMethodInvocation) {
				PTExpression arg = (PTExpression) ((PTMethodInvocation) exp).getArguments().get(0);
				String methodName = ((PTMethodInvocation) ((PTMethodInvocation) exp).getReceiver()).getName();
				if (methodName.equals("getColorRegistry") && isJFaceProject()) { //$NON-NLS-1$
					// set the JFace page
					for (int i = 0; i < jfaceColorValues.length; i++) {
						if (jfaceColorValues[i].equals(value)) {
							jfaceColorTable.setSelection(i);
							isJFace = true;
							tabFolder.setSelection(2);
							break;
						}
					}
				} else if (methodName.equals("getDefault") || methodName.equals("getCurrent")) { //$NON-NLS-1$	//$NON-NLS-2$
					if (arg instanceof PTFieldAccess) {
						int selection = -1;
						String fieldname = ((PTFieldAccess) arg).getField().replaceAll("COLOR_", ""); //$NON-NLS-1$ //$NON-NLS-2$
						for (int i = 0; selection == -1 && i < basicColorConstants.length; i++) {
							if (fieldname.equals(basicColorConstants[i])) {
								selection = i;
								basicTable.setSelection(selection);
								basicColorSelection = i;
								isNamed = true;
								isBasic = true;
							}
						}
						for (int i = 0; selection == -1 && i < systemColorConstants.length; i++) {
							if (fieldname.equals(systemColorConstants[i])) {
								selection = i;
								systemTable.setSelection(selection);
								systemColorSelection = i;
								isNamed = true;
								isSystem = true;
							}
						}
						if (isNamed)
							tabFolder.setSelection(0);
					}
				}
			}
		}
		updateLabelInitializationString();
	}

	private void createTabFolder() {
		GridData tabGD = new GridData();
		tabFolder = new TabFolder(this, SWT.NONE);
		createNamedValueComposite();
		createRGBComposite();
		TabItem namedPage = new TabItem(tabFolder, SWT.NONE);
		TabItem rgbPage = new TabItem(tabFolder, SWT.NONE);
		tabGD.verticalAlignment = GridData.FILL;
		tabGD.horizontalAlignment = GridData.FILL;
		tabGD.grabExcessHorizontalSpace = true;
		tabGD.grabExcessVerticalSpace = true;
		tabFolder.setLayoutData(tabGD);		
		namedPage.setText(ColorPropertyEditorMessages.getString("namedTabTitle")); //$NON-NLS-1$
		namedPage.setControl(namedValueComposite);
		rgbPage.setText(ColorPropertyEditorMessages.getString("rgbTabTitle")); //$NON-NLS-1$
		rgbPage.setControl(rgbComposite);
		if (isJFaceProject()) {
			createJfaceColorComposite();
			TabItem tabItem1 = new TabItem(tabFolder, SWT.NONE);
			tabItem1.setControl(jfaceColorComposite);
			tabItem1.setText(ColorPropertyEditorMessages.getString("jfaceTabTitle")); //$NON-NLS-1$
		}
	}
	
	private void createNamedValueComposite(){
		RowLayout rowLayout = new RowLayout();
		namedValueComposite = new Composite(tabFolder, SWT.NONE);
		namedValueComposite.setLayout(rowLayout);
		rowLayout.wrap = false;
		rowLayout.pack = true;
		rowLayout.justify = true;
		
		initializeColorImages(namedValueComposite.getDisplay());
		
		basicGroup = new Group(namedValueComposite, SWT.NONE);
		basicGroup.setText(ColorPropertyEditorMessages.getString("basicColorsGroupTitle")); //$NON-NLS-1$
		basicGroup.setLayout(new RowLayout());
		
		basicTable = new Table(basicGroup, SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER | SWT.FULL_SELECTION);
		basicTable.setHeaderVisible(false);
		basicTable.setLinesVisible(false);
		
		for (int i = 0; i < basicColorNames.length; i++) {
			TableItem ti = new TableItem(basicTable, SWT.NONE);
			ti.setText(basicColorNames[i]);
			ti.setData(new Integer(i));
			ti.setImage(basicColorImages[i]);
		}
		RowData bRD = new RowData();
		bRD.width = NAMED_LIST_WIDTH;
		bRD.height = NAMED_LIST_HEIGHT;
		basicTable.setLayoutData(bRD);
		
		basicTable.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				changeSelection((Table)e.widget);
			}

			public void widgetDefaultSelected(SelectionEvent e) {
				changeSelection((Table)e.widget);
			}
			
			private void changeSelection(Table t) {
				TableItem items[] = t.getSelection();
				if (items.length > 0) {
					int value = ((Integer)items[0].getData()).intValue();
					if (value < basicColorValues.length) {
						changeInProcess = true;
						setColor(basicColorValues[value], true);
						basicColorSelection = value;
						systemTable.deselectAll();
						deSelectJFaceColorTable();
						updateSpinnersFromColor();
						isBasic = true;
						changeInProcess = false;
						updateLabelInitializationString();
					}
				}
			}
		});
		
		systemGroup = new Group(namedValueComposite, SWT.NONE);
		systemGroup.setText(ColorPropertyEditorMessages.getString("SystemColorsGroupTitle")); //$NON-NLS-1$
		systemGroup.setLayout(new RowLayout());
		
		systemTable = new Table(systemGroup, SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER | SWT.FULL_SELECTION);
		systemTable.setHeaderVisible(false);
		systemTable.setLinesVisible(false);
		
		for (int i = 0; i < systemColorNames.length; i++) {
			TableItem ti = new TableItem(systemTable, SWT.NONE);
			ti.setText(systemColorNames[i]);
			ti.setData(new Integer(i));
			ti.setImage(systemColorImages[i]);
		}
		RowData sRD = new RowData();
		sRD.width = NAMED_LIST_WIDTH;
		sRD.height = NAMED_LIST_HEIGHT;
		systemTable.setLayoutData(sRD);
		
		systemTable.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				changeSelection((Table)e.widget);
			}

			public void widgetDefaultSelected(SelectionEvent e) {
				changeSelection((Table)e.widget);
			}
			
			private void changeSelection(Table t) {
				TableItem items[] = t.getSelection();
				if (items.length > 0) {
					int value = ((Integer)items[0].getData()).intValue();
					if (value < systemColorValues.length) {
						changeInProcess = true;
						setColor(systemColorValues[value], true);
						systemColorSelection = value;
						basicTable.deselectAll();
						deSelectJFaceColorTable();
						updateSpinnersFromColor();
						isSystem = true;
						changeInProcess = false;
						updateLabelInitializationString();
					}
				}
			}
		});
		
		namedValueComposite.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				Image img;
				for (int i = 0; i < basicColorImages.length; i++) {
					img = basicColorImages[i];
					if (img != null && ! img.isDisposed()){
						img.dispose();
						img = null;		
					}
				}
				for (int i = 0; i < systemColorImages.length; i++) {
					img = systemColorImages[i];
					if (img != null && ! img.isDisposed()){
						img.dispose();
						img = null;		
					}
				}
			}
		});
	}
	
	private void createRGBComposite(){
		GridLayout grid = new GridLayout();
		rgbComposite = new Composite(tabFolder, SWT.NONE);
		grid.numColumns = 1;
		grid.verticalSpacing = 5;
		grid.marginHeight = 5;
		grid.marginWidth = 5;
		rgbComposite.setLayout(grid);
		
		createRGBPanel();
		
		externalChooser = new Button(rgbComposite, SWT.PUSH);
		externalChooser.setText(ColorPropertyEditorMessages.getString("advancedButton")); //$NON-NLS-1$
		GridData GD2 = new GridData();
		GD2.grabExcessHorizontalSpace = true;
		GD2.grabExcessVerticalSpace = true;
		GD2.horizontalAlignment = GridData.END;
		GD2.verticalAlignment = GridData.END;
		externalChooser.setLayoutData(GD2);
		externalChooser.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				Shell shell = e.widget.getDisplay().getActiveShell();
				ColorDialog c = new ColorDialog(shell, SWT.APPLICATION_MODAL);
				RGB result = c.open();
				if (result != null) {
					redSpinner.setSelection(result.red);
					greenSpinner.setSelection(result.green);
					blueSpinner.setSelection(result.blue);
				}
			}
		});	
	}
	
	private void createRGBPanel() {		
		GridLayout grid2 = new GridLayout();
		GridData GD1 = new GridData();
		rgbPanel = new Composite(rgbComposite, SWT.NONE);
		rgbPanel.setLayoutData(GD1);
		rgbPanel.setLayout(grid2);
		GD1.grabExcessHorizontalSpace = true;
		GD1.grabExcessVerticalSpace = true;
		GD1.horizontalAlignment = GridData.CENTER;
		GD1.verticalAlignment = GridData.CENTER;
		redL = new Label(rgbPanel, SWT.NONE);		
		redScale = new Scale(rgbPanel, SWT.NONE);
		redSpinner = new Spinner(rgbPanel, SWT.NONE);
		greenL = new Label(rgbPanel, SWT.NONE);		
		greenScale = new Scale(rgbPanel, SWT.NONE);
		greenSpinner = new Spinner(rgbPanel, SWT.NONE);		
		blueL = new Label(rgbPanel, SWT.NONE);		
		blueScale = new Scale(rgbPanel, SWT.NONE);		
		blueSpinner = new Spinner(rgbPanel, SWT.NONE);
		grid2.numColumns = 3;
		grid2.verticalSpacing = 5;
		grid2.horizontalSpacing = 10;
		grid2.marginHeight = 5;
		grid2.marginWidth = 5;
		
		redL.setText(ColorPropertyEditorMessages.getString("redSliderLabel")); //$NON-NLS-1$
		
		redScale.setMinimum(0);
		redScale.setMaximum(255);
		redScale.setPageIncrement(51);
		redScale.setSelection(value.getRed());
		
		redSpinner.setMinimum(0);
		redSpinner.setMaximum(255);
		redSpinner.setSelection(value.getRed());

		greenL.setText(ColorPropertyEditorMessages.getString("greenSliderLabel")); //$NON-NLS-1$

		greenScale.setMinimum(0);
		greenScale.setMaximum(255);
		greenScale.setPageIncrement(51);
		greenScale.setSelection(value.getGreen());

		greenSpinner.setMinimum(0);
		greenSpinner.setMaximum(255);
		greenSpinner.setSelection(value.getGreen());		

		blueL.setText(ColorPropertyEditorMessages.getString("blueSliderLabel")); //$NON-NLS-1$
		
		blueScale.setMinimum(0);
		blueScale.setMaximum(255);
		blueScale.setPageIncrement(51);
		blueScale.setSelection(value.getBlue());
				
		blueSpinner.setMinimum(0);
		blueSpinner.setMaximum(255);
		blueSpinner.setSelection(value.getBlue());

		ModifyListener modifyListener = new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				Spinner s = (Spinner)e.widget;
				if (s == redSpinner) {
					redScale.setSelection(redSpinner.getSelection());
				} else if (s == greenSpinner) {
					greenScale.setSelection(greenSpinner.getSelection());
				} else if (s == blueSpinner) {
					blueScale.setSelection(blueSpinner.getSelection());
				}
				s.setEnabled(true);
				if (!changeInProcess) {
					updateColorFromSpinners();
					basicTable.deselectAll();
					systemTable.deselectAll();
				}
			}
		};
		redSpinner.addModifyListener(modifyListener);
		greenSpinner.addModifyListener(modifyListener);
		blueSpinner.addModifyListener(modifyListener);
		
		SelectionListener scaleListener = new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (e.widget == redScale) {
					redSpinner.setSelection(redScale.getSelection());
					redSpinner.setEnabled(true);
				} else if (e.widget == greenScale) {
					greenSpinner.setSelection(greenScale.getSelection());
					greenSpinner.setEnabled(true);
				} else if (e.widget == blueScale) {
					blueSpinner.setSelection(blueScale.getSelection());
					blueSpinner.setEnabled(true);
				}
			}
		};
		redScale.addSelectionListener(scaleListener);
		blueScale.addSelectionListener(scaleListener);
		greenScale.addSelectionListener(scaleListener);
	}
	
	private void updateSpinnersFromColor() {
		Color c = (Color) getValue();
		redSpinner.setSelection(c.getRed());
		greenSpinner.setSelection(c.getGreen());		
		blueSpinner.setSelection(c.getBlue());
	}
	
	private void initializeColorConstants(Device d) {
		// check to see that it's not already initialized
		if (basicColorValues[0] == null || basicColorValues[0].isDisposed()) {
			for (int i = 0; i < basicColorConstantValues.length; i++) {
				basicColorValues[i] = d.getSystemColor(basicColorConstantValues[i]);
			}
			for (int i = 0; i < systemColorConstantValues.length; i++) {
				systemColorValues[i] = d.getSystemColor(systemColorConstantValues[i]);
			}
		}
	}
	
	private void initializeColorImages(Device d) {
		initializeColorConstants(d);
		
		for (int i = 0; i < basicColorImages.length; i++) {
			basicColorImages[i] = makeSwatchIcon(d, basicColorValues[i]);
		}
		for (int i = 0; i < systemColorImages.length; i++) {
			systemColorImages[i] = makeSwatchIcon(d, systemColorValues[i]);
		}
	}
	
	private void initializeJfaceColorValues(Device d) {
		// check to see that it's not already initialized
		if (jfaceColorValues[0] == null || jfaceColorValues[0].isDisposed()) {
			ColorRegistry registry = JFaceResources.getColorRegistry();
			for (int i = 0; i < jfaceColorConstantValues.length; i++) {
				jfaceColorValues[i] = new Color(d, registry.getRGB(jfaceColorConstantValues[i]));
			}
		}
	}
	
	private void initializeJfaceColorImages(Device d) {
		initializeJfaceColorValues(d);
		
		for (int i = 0; i < jfaceColorImages.length; i++) {
			jfaceColorImages[i] = makeSwatchIcon(d, jfaceColorValues[i]);
		}
	}
	
	
	private Image makeSwatchIcon(Device d, Color c) {
		Rectangle swatchBounds = new Rectangle(0, 0, NAMED_SWATCH_SIZE, NAMED_SWATCH_SIZE);
		Image img = new Image(d, swatchBounds);
		
		GC draw = new GC(img);
		draw.setBackground(c);
		draw.fillRectangle(swatchBounds);
		draw.setForeground(black);
		draw.drawRectangle(swatchBounds);
		draw.dispose();

		return img;
	}

	private void updateColorFromSpinners() {
		Color c = new Color(this.getDisplay(), redSpinner.getSelection(), 
				greenSpinner.getSelection(), blueSpinner.getSelection());
		setColor(c, false);
		updateLabelInitializationString();
	}

	public Color getColor() {
		return value;
	}
	
	private void setColor(Color c, boolean named) {
		if (value != null && !value.isDisposed() && !isNamed) {
			synchronized(value) {
				value.dispose();
			}
		}
		value = c;
		isNamed = named;
		isBasic = false;
		isSystem = false;
		isJFace = false;
		if (preview != null) {
			paintPreviewPanel();
		}
		// Fire the color change
		if(fPropertyChangeListeners != null){
			Iterator iter = fPropertyChangeListeners.iterator();
			while(iter.hasNext()){
				((PropertyChangeListener)iter.next()).propertyChange(new PropertyChangeEvent(this,"value",c,null)); //$NON-NLS-1$
			}
		}	
	}
	
	private void createPreviewPanel() {
		preview = new Composite(this, SWT.NONE);
		GridData previewGD = new GridData();
		previewGD.verticalAlignment = GridData.CENTER;
		previewGD.horizontalAlignment = GridData.FILL;
		previewGD.grabExcessHorizontalSpace = true;
		previewGD.grabExcessVerticalSpace = false;
		preview.setLayoutData(previewGD);
		preview.setLayout(new FillLayout()); 
		previewGroup = new Group(preview, SWT.NONE);
		previewGroup.setText(ColorPropertyEditorMessages.getString("previewGroupTitle")); //$NON-NLS-1$
		RowLayout rowLayout = new RowLayout();
		rowLayout.wrap = false;
		rowLayout.pack = true;
		rowLayout.justify = true;
		previewGroup.setLayout(rowLayout);

		previewCanvas = new Canvas(previewGroup, SWT.NONE);
		black = previewCanvas.getDisplay().getSystemColor(SWT.COLOR_BLACK);
		white = previewCanvas.getDisplay().getSystemColor(SWT.COLOR_WHITE);
		previewCanvas.setLayoutData(new RowData(computePreviewDrawingSize()));
		previewCanvas.addPaintListener(new PaintListener() {

			public void paintControl(PaintEvent e) {
				paintPreviewPanel();
			}
		});

		initialColor = new Color(previewGroup.getDisplay(), getColor().getRGB());
		previewGroup.addDisposeListener(new DisposeListener() {

			public void widgetDisposed(DisposeEvent e) {
				if (initialColor != null && !initialColor.isDisposed()) {
					initialColor.dispose();
					initialColor = null;
				}
			}
		});

		paintPreviewPanel();
	}
		
		private Point computePreviewDrawingSize() {
			GC surface = new GC(previewCanvas);
			Point previewSize = surface.stringExtent(previewText);
			surface.dispose();
			int width = 3 * bigRect + 5 * spacing + swatchWidth + previewSize.x + 5;
			int textHeight = (previewSize.y + 5 ) * 3 + spacing * 2;
			int shapesHeight = bigRect * 2 + spacing; 
			int height = (textHeight > shapesHeight) ? textHeight : shapesHeight;
			
			return new Point(width, height);
		}
		
		private void paintPreviewPanel() {
		int x = 0;
		int y = 0;

		Color current = getColor();

		synchronized (current) {
			if (current == null || current.isDisposed()) { return; }

			GC surface = new GC(previewCanvas);

			// Draw the preview rectangles

			// 0,0
			surface.setBackground(white);
			surface.fillRectangle(x, y, bigRect, bigRect);
			surface.setBackground(current);
			surface.fillRectangle(x + 5, y + 5, medRect, medRect);
			surface.setBackground(white);
			surface.fillRectangle(x + 10, y + 10, smRect, smRect);

			// 0,1
			x = x + bigRect + spacing;
			surface.setBackground(black);
			surface.fillRectangle(x, y, bigRect, bigRect);
			surface.setBackground(current);
			surface.fillRectangle(x + 5, y + 5, medRect, medRect);
			surface.setBackground(white);
			surface.fillRectangle(x + 10, y + 10, smRect, smRect);

			// 0,2
			x = x + bigRect + spacing;
			surface.setBackground(white);
			surface.fillRectangle(x, y, bigRect, bigRect);
			surface.setBackground(current);
			surface.fillRectangle(x + 5, y + 5, medRect, medRect);
			surface.setBackground(black);
			surface.fillRectangle(x + 10, y + 10, smRect, smRect);

			// 1,0
			x = 0;
			y = y + bigRect + spacing;
			surface.setBackground(current);
			surface.fillRectangle(x, y, bigRect, bigRect);

			// 1,1
			x = x + bigRect + spacing;
			surface.setBackground(white);
			surface.fillRectangle(x, y, bigRect, bigRect);
			surface.setBackground(current);
			surface.fillRectangle(x + 5, y + 5, medRect, medRect);

			// 1,2
			x = x + bigRect + spacing;
			surface.setBackground(black);
			surface.fillRectangle(x, y, bigRect, bigRect);
			surface.setBackground(current);
			surface.fillRectangle(x + 5, y + 5, medRect, medRect);

			// Text previews

			x = x + bigRect + spacing;
			y = 0;

			Point textSize = surface.stringExtent(previewText);

			// 1
			surface.setForeground(current);
			surface.drawText(previewText, x + 3, y + 3, true);

			// 2
			y = y + textSize.y + spacing * 2;
			surface.setBackground(current);
			surface.fillRectangle(x, y, textSize.x + 5, textSize.y + 5);
			surface.setForeground(black);
			surface.drawText(previewText, x + 3, y + 3, true);

			// 3
			y = y + textSize.y + spacing * 2;
			surface.setBackground(white);
			surface.fillRectangle(x, y, textSize.x + 5, textSize.y + 5);
			surface.setForeground(current);
			surface.drawText(previewText, x + 3, y + 3, true);

			// Swatches

			// initial
			x = x + textSize.x + 5 + spacing;
			y = 0;
			surface.setBackground(initialColor);
			surface.fillRectangle(x, y, swatchWidth, swatchHeight);

			// current
			y = y + swatchHeight;
			surface.setBackground(current);
			surface.fillRectangle(x, y, swatchWidth, swatchHeight);

			surface.dispose();
		}
	}
	
	public void createColorFromProxy(Control control) {
		if ((fExistingValue == null) || (value != null)) { return; }
		try {
			//type: Color
			IBeanProxy colorProxy = BeanProxyUtilities.getBeanProxy(this.fExistingValue);
			if (colorProxy!=null) {
				IBeanTypeProxy colorType = colorProxy.getTypeProxy();
	
				IMethodProxy getBlue = colorType.getMethodProxy("getBlue"); //$NON-NLS-1$
				IMethodProxy getGreen = colorType.getMethodProxy("getGreen"); //$NON-NLS-1$
				IMethodProxy getRed = colorType.getMethodProxy("getRed"); //$NON-NLS-1$
	
				int blue = ((IIntegerBeanProxy) getBlue.invoke(colorProxy)).intValue();
				int green = ((IIntegerBeanProxy) getGreen.invoke(colorProxy)).intValue();
				int red = ((IIntegerBeanProxy) getRed.invoke(colorProxy)).intValue();
	
				Color color = new Color(control.getDisplay(), red, green, blue); 
				this.value = color;
		    }
		} catch (ThrowableProxy t) {
			//failsafe
		}
	}
	
	public void setValue(Object value) {
		if (value instanceof Color) {
			this.value = (Color) value;
		}
	}
	
	public Object getValue() {
		return value;
	}
	
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		if(fPropertyChangeListeners == null){
			fPropertyChangeListeners = new ArrayList(1);
		}
		fPropertyChangeListeners.add(listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		if(fPropertyChangeListeners != null){
			fPropertyChangeListeners.remove(listener);
		}	
	}

	/* 
	 * Set the existing value into the editor
	 */
	public void setJavaObjectInstanceValue(IJavaObjectInstance value) {
		fExistingValue = value;
		// We have the IDE object that points to the color on the target VM
		
		this.value = null;
	}

	public String getJavaInitializationString() {
		String result = "null"; //$NON-NLS-1$
		if (value == null)
			return result;
		if (value != null) {
			if (!isNamed) {
				result = "new org.eclipse.swt.graphics.Color(org.eclipse.swt.widgets.Display.getCurrent(), " + value.getRed() + ", " + value.getGreen() + ", " + value.getBlue() + ")"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
			} else if (isBasic) {
				result = "org.eclipse.swt.widgets.Display.getCurrent().getSystemColor(" + COLOR_PREFIX + basicColorConstants[basicColorSelection] + ")"; //$NON-NLS-1$ //$NON-NLS-2$
			} else if (isSystem) {
				result = "org.eclipse.swt.widgets.Display.getCurrent().getSystemColor(" + COLOR_PREFIX + systemColorConstants[systemColorSelection] + ")"; //$NON-NLS-1$ //$NON-NLS-2$
			} else if (isJFace) { return "org.eclipse.jface.resource.JFaceResources.getColorRegistry().get(" + jfaceColorInitStrings[jfaceColorSelection] + ")"; //$NON-NLS-1$ //$NON-NLS-2$

			}
		}
		return result;
	}

	/**
	 * This method initializes composite
	 * 
	 */    
	private void createJfaceColorComposite() {
		jfaceColorComposite = new Composite(tabFolder, SWT.NONE);	
		initializeJfaceColorImages(jfaceColorComposite.getDisplay());
		createJFaceColorTable();
		jfaceColorComposite.setLayout(new RowLayout());
	}

	/**
	 * This method initializes table	
	 *
	 */    
	private void createJFaceColorTable() {
		RowData rowData2 = new org.eclipse.swt.layout.RowData();
		jfaceColorTable = new Table(jfaceColorComposite, SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER | SWT.FULL_SELECTION);		   
		rowData2.height = NAMED_LIST_HEIGHT;
		rowData2.width = NAMED_LIST_WIDTH;
		jfaceColorTable.setLayoutData(rowData2);
		for (int i = 0; i < jfaceColorNames.length; i++) {
			TableItem ti = new TableItem(jfaceColorTable, SWT.NONE);
			ti.setText(jfaceColorNames[i]);
			ti.setData(new Integer(i));
			ti.setImage(jfaceColorImages[i]);
			if (value.equals(jfaceColorValues[i])) {
			}
		}
		jfaceColorTable.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				changeSelection((Table)e.widget);
			}

			public void widgetDefaultSelected(SelectionEvent e) {
				changeSelection((Table)e.widget);
			}
			
			private void changeSelection(Table t) {
				TableItem items[] = t.getSelection();
				if (items.length > 0) {
					int index = ((Integer)items[0].getData()).intValue();
					if (index < jfaceColorValues.length) {
						changeInProcess = true;
						setColor(jfaceColorValues[index], true);
						basicTable.deselectAll();
						systemTable.deselectAll();
						updateSpinnersFromColor();
						isJFace = true;
						jfaceColorSelection = index;
						changeInProcess = false;
						updateLabelInitializationString();
					}
				}
			}
		});
	}
	private void deSelectJFaceColorTable() {
		if (isJFaceProject())
			jfaceColorTable.deselectAll();
	}

	/*
	 * Update the init string label at the bottom of the property editor with the respective
	 * initialzation string based on whether it's a SWT or JFace color.
	 */
	private void updateLabelInitializationString() {
		if (value == null)
			return;
		String result = "null"; //$NON-NLS-1$
		String SWT_PREFIX = "SWT."; //$NON-NLS-1$
		if (!isNamed) {
			result = "new Color(Display.getCurrent(), " + value.getRed() + ", " + value.getGreen() + ", " + value.getBlue() + ")"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		} else if (isBasic) {
			result = "Display.getCurrent().getSystemColor(" + SWT_PREFIX + basicColorConstants[basicColorSelection] + ")"; //$NON-NLS-1$ //$NON-NLS-2$
		} else if (isSystem) {
			result = "Display.getCurrent().getSystemColor(" + SWT_PREFIX + systemColorConstants[systemColorSelection] + ")"; //$NON-NLS-1$ //$NON-NLS-2$
		} else if (isJFace) {
			String jfaceConstantName = jfaceColorInitStrings[jfaceColorSelection].replaceAll("org.eclipse.jface.preference.JFacePreferences", //$NON-NLS-1$
					"JFacePreferences"); //$NON-NLS-1$
			result = "JFaceResources.getColorRegistry().get(" + jfaceConstantName + ")"; //$NON-NLS-1$ //$NON-NLS-2$

		}
		initStringLabel.setText(result);
		initStringLabel.setToolTipText(result);
	}

	/*
	 * Return true if the JFace plugin is part of this Java Project.
	 * This is used to determine whether or not to add the JFace page to the property editor. 
	 */
	protected boolean isJFaceProject() {
		// Look this up only once and store the result in the field isJFaceProject
		if (lookupIsJFaceProject) {
			isJFaceProject = BeanSWTUtilities.isJFaceProject(fEditDomain);
		}
		return isJFaceProject;
	}
}  // @jve:decl-index=0:visual-constraint="10,-2"
