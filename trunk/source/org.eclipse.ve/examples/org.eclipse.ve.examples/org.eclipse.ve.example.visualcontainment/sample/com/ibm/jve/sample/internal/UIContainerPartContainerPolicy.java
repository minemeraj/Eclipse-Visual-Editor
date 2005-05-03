package com.ibm.jve.sample.internal;

import org.eclipse.emf.common.util.URI;
import org.eclipse.jem.internal.instantiation.base.JavaInstantiation;
import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.java.core.JavaContainerPolicy;
import org.eclipse.ve.internal.java.core.JavaEditDomainHelper;

public class UIContainerPartContainerPolicy extends JavaContainerPolicy {

	public UIContainerPartContainerPolicy(EditDomain domain) {		
		super(JavaInstantiation.getSFeature(
				JavaEditDomainHelper.getResourceSet(domain), 
				URI.createURI("java:/com.ibm.jve.sample.containers#UIContainer/parts")), 
			domain);		

	}

}
