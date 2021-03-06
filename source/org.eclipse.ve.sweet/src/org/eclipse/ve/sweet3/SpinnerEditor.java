package org.eclipse.ve.sweet3;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.jface.util.Assert;
import org.eclipse.jface.viewers.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.*;

import org.eclipse.ve.sweet2.*;

public class SpinnerEditor extends ContentViewer implements Editor {

	Spinner fSpinner;
	public static int DEFAULT_COMMIT_POLICY = COMMIT_MODIFY;		
	private Listener updateListener;
	private int updateListenerType;
	private int commitPolicy;	
	private boolean isSettingValue;
	private IContentConsumer fContentConsumer;
	private IElementContentProvider fElementProvider;
	private Object fOutput;
	
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
		return null;
	}

	public void refresh() {
		if(!fSpinner.isDisposed() && !isSettingValue && getInput() != null){
			isSettingValue = true;
			Object val = fElementProvider.getElement(getInput());
			if (val!=null)
			    fSpinner.setSelection(((Integer)val).intValue());
			isSettingValue = false;			
		}
	}

	public void setSelection(ISelection selection, boolean reveal) {
 
	}
	
	public void setContentProvider(IContentProvider contentProvider) {
		Assert.isTrue(contentProvider instanceof IElementContentProvider);
		super.setContentProvider(contentProvider);
		fElementProvider = (IElementContentProvider)contentProvider;
	}	

	public void setUpdatePolicy(int updatePolicy) {
		if(updateListener != null){
			fSpinner.removeListener(getListenerType(commitPolicy),updateListener);
		} else {
			updateListener = new Listener(){
				public void handleEvent(Event event) {
					if(!isSettingValue){
						isSettingValue = true;
						// Push the changes down to the model domain
						Object modelValue = new Integer(fSpinner.getSelection()); 
						fContentConsumer.setValue(modelValue);
						isSettingValue = false;						
					}
				}				
			};
		}
		
		commitPolicy = updatePolicy;
		fSpinner.addListener(getListenerType(commitPolicy),updateListener);
	}
	
	protected void inputChanged(Object input, Object oldInput) {
		if(input == null){
			fSpinner.setEnabled(false);
		} else if (input instanceof IObjectDelegate){
			final IObjectDelegate binder = (IObjectDelegate) input;
			fSpinner.setEnabled(binder.getValue() != null);
			// Listen for when the value in the binder changes
			binder.addPropertyChangeListener(new PropertyChangeListener(){
				public void propertyChange(PropertyChangeEvent event) {
					fSpinner.setEnabled(binder.getValue() != null);
				}
			});
			if(fContentConsumer != null){
				fContentConsumer.ouputChanged(binder);
			}
		} else {
			fSpinner.setEnabled(true);
		}		
		refresh();
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

	public Spinner getSpinner() {
		return fSpinner;
	}

	public void setContentConsumer(IContentConsumer contentConsumer) {
		fContentConsumer = contentConsumer;
	}

	public IContentConsumer getContentConsumer() {
		return fContentConsumer;
	}

	public void setOutput(Object anOutput) {
		fOutput = anOutput;
	}

	public Object getOutput() {
		return fOutput;
	}

}
