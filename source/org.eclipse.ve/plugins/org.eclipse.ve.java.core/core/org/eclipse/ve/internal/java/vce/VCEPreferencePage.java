package org.eclipse.ve.internal.java.vce;
/*******************************************************************************
 * Copyright (c) 2001, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: VCEPreferencePage.java,v $
 *  $Revision: 1.4 $  $Date: 2004-04-20 13:29:03 $ 
 */

import java.util.ArrayList;

import javax.swing.UIManager;

import org.eclipse.core.runtime.*;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Preferences;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.*;

import org.eclipse.ve.internal.cde.core.CDEPlugin;
import org.eclipse.ve.internal.java.core.JavaVEPlugin;
import org.eclipse.ve.internal.java.vce.rules.*;
import org.eclipse.ve.internal.propertysheet.PropertysheetMessages;

public class VCEPreferencePage extends PreferencePage implements IWorkbenchPreferencePage {

	public static final String USER_ITEM = "USER_ITEM"; //$NON-NLS-1$
	public static final String DEFAULT_ITEM = "DEFAULT_ITEM"; //$NON-NLS-1$
	public static final String PROGRAM_ITEM = "PROGRAM_ITEM"; //$NON-NLS-1$
	public static final String PLUGIN_ITEM = "PLUGIN_ITEM"; //$NON-NLS-1$
	public static final String LnF_DEFAULT = VCEMessages.getString("PreferencePage.LookAndFeel.Default"); //$NON-NLS-1$
	protected Preferences fStore;
	protected TabFolder tabFolder;
	protected TabItem appearanceTab;
	protected TabItem codeGenTab;
	protected TabItem stylesTab;
	protected SourceViewer fSourceViewer;
	protected Label lookAndFeelLabel;
	protected Table lookAndFeelTable;
	protected ArrayList fLookAndFeelClasses = new ArrayList(4);
	protected Button showWindowCheckBox;
	protected Button showXMLTextCheckBox;
	protected Button splitRadioButton;
	protected Button notebookRadioButton;
	protected Button showNotebookGEFPaletteButton;
	protected Button showSplitPaneGEFPaletteButton;
	protected Button generateExpressionComment;
	protected Table stylesTable;
	protected StackLayout stylesContributorAreaLayout;
	protected Composite styleContributorArea;
	//    protected Button requireIVJforComponents ;
	protected Button requireIVJforNonComponents;
	protected Button generateTryCatchBlock;
	protected Text sourceSyncDelayText;
	protected Text sourceToVisual;
	protected Text fSourceToJavaBeansTotalTimeLabel;
	protected Text fSourceToJavaBeansTimeLabel;
	protected Group sourceSyncGrp;

	protected Image fJavaBeansViewImage;
	protected Image fPropertiesViewImage;

	protected int fLookAndFeelSelected;
	private Button openPropertiesViewIfRequired;
	private Button openJavaBeansViewIfRequired;

	private TableItem currentLookAndFeelItem;

	private String fStyleID = null;

	protected Label createLabel(Composite group, String aLabelString, Image aLabelImage) {
		Label label = new Label(group, SWT.LEFT);
		if (aLabelImage != null) {
			label.setImage(aLabelImage);
		} else {
			label.setText(aLabelString);
		}
		GridData data = new GridData();
		label.setLayoutData(data);
		return label;
	}

	private Button createCheckBox(Composite group, String label, int indent) {
		Button button = new Button(group, SWT.CHECK | SWT.LEFT);
		button.setText(label);
		GridData data = new GridData();
		data.horizontalIndent = indent;
		button.setLayoutData(data);
		return button;
	}

	protected Group createGroup(Composite aParent, String title, int numColumns) {
		Group group = new Group(aParent, SWT.NONE);
		group.setText(title);
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = numColumns;
		group.setLayout(gridLayout);
		GridData data = new GridData();
		data.verticalAlignment = GridData.FILL_VERTICAL;
		data.horizontalAlignment = GridData.FILL_HORIZONTAL;
		data.grabExcessHorizontalSpace = true;
		group.setLayoutData(data);
		return group;
	}

	protected List createList(Composite group, int heightHint) {
		List list = new List(group, SWT.BORDER | SWT.V_SCROLL);
		GridData data = new GridData();
		data.horizontalAlignment = GridData.FILL;
		data.verticalAlignment = GridData.BEGINNING;
		data.heightHint = heightHint;
		list.setLayoutData(data);
		return list;
	}
	protected Button createRadioButton(Composite parent, String label) {
		Button button = new Button(parent, SWT.RADIO | SWT.LEFT);
		button.setText(label);
		GridData data = new GridData();
		button.setData(data);
		return button;
	}
	protected Text createText(Composite parent, int noCharacters, int span) {

		Text text = new Text(parent, SWT.SINGLE | SWT.BORDER);
		GridData data = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
		data.widthHint = convertWidthInCharsToPixels(noCharacters);
		data.horizontalSpan = span;
		text.setLayoutData(data);
		return text;

	}

	protected Text createLabelText(Composite parent, String label, int noCharacters) {

		Composite c = new Composite(parent, SWT.NONE);
		GridLayout gridLayout = new GridLayout();
		gridLayout.marginHeight = 0;
		gridLayout.numColumns = 2;
		//	c.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL));
		c.setLayoutData(new GridData());
		c.setLayout(gridLayout);

		Label l = createLabel(c, label, null);
		l.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_CENTER));
		Text text = new Text(c, SWT.SINGLE | SWT.BORDER);
		GridData data = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
		data.widthHint = convertWidthInCharsToPixels(noCharacters);
		text.setLayoutData(data);
		return text;
	}

	protected Control createContents(Composite parent) {

		// The contents area is divided into notebooks to make best use of real estate
		// Current tabs are for appearance and for code generation
		tabFolder = new TabFolder(parent, SWT.NONE);
//		tabFolder.setLayout(new TabFolderLayout());

		createAppearanceTab();
		createCodeGenerationTab();
		createStylesTab();
		initializeGUIControlsFromStore();

		return tabFolder;
	}
	protected void createAppearanceTab() {

		appearanceTab = new TabItem(tabFolder, SWT.NONE);
		appearanceTab.setText(VCEMessages.getString("VCEPreferencePage.Tab.Appearance.Text")); //$NON-NLS-1$
		Composite appearanceComposite = new Composite(tabFolder, SWT.NONE);
		appearanceComposite.setLayout(new GridLayout());
		appearanceComposite.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_FILL | GridData.HORIZONTAL_ALIGN_FILL));

		appearanceTab.setControl(appearanceComposite);

		// Appearance controls
		Group group = createGroup(appearanceComposite, VCEMessages.getString("PreferencePage.EditorGroup.Title"), 2); //$NON-NLS-1$
		splitRadioButton = createRadioButton(group, VCEMessages.getString("PreferencePage.EditorGroup.SplitPaneOrientation")); //$NON-NLS-1$
		showSplitPaneGEFPaletteButton = createCheckBox(group, VCEMessages.getString("PreferencePage.EditorGroup.PaletteInEditor.InSplitPane"), 0); //$NON-NLS-1$
		notebookRadioButton = createRadioButton(group, VCEMessages.getString("PreferencePage.EditorGroup.UseNoteBook")); //$NON-NLS-1$
		showNotebookGEFPaletteButton = createCheckBox(group, VCEMessages.getString("PreferencePage.EditorGroup.PaletteInEditor.InNotebook"), 0); //$NON-NLS-1$

		// Only enable showing the GEF palette in notebook mode
		SelectionListener listener = new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent event) {
				showNotebookGEFPaletteButton.setEnabled(notebookRadioButton.getSelection());
				showSplitPaneGEFPaletteButton.setEnabled(splitRadioButton.getSelection());
			}
			public void widgetSelected(SelectionEvent event) {
				showNotebookGEFPaletteButton.setEnabled(notebookRadioButton.getSelection());
				showSplitPaneGEFPaletteButton.setEnabled(splitRadioButton.getSelection());
			}
		};

		notebookRadioButton.addSelectionListener(listener);
		splitRadioButton.addSelectionListener(listener);

		Composite openComposite = new Composite(appearanceComposite, SWT.NONE);
		openComposite.setLayout(new GridLayout(2, false));

		openPropertiesViewIfRequired = createCheckBox(openComposite, VCEMessages.getString("PreferencePage.OpenView.Properties"), 0); //$NON-NLS-1$
		try {
			fPropertiesViewImage = CDEPlugin.getImageFromPlugin(Platform.getPluginRegistry().getPluginDescriptor("org.eclipse.ui").getPlugin(), "icons/full/eview16/prop_ps.gif");	//$NON-NLS-1$
		} catch (CoreException e1) {
		} 
		createLabel(openComposite, null, fPropertiesViewImage);

		openJavaBeansViewIfRequired = createCheckBox(openComposite, VCEMessages.getString("PreferencePage.OpenView.JavaBeans"), 0); //$NON-NLS-1$
		fJavaBeansViewImage = CDEPlugin.getImageFromPlugin(JavaVEPlugin.getPlugin(), "icons/full/ctool16/javavisualeditor_co.gif"); //$NON-NLS-1$	
		createLabel(openComposite, null, fJavaBeansViewImage);

		lookAndFeelLabel = createLabel(appearanceComposite, VCEMessages.getString("PreferencePage.LookAndFeel.Title"), null); //$NON-NLS-1$

		// Look and feel is a list of known ones plus a button to let users define new look and feel classes
		Composite lookAndFeelComposite = new Composite(appearanceComposite, SWT.NONE);
		lookAndFeelComposite.setLayout(new GridLayout(2, false));
		GridData lfData = new GridData(GridData.FILL_HORIZONTAL);
		lookAndFeelComposite.setLayoutData(lfData);

		lookAndFeelTable = createTable(lookAndFeelComposite, SWT.CHECK | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL, new String[] { VCEMessages.getString("VCEPreferencePage.Tab.Appearance.Table.LookAndFeel.Name"), VCEMessages.getString("VCEPreferencePage.Tab.Appearance.Table.LookAndFeel.Class")}); //$NON-NLS-1$ //$NON-NLS-2$

		GridData listData = new GridData(GridData.FILL_HORIZONTAL);
		lookAndFeelTable.setLayoutData(listData);
		listData.heightHint = 100;
		listData.widthHint = 200;

		// The buttons are themselves on their own row composite
		Composite lookAndFeelButtons = new Composite(lookAndFeelComposite, SWT.NONE);
		lookAndFeelButtons.setLayout(new GridLayout(1, true));
		GridData lfButtonData = new GridData();
		lfButtonData.horizontalAlignment = GridData.END;
		lookAndFeelButtons.setLayoutData(lfButtonData);
		// Create new, edit and remove buttons
		Button bAdd = new Button(lookAndFeelButtons, SWT.PUSH);
		bAdd.setText(VCEMessages.getString("VCEPreferencePage.Tab.Appearance.Button.New")); //$NON-NLS-1$
		GridData addData = new GridData();
		addData.horizontalAlignment = GridData.FILL;
		addData.widthHint = getButtonWidthHint(bAdd);
		bAdd.setLayoutData(addData);
		bAdd.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				addLookAndFeel();
			}
		});

		final Button bEdit = new Button(lookAndFeelButtons, SWT.PUSH);
		bEdit.setText(VCEMessages.getString("VCEPreferencePage.Tab.Appearance.Button.Edit")); //$NON-NLS-1$
		GridData editData = new GridData();
		editData.horizontalAlignment = GridData.FILL;
		editData.widthHint = getButtonWidthHint(bEdit);
		bEdit.setLayoutData(editData);
		bEdit.setEnabled(false);
		bEdit.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				// Only a user look and feel can be edited
				if (lookAndFeelTable.getSelectionCount() == 1 && USER_ITEM.equals(lookAndFeelTable.getSelection()[0].getData())) {
					editLookAndFeel();
				}
			}
		});

		final Button bRemove = new Button(lookAndFeelButtons, SWT.PUSH);
		bRemove.setText(VCEMessages.getString("VCEPreferencePage.Tab.Appearance.Button.Remove")); //$NON-NLS-1$
		GridData removeData = new GridData();
		removeData.horizontalAlignment = GridData.FILL;
		removeData.widthHint = getButtonWidthHint(bRemove);
		bRemove.setLayoutData(removeData);
		bRemove.setEnabled(false);
		bRemove.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				// Only a user look and feel can be removed
				if (lookAndFeelTable.getSelectionCount() == 1 && USER_ITEM.equals(lookAndFeelTable.getSelection()[0].getData())) {
					removeLookAndFeel();
				}
			}
		});

		// Set the Edit and Remove buttons states based on the table selection
		// and also handle when the table items are checked on and off
		Listener tableListener = new Listener() {
			public void handleEvent(Event event) {
				switch (event.detail) {
					// See whether the event was raised based on an item being checked or unchecked
					case SWT.CHECK :
						// If an item was selected then make this the new default
						TableItem item = (TableItem) event.item;
						if (item.getChecked()) {
							// uncheck the old item
							if (currentLookAndFeelItem != null) {
								currentLookAndFeelItem.setChecked(false);
							}
							currentLookAndFeelItem = item;
						} else {
							// Re-check the item and you can't uncheck an item ( as this would make there be no look and feel )
							item.setChecked(true);
						}
						break;
					default :
						// If something is selected then enable edit and remove if it is a user added look and feel
						if (lookAndFeelTable.getSelectionCount() != -1 && USER_ITEM.equals(lookAndFeelTable.getSelection()[0].getData())) {
							bRemove.setEnabled(true);
							bEdit.setEnabled(true);
						} else {
							bRemove.setEnabled(false);
							bEdit.setEnabled(false);
						}
				}
			}
		};

		lookAndFeelTable.addListener(SWT.Selection, tableListener);

		// These are only shown if a special debug mode is set up
		// with a .options whereby the live AWT/Swing windows can be seen and also
		// the XML text for the JVE object model can be seen, both of which help debugging

		showWindowCheckBox = createCheckBox(appearanceComposite, VCEMessages.getString("PreferencePage.ShowLiveWindow"), 15); //$NON-NLS-1$
		showXMLTextCheckBox = createCheckBox(appearanceComposite, VCEMessages.getString("PreferencePage.ShowXMLText"), 15); //$NON-NLS-1$
		if (!VCEPreferences.isLiveWindow())
			showWindowCheckBox.setVisible(false);
		if (!VCEPreferences.isXMLText())
			showXMLTextCheckBox.setVisible(false);

	}
	protected int getButtonWidthHint(Button aButton) {
		GC fGC = new GC(aButton);
		fGC.setFont(aButton.getFont());
		int widthHint = Dialog.convertHorizontalDLUsToPixels(fGC.getFontMetrics(), aButton.getText().length());
		fGC.dispose();
		return Math.max(widthHint, aButton.computeSize(SWT.DEFAULT, SWT.DEFAULT, true).x);
	}
	protected void addLookAndFeel() {
		// Open a dialog showing all available look and feel classes
		Shell parent = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		LookAndFeelDialog dialog = new LookAndFeelDialog(parent);
		dialog.open();
		// If the dialog returned OK then add the look and feel to the tree
		if (dialog.getReturnCode() == Window.OK) {
			TableItem item = new TableItem(lookAndFeelTable, SWT.NONE);
			item.setText(0, dialog.fName);
			item.setText(1, dialog.fClass);
			item.setData(USER_ITEM);
		}
	}
	protected void editLookAndFeel() {
		// Open a dialog showing all available look and feel classes
		Shell parent = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		LookAndFeelDialog dialog = new LookAndFeelDialog(parent);
		TableItem selectedItem = lookAndFeelTable.getSelection()[0];
		String oldName = selectedItem.getText(0);
		String oldClass = selectedItem.getText(1);
		dialog.fName = oldName;
		dialog.fClass = oldClass;
		dialog.open();
		// If the dialog returned OK then add the look and feel to the tree
		if (dialog.getReturnCode() == Window.OK) {
			TableItem item = lookAndFeelTable.getSelection()[0];
			item.setText(0, dialog.fName);
			item.setText(1, dialog.fClass);
		}
	}
	protected void removeLookAndFeel() {

		TableItem selectedItem = lookAndFeelTable.getSelection()[0];
		selectedItem.dispose();
		// If the current look and feel was removed make the <default> current
		if (currentLookAndFeelItem == selectedItem) {
			currentLookAndFeelItem = lookAndFeelTable.getItem(0);
			currentLookAndFeelItem.setChecked(true);
		}

	}
	protected void createCodeGenerationTab() {

		codeGenTab = new TabItem(tabFolder, SWT.NONE);
		codeGenTab.setText(VCEMessages.getString("VCEPreferencePage.Tab.CodeGeneration.Text")); //$NON-NLS-1$
		Composite codeGenComposite = new Composite(tabFolder, SWT.NONE);
		codeGenComposite.setLayout(new GridLayout());
		codeGenComposite.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_FILL | GridData.HORIZONTAL_ALIGN_FILL));
		codeGenTab.setControl(codeGenComposite);

		Group codeGenGroup_Gen = createGroup(codeGenComposite, VCEMessages.getString("PreferencePage.CodeGen.ParsingGeneration_Style_1"), 1); //$NON-NLS-1$

		generateExpressionComment = createCheckBox(codeGenGroup_Gen, VCEMessages.getString("PreferencePage.NewExpressionCommentPrompt"), 0); //$NON-NLS-1$
		generateTryCatchBlock = createCheckBox(codeGenGroup_Gen, VCEMessages.getString("PreferencePage.GenerateTryCatchBlock"), 0); //$NON-NLS-1$

		Group codeGenGroup_Parsing = createGroup(codeGenComposite, VCEMessages.getString("PreferencePage.CodeGen.ParsingParsing_Filters_2"), 1); //$NON-NLS-1$

		//    requireIVJforComponents = createCheckBox(codeGenGroup_Parsing,VCEMessages.getString("PreferencePage.CodeGen.ParsingRequire_prefix_of___ivj___for_AWT/Swing_component_field_names_3"),0);   //$NON-NLS-1$
		requireIVJforNonComponents = createCheckBox(codeGenGroup_Parsing, VCEMessages.getString("PreferencePage.CodeGen.ParsingRequire_prefix_of___ivj___for_objects_other_than_AWT/Swing_component_field_names_4"), 0); //$NON-NLS-1$

		// Create a listener to see when the values in text boxes changes so we can trigger revalidation
		ModifyListener modifyListener = new ModifyListener() {
			public void modifyText(ModifyEvent e) {
					// Validate that the value in the sourceToVisual is an integer between 1 and 9;
	validateFields();
			}
		};

		sourceSyncGrp = createGroup(codeGenComposite, VCEMessages.getString("PreferencePage.CodeGen.Source_Synchronization_Delay"), 5); //$NON-NLS-1$
		// The group is 5 wide.

		// For JavaBeansToSource show the label, text and spacers
		createLabel(sourceSyncGrp, VCEMessages.getString("PreferencePage.CodeGen.JavaBeansToSource"), null); //$NON-NLS-1$
		sourceSyncDelayText = createText(sourceSyncGrp, 11, 1); //$NON-NLS-1$
		sourceSyncDelayText.setTextLimit(9);
		createLabel(sourceSyncGrp, "", null); //$NON-NLS-1$
		createLabel(sourceSyncGrp, "", null); //$NON-NLS-1$
		createLabel(sourceSyncGrp, "", null); //$NON-NLS-1$	

		// The JavaBeansToSource are 5 wide in the group
		// First the label, then a * and the text box
		createLabel(sourceSyncGrp, VCEMessages.getString("PreferencePage.CodeGen.SourceToJavaBeans"), null); //$NON-NLS-1$
		fSourceToJavaBeansTimeLabel = createText(sourceSyncGrp, 11, 1); //$NON-NLS-1$
		fSourceToJavaBeansTimeLabel.setEditable(false);
		createLabel(sourceSyncGrp, "*", null); //$NON-NLS-1$
		sourceToVisual = createText(sourceSyncGrp, 2, 1); //$NON-NLS-1$
		sourceToVisual.addModifyListener(modifyListener);
		sourceToVisual.setTextLimit(1);
		fSourceToJavaBeansTotalTimeLabel = createText(sourceSyncGrp, 14, 1); //$NON-NLS-1$
		fSourceToJavaBeansTotalTimeLabel.setEditable(false);

		sourceSyncDelayText.addModifyListener(modifyListener);

	}

	protected Table createTable(Composite parent, int Style, String[] headers) {
		final Table result = new Table(parent, Style);
		TableLayout tl = new TableLayout();
		result.setLayout(tl);
		result.setHeaderVisible(true);
		result.setLinesVisible(true);
		for (int i = 0; i < headers.length; i++) {
			TableColumn nameColumn = new TableColumn(result, SWT.NONE);
			nameColumn.setText(headers[i]);
		}
		if (result.getColumnCount() == 1)
			tl.addColumnData(new ColumnWeightData(100));
		return result;
	}

	protected void addStylesTableItems(Table t, String columns[]) {
		for (int i = 0; i < columns.length; i++) {
			TableItem item = new TableItem(t, SWT.NONE);			
			item.setText(0, columns[i]);
			item.setData(new StylesTableItem(columns[i]));
		}
	}

	protected IStyleRegistry getRulesRegistry() {
		return org.eclipse.ve.internal.java.vce.rules.JVEStyleRegistry.getJVEStyleRegistry();
	}

	protected void createContributorUI(StylesTableItem itemData, Composite parent) {
		if (itemData.styleUI != null) {
			itemData.styleContributor = itemData.styleUI.createUI(parent);
		}
	}

	private static class StylesTableItem {
		public boolean activated;
		public String styleID;
		public IEditorStylePrefUI styleUI;
		public Control styleContributor;

		public StylesTableItem(String styleID) {
			this.styleID = styleID;
		}
	}

	protected void populateStylesTable(final Table sTable, final Label info, final String currentStyle) {

		IStyleRegistry reg = getRulesRegistry();
		String styles[] = reg.getStyleIDs();
		int cIndex = 0;
		addStylesTableItems(sTable, styles);
		for (int i = 0; i < styles.length; i++) {
			if (styles[i].equals(currentStyle)) {
				cIndex = i;
				break;
			}
		}
		final int defaultIndex = cIndex;
		sTable.addSelectionListener(new SelectionListener() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				TableItem item = (TableItem) e.item;
				TableItem[] items = sTable.getItems();
				boolean checked = e.detail == SWT.CHECK; 	// Checked event
				if (checked) {
					for (int i = 0; i < items.length; i++) {
						if (items[i] != item && items[i].getChecked()) {
							items[i].setChecked(false);
						}
					}
				}
				StylesTableItem itemData = (StylesTableItem) item.getData();				
				if (checked) {
					if (item.getChecked()) {
						fStyleID = itemData.styleID;
					} else {
						// Unchecked but none currently checked, so go back to default, but we don't switch to that page.
						items[defaultIndex].setChecked(true);
						fStyleID = currentStyle;
					}
				} else
					styleSelected(itemData, info);	// Checked event doesn't actually change selection, so on checked don't select style.				
			}
			public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
			}
		});

		sTable.select(cIndex);
		TableItem currentItem = sTable.getItem(cIndex);
		currentItem.setChecked(true);
		styleSelected((StylesTableItem) currentItem.getData(), info);
	}
	
	/*
	 * Restore to the default style.
	 */
	private void restoreDefaultStyle() {
		fStyleID = getStore().getDefaultString(VCEPreferences.JVE_PATTERN_STYLE_ID);
		TableItem[] items = stylesTable.getItems();
		TableItem defaultItem = items[0];
		for (int i = 0; i < items.length; i++) {
			TableItem item = items[i];
			StylesTableItem itemData = (StylesTableItem) item.getData();
			if (fStyleID.equals(itemData.styleID)) {
				defaultItem = item;
			} else if (item.getChecked())
				item.setChecked(false);
			if (itemData.styleUI != null)
				itemData.styleUI.restoreDefaults();
		}
		
		defaultItem.setChecked(true);
	}

	private void styleSelected(StylesTableItem itemData, Label info) {
		IStyleRegistry reg = getRulesRegistry();
		IEditorStyle style = reg.getStyle(itemData.styleID);
		if (!itemData.activated) {
			itemData.activated = true;
			itemData.styleUI = style.getPrefUI(); // Now have a ui.
			createContributorUI(itemData, styleContributorArea);
		}
		info.setText(style.getDescription());
		stylesContributorAreaLayout.topControl = itemData.styleContributor;
		styleContributorArea.layout();
	}

	protected void createStylesTab() {

		stylesTab = new TabItem(tabFolder, SWT.NONE);
		stylesTab.setText(VCEMessages.getString("VCEPreferencePage.Tab.Styles.Text")); //$NON-NLS-1$

		Composite stylesComposite = new Composite(tabFolder, SWT.NONE);
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		gridLayout.verticalSpacing = 1;
		gridLayout.horizontalSpacing = 1;
		gridLayout.marginHeight = 1;
		stylesComposite.setLayout(gridLayout);
		stylesComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
		stylesTab.setControl(stylesComposite);

		// Create the styles table
		stylesTable = createTable(stylesComposite, SWT.SINGLE | SWT.CHECK | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL, new String[] { VCEMessages.getString("VCEPreferencePage.Tab.Styles.Table.Styles")}); //$NON-NLS-1$
		Label filler = createLabel(stylesComposite, "", null); //$NON-NLS-1$
		GridData lData = new GridData();
		lData.heightHint = 55;
		lData.widthHint = 200;
		stylesTable.setLayoutData(lData);

		GridData rData = new GridData(GridData.FILL_HORIZONTAL);
		filler.setLayoutData(rData);
		// Styles Info 
		Label styleInfo = createLabel(stylesComposite, "", null); //$NON-NLS-1$
		GridData styleInfoData = (GridData) styleInfo.getLayoutData();
		styleInfoData.heightHint = convertHeightInCharsToPixels(1);
		styleInfoData.widthHint = convertWidthInCharsToPixels(40);
		styleInfoData.grabExcessVerticalSpace = false;
		styleInfoData.horizontalAlignment = GridData.FILL;
		styleInfoData.horizontalSpan = 2;
		// Style Contributor area
		styleContributorArea = new Composite(stylesComposite, SWT.NONE);
		stylesContributorAreaLayout = new StackLayout();
		stylesContributorAreaLayout.marginHeight = 1;
		styleContributorArea.setLayout(stylesContributorAreaLayout);
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.horizontalSpan = 2;
		styleContributorArea.setLayoutData(gd);

		fStyleID = VCEPreferences.getStyleID();

		populateStylesTable(stylesTable, styleInfo, fStyleID);

	}

	public static void initializeBasicLookAndFeelItems(Table table) {

		// Add the Default item
		TableItem tableItem = new TableItem(table, SWT.NONE);
		tableItem.setText(0, LnF_DEFAULT); //$NON-NLS-1$
		tableItem.setData(DEFAULT_ITEM);

		// Dynamically query Swing for the available L&Fs
		// This is better than the static list used before, but has the problem that it
		// reflects the L&Fs available in the IDE's JRE, not the remote vm.
		UIManager.LookAndFeelInfo[] lnfs = UIManager.getInstalledLookAndFeels();
		for (int i = 0; i < lnfs.length; i++) {
			// TODO: Hack to remove the Windows L&F from Linux (Sun bug 4843282)
			if (lnfs[i].getClassName().equals("com.sun.java.swing.plaf.windows.WindowsLookAndFeel") && //$NON-NLS-1$
			System.getProperty("os.name").equals("Linux"))  //$NON-NLS-1$ //$NON-NLS-2$
				continue; 

			tableItem = new TableItem(table, SWT.NONE);
			tableItem.setText(0, lnfs[i].getName());
			tableItem.setText(1, lnfs[i].getClassName());
			tableItem.setData(PROGRAM_ITEM);
		}
	}
	public static void addLookAndFeelClassesToTable(Table aTable, String[][] lookAndFeelClasses, String itemType) {

		for (int i = 0; i < lookAndFeelClasses.length; i++) {
			String[] lfNameClass = lookAndFeelClasses[i];
			// Create a table item for the user defined look and feel
			TableItem item = new TableItem(aTable, SWT.NONE);
			item.setText(0, lfNameClass[0]);
			item.setText(1, lfNameClass[1]);
			item.setData(itemType);
		};

	}

	protected Preferences getStore() {
		if (fStore != null)
			return fStore;
		fStore = VCEPreferences.getPlugin().getPluginPreferences();
		return fStore;
	}

	protected void initializeGUIControlsFromStore() {

		getStore();

		initializeBasicLookAndFeelItems(lookAndFeelTable);
		// Get the current look and feel class name
		String lookAndFeelClass = fStore.getString(VCEPreferences.SWING_LOOKANDFEEL);

		// Get any look and feel classes that are registered in plugin extension points
		addLookAndFeelClassesToTable(lookAndFeelTable, VCEPreferences.getPluginLookAndFeelClasses(), PLUGIN_ITEM);

		// Get user defined look and feel classes defined locally in the workbench by the user
		addLookAndFeelClassesToTable(lookAndFeelTable, VCEPreferences.getUserLookAndFeelClasses(), USER_ITEM);

		// Pack the two table columns
		TableColumn[] columns = lookAndFeelTable.getColumns();
		for (int i = 0; i < columns.length; i++) {
			columns[i].pack();
		}

		// Iterate over the look and feel items and see which one is the current look and feel
		// so we can check it for the user
		if (lookAndFeelClass == null | lookAndFeelClass.length() == 0) {
			// If there is no look and feel then the default one is the checked one
			currentLookAndFeelItem = lookAndFeelTable.getItem(0);
			lookAndFeelTable.getItem(0).setChecked(true);
		} else {
			for (int i = 1; i < lookAndFeelTable.getItemCount(); i++) {
				TableItem item = lookAndFeelTable.getItem(i);
				if (lookAndFeelClass.equals(item.getText(1))) {
					currentLookAndFeelItem = item;
					currentLookAndFeelItem.setChecked(true);
					break;
				}
			}
		}

		openPropertiesViewIfRequired.setSelection(fStore.getBoolean(VCEPreferences.OPEN_PROPERTIES_VIEW));
		openJavaBeansViewIfRequired.setSelection(fStore.getBoolean(VCEPreferences.OPEN_JAVABEANS_VIEW));
		showWindowCheckBox.setSelection(fStore.getBoolean(VCEPreferences.SHOW_LIVE_WINDOW));
		showXMLTextCheckBox.setSelection(CDEPlugin.getPlugin().getPluginPreferences().getBoolean(CDEPlugin.SHOW_XML));

		boolean showAsNotebook = fStore.getBoolean(VCEPreferences.NOTEBOOK_PAGE);
		splitRadioButton.setSelection(!showAsNotebook);
		notebookRadioButton.setSelection(showAsNotebook);
		showNotebookGEFPaletteButton.setEnabled(notebookRadioButton.getSelection());
		showSplitPaneGEFPaletteButton.setEnabled(splitRadioButton.getSelection());

		// Code Generation
		//	requireIVJforComponents.setSelection(fStore.getString(VCEPreferences.REQUIRE_IVJ_COMPONENTS).length()==0 ?
		//	                                     false : fStore.getBoolean(VCEPreferences.REQUIRE_IVJ_COMPONENTS)) ;		

		generateExpressionComment.setSelection(fStore.getBoolean(VCEPreferences.GENERATE_COMMENT));
		generateTryCatchBlock.setSelection(fStore.getBoolean(VCEPreferences.GENERATE_TRY_CATCH_BLOCK));
		showNotebookGEFPaletteButton.setSelection(fStore.getBoolean(VCEPreferences.NOTEBOOK_SHOW_GEF_PALETTE));
		showSplitPaneGEFPaletteButton.setSelection(fStore.getBoolean(VCEPreferences.SPLITPANE_SHOW_GEF_PALETTE));
		sourceSyncDelayText.setText(fStore.getString(VCEPreferences.SOURCE_SYNC_DELAY));
		sourceToVisual.setText(fStore.getString(VCEPreferences.SOURCE_DELAY_FACTOR));
		calculateTotalSourceToVisualTime();
		requireIVJforNonComponents.setSelection(true);
		requireIVJforNonComponents.setEnabled(false);

	}
	protected void validateFields() {
		// Validate that the sourceSyncDelayText is an integer greater than the default of 500ms
		boolean isValid = true;
		try {
			int val = Integer.parseInt(sourceSyncDelayText.getText());
			if (val < VCEPreferences.DEFAULT_SYNC_DELAY) {
				isValid = false;
				setErrorMessage(VCEMessages.getString("PreferencePage.CodeGen.Error.DelayTimeMinimum")); //$NON-NLS-1$
				fSourceToJavaBeansTotalTimeLabel.setText("="); //$NON-NLS-1$
				setValid(false);
			}
		} catch (NumberFormatException ex) {
			isValid = false;
			setErrorMessage(VCEMessages.getString("PreferencePage.CodeGen.Error.DelayTimeMustBeInteger")); //$NON-NLS-1$
			setValid(false);
			fSourceToJavaBeansTotalTimeLabel.setText("="); //$NON-NLS-1$
		}
		// If we are valid check that the sourceToVisual delay factor is an integer between 1 and 9
		if (isValid) {
			try {
				int val = Integer.parseInt(sourceToVisual.getText());
				if (val < 1 || val > 9) {
					isValid = false;
					setErrorMessage(VCEMessages.getString("PreferencePage.CodeGen.Error.DelayTimeValidRange")); //$NON-NLS-1$
					fSourceToJavaBeansTotalTimeLabel.setText("="); //$NON-NLS-1$
					setValid(false);
				}
			} catch (NumberFormatException ex) {
				isValid = false;
				setErrorMessage(VCEMessages.getString("PreferencePage.CodeGen.Error.DelayTimeValidRange")); //$NON-NLS-1$
				fSourceToJavaBeansTotalTimeLabel.setText("="); //$NON-NLS-1$
				setValid(false);
			}
		}
		// If we got no errors then clear the error message and refresh the total source to visual time
		if (isValid) {
			// The next step is to calculate the total time which is the delay time * the factor
			// it is possible that is an integer that is too large.
			boolean isValidTotalTime = calculateTotalSourceToVisualTime();
			if (isValidTotalTime) {
				setValid(true);
				setErrorMessage(null);
			} else {
				setValid(false);
			}
		}
	}
	protected boolean calculateTotalSourceToVisualTime() {
		try {
			int javaBeansToSourceDelayTime = Integer.parseInt(sourceSyncDelayText.getText());
			int increaseFactorForSourceToJavaBeans = Integer.parseInt(sourceToVisual.getText());
			Integer total = new Integer(javaBeansToSourceDelayTime * increaseFactorForSourceToJavaBeans); //$NON-NLS-1$
			// I can find no way of seeing whether the result has overflowed or not.  Therefore do a division to see whether the
			// answer is correct
			if (total.intValue() / increaseFactorForSourceToJavaBeans == javaBeansToSourceDelayTime) {
				fSourceToJavaBeansTotalTimeLabel.setText("= " + total); //$NON-NLS-1$
				fSourceToJavaBeansTimeLabel.setText(Integer.toString(javaBeansToSourceDelayTime));
				// Because we have change the contents of the label its size needs updating as its preferred size has changed
				// A layout on its container will force the layout to occur
				sourceSyncGrp.layout();
				return true;
			} else {
				// For the error to show grab the one used by the property sheet to show an invalid integer
				setErrorMessage(PropertysheetMessages.getString(PropertysheetMessages.NOT_INTEGER));
				fSourceToJavaBeansTotalTimeLabel.setText("= " + total); //$NON-NLS-1$
				sourceSyncGrp.layout();
				return false;
			}
		} catch (NumberFormatException exc) {
			// This should not occur because the number have already been validated
			return false;
		}
	}
	public boolean performOk() {

		// Save the Swing look and feel
		String lookAndFeelClass = ""; //$NON-NLS-1$
		if (currentLookAndFeelItem != null) {
			lookAndFeelClass = currentLookAndFeelItem.getText(1);
		}
		if (lookAndFeelClass == null) {
			lookAndFeelClass = ""; //$NON-NLS-1$
		}
		String existingLookAndFeelClass = fStore.getString(VCEPreferences.SWING_LOOKANDFEEL);
		if (!lookAndFeelClass.equals(existingLookAndFeelClass)) {
			fStore.setValue(VCEPreferences.SWING_LOOKANDFEEL, lookAndFeelClass);
		}

		// The user could have added, edited or removed custom look and feel classes
		// These are in the table with a data field of "USER" and will be after the default ones
		// Note that there may be other table entries that come from plugins that we don't save in the preference store
		ArrayList userClasses = new ArrayList(1);
		for (int i = 0; i < lookAndFeelTable.getItemCount(); i++) {
			TableItem item = lookAndFeelTable.getItem(i);
			if (USER_ITEM.equals(item.getData())) {
				userClasses.add(new String[] { item.getText(0), item.getText(1)});
			}
		}
		// Now convert these to an array of String[] and set them into the preference store
		if (userClasses.size() > 0) {
			VCEPreferences.setUserLookAndFeelClasses((String[][]) userClasses.toArray(new String[userClasses.size()][]));
		} else {
			// If there are no user look and feel classes make sure none are stored
			VCEPreferences.setUserLookAndFeelClasses(null);
		}
		// Save whether or not to show the live window
		fStore.setValue(VCEPreferences.SHOW_LIVE_WINDOW, showWindowCheckBox.getSelection());
		// Save whether to show the VCE and Source as a split pane or notebook
		fStore.setValue(VCEPreferences.NOTEBOOK_PAGE, notebookRadioButton.getSelection());
		fStore.setValue(VCEPreferences.NOTEBOOK_SHOW_GEF_PALETTE, showNotebookGEFPaletteButton.getSelection());
		fStore.setValue(VCEPreferences.SPLITPANE_SHOW_GEF_PALETTE, showSplitPaneGEFPaletteButton.getSelection());

		fStore.setValue(VCEPreferences.OPEN_PROPERTIES_VIEW, openPropertiesViewIfRequired.getSelection());
		fStore.setValue(VCEPreferences.OPEN_JAVABEANS_VIEW, openJavaBeansViewIfRequired.getSelection());

		fStore.setValue(VCEPreferences.GENERATE_COMMENT, generateExpressionComment.getSelection());
		fStore.setValue(VCEPreferences.GENERATE_TRY_CATCH_BLOCK, generateTryCatchBlock.getSelection());
		fStore.setValue(VCEPreferences.SOURCE_SYNC_DELAY, Integer.parseInt(sourceSyncDelayText.getText()));
		fStore.setValue(VCEPreferences.SOURCE_DELAY_FACTOR, Integer.parseInt(sourceToVisual.getText()));
		//	fStore.setValue(VCEPreferences.REQUIRE_IVJ_COMPONENTS,requireIVJforComponents.getSelection()) ;

		fStore.setValue(VCEPreferences.JVE_PATTERN_STYLE_ID, fStyleID);

		saveContributorSettings();
		
		VCEPreferences.getPlugin().savePluginPreferences();

		// Do the CDE one now
		Preferences cdePreferenceStore = CDEPlugin.getPlugin().getPluginPreferences();
		boolean existingShowXMLText = cdePreferenceStore.getBoolean(CDEPlugin.SHOW_XML);
		if (existingShowXMLText != showXMLTextCheckBox.getSelection()) {
			cdePreferenceStore.setValue(CDEPlugin.SHOW_XML, showXMLTextCheckBox.getSelection());
		}

		CDEPlugin.getPlugin().savePluginPreferences();

		return true;
	}
	
	protected void saveContributorSettings() {
		TableItem[] items = stylesTable.getItems();
		for (int i = 0; i < items.length; i++) {
			StylesTableItem data = (StylesTableItem) items[i].getData();
			if (data.styleContributor != null) {
				data.styleUI.storeUI();
			}
		}
	}
	
	protected void performDefaults() {

		Preferences store = VCEPreferences.getPlugin().getPluginPreferences();
		showXMLTextCheckBox.setSelection(store.getDefaultBoolean(VCEPreferences.SELECT_SOURCE));
		showWindowCheckBox.setSelection(store.getDefaultBoolean(VCEPreferences.SHOW_LIVE_WINDOW));
		// Initialize the tree to just show the DEFAULT and the plugin defined look and feel classes
		TableItem[] items = lookAndFeelTable.getItems();
		for (int i = 0; i < items.length; i++) {
			items[i].setChecked(false);
			if (USER_ITEM.equals(items[i].getData())) {
				items[i].dispose();
			}
		}
		// Show that the default L&F has been selected
		currentLookAndFeelItem = lookAndFeelTable.getItem(0);
		currentLookAndFeelItem.setChecked(true);

		notebookRadioButton.setSelection(store.getDefaultBoolean(VCEPreferences.NOTEBOOK_PAGE));
		showNotebookGEFPaletteButton.setSelection(store.getDefaultBoolean(VCEPreferences.NOTEBOOK_SHOW_GEF_PALETTE));
		generateExpressionComment.setSelection(store.getDefaultBoolean(VCEPreferences.GENERATE_COMMENT));
		generateTryCatchBlock.setSelection(store.getDefaultBoolean(VCEPreferences.GENERATE_TRY_CATCH_BLOCK));
		splitRadioButton.setSelection(!store.getDefaultBoolean(VCEPreferences.NOTEBOOK_PAGE));
		showSplitPaneGEFPaletteButton.setSelection(store.getDefaultBoolean(VCEPreferences.SPLITPANE_SHOW_GEF_PALETTE));
		sourceSyncDelayText.setText(Integer.toString(VCEPreferences.DEFAULT_SYNC_DELAY));
		sourceToVisual.setText(Integer.toString(VCEPreferences.DEFAULT_L2R_FACTOR));

		// Having set the radio button for the split pane versus notebook to the default
		// We need to make sure that the check boxes for whether the palette should be shown or not
		// is enabled accordingly
		showSplitPaneGEFPaletteButton.setEnabled(splitRadioButton.getSelection());
		showNotebookGEFPaletteButton.setEnabled(notebookRadioButton.getSelection());
		openPropertiesViewIfRequired.setSelection(true);
		openJavaBeansViewIfRequired.setSelection(true);
		//	requireIVJforComponents.setSelection(false) ;
		
		restoreDefaultStyle();

	}

	public void init(IWorkbench aWorkbench) {
	}

	public void dispose() {
		super.dispose();
		if (fJavaBeansViewImage != null)
			fJavaBeansViewImage.dispose();
		if (fPropertiesViewImage != null)
			fPropertiesViewImage.dispose();

	}

}
