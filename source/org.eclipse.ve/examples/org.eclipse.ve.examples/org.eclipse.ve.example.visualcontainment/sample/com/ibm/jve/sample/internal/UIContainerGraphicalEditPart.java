package com.ibm.jve.sample.internal;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.gef.EditPolicy;
import org.eclipse.ve.internal.cde.core.CDELayoutEditPolicy;
import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.swt.ControlGraphicalEditPart;

public class UIContainerGraphicalEditPart extends ControlGraphicalEditPart {

	public UIContainerGraphicalEditPart(Object model) {
		super(model);
	}
	
	protected IFigure createFigure() {
		IFigure fig = super.createFigure();
		fig.setLayoutManager(new XYLayout());
		return fig;
	}	
	
	protected void createEditPolicies() {
		super.createEditPolicies();
		// Edit policy to add children to the "parts" relationship if they are UIBasePart types
		installEditPolicy(EditPolicy.LAYOUT_ROLE, new CDELayoutEditPolicy(
				new UIContainerPartContainerPolicy(EditDomain.getEditDomain(this))
				));
						
	}

}
