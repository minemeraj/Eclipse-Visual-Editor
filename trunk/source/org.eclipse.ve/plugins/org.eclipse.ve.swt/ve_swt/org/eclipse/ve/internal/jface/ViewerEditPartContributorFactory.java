package org.eclipse.ve.internal.jface;

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.gef.*;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.swt.graphics.Image;

import org.eclipse.jem.internal.instantiation.base.IJavaInstance;

import org.eclipse.ve.internal.cde.core.*;
import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cde.core.ImageFigure;
import org.eclipse.ve.internal.cde.emf.EMFCreationFactory;
import org.eclipse.ve.internal.cde.emf.InverseMaintenanceAdapter;
import org.eclipse.ve.internal.cde.emf.InverseMaintenanceAdapter.InverseReferenceEvent;
import org.eclipse.ve.internal.cde.emf.InverseMaintenanceAdapter.InverseReferenceListener;

import org.eclipse.ve.internal.java.core.JavaBeanGraphicalEditPart;

/**
 * Abstract Contributor factory for JFace Viewer.
 * <p>
 * Different viewer types should subclass this to provide their function.
 * 
 * @since 1.2.0
 */
public abstract class ViewerEditPartContributorFactory implements EditPartContributorFactory {

	/**
	 * Get the first viewer, if there is one, that is pointing to this control through the given feature.
	 * 
	 * @param control
	 * @param viewerControlFeature
	 * @return viewer or <code>null</code> if no viewers pointing to this control through the given feature.
	 * 
	 * @since 1.2.0
	 */
	public static IJavaInstance getViewer(IJavaInstance control, EReference viewerControlFeature) {
		return (IJavaInstance) ((InverseMaintenanceAdapter) EcoreUtil.getExistingAdapter(control, InverseMaintenanceAdapter.ADAPTER_KEY))
				.getFirstReferencedBy(viewerControlFeature);
	}

	/**
	 * Subclasses of the contributor factory should supply a subclass of this class for the TreeEditPartContributor.
	 * 
	 * @since 1.2.0
	 */
	protected static abstract class ViewerTreeEditPartContributor extends AbstractEditPartContributor implements TreeEditPartContributor {

		protected boolean hasViewer;

		protected IJavaInstance control;

		private InverseReferenceListener inverseListener;

		protected ViewerTreeEditPartContributor(TreeEditPart beanTreeEditPart, final EReference controlFeature) {
			this.control = (IJavaInstance) beanTreeEditPart.getModel();
			hasViewer = getViewer(control, controlFeature) != null;
			inverseListener = new InverseReferenceListener() {

				public void referenceRemoved(InverseReferenceEvent event) {
					if (event.getReference() == controlFeature) {
						// Get the now new first (if there is one). It could be that it is in the process of changing and codegen has done
						// the remove AFTER the add of the other new one.
						hasViewer = ((InverseMaintenanceAdapter) event.getSource()).getFirstReferencedBy(controlFeature) != null;
						notifyContributionChanged();
					}
				}

				public void referenceAdded(InverseReferenceEvent event) {
					if (event.getReference() == controlFeature) {
						// Get the now new first (if there is one). It could be that it is in the process of changing and codegen has done
						// the add AFTER the remove of the other new one.
						hasViewer = ((InverseMaintenanceAdapter) event.getSource()).getFirstReferencedBy(controlFeature) != null;
						notifyContributionChanged();
					}
				}

			};
			((InverseMaintenanceAdapter) EcoreUtil.getExistingAdapter(control, InverseMaintenanceAdapter.ADAPTER_KEY))
					.addInverseReferenceListener(inverseListener);
		}

		public void dispose() {
			((InverseMaintenanceAdapter) EcoreUtil.getExistingAdapter(control, InverseMaintenanceAdapter.ADAPTER_KEY))
					.removeInverseReferenceListener(inverseListener);
		}
	}

	/**
	 * Subclasses of the contributor factory should supply a subclass of this contributor for the graphical editpart contributor.
	 * 
	 * @since 1.2.0
	 */
	protected abstract static class ViewerGraphicalEditPartContributor extends AbstractEditPartContributor implements GraphicalEditPartContributor {

		protected IJavaInstance control;

		protected Object viewer;

		protected GraphicalEditPart controlEditPart;

		private InverseReferenceListener inverseListener;

		protected EReference controlFeature;

		public ViewerGraphicalEditPartContributor(GraphicalEditPart controlEditPart, final EReference controlFeature) {
			this.controlEditPart = controlEditPart;
			this.control = (IJavaInstance) controlEditPart.getModel();
			this.viewer = getViewer(control, controlFeature);
			this.controlFeature = controlFeature;
			inverseListener = new InverseReferenceListener() {

				public void referenceRemoved(InverseReferenceEvent event) {
					if (event.getRefBy() == ViewerGraphicalEditPartContributor.this.viewer && event.getReference() == controlFeature) {
						// Get the now new first (if there is one). It could be that it is in the process of changing and codegen has done
						// the remove AFTER the add of the other new one.
						ViewerGraphicalEditPartContributor.this.viewer = ((InverseMaintenanceAdapter) event.getSource())
								.getFirstReferencedBy(controlFeature);
						notifyContributionChanged();
					}
				}

				public void referenceAdded(InverseReferenceEvent event) {
					if (ViewerGraphicalEditPartContributor.this.viewer == null && event.getReference() == controlFeature) {
						// Get the now new first (if there is one). It could be that it is in the process of changing and codegen has done
						// the add AFTER the remove of the other new one.
						ViewerGraphicalEditPartContributor.this.viewer = ((InverseMaintenanceAdapter) event.getSource())
								.getFirstReferencedBy(controlFeature);
						notifyContributionChanged();
					}
				}

			};
			((InverseMaintenanceAdapter) EcoreUtil.getExistingAdapter(control, InverseMaintenanceAdapter.ADAPTER_KEY))
					.addInverseReferenceListener(inverseListener);
		}

		public void dispose() {
			((InverseMaintenanceAdapter) EcoreUtil.getExistingAdapter(control, InverseMaintenanceAdapter.ADAPTER_KEY))
					.removeInverseReferenceListener(inverseListener);
		}

		/**
		 * Return the viewer's graphical overlay image. This will not be disposed by this implementation. It must be maintained by subclass.
		 * 
		 * @return
		 * 
		 * @since 1.2.0
		 */
		protected abstract Image getViewerOverlayImage();

		public IFigure getFigureOverLay() {
			if (viewer == null)
				return null;
			Image image = getViewerOverlayImage();
			org.eclipse.draw2d.ImageFigure imageFigure = new org.eclipse.draw2d.ImageFigure(image);
			imageFigure.setBorder(new MarginBorder(2, 2, 0, 0));
			imageFigure.setLocation(new Point(-1, -1));
			imageFigure.setSize(imageFigure.getPreferredSize());
			imageFigure.setVisible(true);
			return imageFigure;
		}

		/**
		 * Return the viewer's graphical image in action bar. This will not be disposed by this implementation. It must be maintained by subclass.
		 * 
		 * @return
		 * 
		 * @since 1.2.0
		 */
		protected abstract Image getViewerImage();

		protected abstract String getActionTextForCreateViewerButton();

		public GraphicalEditPart[] getActionBarChildren() {
			// If this control has an associated Viewer, return an editpart with the viewer as it's model
			if (viewer != null)
				return new GraphicalEditPart[] { new JavaBeanGraphicalEditPart(viewer) {

					protected IFigure createFigure() {
						Label label = (Label) super.createFigure();
						ImageFigure fig = new ImageFigure();
						fig.setImage(getViewerImage());
						fig.add(fErrorIndicator);
						fig.setToolTip(label.getToolTip());
						fig.setCursor(Cursors.HAND);
						fig.setPreferredSize(fig.getPreferredSize().width + 1, fig.getPreferredSize().height);
						return fig;
					}

					protected void createEditPolicies() {
						super.createEditPolicies();
						installEditPolicy(EditPolicy.COMPONENT_ROLE, new ActionBarChildComponentPolicy(
								ViewerGraphicalEditPartContributor.this, control));
					}

					protected void setupLabelProvider() {
						// don't do anything here, we'll provide our own image.
					}
				}};

			// No viewer... return an action editpart that can be selected to promote this control to a viewer.
			else
				return new GraphicalEditPart[] { new ActionBarActionEditPart(getActionTextForCreateViewerButton()) {

					public void run() {
						// If the tree viewer already exists, just the contributions need to be refreshed
						if (viewer != null) {
							notifyContributionChanged();

						} else {
							// Create and execute commands to promote this Control to a JFace Viewer. The class that the control feature is owned by
							// would be the class to be created.
							CreateRequest cr = new CreateRequest();
							cr.setFactory(new EMFCreationFactory(controlFeature.getEContainingClass()));
							Command c = controlEditPart.getCommand(cr);
							if (c != null) {
								EditDomain.getEditDomain(controlEditPart).getCommandStack().execute(c);
								notifyContributionChanged();
							}
						}
					}
				}};
		}
	}

}