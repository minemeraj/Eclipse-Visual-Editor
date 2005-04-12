package org.eclipse.ve.internal.java.vce.launcher.remotevm;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.part.WorkbenchPart;

public class ViewPartHost {

	Shell shell;
	Map viewPartToParentComposite = new HashMap(1);
	private Image IMAGE;
	private int fx;
	private int fy;
	public final int MIN_X = 300;
	public final int MIN_Y = 175;

	public Composite[] addViewPart(WorkbenchPart aWorkbenchPart, String aTitle){

		Composite parent = new Composite(getWorkbenchShell(),SWT.NONE);
		parent.setLayout(new FillLayout());
		
	    CTabFolder folder = new CTabFolder(parent, SWT.BORDER);
	    folder.setUnselectedCloseVisible(false);
	    folder.setMaximizeVisible(true);
	    folder.setMinimizeVisible(true);
	
	    ViewForm viewForm = new ViewForm(folder, SWT.NONE);
	    viewForm.marginHeight = 0;
	    viewForm.marginWidth = 0;
	    viewForm.verticalSpacing = 0;
	    viewForm.setBorderVisible(false);
	
	    CLabel viewMessage = new CLabel(viewForm, SWT.NONE);
	    viewMessage.setText("View Message"); //$NON-NLS-1$
	    viewForm.setTopLeft(viewMessage);
	
	    CTabItem item = new CTabItem(folder, SWT.CLOSE);
	    item.setText(aTitle); //$NON-NLS-1$
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
		item.setImage(getDummyImage());
		item.setControl(viewForm);
	
		viewPartToParentComposite.put(aWorkbenchPart,viewPartArgument);
		aWorkbenchPart.createPartControl(viewPartArgument);
		getWorkbenchShell().layout(true);
		return new Composite[] {folder,viewPartArgument};
		
	}
	
	private Image getDummyImage(){
		
		if(IMAGE == null){
			IMAGE = new Image(null,ViewPartHost.class.getResourceAsStream("rcp_app.gif"));
		}
		return IMAGE;
	}
	
	public Composite getWorkbenchShell(){
		
		if(shell == null){
			shell = new Shell(Display.getCurrent(),SWT.SHELL_TRIM);
			shell.setBounds(fx,fy,200,200);
			shell.setText("View Part Host");
			shell.setLayout(new RowLayout());
		}
		
		return shell;
	}
}

