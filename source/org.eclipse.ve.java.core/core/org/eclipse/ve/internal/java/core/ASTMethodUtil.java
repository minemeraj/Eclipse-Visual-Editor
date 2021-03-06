/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.java.core;

import java.util.*;
import java.util.logging.Level;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.*;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.Modifier.ModifierKeyword;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.jdt.ui.CodeGeneration;
import org.eclipse.jdt.ui.PreferenceConstants;
import org.eclipse.jface.text.*;
import org.eclipse.jface.util.Assert;
import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.text.edits.TextEdit;

import org.eclipse.jem.internal.instantiation.InstantiationFactory;
import org.eclipse.jem.internal.instantiation.PTAnonymousClassDeclaration;
import org.eclipse.jem.java.JavaHelpers;


/**
 * Internal utilities class to return unimplemented methods.
 * 
 * @since 1.2.0
 */
public class ASTMethodUtil{
	
	public static PTAnonymousClassDeclaration createAnonymousDeclaration(JavaHelpers jc, ICompilationUnit icu) {

		// Create a dummy ICompilationUnit in the same place the incoming is in. We don't want to actually parse the
		// incoming icu. Too expensive for what we want.
		IResource cures;
		try {
			cures = icu.getCorrespondingResource();
		} catch (JavaModelException e) {
			return null;
		}
		IFile newcuFile = cures.getParent().getFile(new Path("Tmp.java")); //$NON-NLS-1$
		ICompilationUnit dummyicu = JavaCore.createCompilationUnitFrom(newcuFile);
		
		IDocument tmpDoc = new Document("import " + jc.getQualifiedName() + ";"+ //$NON-NLS-1$ //$NON-NLS-2$
					"public class Tmp{" + //$NON-NLS-1$
					"public void main(){" + //$NON-NLS-1$
						"new "+ jc.getName() + "(){};" + //$NON-NLS-1$ //$NON-NLS-2$
					"}" + //$NON-NLS-1$
				"}"); //$NON-NLS-1$
		
		ASTParser parser = ASTParser.newParser(AST.JLS3);
		parser.setUnitName("Tmp"); //$NON-NLS-1$
		parser.setCompilerOptions(null);
		parser.setProject(dummyicu.getJavaProject());
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setResolveBindings(true);
		parser.setSource(tmpDoc.get().toCharArray());
		ASTNode node = parser.createAST(null);
		
		ASTRewrite rewriter = ASTRewrite.create(node.getAST());
		CompilationUnit cu = (CompilationUnit) node;
		AbstractTypeDeclaration atd = (AbstractTypeDeclaration) cu.types().get(0);
		MethodDeclaration mtd = (MethodDeclaration) atd.bodyDeclarations().get(0);
		ExpressionStatement stmt = (ExpressionStatement) mtd.getBody().statements().get(0);
		ClassInstanceCreation cic = (ClassInstanceCreation) stmt.getExpression();
		ITrackedNodePosition cicLocation = rewriter.track(cic);
		ITypeBinding typeBinding = cic.resolveTypeBinding();
		PTAnonymousClassDeclaration anonDecl = InstantiationFactory.eINSTANCE.createPTAnonymousClassDeclaration();
		List importList = anonDecl.getImports();
		importList.add(jc.getJavaName());
		if(typeBinding!=null){
			IMethodBinding[] unimplementedMethods = ASTMethodUtil.getUnimplementedMethods(typeBinding);
			if(unimplementedMethods!=null){
				ListRewrite anonBodyDecls = rewriter.getListRewrite(cic.getAnonymousClassDeclaration(), AnonymousClassDeclaration.BODY_DECLARATIONS_PROPERTY);
				for (int uic = 0; uic < unimplementedMethods.length; uic++) {
					MethodDeclaration md = ASTMethodUtil.createMethodDeclaration(unimplementedMethods[uic], dummyicu, jc.getName(), rewriter, importList);
					anonBodyDecls.insertLast(md, null);
				}
			}
		}
		TextEdit edits = rewriter.rewriteAST(tmpDoc, icu.getJavaProject().getOptions(true));
		try {
			edits.apply(tmpDoc);
			anonDecl.setDeclaration(tmpDoc.get(cicLocation.getStartPosition(), cicLocation.getLength()));
		} catch (MalformedTreeException e) {
		} catch (BadLocationException e) {
		}
		return anonDecl;
		
	}

	private static IMethodBinding findMethodBinding(IMethodBinding method, List allMethods) {
		for (int i= 0; i < allMethods.size(); i++) {
			IMethodBinding curr= (IMethodBinding) allMethods.get(i);
			if (isEqualMethod(method, curr.getName(), curr.getParameterTypes())) {
				return curr;
			}
		}
		return null;
	}

	private static Type createTypeFromBinding(ITypeBinding binding, AST ast, List importList){
		Type type = null;
		if(binding!=null){
			if(binding.isPrimitive()){
				PrimitiveType pt = ast.newPrimitiveType(PrimitiveType.toCode(binding.getQualifiedName()));
				type = pt;
			}else if(binding.isArray()){
				ArrayType at = ast.newArrayType(createTypeFromBinding(binding.getElementType(), ast, importList), binding.getDimensions());
				type = at;
			}else {
				importList.add(binding.getQualifiedName());
				type = ast.newSimpleType(ast.newName(binding.getName()));
			}
		}
		return type;
	}

	private static boolean isVisibleInHierarchy(IMethodBinding member, IPackageBinding pack) {
		int otherflags= member.getModifiers();
		ITypeBinding declaringType= member.getDeclaringClass();
		if (Modifier.isPublic(otherflags) || Modifier.isProtected(otherflags) || (declaringType != null && declaringType.isInterface())) {
			return true;
		} else if (Modifier.isPrivate(otherflags)) {
			return false;
		}		
		return pack == declaringType.getPackage();
	}

	private static void findUnimplementedInterfaceMethods(ITypeBinding typeBinding, HashSet visited, ArrayList allMethods, IPackageBinding currPack, ArrayList toImplement) {
		if (visited.add(typeBinding)) {
			IMethodBinding[] typeMethods= typeBinding.getDeclaredMethods();
			for (int i= 0; i < typeMethods.length; i++) {
				IMethodBinding curr= typeMethods[i];
				IMethodBinding impl= findMethodBinding(curr, allMethods);
				if (impl == null || !isVisibleInHierarchy(impl, currPack) || ((curr.getExceptionTypes().length < impl.getExceptionTypes().length) && !Modifier.isFinal(impl.getModifiers()))) {
					if (impl != null)
						allMethods.remove(impl);
					toImplement.add(curr);
					allMethods.add(curr);
				}
			}
			ITypeBinding[] superInterfaces= typeBinding.getInterfaces();
			for (int i= 0; i < superInterfaces.length; i++)
				findUnimplementedInterfaceMethods(superInterfaces[i], visited, allMethods, currPack, toImplement);
		}
	}

	public static IMethodBinding[] getUnimplementedMethods(ITypeBinding typeBinding) {
		ArrayList allMethods= new ArrayList();
		ArrayList toImplement= new ArrayList();
	
		IMethodBinding[] typeMethods= typeBinding.getDeclaredMethods();
		for (int i= 0; i < typeMethods.length; i++) {
			IMethodBinding curr= typeMethods[i];
			int modifiers= curr.getModifiers();
			if (!curr.isConstructor() && !Modifier.isStatic(modifiers) && !Modifier.isPrivate(modifiers)) {
				allMethods.add(curr);
			}
		}
	
		ITypeBinding superClass= typeBinding.getSuperclass();
		while (superClass != null) {
			typeMethods= superClass.getDeclaredMethods();
			for (int i= 0; i < typeMethods.length; i++) {
				IMethodBinding curr= typeMethods[i];
				int modifiers= curr.getModifiers();
				if (!curr.isConstructor() && !Modifier.isStatic(modifiers) && !Modifier.isPrivate(modifiers)) {
					if (findMethodBinding(curr, allMethods) == null) {
						allMethods.add(curr);
					}
				}
			}
			superClass= superClass.getSuperclass();
		}
	
		for (int i= 0; i < allMethods.size(); i++) {
			IMethodBinding curr= (IMethodBinding) allMethods.get(i);
			int modifiers= curr.getModifiers();
			if ((Modifier.isAbstract(modifiers) || curr.getDeclaringClass().isInterface()) && (typeBinding != curr.getDeclaringClass())) {
				// implement all abstract methods
				toImplement.add(curr);
			}
		}
	
		HashSet visited= new HashSet();
		ITypeBinding curr= typeBinding;
		while (curr != null) {
			ITypeBinding[] superInterfaces= curr.getInterfaces();
			for (int i= 0; i < superInterfaces.length; i++) {
				findUnimplementedInterfaceMethods(superInterfaces[i], visited, allMethods, typeBinding.getPackage(), toImplement);
			}
			curr= curr.getSuperclass();
		}
	
		return (IMethodBinding[]) toImplement.toArray(new IMethodBinding[toImplement.size()]);
	}

	private static boolean isEqualMethod(IMethodBinding method, String methodName, ITypeBinding[] parameters) {
		if (!method.getName().equals(methodName))
			return false;
			
		ITypeBinding[] methodParameters= method.getParameterTypes();
		if (methodParameters.length != parameters.length)
			return false;
		for (int i= 0; i < parameters.length; i++) {
			if (!equals(methodParameters[i].getErasure(), parameters[i].getErasure()))
				return false;
		}
		return true;
	}

	/**
	 * Checks if the two bindings are equals. First an identity check is
	 * made an then the key of the bindings are compared. 
	 * @param b1 first binding treated as <code>this</code>. So it must
	 *  not be <code>null</code>
	 * @param b2 the second binding.
	 * @return boolean
	 */
	private static boolean equals(IBinding b1, IBinding b2) {
		boolean isEqualTo= b1.isEqualTo(b2);
		boolean originalEquals= originalEquals(b1, b2);
		if (originalEquals != isEqualTo) {
			String message= "Unexpected difference between Bindings.equals(..) and IBinding#isEqualTo(..)"; //$NON-NLS-1$
			String detail= "\nb1 == " + b1.getKey() + ",\nb2 == " + (b2 == null ? "null binding" : b2.getKey()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			try {
				detail+= "\nb1.getJavaElement() == " + b1.getJavaElement() + ",\nb2.getJavaElement() == " + (b2 == null ? "null binding" : b2.getJavaElement().toString()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			} catch (Exception e) {
				detail += "\nException in getJavaElement():\n" + e; //$NON-NLS-1$
			}
			JavaVEPlugin.log(new Status(IStatus.WARNING, JavaVEPlugin.PLUGIN_ID, IStatus.WARNING, message + detail, null));
		}
		return isEqualTo;
	}

	private static boolean originalEquals(IBinding b1, IBinding b2) {
		Assert.isNotNull(b1);
		if (b1 == b2)
			return true;
		if (b2 == null)
			return false;		
		String k1= b1.getKey();
		String k2= b2.getKey();
		if (k1 == null || k2 == null)
			return false;
		return k1.equals(k2);
	}
	
	public static MethodDeclaration createMethodDeclaration(IMethodBinding binding, ICompilationUnit icu, String declaringTypename, ASTRewrite rewriter, List importList) {
		AST ast = rewriter.getAST();
		MethodDeclaration md = ast.newMethodDeclaration();

		md.setName(ast.newSimpleName(binding.getName()));

		md.setReturnType2(createTypeFromBinding(binding.getReturnType(), ast, importList));

		md.setBody(ast.newBlock());

		if (Modifier.isProtected(binding.getModifiers()))
			md.modifiers().add(ast.newModifier(ModifierKeyword.PROTECTED_KEYWORD));
		else
			md.modifiers().add(ast.newModifier(ModifierKeyword.PUBLIC_KEYWORD));

		ITypeBinding[] bindingParams = binding.getParameterTypes();
		String[] names = suggestParameterNames(binding);
		for (int bpc = 0; bpc < bindingParams.length; bpc++) {
			SingleVariableDeclaration svd = ast.newSingleVariableDeclaration();
			String name = names[bpc];
			svd.setName(ast.newSimpleName(name));
			svd.setType(createTypeFromBinding(bindingParams[bpc], ast, importList));
			md.parameters().add(svd);
		}

		Statement body = null;
		if (md.getReturnType2().isPrimitiveType()) {
			if (PrimitiveType.VOID.equals(((PrimitiveType) md.getReturnType2()).getPrimitiveTypeCode())) {
				// void return type - no need to put any return
			} else if (PrimitiveType.BOOLEAN.equals(((PrimitiveType) md.getReturnType2()).getPrimitiveTypeCode())) {
				// boolean return - put in a return statement of false;
				ReturnStatement rs = ast.newReturnStatement();
				rs.setExpression(ast.newBooleanLiteral(false));
				body = rs;
			} else {
				ReturnStatement rs = ast.newReturnStatement();
				rs.setExpression(ast.newNumberLiteral("-1")); //$NON-NLS-1$
				body = rs;
			}
		} else {
			ReturnStatement rs = ast.newReturnStatement();
			rs.setExpression(ast.newNullLiteral());
			body = rs;
		}

		if (body != null) {
			try {
				String bodyContent = CodeGeneration.getMethodBodyContent(icu, declaringTypename, md.getName().getIdentifier(), false, body.toString(), "\n"); //$NON-NLS-1$
				if (bodyContent != null) {
					ASTNode todoNode= rewriter.createStringPlaceholder(bodyContent, ASTNode.RETURN_STATEMENT);
					md.getBody().statements().add(todoNode);
				}
			} catch (CoreException e) {
			}
		}
		
		if (Boolean.valueOf(PreferenceConstants.getPreference(PreferenceConstants.CODEGEN_ADD_COMMENTS, icu.getJavaProject())).booleanValue()) {
			try {
				String methodComment = CodeGeneration.getMethodComment(icu, declaringTypename, md, binding, "\n"); //$NON-NLS-1$
				if (methodComment != null) {
					Javadoc javadoc= (Javadoc) rewriter.createStringPlaceholder(methodComment, ASTNode.JAVADOC);
					md.setJavadoc(javadoc);
				}
			} catch (CoreException e) {
			}
		}
		return md;
	}

	private static String[] suggestParameterNames(IMethodBinding methodBinding) {
		String[] names = new String[methodBinding.getParameterTypes().length];
		if(names.length>0){
			IJavaElement je = methodBinding.getMethodDeclaration().getJavaElement();
			boolean gotNames = false;
			if(je!=null && je instanceof IMethod){
				try {
					IMethod method = (IMethod) je;
					String[] mns;
					mns = method.getParameterNames();
					Assert.isTrue(mns.length==names.length);
					System.arraycopy(mns, 0, names, 0, mns.length);
					gotNames = true;
				} catch (JavaModelException e) {
					JavaVEPlugin.log(e, Level.FINE);
				}
			}
			
			if(!gotNames){
				int count = 1;
				ITypeBinding[] paramTypes = methodBinding.getParameterTypes();
				for (int pc = 0; pc < paramTypes.length; pc++) {
					String name = paramTypes[pc].getName();
					name = new String(new char[]{Character.toLowerCase(name.charAt(0))}) + (name.length()>1 ? name.substring(1): ""); //$NON-NLS-1$
					IStatus status = JavaConventions.validateFieldName(name);
					if(status.isOK()){
						names[pc] = name;
					}else{
						names[pc] = name + (count++);
					}
				}
			}
		}
		return names;
	}
}