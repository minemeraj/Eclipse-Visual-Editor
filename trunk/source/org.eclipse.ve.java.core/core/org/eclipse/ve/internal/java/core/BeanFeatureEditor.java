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
package org.eclipse.ve.internal.java.core;
/*
 *  $RCSfile: BeanFeatureEditor.java,v $
 *  $Revision: 1.11 $  $Date: 2006-02-25 23:32:06 $ 
 */

import java.text.MessageFormat;
import java.util.logging.Level;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.jface.viewers.*;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;

import org.eclipse.jem.internal.instantiation.JavaAllocation;
import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.jem.internal.proxy.core.*;
import org.eclipse.jem.java.JavaDataType;
import org.eclipse.jem.java.JavaHelpers;

import org.eclipse.ve.internal.cde.core.EditDomain;

import org.eclipse.ve.internal.propertysheet.INeedData;
import org.eclipse.ve.internal.propertysheet.ISourced;
/**
 * This class is a Cell editor that wraps a Java beans' property editor
 * The java bean property editor is proxied via the interface IBeanPropertyEditorProxy.
 * and created by the current BeanPropertyEditorProxyFactory.  This indirection allows support
 * for a property editor running on a remote VM.
 * 
 * This wrappers the real cell editor. Since we don't know what kind of cell editor to put up until
 * we become activated, we can't pre-create the correct kind. The kind is determined by the bean property editor.
 * 
 * The actual cell editor cannot be changed while the editor is active. This is because there are some things that
 * are holding onto the actual SWT control while it is active. These somethings are outside of this editor and its control.
 * 
 * For this to work the following assumptions must be made:
 *   1) The sources can't change while the editor is active, they can be set, but must of the same sources.
 *      If they became different sources while the editor is active, we wouldn't be able to change the type
 *      of editor. The sources could cause the bean property editor to decide upon a different type of editor.
 *   2) The actual cell editor must implement the BeanFeatureEditor.IWrappedCellEditor interface. This is so that
 *      the cell editor can be told that there is a different value in the bean proxy editor.
 * 
 * The life cycle is:
 *   1) Upon activation, the bean property editor and the actual cell editor will be created.
 *   2) Whenever the value of this cell editor is changed, the bean property editor and the actual cell editor will be notified
 *      so that it can be refreshed.
 *   3) Upon deactivation, the actual cell editor will be deactivated.
 *   4) Upon dispose, the actual cell editor will be disposed.
 * Creation date: (2/2/00 4:46:00 PM)
 * @author: Richard Lee Kulp
 */
public class BeanFeatureEditor
	extends CellEditor
	implements ISourced, IExecutableExtension, INeedData, IJavaCellEditor, IJavaCellEditor2 {
	
	/**
	 * Interface that the actual cell editor must implement.
	 */	
	public interface IWrappedCellEditor {
		/**
		 * The text will be the new value as text. The editor can use this if it wishes,
		 * or it can go to the bean property editor (if it has it) to retrieve the value.
		 */
		public void newValue(String text);
	}
	
	// The actual cell editor that is used. BeanEditors aren't sure
	// of the correct cell editor to use until value has been set,
	// so we will use a specific cell editor at that time.
	protected JavaHelpers fFeatureType;
	protected EditDomain fEditDomain;
	private IWrappedCellEditor fActualCellEditor;
	protected PropertyEditorBeanProxyWrapper fPropertyEditorWrapperProxy;
	protected IJavaObjectInstance source;
	protected boolean rebuildActualCellEditor = true;
	private IJavaInstance fValue;
	protected Composite fComposite;
	protected IBeanProxy fLastKnownBeanProxy = null;
	private boolean fGotANull;
	// The actual java.beans.PropertyEditor class can be known and given to us
	// It is either stored as a string ( passed into the initialization Data )
	// or else as the JavaClass passed into the constructor
	// If neither are given then it calculated by looking at the IPropertyDescriptor
	// and looking at its decorators
	protected String fJavaBeansPropertyEditorClassName;

	// A cell editor listener that will handle a setAsText for the strings from
	// the actual cell editor.
	// The text cell editor nevers has an invalid input because it has no validators sent to it.
	// We will run the validators locally within the setAsText() method.
	protected class SetAsTextEditorListener implements ICellEditorListener {
		public void applyEditorValue() {
			// Treat as editValueChanged because editValueChanged is not always sent before finish.
			setAsText((String) getActualCellEditor().getValue());
			fireApplyEditorValue();
		}
		public void editorValueChanged(boolean oldValidState, boolean newValidState) {
			boolean oldVState = isValueValid();
			setAsText((String) getActualCellEditor().getValue());
			fireEditorValueChanged(oldVState, isValueValid());
		};
		public void cancelEditor() {
			fireCancelEditor();
		}
	};

	/**
	 * This one is called when the property editor class name will be sent in on the
	 * initialization data. This is the case when we have a default property editor
	 * for a type instead of given by the feature itself. In those cases there
	 * will be an BasePropertyDecorator on the type with the property editor classname
	 * as the initialization data for that type.
	 */	
	public BeanFeatureEditor(
		JavaHelpers aFeatureType,
		Composite aComposite) {	
		super(aComposite);
		fComposite = aComposite;
		fFeatureType = aFeatureType;
	}
		
	/**
	 * The first argument is the type of the feature being edited
	 * for a bean property it is the type of the feature
	 * for a java parameter on a method it is the type of the parameter.
	 */
	public BeanFeatureEditor(
		JavaHelpers aFeatureType,
		Composite aComposite,
		String aPropertyEditorClassName) {
		this(aFeatureType, aComposite);
		fJavaBeansPropertyEditorClassName = aPropertyEditorClassName;
	}
	/**
	 For the actualCellEditor we return four types
	 1	-	A label with a launch button if our property editor supports custom editing
	 2	-	A combo box if our property editor has tags that we can use to do combo style editing
	 3	-	Otherwise a text box 
	 */
	protected void calculateActualCellEditor() {
		rebuildActualCellEditor = false;

		getBeanPropertyEditorProxy(); // Create it if necessary.

		if (fPropertyEditorWrapperProxy.supportsCustomEditor()) // 	2
			createDialogCellEditor();
		else if (fPropertyEditorWrapperProxy.getTags() != null) // 	3
			createComboCellEditor();
		else
			createTextCellEditor(); //	4
	}
	/**
	 * Get the type of the attribute and get the property editor for this type
	 */
	protected void calculateJavaBeanPropertyEditor() {

		// We should of been given a property editor class name, if not, we return a wrapper which does nothing.
		if (fJavaBeansPropertyEditorClassName != null) {
			IConstructorProxy ctor = null;
			IBeanTypeProxy fPropertyEditorTypeProxy =
				getProxyFactoryRegistry().getBeanTypeProxyFactory().getBeanTypeProxy(
					fJavaBeansPropertyEditorClassName);
			try {
				// if there is an ISourced object, see if the property editor has a ctor that takes an object and pass it in.
				if (source != null) {
					ctor = fPropertyEditorTypeProxy.getConstructorProxy(new String[] { "java.lang.Object" }); //$NON-NLS-1$
					if (ctor != null) {
						// got a ctor? let's instantiate the property editor
						fPropertyEditorWrapperProxy =
							new PropertyEditorBeanProxyWrapper(
								ctor.newInstance(new IBeanProxy[] { BeanProxyUtilities.getBeanProxy(source, JavaEditDomainHelper.getResourceSet(fEditDomain))}));
					} else {
						// just instantiate with a null ctor
						fPropertyEditorWrapperProxy =
							new PropertyEditorBeanProxyWrapper(fPropertyEditorTypeProxy.newInstance());
					}
				} else {
					// no ISourced object... just instantiate with a null ctor
					fPropertyEditorWrapperProxy =
						new PropertyEditorBeanProxyWrapper(fPropertyEditorTypeProxy.newInstance());
				}
			} catch (ThrowableProxy exc) {
				// The ctor that takes an object may have failed because we gave it an object it didn't expect,
				// so let's try with the default ctor. But don't try again if the default ctor is the one that threw the exception.
				if (ctor != null) {
					try {
						fPropertyEditorWrapperProxy =
							new PropertyEditorBeanProxyWrapper(fPropertyEditorTypeProxy.newInstance());
					} catch (ThrowableProxy e) {
						// Still failed, so log it.
						JavaVEPlugin.log(e);
					}
				} else {
					// It failed and we had used the default ctor, so log it.
					JavaVEPlugin.log(exc);
				}
					
			}
		}
		
		if (fPropertyEditorWrapperProxy == null) {
			// We have no descriptor or we could not create a property editor.
			// Create a null one and a label will be created for us
			fPropertyEditorWrapperProxy = new PropertyEditorBeanProxyWrapper(null);
		}
	}
	/**
	 * Return the proxy factory registry of our source
	 */
	protected ProxyFactoryRegistry getProxyFactoryRegistry() {
		return JavaEditDomainHelper.getBeanProxyDomain(fEditDomain).getProxyFactoryRegistry();
	}
	public void setData(Object data) {
		fEditDomain = (EditDomain) data;
	}
	/**
	 * createLabelCellEditor
	 */
	protected void createComboCellEditor() {
		setProxyValue(fValue); // Put the value into the property editor, see if valid.
		final String[] fTags = fPropertyEditorWrapperProxy.getTags();
		BeanFeatureComboBoxCellEditor cellEditor = new BeanFeatureComboBoxCellEditor(fComposite, fTags);
		cellEditor.newValue(getAsText());
		
		// TODO	"Need to deal with <null> and stuff better - perhaps add it automatically to the list for IJavaObjectInstance
		//	cellEditor.setErrorMessage(errMsg);
		// The cell editor listener for text expects the value to be a String
		// it will be an integer so we must do the conversion using the tags array
		// There are no validators on actual cell editor, so everything will always be valid
		// from that side. We need to run the validators locally from ourself.
		// setAsText(text) will do the appropriate validation.
		cellEditor.addListener(new ICellEditorListener() {
			public void applyEditorValue() {
				String textValue = getComboTextValue();
				setAsText(textValue);
				markDirty();
				fireApplyEditorValue();
			}
			public void editorValueChanged(boolean oldValidState, boolean newValidState) {
				// This is actually never sent by the combobox cell editor, but to be on the
				// safe side, if they did in the future.
				
				// The old/new valid start from the notifier is not of any value to us
				// since it doesn't do validation. We will do the validation here.
				boolean oldVState = isValueValid();
				setAsText(getComboTextValue());
				fireEditorValueChanged(oldVState, isValueValid());
			};
			public void cancelEditor() {
				fireCancelEditor();
			}
			protected String getComboTextValue() {
				return (String) getActualCellEditor().getValue();
			}
		});
		fActualCellEditor = cellEditor;
	}
	/**
	 * createLabelCellEditor
	 */
	protected void createDialogCellEditor() {
		setProxyValue(fValue); // Put the value into the property editor, see if valid.
		final BeanFeatureDialogCellEditor cellEditor =
			new BeanFeatureDialogCellEditor(fComposite, fPropertyEditorWrapperProxy, getDisplayName());
		cellEditor.newValue(getAsText());
		// There are no validators on actual cell editor, so everything will always be valid
		// from that side. We need to run the validators locally from ourself.	
		// getProxyValueFromPropertyEditor() will run the validators for us.
		cellEditor.addListener(new ICellEditorListener() {
			public void applyEditorValue() {
				getProxyValueFromPropertyEditor();
				// A lot of dialog editors return the value from the dialog editor but do not use
				// this for their getAsText - they hold onto a separate value that they cache
				// To allow for this "bug" do a set value to ensure that the property editor
				// value that is set is the same as the value it gave us so that when
				// getAsText fires we get the correct up to date text
				fPropertyEditorWrapperProxy.setValue(fPropertyEditorWrapperProxy.getValue());
				cellEditor.newValue(getAsText()); // Update the label in the DialogCellEditor
				markDirty();
				fireApplyEditorValue();
			}
			public void cancelEditor() {
				fireCancelEditor();
			}
			public void editorValueChanged(boolean oldValidState, boolean newValidState) {
				// Right now the dialog cell editor never fires this but in future we may want
				// to have a mode where the cell editor can remain open and fire values through
				// an apply button so we will leave this code here
				boolean oldVState = isValueValid();
				getProxyValueFromPropertyEditor();
				fireEditorValueChanged(oldVState, isValueValid());
			};
		});
		fActualCellEditor = cellEditor;
	}
	/**
	 * Return a name for the title of the dialog cell editor
	 * take this from the descriptor if there is one
	 */
	protected String getDisplayName() {
		// TODO	"Need some way of getting the name of the property for a bean feature
		return ""; //$NON-NLS-1$
	}
	/**
	 * createTextCellEditor
	 */
	protected void createTextCellEditor() {
		setProxyValue(fValue); // Put the value into the property editor, see if valid.
		BeanFeatureTextCellEditor cellEditor = new BeanFeatureTextCellEditor(fComposite);
		cellEditor.newValue(getAsText());
		cellEditor.addListener(new SetAsTextEditorListener());
		fActualCellEditor = cellEditor;
	}
	
	protected void disposeOfActualCellEditor() {
		if (fActualCellEditor != null) {
			((CellEditor) fActualCellEditor).dispose();
			fActualCellEditor = null;			
		}
		if (fPropertyEditorWrapperProxy != null) {
			fPropertyEditorWrapperProxy.dispose();
			fPropertyEditorWrapperProxy = null;
		}
	}
	/**
	 * Forward this to the actual cell editor if we have one
	 */
	public void dispose() {
		disposeOfActualCellEditor();
	}
	/**
	 * Forward this to the actual cell editor if we have one
	 */
	public void deactivate() {
		if (fActualCellEditor != null) {
			((CellEditor) fActualCellEditor).deactivate();
		}
	}

	/*
	 * This method is called during the constructor which makes it even harder for us to 
	 * subclass.  We can't really create a control until after the constructor so for now
	 * just return null and when we are really asked for our control via the getControl()
	 * method we return the wrapped control.
	 */
	protected Control createControl(Composite aComposite) {
		return null;
	}
	
	protected CellEditor getActualCellEditor() {
		if (fActualCellEditor == null) {
			calculateActualCellEditor();
		}
		return (CellEditor) fActualCellEditor;
	}
	/**
	 * Forward this to the java property editor
	 * If the java property editor returns a null, then
	 * set that we got a null and return the null display text instead.
	 */
	protected String getAsText() {
		String res = "";	// All of the CDE celleditors display "" for null, so that is the initial text. //$NON-NLS-1$	
		fGotANull = fValue == null;
		if (!fGotANull) {		
			if (fPropertyEditorWrapperProxy != null) {
				try {
					res = fPropertyEditorWrapperProxy.getAsText();
				} catch (Throwable exc) {
					JavaVEPlugin.log(exc, Level.WARNING);
				}
			} else {
				// We don't have a property editor, so just get the bean string.
				if (fLastKnownBeanProxy != null)
					res = fLastKnownBeanProxy.toBeanString();
			}
			
			if (res == null)
				res = "";	// In case it returns a null. It shouldn't. //$NON-NLS-1$
		}
		return res;
	}
	/**
	 * Get the bean property editor proxy, create if necessary.
	 */
	protected PropertyEditorBeanProxyWrapper getBeanPropertyEditorProxy() {
		if (fPropertyEditorWrapperProxy == null) {
			calculateJavaBeanPropertyEditor();
		}
		return fPropertyEditorWrapperProxy;
	}
	/**
	 * getJavaInitializationString: Get the init string for
	 * bean in the value. This should only be called if
	 * this editor is being used to get the init string, and
	 * not if this editor is in the property sheet. 
	 */
	public String getJavaInitializationString() {
		getBeanPropertyEditorProxy(); // Create it if necessary
		setProxyValue(fValue); // Now load the value into the editor

		return isValueValid() ? fPropertyEditorWrapperProxy.getJavaInitializationString() : null;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.core.IJavaCellEditor2#getJavaAllocation()
	 */
	public JavaAllocation getJavaAllocation() {
		return BeanPropertyDescriptorAdapter.createAllocation(getJavaInitializationString(), fEditDomain);
	}

	/**
	 * getValue method comment.
	 */
	public Object doGetValue() {
		if (isValueValid())
			return fValue;
		else
			return null;
	}
	/**
	 * The control is the actual cell editor
	 */
	public Control getControl() {
		if (getActualCellEditor() != null) {
			return getActualCellEditor().getControl();
		} else {
			return null;
		}
	}

	/**
	 * Get the current proxy value and set it to the current value.
	 * Called whenever need to sync up witht the proxy editor, ie. it was
	 * changed but not through setAsText or setValue. For example, by the
	 * custom dialog.
	 * Creation date: (2/9/00 6:05:18 PM)
	 */
	protected void getProxyValueFromPropertyEditor() {
		IBeanProxy newProxy = fPropertyEditorWrapperProxy.getValue();
		// Now create the new value if a new proxy
		// This code needs to be special because our proxy might be an Integer ( because getValue() on
		// java.beans.PropertyEditor returns Object and cannot return a primitive ) whereas we must create
		// a primitive type
		// However we cannot just blindly use the type of the feature because in other cases the class of the 
		// proxy is more generalized, e.g. Color returning SystemColor and we need to use the class
		// of the more generalized instance
		if (newProxy != fLastKnownBeanProxy) {
			// See whether the type is primitive
			IJavaInstance bean = null;
			if (getFeatureType().isPrimitive()) {
				bean =
					BeanProxyUtilities.wrapperBeanProxyAsPrimitive(
						newProxy,
						(JavaDataType) getFeatureType(),
						JavaEditDomainHelper.getResourceSet(fEditDomain),
						BeanPropertyDescriptorAdapter.createAllocation(fPropertyEditorWrapperProxy.getJavaInitializationString(), fEditDomain));
			} else {
				bean =
					BeanProxyUtilities.wrapperBeanProxy(
						newProxy,
						JavaEditDomainHelper.getResourceSet(fEditDomain),
						true,
						BeanPropertyDescriptorAdapter.createAllocation(fPropertyEditorWrapperProxy.getJavaInitializationString(), fEditDomain));
			}
			fValue = bean;
			fLastKnownBeanProxy = newProxy;
			setValueValid(isCorrect(fValue));
		}
	}
	protected JavaHelpers getFeatureType() {
		return fFeatureType;
	}
	/**
	 Forward this to the java property editor
	 It is possible that the javaPropertyEditor rejects this value and throws an exception
	 If this is the case then error this to the user.
	 */
	protected void setAsText(String text) {
		if (fGotANull && text.length() == 0) {
			getProxyValueFromPropertyEditor();
		} else {
			try {
				fPropertyEditorWrapperProxy.setAsText(text);
				fGotANull = false;
				getProxyValueFromPropertyEditor();
			} catch (IllegalArgumentException e) {
				setValueValid(false);
				if (e.getMessage().equals("")) { //$NON-NLS-1$
					setErrorMessage(
						MessageFormat.format(
							JavaNls.BEANFEATUREEDITOR_ERRSETWITHNOMSG,
							new Object[] { e.getClass().getName()}));
				} else {
					setErrorMessage(
						MessageFormat.format(JavaNls.BEANFEATUREEDITOR_ERRSETWITHMSG, new Object[] { e.getMessage()}));
				}
			} catch (Throwable e) {
				setValueValid(false);
				setErrorMessage(
					MessageFormat.format(
						JavaNls.BEANFEATUREEDITOR_ERRSETWITHNOMSG,
						new Object[] { e.getClass().getName()}));
			}
		}
	}
	/**
	 * Forward this to the real cell editor
	 */
	public void doSetFocus() {
		getActualCellEditor().setFocus();
	}
	/**
	 * set the value into the proxy, evaluates is and set the fValidFlag and error message if 
	 * there is an error
	 */
	protected void setProxyValue(IJavaInstance value) {
		try {
			if (value != null) {
				IBeanProxyHost aBeanProxyHost = BeanProxyUtilities.getBeanProxyHost(value);
				aBeanProxyHost.instantiateBeanProxy();
				fLastKnownBeanProxy = aBeanProxyHost.getBeanProxy();
			} else {
				fLastKnownBeanProxy = null;
			}
			if (getBeanPropertyEditorProxy() != null) {
				fPropertyEditorWrapperProxy.setValue(fLastKnownBeanProxy);
			}
			setValueValid(isCorrect(value));
		} catch (IllegalArgumentException e) {
			// Catch the error from the remote VM which is nicely re-thrown as an IllegalArgumentException on this VM
			setValueValid(false);
			setErrorMessage(
				MessageFormat.format(JavaNls.BEANFEATUREEDITOR_ERRSETWITHMSG, new Object[] { e.getMessage()}));
		} catch (Throwable e) {
			// Other errors might be things like null pointers or the like - Should not occurr but deal with them anyway
			setValueValid(false);
			setErrorMessage(
				MessageFormat.format(
					JavaNls.BEANFEATUREEDITOR_ERRSETWITHNOMSG,
					new Object[] { e.getClass().getName()}));
		}
	}
	/**
	 * For a JavaBean where we are setting properties the source will be the java bean(s) that
	 * are selected
	 * At other times, such as when we are being used as a cell editor for a java parameter on an 
	 * event to method connection - the source is not a java instance
	 * If it is a java instance store it because we use it on the constructor to the java.beans.PropertyEditor
	 * if it has a constructor with an argument of Object.
	 * 
	 * It is required that this be called only while the cell editor is deactivated. 
	 */
	public void setSources(Object[] sources, IPropertySource[] pos, IPropertyDescriptor[] des) {
		if (sources[0] instanceof IJavaObjectInstance) {
			rebuildActualCellEditor = !(sources[0].equals(source));
			source = (IJavaObjectInstance) sources[0];
		}
	}

	/**
	 * setSource method comment.
	 */
	public void doSetValue(Object value) {
		fValue = (IJavaInstance) value;
		if (isActivated())
			setCellEditorNewValue();
	}
	protected void setCellEditorNewValue() {
		setProxyValue(fValue);	// Set in the new value since we got a new value now (actually it could also of come from the actual apply value, but then it would be the same value anyhow).
		fActualCellEditor.newValue(getAsText());
	}
	/**
	 * This will only expect initData to be a string that contains the qualified name
	 * of the java.beans.PropertyEditor class that is used to edit the feature
	 */
	public void setInitializationData(IConfigurationElement ce, String pName, Object initData) {
		if (initData instanceof String) {
			fJavaBeansPropertyEditorClassName = ((String) initData).trim();
		}
	}
	/**
	 * @see CellEditor#isActivated()
	 */
	public boolean isActivated() {
		if (fActualCellEditor != null) {
			return ((CellEditor) fActualCellEditor).isActivated();
		}
		return false;
	}

	/**
	 * @see CellEditor#activate()
	 */
	public void activate() {
		// Coming up, clear out any old and recreate.
		if (fActualCellEditor != null && rebuildActualCellEditor) {
			disposeOfActualCellEditor();	// We need to rebuild so get rid the actual editor too. Next time we get the control we will reactivate the editor.
		}
		
		if (getActualCellEditor() != null) {
			// Recreate if necessary (was done in the if (getActualCellEd...) above), then activate it.			
			setCellEditorNewValue();	// The value may of have changed while deactive.
			getActualCellEditor().activate();
		}
	}

	/**
	 * @see org.eclipse.jface.viewers.CellEditor#getLayoutData()
	 */
	public LayoutData getLayoutData() {
		return getActualCellEditor().getLayoutData();
	}

}
