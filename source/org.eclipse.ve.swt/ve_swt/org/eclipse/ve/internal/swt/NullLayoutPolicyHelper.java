package org.eclipse.ve.internal.swt;
/*
 * Licensed Material - Property of IBM 
 * (C) Copyright IBM Corp. 2002 - All Rights Reserved. 
 * US Government Users Restricted Rights - Use, duplication or disclosure 
 * restricted by GSA ADP Schedule Contract with IBM Corp. 
 */

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.UnexecutableCommand;
import org.eclipse.ve.internal.cde.core.ContainerPolicy;
import org.eclipse.ve.internal.cde.commands.ApplyAttributeSettingCommand;
import org.eclipse.ve.internal.cde.commands.CommandBuilder;

import org.eclipse.jem.internal.instantiation.InstantiationFactory;
import org.eclipse.jem.internal.instantiation.JavaAllocation;
import org.eclipse.jem.internal.instantiation.base.*;
import org.eclipse.ve.internal.java.rules.*;
import org.eclipse.ve.internal.java.visual.ILayoutPolicyHelper;
import org.eclipse.ve.internal.java.visual.VisualContainerPolicy;

import org.eclipse.ve.internal.propertysheet.common.commands.CompoundCommand;
;
/**
 * Null layout policy helper.
 */
public class NullLayoutPolicyHelper extends LayoutPolicyHelper {

	
/**
 * Constraint object to be passed into this class.
 * rect: the constraint rectangle
 * moved: True if the location portion of the rect should be applied
 * resized: True if the size portion of the rect should be applied.
 */
public static class NullConstraint {
	public Rectangle rect;
	public boolean moved, resized;

	public NullConstraint(Rectangle rect, boolean moved, boolean resized) {
		this.rect = rect;
		this.moved = moved;
		this.resized = resized;
	}
}

public NullLayoutPolicyHelper(VisualContainerPolicy ep) {
	super(ep);
}

/**
 * This is a temporary hack to add an initialization string (allocation) to a dropped component
 * which contain a parsed tree referencing the parent.
 * 
 * Rich has not implemented a ref. parsed tree yet, so use this as a deprecated method
 * 
 * @deprecated
 * @param parent
 * @return
 * 
 * @since 1.0.0
 */

private Command  createInitStringCommand(IJavaObjectInstance child, IJavaObjectInstance parent) {
  
	TemporaryPTE pt = new TemporaryPTE() ;
	pt.setParent(parent);
	pt.setFlags("org.eclipse.swt.SWT.None") ;
	JavaAllocation alloc = InstantiationFactory.eINSTANCE.createParseTreeAllocation(pt);
	ApplyAttributeSettingCommand applyCmd = new ApplyAttributeSettingCommand();
	applyCmd.setTarget(child);
	applyCmd.setAttribute(child.eClass().getEStructuralFeature("allocation"));
	applyCmd.setAttributeSettingValue(alloc);	
	
	return applyCmd;
}

public Command getCreateChildCommand(Object childComponent, Object constraint, Object position) {
	return getCreateChildCommand(childComponent,null,constraint,position) ;

}
/**
 * Return a command to create the child with the constraints set correctly
 */
public Command getCreateChildCommand(Object childComponent, Object parent, Object constraint, Object position) {
	
	Command createContributionCmd = policy.getCreateCommand(childComponent, position);
	if (createContributionCmd == null || !createContributionCmd.canExecute())
		return UnexecutableCommand.INSTANCE;	// It can't be created
			
	CompoundCommand command = new CompoundCommand("");		 //$NON-NLS-1$
	if (parent != null)
	  command.append(createInitStringCommand((IJavaObjectInstance) childComponent, (IJavaObjectInstance) parent));
	command.append(createChangeConstraintCommand((IJavaObjectInstance) childComponent, (NullConstraint) constraint));
	command.append(createContributionCmd);

	return command.unwrap();
}

protected void cancelConstraints(CommandBuilder cb, List children) {
	IJavaObjectInstance parent = (IJavaObjectInstance) policy.getContainer();
	EStructuralFeature
		sfComponentLocation = JavaInstantiation.getSFeature(parent, SWTConstants.SF_CONTROL_LOCATION),
		sfComponentSize = JavaInstantiation.getSFeature(parent, SWTConstants.SF_CONTROL_SIZE),
		sfComponentBounds = JavaInstantiation.getSFeature(parent, SWTConstants.SF_CONTROL_BOUNDS);	

	Iterator childrenItr = children.iterator();
	while(childrenItr.hasNext()) {
		EObject child = (EObject) childrenItr.next();
		if (child.eIsSet(sfComponentBounds))
			cb.cancelAttributeSetting(child, sfComponentBounds);
		if (child.eIsSet(sfComponentSize))
			cb.cancelAttributeSetting(child, sfComponentSize);
		if (child.eIsSet(sfComponentLocation))
			cb.cancelAttributeSetting(child, sfComponentLocation);

	}
}
/**
 * Determine what the default constraint(s) are for this layout manager
 * and assign a constraint to each child.
 * Return a List with a constraint for each child.
 */
public List getDefaultConstraint(List children) {

	// We are going to return a array of constraints that 
	// have x and y values equal to Integer.MIN_VALUE and a width and height that are equal to -1.
	// These are the place holders that mean that when the constraints are applied, the 
	// preferred size will be used.
	return Collections.nCopies(children.size(), new NullConstraint(new Rectangle(Integer.MIN_VALUE, Integer.MIN_VALUE, -1, -1), true, true));
}

public Command getChangeConstraintCommand(List children, List constraints) {
	Iterator childItr = children.iterator();
	Iterator conItr = constraints.iterator();
	CompoundCommand cmd = new CompoundCommand();
	while (childItr.hasNext()) {
		cmd.append(createChangeConstraintCommand((IJavaObjectInstance) childItr.next(), (NullConstraint) conItr.next()));
	}
	
	return !cmd.isEmpty() ? cmd.unwrap() : null;
}

protected Command createChangeConstraintCommand(IJavaObjectInstance child, NullConstraint constraint) {
	ApplyNullLayoutConstraintCommand cmd = new ApplyNullLayoutConstraintCommand();
	cmd.setTarget(child);
	cmd.setDomain(policy.getEditDomain());
	if (constraint.moved || constraint.resized)
		cmd.setConstraint(constraint.rect, constraint.moved, constraint.resized);
	else
		return null;
	return cmd;
}

}
