package org.eclipse.ve.internal.java.core;

import java.util.*;
import java.util.logging.Level;

import org.eclipse.core.runtime.*;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.Modifier.ModifierKeyword;
import org.eclipse.jface.util.Assert;

import org.eclipse.jem.internal.instantiation.InstantiationFactory;
import org.eclipse.jem.internal.instantiation.PTAnonymousClassDeclaration;
import org.eclipse.jem.java.JavaHelpers;


/**
 * Internal utilities class to return unimplemented methods.
 * 
 * @since 1.2.0
 */
public class ASTMethodUtil{
	
	public static PTAnonymousClassDeclaration createAnonymousDeclaration(JavaHelpers jc, IJavaProject javaproject) {

		// TODO Should use ASTRewrite and CodeGeneration.getMethodBodyContent() so that we can get the format of the unimplemented
		// methods using the templates of the java eclipse. Also the rewrite will give us a nice output instead of what
		// toString does (toString on an MethodDeclaration puts spaces where they shouldn't).
		
		String classSource = "import " + jc.getQualifiedName() + ";"+
					"public class Tmp{" +
					"public void main(){" +
						"new "+ jc.getName() + "(){};" +
					"}" +
				"}";
		
		ASTParser parser = ASTParser.newParser(AST.JLS3);
		parser.setUnitName(jc.getName());
		parser.setCompilerOptions(null);
		parser.setProject(javaproject);
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setResolveBindings(true);
		parser.setSource(classSource.toCharArray());
		ASTNode node = parser.createAST(new NullProgressMonitor());
		
		if (node instanceof CompilationUnit) {
			PTAnonymousClassDeclaration anonDecl = InstantiationFactory.eINSTANCE.createPTAnonymousClassDeclaration();
			List importList = anonDecl.getImports();
			importList.add(jc.getJavaName());
			CompilationUnit cu = (CompilationUnit) node;
			AbstractTypeDeclaration atd = (AbstractTypeDeclaration) cu.types().get(0);
			MethodDeclaration mtd = (MethodDeclaration) atd.bodyDeclarations().get(0);
			ExpressionStatement stmt = (ExpressionStatement) mtd.getBody().statements().get(0);
			ClassInstanceCreation cic = (ClassInstanceCreation) stmt.getExpression();
			ITypeBinding typeBinding = cic.resolveTypeBinding();
			if(typeBinding!=null){
				IMethodBinding[] unimplementedMethods = ASTMethodUtil.getUnimplementedMethods(typeBinding);
				if(unimplementedMethods!=null){
					for (int uic = 0; uic < unimplementedMethods.length; uic++) {
						MethodDeclaration md = ASTMethodUtil.createMethodDeclaration(unimplementedMethods[uic], cic.getAST(), importList);
						cic.getAnonymousClassDeclaration().bodyDeclarations().add(md);
					}
				}
			}
			anonDecl.setDeclaration(cic.toString());
			return anonDecl;
		} else
			return null;
		
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
	
	public static MethodDeclaration createMethodDeclaration(IMethodBinding binding, AST ast, List importList){
		MethodDeclaration md = ast.newMethodDeclaration();
		
		md.setName(ast.newSimpleName(binding.getName()));
		
		md.setReturnType2(createTypeFromBinding(binding.getReturnType(), ast, importList));
		
		md.setBody(ast.newBlock());
		
		if(Modifier.isProtected(binding.getModifiers()))
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
		
		if(md.getReturnType2().isPrimitiveType()){
			if(PrimitiveType.VOID.equals(((PrimitiveType)md.getReturnType2()).getPrimitiveTypeCode())){
				// void return type - no need to put any return
			}else if(PrimitiveType.BOOLEAN.equals(((PrimitiveType)md.getReturnType2()).getPrimitiveTypeCode())){
				// boolean return - put in a return statement of false;
				ReturnStatement rs = ast.newReturnStatement();
				rs.setExpression(ast.newBooleanLiteral(false));
				md.getBody().statements().add(rs);
			}else{
				ReturnStatement rs = ast.newReturnStatement();
				rs.setExpression(ast.newNumberLiteral("-1"));
				md.getBody().statements().add(rs);
			}
		}else{
			ReturnStatement rs = ast.newReturnStatement();
			rs.setExpression(ast.newNullLiteral());
			md.getBody().statements().add(rs);
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
					name = new String(new char[]{Character.toLowerCase(name.charAt(0))}) + (name.length()>1 ? name.substring(1): "");
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