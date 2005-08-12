package org.eclipse.ve.sweet2;

import org.eclipse.jface.viewers.ContentViewer;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Decorations;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;

public class SpinnerEditor extends ContentViewer implements Editor {

	Spinner fSpinner;
	private IDomainProvider domainProvider;
	public static int DEFAULT_COMMIT_POLICY = COMMIT_MODIFY;	
	private IValueProvider valueProvider;	
	private Listener updateListener;
	private int updateListenerType;
	private int commitPolicy;	
	
	public SpinnerEditor(Spinner aSpinner){
		fSpinner = aSpinner;
		hookControl(fSpinner);
	}
	
	public SpinnerEditor(Composite parent, int styles){
		fSpinner = new Spinner(parent,styles);
		hookControl(fSpinner);
		fSpinner.setEnabled(false);
		setUpdatePolicy(DEFAULT_COMMIT_POLICY);
	}	
	
	public Control getControl() {
		return fSpinner;
	}

	public ISelection getSelection() {
		// TODO Auto-generated method stub
		return null;
	}

	public void refresh() {
		if(!fSpinner.isDisposed()){
			fSpinner.setSelection(((Integer)valueProvider.getValue()).intValue());
		}
	}

	public void setSelection(ISelection selection, boolean reveal) {
 
	}
	
	public void setContentProvider(IContentProvider contentProvider) {
		super.setContentProvider(contentProvider);
		valueProvider = (IValueProvider)contentProvider;
		fSpinner.setEnabled(valueProvider.canSetValue());
		setInput(valueProvider.getValue());
	}	

	public void setUpdatePolicy(int updatePolicy) {
		if(updateListener != null){
			fSpinner.removeListener(getListenerType(commitPolicy),updateListener);
		} else {
			updateListener = new Listener(){
				public void handleEvent(Event event) {
					// Push the changes down to the model domain
					Object modelValue = new Integer(fSpinner.getSelection()); 
					valueProvider.setValue(modelValue);
				}				
			};
		}
		
		commitPolicy = updatePolicy;
		fSpinner.addListener(getListenerType(commitPolicy),updateListener);
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

	public void setDomainProvider(IDomainProvider aDomainProvider) {
		domainProvider = aDomainProvider;
	}

	public IDomainProvider getDomainProvider() {
		return domainProvider;
	}

	public Spinner getSpinner() {
		return fSpinner;
	}

}
