package org.eclipse.ve.internal.cde.core;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.gef.requests.TargetRequest;

public class CDECreateRequest extends CreateRequest implements TargetRequest {
	
	EditPart targetEditPart;
		
	public void setTargetEditPart(EditPart aTargetEditPart){
		targetEditPart = aTargetEditPart;
	}
	
	public Object getNewObject() {
		// Hard code for now for the scenario of dropping a TreeViewer and wanting to drop a Tree
		Object result = super.getNewObject();
		if(result instanceof EObject){
			EObject eResult = (EObject)result;
			EStructuralFeature treeSF = eResult.eClass().getEStructuralFeature("tree");
			if(treeSF != null){
				Object tree = eResult.eGet(treeSF);
				return tree;
			}
		}
		return super.getNewObject();
	}

}
