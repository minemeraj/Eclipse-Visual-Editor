
package org.eclipse.ve.internal.swt;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.*;
import org.eclipse.ve.internal.java.core.BeanProxyUtilities;
import org.eclipse.ve.internal.java.core.Spinner;

public class ColorPropertyEditor implements PropertyEditor {
	
	private static final String BUNDLE_NAME = "org.eclipse.ve.internal.swt.colorpropertyeditor";//$NON-NLS-1$
	private static java.util.ResourceBundle messages = java.util.ResourceBundle.getBundle(BUNDLE_NAME);
	
	private class PreviewPanel {
		private Group control;
		private Canvas canvas;
		
		private Color initial;
		private Color black;
		private Color white;
		
		private static final int spacing = 5;
		private static final int bigRect = 25;
		private static final int medRect = 15;
		private static final int smRect = 5;
		
		private static final int swatchWidth  = 50;
		private static final int swatchHeight = 25;
		
		private final String previewText = messages.getString("previewText"); //$NON-NLS-1$

		public Control createControl(Composite parent, int style, Color initialColor) {
			if (control == null) {
				control = new Group(parent, style);
				control.setText(messages.getString("previewGroupTitle")); //$NON-NLS-1$
				RowLayout rowLayout = new RowLayout();
				rowLayout.wrap = false;
				rowLayout.pack = true;
				rowLayout.justify = true;
				control.setLayout(rowLayout);
				
				canvas = new Canvas(control, SWT.NONE);
				
				black = canvas.getDisplay().getSystemColor(SWT.COLOR_BLACK);
				white = canvas.getDisplay().getSystemColor(SWT.COLOR_WHITE);
				
				canvas.setLayoutData(new RowData(computeDrawingSize()));

				control.pack();
				canvas.addPaintListener(new PaintListener() {
					public void paintControl(PaintEvent e) {
						paint();
					}
				});
				
				initial = new Color(control.getDisplay(), initialColor.getRGB());
				control.addDisposeListener(new DisposeListener() {
					public void widgetDisposed(DisposeEvent e) {
						if (initial != null && !initial.isDisposed()) {
							initial.dispose();
							initial = null;
						}
					}
				});
				
				paint();
				
			}
			return control;
		}
		
		private Point computeDrawingSize() {
			GC surface = new GC(canvas);
			Point previewSize = surface.stringExtent(previewText);
			surface.dispose();
			int width = 3 * bigRect + 5 * spacing + swatchWidth + previewSize.x + 5;
			int textHeight = (previewSize.y + 5 ) * 3 + spacing * 2;
			int shapesHeight = bigRect * 2 + spacing; 
			int height = (textHeight > shapesHeight) ? textHeight : shapesHeight;
			
			return new Point(width, height);
		}
		
		public void paint() {
			int x = 0;
			int y = 0;
			
			Color current = getColor();
			
			synchronized (current) {
				if (current == null || current.isDisposed()) {
					return;
				}

				GC surface = new GC(canvas);

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
				surface.setBackground(initial);
				surface.fillRectangle(x, y, swatchWidth, swatchHeight);

				// current
				y = y + swatchHeight;
				surface.setBackground(current);
				surface.fillRectangle(x, y, swatchWidth, swatchHeight);

				surface.dispose();
			}
		}
	}
	
	private java.util.List fPropertyChangeListeners;
	
	private IJavaObjectInstance fExistingValue;

	private static final String COLOR_PREFIX = "org.eclipse.swt.SWT.COLOR_"; //$NON-NLS-1$
	
	private static final String[] basicColorNames = { messages.getString("black"), messages.getString("blue"), messages.getString("cyan"), messages.getString("gray"), messages.getString("green"), messages.getString("magenta"), messages.getString("red"), messages.getString("white"), messages.getString("yellow"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$
		messages.getString("darkBlue"), messages.getString("darkCyan"), messages.getString("darkGray"), messages.getString("darkGreen"), messages.getString("darkMagenta"), messages.getString("darkRed"), messages.getString("darkYellow") }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$
	
	private static final String[] basicColorConstants = { "BLACK", "BLUE", "CYAN", "GRAY", "GREEN", "MAGENTA", "RED", "WHITE", "YELLOW", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$
		"DARK_BLUE", "DARK_CYAN", "DARK_GRAY", "DARK_GREEN", "DARK_MAGENTA", "DARK_RED", "DARK_YELLOW" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$
	
	private static final int[] basicColorConstantValues = { SWT.COLOR_BLACK, SWT.COLOR_BLUE, SWT.COLOR_CYAN, SWT.COLOR_GRAY, SWT.COLOR_GREEN,
		SWT.COLOR_MAGENTA, SWT.COLOR_RED, SWT.COLOR_WHITE, SWT.COLOR_YELLOW,
		SWT.COLOR_DARK_BLUE, SWT.COLOR_DARK_CYAN, SWT.COLOR_DARK_GRAY, SWT.COLOR_DARK_GREEN, SWT.COLOR_DARK_MAGENTA, SWT.COLOR_DARK_RED, 
		SWT.COLOR_DARK_YELLOW };
		
	private static Color[] basicColorValues = new Color[basicColorConstantValues.length];
	private Image basicColorImages[] = new Image[basicColorValues.length];
		
	private static final String[] systemColorNames = { messages.getString("infoBackground"), messages.getString("infoForeground"),  //$NON-NLS-1$ //$NON-NLS-2$
		messages.getString("listBackground"), messages.getString("listForeground"), messages.getString("listSeletion"), messages.getString("listSelectionText"),  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		messages.getString("titleBackground"), messages.getString("titleBackgroundGradient"), messages.getString("titleForeground"), messages.getString("titleInactiveBackground"), messages.getString("titleInactiveBackgroundGradient"),  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
		messages.getString("titleInactiveForeground"), //$NON-NLS-1$
		messages.getString("widgetBackground"), messages.getString("widgetBorder"), messages.getString("widgetDarkShadow"), messages.getString("widgetForeground"), messages.getString("widgetHighlightShadow"), messages.getString("widgetLightShadow"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
		messages.getString("widgetNormalShadow") }; //$NON-NLS-1$

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
	
	private Color black;
	
	private Composite control = null;
	private PreviewPanel preview = null;
	
	private Table basicTable;
	private Table systemTable;
	
	private Spinner redSpinner;
	private Spinner greenSpinner;
	private Spinner blueSpinner;
	
	private Scale redScale;
	private Scale greenScale;
	private Scale blueScale;
	
	private Color color = null;
	private boolean isNamed = false;
	private boolean isBasic = false;
	private boolean isSystem = false;
	
	private boolean changeInProcess = false;
	
	private static final int NAMED_SWATCH_SIZE = 10;
	private static final int NAMED_LIST_HEIGHT = 175;
	private static final int NAMED_LIST_WIDTH = 150;
	
	public ColorPropertyEditor() {
	}
	
	public ColorPropertyEditor(Color initialColor) {
		setColor(initialColor);
	}
	
	
	public Control createControl(Composite parent, int style) {
		return createControl(parent, style, true);
	}
	
	/* (non-Javadoc)
	 * @see PropertyEditor#createControl(org.eclipse.swt.widgets.Composite, int)
	 */
	public Control createControl(Composite parent, int style, boolean showPreview) {
		if (control == null || control.isDisposed()) {
			control = new Composite(parent, style);
			
			black = control.getDisplay().getSystemColor(SWT.COLOR_BLACK);
			
			// Setup default initial color if it's not set yet
			if (color == null) {
				//setColor(new Color(control.getDisplay(),150,200,150), false);
				setColor(control.getDisplay().getSystemColor(SWT.COLOR_DARK_GREEN), true);
				isBasic = true;
			}
			
			GridLayout grid = new GridLayout();
			grid.numColumns = 1;
			grid.verticalSpacing = 5;
			control.setLayout(grid);
		
			TabFolder tabPane = new TabFolder(control, SWT.NONE);
			GridData tabGD = new GridData();
			tabGD.verticalAlignment = GridData.FILL;
			tabGD.horizontalAlignment = GridData.FILL;
			tabGD.grabExcessHorizontalSpace = true;
			tabGD.grabExcessVerticalSpace = true;
			tabPane.setLayoutData(tabGD);
			
			TabItem namedPage = new TabItem(tabPane, SWT.NONE);
			namedPage.setText(messages.getString("namedTabTitle")); //$NON-NLS-1$
			Control namedPageContents = makeNamedPage(tabPane, SWT.NONE);
			namedPage.setControl(namedPageContents);
			
			TabItem rgbPage = new TabItem(tabPane, SWT.NONE);
			rgbPage.setText(messages.getString("rgbTabTitle")); //$NON-NLS-1$
			Control rgbPageContents = makeRGBPage(tabPane, SWT.NONE);
			rgbPage.setControl(rgbPageContents);
			
			if ( ! isNamed ) {
				tabPane.setSelection(1);
			}
		
			if (showPreview) {
				preview = new PreviewPanel();
				Control pControl = preview.createControl(control, SWT.NONE, getColor());
				GridData previewGD = new GridData();
				previewGD.verticalAlignment = GridData.CENTER;
				previewGD.horizontalAlignment = GridData.FILL;
				previewGD.grabExcessHorizontalSpace = true;
				previewGD.grabExcessVerticalSpace = false;
				pControl.setLayoutData(previewGD);
			}
			control.pack();
		}
		return control;
	}
	
	private Control makeNamedPage(Composite parent, int style) {
		Composite page = new Composite(parent, style);
		
		int selection = -1;
		
		initializeColorImages(page.getDisplay());
		
		RowLayout rowLayout = new RowLayout();
		rowLayout.wrap = false;
		rowLayout.pack = true;
		rowLayout.justify = true;
		page.setLayout(rowLayout);
		
		Group basicGroup = new Group(page, SWT.NONE);
		basicGroup.setText(messages.getString("basicColorsGroupTitle")); //$NON-NLS-1$
		basicGroup.setLayout(new RowLayout());
		
		basicTable = new Table(basicGroup, SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER | SWT.FULL_SELECTION);
		basicTable.setHeaderVisible(false);
		basicTable.setLinesVisible(false);
		
		for (int i = 0; i < basicColorNames.length; i++) {
			TableItem ti = new TableItem(basicTable, SWT.NONE);
			ti.setText(basicColorNames[i]);
			ti.setData(new Integer(i));
			ti.setImage(makeSwatchIcon(ti.getDisplay(), basicColorValues[i]));
			if (color.equals(basicColorValues[i])) {
				selection = i;
			}
		}
		if ( selection != -1 ) {
			basicTable.setSelection(selection);
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
						systemTable.deselectAll();
						updateSpinnersFromColor();
						isBasic = true;
						changeInProcess = false;
					}
				}
			}
		});
		
		Group systemGroup = new Group(page, SWT.NONE);
		systemGroup.setText(messages.getString("SystemColorsGroupTitle")); //$NON-NLS-1$
		systemGroup.setLayout(new RowLayout());
		
		systemTable = new Table(systemGroup, SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER | SWT.FULL_SELECTION);
		systemTable.setHeaderVisible(false);
		systemTable.setLinesVisible(false);
		
		for (int i = 0; i < systemColorNames.length; i++) {
			TableItem ti = new TableItem(systemTable, SWT.NONE);
			ti.setText(systemColorNames[i]);
			ti.setData(new Integer(i));
			ti.setImage(makeSwatchIcon(ti.getDisplay(), systemColorValues[i]));
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
						basicTable.deselectAll();
						updateSpinnersFromColor();
						isSystem = true;
						changeInProcess = false;
					}
				}
			}
		});
		
		page.addDisposeListener(new DisposeListener() {
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
		
		return page;
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
	
	private Control makeRGBPage(Composite parent, int style) {
		Composite page = new Composite(parent, style);
		GridLayout grid = new GridLayout();
		grid.numColumns = 1;
		grid.verticalSpacing = 5;
		grid.marginHeight = 5;
		grid.marginWidth = 5;
		page.setLayout(grid);
		
		Composite rgbPanel = new Composite(page, SWT.NONE);
		GridData GD1 = new GridData();
		GD1.grabExcessHorizontalSpace = true;
		GD1.grabExcessVerticalSpace = true;
		GD1.horizontalAlignment = GridData.CENTER;
		GD1.verticalAlignment = GridData.CENTER;
		rgbPanel.setLayoutData(GD1);
		
		GridLayout grid2 = new GridLayout();
		grid2.numColumns = 3;
		grid2.verticalSpacing = 5;
		grid2.horizontalSpacing = 10;
		grid2.marginHeight = 5;
		grid2.marginWidth = 5;
		rgbPanel.setLayout(grid2);
		
		Label redL = new Label(rgbPanel, SWT.NONE);
		redL.setText(messages.getString("redSliderLabel")); //$NON-NLS-1$
		
		redScale = new Scale(rgbPanel, SWT.NONE);
		redScale.setMinimum(0);
		redScale.setMaximum(255);
		redScale.setPageIncrement(51);
		redScale.setSelection(color.getRed());
		
		redSpinner = new Spinner(rgbPanel, SWT.NONE, color.getRed());
		redSpinner.setMinimum(0);
		redSpinner.setMaximum(255);
		
		Label greenL = new Label(rgbPanel, SWT.NONE);
		greenL.setText(messages.getString("greenSliderLabel")); //$NON-NLS-1$
		
		greenScale = new Scale(rgbPanel, SWT.NONE);
		greenScale.setMinimum(0);
		greenScale.setMaximum(255);
		greenScale.setPageIncrement(51);
		greenScale.setSelection(color.getGreen());
		
		greenSpinner = new Spinner(rgbPanel, SWT.NONE, color.getGreen());
		greenSpinner.setMinimum(0);
		greenSpinner.setMaximum(255);
		
		Label blueL = new Label(rgbPanel, SWT.NONE);
		blueL.setText(messages.getString("blueSliderLabel")); //$NON-NLS-1$
		
		blueScale = new Scale(rgbPanel, SWT.NONE);
		blueScale.setMinimum(0);
		blueScale.setMaximum(255);
		blueScale.setPageIncrement(51);
		blueScale.setSelection(color.getBlue());
				
		blueSpinner = new Spinner(rgbPanel, SWT.NONE, color.getBlue());
		blueSpinner.setMinimum(0);
		blueSpinner.setMaximum(255);

		Listener modifyListener = new Listener() {
			public void handleEvent(Event e) {
				Spinner s = (Spinner)e.widget;
				if (s == redSpinner) {
					redScale.setSelection(redSpinner.getValue());
				} else if (s == greenSpinner) {
					greenScale.setSelection(greenSpinner.getValue());
				} else if (s == blueSpinner) {
					blueScale.setSelection(blueSpinner.getValue());
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
					redSpinner.setValue(redScale.getSelection());
					redSpinner.setEnabled(true);
				} else if (e.widget == greenScale) {
					greenSpinner.setValue(greenScale.getSelection());
					greenSpinner.setEnabled(true);
				} else if (e.widget == blueScale) {
					blueSpinner.setValue(blueScale.getSelection());
					blueSpinner.setEnabled(true);
				}
			}
		};
		
		redScale.addSelectionListener(scaleListener);
		blueScale.addSelectionListener(scaleListener);
		greenScale.addSelectionListener(scaleListener);
		
		Button externalChooser = new Button(page, SWT.PUSH);
		externalChooser.setText(messages.getString("advancedButton")); //$NON-NLS-1$
		externalChooser.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				Shell shell = e.widget.getDisplay().getActiveShell();
				ColorDialog c = new ColorDialog(shell, SWT.APPLICATION_MODAL);
				RGB result = c.open();
				if (result != null) {
					redSpinner.setValue(result.red);
					greenSpinner.setValue(result.green);
					blueSpinner.setValue(result.blue);
				}
			}
		});
		GridData GD2 = new GridData();
		GD2.grabExcessHorizontalSpace = true;
		GD2.grabExcessVerticalSpace = true;
		GD2.horizontalAlignment = GridData.END;
		GD2.verticalAlignment = GridData.END;
		externalChooser.setLayoutData(GD2);
		
		page.pack();

		return page;
	}

	private void updateColorFromSpinners() {
		Color c = new Color(control.getDisplay(), redSpinner.getValue(), greenSpinner.getValue(), blueSpinner.getValue());
		setColor(c, false);
	}
	
	private void updateSpinnersFromColor() {
		Color c = getColor();
		redSpinner.setValue(c.getRed());
		greenSpinner.setValue(c.getGreen());		
		blueSpinner.setValue(c.getBlue());
	}
	
	public Color getColor() {
		return color;
	}
	
	public void setColor(Color c) {
		setColor(c, false);
		
	}
	
	private void setColor(Color c, boolean named) {
		if (color != null && !color.isDisposed() && !isNamed) {
			synchronized(color) {
				color.dispose();
			}
		}
		color = c;
		isNamed = named;
		isBasic = false;
		isSystem = false;
		if (preview != null) {
			preview.paint();
		}
		// Fire the color change
		if(fPropertyChangeListeners != null){
			Iterator iter = fPropertyChangeListeners.iterator();
			while(iter.hasNext()){
				((PropertyChangeListener)iter.next()).propertyChange(new PropertyChangeEvent(this,"value",c,null)); //$NON-NLS-1$
			}
		}
		
	}
	
	public void setValue(Object value) {
		if (value instanceof Color) {
			setColor((Color)value);
		}
	}
	
	public Object getValue() {
		return getColor();
	}
	
	public String getJavaInitializationString() {
		String result = "null"; //$NON-NLS-1$
		synchronized(color) {
			if (color != null) {
				if (!isNamed) {
					result = "new org.eclipse.swt.graphics.Color(org.eclipse.swt.widgets.Display.getDefault(), " + color.getRed() + ", " + color.getGreen() + ", " + color.getBlue() + ")"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
				} else {
					initializeColorConstants(control.getDisplay());
					if ( isBasic ) {
						for (int i = 0; i < basicColorValues.length; i++) {
							if(color.getRGB().equals(basicColorValues[i].getRGB())) {
								result = "org.eclipse.swt.widgets.Display.getDefault().getSystemColor(" + COLOR_PREFIX + basicColorConstants[i] + ")"; //$NON-NLS-1$ //$NON-NLS-2$
								break;
							}
						}
					} else if ( isSystem ) {
						for (int i = 0; i < systemColorValues.length; i++) {
							if(color.getRGB().equals(systemColorValues[i].getRGB())) {
								result = "org.eclipse.swt.widgets.Display.getDefault().getSystemColor(" + COLOR_PREFIX + systemColorConstants[i] + ")"; //$NON-NLS-1$ //$NON-NLS-2$
								break;
							}
						}
					}
				}
			}
		}
		return result;
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

	public String getText() {
		// Return a textual representation of the Color
		// If we don't have a color but we do have a java object instance then use this
		if(color != null){
			return color.toString();
		} else if (fExistingValue != null) {
			// Get the toString() of the target VM color
			return BeanProxyUtilities.getBeanProxy(fExistingValue).toBeanString();
		} else {
			return ""; //$NON-NLS-1$
		}
	}

	/* 
	 * Set the existing value into the editor
	 */
	public void setJavaObjectInstanceValue(IJavaObjectInstance value) {
		fExistingValue = value;
		// We have the IDE object that points to the color on the target VM
		
		
	}
}
