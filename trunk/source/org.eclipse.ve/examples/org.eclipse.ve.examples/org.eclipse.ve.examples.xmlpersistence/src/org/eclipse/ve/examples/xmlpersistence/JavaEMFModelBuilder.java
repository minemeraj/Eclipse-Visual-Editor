package org.eclipse.ve.examples.xmlpersistence;

import java.io.ByteArrayInputStream;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cde.emf.EMFEditDomainHelper;
import org.eclipse.ve.internal.cdm.Diagram;
import org.eclipse.ve.internal.java.codegen.core.IDiagramModelBuilder;
import org.eclipse.ve.internal.java.codegen.java.ISynchronizerListener;
import org.eclipse.ve.internal.java.codegen.util.CodeGenException;
import org.eclipse.ve.internal.jcm.BeanComposition;
import org.eclipse.ve.internal.jcm.BeanSubclassComposition;
import org.eclipse.ve.internal.jcm.JCMFactory;
import org.eclipse.ve.internal.jcm.JCMPackage;

public class JavaEMFModelBuilder implements IDiagramModelBuilder {
	
	private EditDomain fEditDomain;
	private IFile fFile;
	private BeanComposition beanComposition;

	public JavaEMFModelBuilder(EditDomain editDomain){
		fEditDomain = editDomain;
	}

	public void loadModel(IFileEditorInput file, boolean useCache,IProgressMonitor pm) throws CodeGenException {
		fFile = file.getFile();
		
		try {
			ResourceSet resourceSet = EMFEditDomainHelper.getResourceSet(fEditDomain);		
			Resource resource = resourceSet.createResource(null);			
			ByteArrayInputStream is = new ByteArrayInputStream(fFile.getContents().toString().getBytes());
			resource.load(is, null);	
			// If we don't have a BeanSubclassComposition create one
			List contents = resource.getContents();
			if(contents.isEmpty()){
				beanComposition = JCMFactory.eINSTANCE.createBeanComposition();
				contents.add(beanComposition);
			} else {
				beanComposition = (BeanComposition) contents.get(0);
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	

	}

	public BeanComposition getModelRoot() {
		// TODO Auto-generated method stub
		return null;
	}

	public Diagram getDiagram() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getThisTypeName() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isBusy() {
		// TODO Auto-generated method stub
		return false;
	}

	public void dispose() {
		// TODO Auto-generated method stub

	}

	public void startTransaction() {
		// TODO Auto-generated method stub

	}

	public void commit() {
		// TODO Auto-generated method stub

	}

	public void commitAndFlush(ISynchronizerListener listener, String marker) {
		// TODO Auto-generated method stub

	}

	public void setSynchronizerSyncDelay(int delay) {
		// TODO Auto-generated method stub

	}

	public boolean pause() {
		// TODO Auto-generated method stub
		return false;
	}

	public void addIBuilderListener(IBuilderListener l) {
		// TODO Auto-generated method stub

	}

	public void removeIBuilderListener(IBuilderListener l) {
		// TODO Auto-generated method stub

	}

	public void doSave(IProgressMonitor monitor) {
		// TODO Auto-generated method stub

	}

	public void waitforNotBusy(boolean cancelJobs) {
		// TODO Auto-generated method stub

	}

}
