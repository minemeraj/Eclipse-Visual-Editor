/*
 * Created on 02-Jan-04
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package org.eclipse.ve.internal.swt;

import java.util.List;

import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.gef.commands.Command;
import org.eclipse.jem.internal.instantiation.base.JavaInstantiation;

import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.java.core.JavaEditDomainHelper;
import org.eclipse.ve.internal.java.visual.VisualContainerPolicy;

/**
 * @author JoeWin
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class CompositeContainerPolicy extends VisualContainerPolicy {
	
	protected EReference sfLayoutData;
	
	public CompositeContainerPolicy(EditDomain domain) {
		super(JavaInstantiation.getSFeature(
			JavaEditDomainHelper.getResourceSet(domain), 
			SWTConstants.SF_COMPOSITE_CONTROLS), 
			domain);
		
		ResourceSet rset = JavaEditDomainHelper.getResourceSet(domain);
		sfLayoutData = JavaInstantiation.getReference(rset, SWTConstants.SF_CONTROL_LAYOUTDATA);
	}
	

	public Command getCreateCommand(Object constraintComponent, Object childComponent, Object position) {
		// TODO Auto-generated method stub
		return null;
	}

	public Command getAddCommand(List componentConstraints, List childrenComponents, Object position) {
		// TODO Auto-generated method stub
		return null;
	}

}
