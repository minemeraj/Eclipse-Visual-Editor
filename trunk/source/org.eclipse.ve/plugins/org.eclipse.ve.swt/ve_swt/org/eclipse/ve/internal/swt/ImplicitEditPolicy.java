package org.eclipse.ve.internal.swt;

import org.eclipse.emf.ecore.EReference;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ContainerEditPolicy;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.jem.internal.instantiation.ImplicitAllocation;
import org.eclipse.jem.internal.instantiation.JavaAllocation;
import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.java.rules.IChildRule;

public class ImplicitEditPolicy extends ContainerEditPolicy {
	
	private EditDomain editDomain;
	private Command command;
	private IJavaInstance container;

	public ImplicitEditPolicy(EditDomain anEditDomain, IJavaInstance aContainer){
		editDomain = anEditDomain;
		container = aContainer;
	}

	protected Command getCreateCommand(CreateRequest request) {

		// The object in the createRequest is the one being created.  Find the one it is implicitly being retrieved from
		// and create a command to add this to the BeanSubclassComposition
		
		IJavaInstance newJavaObject = (IJavaInstance) request.getNewObject();
		JavaAllocation allocation = newJavaObject.getAllocation();
		if(allocation instanceof ImplicitAllocation){
			ImplicitAllocation implicitAllocation = (ImplicitAllocation)allocation;
			IJavaInstance parentObject = (IJavaInstance) implicitAllocation.getParent();
			EReference implicitFeature = (EReference) implicitAllocation.getFeature();
			// Create the command to add the parent to the BeanComposition 
			return ((IChildRule)editDomain.getRuleRegistry().getRule(IChildRule.RULE_ID)).preCreateChild(
					editDomain, container , parentObject , implicitFeature);
		} else {
			return null;
		}
		
	}

}
