package org.eclipse.ve.internal.jface.targetvm;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.part.WorkbenchPart;

public class ViewPartHost {

	static Shell shell;
	static Map viewPartToParentComposite = new HashMap(1);
	private static Image IMAGE;

public static Composite[] addViewPart(WorkbenchPart aWorkbenchPart){

	Composite parent = new Composite(getWorkbenchShell(),SWT.NONE){
		public Point computeSize(int wHint,int hHint){
			Point preferredSize = super.computeSize(wHint,hHint);
			Point result = new Point(
					preferredSize.x > 200 ? preferredSize.x : 200,
					preferredSize.y > 200 ? preferredSize.y : 200
					);
			return result;
		}
	};

	parent.setLayout(new FillLayout());
	
    CTabFolder folder = new CTabFolder(parent, SWT.BORDER);
    folder.setUnselectedCloseVisible(false);
    folder.setEnabled(false);
    folder.setMaximizeVisible(true);
    folder.setMinimizeVisible(true);

    ViewForm viewForm = new ViewForm(folder, SWT.NONE);
    viewForm.marginHeight = 0;
    viewForm.marginWidth = 0;
    viewForm.verticalSpacing = 0;
    viewForm.setBorderVisible(false);
    ToolBar toolBar = new ToolBar(viewForm, SWT.FLAT | SWT.WRAP);
    ToolItem toolItem = new ToolItem(toolBar, SWT.PUSH);

    viewForm.setTopRight(toolBar);

    CLabel viewMessage = new CLabel(viewForm, SWT.NONE);
    viewMessage.setText("View Message"); //$NON-NLS-1$
    viewForm.setTopLeft(viewMessage);

    CTabItem item = new CTabItem(folder, SWT.CLOSE);
    item.setText("View Part Name"); //$NON-NLS-1$
    Composite viewPartArgument = new Composite(viewForm, SWT.NONE);
	viewPartArgument.setLayout(new GridLayout(1,false));
	viewForm.setContent(viewPartArgument);
    folder.setSelection(item);	
	item.setImage(getDummyImage());
	item.setControl(viewForm);

	viewPartToParentComposite.put(aWorkbenchPart,viewPartArgument);
	aWorkbenchPart.createPartControl(viewPartArgument);
	getWorkbenchShell().layout(true);
	return new Composite[] {folder,viewPartArgument};
		
}

public static Image getDummyImage(){
	if(IMAGE == null){
		IMAGE = new Image(null,ViewPartHost.class.getResourceAsStream("rcp_app.gif"));
	}
	return IMAGE;
}

public static void removeViewPart(WorkbenchPart aWorkbenchPart){
	
	Composite c = (Composite)viewPartToParentComposite.get(aWorkbenchPart);
	c.dispose();
	viewPartToParentComposite.remove(aWorkbenchPart);	
	
}

public static void layoutViewPart(WorkbenchPart aWorkbenchPart){
	
	Composite c = (Composite) viewPartToParentComposite.get(aWorkbenchPart);
	c.pack();
	getWorkbenchShell().layout(true);
	
}

public static void main(String[] args) {
	
	TestViewPartTest testViewPart = new TestViewPartTest(); 
	addViewPart(testViewPart);
	layoutViewPart(testViewPart);
	while(!shell.isDisposed()){
		if(!shell.getDisplay().readAndDispatch())shell.getDisplay().sleep();
	}
	
}

protected static Composite getWorkbenchShell(){
	
	if(shell == null){
		shell = new Shell();
		shell.setBounds(5,5,200,200);
		shell.setText("View part host");
		shell.setLayout(new RowLayout());
		shell.open();
	}
	
	return shell;
}
}

