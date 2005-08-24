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
package org.eclipse.ve.internal.swt;
/*
 *  $RCSfile: ImageDialog.java,v $
 *  $Revision: 1.6 $  $Date: 2005-08-24 23:52:55 $ 
 */

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.action.StatusLineManager;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;

public class ImageDialog extends Dialog {

	private ImageController icc = null;
	private String value;
	protected IProject myProject = null;
	private StatusLineManager statusLineManager;
	private Button projRadioButton, fileRadioButton;

	public ImageDialog(Shell parentShell, IProject project) {
		super(parentShell);
		setShellStyle(getShellStyle() | SWT.MIN | SWT.MAX | SWT.RESIZE);
		myProject = project;
	}

	protected Label createLabel(Composite group, String aLabelString) {
		Label label = new Label(group, SWT.LEFT);
		label.setText(aLabelString);
		return label;
	}

	protected Group createGroup(Composite aParent, String title, int numColumns) {
		Group group = new Group(aParent, SWT.NONE);
		group.setText(title);
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = numColumns;
		group.setLayout(gridLayout);
		group.setLayoutData(new GridData(GridData.FILL_BOTH));
		group.setFont(aParent.getFont());
		return group;
	}

	protected Text createLabelText(Composite parent, String label, int textStyle) {
		Composite c = new Composite(parent, SWT.NONE);
		GridLayout gridLayout = new GridLayout();
		gridLayout.marginHeight = gridLayout.marginWidth = 0;
		gridLayout.numColumns = 2;
		c.setLayout(gridLayout);
		c.setLayoutData(new GridData());

		Label l = createLabel(c, label);
		l.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING | GridData.VERTICAL_ALIGN_END));

		Text text = new Text(c, textStyle);
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		data.horizontalIndent = convertHorizontalDLUsToPixels(IDialogConstants.SMALL_INDENT);
		text.setLayoutData(data);
		return text;
	}

	protected Combo createLabelCombo(Composite parent, String label, int comboStyle) {
		Composite c = new Composite(parent, SWT.NONE);
		GridLayout gridLayout = new GridLayout();
		gridLayout.marginHeight = gridLayout.marginWidth = 0;
		gridLayout.numColumns = 2;
		c.setLayout(gridLayout);
		c.setLayoutData(new GridData());

		Label l = createLabel(c, label);
		l.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING | GridData.VERTICAL_ALIGN_END));

		Combo combo = new Combo(c, comboStyle);
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		data.horizontalIndent = convertHorizontalDLUsToPixels(IDialogConstants.SMALL_INDENT);
		combo.setLayoutData(data);
		return combo;
	}

	protected Label createLabeledLabel(Composite parent, String labelText, int textStyle) {
		Composite c = new Composite(parent, SWT.NONE);
		GridLayout gridLayout = new GridLayout();
		gridLayout.marginHeight = gridLayout.marginWidth = 0;
		gridLayout.numColumns = 2;
		c.setLayout(gridLayout);

		Label l = createLabel(c, labelText);
		l.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING));

		Label label = createLabel(c, ""); //$NON-NLS-1$
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		data.horizontalIndent = convertHorizontalDLUsToPixels(IDialogConstants.SMALL_INDENT);
		label.setLayoutData(data);
		return label;
	}

	protected SingleImageDisplay createSingleImageDisplay(Composite group) {
		SingleImageDisplay sid = new SingleImageDisplay(group, SWT.NULL | SWT.H_SCROLL | SWT.V_SCROLL);
		return sid;
	}

	protected Tree createTree(Composite group) {
		Tree tree = new Tree(group, SWT.SINGLE | SWT.SHADOW_OUT | SWT.BORDER);
		return tree;
	}

	protected List createList(Composite group, int style) {
		List list = new List(group, style);
		return list;
	}

	protected ImageScreenDisplay createIconScreenDisplay(
		Composite parent,
		int style,
		int rows,
		int cols,
		int width,
		int height,
		boolean enableSelectable,
		boolean enableToolTip,
		int offset,
		int borderOffset) {
		ImageScreenDisplay disp =
			new ImageScreenDisplay(parent, style, rows, cols, width, height, enableSelectable, enableToolTip, offset, borderOffset);
		return disp;
	}

	protected Button createCheckBox(Composite group, String label) {
		Button button = new Button(group, SWT.CHECK | SWT.LEFT);
		button.setText(label);
		return button;
	}

	protected Button createButton(Composite group, String label) {
		Button button = new Button(group, SWT.PUSH | SWT.LEFT);
		button.setText(label);
		button.setLayoutData(new GridData());
		return button;
	}

	protected Button createRadioButton(Composite group, String label) {
		Button button = new Button(group, SWT.RADIO | SWT.LEFT);
		button.setText(label);
		return button;
	}

	protected GridData createSpacer(Composite parent) {
		Label spacer = new Label(parent, SWT.NONE);
		GridData data = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING | GridData.VERTICAL_ALIGN_BEGINNING);
		spacer.setLayoutData(data);
		return data;
	}

	private static final int GRID_IMAGE_WIDTH = 32, GRID_IMAGE_HEIGHT = 32; // Width/height of the images in the display grid
	private static final int GRID_ROWS = 5, GRID_COLS = 8; // Number of rows and columns by default in display grid;
	protected Control createDialogArea(Composite parent) {
		GridData data;

		Composite panel = (Composite) super.createDialogArea(parent);

		GridLayout gl = (GridLayout) panel.getLayout();
		gl.numColumns = 2;

		// Create BrowseIn group:
		Group locGroup = createGroup(panel, SWTMessages.ImageDialog_Group_Browse, 1); 
		data = (GridData) locGroup.getLayoutData();
		data.verticalSpan = 2;

		projRadioButton = createRadioButton(locGroup, SWTMessages.ImageDialog_Radio_BrowseProject); 
		projRadioButton.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING));
		fileRadioButton = createRadioButton(locGroup, SWTMessages.ImageDialog_Radio_BrowseFiles); 
		projRadioButton.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING));

		Label locLabel = createLabel(locGroup, SWTMessages.ImageDialog_Label_Location); 
		data = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
		int smallIndent = convertHorizontalDLUsToPixels(IDialogConstants.SMALL_INDENT);
		data.horizontalIndent = smallIndent;
		locLabel.setLayoutData(data);

		Tree direcTree = createTree(locGroup);
		data = new GridData(GridData.FILL_BOTH);
		data.heightHint = convertHeightInCharsToPixels(10);
		data.widthHint = convertWidthInCharsToPixels(30);
		data.horizontalIndent = smallIndent;
		direcTree.setLayoutData(data);

		Button crawlButton = createCheckBox(locGroup, SWTMessages.ImageDialog_Checkbox_SearchSubDirs); 
		data = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
		data.horizontalIndent = smallIndent;
		crawlButton.setLayoutData(data);

		// Column 2 should have an horizontal indent of leftMargin.
		int leftMargin = convertHorizontalDLUsToPixels(IDialogConstants.LEFT_MARGIN);

		// Filter
		Combo filter = createLabelCombo(panel, SWTMessages.ImageDialog_Text_Filter, SWT.DROP_DOWN); 
		data = (GridData) filter.getLayoutData();
		data.widthHint = convertWidthInCharsToPixels(20);
		filter.setToolTipText(SWTMessages.ImageDialog_Msg); 
		// Create selection list.
		String[] patterns = new String[ImageController.VALID_EXTENSIONS.length+1];
		patterns[0] = "";	// Blank, for search all. //$NON-NLS-1$
		for (int i = 0; i < ImageController.VALID_EXTENSIONS.length; i++) {
			patterns[i+1] = '*' + ImageController.VALID_EXTENSIONS[i];
		}
		filter.setItems(patterns);

		data = new GridData(GridData.FILL_HORIZONTAL);
		data.horizontalIndent = leftMargin;
		filter.getParent().setLayoutData(data);

		// Files composite
		Composite filesComposite = new Composite(panel, SWT.NULL);
		data = new GridData(GridData.FILL_BOTH);
		data.horizontalIndent = leftMargin;
		filesComposite.setLayoutData(data);
		GridLayout filesLayout = new GridLayout();
		filesLayout.marginHeight = filesLayout.marginWidth = filesLayout.verticalSpacing = 0;
		filesComposite.setLayout(filesLayout);
		filesComposite.setFont(panel.getFont());

		Label filesLabel = createLabel(filesComposite, SWTMessages.ImageDialog_Label_Files); 
		filesLabel.setLayoutData(new GridData());

		List fileList = createList(filesComposite, SWT.V_SCROLL | SWT.SHADOW_OUT | SWT.BORDER);
		data = new GridData(GridData.FILL_BOTH);
		data.heightHint = convertHeightInCharsToPixels(10);
		data.widthHint = convertWidthInCharsToPixels(30);
		fileList.setLayoutData(data);

		createSpacer(panel);
		createSpacer(panel);

		// Available Group
		Group availableImages = createGroup(panel, SWTMessages.ImageDialog_Group_AvailableIcons, 1); 

		ImageScreenDisplay drawingBoard =
			createIconScreenDisplay(
				availableImages,
				SWT.V_SCROLL | SWT.SHADOW_OUT | SWT.BORDER,
				GRID_ROWS,
				GRID_COLS,
				GRID_IMAGE_WIDTH,
				GRID_IMAGE_HEIGHT,
				true,
				true,
				5,
				10);
		data = new GridData(GridData.FILL_BOTH);
		data.widthHint = GRID_COLS * GRID_IMAGE_WIDTH;
		data.heightHint = GRID_ROWS * GRID_IMAGE_HEIGHT;
		drawingBoard.setLayoutData(data);

		// Selected Image Group
		Group selectedImage = createGroup(panel, SWTMessages.ImageDialog_Group_SelectedImage, 1); 
		data = (GridData) selectedImage.getLayoutData();
		data.heightHint = 160;
		data.widthHint = 150;
		data.horizontalIndent = leftMargin;

		SingleImageDisplay imageDisplay = createSingleImageDisplay(selectedImage);
		imageDisplay.setLayoutData(new GridData(GridData.FILL_BOTH));

		// Path field        
		final Label path = createLabeledLabel(panel, SWTMessages.ImageDialog_Text_PathOfFile, SWT.NULL); 
		data = new GridData(GridData.FILL_HORIZONTAL);
		data.horizontalSpan = 2;
		path.getParent().setLayoutData(data);

		icc =
			new ImageController(
				this,
				myProject,
				direcTree,
				fileList,
				drawingBoard,
				path,
				imageDisplay,
				panel.getDisplay(),
				crawlButton,
				locGroup,
				filter);

		parent.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(org.eclipse.swt.events.DisposeEvent e) {
				icc.dispose();
			}
		});

		projRadioButton.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
			}
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				if (((Button) e.widget).getSelection())
					icc.setSearchScope(ImageController.SEARCH_PROJECT);
			}
		});
		fileRadioButton.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
			}
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				if (((Button) e.widget).getSelection())
					icc.setSearchScope(ImageController.SEARCH_FILE_SYSTEM);
			}
		});
		//fileRadioButton.setEnabled( false );

		getShell().setText(SWTMessages.ImageDialog_Shell_Text); 

		applyDialogFont(panel);
		return panel;
	} // createContents()

	protected void okPressed() {
		String initStr = icc.getInitString();
		if (initStr.equals("")) //$NON-NLS-1$
			cancelPressed();
		else {
			setValue(icc.getInitString());
			super.okPressed();
		}
	}

	public void setValue(String pathname) {
		if (pathname != null && !pathname.equals("")) { //$NON-NLS-1$
			value = pathname;
		}
	}

	public String getValue() {
		return value;
	}

	protected Button getButton(int id) {
		return super.getButton(id);
	}

	protected StatusLineManager getStatusLineManager() {
		return statusLineManager;
	}

	protected void createButtonsForButtonBar(Composite parent) {
		super.createButtonsForButtonBar(parent);

		super.getButton(IDialogConstants.OK_ID).setEnabled(false); // OK button should be initially disabled.
	}

	protected Control createContents(Composite parent) {
		Composite panel = (Composite) super.createContents(parent);

		statusLineManager = new StatusLineManager();
		Control statusLine = statusLineManager.createControl(panel);
		statusLine.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		// Now it can be set, after everything is created.
		switch (icc.setInitialValue(getValue())) {
			case ImageController.SEARCH_PROJECT :
				projRadioButton.setSelection(true);
				break;
			case ImageController.SEARCH_FILE_SYSTEM :
				fileRadioButton.setSelection(true);
				break;
		}
		return panel;
	}

}
