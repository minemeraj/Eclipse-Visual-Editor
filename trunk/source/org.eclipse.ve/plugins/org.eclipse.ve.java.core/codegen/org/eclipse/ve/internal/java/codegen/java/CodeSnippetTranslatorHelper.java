package org.eclipse.ve.internal.java.codegen.java;
/*******************************************************************************
 * Copyright (c) 2001, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: CodeSnippetTranslatorHelper.java,v $
 *  $Revision: 1.4 $  $Date: 2004-02-20 00:44:29 $ 
 */

import java.util.*;
import java.util.logging.Level;

import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.ISourceRange;
import org.eclipse.jdt.core.compiler.IProblem;
import org.eclipse.jdt.core.compiler.InvalidInputException;
import org.eclipse.jdt.internal.compiler.*;
import org.eclipse.jdt.internal.compiler.Compiler;
import org.eclipse.jdt.internal.compiler.ast.*;
import org.eclipse.jdt.internal.compiler.env.ICompilationUnit;
import org.eclipse.jdt.internal.compiler.env.INameEnvironment;
import org.eclipse.jdt.internal.compiler.impl.CompilerOptions;
import org.eclipse.jdt.internal.compiler.parser.*;
import org.eclipse.jdt.internal.compiler.problem.DefaultProblemFactory;
import org.eclipse.jdt.internal.compiler.problem.ProblemReporter;
import org.eclipse.jdt.internal.core.builder.NameEnvironment;
import org.eclipse.jdt.internal.core.builder.ProblemFactory;

import org.eclipse.ve.internal.java.codegen.core.TransientErrorEvent;
import org.eclipse.ve.internal.java.codegen.model.*;
import org.eclipse.ve.internal.java.codegen.util.CodeGenUtil;
import org.eclipse.ve.internal.java.core.JavaVEPlugin;

public class CodeSnippetTranslatorHelper {


protected static final String WRAPPER_CLASS_NAME="Wrapper_Class_For_Entries1"; //$NON-NLS-1$

/**
 * Returns IProblems of the compile
 */
protected static List compile(String classCode, final String classType, IJavaProject project, final char[][] packageName, final boolean wantWarnings){
	
	final String content = classCode;
	final List problems = new ArrayList();
	try{
		IProblemFactory problemFactory = ProblemFactory.getProblemFactory(Locale.getDefault());
		INameEnvironment nameEnvironment =  getBuildNameEnvironment(project);
		ICompilerRequestor requestor = new ICompilerRequestor(){
			public void acceptResult(CompilationResult result){
				if(result.getProblems()==null || result.getProblems().length<1){
					// No problems!
				}else{
					// Problems found.. returning them.
					for(int i=0;i<result.getProblems().length;i++){
						IProblem pr = (IProblem) result.getProblems()[i];
						if(pr.isError() || (pr.isWarning() && wantWarnings)){
							JavaVEPlugin.log("Compile problem** Message:"+pr.getMessage(), Level.FINEST); //$NON-NLS-1$
							problems.add(result.getProblems()[i]);
						}
					}
				}
			}
		};
		ICompilationUnit cu = new ICompilationUnit(){
			public char[] getContents(){return content.toCharArray();}
			public char[] getMainTypeName(){return classType.toCharArray();}
			public char[] getFileName(){
				String fileName = classType+".java";//$NON-NLS-1$
				return fileName.toCharArray();
			}
			public char[][] getPackageName(){return packageName;}
		};
		Compiler compiler = new Compiler(
			nameEnvironment,
			DefaultErrorHandlingPolicies.exitAfterAllProblems(),
			null,
			requestor,
			problemFactory
		);
		ICompilationUnit[] cus = new ICompilationUnit[1];
		cus[0] = cu;
		compiler.compile(cus);
	}catch(Exception e){
		problems.add(e);
		JavaVEPlugin.log(e, Level.WARNING) ;
	}
	return problems;
}

/**
 * Returns a name environment for the last built state.
 */
protected static INameEnvironment getBuildNameEnvironment(IJavaProject javaProject){
	return new NameEnvironment(javaProject);
}

/**
 * Reconstructs a copy of a BDM, based on an original BDM, having
 * only a specified beanPart and its init method. The shadow BDM
 * is an extreme skeleton, which does not have any JDOM information.
 */
public static IBeanDeclModel createShadowBDM(IBeanDeclModel original, String initMethodHandle){
	BeanDeclModel newModel = new BeanDeclModel();
	newModel.setDomain(original.getDomain()) ;
	try{
		BeanPart[] originalBeanParts = original.getBeansInializedByMethod(initMethodHandle);
		if (originalBeanParts == null) return newModel ;
		BeanPart[] newBeanParts = new BeanPart[originalBeanParts.length];
		for(int i=0;i<originalBeanParts.length;i++){
			newBeanParts[i] = new BeanPart(originalBeanParts[i].getSimpleName(), originalBeanParts[i].getType());
			newBeanParts[i].setInstanceVar(originalBeanParts[i].isInstanceVar());
			newModel.addBean(newBeanParts[i]);
		}
		CodeTypeRef type = new CodeTypeRef(original.getTypeRef().getName(), newModel);
		CodeMethodRef method = new CodeMethodRef(type, original.getMethodInitializingABean(initMethodHandle).getMethodName());
		method.setMethodHandle(initMethodHandle);
		method.setContent(original.getMethodInitializingABean(initMethodHandle).getContent());
		newModel.setTypeRef(type);
		for(int i=0;i<newModel.getBeans().size();i++)
			((BeanPart)newModel.getBeans().get(i)).addInitMethod(method);
//		Iterator expressions = original.getMethodInitializingABean(initMethodHandle).getExpressions();
//		while(expressions.hasNext()){
//			CodeExpressionRef originalExp = (CodeExpressionRef) expressions.next();
//			String bpName = originalExp.getBean().getUniqueName();
//		};
	}catch(Exception e){
		JavaVEPlugin.log(e, Level.WARNING) ;
	}
	return newModel;
}

protected static ISourceRange createSourceRange(final int offset, final int length){
	return new ISourceRange(){
		public int getOffset(){
			return offset;
		}
		public int getLength(){
			return length;
		}
	};
}

private static boolean problemsInResult(List allProblems){
	if(allProblems!=null && allProblems.size()>0 && allProblems.get(0) instanceof IProblem)
		return true;
	return false;
}

private static char[][] getPackageNameFromCUDecl(CompilationUnitDeclaration decl){
	char[][] pkgName = new char[0][0];
	if(decl !=null && decl.currentPackage!=null && decl.currentPackage.tokens!=null)
		pkgName = decl.currentPackage.tokens;
	return pkgName;
}

/**
 * We know the structure of the class, hence get
 * to the statements and determine their compilation
 * competence. We are only interested in the statements
 * in the second method.
 * We also need to send in only the 'compiler correct'
 * import, field and method declarations.
 * 
 * 1. Compile all imports, fields, inner type skeletons and other method skeletons to determine problems.
 * 2. If problems in step 1, then check for INDIVIDUALLY each import, field, inner-type skeletons and method skeletons for problems.
 * 3. After removing the problems from step 2., determine problems in the method.
 */
protected static List determineCompilerProblemsInMethod(
							String content, 
							CompilationUnitDeclaration decl, 
							String changedMethodDecl, 
							IJavaProject project){
	AbstractMethodDeclaration changedMethod = null;
	List otherSkeletonMethods = new ArrayList();
	char[][] packageName = getPackageNameFromCUDecl(decl);
	
	for(int mc=0;mc<decl.types[0].methods.length;mc++){
		AbstractMethodDeclaration m = decl.types[0].methods[mc];
		if(m.isClinit() || m.isConstructor() || m.isDefaultConstructor()) // yes, yes very cautious.
			continue;
		String handle = getAMDHandle(m, content);
		if(handle.equals(changedMethodDecl))
			changedMethod = m;
		else
			otherSkeletonMethods.add(m);
	}
	AbstractMethodDeclaration[] mds = new AbstractMethodDeclaration[otherSkeletonMethods.size()];
	for(int mc=0;mc<otherSkeletonMethods.size();mc++)
		mds[mc] = (AbstractMethodDeclaration)otherSkeletonMethods.get(mc);
	List allProblems = compile(
						getWrappedInClass(
							packageName,
							decl.imports, 
							decl.types[0].superclass,
							decl.types[0].superInterfaces,
							decl.types[0].memberTypes,
							decl.types[0].fields,
							mds,
							content), 
						WRAPPER_CLASS_NAME,
						project,
						packageName, 
						false);
	
	ImportReference[] goodImports = decl.imports;
	TypeDeclaration[] goodInnerTypes = decl.types[0].memberTypes;
	FieldDeclaration[] goodFields = decl.types[0].fields;
	
	TypeReference superClass = decl.types[0].superclass;
	TypeReference[] superInterfaces = decl.types[0].superInterfaces;

	if(problemsInResult(allProblems)){

		// (1) Get only the good imports which did not cause compiler problems
		ImportReference[] allImports = decl.imports;
		if(problemsInResult(compile(getWrappedInClass(packageName,allImports,null,null,null, null,null, content), WRAPPER_CLASS_NAME, project, packageName, false))){
			List goodImportsList = new ArrayList();
			goodImports = null;
			for(int ic=0;allImports!=null&&ic<allImports.length;ic++){
				List problems = compile(getWrappedInClass(packageName,new ImportReference[]{allImports[ic]},null,null,null,null,null,content), WRAPPER_CLASS_NAME, project, packageName, false);
				if(problems!=null && problems.size()>0 && problems.get(0) instanceof IProblem){
					// atleast one problem found with the import, hence ignore it.
				}else{
					goodImportsList.add(allImports[ic]);
				}
			}
			if(goodImportsList.size()>0)
				goodImports = new ImportReference[goodImportsList.size()];
			for(int ic=0;ic<goodImportsList.size();ic++)
				goodImports[ic] = (ImportReference)goodImportsList.get(ic);
		}else{
			goodImports = allImports;
		}
		
		// (2) Get only the good inner-types which did not cause compiler problems
		TypeDeclaration[] allInnerTypes = decl.types[0].memberTypes;
		if(problemsInResult(compile(getWrappedInClass(packageName,goodImports,superClass,superInterfaces,allInnerTypes, null,null, content), WRAPPER_CLASS_NAME, project, packageName, false))){
			List goodInnerTypesList = new ArrayList();
			goodInnerTypes = null;
			for(int itc=0;allInnerTypes!=null&&itc<allInnerTypes.length;itc++){
				List problems = compile(getWrappedInClass(packageName,goodImports,superClass,superInterfaces,new TypeDeclaration[]{allInnerTypes[itc]},null,null,content), WRAPPER_CLASS_NAME, project, packageName, false);
				if(problems!=null && problems.size()>0 && problems.get(0) instanceof IProblem){
					// atleast one problem found with the import, hence ignore it.
				}else{
					goodInnerTypesList.add(allInnerTypes[itc]);
				}
			}
			if(goodInnerTypesList.size()>0)
				goodInnerTypes = new TypeDeclaration[goodInnerTypesList.size()];
			for(int itc=0;itc<goodInnerTypesList.size();itc++)
				goodInnerTypes[itc] = (TypeDeclaration)goodInnerTypesList.get(itc);
		}else{
			goodInnerTypes = allInnerTypes;
		}
		
		// (3) Get only the good fields which did not cause compiler problems
		FieldDeclaration[] allFields = decl.types[0].fields;
		if(problemsInResult(compile(getWrappedInClass(packageName,goodImports, superClass, superInterfaces, goodInnerTypes, allFields,null,content), WRAPPER_CLASS_NAME, project,  packageName, false))){
			List goodFieldsList = new ArrayList();
			goodFields = null;
			for(int ic=0;allFields!=null&&ic<allFields.length;ic++){
				if(problemsInResult(compile(getWrappedInClass(packageName,goodImports, superClass, superInterfaces, goodInnerTypes, new FieldDeclaration[]{allFields[ic]},null,content), WRAPPER_CLASS_NAME, project, packageName, false))){
					// atleast one problem found with the import, hence ignore it.
				}else{
					goodFieldsList.add(allFields[ic]);
				}
			}
			if(goodFieldsList.size()>0)
				goodFields = new FieldDeclaration[goodFieldsList.size()];
			for(int ic=0;ic<goodFieldsList.size();ic++)
				goodFields[ic] = (FieldDeclaration)goodFieldsList.get(ic);
		}else{
			goodFields = allFields;
		}
		
		// (4) Get the good skeletons of the other methods
		AbstractMethodDeclaration[] mts = new AbstractMethodDeclaration[otherSkeletonMethods.size()];
		for(int mc=0;mc<otherSkeletonMethods.size();mc++)
			mts[mc] = (AbstractMethodDeclaration)otherSkeletonMethods.get(mc);
		if(problemsInResult(compile(getWrappedInClass(packageName,goodImports,superClass, superInterfaces, goodInnerTypes, goodFields,mts,content),WRAPPER_CLASS_NAME,project,  packageName, false))){
			List problemSkeletonMethods = new ArrayList();
			for(int mc=0;mc<otherSkeletonMethods.size();mc++){
				AbstractMethodDeclaration m = (AbstractMethodDeclaration)otherSkeletonMethods.get(mc);
				if(problemsInResult(compile(getWrappedInClass(packageName,goodImports,superClass, superInterfaces, goodInnerTypes, goodFields,new AbstractMethodDeclaration[]{m},content),WRAPPER_CLASS_NAME,project,  packageName, false))){
						// Compile problem with the method skeleton itself!
						// maybe an unresolvable return type.
						problemSkeletonMethods.add(m);
				}
			}
			otherSkeletonMethods.removeAll(problemSkeletonMethods);
		}
	}

	StringBuffer otherMethods = new StringBuffer();
	for(int mc=0;mc<otherSkeletonMethods.size();mc++)
		otherMethods.append(getCompleteSource(content,(AbstractMethodDeclaration)otherSkeletonMethods.get(mc)));
	
	// (5) Now get the problems with the statements
	List inErrorStatements = new ArrayList();
	parseStatements(content, packageName, goodImports, superClass, superInterfaces, goodInnerTypes, goodFields, changedMethod, new String(), otherMethods.toString(), changedMethod.statements, inErrorStatements, project);
	if(inErrorStatements.size()>0)
		return inErrorStatements;
	else
		return null;
}

private static void dumpErrors (List errors) {
	if (errors.isEmpty()) return ;
	JavaVEPlugin.log("CodeSnippetDecoderHelper: Parsing Error/s:", Level.FINE) ; //$NON-NLS-1$
	Iterator itr = errors.iterator() ;
	while (itr.hasNext()) {
		Object n = itr.next() ;
		if (n instanceof IProblem) {
			IProblem p = (IProblem) n ;
			if (p.isWarning()) continue ;
			JavaVEPlugin.log(p.getMessage(), Level.FINE) ;
		}
	}
}


/*
 * The input at this stage consists of the package, the imports,
 * the class with the extends,implements in it, the skeletonized 
 * inner-types, the un-skeletonized changed method, and all
 * the skeletonized other methods.
 *  
 * First check if the entire method is parseable and
 * compilable.... if correct, return null. Else returns
 * a List of TransientErrorEvents holding the errors in
 * the process.
 * 
 * Modification: 06/13/2002 - Now the parameter 'entireCode'
 *               should contain skeletonized methods! This
 *               class DOES NOT skeletonize methods. It is now
 *               the responsibility of the workItem to contain
 *               skeletonized code. 
 * Modification: 06/14/2002 - The code passed in should have
 *               all the correct environment - imports, 
 *               packageName etc. - else compile problems would
 *               be reported.
 * 
 * 
 * @return null Absolutely no errors.
 * @return List If size of list is zero, errors were not
 *               correctable! Else the list has a list of
 *               all statements which were corrected. 
 */
public static List determineOffendingParts(
		String entireCode, 
		String typeName,
		IJavaProject project, 
		int changedMethodIndex){
	List wholeMethodSyntax = new ArrayList();
	try{
		String skeletonCode =entireCode; 
		wholeMethodSyntax = parseSyntacticallyCodeSnippet(skeletonCode);
		if(wholeMethodSyntax.get(0) instanceof CompilationUnitDeclaration){
			// Whole classes parses..  
			CompilationUnitDeclaration skeletonDecl = (CompilationUnitDeclaration)wholeMethodSyntax.get(0);
			// Parses correctly...  now check for compilation problems
			char[][] packageName = getPackageNameFromCUDecl(skeletonDecl);
			List wholeMethodCompileProblems = compile(skeletonCode, typeName, project,  packageName, false);
			if(wholeMethodCompileProblems==null || wholeMethodCompileProblems.size()<1){
				return null; // Absolutely no error at all.
			}else{
				int index = changedMethodIndex+1;
				int count = 0;
				for(int mc=0;mc<skeletonDecl.types[0].methods.length;mc++){
					if(!skeletonDecl.types[0].methods[mc].isClinit()){
						if(!skeletonDecl.types[0].methods[mc].isConstructor()){
							if(count == changedMethodIndex)
								index = mc;
							count++;
						}
					}
				}
				AbstractMethodDeclaration changedM = skeletonDecl.types[0].methods[index];
				String changedMethodHandle = getAMDHandle(changedM, skeletonCode);
				// Parseable, hence shall determine which were not compilable.
				return determineCompilerProblemsInMethod(skeletonCode, skeletonDecl, changedMethodHandle, project); 
			}
		}
		dumpErrors(wholeMethodSyntax) ;
	}catch(Exception e){
		JavaVEPlugin.log(e, Level.WARNING) ;
	}
	return wholeMethodSyntax; // Some error, but couldnt figure out which, because it was not even parseable.
}

private static String getAMDHandle(AbstractMethodDeclaration method, String entireCode){
	return entireCode.substring(method.sourceStart, method.bodyStart-1);
}

/**
 * Return the complete source of the passed in node, 
 * including the ';' which seperates expressions.
 */
public static String getCompleteSource(String content, ASTNode node){
	if(node instanceof FieldDeclaration){
		FieldDeclaration field = (FieldDeclaration) node;
		return content.substring(field.declarationSourceStart, field.declarationSourceEnd+1);
	}else{
		if(node instanceof ImportReference){
			ImportReference importNode = (ImportReference) node;
			return content.substring(importNode.declarationSourceStart, importNode.declarationSourceEnd+1);
		}else{
			if (node instanceof LocalDeclaration) {
				LocalDeclaration localDecl = (LocalDeclaration) node;
				return content.substring(localDecl.declarationSourceStart,localDecl.declarationSourceEnd+1);
				
			}else{
				if(node instanceof AbstractVariableDeclaration){
					AbstractVariableDeclaration varDecl= (AbstractVariableDeclaration) node;
					return content.substring(varDecl.type.sourceStart, varDecl.declarationSourceEnd+1);
				}else{
					if (node instanceof AbstractMethodDeclaration) {
						AbstractMethodDeclaration methodDecl = (AbstractMethodDeclaration) node;
						return content.substring(methodDecl.declarationSourceStart, methodDecl.declarationSourceEnd+1);
					}else{
						if (node instanceof TypeDeclaration) {
							TypeDeclaration typeDecl = (TypeDeclaration) node;
							return content.substring(typeDecl.declarationSourceStart, typeDecl.declarationSourceEnd+1);
						}
					}
				}
			}
		}
	}
	return content.substring(node.sourceStart, node.sourceEnd+1);
}

private static String getStatementWrappedInClass(
	char[][] pkgName,
	ImportReference[] imports, 
	TypeReference superClass,
	TypeReference[] superInterfaces,
	TypeDeclaration[] innerTypes,
	FieldDeclaration[] fields, 
	String statement, 
	String localDeclarations,
	String otherMethods,
	String content){
		
	StringBuffer buff = new StringBuffer();
	if(pkgName!=null)
		if(pkgName.length>0)
			buff.append("package "+CodeGenUtil.tokensToString(pkgName)+";\n"); //$NON-NLS-1$ //$NON-NLS-2$
	if(imports!=null)
		for(int i=0;i<imports.length;i++){
			buff.append(getCompleteSource(content, imports[i])+"\n"); //$NON-NLS-1$
		}
	buff.append("public class "+WRAPPER_CLASS_NAME);
	if(superClass!=null)
		buff.append(" extends "+CodeGenUtil.tokensToString(superClass.getTypeName()));
	if(superInterfaces!=null && superInterfaces.length>0){
		buff.append(" implements ");
		for(int sic=0;sic<superInterfaces.length;sic++){
			buff.append(CodeGenUtil.tokensToString(superInterfaces[sic].getTypeName()));
			if(sic<superInterfaces.length-1)
				buff.append(" , ");
		}
	}
	buff.append("{\n");
	if(innerTypes!=null && innerTypes.length>0)
		for(int itc=0;itc<innerTypes.length;itc++)
			buff.append(getCompleteSource(content, innerTypes[itc]));
	if(fields!=null)
		for(int i=0;i<fields.length;i++)
			if(!isWrappedByOtherPreviousFields(fields, fields[i]))
				buff.append(getCompleteSource(content, fields[i])+"\n"); //$NON-NLS-1$
	buff.append("public void run() throws Throwable{\n"); //$NON-NLS-1$
	buff.append(localDeclarations);
	buff.append(statement+";"); //$NON-NLS-1$
	buff.append("\n}\n"); //$NON-NLS-1$
	buff.append(otherMethods);
	buff.append("\n}\n"); //$NON-NLS-1$
	return buff.toString();
}

private static boolean isWrappedByOtherPreviousFields(FieldDeclaration[] fields, FieldDeclaration ff){
	if(fields==null || ff==null || fields.length<1)
		return false;
	int from = ff.declarationSourceStart;
	int to = ff.declarationEnd;
	for(int i=0;i<fields.length;i++){
		if(fields[i].equals(ff))
			break;
		int aFrom = fields[i].declarationSourceStart;
		int aTo = fields[i].declarationSourceEnd;
		if(aFrom<=from && aTo>=to)
			return true;
	}
	return false;
}

private static String getWrappedInClass(
	char[][] pkgName,
	ImportReference[] imports,
	TypeReference superClass,
	TypeReference[] superInterfaces,
	TypeDeclaration[] innerTypes,
	FieldDeclaration[] fields,
	AbstractMethodDeclaration[] methods,
	String content){
	StringBuffer buff = new StringBuffer();
	if(pkgName!=null)
		if(pkgName.length>0)
			buff.append("package "+CodeGenUtil.tokensToString(pkgName)+" ;\n");//$NON-NLS-1$ //$NON-NLS-2$
	if(imports!=null)
		for(int i=0;i<imports.length;i++)
			buff.append(getCompleteSource(content, imports[i])+"\n"); //$NON-NLS-1$
	buff.append("public class "+WRAPPER_CLASS_NAME );
	if(superClass!=null)
		buff.append(" extends "+CodeGenUtil.tokensToString(superClass.getTypeName()));
	if(superInterfaces!=null && superInterfaces.length>0){
		buff.append(" implements ");
		for(int sic=0;sic<superInterfaces.length;sic++){
			buff.append(CodeGenUtil.tokensToString(superInterfaces[sic].getTypeName()));
			if(sic<superInterfaces.length-1)
				buff.append(" , ");
		}
	}
	buff.append("{\n");
	if(innerTypes!=null && innerTypes.length>0)
		for(int itc=0;itc<innerTypes.length;itc++)
			buff.append(getCompleteSource(content, innerTypes[itc]));
	if(fields!=null)
		for(int fc=0;fc<fields.length;fc++)
			if(!isWrappedByOtherPreviousFields(fields, fields[fc]))
				buff.append(getCompleteSource(content, fields[fc])+"\n"); //$NON-NLS-1$
	if(methods!=null)
		for(int mc=0;mc<methods.length;mc++)
			buff.append(getCompleteSource(content, methods[mc]));
	buff.append("\n}\n"); //$NON-NLS-1$
	return buff.toString();
}

/**
 * Returns/Fills up the errorlist with the TransientErrors in the
 * statements
 */
protected static void parseStatements(
		String content, 
		char[][] packageName,
		ImportReference[] goodImports,
		TypeReference superClass,
		TypeReference[] superInterfaces,
		TypeDeclaration[] goodInnerTypes,
		FieldDeclaration[] goodFields,
		AbstractMethodDeclaration method,
		String prependLocalDeclarations,
		String otherMethods,
		Statement[] statements, 
		List errorList, 
		IJavaProject project){
    if (statements == null) return ;
	for(int sc=0;sc<statements.length;sc++){
		Statement stmt = statements[sc];
		if(stmt instanceof TryStatement){
			TryStatement tryStmt = (TryStatement)stmt;
			parseStatements(content, packageName, goodImports, superClass, superInterfaces, goodInnerTypes, goodFields, method, prependLocalDeclarations, otherMethods, tryStmt.tryBlock.statements, errorList, project);
		}else{
			if(stmt instanceof IfStatement){
				IfStatement ifStmt = (IfStatement)stmt;
				Statement[] thenStmts = {ifStmt.thenStatement};
				parseStatements(content, packageName, goodImports, superClass, superInterfaces, goodInnerTypes, goodFields, method, prependLocalDeclarations, otherMethods, thenStmts, errorList, project);
			}else{
				if(stmt instanceof Block){
					Block block = (Block)stmt;
					parseStatements(content, packageName, goodImports, superClass, superInterfaces, goodInnerTypes, goodFields, method, prependLocalDeclarations, otherMethods, block.statements, errorList, project);
				}else{
					if(stmt instanceof ReturnStatement)
						return;
					String msg = getCompleteSource(content, stmt);
					JavaVEPlugin.log("Analyzing:"+msg, Level.FINEST); //$NON-NLS-1$
					List problems = compile(getStatementWrappedInClass(packageName, goodImports, superClass, superInterfaces, goodInnerTypes, goodFields, msg, prependLocalDeclarations, otherMethods, content), WRAPPER_CLASS_NAME, project, packageName, false);
					if((problems==null || problems.size()<1) && (stmt instanceof LocalDeclaration)){
						// Add the local declarations before the statements.
						prependLocalDeclarations = prependLocalDeclarations.concat(getCompleteSource(content, stmt));
						prependLocalDeclarations = prependLocalDeclarations.concat(" ;\n"); //$NON-NLS-1$
					}
					if(problems.size()>0){
						int stmtStart = stmt.sourceStart;
						int stmtEnd = stmt.sourceEnd;
						if(stmt instanceof AbstractVariableDeclaration){
							stmtStart = ((AbstractVariableDeclaration)stmt).declarationSourceStart;
							stmtEnd = ((AbstractVariableDeclaration)stmt).declarationSourceEnd;
						}
						for(int i=0;i<problems.size();i++){
							IProblem problem = (IProblem) problems.get(i);
							TransientErrorEvent event = new TransientErrorEvent(
																TransientErrorEvent.TYPE_COMPILER_ERROR, 
																null, 
																stmtStart - method.declarationSourceStart, 
																stmtEnd+1 - method.declarationSourceStart, 
																problem.getMessage(), 
																new String(method.selector), 
																content.substring(method.declarationSourceStart, method.declarationSourceEnd));
							errorList.add(event);
						}
					}
				}
			}
		}
	}
}

/**
 * Returns a list of IProblems which have been found
 * for the code snippet. If the first element is a 
 * Exception, then there was a fatal problem with the
 * parsing.
 */
public static List parseSyntacticallyCodeSnippet(final String classCode){
	try{
		ICompilationUnit cu = new ICompilationUnit(){
			public char[] getContents(){
				return classCode.toCharArray();
			}
			public char[] getMainTypeName(){
				return CodeSnippetTranslator.CODE_SNIPPET_CLASSNAME.toCharArray();
			}
			
			public char[] getFileName(){
				return CodeSnippetTranslator.CODE_SNIPPET_CLASSNAME.toCharArray();
			}
			public char[][] getPackageName(){return new char[][] {"".toCharArray()};} //$NON-NLS-1$
		};
		CompilationResult result = new CompilationResult(cu, 0, 0, 20);
		ProblemReporter reporter = new ProblemReporter(
							DefaultErrorHandlingPolicies.exitAfterAllProblems(),
							new CompilerOptions(),
							new DefaultProblemFactory(Locale.getDefault()));
		Parser parser = new Parser(reporter, true);
		
		CompilationUnitDeclaration decl = parser.parse(cu, result);
		IProblem[] problems = result.getProblems();
		List problemsList = new ArrayList();
		if(problems==null){
			problemsList.add(decl);
			return problemsList;
		}
		for(int i=0;i<problems.length;i++)	
			problemsList.add(problems[i]);
		return problemsList;
	}catch(Exception e){
		List dummyProblem = new ArrayList();
		dummyProblem.add(e);
		return dummyProblem;
	}
}

/**
 * Removes comments inside the passed in String, and returns the 
 * ranges of the comments in original unmodified String.
 * 
 * @param originalCopy  String from which comments would be removed.
 * @return  List containing the modified source first, and the 
 *          ISourceRanges next.
 */
public static List removeAllComments(String originalCopy){
	List commentRanges = new ArrayList();
	Scanner scanner = new Scanner();
	scanner.setSource(originalCopy.toCharArray());
	scanner.recordLineSeparator = true;
	scanner.tokenizeWhiteSpace = true;
	scanner.tokenizeComments = true;
	StringBuffer corrected = new StringBuffer();
	try{
		int token = scanner.getNextToken();
		while(true){
			if(token == TerminalTokens.TokenNameEOF)
				break;
			if(token == TerminalTokens.TokenNameCOMMENT_BLOCK ||
			   token == TerminalTokens.TokenNameCOMMENT_JAVADOC ||
			   token == TerminalTokens.TokenNameCOMMENT_LINE){
			   	// Add it to the list of comment ranges.
			   	commentRanges.add(createSourceRange(scanner.startPosition, scanner.currentPosition-scanner.startPosition+1));
			}else{
				corrected.append(scanner.getCurrentTokenSource());
			}
			try {
			  token = scanner.getNextToken();
			}
			catch (InvalidInputException e) {}
		}
	}catch(Exception e){
		JavaVEPlugin.log(e, Level.WARNING) ;
	}
	commentRanges.add(0,corrected.toString());
	return commentRanges;
}
}