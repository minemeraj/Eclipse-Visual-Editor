package org.eclipse.ve.sweet2;

import org.eclipse.jface.viewers.ContentViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;

public class TextViewer extends ContentViewer {
	
	private Text text;
	
	public TextViewer(Composite parent, int styles){
		text = new Text(parent,styles);
		
	}
	
	public Text getText(){
		return text;
	}

	public Control getControl() {
		return text;
	}

	public ISelection getSelection() {
		return null;
	}

	public void refresh() {
		
	}

	public void setSelection(ISelection selection, boolean reveal) {

	}

}
