/*******************************************************************************
 * Copyright (c) 2001,2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: ViewPartHost.java,v $
 *  $Revision: 1.8 $  $Date: 2005-12-09 21:55:57 $ 
 */

package org.eclipse.ve.internal.java.vce.launcher.remotevm;

import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.part.*;

public class ViewPartHost {

	Shell shell;
	Map viewPartToParentComposite = new HashMap(1);
	private int fx;
	private int fy;
	public final int MIN_X = 300;
	public final int MIN_Y = 175;
	private int fTabPosition = 0;
	private boolean fTraditionalTabs = true;
	private String className = ""; //$NON-NLS-1$
	private Image image;

	public Composite[] addViewPart(WorkbenchPart aWorkbenchPart, String aTitle, String iconPath){

		Composite parent = new Composite(getWorkbenchShell(),SWT.NONE);
		parent.setLayout(new FillLayout());
		
	    CTabFolder folder = new CTabFolder(parent, SWT.BORDER);
	    folder.setUnselectedCloseVisible(false);
	    folder.setMaximizeVisible(true);
	    folder.setMinimizeVisible(true);

		folder.setTabPosition(fTabPosition);
		// The method CTabFolder.setSimple(boolean) is only available in 3.1 and higher so we must use reflection
		// to simulat the method call folder.setSimple(fTraditionalTabs);
		try{
			Method setSimpleMethod = folder.getClass().getMethod("setSimple",new Class[] {Boolean.TYPE}); //$NON-NLS-1$
			setSimpleMethod.invoke(folder,new Object[]{fTraditionalTabs ? Boolean.TRUE : Boolean.FALSE});
		} catch (Exception e){
		}		
	
	    ViewForm viewForm = new ViewForm(folder, SWT.NONE);
	    viewForm.marginHeight = 0;
	    viewForm.marginWidth = 0;
	    viewForm.verticalSpacing = 0;
	    viewForm.setBorderVisible(false);
	
	    CTabItem item = new CTabItem(folder, SWT.CLOSE);
	    item.setText(aTitle);
		folder.addCTabFolder2Listener(new CTabFolder2Adapter(){
			public void close(CTabFolderEvent event){
				event.doit = false;
			}
		});
		
	    Composite viewPartArgument = new Composite(viewForm, SWT.NONE){
			public Point computeSize(int wHint,int hHint, boolean changed){
				Point preferredSize = super.computeSize(wHint,hHint, changed);
				Point result = new Point(
						preferredSize.x > MIN_X ? preferredSize.x : MIN_X,
						preferredSize.y > MIN_Y ? preferredSize.y : MIN_Y
						);
				return result;
			}		
	    };
		viewPartArgument.setLayout(new FillLayout(SWT.HORIZONTAL));
		viewForm.setContent(viewPartArgument);
	    folder.setSelection(item);	
		// Use the passed in icon which comes from the plugin.xml on the IDE if the EditorPart or ViewPart is defined
		// in the plugin.xml with an icon= attribute
		// If none is passed in use a default depending on the hierarchy of the class		
		try{
			if(iconPath != null){
				image = new Image(getWorkbenchShell().getDisplay(),iconPath);
			} else if(aWorkbenchPart instanceof EditorPart){
				image = new Image(getWorkbenchShell().getDisplay(),ViewPartHost.class.getResourceAsStream("rcp_editor.gif")); //$NON-NLS-1$				
			} else {
				image = new Image(getWorkbenchShell().getDisplay(),ViewPartHost.class.getResourceAsStream("rcp_app.gif")); //$NON-NLS-1$
			}
		} catch (Exception exc){
			exc.printStackTrace();
		}		
		if(image != null){
			item.setImage(image);
			item.addDisposeListener(new DisposeListener(){
				public void widgetDisposed(DisposeEvent e) {
					if(image != null){
						image.dispose();
					}
				}				
			});
		}
		item.setControl(viewForm);
	
		viewPartToParentComposite.put(aWorkbenchPart,viewPartArgument);
		aWorkbenchPart.createPartControl(viewPartArgument);
		getWorkbenchShell().layout(true);
		return new Composite[] {folder,viewPartArgument};
		
	}
		
	public Composite getWorkbenchShell(){
		
		if(shell == null){
			shell = new Shell(Display.getCurrent(),SWT.SHELL_TRIM);
			shell.setBounds(fx,fy,200,200);
			shell.setText(MessageFormat.format(VCELauncherMessages.getString("BeansLauncher.FrameTitle.LaunchComponent"), new Object[] {className})); //$NON-NLS-1$
			shell.setLayout(new FillLayout());
		}
		
		return shell;
	}
	
	public void setDetails(boolean traditionalTabs, int tabPosition, String name){
		fTabPosition = tabPosition;
		fTraditionalTabs = traditionalTabs;
		className = name;
	}
}

