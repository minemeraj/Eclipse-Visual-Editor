/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: JavaVisualEditorModelChangeController.java,v $
 *  $Revision: 1.6 $  $Date: 2005-02-22 13:43:15 $ 
 */
package org.eclipse.ve.internal.java.codegen.editorpart;

import org.eclipse.jface.text.Assert;
import org.eclipse.jface.text.IRewriteTarget;
import org.eclipse.swt.widgets.Display;

import org.eclipse.ve.internal.cde.core.CDEMessages;
import org.eclipse.ve.internal.cde.core.IModelChangeController;

import org.eclipse.ve.internal.java.codegen.core.IDiagramModelBuilder;
import org.eclipse.ve.internal.propertysheet.IDescriptorPropertySheetEntry;

/**
 * Change controller for the JVE. It is <package>protected because only the
 * JavaVisualEditorPart should access it.
 * 
 * @since 1.0.0
 */
class JavaVisualEditorModelChangeController extends IModelChangeController {

    private JavaVisualEditorPart part;
    private IDiagramModelBuilder modelBuilder;

    public JavaVisualEditorModelChangeController(JavaVisualEditorPart part,IDiagramModelBuilder modelBuilder) {
        this.modelBuilder = modelBuilder;
        this.part = part;
    }

    protected IDescriptorPropertySheetEntry getRootPropertySheetEntry() {
        return part.rootPropertySheetEntry;
    }

    protected synchronized void startChange() {
        if (compoundChangeCount++ == 0) {
            // The undomanager doesn't handle nesting of compound changes, so we
            // need to do it here.
            IRewriteTarget rewriteTarget = (IRewriteTarget) this.part
                    .getAdapter(IRewriteTarget.class);
            rewriteTarget.beginCompoundChange();
            modelBuilder.startTransaction();
        }
    }

    protected synchronized void stopChange() {
        if (--compoundChangeCount <= 0) {
            compoundChangeCount = 0; // In case we get out of sync.
            try {
                modelBuilder.commit();
            } finally {
                // this must be done or undo stack will get messed up. Just in
                // case an error in commit.
                // Also must be done AFTER commit in case commit changes some
                // more of the code and that
                // needs to be under compound change too.
                IRewriteTarget rewriteTarget = (IRewriteTarget) this.part
                        .getAdapter(IRewriteTarget.class);
                rewriteTarget.endCompoundChange();
                executeAsyncRunnables();
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ve.internal.cde.core.IModelChangeController#getHoldState()
     */
    public int getHoldState() {
        Assert.isTrue(Display.getCurrent() != null);

        if (modelBuilder.isBusy())
            return BUSY_STATE;
        else if (holdState != READY_STATE)
            return holdState;
        else {
            synchronized (this) {
                // If not in transaction and not valid to change model
                if (compoundChangeCount == 0 && !part.validateEditorInputState()) {
                    return NO_UPDATE_STATE;
                } else {
                    return READY_STATE;
                }
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ve.internal.cde.core.IModelChangeController#getHoldMsg()
     */
    public String getHoldMsg() {
        if (holdMsg != null)
            return holdMsg;
        switch (getHoldState()) {
        case BUSY_STATE:
            return CDEMessages.getString("ModelChangeController.EditorBusyAndCannotChangeNow"); //$NON-NLS-1$
        case NO_UPDATE_STATE:
            return CDEMessages.getString("ModelChangeController.EditorCannotBeChangedNow"); //$NON-NLS-1$
        }
        return null;
    }

}