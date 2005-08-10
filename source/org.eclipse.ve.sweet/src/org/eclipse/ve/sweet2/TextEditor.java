package org.eclipse.ve.sweet2;

import org.eclipse.jface.viewers.ContentViewer;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

public class TextEditor extends ContentViewer implements Editor {
	
	private Text text;
	private IPropertyProvider propertyProvider;
	public static int DEFAULT_COMMIT_POLICY = COMMIT_FOCUS;
	private Listener updateListener;
	private int updateListenerType;
	private int commitPolicy = DEFAULT_COMMIT_POLICY;
	
	public TextEditor(Composite parent, int styles){
		text = new Text(parent,styles);
		text.setEnabled(false);
		setUpdatePolicy(DEFAULT_COMMIT_POLICY);
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
	
	public void setContentProvider(IContentProvider contentProvider) {
		super.setContentProvider(contentProvider);
		propertyProvider = (IPropertyProvider)contentProvider;
		Object newSource = propertyProvider.getSource();
		getText().setEnabled(newSource != null);
		setInput(newSource);
	}
	
	public void setUpdatePolicy(int aCommitPolicy){
				
		if(updateListener != null){
			text.removeListener(getListenerType(commitPolicy),updateListener);
		} else {
			updateListener = new Listener(){
				public void handleEvent(Event event) {
					propertyProvider.refreshDomain();
				}				
			};
		}
		
		commitPolicy = aCommitPolicy;
		text.addListener(getListenerType(commitPolicy),updateListener);
		
	}
	private int getListenerType(int aCommitPolicy){
		switch (aCommitPolicy) {
		case COMMIT_MODIFY:
			return SWT.Modify;
		case COMMIT_FOCUS:
			return SWT.FocusOut;
		case COMMIT_EXPLICIT:
			return SWT.None;
		default:
			return DEFAULT_COMMIT_POLICY;
		}		
	}
}