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
package org.eclipse.ve.examples.cdm.dept.ui;
/*
 *  $RCSfile: CompanyGraphicalEditorPart.java,v $
 *  $Revision: 1.8 $  $Date: 2005-08-24 23:16:43 $ 
 */

import java.io.*;
import java.text.MessageFormat;
import java.util.*;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.common.util.WrappedException;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.gef.*;
import org.eclipse.gef.commands.*;
import org.eclipse.gef.editparts.FreeformGraphicalRootEditPart;
import org.eclipse.gef.palette.*;
import org.eclipse.gef.ui.actions.*;
import org.eclipse.gef.ui.parts.*;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.*;
import org.eclipse.ui.actions.WorkspaceModifyOperation;
import org.eclipse.ui.dialogs.SaveAsDialog;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;
import org.eclipse.ui.views.properties.*;

import org.eclipse.ve.internal.cdm.*;

import org.eclipse.ve.internal.cde.core.CDEMessages;

import org.eclipse.ve.examples.cdm.dept.Company;
import org.eclipse.ve.internal.propertysheet.EToolsPropertySheetPage;
import org.eclipse.ve.internal.propertysheet.command.CommandStackPropertySheetEntry;

/**
 * Company Graphical Editor
 */
public class CompanyGraphicalEditorPart extends GraphicalEditorWithPalette {

	protected Company company; // The working model.
	protected boolean dirty;
	protected org.eclipse.ve.internal.cde.core.EditDomain editDomain;

	public void dispose() {
		super.dispose();
		((org.eclipse.ve.internal.cde.core.EditDomain) getEditDomain()).dispose();
	}

	protected CommandStackListener fDirtyListener = new CommandStackListener() {
		public void commandStackChanged(EventObject event) {
			setDirty(true);
		}
	};

	protected PaletteRoot getPaletteRoot() {
		// Must have a pallete or selection won't work.
		PaletteRoot dinnerPalette = new PaletteRoot(); 
		fillPalette(dinnerPalette);
		return dinnerPalette;
	}
	
	private void fillPalette(PaletteRoot root) {
		fillControlGroup(root);
		
		PaletteGroup companyGroup = new PaletteGroup("Company Group");
		root.add(companyGroup);

		companyGroup.add(new CreationToolEntry("Department", "Create a new department",new DepartmentFactory(), null, null));
		companyGroup.add(new CreationToolEntry("Employee", "Create a new employee", new EmployeeFactory(), null, null));		
	}


	private void fillControlGroup(PaletteRoot root) {
		PaletteGroup controlGroup = new PaletteGroup("Control Group");
		ToolEntry tool = new SelectionToolEntry();
		root.setDefaultEntry(tool);
		controlGroup.add(tool);
		root.add(controlGroup);
	}

	public Object getAdapter(Class type) {
		if (type == IPropertySheetPage.class) {
			PropertySheetPage page = createPropertySheetPage();
			page.setRootEntry(getRootPropertySheetEntry());
			return page;
		}

		if (type == GraphicalViewer.class)
			return getGraphicalViewer();
			
		if (type == IContentOutlinePage.class)
			return createOutlinePage();			
			
		return super.getAdapter(type);
	}

	protected PropertySheetPage createPropertySheetPage() {
		return new EToolsPropertySheetPage();
	}

	protected IPropertySheetEntry getRootPropertySheetEntry() {
		return new CommandStackPropertySheetEntry(getCommandStack(), null, null);

	}

	public void doSave(final IProgressMonitor progressMonitor) {

		final org.eclipse.ve.internal.cde.core.EditDomain dom = (org.eclipse.ve.internal.cde.core.EditDomain) getEditDomain();
		Command cmd = dom.getAnnotationLinkagePolicy().cleanupDiagramData(dom.getDiagramData());
		if (cmd != null)
			cmd.execute();
		WorkspaceModifyOperation op = new WorkspaceModifyOperation() {
			public void execute(final IProgressMonitor monitor) throws CoreException {
				try {
					ByteArrayOutputStream bout = new ByteArrayOutputStream();
					ObjectOutputStream os = new ObjectOutputStream(bout);
					os.writeObject(company);
					os.close();

					IFile file = ((IFileEditorInput) getEditorInput()).getFile();
					file.setContents(new ByteArrayInputStream(bout.toByteArray()), true, true, new SubProgressMonitor(progressMonitor, 10));

					if (dom.getDiagramData() != null) {
						DiagramData dd = dom.getDiagramData();
						dd.eResource().save(Collections.EMPTY_MAP);
					}

					((CommandStack) getAdapter(CommandStack.class)).flush();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};

		try {
			op.run(progressMonitor);
		} catch (Exception e) {
		}
	}

	public boolean isSaveAsAllowed() {
		return true;
	}

	public boolean isDirty() {
		return dirty;
	}

	protected boolean performSaveAs() {
		SaveAsDialog dialog = new SaveAsDialog(getSite().getWorkbenchWindow().getShell());
		dialog.setOriginalFile(((IFileEditorInput) getEditorInput()).getFile());
		dialog.open();
		IPath path = dialog.getResult();

		if (path == null)
			return false;

		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		final IFile file = workspace.getRoot().getFile(path);
		final org.eclipse.ve.internal.cde.core.EditDomain dom = (org.eclipse.ve.internal.cde.core.EditDomain) getEditDomain();

		Command cmd = dom.getAnnotationLinkagePolicy().cleanupDiagramData(dom.getDiagramData());
		if (cmd != null)
			cmd.execute();

		WorkspaceModifyOperation op = new WorkspaceModifyOperation() {
			public void execute(final IProgressMonitor monitor) throws CoreException {
				try {
					ByteArrayOutputStream bout = new ByteArrayOutputStream();
					ObjectOutputStream os = new ObjectOutputStream(bout);
					os.writeObject(company);
					os.close();

					file.create(new ByteArrayInputStream(bout.toByteArray()), true, monitor);

					// Now need to save new DiaglogData.
					if (dom.getDiagramData() != null) {
						String ddPath = file.getFullPath().removeFileExtension().lastSegment();
						ddPath += "DialogData.xmi";
						DiagramData dd = dom.getDiagramData();
						dd.eResource().setURI(URI.createPlatformResourceURI(ddPath));
						dd.eResource().save(Collections.EMPTY_MAP);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};

		try {
			PlatformUI.getWorkbench().getProgressService().run(false, true, op);
			setInput(new FileEditorInput(file));
			getCommandStack().flush();
		} catch (Exception x) {
			x.printStackTrace();
		}
		return true;
	}

	protected void setDirty(boolean dirty) {
		// If being set clean, signal. Else if not already dirty, then signal.
		if (!dirty || !this.dirty) {
			this.dirty = dirty;
			firePropertyChange(PROP_DIRTY);
		}
	}


	protected IContentOutlinePage createOutlinePage() {
		return new ContentOutlinePage(new TreeViewer()) {
			
			public void createControl(Composite parent){
				super.createControl(parent);
				getEditDomain().addViewer(getViewer());				
				getViewer().setContents(createOutlineViewerContents());
				getSelectionSynchronizer().addViewer(getViewer());
			}
			
			public void dispose(){
				getSelectionSynchronizer().removeViewer(getViewer());
				getEditDomain().removeViewer(getViewer());		
				super.dispose();
			}						
		};
	}
	
	

	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		// Check input.
		if (!(input instanceof IFileEditorInput))
			throw new PartInitException(
				MessageFormat.format(CDEMessages.NOT_FILE_INPUT_ERROR_, new Object[] { input.getName()}));

		// Save input.
		org.eclipse.ve.internal.cde.core.EditDomain dom = new org.eclipse.ve.internal.cde.core.EditDomain(this);
		dom.setAnnotationLinkagePolicy(new CompanyAnnotationLinkagePolicy());		
		setEditDomain(dom);		
		setSite(site);
		setInput(input);
		getCommandStack().addCommandStackListener(fDirtyListener);
	}

	protected void initializeActionRegistry() {
		ActionRegistry registry = getActionRegistry();
		registry.registerAction(new UndoAction(this));
		registry.registerAction(new RedoAction(this));
		registry.registerAction(new DeleteAction((IWorkbenchPart) this));
		registry.registerAction(new SaveAction(this));

		registry.registerAction(new org.eclipse.ve.internal.cde.core.ZoomAction(this));
		registry.registerAction(new org.eclipse.ve.internal.cde.core.ZoomInAction(this));
		registry.registerAction(new org.eclipse.ve.internal.cde.core.ZoomOutAction(this));

		org.eclipse.ve.internal.cde.core.ShowGridAction sgAction = new org.eclipse.ve.internal.cde.core.ShowGridAction(this);
		registry.registerAction(sgAction);
		registry.registerAction(new org.eclipse.ve.internal.cde.core.GridPropertiesAction(this, sgAction));
		registry.registerAction(new org.eclipse.ve.internal.cde.core.SnapToGridAction(this, sgAction));
	}

	public void setInput(IEditorInput input) {
		super.setInput(input);
		company = null;
		org.eclipse.ve.internal.cde.core.EditDomain dom = (org.eclipse.ve.internal.cde.core.EditDomain) getEditDomain();
		ResourceSet rs = null;
		if (dom.getDiagramData() != null) {
			// Need to release the old diagram data.
			DiagramData dd = dom.getDiagramData();
			rs = dd.eResource().getResourceSet();
			rs.getResources().remove(dd.eResource());
			dom.setDiagramData(null);
		} else
			rs = createResourceSet();

		IFile file = ((IFileEditorInput) input).getFile();

		try {
			InputStream is = file.getContents(false);
			try {
				ObjectInputStream ois = new ObjectInputStream(is);
				try {
					company = ((Company) ois.readObject());
				} finally {
					ois.close();
				}
			} catch (EOFException e) {
				// Empty file, this is ok.
			}

			// Now read in the corresponding diagram data.
			EClass metaDD = CDMPackage.eINSTANCE.getDiagramData();
			IPath filePath = file.getFullPath().removeFileExtension(); 
			String ddPath = filePath.lastSegment();
			if (company == null) {
				// We don't have a company, create a dummy one.
				company = new Company();
				company.setName(ddPath);	// Use the filename.
			}			
			ddPath += "DialogData.xmi";
			filePath = filePath.removeLastSegments(1).append(ddPath);
			Resource ddRes = null;
			try {
				ddRes = rs.getResource(URI.createPlatformResourceURI(filePath.toString()), true);
			} catch (WrappedException e) {
				// Probably file not found exception. This is ok.
			}
			DiagramData dd = null;
			if (ddRes != null) {
				dd = (DiagramData) EcoreUtil.getObjectByType(ddRes.getContents(), metaDD);
			} 
			if (dd == null) {
				// Create an empty one that will be created upon save.
				dd = CDMFactory.eINSTANCE.createDiagramData();
				ddRes = rs.createResource(URI.createPlatformResourceURI(filePath.toString()));
				ddRes.getContents().add(dd);
			}
			Diagram d = null;
			Iterator iter = dd.getDiagrams().iterator();
			while (iter.hasNext()) {
				Diagram element = (Diagram) iter.next();
				if (Diagram.PRIMARY_DIAGRAM_ID.equals(element.getId())) {
					d = element;
					break;
				}
			}
			if (d == null) {
				// We need a diagram to store the default graphical view data in
				// This will be added outside of command stack. If we don't change the company, then this
				// won't be saved. This is ok because it isn't really dirty until company has been changed.
				d = CDMFactory.eINSTANCE.createDiagram();
				d.setId(Diagram.PRIMARY_DIAGRAM_ID);
				dd.getDiagrams().add(d);
			}
			((CompanyAnnotationLinkagePolicy) dom.getAnnotationLinkagePolicy()).setCompany(company);
			if (dd != null) {
				dom.setDiagramData(dd);
				dom.setViewerData(getGraphicalViewer(), org.eclipse.ve.internal.cde.core.EditDomain.DIAGRAM_KEY, d);
				// Put out the diagram for the viewer.
			}
		
		} catch (Exception e) {
			//This is not a real application.  All exceptions caught here.
			e.printStackTrace();
		}

	}

	public void gotoMarker(IMarker marker) {
	}

	protected ResourceSet createResourceSet() {
		return new ResourceSetImpl();
	}

	/**
	 * @see AbstractGraphicalEditor#createGraphicalViewerContents()
	 */
	protected EditPart createGraphicalViewerContents() {
		return company != null ? new CompanyContentsGraphicalEditPart(company) : null;
	}

	/**
	 * @see AbstractGraphicalEditor#createOutlineViewerContents()
	 */
	protected EditPart createOutlineViewerContents() {
		return company != null ? new CompanyContentsTreeEditPart(company) : null;
	}

	/**
	 * @see AbstractGraphicalEditor#getGraphicalViewerPartFactory()
	 */
	protected EditPartFactory getGraphicalViewerPartFactory() {
		return null;
	}

	/**
	 * @see AbstractGraphicalEditor#getOutlineViewerPartFactory()
	 */
	protected EditPartFactory getOutlineViewerPartFactory() {
		return null;
	}

	/**
	 * @see IEditorPart#doSaveAs()
	 */
	public void doSaveAs() {
		performSaveAs();
	}

	/**
	 * @see IWorkbenchPart#setFocus()
	 */
	public void setFocus() {
	}

	/**
	 * @see org.eclipse.gef.ui.parts.GraphicalEditor#initializeGraphicalViewer()
	 */
	protected void initializeGraphicalViewer() {
		EditPartViewer viewer = getGraphicalViewer();
		org.eclipse.ve.internal.cde.core.EditDomain editDomain = (org.eclipse.ve.internal.cde.core.EditDomain) getEditDomain();		
		Diagram d = null;
		Iterator iter = editDomain.getDiagramData().getDiagrams().iterator();
		while (iter.hasNext()) {
			Diagram element = (Diagram) iter.next();
			if (Diagram.PRIMARY_DIAGRAM_ID.equals(element.getId())) {
				d = element;
				break;
			}
		}
		if (d == null) {
			// We need a diagram to store the default graphical view data in
			// This will be added outside of command stack. If we don't change the company, then this
			// won't be saved. This is ok because it isn't really dirty until company has been changed.
			d = CDMFactory.eINSTANCE.createDiagram();
			d.setId(Diagram.PRIMARY_DIAGRAM_ID);
			editDomain.getDiagramData().getDiagrams().add(d);
		}
		editDomain.setViewerData(viewer, org.eclipse.ve.internal.cde.core.EditDomain.DIAGRAM_KEY, d);
		viewer.setRootEditPart(new FreeformGraphicalRootEditPart());		
		viewer.setContents(createGraphicalViewerContents());
		editDomain.addViewer(viewer);
		(editDomain).setViewerData(viewer, org.eclipse.ve.internal.cde.core.ZoomController.ZOOM_KEY, new org.eclipse.ve.internal.cde.core.ZoomController());
		(editDomain).setViewerData(viewer, org.eclipse.ve.internal.cde.core.GridController.GRID_KEY, new org.eclipse.ve.internal.cde.core.GridController());
		(editDomain).setViewerData(
			viewer,
			org.eclipse.ve.internal.cde.core.DistributeController.DISTRIBUTE_KEY,
			new org.eclipse.ve.internal.cde.core.DistributeController((GraphicalViewer) viewer));				
	}

}
