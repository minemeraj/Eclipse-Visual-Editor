/*******************************************************************************
 * Copyright (c)  2003, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 * $RCSfile: CDECreationTool.java,v $ $Revision: 1.12 $ $Date: 2005-12-14 21:27:11 $
 */
package org.eclipse.ve.internal.cde.core;

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.text.*;
import org.eclipse.gef.*;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editparts.LayerManager;
import org.eclipse.gef.requests.CreationFactory;
import org.eclipse.gef.tools.CreationTool;
import org.eclipse.swt.graphics.Cursor;

import org.eclipse.ve.internal.cde.emf.IDomainedFactory;

/**
 * This is the base Creation Tool that all CDE editors should use. It can be a CreationTool itself for most
 * cases. It does:
 * <p>
 * <bl>
 * <li>Will use the Cursor out of the CreateRequest's extended data so that edit policies can return their specific cursor to use.
 * <li>Uses the CDEUtilities.calculateCursor to return the appropriate cursor for the state of the domain.
 * <li>Dispose of the cursor from the CreateRequest when no longer needed.
 * <li>Support the CreationPolicy for EClassifiers. </bl>
 * 
 * @author JoeWin
 */
public class CDECreationTool extends CreationTool {

	public static String CREATION_POLICY_KEY = "org.eclipse.ve.internal.cde.core.creationtool.policy"; //$NON-NLS-1$

	protected boolean fHasLookedupCreationPolicy = false;

	protected Cursor editPolicyCursor;

	private FlowPage unexecutableMsgPage;
	private Label unexecutableImageLabel;
	private TextFlow unexecutableTextFlow;

	protected CDECreationTool(CreationFactory aFactory) {
		super(aFactory);
	}

	/**
	 * For when constructed through ToolEntry.createTool().
	 * 
	 * 
	 * @since 1.1.0
	 */
	public CDECreationTool() {
	}

	public void activate() {
		super.activate();
		CreationFactory fact = getFactory();
		if (fact instanceof IDomainedFactory)
			((IDomainedFactory) fact).setEditDomain((EditDomain) getDomain());
	}

	
	protected void showTargetFeedback() {
		super.showTargetFeedback();
		Object cursor = getTargetRequest().getExtendedData().get(Cursor.class);
		if (cursor instanceof Cursor) {
			editPolicyCursor = (Cursor) cursor;
		}
		UnExecutableCommandData data = (UnExecutableCommandData) getTargetRequest().getExtendedData().get(UnExecutableCommandData.class);
		if(data!=null){
			EditPart targetEP = getTargetEditPart();
			if(targetEP!=null && targetEP instanceof GraphicalEditPart){
				GraphicalEditPart gep = (GraphicalEditPart) targetEP;
				LayerManager layoutManager = LayerManager.Helper.find(gep);
				if(layoutManager!=null){
					IFigure feedbackLayer = layoutManager.getLayer(LayerConstants.FEEDBACK_LAYER);
					if(unexecutableMsgPage==null){
						// show feedback is called many times before
						// an erase feedback is called. Erase is called
						// once per target change.
						unexecutableMsgPage = new FlowPage();
						unexecutableMsgPage.setBorder(new MarginBorder(5));

						FlowAdapter imageAdapter = new FlowAdapter();
						imageAdapter.setLayoutManager(new StackLayout());
						imageAdapter.setBorder(new MarginBorder(0,0,0,5));
						unexecutableImageLabel = new Label();
						imageAdapter.add(unexecutableImageLabel);
						unexecutableMsgPage.add(imageAdapter);

						unexecutableTextFlow = new TextFlow();
						unexecutableTextFlow.setForegroundColor(ColorConstants.darkBlue);
						unexecutableTextFlow.setLayoutManager(new ParagraphTextLayout(unexecutableTextFlow, ParagraphTextLayout.WORD_WRAP_TRUNCATE));
						unexecutableMsgPage.add(unexecutableTextFlow);
					}

					unexecutableImageLabel.setIcon(data.getImage());
					unexecutableTextFlow.setText(data.getMessage());

					feedbackLayer.add(unexecutableMsgPage);
					unexecutableMsgPage.setBounds(gep.getFigure().getBounds());
				}
			}
		}
	}

	protected Cursor getEditPolicyCursor() {
		if (editPolicyCursor != null) {
			return editPolicyCursor;
		} else {
			return getDefaultCursor();
		}
	}

	protected Cursor calculateCursor() {
		Cursor result = CDEUtilities.calculateCursor((EditDomain) getDomain());
		if (result != null)
			return result;
		Command command = getCurrentCommand();
		if (command == null || !command.canExecute()) {
			return getDisabledCursor();
		} else {
			return getEditPolicyCursor();
		}
	}

	protected void eraseTargetFeedback() {
		super.eraseTargetFeedback();
		Object cursor = getTargetRequest().getExtendedData().get(Cursor.class);
		if (cursor == null && editPolicyCursor != null) {
			setCursor(getDefaultCursor());
			editPolicyCursor.dispose();
			editPolicyCursor = null;
		}
		if(getTargetRequest().getExtendedData().containsKey(UnExecutableCommandData.class)) // clean it up
			getTargetRequest().getExtendedData().remove(UnExecutableCommandData.class);
		if(unexecutableMsgPage!=null){
			EditPart targetEP = getTargetEditPart();
			if(targetEP!=null){
				LayerManager layerManager = LayerManager.Helper.find(targetEP);
				if(layerManager!=null){
					IFigure feedbackLayer = layerManager.getLayer(LayerConstants.FEEDBACK_LAYER);
					feedbackLayer.remove(unexecutableMsgPage);
					unexecutableMsgPage = null;
				}
			}
		}
	}
}
