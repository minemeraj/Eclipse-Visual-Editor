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
 *  $RCSfile: CursorPropertyEditor.java,v $
 *  $Revision: 1.7 $  $Date: 2005-08-24 23:52:56 $ 
 */

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;

public class CursorPropertyEditor implements PropertyEditor {

	private static final String BUNDLE_NAME = "org.eclipse.ve.internal.swt.cursorpropertyeditor";//$NON-NLS-1$
	private static java.util.ResourceBundle messages = java.util.ResourceBundle.getBundle(BUNDLE_NAME);
	
	private java.util.List fPropertyChangeListeners;
	private Composite control;
	private IJavaObjectInstance fCursorInstance = null;
	
	private class PreviewPanel {
		
		private Group control;
		private Button previewButton;
		
		public Control createControl(Composite parent, int style) {
			if (control == null) {
				control = new Group(parent, style);
				control.setText(messages.getString("preview")); //$NON-NLS-1$
				RowLayout rowLayout = new RowLayout ();
				rowLayout.type = SWT.VERTICAL;
				control.setLayout(rowLayout);				
				control.setToolTipText(messages.getString("hoverTooltip")); //$NON-NLS-1$

				Composite preview = new Composite (control, SWT.BORDER);
				preview.setToolTipText(messages.getString("hoverTooltip")); //$NON-NLS-1$
				preview.setBackground(preview.getDisplay().getSystemColor(SWT.COLOR_WHITE));
				preview.setSize(new Point(150,150));
				preview.setLayout(new GridLayout());
				
				previewButton = new Button(preview, SWT.PUSH);
				previewButton.setText("..."); //$NON-NLS-1$
				
				GridData data = new GridData ();
				data.horizontalAlignment = GridData.CENTER;
				data.grabExcessHorizontalSpace = true;
				data.grabExcessVerticalSpace = true;
				previewButton.setLayoutData(data);
				
				preview.pack();
				
				control.pack();
				
			}
			return control;
		}
		
		public void updatePreview(Cursor newCursor) {
			previewButton.setCursor(newCursor);
		}
	}
	
	private class NumberVerifier implements VerifyListener {
		public void verifyText(VerifyEvent arg0) {
			arg0.doit = true;
			try {
				if (arg0.text != null && arg0.text.length() > 0) {
					Integer.parseInt(arg0.text);
				}
			} catch (NumberFormatException e) {
				arg0.doit = false;
			}
		}
	}
	
	private PreviewPanel preview;
	private Cursor cursorValue;
	private int cursorConstant;
	
	private static final String CURSOR_CLASS_PREFIX = "org.eclipse.swt.graphics.Cursor("; //$NON-NLS-1$
	private static final String CURSOR_PREFIX = "org.eclipse.swt.SWT.CURSOR_"; //$NON-NLS-1$
	
	public static final String cursorNames[] = { messages.getString("appStarting"), messages.getString("arrow"), messages.getString("cross"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			messages.getString("hand"), messages.getString("help"), messages.getString("iBeam"), messages.getString("no"), messages.getString("sizeAll"), messages.getString("sizeE"), messages.getString("sizeN"), messages.getString("sizeNE"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$
			messages.getString("sizeNESW"), messages.getString("sizeNS"), messages.getString("sizeNW"),  messages.getString("sizeNWSE"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
			messages.getString("sizeS"), messages.getString("sizeSE"), messages.getString("sizeSW"), messages.getString("sizeW"), messages.getString("sizeWE"), messages.getString("upArrow"),  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
			messages.getString("wait") //$NON-NLS-1$
	};
	
	public static final String[] cursorConstants = { "APPSTARTING", "ARROW", "CROSS",   //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			"HAND", "HELP", "IBEAM", "NO", "SIZEALL", "SIZEE", "SIZEN", "SIZENE",  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$
			"SIZENESW", "SIZENS", "SIZENW", "SIZENWSE",  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
			"SIZES", "SIZESE", "SIZESW", "SIZEW",  "SIZEWE", "UPARROW",  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
			"WAIT" }; //$NON-NLS-1$
	
	private static final int[] cursorConstantValues = { SWT.CURSOR_APPSTARTING, SWT.CURSOR_ARROW, SWT.CURSOR_CROSS,  
			SWT.CURSOR_HAND, SWT.CURSOR_HELP, SWT.CURSOR_IBEAM, SWT.CURSOR_NO, SWT.CURSOR_SIZEALL, SWT.CURSOR_SIZEE, SWT.CURSOR_SIZEN, SWT.CURSOR_SIZENE, 
			SWT.CURSOR_SIZENESW, SWT.CURSOR_SIZENS, SWT.CURSOR_SIZENW, SWT.CURSOR_SIZENWSE, 
			SWT.CURSOR_SIZES, SWT.CURSOR_SIZESE, SWT.CURSOR_SIZESW, SWT.CURSOR_SIZEW,  SWT.CURSOR_SIZEWE, SWT.CURSOR_UPARROW, 
			SWT.CURSOR_WAIT };
	
	private Button standardButton;
	private Button customButton;
	
	private Combo constantsSelect;

	private Label sourceLabel;
	private Label maskLabel;
	private Text sourceText;
	private Text maskText;
	private Button sourceBrowse;
	private Button maskBrowse;
	
	private Group hotspotGroup;
	private Group canvasGroup;
	private Canvas hotspotCanvas;
	private Label xLabel;
	private Text xText;
	private Label yLabel;
	private Text yText;
	
	private Point canvasSize;
	private ImageData sourceImageData;
	private ImageData maskImageData;
	
	private ImageData sourceTransData;
	private ImageData maskTransData;
	
	private int x, y;
	private int ratio = 1;
	
	private Color red = Display.getDefault().getSystemColor(SWT.COLOR_RED);
	
	private boolean inModify = false;
	
	/* (non-Javadoc)
	 * @see PropertyEditor#createControl(org.eclipse.swt.widgets.Composite, int)
	 */
	public Control createControl(Composite parent, int style) {
		if (control == null || control.isDisposed()) {
			control = new Composite(parent, style);
			RowLayout rowLayout = new RowLayout();
			rowLayout.type = SWT.VERTICAL;
			rowLayout.fill = true;
			control.setLayout(rowLayout);
			standardButton = new Button(control, SWT.RADIO);
			standardButton.setText(messages.getString("standardCursor")); //$NON-NLS-1$
			standardButton.addSelectionListener(new SelectionListener() {
				public void widgetSelected(SelectionEvent arg0) {
					radioSwitchStandard(true);
				}
				public void widgetDefaultSelected(SelectionEvent arg0) {
					radioSwitchStandard(true);
				}
			});
			constantsSelect = new Combo(control, SWT.DROP_DOWN | SWT.READ_ONLY);
			for (int i = 0; i < cursorNames.length; i++) {
				constantsSelect.add(cursorNames[i], i);
			}
			constantsSelect.addSelectionListener(new SelectionListener() {
				public void widgetSelected(SelectionEvent e) {
					int selection = constantsSelect.getSelectionIndex();
					setCursorFromList(selection);
				}
				public void widgetDefaultSelected(SelectionEvent e) {
					int selection = constantsSelect.getSelectionIndex();
					setCursorFromList(selection);
				}
			});
			customButton = new Button(control, SWT.RADIO);
			customButton.setText(messages.getString("customCursor")); //$NON-NLS-1$
			customButton.addSelectionListener(new SelectionListener() {
				public void widgetSelected(SelectionEvent arg0) {
					radioSwitchStandard(false);
				}
				public void widgetDefaultSelected(SelectionEvent arg0) {
					radioSwitchStandard(false);
				}
			});
			makeCustomPanel(control);
			preview = new PreviewPanel();
			Control pcontrol = preview.createControl(control, SWT.NONE);
			if (cursorConstant != -1) {
				cursorValue = new Cursor(pcontrol.getDisplay(), cursorConstantValues[cursorConstant]);
				constantsSelect.select(cursorConstant);
				preview.updatePreview(cursorValue);
			} else
				constantsSelect.select(-1);
			control.pack();
			radioSwitchStandard(true);
			xText.setText("0"); //$NON-NLS-1$
			yText.setText("0"); //$NON-NLS-1$
			
		}
		
		return control;
	}
	
	private void makeCustomPanel(Composite parent) {
		Group control = new Group(parent, SWT.NONE);
		GridLayout grid = new GridLayout();
		grid.numColumns = 3;
		control.setLayout(grid);
		
		sourceLabel = new Label(control, SWT.NONE);
		sourceLabel.setText(messages.getString("sourceLabel")); //$NON-NLS-1$
		
		sourceText = new Text(control, SWT.SINGLE | SWT.BORDER);
		sourceText.setEditable(false);
		GridData gd1 = new GridData();
		gd1.horizontalAlignment = GridData.FILL;
		sourceText.setLayoutData(gd1);

		sourceBrowse = new Button(control, SWT.PUSH);
		sourceBrowse.setText(messages.getString("browseButton")); //$NON-NLS-1$
		sourceBrowse.addSelectionListener(new SelectionListener(){
			public void widgetSelected(SelectionEvent arg0) {
				doPush();
			}
			public void widgetDefaultSelected(SelectionEvent arg0) {
				doPush();
			}
			private void doPush() {
				FileDialog fd = new FileDialog(sourceBrowse.getShell(), SWT.OPEN);
				fd.setText(messages.getString("chooseSource")); //$NON-NLS-1$
				String fileName = fd.open();
				if (fileName != null) {
					try {
						sourceImageData = new ImageData(fileName);
						inModify = true;
						sourceText.setText(fileName);
						if (x >= sourceImageData.width) {
							x = 0;
							xText.setText("0"); //$NON-NLS-1$
						} if (y >= sourceImageData.height) {
							y = 0;
							yText.setText("0"); //$NON-NLS-1$
						}
						inModify = false;
						updateHotspotImage();
						updateCustomPreview();
					} catch (SWTException e) {
						if (e.code == SWT.ERROR_INVALID_IMAGE) {
							MessageBox mb = new MessageBox(sourceBrowse.getShell(), SWT.ICON_ERROR | SWT.OK);
							mb.setMessage(messages.getString("invalidFormatError")); //$NON-NLS-1$
							mb.open();
						} else if (e.code == SWT.ERROR_IO) {
							MessageBox mb = new MessageBox(sourceBrowse.getShell(), SWT.ICON_ERROR | SWT.OK);
							mb.setMessage(messages.getString("errorReading")); //$NON-NLS-1$
							mb.open();							
						} else {
							MessageBox mb = new MessageBox(sourceBrowse.getShell(), SWT.ICON_ERROR | SWT.OK);
							mb.setMessage(messages.getString("errorOpening") + e.getLocalizedMessage()); //$NON-NLS-1$
							mb.open();
						}
					}
				}
				transformImages();
			}
		});
		
		maskLabel = new Label(control, SWT.NONE);
		maskLabel.setText(messages.getString("maskImageLabel")); //$NON-NLS-1$
		
		maskText = new Text(control, SWT.SINGLE | SWT.BORDER);
		maskText.setEditable(false);
		GridData gd2 = new GridData();
		gd2.horizontalAlignment = GridData.FILL;
		maskText.setLayoutData(gd2);
		
		maskBrowse = new Button(control, SWT.PUSH);
		maskBrowse.setText(messages.getString("browseButton")); //$NON-NLS-1$
		maskBrowse.addSelectionListener(new SelectionListener(){
			public void widgetSelected(SelectionEvent arg0) {
				doPush();
			}
			public void widgetDefaultSelected(SelectionEvent arg0) {
				doPush();
			}
			private void doPush() {
				FileDialog fd = new FileDialog(sourceBrowse.getShell(), SWT.OPEN);
				fd.setText(messages.getString("chooseMask")); //$NON-NLS-1$
				String fileName = fd.open();
				if (fileName != null) {
					try {
						maskImageData = new ImageData(fileName);
						maskText.setText(fileName);
						updateHotspotImage();
						updateCustomPreview();
					} catch (SWTException e) {
						maskImageData = null;
						if (e.code == SWT.ERROR_INVALID_IMAGE) {
							MessageBox mb = new MessageBox(sourceBrowse.getShell(), SWT.ICON_ERROR | SWT.OK);
							mb.setMessage(messages.getString("invalidFormatError")); //$NON-NLS-1$
							mb.open();
						} else if (e.code == SWT.ERROR_IO) {
							MessageBox mb = new MessageBox(sourceBrowse.getShell(), SWT.ICON_ERROR | SWT.OK);
							mb.setMessage(messages.getString("errorReading")); //$NON-NLS-1$
							mb.open();							
						} else {
							MessageBox mb = new MessageBox(sourceBrowse.getShell(), SWT.ICON_ERROR | SWT.OK);
							mb.setMessage(messages.getString("errorOpening") + e.getLocalizedMessage()); //$NON-NLS-1$
							mb.open();
						}
					}
				} else {
					maskImageData = null;
				}
				transformImages();
			}
		});
		
		hotspotGroup = new Group(control, SWT.NONE);
		hotspotGroup.setText(messages.getString("cursorHotspot")); //$NON-NLS-1$
		GridLayout grid2 = new GridLayout();
		grid2.numColumns = 3;
		hotspotGroup.setLayout(grid2);
		
		GridData gd3 = new GridData();
		gd3.horizontalSpan = 3;
		hotspotGroup.setLayoutData(gd3);
		
		canvasGroup = new Group(hotspotGroup, SWT.NONE);
		canvasGroup.setLayout(new FillLayout());
		GridData gd4 = new GridData();
		gd4.verticalSpan = 2;
		canvasGroup.setLayoutData(gd4);
		
		hotspotCanvas = new Canvas(canvasGroup, SWT.NONE);
		canvasSize = new Point(64,64);
		hotspotCanvas.setSize(canvasSize);
		hotspotCanvas.setToolTipText(messages.getString("setHotspot")); //$NON-NLS-1$
		
		xLabel = new Label(hotspotGroup, SWT.NONE);
		xLabel.setText(messages.getString("xLabel")); //$NON-NLS-1$
		
		xText = new Text(hotspotGroup, SWT.BORDER);
		xText.setTextLimit(3);
		xText.setText("000"); //$NON-NLS-1$
		
		yLabel = new Label(hotspotGroup, SWT.NONE);
		yLabel.setText(messages.getString("yLabel")); //$NON-NLS-1$
		
		yText = new Text(hotspotGroup, SWT.BORDER);
		yText.setTextLimit(3);
		yText.setText("000"); //$NON-NLS-1$
		
		NumberVerifier verifier = new NumberVerifier();
		xText.addVerifyListener(verifier);
		yText.addVerifyListener(verifier);
		
		hotspotCanvas.addMouseListener(new MouseAdapter() {
			public void mouseUp(MouseEvent e) {
				int newX = e.x / ratio;
				int newY = e.y / ratio;
				if (sourceImageData != null) {
					if (newX <= sourceImageData.width &&
							newY <= sourceImageData.height) {
						inModify = true;
						x = newX;
						y = newY;
						xText.setText(String.valueOf(x));
						yText.setText(String.valueOf(y));
						inModify = false;
					}
				} else {
					inModify = true;
					x = newX;
					y = newY;
					xText.setText(String.valueOf(x));
					yText.setText(String.valueOf(y));
					inModify = false;
				}
				updateHotspotImage();
				updateCustomPreview();
			}
		});
		hotspotCanvas.addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent arg0) {
				updateHotspotImage();
			}
		});
		
		xText.addModifyListener(new ModifyListener(){
			public void modifyText(ModifyEvent arg0) {
				if (inModify) return;
				inModify = true;
				try {
					int value = Integer.parseInt(xText.getText());
					if (sourceImageData != null) {
						if (value >= 0 && value <= sourceImageData.width) {
							x = value;
							updateHotspotImage();
							updateCustomPreview();
						} else {
							xText.setText(String.valueOf(x));
						}
					}
				} catch (NumberFormatException e) {
					xText.setText(String.valueOf(x));
				}
				inModify = false;
			}
		});
		
		yText.addModifyListener(new ModifyListener(){
			public void modifyText(ModifyEvent arg0) {
				if (inModify) return;
				inModify = true;
				try {
					int value = Integer.parseInt(yText.getText());
					if (sourceImageData != null) {
						if (value >= 0 && value <= sourceImageData.height) {
							y = value;
							updateHotspotImage();
							updateCustomPreview();
						} else {
							yText.setText(String.valueOf(y));
						}
					}
				} catch (NumberFormatException e) {
					yText.setText(String.valueOf(y));
				}
				inModify = false;
			}
		});
	}
	
	private void radioSwitchStandard(boolean on) {
		constantsSelect.setEnabled(on);
		
		sourceLabel.setEnabled(!on);
		sourceText.setEnabled(!on);
		sourceBrowse.setEnabled(!on);
		
		maskLabel.setEnabled(!on);
		maskText.setEnabled(!on);
		maskBrowse.setEnabled(!on);
		
		hotspotGroup.setEnabled(!on);
		canvasGroup.setEnabled(!on);
		hotspotCanvas.setEnabled(!on);
		xLabel.setEnabled(!on);
		xText.setEnabled(!on);
		yLabel.setEnabled(!on);
		yText.setEnabled(!on);
		if (on) {
			int selection = constantsSelect.getSelectionIndex();
			setCursorFromList(selection);
		} else {
			updateCustomPreview();
			updateHotspotImage();
		}
	}
	
	private void updateHotspotImage() {
		transformImages();
		GC gc = new GC(hotspotCanvas);
		gc.setBackground(hotspotCanvas.getBackground());
		// Clear the canvas
		gc.fillRectangle(0, 0, canvasSize.x, canvasSize.y);
		ratio = 1;
		if (sourceTransData != null) {
			Image img;
			int max = (sourceTransData.height > sourceTransData.width) ? 
					sourceTransData.height : sourceTransData.width;
			ratio = canvasSize.x / max;
			ImageData scaledSource = sourceTransData.scaledTo(sourceTransData.height * ratio,
					sourceTransData.width * ratio);
			if (maskTransData != null) {
				ImageData scaledMask = maskTransData.scaledTo(maskTransData.height * ratio,
					maskTransData.width * ratio);
				img = new Image(hotspotCanvas.getDisplay(), scaledSource, scaledMask);
			} else {
				img = new Image(hotspotCanvas.getDisplay(), scaledSource);
			}
			gc.drawImage(img, 0, 0);
		}
		// Draw the hotspot indicator
		gc.setBackground(red);
		gc.fillRectangle((x * ratio)-1, (y * ratio)-1, ratio + 2, ratio + 2);
		gc.dispose();
	}
	
	private void updateCustomPreview() {
		Cursor newValue;
		if (sourceImageData != null) {
			try {
				if (maskImageData != null) {
					newValue = new Cursor(control.getDisplay(), sourceImageData, maskImageData, x, y);
				} else {
					newValue = new Cursor(control.getDisplay(), sourceImageData, x, y);
				}
				cursorConstant = -1;
				setValue(cursorValue);
				preview.updatePreview(newValue);
				if (cursorValue != null && !cursorValue.isDisposed()) {
					cursorValue.dispose();
				}
				cursorConstant = -1;
				cursorValue = newValue;
			} catch (IllegalArgumentException e) {
				
			}
		}
	}
	
	private void setCursorFromList(int index) {
		if (index < 0 || index >= cursorConstantValues.length) return;
		
		Cursor newValue = new Cursor(control.getDisplay(), cursorConstantValues[index]);
		cursorConstant = index;
		setValue(newValue);
	}
	
	/**
	 * Transform the cursor image format to the Image image format.
	 * The bit patterns for the cursor image format and the Image format do not match.
	 * This method transforms the cursor image format into the equivalent Image format.
	 * 
	 * Cursor format               Image format
	 * 
	 * image | mask | result       image | mask | result
	 * =====================       =====================
	 * 1     | 0    | T            1     | 0    | T
	 * 0     | 0    | B            0     | 0    | T
	 * 1     | 1    | S            1     | 1    | W
	 * 0     | 1    | W            0     | 1    | B
	 * 
	 * Key:
	 * T - transparent
	 * B - black
	 * W - white
	 * S - shaded (half black, half transparent)
	 * 
	 */
	private void transformImages() {
		if (sourceImageData != null) {
			sourceTransData = (ImageData)sourceImageData.clone();
		} else {
			sourceTransData = null;
		}
		if (maskImageData != null) {
			maskTransData = (ImageData)maskImageData.clone();
		} else {
			maskTransData = null;
		}
		
		if (sourceImageData != null && maskImageData != null) {
			byte[] masks = new byte[8];
			byte current = 1;
			for (int i = 0; i < masks.length; i++) {
				masks[i] = current;
				current = (byte) (current << 1);
			}
			
			byte[] src = sourceTransData.data;
			byte[] mask = maskTransData.data;
			
			byte s, m, curMask;
			boolean sIs1, mIs1;
			for (int i = 0; i < src.length; i++) {
				s = src[i];
				m = mask[i];
				for (int j = 0; j < 8; j++) {
					curMask = masks[j];
					sIs1 = (s & curMask) == curMask;
					mIs1 = (m & curMask) == curMask;
					
					if (!sIs1 && !mIs1) {
						// cursor is black, need to change to s = 0, m = 1
						m |= curMask;
					} else if (sIs1 && mIs1) {
						// cursor is shaded, need to change to s = 1, m = 0
						m &= ~curMask;
					} else if (!sIs1 && mIs1) {
						// cursor is white, need to change to s = 1, m = 1
						s |= curMask;
					}
				}
				src[i] = s;
				mask[i] = m;
			}
			sourceTransData.data = src;
			maskTransData.data = mask;
		}
	}

	/* (non-Javadoc)
	 * @see PropertyEditor#setValue(java.lang.Object)
	 */
	public void setValue(Object value) {
		if (value == null)
			cursorConstant = -1;
		if (value instanceof Cursor) {
			if (preview != null) {
				preview.updatePreview((Cursor)value);
			}
			if (cursorValue != null && !cursorValue.isDisposed()) {
				cursorValue.dispose();
			}
			cursorValue = (Cursor)value;
			// Fire the cursor change
			if(fPropertyChangeListeners != null){
				Iterator iter = fPropertyChangeListeners.iterator();
				while(iter.hasNext()){
					((PropertyChangeListener)iter.next()).propertyChange(new PropertyChangeEvent(this,"value",cursorValue,null)); //$NON-NLS-1$
				}
			}
		}
	}
	
	public void setJavaObjectInstanceValue(IJavaObjectInstance value) {
		fCursorInstance = value;
		String cursorName = CursorJavaClassLabelProvider.getText(value);
		if (!cursorName.equals("")) { //$NON-NLS-1$
			for (int i = 0; i < cursorNames.length; i++) {
				if (cursorNames[i].equals(cursorName)) {
					cursorConstant = i;
					break;
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see PropertyEditor#getValue()
	 */
	public Object getValue() {
		return cursorValue;
	}

	/* (non-Javadoc)
	 * @see PropertyEditor#getText()
	 */
	public String getText() {
		if (fCursorInstance != null) {
			return CursorJavaClassLabelProvider.getText(fCursorInstance);
		} else {
			return ""; //$NON-NLS-1$
		}
	}

	/* (non-Javadoc)
	 * @see PropertyEditor#getJavaInitializationString()
	 */
	public String getJavaInitializationString() {
		StringBuffer init = new StringBuffer("new " + CURSOR_CLASS_PREFIX + "org.eclipse.swt.widgets.Display.getDefault(), "); //$NON-NLS-1$ //$NON-NLS-2$
		if (cursorConstant != -1) {
			init.append(CURSOR_PREFIX + cursorConstants[cursorConstant] + ")"); //$NON-NLS-1$
			return init.toString();
		} else {
			if (sourceImageData != null) {
				init.append("new org.eclipse.swt.graphics.ImageData(\"" + makeBackslashSafe(sourceText.getText()) + "\"), "); //$NON-NLS-1$ //$NON-NLS-2$
				if (maskImageData != null) {
					init.append("new org.eclipse.swt.graphics.ImageData(\"" + makeBackslashSafe(maskText.getText()) + "\"), ");	  //$NON-NLS-1$ //$NON-NLS-2$
				}
				init.append("" + x + ", " + y + ")"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				return init.toString();
			}
		}
		return null;
	}
	
	/**
	 * Double any backslashes in the input string to escape the backslash charater
	 * @param input
	 * @return
	 */
	private String makeBackslashSafe(String input) {
		StringBuffer sb = new StringBuffer(input);
		for (int i = 0; i < sb.length(); i++) {
			if (sb.charAt(i) == '\\') {
				sb.insert(i, '\\');
				i++;
			}
		}
		return sb.toString();
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
}
