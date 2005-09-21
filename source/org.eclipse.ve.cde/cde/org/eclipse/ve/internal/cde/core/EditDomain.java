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
package org.eclipse.ve.internal.cde.core;
/*
 *  $RCSfile: EditDomain.java,v $
 *  $Revision: 1.13 $  $Date: 2005-09-21 23:09:01 $ 
 */

import java.text.MessageFormat;
import java.util.*;
import java.util.logging.Level;

import org.eclipse.core.runtime.*;
import org.eclipse.gef.*;
import org.eclipse.gef.palette.*;
import org.eclipse.gef.ui.palette.PaletteViewer;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.IActionFilter;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.views.properties.IPropertyDescriptor;

import org.eclipse.ve.internal.cdm.Diagram;
import org.eclipse.ve.internal.cdm.DiagramData;

import org.eclipse.ve.internal.cde.commands.AddAnnotationsCommand;
import org.eclipse.ve.internal.cde.properties.PropertySourceAdapter;
import org.eclipse.ve.internal.cde.rules.IRuleRegistry;

import org.eclipse.ve.internal.propertysheet.INeedData;
/**
 * Common Diagram Editor EditDomain.
 * 
 * Data added to generic data and viewer data will be disposed (if implements ICDEDisposable) when dispose() is called.
 */
public class EditDomain extends DefaultEditDomain {
	
	public static final String DIAGRAM_KEY = "org.eclipse.ve.internal.cde.core.diagramkey";	// The key to use in getViewerData to return the Diagram object for that viewer. //$NON-NLS-1$
	protected List viewers = new ArrayList(2);
	protected List toolListeners;
	public ToolInfo toolInfo;
	
	public static interface ToolChangedListener{
		public void toolChanged();
	}
	public static class ToolInfo{
		public ToolInfo(String aMsg, Image anImage) {
			msg = aMsg;
			image = anImage;
		}
		public Image image;
		public String msg;
	}
	
	/**
	 * A utility method to get the edit domain from an EditPart.
	 */
	public static EditDomain getEditDomain(EditPart ep) {
		return (EditDomain) ep.getRoot().getViewer().getEditDomain();
	}
	
	private Map imageCache;	// Cache of ImageDescriptor to Image;
	private Image getImage(ImageDescriptor imageDescriptor){
		if (imageCache == null) imageCache = new HashMap(20);
		Image existingImage = (Image)imageCache.get(imageDescriptor);
		if(existingImage == null) {
			existingImage = imageDescriptor.createImage();
			imageCache.put(imageDescriptor,existingImage);
		}
		return existingImage;
	}
	
	public void setPaletteViewer(PaletteViewer palette) {
		super.setPaletteViewer(palette);
		palette.addPaletteListener(new PaletteListener(){
			public void activeToolChanged(PaletteViewer palette, ToolEntry tool) {
				if (tool instanceof CreationToolEntry){
					CreationToolEntry creationTool = (CreationToolEntry)tool;
					toolInfo = new ToolInfo(
							MessageFormat.format(CDEMessages.ActionContributor_Status_Creating_label_, new Object[]{creationTool.getLabel()}),
							getImage(creationTool.getSmallIcon()));
				} else {
					toolInfo = null;
				}
				if(toolListeners != null){
					Iterator iter = toolListeners.iterator();
					while(iter.hasNext()){
						((ToolChangedListener)iter.next()).toolChanged();
					}
				}				
			}			
		});
	}
	
	public void setActiveTool(Tool tool) {
		super.setActiveTool(tool);	
	}	
	
	public void addToolChangedListener(ToolChangedListener aToolChangedListener){
		if(toolListeners == null){
			toolListeners = new ArrayList(2);
		}
		toolListeners.add(aToolChangedListener);
	}
	
	public void removeToolChangedListener(ToolChangedListener aToolChangedListener){
		if(toolListeners != null){
			toolListeners.remove(aToolChangedListener);			
		}
	}
	
	/**
	 * A utility method to get the diagram for a viewer.
	 */
	public Diagram getDiagram(EditPartViewer viewer) {
		return (Diagram) getViewerData(viewer, DIAGRAM_KEY);
	}
	
	protected void finalize() {
		dispose();
	}
	
	/**
	 * Get the list of EditPartViewers registered with this domain.
	 */
	public Collection getViewers() {
		return Collections.unmodifiableCollection(viewers);
	}
	
	/**
	 * Call when it is time to go away.
	 */
	public void dispose() {
		if (getAnnotationLinkagePolicy() != null) {
			getAnnotationLinkagePolicy().dispose();
			linkageHelper = null;
		}
		
		if (genericData != null) {
			disposeCollection(genericData.values());
		}
		
		if (viewerData != null) {
			Iterator itr = viewerData.values().iterator();
			while (itr.hasNext()) {
				disposeCollection(((HashMap) itr.next()).values());
			}
		}
		if(imageCache != null){
			Iterator images = imageCache.values().iterator();
			while(images.hasNext()){
				((Image)images.next()).dispose();
			}
		}
	}

	protected void disposeCollection(Collection c) {
		Iterator itr = c.iterator();
		while (itr.hasNext()) {
			Object next = itr.next();
			if (next instanceof ICDEDisposable)
				((ICDEDisposable) next).dispose();
		}
	}
	
	protected HashMap genericData;	// Key/value of generic data stored for the entire edit domain
	protected HashMap viewerData;		// Key/value of generic data on a per viewer basis. Each key will be
						//  a viewer and the value will be another HashMap of the key/values for that viewer.
	protected AnnotationLinkagePolicy linkageHelper;
	protected DiagramData diagramData;
	protected HashMap annotationDescriptors;	// Map between a Key (in annotation) and the PropertyDescriptor to use for that key.
	
	protected Class defaultAddAnnotationsCommand = AddAnnotationsCommand.class;	// Default add annotations command.
	private IRuleRegistry ruleRegistry;
	
	public EditDomain(IEditorPart editorPart) {
		super(editorPart);
	}
	
	/**
	 * Get the annotation linkage helper for this domain.
	 */
	public AnnotationLinkagePolicy getAnnotationLinkagePolicy() {
		return linkageHelper;
	}
	
	/**
	 * Set the annotation linkage policy for this domain.
	 *
	 * It is assumed that once set up, another policy is not set.
	 * This could cause linkage problems, so this will throw an
	 * exception in that case.
	 */
	public void setAnnotationLinkagePolicy(AnnotationLinkagePolicy helper) {
		if (linkageHelper != null)
			throw new IllegalStateException();
			
		linkageHelper = helper;
	}
	
	/**
	 * Get the diagram data for this domain.
	 */
	public DiagramData getDiagramData() {
		return diagramData;
	}
	
	/**
	 * Set the diagram data for this domain.
	 * It must be set AFTER the annotation linkage policy.
	 *
	 * It will throw an exception if not.
	 */
	public void setDiagramData(DiagramData dd) {
		if (linkageHelper == null)
			throw new IllegalStateException();
			
		// Clear out any DIAGRAM from all viewers.
		if (viewerData != null) {
			Iterator itr = viewerData.values().iterator();
			while (itr.hasNext()) {
				HashMap value = (HashMap) itr.next();
				value.remove(DIAGRAM_KEY);
			}
		}
		
		diagramData = dd;
		if (diagramData != null)
			linkageHelper.initializeLinkages(diagramData);
	}
	
	/**
	 * Get the PropertyDescriptor for an annotation key.
	 * Return null if key not registered.
	 */
	public IPropertyDescriptor getKeyedPropertyDescriptor(Object key) {
		return (annotationDescriptors != null) ?
			(IPropertyDescriptor) annotationDescriptors.get(key) : null;
	}
	
	/**
	 * Register a descriptor for the annotation key.
	 *
	 * Note: It is important that the descriptors are ICommandPropertyDescriptor and ISourcedPropertyDescriptor.
	 * This is because these descriptors will be given the model object as the source,
	 * and what is wanted is the annotation. Use AbstractAnnotationPropertyDescriptor
	 * as a base class to supply most of what is needed.
	 */
	public void registerKeyedPropertyDescriptor(String key, IPropertyDescriptor descriptor) {
		if (annotationDescriptors == null)
			annotationDescriptors = new HashMap(5);
		if (descriptor instanceof INeedData)
			((INeedData) descriptor).setData(this);
		annotationDescriptors.put(key, descriptor);
	}
	
	/**
	 * Unregister a descriptor for the annotation key.
	 */
	public void unregisterKeyedPropertyDescriptor(String key) {
		if (annotationDescriptors != null)
			annotationDescriptors.remove(key);
	}
	
	/**
	 * @see org.eclipse.gef.EditDomain#addViewer(EditPartViewer)
	 */
	public void addViewer(EditPartViewer viewer) {
		super.addViewer(viewer);
		viewers.add(viewer);
	}
		
	public void removeViewer(EditPartViewer viewer) {
		super.removeViewer(viewer);
		viewers.remove(viewer);
		// Remove any data associated with this viewer.
		if (viewerData != null) {
			Map m = (Map) viewerData.remove(viewer);
			if (m != null)
				disposeCollection(m.values());
		}
	}
	
	/**
	 * Get data for the specified key. Return null if the
	 * key is not set.
	 */
	public Object getData(Object key) {
		if (genericData != null)
			return genericData.get(key);
		return null;
	}
	
	/**
	 * Set the data for the specified key.
	 */
	public void setData(Object key, Object data) {
		if (genericData == null)
			genericData = new HashMap(3);
		genericData.put(key, data);
	}
	
	/**
	 * Remove the data for the specified key.
	 * Return the old value. Return null if not set.
	 */	
	public Object removeData(Object key) {
		if (genericData != null)
			return genericData.remove(key);
		return null;
	}
	
	/**
	 * Get the data for the specified key for a particular viewer.
	 * Return null if the key is not set.
	 */
	public Object getViewerData(EditPartViewer viewer, Object key) {
		if (viewerData != null) {
			HashMap data = (HashMap) viewerData.get(viewer);
			if (data != null)
				return data.get(key);
		}
		return null;
	}
	
	/**
	 * Set the data for the specified key for a particular viewer.
	 */
	public void setViewerData(EditPartViewer viewer, Object key, Object data) {
		if (viewerData == null)
			viewerData = new HashMap(3);
			
		HashMap vdata = (HashMap) viewerData.get(viewer);
		if (vdata == null)
			viewerData.put(viewer, vdata = new HashMap(3));
		vdata.put(key, data);
	}
	
	/**
	 * Remove the data for this key for the viewer, return the old value.
	 * If not set, the old value will be null.
	 */
	public Object removeViewerData(EditPartViewer viewer, Object key) {
		if (viewerData != null) {
			HashMap vdata = (HashMap) viewerData.get(viewer);
			if (vdata != null)
				return vdata.remove(key);
		}
		return null;
	}
	
	/**
	 * Set the class to be used as the default add annotations to diagram data command.
	 * An new instance is returned by the AnnotationPolicy.getDefaultAddAnnotationsCommand.
	 * 
	 * The class must be a either the AddAnnotationsCommand or one of its subclasses and
	 * it must have a default ctor.
	 */
	public void setDefaultAddAnnotationsCommandClass(Class defaultAdd) throws NoSuchMethodException, ClassCastException {
		if (!AddAnnotationsCommand.class.isAssignableFrom(defaultAdd))
			throw new ClassCastException(defaultAdd.toString());
		defaultAdd.getConstructor(new Class[0]);
		
		this.defaultAddAnnotationsCommand = defaultAdd;
	}
	
	/**
	 * Return the default add annotations command.
	 */
	public Class getDefaultAddAnnotationsCommandClass() {
		return defaultAddAnnotationsCommand;
	}

	public PaletteRoot getPaletteRoot(){
		return getPaletteViewer().getPaletteRoot();
	}
	
	/**
	 * The rule registry allows customization of function via rules.
	 * If your application doesn't require such customization, then
	 * you don't need to have a rule registry. Make sure thought that
	 * any CDE function that uses the rule registry isn't used by
	 * your application. Each class in CDE will indicate if it requires
	 * the rule registry (and which rule).
	 */
	public void setRuleRegistry(IRuleRegistry ruleRegistry) {
		this.ruleRegistry = ruleRegistry;
	}

	public IRuleRegistry getRuleRegistry() {
		return ruleRegistry;
	}
	/**
	 * API to allow a model object to be selected that generates selection in the viewers, but without a GUI selection change
	 * This is useful if there is no edit part but we still want model selection to drive downstream code listening to event changes
	 */
	public void selectModel(Object model){
		if(getEditorPart() instanceof DirectSelectionInput){
			((DirectSelectionInput)getEditorPart()).modelSelected(model);
		}
	}
	
	private IConfigurationElement[] configElements;
	private AdaptableContributorFactory[] contributorFactories;
	// This factory is used whenever a factory could not be successfully created so that it doesn't spend time trying each time.
	// It is a factory that will return no contributors.
	private static AdaptableContributorFactory INVALID_FACTORY = new AdaptableContributorFactory(){
	
		public GraphicalEditPartContributor getGraphicalEditPartContributor(GraphicalEditPart graphicalEditPart) {
			return null;
		}
	
		public TreeEditPartContributor getTreeEditPartContributor(TreeEditPart treeEditPart) {
			return null;
		}

		public PropertySourceContributor getPropertySourceContributor(PropertySourceAdapter propertySourceAdapter) {
			return null;
		}
	
	};
	
	
	public List getContributors(IAdaptable anAdaptable) {
		if(configElements == null){
			IExtensionPoint extp = Platform.getExtensionRegistry().getExtensionPoint("org.eclipse.ve.cde.contributor");
			IExtension[] extensions = extp.getExtensions();
			List configElementsList = new ArrayList();
			for (int i = 0; i < extensions.length; i++) {
				IConfigurationElement[] configurationElements = extensions[i].getConfigurationElements();
				for (int j = 0; j < configurationElements.length; j++) {
					if ("contributor".equals(configurationElements[j].getName()))
						configElementsList.add(configurationElements[j]);
				}
			}
			configElements = (IConfigurationElement[]) configElementsList.toArray(new IConfigurationElement[configElementsList.size()]);
			contributorFactories = new AdaptableContributorFactory[configElements.length];
		}
		IActionFilter actionFilter = (IActionFilter) anAdaptable.getAdapter(IActionFilter.class);
		// Now we have some of the configElements get their filter
		List result = new ArrayList(1);
		for (int i = 0; i < configElements.length; i++) {
			IConfigurationElement configElement = configElements[i];
			IConfigurationElement[] filters = configElement.getChildren("filter");
			for (int j = 0; j < filters.length; j++) {
				String name = filters[j].getAttribute("name");
				String value = filters[j].getAttribute("value");
				if (actionFilter.testAttribute(anAdaptable,name,value)){
					// The edit part wishes to be contributed to.  See if we have a contributor existing for this, or if not create one 
					AdaptableContributorFactory editPartContributorFactory = contributorFactories[i];
					if(editPartContributorFactory == null){
						try {
							editPartContributorFactory = contributorFactories[i] = (AdaptableContributorFactory) configElement.createExecutableExtension("class");
						} catch (CoreException e) {
							CDEPlugin.getPlugin().getLogger().log(e,Level.WARNING);
							contributorFactories[i] = INVALID_FACTORY;
						}
					}
					if (editPartContributorFactory != null)
						result.add(editPartContributorFactory);
				}
			}			
		}
		return result;
	}
}
