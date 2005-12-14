/*******************************************************************************
 * Copyright (c) 2001, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 * $RCSfile: WindowContainmentHandler.java,v $ $Revision: 1.4 $ $Date: 2005-12-14 21:37:04 $
 */
package org.eclipse.ve.internal.jfc.core;



import java.util.*;

import org.eclipse.gef.commands.Command;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.jem.internal.instantiation.*;
import org.eclipse.jem.internal.instantiation.base.*;
import org.eclipse.jem.java.*;

import org.eclipse.ve.internal.cdm.DiagramData;

import org.eclipse.ve.internal.cde.commands.ApplyAttributeSettingCommand;
import org.eclipse.ve.internal.cde.commands.CommandBuilder;
import org.eclipse.ve.internal.cde.core.EditDomain;

import org.eclipse.ve.internal.jcm.*;

import org.eclipse.ve.internal.java.rules.RuledCommandBuilder;

import org.eclipse.ve.internal.propertysheet.common.commands.CommandWrapper;

/**
 * Window/Dialog containment handler.
 * <p>
 * It handles dropping on Window that requires a frame. It will see if over a frame, and if not, are there any frames available. If none, then not
 * accepted. If one, it will use it. If more than one it will pop up a selection dialog.
 * <p>
 * If there is an allocation already then it will only allow drop if on the freeform. It will not popup all frames available because we don't know
 * which arg is for the frame parent. (Maybe later we can get fancier if the need arises).
 * 
 * @since 1.2.0
 */
public class WindowContainmentHandler extends AbstractComponentModelContainmentHandler {

	public WindowContainmentHandler(Object component) {
		super(component);
	}

	public Object contributeToDropRequest(Object parent, Object child, CommandBuilder preCmds, CommandBuilder postCmds, boolean creation,
			final EditDomain domain) throws StopRequestException {
		if (child instanceof IJavaObjectInstance) {
			IJavaObjectInstance jo = (IJavaObjectInstance) child;
			child = dropWindow(parent, jo, domain, preCmds);
		}
		return child;
	}
	
	/*
	 * Create the window with a parent.
	 * @param jo
	 * @param javaClass
	 * @param frame
	 * @return
	 * 
	 * @since 1.2.0
	 */
	private static Command createWithParent(IJavaObjectInstance jo, JavaClass javaClass, IJavaObjectInstance frame) {
		// Now that we have a frame.
		PTInstanceReference parentFrame = InstantiationFactory.eINSTANCE.createPTInstanceReference(frame);
		PTClassInstanceCreation newExpr = InstantiationFactory.eINSTANCE.createPTClassInstanceCreation(javaClass
				.getQualifiedNameForReflection(), Collections.singletonList(parentFrame));
		ApplyAttributeSettingCommand applycommand = new ApplyAttributeSettingCommand();
		applycommand.setTarget(jo);
		applycommand.setAttribute(JavaInstantiation.getAllocationFeature(jo));
		applycommand.setAttributeSettingValue(InstantiationFactory.eINSTANCE.createParseTreeAllocation(newExpr));
		return applycommand;
	}

	/**
	 * Static helper to allow drop of a Window/JWindow (i.e. something that takes a Frame in a constructor) and should be on the
	 * freeform. It will allow drop on a frame if the child has no allocation and has a constructor that takes a frame. If dropping
	 * on the freeform it will allow drop if a null constructor or there is a frame available (brings up dialog to ask customer) it
	 * will use that frame. If allocation is set, then it will only allow drop on freeform. It is assumed allocation already correct.
	 * 
	 * @param parent
	 * @param jo
	 * @param domain
	 * @param preCmds
	 * @return
	 * @throws StopRequestException 
	 * 
	 * @since 1.2.0
	 */
	public static Object dropWindow(Object parent, final IJavaObjectInstance jo, final EditDomain domain, CommandBuilder preCmds) throws StopRequestException {
		Object childToDrop = jo;
		DiagramData freeform = domain.getDiagramData();
		if (!jo.isSetAllocation()) {
			// See if there is a null ctor (in case of subclassing) and if so it will just accept it on freeform only.
			final JavaClass javaClass = (JavaClass) jo.getJavaType();
			JavaClass frameClass = (JavaClass) JavaRefFactory.eINSTANCE.reflectType("java.awt.Frame", javaClass); //$NON-NLS-1$
			JavaClass windowClass = (JavaClass) JavaRefFactory.eINSTANCE.reflectType("java.awt.Window", javaClass); //$NON-NLS-1$
			JavaClass dialogClass = (JavaClass) JavaRefFactory.eINSTANCE.reflectType("java.awt.Dialog", javaClass); //$NON-NLS-1$
			// Look at the constructors to see whether there is a null constructor or not
			// Remember that the absence of any contructors means that there is an implicit null constructor
			Iterator methods = javaClass.getMethods().iterator();
			boolean hasNullConstructor = false, hasFrameConstructor = false, hasWindowConstructor = false, hasDialogConstructor = false;
			while (methods.hasNext() && (!hasNullConstructor || !hasFrameConstructor || !hasWindowConstructor)) {
				Method javaMethod = (Method) methods.next();
				// If we have a constructor then flag this
				if (javaMethod.isConstructor() && JavaVisibilityKind.PUBLIC_LITERAL == javaMethod.getJavaVisibility()) {
					List methodInputParms = javaMethod.getParameters();
					// See whether we have a null constructor
					if (methodInputParms.isEmpty()) {
						hasNullConstructor = true;
					} else if (methodInputParms.size() == 1) {
						// See whether or not there is a constructor that takes only frame argument
						JavaParameter inputParm = (JavaParameter) methodInputParms.iterator().next();
						JavaHelpers type = inputParm.getJavaType();
						if (type == frameClass)
							hasFrameConstructor = true;
						else if (type == windowClass)
							hasWindowConstructor = true;
						else if (type == dialogClass)
							hasDialogConstructor = true;
					}
				}
			}
			
			if (hasFrameConstructor || hasWindowConstructor || hasDialogConstructor) {
				// See how many frames/windows are available.
				final List validParents = new ArrayList();
				if (!((hasFrameConstructor && frameClass.isInstance(parent)) || (hasWindowConstructor && windowClass.isInstance(parent)) || (hasDialogConstructor && dialogClass.isInstance(parent)))) {
					// We are not over a valid parent (i.e. there is not a frame or window or dialog constructor that can take the parent)).
					// So only if over freeform, then we find all valid parents on freeform.
					if (parent == freeform) {
						if (freeform instanceof BeanSubclassComposition) {
							IJavaObjectInstance thisPart = ((BeanSubclassComposition) freeform).getThisPart();
							if (thisPart != null && (hasFrameConstructor && frameClass.isInstance(thisPart) || (hasWindowConstructor && windowClass.isInstance(thisPart))))
								validParents.add(thisPart);
						}
						if (freeform instanceof BeanComposition) {
							List components = ((BeanComposition) freeform).getComponents();
							for (Iterator comps = components.iterator(); comps.hasNext();) {
								IJavaInstance comp = (IJavaInstance) comps.next();
								if ((hasFrameConstructor && frameClass.isInstance(comp) || (hasWindowConstructor && windowClass.isInstance(comp))) || (hasDialogConstructor && dialogClass.isInstance(comp)))
									validParents.add(comp);
							}
						}
					} else
						throw new StopRequestException(JFCMessages.WindowContainmentHandler_ChildDroppableOnFFOrValidParent);
				} else {
					// Parent is a frame or window or dialog and we have a good constructor for it, so we choose just it.
					validParents.add(parent);
				}
				if (!validParents.isEmpty()) {
					if (validParents.size() > 1) {
						final boolean childHasNullConstructor = hasNullConstructor;
						preCmds.append(new CommandWrapper() {

							protected boolean prepare() {
								return true;
							}

							public void execute() {
								IJavaObjectInstance frame = null;
								// Popup a dialog.
								Shell s = domain.getEditorPart().getSite().getShell();
								FrameSelectorDialog fsd = new FrameSelectorDialog(s, domain);
								fsd.setInput(validParents);
								int r = fsd.open();
								if (r == Dialog.CANCEL)
									if (childHasNullConstructor)
										return; // Use the null constructor.
									else
										throw new RuntimeException(JFCMessages.WindowContainmentHandler_UserCancelledDrop);
								frame = (IJavaObjectInstance) fsd.getSelectedFrame();

								command = createWithParent(jo, javaClass, frame);
								command.execute();
							}
						});
					} else
						preCmds.append(createWithParent(jo,javaClass, (IJavaObjectInstance) validParents.get(0)));
					
					if (parent != freeform) {
						// We need to add to diagram data components.
						BeanComposition bc = (BeanComposition) domain.getDiagramData();
						RuledCommandBuilder rcb = new RuledCommandBuilder(domain);
						rcb.applyAttributeSetting(bc, JCMPackage.eINSTANCE.getBeanComposition_Components(), childToDrop);
						preCmds.append(rcb.getCommand());
						childToDrop = null; // We handled it on the ff, no child for the parent. (We just used parent as the frame).
					}
				} else if (hasNullConstructor) {
					// Null ctor and on free form is just do as is.
					if (parent != freeform)
						throw new StopRequestException(JFCMessages.WindowContainmentHandler_ChildDroppableOnFFOrValidParent);
				} else
					throw new StopRequestException(JFCMessages.WindowContainmentHandler_ChildNoDrop);
			} else if (hasNullConstructor) {
				// Null ctor and on free form is just do as is.
				if (parent != freeform)
					throw new StopRequestException(JFCMessages.WindowContainmentHandler_ChildDroppableOnFFOrValidParent);
			} else
				throw new StopRequestException(JFCMessages.WindowContainmentHandler_ChildNoDrop);
		} else if (parent != freeform)
			throw new StopRequestException(JFCMessages.WindowContainmentHandler_ChildDroppableOnFF);	// It has an allocation. In which case we can't look into and change it at this time.
		return childToDrop;
	}

}
