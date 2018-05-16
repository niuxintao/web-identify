package edu.utsa.cs.guidereg;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import soot.Body;
import soot.BodyTransformer;
import soot.Local;
import soot.LongType;
import soot.PackManager;
import soot.PatchingChain;
import soot.RefType;
import soot.Scene;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.Transform;
import soot.Trap;
import soot.Type;
import soot.Unit;
import soot.Value;
import soot.jimple.AssignStmt;
import soot.jimple.DefinitionStmt;
import soot.jimple.GotoStmt;
import soot.jimple.IdentityStmt;
import soot.jimple.IfStmt;
import soot.jimple.InvokeExpr;
import soot.jimple.InvokeStmt;
import soot.jimple.Jimple;
import soot.jimple.NopStmt;
import soot.jimple.ReturnStmt;
import soot.jimple.ReturnVoidStmt;
import soot.jimple.SpecialInvokeExpr;
import soot.jimple.StaticInvokeExpr;
import soot.jimple.Stmt;
import soot.jimple.StringConstant;
import soot.jimple.ThrowStmt;
import soot.jimple.toolkits.annotation.logic.Loop;
import soot.options.Options;
import soot.toolkits.graph.LoopNestTree;
import soot.util.Chain;

public class GoToChecker {

	public static void main(String[] args) {

		// here we try to set the library class path
		/*
		 * Options.v().set_soot_classpath( Scene.v().defaultClassPath() +
		 * File.pathSeparator+
		 * "C:/Users/Tareg/Desktop/sem2/Independent-Study/subjects-new/jodatime/joda-time-2.6/target/joda-time-2.6.jar"
		 * +File.pathSeparator +
		 * "C:/Users/Tareg/Desktop/sem2/Independent-Study/subjects-new/jodatime/joda-time-2.6/target/test-classes"
		 * +File.pathSeparator+
		 * "C:/Users/Tareg/Desktop/sem2/Independent-Study/log4j-1.2.13.jar"+
		 * File.pathSeparator
		 * +"C:/Users/Tareg/Desktop/sem2/Independent-Study/hamcrest-all-1.3.jar"
		 * );
		 */

		String exFolder = "C:/Users/Xintao/Desktop/doExForWang/";
		String libFolder = "exlibs/";
		
		String subject = "joda";
		String version = "joda-time-2.3-use2.2";
		String target = "";

		Options.v()
				.set_soot_classpath(
						Scene.v().defaultClassPath()
								+ File.pathSeparator
								+ exFolder
								+ "subjects-new/"
								+ File.pathSeparator
								+ exFolder
								+ "subjects-new/" + subject + "/" +version + "/target/test-classes"
								+ File.pathSeparator
								+ exFolder
								+ "subjects-new/" + subject + "/" +version + "/target/classes"
								+ File.pathSeparator + exFolder + libFolder
								+ "log4j-1.2.13.jar"
								+ File.pathSeparator + exFolder + libFolder
								+ "joda-convert-1.2.jar"
								+ File.pathSeparator + exFolder + libFolder
								+ "junit-3.8.2.jar"
								+ File.pathSeparator + exFolder + libFolder
								+ "hamcrest-all-1.3.jar");
		Options.v().set_keep_line_number(true);

		// here we are defining an array sootArgs and set its elements to:
		// 1-the input directory from which we are going to take the classes
		// that we need to instrument
		// 2-the output directory to which we are going to place the
		// instrumented jar file

		String[] sootArgs = {
				"-pp",
				"-process-dir",

				exFolder + "subjects-new/"+ subject + "/" + version + "/"
						+ "target/test-classes",
				"-output-jar",
				"-d",
				"C:/Users/xintao/Desktop/doExForWang/instrumented-file/joda/" +"1.1.1.jar" };

		// String[] sootArgs = {"-pp",
		// "-process-dir","C:/Users/Tareg/Desktop/sem2/Independent-Study/subjects-new/jodatime/joda-time-2.6/target/test-classes",
		// "-output-jar","-d","C:/Users/Tareg/Desktop/sem2/Independent-Study/instrumented-file/joda/1.1.1.jar"
		// };

		// here we are trying to make soot perform our analysis that defined in
		// the instrumenter class
		// which is an instance from GotoInstrumenter class
		// this analysis will be performed during the jtp pack
		GotoInstrumenter instrumenter = GotoInstrumenter.getInstance();
		PackManager.v().getPack("jtp")
				.add(new Transform("jtp.instrumenter", instrumenter)); // ????

		// Just in case, resolve the PrintStream and System SootClasses.
		Scene.v().addBasicClass("java.io.PrintStream", SootClass.SIGNATURES);
		Scene.v().addBasicClass("java.lang.System", SootClass.SIGNATURES);

		soot.Main.main(sootArgs);

	}
} // end of the main

// define class GotoInstrumenter that contain the analysis (instrumentation)
class GotoInstrumenter extends BodyTransformer {
	private static GotoInstrumenter instance = new GotoInstrumenter();
	static Map<String, Integer> indexMap;

	// define soot class which is the class that contains the method that going
	// to be executed during the analysis
	// define soot method which is the method that contain the logic of analysis
	static SootClass dumperClass;
	static SootMethod reportDump;

	// load the dumper class into the soot class
	// then load the dump method into the soot method
	static {
		dumperClass = Scene.v().loadClassAndSupport(
				"edu.utsa.cs.guidereg.Dumper");
		reportDump = dumperClass
				.getMethod("void dump(java.lang.Object,java.lang.String)");

	}

	private GotoInstrumenter() {

	}

	public static GotoInstrumenter getInstance() {
		if (instance == null) {
			instance = new GotoInstrumenter();
		}
		return instance;
	}

	// this method is the method that will be executed during analysis
	// during execution soot will load the body of the input classes methods and
	// pass it to this method
	protected void internalTransform(Body body, String phaseName, Map options) {

		// since we want to perform our analysis on certain methods of the input
		// classes,
		// here we filter those methods if they are:
		// 1-abstract method
		// 2-complex methods
		if (body.getMethod().isAbstract())
			return;
		if (!isComplexMethod(body))
			return;

		SootClass sClass = body.getMethod().getDeclaringClass();
		SootField gotoCounter = null;
		boolean addedLocals = false;
		Local tmpRef = null, tmpLong = null;
		Chain units = body.getUnits();

		// filter the method if it is:
		// 1- constructor
		// 2-
		// 3- does not start with void test
		if (body.getMethod().getName().equalsIgnoreCase("<init>")
				|| body.getMethod().getName().equalsIgnoreCase("<clinit>")
				|| (!body.getMethod().getSubSignature().startsWith("void test"))) {
			System.out.println(body.getMethod().getSubSignature());
			return;
		}

		{
			boolean isMainMethod = body.getMethod().getSubSignature()
					.startsWith("test");

			String packageName = body.getMethod().getDeclaringClass()
					.getJavaPackageName();
			String testClassName = body.getMethod().getDeclaringClass()
					.getName();
			String methodName = body.getMethod().getName();

			// here trying to get any instance of the class that we are
			// analyzing
			Local referenceOfObject = getReferenceOfObject(body, testClassName);

			if (referenceOfObject == null)
				return;
			// try to invoke the dumper method on the reference object

			InvokeExpr recordDumpExp = Jimple.v().newStaticInvokeExpr(
					reportDump.makeRef(), referenceOfObject,
					StringConstant.v(methodName));
			Iterator stmtIt = units.snapshotIterator();

			// try to invoke the dumper method
			// first check weather the statement is not static
			int i = 0;
			Stmt prev = null;
			while (stmtIt.hasNext()) {

				Stmt s = (Stmt) stmtIt.next();
				System.out.println("instert : " + s.toString());
				if (s instanceof InvokeStmt) {
					InvokeExpr iexpr = (InvokeExpr) ((InvokeStmt) s)
							.getInvokeExpr();
					if (iexpr instanceof StaticInvokeExpr) {
						SootMethod target = ((StaticInvokeExpr) iexpr)
								.getMethod();
					}
				}

				else if (s instanceof ReturnStmt || s instanceof ReturnVoidStmt) {
					Stmt dumpStmt = Jimple.v().newInvokeStmt(recordDumpExp);
					units.insertBefore(dumpStmt, s);
				}
				i++;
				prev = s;

			}
		}
	}

	String getObjectType(String className, String packageName) {
		className = className.replace("Test", "");
		String[] split = className.split("_");
		return packageName + "." + split[0];
	}

	Local getReferenceOfObject(Body body, String objectType) {
		Chain<Local> locals = body.getLocals();
		for (Local local : locals) {

			if (local.getType().toString().equalsIgnoreCase(objectType)) {

				return local;
			}
		}
		return null;
	}

	boolean isComplexMethod(Body body) {

		PatchingChain<Unit> units = body.getUnits();
		Iterator stmtIt = units.snapshotIterator();

		while (stmtIt.hasNext()) {
			Stmt s = (Stmt) stmtIt.next();
			if (s instanceof InvokeStmt) {
				return true;
			}

			if (s instanceof DefinitionStmt) {

				Value lhsOp = ((DefinitionStmt) s).getLeftOp();
				Value rhsOp = ((DefinitionStmt) s).getRightOp();
				if (rhsOp instanceof InvokeExpr) {
					return true;

				}
			}

		}

		// what the purpose of this????
		Chain<Trap> traps = body.getTraps();
		for (Trap trap : traps) {
			return true;

		}

		return false;

	}

}
