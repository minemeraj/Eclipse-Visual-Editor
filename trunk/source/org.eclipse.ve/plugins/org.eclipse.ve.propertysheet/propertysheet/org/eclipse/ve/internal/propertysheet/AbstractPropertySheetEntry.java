/*******************************************************************************
 * Copyright (c) 2001, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.propertysheet;
/*
 *  $RCSfile: AbstractPropertySheetEntry.java,v $
 *  $Revision: 1.16 $  $Date: 2006-05-17 20:16:06 $ 
 */


import java.util.*;

import org.eclipse.core.runtime.*;
import org.eclipse.jface.viewers.*;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.views.properties.*;

import com.ibm.icu.text.Collator;
import com.ibm.icu.util.ULocale;

/**
 * <code>AbstractPropertySheetEntry</code> is an abstract implementation of
 * <code>IPropertySheetEntry</code> which uses <code>IPropertySource</code>
 * and <code>IPropertyDescriptor</code> to interact with domain model objects.
 * <p>
 * Every property sheet entry has a set of descriptors (except the root entry
 * which has none), one for each propertySource, which comes from the parent.
 * These descriptors determines what property
 * of its objects it will display/edit. The first descriptor will be used
 * to determine the property to display/edit. The subclasses will determine
 * how the descriptors are used for getting and setting values. They may
 * use only the id and use the property source to set it, or they may
 * create commands for each individual descriptor.
 * </p>
 * <p>
 * Entries do not listen for changes in their objects. Since there is no
 * restriction on properties being independent, a change in one property
 * may affect other properties. The value of a parent's property may also
 * change. As a result we are forced to refresh the entire entry tree
 * when a property changes value.
 * </p>
 */
public abstract class AbstractPropertySheetEntry implements IDescriptorPropertySheetEntry {
	/**
	 * The values we are displaying/editing.
	 * These objects represent the value of one of the
	 * properties of the values of our parent entry.
	 * Except for the root entry where they represent the
	 * input (selected) objects.
	 */
	protected Object[] values = new Object[0];
	
	/**
	 * The property sources for the values we are displaying/editing.
	 * If the value is not convertable to a property source, then
	 * null will be stored in the corresponding location.
	 */
	protected IPropertySource[] propertySources = new IPropertySource[0];
	
	/**
	 * Should we be expandable. Sometimes this is not desired.
	 */
	protected boolean fIsExpandable = true;

	/**
	 * The value of this entry is defined as the the first object
	 * in its value array or, if that object is an 
	 * <code>IPropertySource</code>, the value it returns when sent
	 * <code>getEditableValue</code>
	 */
	private Object editValue;

	protected boolean entryStale = true;	// Flag indicating the entry is stale, don't do anything until refreshed. Don't want to put out possibly invalid results.
	
	protected IDescriptorPropertySheetEntry parent;
	private IPropertySourceProvider propertySourceProvider;
	protected IPropertyDescriptor[] fDescriptors;
	private CellEditor editor;
	private String errorText;
	protected IDescriptorPropertySheetEntry[] childEntries = null;
	private ListenerList listeners;
	protected boolean fShowNulls = false;
	protected boolean fShowSet = false;
	protected boolean fShowReadOnly = false;
	private boolean fValueIsSet = false;	

	private List fCachedMergedDescriptors = null;	// Cache. This way the computation won't be performed
								// twice, once in hasChildEntries and once again in create/refreshChildEntries.
								// However, it is cleared up after create/refresh so that if it changes it will
								// be recomputed. It will also be cleared when we get new values so that
								// it can be recomputed on the next request.
								
	private boolean fRefreshQueued = false;	// A refresh is queued up.
	
	private Object fData;	// Arbitrary data.
	
	/**
	 * Create the CellEditorListener for this entry. It listens for
	 * value changes in the CellEditor, and cancel and finish requests.
	 */
	private ICellEditorListener cellEditorListener = new ICellEditorListener() {
		public void editorValueChanged(boolean oldValidState, boolean newValidState) {
			if (!newValidState)
				// currently not valid so show an error message
				setErrorText(editor.getErrorMessage());
			else 
				// currently valid 
				setErrorText(null);
		}
		public void cancelEditor() {
			setErrorText(null);
		}
		public void applyEditorValue() {
			AbstractPropertySheetEntry.this.applyEditorValue();
		}
	};
	
/**
 * Create an entry.
 */
public AbstractPropertySheetEntry(IDescriptorPropertySheetEntry parent, IPropertySourceProvider provider) {
	this.parent = parent;
	propertySourceProvider = provider;
}

/**
 * Set the data.
 */
public 	void setData(Object data) {
	fData = data;
}

public void markStale() {
	if (parent != null)
		entryStale = true;	// Only children are marked stale, the root is never stale.
	if (childEntries != null) {
		for (int i = 0; i < childEntries.length; i++)
			childEntries[i].markStale();
	}
}

/**
 * Get the data.
 */
public Object getData() {
	if (fData == null)
		return parent != null ? parent.getData() : null;
	else
		return fData;
}

/* (non-Javadoc)
 * Method declared on IPropertySheetEntry.
 */
public void addPropertySheetEntryListener(IPropertySheetEntryListener listener) {
	if (listeners == null)
		listeners = new ListenerList(ListenerList.IDENTITY);
	listeners.add(listener);
}

/* (non-Javadoc)
 * Method declared on IPropertySheetEntry.
 */
public final void applyEditorValue() {
	// Check if editor has a valid value
	if (editor == null || !isEditorActivated())
		return;
	// Check if editor has a valid value
	if (!editor.isValueValid()) {
		setErrorText(editor.getErrorMessage());
		return;
	} else {
		setErrorText(null);
	}

	Object newValue = editor.getValue();
	applyNewValue(newValue);
}

/*
 * Apply this specific value to the property sources.
 */
protected final void applyNewValue(Object newValue) {
	try {

		// See if the value changed and if so update	
		boolean changed = false;
		if (editValue == null) {
			if (newValue != null)
				changed = true;
		} else if (!editValue.equals(newValue))
			changed = true;

		// Set the editor value
		if (changed) {
			// Set the value into the values array.
			fillValues(newValue, values);

			// Now apply the values to the sources.
			primApplyValues();

			// Refresh the model
			refreshFromRoot();
		}
	} catch (RuntimeException e) {
		// Don't want to let this go on up. Causes problems later on.
		PSheetPlugin.getPlugin().getLog().log(new Status(IStatus.WARNING, PSheetPlugin.getPlugin().getBundle().getSymbolicName(), 0, "", e)); //$NON-NLS-1$
	}
}

/**
 * Return the sorted intersection of all the <code>IPropertyDescriptor</code>s 
 * for the objects. Each entry will be IPropertyDescriptor[], where each entry
 * in the array corresponds to the descriptor for the input value of the same index.
 */
protected List computeMergedPropertyDescriptors() {
	if (values.length == 0)
		return Collections.EMPTY_LIST;
		
	// get all descriptors from each object
	Map[] propertyDescriptorMaps = new Map[values.length];
	IPropertySource[] sources = getPropertySources();
	for (int i = 0; i < sources.length; i++) {
		IPropertySource source = sources[i];
		if (source == null) {
			// if one of the selected items is not a property source
			// then we show no properties
			return Collections.EMPTY_LIST;
		}
		// get the property descriptors keyed by id
		propertyDescriptorMaps[i] = computePropertyDescriptorsFor(source);
	}

	// compute the intersection, saving a map of id->{descriptors}
	// The intersection will be of compatible descriptors found in all values. 
	Map intersection = new HashMap(propertyDescriptorMaps[0].size());
	Iterator v0Itr = propertyDescriptorMaps[0].values().iterator();
	IPropertyDescriptor[] work = new IPropertyDescriptor[values.length];
nextDesc:
	while (v0Itr.hasNext()) {
		IPropertyDescriptor v0desc = (IPropertyDescriptor) v0Itr.next();	// Next descriptor entry for value[0]
		work[0] = v0desc;
		Object v0Id = v0desc.getId();
		for (int i = 1; i < propertyDescriptorMaps.length; i++){
			IPropertyDescriptor desc = (IPropertyDescriptor) propertyDescriptorMaps[i].get(v0Id);
			// see if the descriptors (which have the same id) are compatible			
			if (desc == null || !v0desc.isCompatibleWith(desc))
				continue nextDesc;	// Not compatible, so don't add any of these desc to list
			else
				work[i] = desc;	// Add it to the list of this id.
		}
		intersection.put(v0Id, work.clone());
	}

	// Sort the descriptors	
	// Sort here because we need to reuse entries... building new entries is slow... 
	// see https://bugs.eclipse.org/bugs/show_bug.cgi?id=97593
	List descriptors = new ArrayList(intersection.values());
		Collections.sort(descriptors, new Comparator() {
			Collator coll = Collator.getInstance(ULocale.getDefault().toLocale());
			public int compare(Object a, Object b) {
				// Sort using the first descriptor from each array. They will all have the same id in the array, so the first is sufficient
				IPropertyDescriptor d1, d2;
				String dname1, dname2;
				d1 = ((IPropertyDescriptor[]) a)[0];
				dname1 = d1.getDisplayName();
				d2 = ((IPropertyDescriptor[]) b)[0];
				dname2 = d2.getDisplayName();
				return coll.compare(dname1, dname2);
			}
		});
		return descriptors;
}

/**
 * Returns an map of property descritptors (keyed on id) for the 
 * given property source.
 *
 * @source a property source for which to obtain descriptors
 * @return a table of decriptors keyed on their id
 */
protected Map computePropertyDescriptorsFor(IPropertySource source) {
	IPropertyDescriptor[] descriptors = source.getPropertyDescriptors();
	Map result = new HashMap(descriptors.length*2+1);
	for (int i = 0; i < descriptors.length; i++){
		result.put(descriptors[i].getId(), descriptors[i]);
	}
	return result;
}

/**
 * Create a property sheet entry of the desired type.
 * Use the provider passed in.
 *
 * It is used to create children of this current entry.
 */
protected abstract IDescriptorPropertySheetEntry createPropertySheetEntry(IPropertySourceProvider provider);

/**
 * Create our child entries.
 */
protected IDescriptorPropertySheetEntry[] createChildEntries() {
	// get the current descriptors
	List descriptors = fCachedMergedDescriptors == null ? computeMergedPropertyDescriptors() : fCachedMergedDescriptors;
	fCachedMergedDescriptors = null;	// So next time we recompute

	//IDescriptorPropertySheetEntry[] entries = new IDescriptorPropertySheetEntry[descriptors.size()];
	IDescriptorPropertySheetEntry[] entries = null;
	ArrayList newChildren = new ArrayList(descriptors.size());

	Iterator itr = descriptors.iterator();
	for(int i=0; itr.hasNext(); i++) {
		IPropertyDescriptor[] ds = (IPropertyDescriptor[]) itr.next();
		
		//if the entry is read only, a new entry won't get created and displayed in the property sheet
		if (!fShowReadOnly && ds[0] instanceof IEToolsPropertyDescriptor){
			IEToolsPropertyDescriptor desc = (IEToolsPropertyDescriptor)ds[0];
			if (desc.isReadOnly())
				continue;
		}

		// create new entry
		IDescriptorPropertySheetEntry entry = createPropertySheetEntry(propertySourceProvider);
		entry.setDescriptors(ds);
		entry.refreshValues();
		entry.setShowNulls(fShowNulls);
		entry.setShowSetValues(fShowSet);
		entry.setShowReadOnly(fShowReadOnly);	
		newChildren.add(entry);
	}
	entries = new IDescriptorPropertySheetEntry[newChildren.size()];
	entries = (IDescriptorPropertySheetEntry[])(newChildren.toArray(entries));
	return entries;
}

/* (non-Javadoc)
 * Method declared on IPropertySheetEntry.
 */
public void dispose() {
	synchronized (this) {
		fRefreshQueued = false;	// In case any are queued up, this will prevent them wasting time refreshing.
	}
	
	if (editor != null) {
		editor.dispose();
		editor = null;
	}
	// recursive call to dispose children
	if (childEntries != null)
		for (int i = 0; i < childEntries.length; i++)
			childEntries[i].dispose();
			
	listeners = null;	// No more listeners for a disposed entry.
}
/**
 * The child entries of this entry have changed 
 * (children added or removed).
 * Notify all listeners of the change.
 */
protected void fireChildEntriesChanged() {
	if (listeners == null)
		return;
	Object[] array = listeners.getListeners();	
	for (int i = 0; i < array.length; i++) {
		IPropertySheetEntryListener listener = (IPropertySheetEntryListener)array[i];
		listener.childEntriesChanged(this);
	}
}
/**
 * The error message of this entry has changed.
 * Notify all listeners of the change.
 */
private void fireErrorMessageChanged() {
	if (listeners == null)
		return;
	Object[] array = listeners.getListeners();	
	for (int i = 0; i < array.length; i++) {
		IPropertySheetEntryListener listener = (IPropertySheetEntryListener)array[i];
		listener.errorMessageChanged(this);
	}
}
/**
 * The values of this entry have changed.
 * Notify all listeners of the change.
 */
private void fireValueChanged() {
	if (listeners == null)
		return;
	Object[] array = listeners.getListeners();	
	for (int i = 0; i < array.length; i++) {
		IPropertySheetEntryListener listener = (IPropertySheetEntryListener)array[i];
		listener.valueChanged(this);
	}
}
/* (non-Javadoc)
 * Method declared on IPropertySheetEntry.
 */
public String getCategory() {
	return !isStale() ? fDescriptors[0].getCategory() : null;	// While stale, don't go against the descriptors, they may be bad;
}
/* (non-Javadoc)
 * Method declared on IPropertySheetEntry.
 */
public IPropertySheetEntry[] getChildEntries() {
	if (childEntries == null) 
		childEntries = createChildEntries();
	return childEntries;
}

/* (non-Javadoc)
 * Method declared on IPropertySheetEntry.
 */
public String getDescription() {
	return !isStale() ? fDescriptors[0].getDescription() : null;	// While stale, don't go against the descriptors, they may be bad;
}

/* (non-Javadoc)
 * Method declared on IPropertySheetEntry.
 */
public String getDisplayName() {
	if (entryStale)
		return PropertysheetMessages.AbstractPropertySheetEntry_DisplayName_StaleEntry; 
	
	try {
		String name = fDescriptors[0].getDisplayName();
		if (fShowSet && fValueIsSet)
			name = '>'+name;
		return name;
	} catch (RuntimeException e) {
		// Don't want to let this go on up. Causes problems later on.
		PSheetPlugin.getPlugin().getLog().log(new Status(IStatus.WARNING, PSheetPlugin.getPlugin().getBundle().getSymbolicName(), 0, "", e)); //$NON-NLS-1$
		return PropertysheetMessages.AbstractPropertySheetEntry_DisplayName_Error; 
	}	
}

public String getSortDisplayName() {
	if (entryStale)
		return PropertysheetMessages.AbstractPropertySheetEntry_DisplayName_StaleEntry; 
	
	try {
		String name = fDescriptors[0].getDisplayName();
		return name;
	} catch (RuntimeException e) {
		// Don't want to let this go on up. Causes problems later on.
		PSheetPlugin.getPlugin().getLog().log(new Status(IStatus.WARNING, PSheetPlugin.getPlugin().getBundle().getSymbolicName(), 0, "", e)); //$NON-NLS-1$
		return PropertysheetMessages.AbstractPropertySheetEntry_DisplayName_Error; 
	}	
}

/* (non-Javadoc)
 * Method declared on IPropertySheetEntry.
 */
public CellEditor getEditor(Composite parent) {

	if (editor == null) {
		editor = fDescriptors[0].createPropertyEditor(parent);
		processEditorValidator(editor);			
		if (editor != null)
			editor.addListener(cellEditorListener);					
	}
	
	setupEditor();
	
	return editor;
}

/**
 * This method is for processing the validator. It allows adding more validators to the editor. Subclass to do this,
 * but always call super.processEditorValidator() too.
 * 
 * @since 1.0.0
 */
protected void processEditorValidator(CellEditor cellEditor) {
	if (cellEditor != null && areNullsInvalid()) {
		// Nulls aren't valid, so add in the no nulls validator.
		cellEditor.setValidator(new NoNullsValidator(cellEditor.getValidator()));
	}
}

/*
 * @see org.eclipse.ve.internal.propertysheet.IDescriptorPropertySheetEntry#areNullsInvalid()
 */
public final boolean areNullsInvalid() {
	if (isStale())
		return false;	// While stale, don't go against the descriptors, they may be bad
	// If any of the descriptors don't allow nulls, then nulls are invalid.
	// Non-IEToolsPropertyDescriptors by design don't allow nulls. This may not be true, but this is typically true. If they
	// want to allows nulls, then they should use IEToolsPropertyDescriptors.
	for (int i = 0; i < fDescriptors.length; i++) {
		if (!(fDescriptors[i] instanceof IEToolsPropertyDescriptor) || ((IEToolsPropertyDescriptor) fDescriptors[i]).areNullsInvalid())
			return true;
	}
	return false;
}

/**
 * Used to setup the editor with new values.
 */
protected void setupEditor() {
	if (editor != null) {
		try {
			ICellEditorValidator validator = editor.getValidator();	
			
			if (editor instanceof INeedData|| anyNeedDataValidators(validator)) {
				Object data = getData();
				if (editor instanceof INeedData)
					((INeedData) editor).setData(data);
				applyDataToValidators(validator, data);
			}
			
			if (editor instanceof ISourced || anySourcedValidators(validator)) {
				IPropertySource[] pos = parent.getPropertySources();
				Object[] sources = new Object[pos.length];
				for (int i=0; i<pos.length; i++)
					sources[i] = pos[i].getEditableValue();
				if (editor instanceof ISourced)
					((ISourced) editor).setSources(sources, pos, fDescriptors);
				applySourcesToValidators(validator, sources, pos, fDescriptors);
			}
		
			editor.setValue(editValue);			
			setErrorText(editor.getErrorMessage());
		} catch (Exception e) {
			// Some editors throw exceptions if invalid values are sent in.
			// so since it didn't accept this new value, the old value will display.
		}
	}
}	

private boolean anySourcedValidators(ICellEditorValidator validator) {
	if (validator instanceof ISourced)
		return true;
		
	if (validator instanceof IWrapperedValidator) {
		ICellEditorValidator[] validators = ((IWrapperedValidator) validator).getValidators();
		if (validators != null) {
			for (int i=0; i<validators.length; i++) {
				if (anySourcedValidators(validators[i]))
					return true;
			}
		}
	}	
	return false;
}

private boolean anyNeedDataValidators(ICellEditorValidator validator) {
	if (validator instanceof INeedData)
		return true;
		
	if (validator instanceof IWrapperedValidator) {
		ICellEditorValidator[] validators = ((IWrapperedValidator) validator).getValidators();
		if (validators != null) {
			for (int i=0; i<validators.length; i++) {
				if (anyNeedDataValidators(validators[i]))
					return true;
			}
		}
	}	
	return false;
}


private void applySourcesToValidators(ICellEditorValidator validator, Object[] sources, IPropertySource[] propertySources, IPropertyDescriptor[] descriptors) {
	if (validator instanceof ISourced)
		((ISourced) validator).setSources(sources, propertySources, descriptors);
		
	if (validator instanceof IWrapperedValidator) {
		ICellEditorValidator[] validators = ((IWrapperedValidator) validator).getValidators();
		if (validators != null) {
			for (int i=0; i<validators.length; i++)
				applySourcesToValidators(validators[i], sources, propertySources, descriptors);
		}	
	}
}

private void applyDataToValidators(ICellEditorValidator validator, Object data) {
	if (validator instanceof INeedData)
		((INeedData) validator).setData(data);
		
	if (validator instanceof IWrapperedValidator) {
		ICellEditorValidator[] validators = ((IWrapperedValidator) validator).getValidators();
		if (validators != null) {
			for (int i=0; i<validators.length; i++)
				applyDataToValidators(validators[i], data);
		}	
	}
}

	
/**
 * Returns the edited value of this entry.
 *
 * @return the edited value of this entry
 */
public Object getEditValue() {
	return editValue;
}

/**
 * Returns the edit value for the object at the given index.
 *
 * @param index the value object index
 * @return the edit value for the object at the given index
 */
protected Object getEditValue(int index) {
	Object value = values[index];
	IPropertySource[] sources = getPropertySources();
	if (sources[index] != null) {
		value = sources[index].getEditableValue();
	}
	return value;
}

/* (non-Javadoc)
 * Method declared on IPropertySheetEntry.
 */
public String getErrorText() {
	return errorText;
}

/* (non-Javadoc)
 * Method declared on IPropertySheetEntry.
 */
public String getFilters() [] {
	return !isStale() ? fDescriptors[0].getFilterFlags() : null;	// While stale, don't go against the descriptors, they may be bad;
}

/* (non-Javadoc)
 * Method declared on IPropertySheetEntry.
 */
public Object getHelpContextIds() {
	return !isStale() ? fDescriptors[0].getHelpContextIds() : null;	// While stale, don't go against the descriptors, they may be bad;
}

/* (non-Javadoc)
 * Method declared on IDescriptorPropertySheetEntry
 */
public Object getId() {
	return fDescriptors[0].getId();
}

/* (non-Javadoc)
 * Method declared on IPropertySheetEntry.
 */
public Image getImage() {
	if (entryStale)
		return null;
	
	try {
		ILabelProvider provider = fDescriptors[0].getLabelProvider();
		if (provider == null)
			return null;
		if (provider instanceof INeedData)
			((INeedData) provider).setData(getData());
		return provider.getImage(editValue);
	} catch (RuntimeException e) {
		// Don't want to let this go on up. Causes problems later on.
		PSheetPlugin.getPlugin().getLog().log(new Status(IStatus.WARNING, PSheetPlugin.getPlugin().getBundle().getSymbolicName(), 0, "", e)); //$NON-NLS-1$
		return null;
	}	
}


/**
 * Returns a property source for the given object.
 * It will always reaccess the property source. This
 * should be used only for first access to the property source.
 * Otherwise the getPropertySources should be used.
 *
 * @object an object for which to obtain a property source or
 *  <code>null</code> if a property source is not available
 * @return an property source for the given object
 */
protected IPropertySource getPropertySource(Object object) {
		
	IPropertySource result = null; 
	if (propertySourceProvider != null)
		result = propertySourceProvider.getPropertySource(object);
	else if (object instanceof IPropertySource) 
		result = (IPropertySource)object;
	else if (object instanceof IAdaptable)
		result = (IPropertySource)((IAdaptable)object).getAdapter(IPropertySource.class);
		
	return result;
}

/**
 * Return the array of property sources corresponding to
 * the values.
 */
public IPropertySource[] getPropertySources() {
	if (propertySources == null) {
		propertySources = new IPropertySource[values.length];
		for (int i=0; i<values.length; i++) 
			propertySources[i] = getPropertySource(values[i]);
	}
	
	return propertySources;
}

public Object[] getValues() {
	return values;
}

/* (non-Javadoc)
 * Method declared on IPropertySheetEntry.
 */
public String getValueAsString() {
	if (entryStale)
		return PropertysheetMessages.AbstractPropertySheetEntry_ValueAsString_StaleEntry; 
		
	try {
		if (editValue == null)
			return fShowNulls ? PropertysheetMessages.display_null : ""; //$NON-NLS-1$
		
		ILabelProvider provider = fDescriptors[0].getLabelProvider();
		if (provider == null)
			return editValue.toString();
		else {
			if (provider instanceof INeedData)
				((INeedData) provider).setData(getData());
			if (provider instanceof ISourced) {
				IPropertySource[] pos = parent.getPropertySources();
				Object[] sources = new Object[pos.length];
				for (int i=0; i<pos.length; i++)
					sources[i] = pos[i].getEditableValue();
				((ISourced) provider).setSources(sources, pos, fDescriptors);
			}
			return provider.getText(editValue);
		}
	} catch (RuntimeException e) {
		// Don't want to let this go on up. Causes problems later on.
		PSheetPlugin.getPlugin().getLog().log(new Status(IStatus.WARNING, PSheetPlugin.getPlugin().getBundle().getSymbolicName(), 0, "", e)); //$NON-NLS-1$
		return PropertysheetMessages.AbstractPropertySheetEntry_ValueAsString_Error; 
	}
}

/* (non-Javadoc)
 * Method declared on IPropertySheetEntry.
 */
public boolean hasChildEntries() {
	if (childEntries != null && childEntries.length > 0)
		return true;
	else if (fIsExpandable) {
		fCachedMergedDescriptors = computeMergedPropertyDescriptors();
		int numberOfChilden = fCachedMergedDescriptors.size();
		if (!fShowReadOnly && fCachedMergedDescriptors.size() > 0) {
			for (int i = 0; i < fCachedMergedDescriptors.size(); i++) {
				IPropertyDescriptor[] ds = (IPropertyDescriptor[])fCachedMergedDescriptors.get(i);
				if (ds[0] instanceof IEToolsPropertyDescriptor){
					IEToolsPropertyDescriptor desc = (IEToolsPropertyDescriptor)ds[0];
					if ( desc.isReadOnly())
						numberOfChilden--;
				}
			}
		}
		// see if we could have entires if we were asked
		return numberOfChilden > 0;
	} else
		return false;
}
/**
 * Update our child entries.
 * This implementation tries to reuse child entries if possible 
 * (if the id of the new descriptor matches the descriptor id of the
 * old entry).
 */
protected void refreshChildEntries() {
	synchronized (this) {
		fRefreshQueued = false;	// So that if this occurs while more are queued up, don't have them done.	
	}
	
	if (childEntries == null) {
		// no children to refresh
		return;
	}
		
	// get the current descriptors
	List descriptors = fCachedMergedDescriptors == null ? computeMergedPropertyDescriptors() : fCachedMergedDescriptors;
	fCachedMergedDescriptors = null;	// So next time we recompute
		
	// rebuild child entries using old when possible
	// For now we assume any entry can be reused simply by assigning descriptors and calling refresh values.
	ArrayList newChildren = new ArrayList(descriptors.size());
	ArrayList entriesToDispose = new ArrayList(2);	// Any determined not reusable will be set into here.
	boolean entriesAdded = false;	// Where any entries added?
	int nextEntry = 0;
	for (int i = 0; i < descriptors.size(); i++) {
		IPropertyDescriptor[] ds = (IPropertyDescriptor[])descriptors.get(i);
		
		//if the entry is read only and we are not read-only's, an entry won't get created and displayed in the property sheet
		if (!fShowReadOnly && ds[0] instanceof IEToolsPropertyDescriptor){
			IEToolsPropertyDescriptor desc = (IEToolsPropertyDescriptor)ds[0];
			if ( desc.isReadOnly())
				continue;
		}

		// Kludge: Bug in current PropertySheetViewer (bugzilla #10001) won't allow an image to go to null if currently not null
		// So if this is the case, we can't reuse the entry.
		boolean oldHasImage = false;

		IDescriptorPropertySheetEntry entry = null;
		if (nextEntry < childEntries.length) {
			entry = childEntries[nextEntry++];	// Reuse this one.
			oldHasImage = entry.getImage() != null;
		} else {
			// create new entry
			entry = createPropertySheetEntry(propertySourceProvider);
			entry.setShowNulls(fShowNulls);	
			entry.setShowSetValues(fShowSet);
			entry.setShowReadOnly(fShowReadOnly);			
			entriesAdded = true;
		}
		entry.setDescriptors(ds);
		entry.refreshValues();
		
		// Continuation of Kludge for bugzilla #10001.
		if (oldHasImage && entry.getImage() == null) {
			entriesToDispose.add(entry);
			// Create a new entry.
			entry = createPropertySheetEntry(propertySourceProvider);
			entry.setShowNulls(fShowNulls);	
			entry.setShowSetValues(fShowSet);
			entry.setShowReadOnly(fShowReadOnly);			
			entriesAdded = true;
			entry.setDescriptors(ds);
			entry.refreshValues();
		}

		newChildren.add(entry);
	}
	
	IDescriptorPropertySheetEntry[] oldEntries = childEntries;
	childEntries = new IDescriptorPropertySheetEntry[newChildren.size()];
	childEntries = (IDescriptorPropertySheetEntry[])(newChildren.toArray(childEntries));
	
	if (entriesAdded || !entriesToDispose.isEmpty() || childEntries.length != oldEntries.length)
		fireChildEntriesChanged();
		
	//Dispose of entries which are no longer needed		
	for (int i = newChildren.size(); i < oldEntries.length; i++) {
		oldEntries[i].dispose();		
	}
	
	for (int i = 0; i < entriesToDispose.size(); i++) {
		((IDescriptorPropertySheetEntry) entriesToDispose.get(i)).dispose();		
	}
}
/* (non-Javadoc)
 * Method declared on IDescriptorPropertySheetEntry.
 */
public void refreshFromRoot() {
	if (parent == null) {
		// These can happen several at a time, so we will queue them up so they only happen once.
		synchronized (this) {
			fRefreshQueued = true;
			markStale();	// Mark all of my children as stale.
		}

		Display d = Display.getCurrent();
		if (d == null)
			d = Display.getDefault();
		d.asyncExec(new Runnable() {
			public void run() {
				boolean refresh;
				synchronized (AbstractPropertySheetEntry.this) {
					refresh = fRefreshQueued;
					fRefreshQueued = false;
				}
					if (refresh)
						refreshChildEntries();
			}
		});
	} else
		parent.refreshFromRoot();
}
/**
 * Update our value objects.
 * We ask our parent for the property values based on
 * our descriptor.
 */
public final void refreshValues() {
	// set our new values
	setValues(primGetValues());
}

/**
 * Retrieve new values and return the array.
 */
protected abstract Object[] primGetValues();

/* (non-Javadoc)
 * Method declared on IPropertySheetEntry.
 */
public void removePropertySheetEntryListener(IPropertySheetEntryListener listener) {
	if (listeners == null)
		return;
	listeners.remove(listener);
}
/* (non-Javadoc)
 * Method declared on IPropertySheetEntry.
 */
public final void resetPropertyValue() {
	if (parent == null) {
		// root does not have a default value
		return;
	}

	if (primResetPropertyValues())
		refreshFromRoot();	// A change was made so we need to refresh our local value.
}

/**
 * Reset the value, answer true if there is any change.
 */
protected abstract boolean primResetPropertyValues();

/**
 * Answer whether this property is set for this entry.
 */
protected abstract boolean isPropertySet();

/* (non-Javadoc)
 * Method declared on IDescriptorPropertySheetEntry.
 */
public void setDescriptors(IPropertyDescriptor[] newDescriptors) {
	// if our descriptor is changing, we have to get rid
	// of our current editor if there is one
	if (editor != null && !Arrays.equals(fDescriptors, newDescriptors)) {
		editor.dispose();
		editor = null;
	}
	
	fDescriptors = newDescriptors;
	
	// See if the descriptor is marked as not expandable.
	fIsExpandable = !(fDescriptors[0] instanceof IEToolsPropertyDescriptor) || ((IEToolsPropertyDescriptor) fDescriptors[0]).isExpandable();	
}

/*
 * Set the error text.  This should be set to null when
 * the current value is valid, otherwise it should be
 * set to a error string
 */
private void setErrorText(String newErrorText) {
	errorText = newErrorText;
	// inform listeners
	fireErrorMessageChanged();
}

/*
 * Sets a property source provider for this entry. 
 * This provider is used to obtain an <code>IPropertySource</code>
 * for each of this entries objects. If no provider is
 * set then a default provider is used.
 */
public void setPropertySourceProvider(IPropertySourceProvider provider) {
	propertySourceProvider = provider;	
}

protected final void fillValues(Object newEditValue, Object[] valuesArray) {
	primFillValues(newEditValue, valuesArray);
	propertySources = null;	// So that they will be rebuild next time needed.
}

/**
 * Given the edit value, fill in the passed in array
 * with the appropriate values. This is used to take
 * the value from the editor and to propagate it to
 * all of the values for this entry.
 *
 * Note: This shouldn't be called directly. fillValues above should
 * be called instead to make sure things are setup correctly.
 */
protected abstract void primFillValues(Object newEditValue, Object[] valuesArray);

/**
 * Apply all of the values to thier sources.
 */
protected abstract void primApplyValues();

/** 
 * The <code>PropertySheetEntry</code> implmentation of this
 * method declared on<code>IPropertySheetEntry</code> will
 * obtain an editable value for the given objects and update
 * the child entries.
 * <p>
 * Updating the child entries will typically call this method
 * on the child entries and thus the entire entry tree is updated
 * </p>
 * @param objects the new values for this entry
 */
public void setValues(Object[] objects) {
	values = objects;
	propertySources = null;	// Lazy build the list.
	fCachedMergedDescriptors = null;	// So that next refresh will rebuild it.
	
	entryStale = false;	// This particular entry is no longer stale.
	if (values.length == 0) {
		editValue = null;
		fValueIsSet = false;
	} else {
		// set the last value object as the entry's value. This is the "Primary" in GEF.
		Object newValue = values[values.length-1];

		// see if we should convert the value to an editable value
		IPropertySource source = getPropertySource(newValue);
		if (source != null)
			newValue = source.getEditableValue();
		editValue = newValue;
		fValueIsSet = isPropertySet();		
	}
	
	// If we have an active editor, change its value to the new one.		
	if (isEditorActivated()) {
		setupEditor();
	}	

	// update our child entries
	refreshChildEntries();

	// inform listeners that our value changed
	fireValueChanged();
}

/**
 * Set the value to null.
 */
public void setToNull() {
	if (editValue == null && !isPropertySet()) {
		// This is just to force the change. Without this on a not set property, 
		// it would look like it was being changed from null to null, and so it would not apply it.
		editValue = Void.TYPE;
		applyNewValue(null);
		if (editValue == Void.TYPE)
			editValue = null; // In case the apply failed, we don't want it to stay Void.TYPE.
	} else
		applyNewValue(null);
}

/**
 * Set whether should nulls or empty string.
 */
public void setShowNulls(boolean showNulls) {
	fShowNulls = showNulls;
	if (childEntries != null) {
		for (int i=0; i<childEntries.length; i++)
			childEntries[i].setShowNulls(showNulls);
	}
}

public void setShowReadOnly(boolean showReadOnly){
	fShowReadOnly = showReadOnly;
	if (childEntries != null){
		for (int i= 0; i < childEntries.length; i++)
			childEntries[i].setShowReadOnly(showReadOnly);
	}
}

/**
 * Set whether should nulls or empty string.
 */
public void setShowSetValues(boolean showSet) {
	fShowSet = showSet;
	if (childEntries != null) {
		for (int i=0; i<childEntries.length; i++)
			childEntries[i].setShowSetValues(showSet);
	}
}

/**
 * @see org.eclipse.ve.internal.propertysheet.IDescriptorPropertySheetEntry#isEditorActivated()
 */
public final boolean isEditorActivated() {
	return editor != null && editor.isActivated();
}

/**
 * Is the entry stale. If it is stale then it is
 * in an intermediate state and is in the process 
 * of being refreshed. This takes time, so don't
 * rely on anything while it is stale.
 */
public boolean isStale() {
	return entryStale;
}

/**
 * @see org.eclipse.ve.internal.propertysheet.IDescriptorPropertySheetEntry#getParent()
 */
public IDescriptorPropertySheetEntry getParent() {
	return parent;
}

}
