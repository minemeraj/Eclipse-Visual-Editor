/*******************************************************************************
 * Copyright (c) 2003, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.java.rules;
/*
 *  $RCSfile: RuledWrapperedPropertyDescriptor.java,v $
 *  $Revision: 1.5 $  $Date: 2005-08-24 23:30:48 $ 
 */

import org.eclipse.gef.commands.Command;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.*;

import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.propertysheet.*;
import org.eclipse.ve.internal.propertysheet.command.*;

/**
 * This is a RuledWrapperedPropertyDescriptor that will use the 
 * RuledPropertySet/RestoreDefault Commands.
 */
public class RuledWrapperedPropertyDescriptor implements ICommandPropertyDescriptor, ISourcedPropertyDescriptor, IEToolsPropertyDescriptor {

	
	protected IPropertyDescriptor fDescriptor;
	protected IPropertySource fSource;
	protected EditDomain domain;
	protected Object fID;
	private String fDisplayName;
	
	/**
	 * MergedPropertyDescriptor constructor comment.
	 */
	public RuledWrapperedPropertyDescriptor(EditDomain domain, IPropertySource source, IPropertyDescriptor descriptor) {
		this(domain, descriptor.getId(), source, descriptor);
	}
	
	public RuledWrapperedPropertyDescriptor(EditDomain domain, String displayName, IPropertySource source, IPropertyDescriptor descriptor) {
		this(domain, descriptor.getId(), source, descriptor);
		fDisplayName = displayName;
	}

	
	public Object getHelpContextIds(){
		return fDescriptor.getHelpContextIds();
	}
	
	
	/**
	 * They are equal if the wrapper the same descriptor, id, and source.
	 */
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof RuledWrapperedPropertyDescriptor))
			return false;
			
		RuledWrapperedPropertyDescriptor wo = (RuledWrapperedPropertyDescriptor) o;
		return this.fDescriptor.equals(wo.fDescriptor) && this.fSource.equals(wo.fSource) && this.fID.equals(wo.fID);
	}
	
	/**
	 * Need to create a hashcode that goes with the equals.
	 */
	public int hashCode() {
		return fDescriptor.hashCode() ^ fSource.hashCode() ^ fID.hashCode();
	}
	
	/**
	 * MergedPropertyDescriptor constructor comment.
	 */
	public RuledWrapperedPropertyDescriptor(EditDomain domain, Object anID, IPropertySource source, IPropertyDescriptor descriptor) {
		super();
		this.domain = domain;
		fID = anID;
		fSource = source;
		fDescriptor = descriptor;
	}
	/**
	 * getCategory method comment.
	 */
	public String getCategory() {
		return fDescriptor.getCategory();
	}
	/**
	 * getDescription method comment.
	 */
	public String getDescription() {
		return fDescriptor.getDescription();
	}
	/**
	 * getDisplayName method comment.
	 */
	public String getDisplayName() {
		return fDisplayName == null ? fDescriptor.getDisplayName() : fDisplayName;
	}
	
	/**
	 * getFilterFlags method comment.
	 */
	public String[] getFilterFlags() {
		return fDescriptor.getFilterFlags();
	}
	/**
	 * getName method comment.
	 */
	public Object getId() {
		return fID;
	}
	/**
	 * getPropertyEditor method comment.
	 */
	public CellEditor createPropertyEditor(Composite parent) {
		return fDescriptor.createPropertyEditor(parent);
	}
	/**
	 * getRenderer method comment.
	 */
	public ILabelProvider getLabelProvider() {
		return fDescriptor.getLabelProvider();
	}
	/**
	 * getValue method comment.
	 */
	public Object getValue(IPropertySource source) {
		// If the descriptor is also an ISourcedPropertyDescriptor, then
		// route over to it, but use the wrappered source instead. Else
		// Use the standard mechanism for getting the value from the wrappered source.
		if (fDescriptor instanceof ISourcedPropertyDescriptor)
			return ((ISourcedPropertyDescriptor) fDescriptor).getValue(fSource);
		else
			return fSource.getPropertyValue(fDescriptor.getId());
	}
	/**
	 * Answer whether the value is set or not.
	 */
	public boolean isSet(IPropertySource source) {
		// If the descriptor is also an ISourcedPropertyDescriptor, then
		// route over to it, but use the wrappered source instead. Else
		// Use the standard mechanism for testing the value from the wrappered source.
		if (fDescriptor instanceof ISourcedPropertyDescriptor)
			return ((ISourcedPropertyDescriptor) fDescriptor).isSet(fSource);
		else
			return fSource.isPropertySet(fDescriptor.getId());
	}
	 
	/**
	 * isCompatibleWith method comment.
	 */
	public boolean isCompatibleWith(IPropertyDescriptor anotherProperty) {
		return fDescriptor.isCompatibleWith(anotherProperty instanceof RuledWrapperedPropertyDescriptor ? ((RuledWrapperedPropertyDescriptor) anotherProperty).fDescriptor : anotherProperty);
	}
	/**
	 * setValue method comment.
	 */
	public Command setValue(IPropertySource source, Object setValue) {
		// If the descriptor is also a CommandPropertyDescriptor, then
		// route over to it, but use the wrappered source instead. Else
		// use the standard mechanism for setting the value from the wrappered source.
		if (fDescriptor instanceof ICommandPropertyDescriptor)
			return ((ICommandPropertyDescriptor) fDescriptor).setValue(fSource , setValue);
		else {
			return new RuledPropertySetCommand(domain, fSource, fID, setValue);
		}
	}
	
	/**
	 * resetValue method comment.
	 */
	public Command resetValue(IPropertySource source) {
		// If the descriptor is also a CommandPropertyDescriptor, then
		// route over to it, but use the wrappered source instead. Else
		// use the standard mechanism for resetting the value from the wrappered source.
		if (fDescriptor instanceof ICommandPropertyDescriptor)
			return ((ICommandPropertyDescriptor) fDescriptor).resetValue(fSource);
		else {
			return new RuledRestoreDefaultPropertyCommand(domain, source, fID);
		}
	}
	
	public boolean areNullsInvalid() {
		return (fDescriptor instanceof IEToolsPropertyDescriptor) ? ((IEToolsPropertyDescriptor) fDescriptor).areNullsInvalid() : false;
	}
	
	public boolean isExpandable() {
		return (fDescriptor instanceof IEToolsPropertyDescriptor) ? ((IEToolsPropertyDescriptor) fDescriptor).isExpandable() : true;
	}
	
	public boolean isReadOnly()	{
		return (fDescriptor instanceof IEToolsPropertyDescriptor) ? ((IEToolsPropertyDescriptor) fDescriptor).isReadOnly() : false;
	}


	public String toString() {
		return getClass().getName() + "(" + fDescriptor.toString() + ")";//$NON-NLS-2$//$NON-NLS-1$
	}

	public boolean isPropertyResettable(IPropertySource source) {
		// If the descriptor is also an ISourcedPropertyDescriptor, then
		// route over to it, but use the wrappered source instead. Else
		// Use the standard mechanism for testing the value from the wrappered source.
		if (fDescriptor instanceof ISourcedPropertyDescriptor)
			return ((ISourcedPropertyDescriptor) fDescriptor).isPropertyResettable(fSource);
		else if (fSource instanceof IPropertySource2)
			return ((IPropertySource2) fSource).isPropertyResettable(fDescriptor.getId());
		else
			return true;
	}
}
