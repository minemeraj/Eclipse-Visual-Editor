/*
 * Created on 02-Jan-04
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package org.eclipse.ve.internal.swt;

import java.util.List;

import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.gef.commands.Command;

import org.eclipse.jem.internal.instantiation.*;
import org.eclipse.jem.internal.instantiation.InstantiationFactory;
import org.eclipse.jem.internal.instantiation.PTClassInstanceCreation;
import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.jem.internal.instantiation.base.JavaInstantiation;

import org.eclipse.ve.internal.cde.commands.ApplyAttributeSettingCommand;
import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.java.core.JavaEditDomainHelper;
import org.eclipse.ve.internal.java.visual.VisualContainerPolicy;

import org.eclipse.ve.internal.propertysheet.common.commands.AbstractCommand;

/**
 * @author JoeWin
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class CompositeContainerPolicy extends VisualContainerPolicy {
	
	protected EReference sfLayoutData;
	
	public CompositeContainerPolicy(EditDomain domain) {
		super(JavaInstantiation.getSFeature(
			JavaEditDomainHelper.getResourceSet(domain), 
			SWTConstants.SF_COMPOSITE_CONTROLS), 
			domain);
		
		ResourceSet rset = JavaEditDomainHelper.getResourceSet(domain);
		sfLayoutData = JavaInstantiation.getReference(rset, SWTConstants.SF_CONTROL_LAYOUTDATA);
	}
	
	
	public Command getCreateCommand(Object child, Object positionBeforeChild) {
		Command result = super.getCreateCommand(child, positionBeforeChild);
		final IJavaObjectInstance javaChild = (IJavaObjectInstance)child;
		// If we already have a java allocation then check to see whether it is a prototype instance with a 
		// {parentComposite} that needs substituting with the real parent
		if(javaChild.getAllocation() != null){
			Command insertCorrectParentCommand = new AbstractCommand(){
				public void execute() {
					if(javaChild.getAllocation() != null){
						PTExpression expression = ((ParseTreeAllocation)javaChild.getAllocation()).getExpression();
						if(expression instanceof PTClassInstanceCreation){
							PTClassInstanceCreation classInstanceCreation = (PTClassInstanceCreation) expression;
							if(classInstanceCreation.getArguments().size() == 2){
								Object firstArgument = classInstanceCreation.getArguments().get(0);
								if(firstArgument instanceof PTName && ((PTName)firstArgument).getName().equals("{parentComposite}")){
									PTInstanceReference parentRef = InstantiationFactory.eINSTANCE.createPTInstanceReference();
									parentRef.setObject((IJavaObjectInstance)getContainer());
									classInstanceCreation.getArguments().remove(0);
									classInstanceCreation.getArguments().add(0,parentRef);
								}
							}
						} 			
					}
				}
				protected boolean prepare() {
					return true;
				}
			};
			return insertCorrectParentCommand.chain(result);
		} else {
			return createInitStringCommand((IJavaObjectInstance)child).chain(result);
		}
	}
		
	/**
	 * This is a temporary hack to add an initialization string (allocation) to a dropped component
	 * which contain a parsed tree referencing the parent.
	 * 
	 * Rich has not implemented a ref. parsed tree yet, so use this as a deprecated method
	 * 
	 * @param parent
	 * @return
	 * 
	 * @since 1.0.0
	 */
	
	private Command createInitStringCommand(IJavaObjectInstance child) {
					
		// Class Creation tree - new Foo(args[])
		PTClassInstanceCreation ic = InstantiationFactory.eINSTANCE.createPTClassInstanceCreation() ;
		ic.setType(child.getJavaType().getJavaName()) ;
		
		// set the arguments
		PTInstanceReference ir = InstantiationFactory.eINSTANCE.createPTInstanceReference() ;
		ir.setObject((IJavaObjectInstance)getContainer()) ;	
		PTFieldAccess fa = InstantiationFactory.eINSTANCE.createPTFieldAccess();	
		PTName name = InstantiationFactory.eINSTANCE.createPTName("org.eclipse.swt.SWT") ;
		fa.setField("NONE");
		fa.setReceiver(name) ;
		
		
		ic.getArguments().add(ir);
		ic.getArguments().add(fa) ;
		
		JavaAllocation alloc = InstantiationFactory.eINSTANCE.createParseTreeAllocation(ic);
		ApplyAttributeSettingCommand applyCmd = new ApplyAttributeSettingCommand();
		applyCmd.setTarget(child);
		applyCmd.setAttribute(child.eClass().getEStructuralFeature("allocation"));
		applyCmd.setAttributeSettingValue(alloc);	
		
		return applyCmd;		
		
	}

	public Command getCreateCommand(Object constraintComponent, Object childComponent, Object position) {
		// TODO Auto-generated method stub
		return null;
	}

	public Command getAddCommand(List componentConstraints, List childrenComponents, Object position) {
		// TODO Auto-generated method stub
		return null;
	}

}
