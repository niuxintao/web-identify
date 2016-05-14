package edu.utsa.cs.guidereg;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import com.google.gson.Gson;

import soot.Body;
import soot.BodyTransformer;
import soot.Local;
import soot.PackManager;
import soot.PatchingChain;
import soot.RefType;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.Transform;
import soot.Unit;
import soot.Value;
import soot.jimple.DefinitionStmt;
import soot.jimple.InvokeExpr;
import soot.jimple.InvokeStmt;
import soot.jimple.Jimple;
import soot.jimple.Stmt;
import soot.jimple.StringConstant;
import soot.options.Options;
import soot.toolkits.graph.BriefUnitGraph;
import soot.toolkits.graph.UnitGraph;
import soot.toolkits.scalar.FlowSet;
import soot.toolkits.scalar.InitAnalysis;
import soot.util.Chain;

public class GoToChecker {

	public static void test(String objectPath, String methodName,
			String outputPath) {

		Options.v().set_soot_classpath(
				Scene.v().defaultClassPath() + File.pathSeparator + objectPath);

		// keep line number
		Options.v().set_keep_line_number(true);

		// here we are defining an array sootArgs and set its elements to:
		// 1-the input directory from which we are going to take the classes
		// that we need to instrument
		// 2-the output directory to which we are going to place the
		// instrumented jar file

		String[] sootArgs = { "-pp", "-process-dir", objectPath, "-d",
				outputPath };

		// "-output-jar",

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

	public static void main(String[] args) {

		// here we try to set the library class path
		String objectPath = "C:/Users/xintao/Desktop/njutest/test_original.jar";
		String methodName = "";
		String outputPath = "C:/Users/xintao/Desktop/njutest/output/test/";
		GoToChecker.test(objectPath, methodName, outputPath);

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

	private SootClass javaIoPrintStream;

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
	@SuppressWarnings("rawtypes")
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
		// filter the method if it is:
		// 1- constructor
		// 2-
		if (body.getMethod().getName().equalsIgnoreCase("<init>")
				|| body.getMethod().getName().equalsIgnoreCase("<clinit>")) {
			// System.out.println(body.getMethod().getSubSignature());
			return;
		}

		javaIoPrintStream = Scene.v().getSootClass("java.io.PrintStream");
		Local tempRef = addTmpRef(body);

		SootMethod method = body.getMethod();
		System.out.println("instrumenting method : " + method.getSignature());
		Chain<Unit> units = body.getUnits();
		Iterator stmtIt = units.snapshotIterator();

//		String testClassName = body.getMethod().getDeclaringClass().getName();
		// Local referenceOfObject = getReferenceOfObject(body, testClassName);
		// if (referenceOfObject == null)
		// return;

		UnitGraph g = new BriefUnitGraph(body);
		InitAnalysis analysis = new InitAnalysis(g);

		while (stmtIt.hasNext()) {
			Stmt s = (Stmt) stmtIt.next();

//			String methodName = body.getMethod().getName();

			if (s instanceof InvokeStmt) {
				System.out.println("instrumtaion : " + s);
				addStmtsIndicator(units, s, tempRef);
				Chain<Local> locals = body.getLocals();

				// get those variables that are initialized
				FlowSet init = (FlowSet) analysis.getFlowBefore(s);

				for (Local local : locals) {
					if (init.contains(local)) {
						addStmtsToBefore(units, s, local, tempRef);
					}
					//
					// else {
					// System.out.println("Warning: Local variable " + local
					// + " not definitely defined at " + s + " in "
					// + method);
					// }
					// temp = repStmt;
					// **************************************** here to try
					// break;
				}
			}
		}
	}

	private Local addTmpRef(Body body) {
		Local tmpRef = Jimple.v().newLocal("tmpRef",
				RefType.v("java.io.PrintStream"));
		body.getLocals().add(tmpRef);
		return tmpRef;
	}
	
	public void addStmtsIndicator(Chain<Unit> units, Stmt s, Local tmpRef){
		units.insertBefore(
				Jimple.v()
						.newAssignStmt(
								tmpRef,
								Jimple.v()
										.newStaticFieldRef(
												Scene.v()
														.getField(
																"<java.lang.System: java.io.PrintStream out>")
														.makeRef())), s);
		
		SootMethod toCallCha = javaIoPrintStream
				.getMethod("void print(java.lang.String)");
		units.insertBefore(
				Jimple.v().newInvokeStmt(
						Jimple.v().newVirtualInvokeExpr(tmpRef,
								toCallCha.makeRef(),
								StringConstant.v("memories before :" + s.toString() ))),
				s);

	}

	public void addStmtsToBefore(Chain<Unit> units, Stmt s, Local variable,
			Local tmpRef) {

		// insert "tmpRef = java.lang.System.out;"
		units.insertBefore(
				Jimple.v()
						.newAssignStmt(
								tmpRef,
								Jimple.v()
										.newStaticFieldRef(
												Scene.v()
														.getField(
																"<java.lang.System: java.io.PrintStream out>")
														.makeRef())), s);


		
		String[] forbidden = { "StringBuilder", "PrintStream", "lib", "String",
				"byte" };
		for (String forb : forbidden)
			if (variable.getType().toString().contains(forb))
				return;


		SootMethod toCallCha = javaIoPrintStream
				.getMethod("void print(java.lang.String)");
		units.insertBefore(
				Jimple.v().newInvokeStmt(
						Jimple.v().newVirtualInvokeExpr(tmpRef,
								toCallCha.makeRef(),
								StringConstant.v(variable.getName() + " : "))),
				s);

		String type = variable.getType().toString();
		SootMethod toCall = javaIoPrintStream.getMethod("void println(" + type
				+ ")");
		units.insertBefore(
				Jimple.v().newInvokeStmt(
						Jimple.v().newVirtualInvokeExpr(tmpRef,
								toCall.makeRef(), variable)), s);
	}
	

	static void dump(Object obj, String methodName) {
		try {
			File file = new File("output" + obj.getClass().getName()
					+ methodName + ".json");

			System.out
					.println(obj.getClass().getName() + "  " + obj.toString());

			FileWriter output = new FileWriter(file, true);
			if (!file.exists()) {
				file.createNewFile();
			}

			Gson gson = new Gson(); // ?????????????????
			String objOutput = gson.toJson(obj);
			output.write(objOutput);
			output.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	String getObjectType(String className, String packageName) {
		className = className.replace("Test", "");
		String[] split = className.split("_");
		return packageName + "." + split[0];
	}

	boolean isComplexMethod(Body body) {

		PatchingChain<Unit> units = body.getUnits();
		Iterator<Unit> stmtIt = units.snapshotIterator();

		while (stmtIt.hasNext()) {
			Stmt s = (Stmt) stmtIt.next();
			if (s instanceof InvokeStmt) {
				return true;
			}

			if (s instanceof DefinitionStmt) {
				// Value lhsOp = ((DefinitionStmt) s).getLeftOp();
				Value rhsOp = ((DefinitionStmt) s).getRightOp();
				if (rhsOp instanceof InvokeExpr) {
					return true;

				}
			}
		}
		return false;

	}

}
