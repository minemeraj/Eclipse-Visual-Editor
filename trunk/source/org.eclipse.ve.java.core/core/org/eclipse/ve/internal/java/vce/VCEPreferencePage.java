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
package org.eclipse.ve.internal.java.vce;
/*
 *  $RCSfile: VCEPreferencePage.java,v $
 *  $Revision: 1.34 $  $Date: 2005-08-24 23:30:49 $ 
 */

import java.net.URL;
import java.util.ArrayList;

import javax.swing.UIManager;

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
import org.eclipse.ui.browser.IWebBrowser;
import org.eclipse.ui.browser.IWorkbenchBrowserSupport;

import org.eclipse.jem.internal.proxy.core.ProxyPlugin;

import org.eclipse.ve.internal.cde.core.CDEPlugin;

import org.eclipse.ve.internal.java.core.JavaVEPlugin;
import org.eclipse.ve.internal.java.vce.rules.*;

import org.eclipse.ve.internal.propertysheet.PropertysheetMessages;

public class VCEPreferencePage extends PreferencePage implements IWorkbenchPreferencePage {

	public static final String USER_ITEM = "USER_ITEM"; //$NON-NLS-1$
	public static final String DEFAULT_ITEM = "DEFAULT_ITEM"; //$NON-NLS-1$
	public static final String PROGRAM_ITEM = "PROGRAM_ITEM"; //$NON-NLS-1$
	public static final String PLUGIN_ITEM = "PLUGIN_ITEM"; //$NON-NLS-1$
	public static final String LnF_DEFAULT = VCEMessages.PreferencePage_LookAndFeel_Default; 
	protected Preferences fStore;
	protected Preferences cdeStore;	
	protected TabFolder tabFolder;
	protected TabItem appearanceTab;
	protected TabItem codeGenTab;
	protected TabItem stylesTab;
	protected SourceViewer fSourceViewer;
	protected Label lookAndFeelLabel;
	protected Table lookAndFeelTable;
	protected ArrayList fLookAndFeelClasses = new ArrayList(4);
	protected Button showGridCheckBox;
	protected Button renameAskCheckbox;
	protected Text xyGridSpacingText;
	
	protected Button showWindowCheckBox;
	protected Button showXMLTextCheckBox;
	protected Button showClipboardCheckBox;
		
	protected Button splitRadioButton;
	protected Button notebookRadioButton;
	protected Button generateExpressionComment;
	protected Table stylesTable;
	protected StackLayout stylesContributorAreaLayout;
	protected Composite styleContributorArea;
	//    protected Button requireIVJforComponents ;
	protected Button generateTryCatchBlock;
	protected Text sourceToVisual;
	protected Group sourceSyncGrp;

	protected Image fJavaBeansToSourceImage;
	protected Image fSourceToJavaBeansImage;
	protected Image fJavaBeansViewImage;
	protected Image fPropertiesViewImage;	

	protected int fLookAndFeelSelected;
	
	private Button openPropertiesViewIfRequired;
	private Button openJavaBeansViewIfRequired;
	private Button noverifyCheckbox;

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

	protected Text createLabelText(Composite parent, String label, int noCharacters, int indent) {

		Composite c = new Composite(parent, SWT.NONE);
		GridLayout gridLayout = new GridLayout();
		gridLayout.marginHeight = 0;
		gridLayout.numColumns = 2;
		GridData cData = new GridData();
		cData.horizontalIndent = indent;
		c.setLayoutData(cData);
		c.setLayout(gridLayout);

		Label l = createLabel(c, label, null);
		l.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_CENTER));
		Text text = new Text(c, SWT.SINGLE | SWT.BORDER);
		GridData data = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
		data.widthHint = convertWidthInCharsToPixels(noCharacters);
		text.setLayoutData(data);
		return text;
	}
	
	private static final String HOMELINK = "hp";	//$NON-NLS-1$
	private static final String NEWSGROUPLINK = "ng";	//$NON-NLS-1$
	protected Control createContents(Composite parent) {

		Composite contentsParent = new Composite(parent, SWT.NONE);
		contentsParent.setLayout(new GridLayout());
		
		Link newsgroupLink = new Link(contentsParent,SWT.NONE);
		newsgroupLink.setLayoutData(new GridData(SWT.END, SWT.CENTER, true, false));	// Right align
		newsgroupLink.setText(VCEMessages.VCEPreferencePage_LinkToResources);
		newsgroupLink.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				URL url = null;
				if (HOMELINK.equals(e.text))
					url = JavaVEPlugin.URL_HOMEPAGE;
				else if (NEWSGROUPLINK.equals(e.text))
					url = JavaVEPlugin.URL_NEWSGROUP;
				if (url != null) {
					IWorkbenchBrowserSupport support = PlatformUI.getWorkbench().getBrowserSupport();
					try {
						IWebBrowser browser = support.getExternalBrowser();
						browser.openURL(url);
					}
					catch (PartInitException e2) {
					}
				}
			};
		});
		
		
		// The contents area is divided into notebooks to make best use of real estate
		// Current tabs are for appearance and for code generation
		tabFolder = new TabFolder(parent, SWT.NONE);
		tabFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));	// Fill both and grab extra both.

		createAppearanceTab();
		createCodeGenerationTab();
		createStylesTab();
		initializeGUIControlsFromStore();
		
		applyDialogFont(contentsParent);

		return contentsParent;
	}
	protected void createAppearanceTab() {

		appearanceTab = new TabItem(tabFolder, SWT.NONE);
		appearanceTab.setText(VCEMessages.VCEPreferencePage_Tab_Appearance_Text); 
		Composite appearanceComposite = new Composite(tabFolder, SWT.NONE);
		appearanceComposite.setLayout(new GridLayout());
		appearanceComposite.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_FILL | GridData.HORIZONTAL_ALIGN_FILL));

		appearanceTab.setControl(appearanceComposite);

		// Appearance controls
		Group group = createGroup(appearanceComposite, VCEMessages.PreferencePage_EditorGroup_Title, 2); 
		splitRadioButton = createRadioButton(group, VCEMessages.PreferencePage_EditorGroup_SplitPaneOrientation); 
		notebookRadioButton = createRadioButton(group, VCEMessages.PreferencePage_EditorGroup_UseNoteBook); 

		Composite openComposite = new Composite(appearanceComposite, SWT.NONE);
		openComposite.setLayout(new GridLayout(2, false));

		openPropertiesViewIfRequired = createCheckBox(openComposite, VCEMessages.PreferencePage_OpenView_Properties, 0); 
		fPropertiesViewImage = CDEPlugin.getImageFromBundle(Platform.getBundle("org.eclipse.ui"), "icons/full/eview16/prop_ps.gif");	//$NON-NLS-1$ //$NON-NLS-2$
		createLabel(openComposite, null, fPropertiesViewImage);

		openJavaBeansViewIfRequired = createCheckBox(openComposite, VCEMessages.PreferencePage_OpenView_JavaBeans, 0); 
		fJavaBeansViewImage = CDEPlugin.getImageFromPlugin(JavaVEPlugin.getPlugin(), "icons/full/ctool16/javavisualeditor_co.gif"); //$NON-NLS-1$	
		createLabel(openComposite, null, fJavaBeansViewImage);

		lookAndFeelLabel = createLabel(appearanceComposite, VCEMessages.PreferencePage_LookAndFeel_Title, null); 

		// Look and feel is a list of known ones plus a button to let users define new look and feel classes
		Composite lookAndFeelComposite = new Composite(appearanceComposite, SWT.NONE);
		lookAndFeelComposite.setLayout(new GridLayout(2, false));
		GridData lfData = new GridData(GridData.FILL_HORIZONTAL);
		lookAndFeelComposite.setLayoutData(lfData);

		lookAndFeelTable = createTable(lookAndFeelComposite, SWT.CHECK | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL, new String[] { VCEMessages.VCEPreferencePage_Tab_Appearance_Table_LookAndFeel_Name, VCEMessages.VCEPreferencePage_Tab_Appearance_Table_LookAndFeel_Class}); 

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
		bAdd.setText(VCEMessages.VCEPreferencePage_Tab_Appearance_Button_New); 
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
		bEdit.setText(VCEMessages.VCEPreferencePage_Tab_Appearance_Button_Edit); 
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
		bRemove.setText(VCEMessages.VCEPreferencePage_Tab_Appearance_Button_Remove); 
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
		
		showGridCheckBox = createCheckBox(appearanceComposite, VCEMessages.PreferencePage_ShowGridWhenSelected, 15); 

		// Create controls for setting the XY grid spacing default
		xyGridSpacingText = createLabelText(appearanceComposite, VCEMessages.VCEPreferencePage_XYGridSpacing, 10, 12);

		renameAskCheckbox = createCheckBox(appearanceComposite, VCEMessages.VCEPreferencePage_Checkbox_PromptNameOnCreation_Text, 15); 

		showWindowCheckBox = createCheckBox(appearanceComposite, VCEMessages.PreferencePage_ShowLiveWindow, 15); 
		showXMLTextCheckBox = createCheckBox(appearanceComposite, VCEMessages.PreferencePage_ShowXMLText, 15); 
		showClipboardCheckBox = createCheckBox(appearanceComposite, VCEMessages.PreferencePage_ShowClipboardText, 15); 
				
		if (!VCEPreferences.isLiveWindow())
			showWindowCheckBox.setVisible(false);
		if (!VCEPreferences.isXMLText())
			showXMLTextCheckBox.setVisible(false);
		if (!VCEPreferences.isClipboardText())
			showClipboardCheckBox.setVisible(false);		
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
		codeGenTab.setText(VCEMessages.VCEPreferencePage_Tab_CodeGeneration_Text); 
		Composite codeGenComposite = new Composite(tabFolder, SWT.NONE);
		codeGenComposite.setLayout(new GridLayout());
		codeGenComposite.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_FILL | GridData.HORIZONTAL_ALIGN_FILL));
		codeGenTab.setControl(codeGenComposite);

		Group codeGenGroup_Gen = createGroup(codeGenComposite, VCEMessages.PreferencePage_CodeGen_ParsingGeneration_Style_1, 1); 

		generateExpressionComment = createCheckBox(codeGenGroup_Gen, VCEMessages.PreferencePage_NewExpressionCommentPrompt, 0); 
		generateTryCatchBlock = createCheckBox(codeGenGroup_Gen, VCEMessages.PreferencePage_GenerateTryCatchBlock, 0); 

		// Create a listener to see when the values in text boxes changes so we can trigger revalidation
		ModifyListener modifyListener = new ModifyListener() {
			public void modifyText(ModifyEvent e) {
					// Validate that the value in the sourceToVisual is an integer between 1 and 9;
	validateFields();
			}
		};

		sourceSyncGrp = createGroup(codeGenComposite, VCEMessages.PreferencePage_CodeGen_Source_Synchronization_Delay, 6); 
		// The group is 5 wide.

		// The JavaBeansToSource are 6 wide in the group
		createLabel(sourceSyncGrp, VCEMessages.PreferencePage_CodeGen_SourceToJavaBeans, null); 
		sourceToVisual = createText(sourceSyncGrp, 11, 1); //$NON-NLS-1$
		sourceToVisual.addModifyListener(modifyListener);
		sourceToVisual.setTextLimit(9);
		
		Label spacer = new Label(sourceSyncGrp, SWT.NONE);
		spacer.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
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
		stylesTab.setText(VCEMessages.VCEPreferencePage_Tab_Styles_Text); 

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
		stylesTable = createTable(stylesComposite, SWT.SINGLE | SWT.CHECK | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL, new String[] { VCEMessages.VCEPreferencePage_Tab_Styles_Table_Styles}); 
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
		
		filler = createLabel(stylesComposite, "", null); //$NON-NLS-1$
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan=2;
		filler.setLayoutData(gd);
		noverifyCheckbox = new Button(stylesComposite, SWT.CHECK);
		noverifyCheckbox.setText(VCEMessages.VCEPreferencePage_NoverifyCheckbox_text); 
		noverifyCheckbox.setSelection(ProxyPlugin.getPlugin().getPluginPreferences().getBoolean(ProxyPlugin.PREFERENCES_VM_NOVERIFY_KEY));
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan=2;
		noverifyCheckbox.setLayoutData(gd);
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
					!Platform.getOS().equals(Platform.OS_WIN32))
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

	private Preferences getStore() {
		if (fStore != null)
			return fStore;
		fStore = VCEPreferences.getPlugin().getPluginPreferences();
		cdeStore = CDEPlugin.getPlugin().getPluginPreferences();
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
		showGridCheckBox.setSelection(cdeStore.getBoolean(CDEPlugin.SHOW_GRID_WHEN_SELECTED));
		xyGridSpacingText.setText(String.valueOf(cdeStore.getInt(CDEPlugin.XY_GRID_SPACING)));
		if(VCEPreferences.isLinux()){
			renameAskCheckbox.setSelection(false);
			renameAskCheckbox.setEnabled(false);
		} else {
			renameAskCheckbox.setSelection(fStore.getBoolean(VCEPreferences.RENAME_INSTANCE_ASK_KEY));			
		}
		showWindowCheckBox.setSelection(fStore.getBoolean(VCEPreferences.SHOW_LIVE_WINDOW));
		showXMLTextCheckBox.setSelection(CDEPlugin.getPlugin().getPluginPreferences().getBoolean(CDEPlugin.SHOW_XML));
		showClipboardCheckBox.setSelection(fStore.getBoolean(VCEPreferences.USE_TEXT_FOR_CLIPBOARD));		

		boolean showAsNotebook = fStore.getBoolean(VCEPreferences.NOTEBOOK_PAGE);
		splitRadioButton.setSelection(!showAsNotebook);
		notebookRadioButton.setSelection(showAsNotebook);

		// Code Generation
		//	requireIVJforComponents.setSelection(fStore.getString(VCEPreferences.REQUIRE_IVJ_COMPONENTS).length()==0 ?
		//	                                     false : fStore.getBoolean(VCEPreferences.REQUIRE_IVJ_COMPONENTS)) ;		

		generateExpressionComment.setSelection(fStore.getBoolean(VCEPreferences.GENERATE_COMMENT));
		generateTryCatchBlock.setSelection(fStore.getBoolean(VCEPreferences.GENERATE_TRY_CATCH_BLOCK));
		sourceToVisual.setText(fStore.getString(VCEPreferences.SOURCE_SYNC_DELAY));
		calculateTotalSourceToVisualTime();
	}
	protected void validateFields() {
		// Validate that the sourceSyncDelayText is an integer greater than the default of 500ms
		boolean isValid = true;
		try {
			int val = Integer.parseInt(sourceToVisual.getText());
			if (val < VCEPreferences.DEFAULT_SYNC_DELAY) {
				isValid = false;
				setErrorMessage(VCEMessages.PreferencePage_CodeGen_Error_DelayTimeMinimum_ERROR_); 
				setValid(false);
			}
		} catch (NumberFormatException ex) {
			isValid = false;
			setErrorMessage(VCEMessages.PreferencePage_CodeGen_Error_DelayTimeMustBeInteger_ERROR_); 
			setValid(false);
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
			int total = Integer.parseInt(sourceToVisual.getText());
			if(total<0 || total>Integer.MAX_VALUE){
				setErrorMessage(PropertysheetMessages.not_integer_WARN_);
				return false;
			}
			return true;
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
		fStore.setValue(VCEPreferences.USE_TEXT_FOR_CLIPBOARD, showClipboardCheckBox.getSelection());
		// Save whether to show the VCE and Source as a split pane or notebook
		fStore.setValue(VCEPreferences.NOTEBOOK_PAGE, notebookRadioButton.getSelection());
		
		cdeStore.setValue(CDEPlugin.SHOW_GRID_WHEN_SELECTED,showGridCheckBox.getSelection());
		try {
			int gridSpacing = Integer.parseInt(xyGridSpacingText.getText());
			if (gridSpacing > 1)
				cdeStore.setValue(CDEPlugin.XY_GRID_SPACING, gridSpacing);
		} catch (NumberFormatException e) {
			// must be a number
		}
		fStore.setValue(VCEPreferences.RENAME_INSTANCE_ASK_KEY, renameAskCheckbox.getSelection());
		fStore.setValue(VCEPreferences.OPEN_PROPERTIES_VIEW, openPropertiesViewIfRequired.getSelection());
		fStore.setValue(VCEPreferences.OPEN_JAVABEANS_VIEW, openJavaBeansViewIfRequired.getSelection());

		fStore.setValue(VCEPreferences.GENERATE_COMMENT, generateExpressionComment.getSelection());
		fStore.setValue(VCEPreferences.GENERATE_TRY_CATCH_BLOCK, generateTryCatchBlock.getSelection());
		fStore.setValue(VCEPreferences.SOURCE_SYNC_DELAY, Integer.parseInt(sourceToVisual.getText()));		
		//	fStore.setValue(VCEPreferences.REQUIRE_IVJ_COMPONENTS,requireIVJforComponents.getSelection()) ;

		fStore.setValue(VCEPreferences.JVE_PATTERN_STYLE_ID, fStyleID);
		
		ProxyPlugin.getPlugin().getPluginPreferences().setValue(ProxyPlugin.PREFERENCES_VM_NOVERIFY_KEY, noverifyCheckbox.getSelection());
		ProxyPlugin.getPlugin().savePluginPreferences();
		
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
		Preferences store = getStore();
		showXMLTextCheckBox.setSelection(store.getDefaultBoolean(VCEPreferences.SELECT_SOURCE));
		showWindowCheckBox.setSelection(store.getDefaultBoolean(VCEPreferences.SHOW_LIVE_WINDOW));
		showClipboardCheckBox.setSelection(store.getDefaultBoolean(VCEPreferences.USE_TEXT_FOR_CLIPBOARD));		
		showGridCheckBox.setSelection(cdeStore.getDefaultBoolean(CDEPlugin.SHOW_GRID_WHEN_SELECTED));
		xyGridSpacingText.setText(String.valueOf(cdeStore.getDefaultInt(CDEPlugin.XY_GRID_SPACING)));
		if(VCEPreferences.isLinux()){
			renameAskCheckbox.setSelection(false);
		} else {
			renameAskCheckbox.setSelection(fStore.getDefaultBoolean(VCEPreferences.RENAME_INSTANCE_ASK_KEY));
		}
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
		generateExpressionComment.setSelection(store.getDefaultBoolean(VCEPreferences.GENERATE_COMMENT));
		generateTryCatchBlock.setSelection(store.getDefaultBoolean(VCEPreferences.GENERATE_TRY_CATCH_BLOCK));
		splitRadioButton.setSelection(!store.getDefaultBoolean(VCEPreferences.NOTEBOOK_PAGE));
		sourceToVisual.setText(Integer.toString(VCEPreferences.DEFAULT_SYNC_DELAY));
		
		openPropertiesViewIfRequired.setSelection(true);
		openJavaBeansViewIfRequired.setSelection(true);
		//	requireIVJforComponents.setSelection(false) ;
		
		noverifyCheckbox.setSelection(ProxyPlugin.getPlugin().getPluginPreferences().getDefaultBoolean(ProxyPlugin.PREFERENCES_VM_NOVERIFY_KEY));
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
		if (fJavaBeansToSourceImage != null)
			fJavaBeansToSourceImage.dispose();
		if (fSourceToJavaBeansImage != null)
			fSourceToJavaBeansImage.dispose();

	}

}
