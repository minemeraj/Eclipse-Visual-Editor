package org.eclipse.ve.internal.java.codegen.editorpart;
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
 *  $RCSfile: JVEActionStatusLineContributionItem.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 17:48:30 $ 
 */

import java.util.Timer;
import java.util.TimerTask;

import org.eclipse.jface.action.ContributionItem;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;

import org.eclipse.ve.internal.cde.core.CDEPlugin;
import org.eclipse.ve.internal.java.core.JavaVEPlugin;

/**
 * Status line contribution item to handle the reload, and current loading status.
 */
public class JVEActionStatusLineContributionItem extends ContributionItem implements IJVEActionField {

	private Label fUpLabel;
	private boolean fUpFlag = false;
	private static String MSG_SYNC_MODEL = CodegenEditorPartMessages.getString("JVE_STATUS_BAR_MSG_SYNC_CANVAS"); //$NON-NLS-1$
	private Label fDownLabel;
	private static String MSG_SYNC_SRC = CodegenEditorPartMessages.getString("JVE_STATUS_BAR_MSG_SYNC_SOURCE"); //$NON-NLS-1$
	private boolean fDownFlag = false;

	public static String MSG_PUSH_TO_PAUSE = CodegenEditorPartMessages.getString("JVE_STATUS_BAR_MSG_STOP_RT"); //$NON-NLS-1$
	public static String MSG_PUSH_TO_PLAY = CodegenEditorPartMessages.getString("JVE_STATUS_BAR_MSG_RESUME_RT"); //$NON-NLS-1$
	public static String MSG_PARSING_ERROR = CodegenEditorPartMessages.getString("JVE_STATUS_BAR_MSG_PARSE_ERROR"); //$NON-NLS-1$
	private Button fPauseButton;
	private boolean fPauseFlag = false;
	private boolean fErrorFlag = false;

	private MouseListener fPauseListener = null;

	// All editors (even those in other perspectives) can share this timer. Once created it will stay around.
	// This is ok. A simple thread that will do nothing until needed.	
	protected static Timer guiSrcTimer = new Timer(true);
	protected TimerTask fsrc2guiTick;
	protected TimerTask fgui2srcTick;

	private static ImageRegistry imageRegistry;

	// Set up the varous images	
	protected static String[] fsrc2guiImageName = { "icons/full/cview16/src2gui.gif", //$NON-NLS-1$
		"icons/full/cview16/src2gui1.gif", //$NON-NLS-1$
		"icons/full/cview16/src2gui2.gif", //$NON-NLS-1$		
		"icons/full/cview16/src2gui3.gif" //$NON-NLS-1$		
	};

	protected static String[] fgui2srcImageName = { "icons/full/cview16/gui2src.gif", //$NON-NLS-1$
		"icons/full/cview16/gui2src1.gif", //$NON-NLS-1$
		"icons/full/cview16/gui2src2.gif", //$NON-NLS-1$
		"icons/full/cview16/gui2src3.gif" //$NON-NLS-1$				
	};

	protected static String fPauseImageName[] = { "icons/full/cview16/pause.gif", //$NON-NLS-1$
		"icons/full/cview16/play.gif", //$NON-NLS-1$			
		"icons/full/cview16/error_obj.gif" //$NON-NLS-1$			
	};

	protected static Image getImage(String imageURL) {
		if (imageRegistry == null) {
			imageRegistry = new ImageRegistry();
			for (int i = 0; i < fsrc2guiImageName.length; i++) {
				imageRegistry.put(
					fsrc2guiImageName[i],
					CDEPlugin.getImageDescriptorFromPlugin(JavaVEPlugin.getPlugin(), fsrc2guiImageName[i]));
			}
			for (int i = 0; i < fgui2srcImageName.length; i++) {
				imageRegistry.put(
					fgui2srcImageName[i],
					CDEPlugin.getImageDescriptorFromPlugin(JavaVEPlugin.getPlugin(), fgui2srcImageName[i]));
			}
			for (int i = 0; i < fPauseImageName.length; i++) {
				imageRegistry.put(fPauseImageName[i], CDEPlugin.getImageDescriptorFromPlugin(JavaVEPlugin.getPlugin(), fPauseImageName[i]));
			}
		}
		return imageRegistry.get(imageURL);
	}

	/**
	 * Constructor for JVEStatusLineContributionItem.
	 * @param id
	 */
	public JVEActionStatusLineContributionItem(String id) {
		super(id);
	}

	/*
	 * @see IContributionItem#fill(Composite)
	 */
	public synchronized void fill(Composite parent) {
		Composite Root = new Composite(parent, SWT.SHADOW_IN);
		Root.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				fUpLabel = fDownLabel = null;
				fPauseButton = null;
				if (fgui2srcTick != null) {
					fgui2srcTick.cancel();
					fgui2srcTick = null;
				}
				if (fsrc2guiTick != null) {
					fsrc2guiTick.cancel();
					fsrc2guiTick = null;
				}
			}
		});

		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 3;
		gridLayout.marginHeight = 0;
		Root.setLayout(gridLayout);

		fUpLabel = new Label(Root, SWT.CENTER | SWT.SHADOW_IN);
		GridData data = new GridData(GridData.VERTICAL_ALIGN_CENTER | GridData.HORIZONTAL_ALIGN_CENTER);
		fUpLabel.setLayoutData(data);

		fPauseButton = new Button(Root, SWT.CENTER | SWT.TOGGLE);
		data = new GridData(GridData.VERTICAL_ALIGN_CENTER | GridData.HORIZONTAL_ALIGN_CENTER);
		fPauseButton.setLayoutData(data);
		if (fPauseListener != null)
			fPauseButton.addMouseListener(fPauseListener);

		fDownLabel = new Label(Root, SWT.CENTER | SWT.SHADOW_IN);
		data = new GridData(GridData.VERTICAL_ALIGN_CENTER | GridData.HORIZONTAL_ALIGN_CENTER);
		fDownLabel.setLayoutData(data);

		fDownLabel.setToolTipText(MSG_SYNC_SRC);
		fUpLabel.setToolTipText(MSG_SYNC_MODEL);

		setModel2Src(fDownFlag);
		setSrc2Model(fUpFlag);
		if (fErrorFlag)
			setError(fErrorFlag);
		else
			setPause(fPauseFlag);
	}

	public void setPauseListener(MouseListener listener) {
		if (listener == fPauseListener)
			return;
		if (fPauseButton != null && !fPauseButton.isDisposed()) {
			if (fPauseListener != null)
				fPauseButton.removeMouseListener(fPauseListener);
			fPauseListener = listener;
			if (fPauseListener != null)
				fPauseButton.addMouseListener(fPauseListener);
		} else
			fPauseListener = listener;

		if (fPauseListener == null) {
			if (fgui2srcTick != null) {
				fgui2srcTick.cancel();
				fgui2srcTick = null;
			}
			if (fsrc2guiTick != null) {
				fsrc2guiTick.cancel();
				fsrc2guiTick = null;
			}
		}
	}

	/**
	 * @see org.eclipse.ve.internal.java.codegen.editorpart.IJVEActionField#setModel2Src(boolean)
	 */
	public synchronized void setModel2Src(boolean flag) {
		fDownFlag = flag;
		if (fDownLabel == null || fDownLabel.isDisposed())
			return;
		if (flag) {
			if (fgui2srcTick == null) {
				// For showing that the update is in place we animate a series of icons to give the 
				// appearance of movement along the arrow.  Because SWT doesn't automatically animate an
				// animated GIF for you we must do this by hand with a  tick thread that repeats every 300ms				
				final Display display = fDownLabel.getDisplay();
				fgui2srcTick = new TimerTask() {
					protected int imageNumber = 1;
					public void run() {
						display.syncExec(new Runnable() {
							public void run() {
								if (fDownLabel != null && !fDownLabel.isDisposed())
									fDownLabel.setImage(getImage(fgui2srcImageName[imageNumber]));
								else
									cancelgui2src(); // Label is disposed, so cancel the task.
							}
						});
						imageNumber++;
						if (imageNumber > 3)
							imageNumber = 1;
					}
				};
				guiSrcTimer.schedule(fgui2srcTick, 0, 250);
			}
		} else {
			cancelgui2src();
		}
	}

	private void cancelgui2src() {
		// We need to revert to the default image and also stop the timer task
		if (fgui2srcTick != null) {
			fgui2srcTick.cancel();
			fgui2srcTick = null;
		}
		// Set the image if it isn't already the default image
		if (fDownLabel != null && getImage(fgui2srcImageName[0]) != fDownLabel.getImage()) {
			fDownLabel.setImage(getImage(fgui2srcImageName[0]));
		}
	}

	/**
	 * @see org.eclipse.ve.internal.java.codegen.editorpart.IJVEActionField#setPause(boolean)
	 */
	public void setPause(boolean flag) {
		fPauseFlag = flag;
		if (fPauseButton == null || fPauseButton.isDisposed())
			return;
		if (flag) {
			if (!fPauseButton.getSelection())
				fPauseButton.setSelection(true);
			if (getImage(fPauseImageName[1]) != fPauseButton.getImage())
				fPauseButton.setImage(getImage(fPauseImageName[1]));
			fPauseButton.setToolTipText(MSG_PUSH_TO_PLAY);
		} else {
			if (fPauseButton.getSelection())
				fPauseButton.setSelection(false);
			if (getImage(fPauseImageName[0]) != fPauseButton.getImage())
				fPauseButton.setImage(getImage(fPauseImageName[0]));
			fPauseButton.setToolTipText(MSG_PUSH_TO_PAUSE);
		}
	}
	/**
	 * @see org.eclipse.ve.internal.java.codegen.editorpart.IJVEActionField#setSrc2Model(boolean)
	 */
	public synchronized void setSrc2Model(boolean flag) {
		fUpFlag = flag;
		if (fUpLabel == null || fUpLabel.isDisposed())
			return;
		if (flag) {
			if (fsrc2guiTick == null) {
				// For showing that the update is in place we animate a series of icons to give the 
				// appearance of movement along the arrow.  Because SWT doesn't automatically animate an
				// animated GIF for you we must do this by hand with a  tick thread that repeats every 300ms
				final Display display = fUpLabel.getDisplay();
				fsrc2guiTick = new TimerTask() {
					protected int imageNumber = 1;
					public void run() {
						display.syncExec(new Runnable() {
							public void run() {
								// check to see if it has been canceled (tell by the variable going null)
								// If it has been canceled we don't want to go ahead and set the image.
								// It would leave it in the wrong state.
								// It is ok to just check the variable because it is only changed within
								// the UI thread, either via setSrc2Model or through the cancel call below.
								if (fsrc2guiTick != null)
									if (fUpLabel != null && !fUpLabel.isDisposed())
										fUpLabel.setImage(getImage(fsrc2guiImageName[imageNumber]));
									else
										cancelsrc2gui(); // Label is disposed, so cancel the task.
							}
						});
						imageNumber++;
						if (imageNumber > 3)
							imageNumber = 1;
					}
				};
				guiSrcTimer.schedule(fsrc2guiTick, 0, 250);
			}
		} else {
			cancelsrc2gui();
		}
	}

	private void cancelsrc2gui() {
		// We need to revert to the default image and also stop the timer
		if (fsrc2guiTick != null) {
			fsrc2guiTick.cancel();
			fsrc2guiTick = null;
		}
		// Set the image if it isn't already the default image
		if (!fUpLabel.isDisposed() && getImage(fsrc2guiImageName[0]) != fUpLabel.getImage()) {
			fUpLabel.setImage(getImage(fsrc2guiImageName[0]));
		}
	}
	/**
	 * @see org.eclipse.ve.internal.java.codegen.editorpart.IJVEActionField#setError(boolean)
	 */
	public void setError(boolean flag) {
		fErrorFlag = flag;
		if (fPauseButton == null)
			return;
		if (flag) {
			if (!fPauseButton.getSelection())
				fPauseButton.setSelection(true);
			if (getImage(fPauseImageName[2]) != fPauseButton.getImage())
				fPauseButton.setImage(getImage(fPauseImageName[2]));
			fPauseButton.setToolTipText(MSG_PARSING_ERROR);
		} else {
			if (fPauseButton.getSelection())
				fPauseButton.setSelection(false);
			if (getImage(fPauseImageName[0]) != fPauseButton.getImage())
				fPauseButton.setImage(getImage(fPauseImageName[0]));
			fPauseButton.setToolTipText(MSG_PUSH_TO_PAUSE);
		}

	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.texteditor.IStatusField#setImage(org.eclipse.swt.graphics.Image)
	 */
	public void setImage(Image image) {
		// NO-OP - Here only because needs to be IStatusField implementer
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.texteditor.IStatusField#setText(java.lang.String)
	 */
	public void setText(String text) {
		// NO-OP - Here only because needs to be IStatusField implementerOP

	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.editorpart.IJVEActionField#unsetPauseListener(org.eclipse.swt.events.MouseListener)
	 */
	public void unsetPauseListener(MouseListener listener) {
		if (listener != fPauseListener)
			return;

		if (fPauseButton != null && !fPauseButton.isDisposed()) {
			fPauseButton.removeMouseListener(fPauseListener);
			if (fgui2srcTick != null) {
				fgui2srcTick.cancel();
				fgui2srcTick = null;
			}
			if (fsrc2guiTick != null) {
				fsrc2guiTick.cancel();
				fsrc2guiTick = null;
			}
		}
		fPauseListener = null;
	}

}
