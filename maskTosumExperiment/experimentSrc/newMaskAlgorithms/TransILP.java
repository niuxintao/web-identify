package newMaskAlgorithms;

//import com.fc.tuple.Tuple;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.fc.testObject.TestCase;
import com.fc.testObject.TestCaseImplement;

import net.sf.javailp.Linear;
import net.sf.javailp.OptType;
import net.sf.javailp.Problem;
import net.sf.javailp.Result;
import net.sf.javailp.Solver;
import net.sf.javailp.SolverFactory;
import net.sf.javailp.SolverFactoryLpSolve;

public class TransILP {

	private Problem problem;
	private SolverFactory factory;

	private int allNum;
	private int[] Vindex;
	private int fault;
	private int[] param;
	private int[] fixed;
	private HashMap<Integer, double[]> allMaxtrix;
	private TestCase wrongCase;
	private List<TestCase> executed;

	// private int[] param;

	public TransILP(int[] param, int[] Vindex,
			HashMap<Integer, double[]> allMaxtrix, int fault,
			TestCase wrongCase, int[] fixed, List<TestCase> executed) {
		factory = new SolverFactoryLpSolve(); // use lp_solve
		factory.setParameter(Solver.VERBOSE, 0);
		factory.setParameter(Solver.TIMEOUT, 100); // set timeout to 100 seconds
		this.param = param;
		this.Vindex = Vindex;
		this.allMaxtrix = allMaxtrix;
		this.fault = fault;
		this.fixed = fixed;
		this.allNum = Vindex[Vindex.length - 1] + param[param.length - 1];

		this.wrongCase = wrongCase;
		this.executed = executed;

		// System.out.println("param");
		// for (int i : param) {
		// System.out.print(i + " ");
		// }
		// System.out.println();
		// System.out.println("Vindex");
		// for (int i : Vindex) {
		// System.out.print(i + " ");
		// }
		// System.out.println();
		// System.out.println("fixed");
		// for (int i : fixed) {
		// System.out.print(i + " ");
		// }
		// System.out.println();
		// System.out.println("fault");
		// System.out.println(fault);
		// System.out.println("maxtrix");
		// for (Integer key : allMaxtrix.keySet()) {
		// System.out.println("key: " + key);
		// int cur = 0;
		// for (int i = 0; i < allMaxtrix.get(key).length; i++) {
		// double o = allMaxtrix.get(key)[i];
		// String chara = "";
		// if (cur < Vindex.length && i == Vindex[cur]) {
		// chara = " | ";
		// cur++;
		// }
		// System.out.print(chara + o + "  ");
		//
		// }
		// System.out.println();
		// // for (double o : allMaxtrix.get(key)) {
		// // System.out.print(o + "  ");
		// // }
		// // System.out.println();
		// }
	}

	public TransILP() {
		factory = new SolverFactoryLpSolve(); // use lp_solve
		factory.setParameter(Solver.VERBOSE, 0);
		factory.setParameter(Solver.TIMEOUT, 100); // set timeout to 100 seconds
	}

	public TestCase run() {
		// int faultNum = allMaxtrix.keySet().size();
		Result last = null;

		// System.out.println("maxtrix");
		// for (Integer key : allMaxtrix.keySet()) {
		// System.out.println("key: " + key);
		// int cur = 0;
		// for (int i = 0; i < allMaxtrix.get(key).length; i++) {
		// double o = allMaxtrix.get(key)[i];
		// String chara = "";
		// if (cur < Vindex.length && i == Vindex[cur]) {
		// chara = " | ";
		// cur++;
		// }
		// System.out.print(chara + o + "  ");
		//
		// }
		// System.out.println();
		// }
		@SuppressWarnings("unused")
		int faultM = -1;
		if (allMaxtrix.keySet().size() <= 1) {
			this.setup(param, Vindex, allMaxtrix, fixed, allNum);
			last = this.solve(Vindex, allNum);
		} else {
			for (int i : allMaxtrix.keySet()) {
				if (i != fault) {
					this.setup(param, Vindex, allMaxtrix, i, fault, fixed,
							allNum);
					Result result = this.solve(Vindex, allNum);
					if (result != null)
						if (last == null
								|| result.getObjective().doubleValue() < last
										.getObjective().doubleValue()) {
							last = result;
							faultM = i;
						}
				}
			}

			// System.out.println("result");
			// System.out.println(last);
			// System.out.println(faultM);
		}

		if (last == null)
			return null;
//		System.out.print("minimal maximal: " + faultM + " ");
//		System.out.println(last);
		int[] test = this.extractTest(Vindex, allNum, last);
		TestCase testCase = this.getTestCase(test);
		this.executed.add(testCase);
		return testCase;
	}

	public TestCase getTestCase(int[] n) {
		TestCaseImplement gen = new TestCaseImplement();
		gen.setTestCase(n);

		return gen;
	}

	// public void setup() {
	//
	// }
	//
	// public void setTestPart(Tuple tuple, int[] param, int[] Vindex,
	// float[][] allMaxtrix) {
	//
	// }

	/**
	 * 
	 * @param param
	 *            :SUT param, e.g., (3 3 3 3)
	 * @param Vindex
	 *            :the index for the SUT e.g. (0, 3, 6, 9)
	 * @param allMaxtrix
	 *            : the matrix for the related strength for each factor
	 *            float[][], the first array is specific fault, second is for
	 *            each factor
	 * @param fault
	 *            : the fault that full fill that SUM( O(fault, o) ) > others.
	 *            it is one of the computing, we should compare each fault.
	 * @param fixed
	 *            : the fixed part that should not change
	 */

	// need to fix the fault that should not to compare
	public void setup(int[] param, int[] Vindex,
			HashMap<Integer, double[]> allMaxtrix, int fault, int faultIgnore,
			int[] fixed, int allNum) {
		problem = new Problem();
		// if (allMaxtrix.length <= 0)
		// throw new Exception("Matrix is not permitted to be null");

		// allNum = allMaxtrix[0].length;
		// if (Vindex != null)
		// this.Vindex = Vindex;

		// set the object
		Linear linear = new Linear();
		for (int i = 0; i < allNum; i++) {
			linear.add(allMaxtrix.get(fault)[i], "x" + i);
		}
		problem.setObjective(linear, OptType.MIN);

		// set the constraint

		// each factor should only take one value
		for (int i = 0; i < param.length; i++) {
			int bond = Vindex[i];
			int num = param[i];
			linear = new Linear();
			for (int j = 0; j < num; j++) {
				linear.add(1, "x" + (bond + j));
			}
			problem.add(linear, "=", 1);
		}

		// fixed should not change
		for (int i : fixed) {
			linear = new Linear();
			linear.add(1, "x" + i);
			problem.add(linear, "=", 1);
		}

		// the final matrix should be the max
		for (int i : allMaxtrix.keySet()) {
			if (i == fault || i == faultIgnore)
				continue;
			double[] other = allMaxtrix.get(i);
			double[] mMaxt = allMaxtrix.get(fault);
			double[] minusMaxtrix = new double[allNum];
			for (int j = 0; j < allNum; j++) {
				minusMaxtrix[j] = mMaxt[j] - other[j];
			}
			linear = new Linear();
			for (int j = 0; j < allNum; j++) {
				linear.add(minusMaxtrix[j], "x" + j);
			}
			problem.add(linear, ">=", 0);
		}

		// the non fixed part of the original test case should not be selected
		int[] unfixed = getUnfixed(wrongCase, Vindex, fixed);
		for (int i : unfixed) {
			linear = new Linear();
			linear.add(1, "x" + i);
			problem.add(linear, "=", 0);
		}

		// we should not generate the test cases in the executed
		for (TestCase exi : this.executed) {
			linear = new Linear();
			int[] test = getUnfixed(exi, Vindex, new int[] {});
			for (int j = 0; j < test.length; j++) {
				linear.add(1, "x" + test[j]);
			}
			problem.add(linear, "<=", test.length - 1);
		}

		// each factor should be 0 - 1
		for (int i = 0; i < allNum; i++) {
			problem.setVarType("x" + i, Integer.class);
			problem.setVarUpperBound("x" + i, 1);
		}
	}

	private int[] getUnfixed(TestCase wrongCase, int[] Vindex, int[] fixed) {
		int[] unfixed = new int[wrongCase.getLength() - fixed.length];
		int cur = 0;
		for (int i = 0; i < wrongCase.getLength(); i++) {
			int maxtrixIndex = Vindex[i] + wrongCase.getAt(i);
			boolean isUnfixed = true;
			for (int j : fixed) {
				if (maxtrixIndex == j) {
					isUnfixed = false;
					break;
				}
			}
			if (isUnfixed) {
				unfixed[cur] = maxtrixIndex;
				cur++;
			}
		}
		return unfixed;
	}

	/**
	 * there is no other fault, we should make the next test case to pass as
	 * most possible
	 * 
	 * @param param
	 * @param Vindex
	 * @param allMaxtrix
	 * @param fixed
	 * @param allNum
	 */
	public void setup(int[] param, int[] Vindex,
			HashMap<Integer, double[]> allMaxtrix, int[] fixed, int allNum) {
		problem = new Problem();
		// if (allMaxtrix.length <= 0)
		// throw new Exception("Matrix is not permitted to be null");

		// allNum = allMaxtrix[0].length;
		// if (Vindex != null)
		// this.Vindex = Vindex;

		// set the object
		Linear linear = new Linear();
		for (int i = 0; i < allNum; i++) {
			linear.add(allMaxtrix.get(fault)[i], "x" + i);
		}
		problem.setObjective(linear, OptType.MIN);

		// set the constraint

		// each factor should only take one value
		for (int i = 0; i < param.length; i++) {
			int bond = Vindex[i];
			int num = param[i];
			linear = new Linear();
			for (int j = 0; j < num; j++) {
				linear.add(1, "x" + (bond + j));
			}
			problem.add(linear, "=", 1);
		}

		// fixed should not change
		for (int i : fixed) {
			linear = new Linear();
			linear.add(1, "x" + i);
			problem.add(linear, "=", 1);
		}

		// the non fixed part of the original test case should not be selected
		int[] unfixed = getUnfixed(wrongCase, Vindex, fixed);
		for (int i : unfixed) {
			linear = new Linear();
			linear.add(1, "x" + i);
			problem.add(linear, "=", 0);
		}

		// we should not generate the test cases in the executed
		for (TestCase exi : this.executed) {
			linear = new Linear();
			int[] test = getUnfixed(exi, Vindex, new int[] {});
			for (int j = 0; j < test.length; j++) {
				linear.add(1, "x" + test[j]);
			}
			problem.add(linear, "<=", test.length - 1);
		}

		// each factor should be 0 - 1
		for (int i = 0; i < allNum; i++) {
			problem.setVarType("x" + i, Integer.class);
			problem.setVarUpperBound("x" + i, 1);
		}
	}

	public Result solve(int[] Vindex, int allNum) {

		Solver solver = factory.get(); // you should use this solver only once
		// for one problem
		Result result = solver.solve(problem);
		// System.out.println(result);
		if (result != null) {
			extractTest(Vindex, allNum, result);

			// System.out.println();

			// System.out.println(result.getObjective());
		}
		return result;

	}

	private int[] extractTest(int[] Vindex, int allNum, Result result) {
		int[] num = new int[allNum];
		for (int i = 0; i < allNum; i++) {
			// System.out.print("x" + i + ":" + result.get("x" + i) + "  ");
			num[i] = (Integer) result.get("x" + i);
		}
		// System.out.println();

		int[] test = new int[Vindex.length];
		int cur = 0;
		for (int i = 0; i < num.length; i++) {
			if (num[i] == 1) {
				test[cur] = i - Vindex[cur];
				cur++;
			}
		}

		// System.out.println("test");
		// for (int i : test) {
		// System.out.print(i + " ");
		// }

		return test;
	}

	public static void test() {
		SolverFactory factory = new SolverFactoryLpSolve(); // use lp_solve
		factory.setParameter(Solver.VERBOSE, 0);
		factory.setParameter(Solver.TIMEOUT, 100); // set timeout to 100 seconds

		/**
		 * Constructing a Problem: Maximize: 143x+60y Subject to: 120x+210y <=
		 * 15000 110x+30y <= 4000 x+y <= 75
		 * 
		 * With x,y being integers
		 * 
		 */
		Problem problem = new Problem();

		Linear linear = new Linear();
		linear.add(143.1, "x");
		linear.add(60.23, "y");

		problem.setObjective(linear, OptType.MAX);

		linear = new Linear();
		linear.add(120.23, "x");
		linear.add(210.11, "y");

		problem.add(linear, "<=", 15000);

		linear = new Linear();
		linear.add(110, "x");
		linear.add(30, "y");

		problem.add(linear, "<=", 4000);

		linear = new Linear();
		linear.add(1, "x");
		linear.add(1, "y");

		problem.add(linear, "<=", 75);

		problem.setVarType("x", Integer.class);
		problem.setVarType("y", Integer.class);

		Solver solver = factory.get(); // you should use this solver only once
										// for one problem
		Result result = solver.solve(problem);

		System.out.println(result);

		/**
		 * Extend the problem with x <= 16 and solve it again
		 */
		problem.setVarUpperBound("x", 1);
		problem.setVarUpperBound("y", 1);

		solver = factory.get();
		result = solver.solve(problem);

		System.out.println(result);

		// Results in the following output:
		// Objective: 6266.0 {y=52, x=22}
		// Objective: 5828.0 {y=59, x=16}
	}

	public static void main(String[] args) {
		// TransILP.test();
		// TransILP.simpleExample(0);
		TransILP.simpleExample(1);
		TransILP.simpleExample(2);
		TransILP.simpleExample(3);

		int[] param = new int[] { 2, 2, 2, 2 };
		int[] Vindex = new int[param.length];
		int curr = 0;
		for (int i = 0; i < Vindex.length; i++) {
			Vindex[i] = curr;
			curr += param[i];
		}

		double[][] matrix = new double[][] {
				{ 0.0, 0.8, 0.6, 0.3, 0.7, 0.5, 0.1, 0.9 },
				{ 0.5, 0.7, 0.4, 0.3, 0.9, 0.5, 0.2, 0.3 },
				{ 0.8, 0.3, 0.3, 0.5, 0.9, 0.1, 0.4, 0.8 },
				{ 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0 } };

		HashMap<Integer, double[]> mm = new HashMap<Integer, double[]>();
		for (int i = 1; i <= matrix.length; i++) {
			mm.put(i, matrix[i - 1]);
		}

		// int fault = 1;

		int[] fixed = new int[] { 1, 4 };

		int[] test = new int[] { 1, 0, 0, 0 };

		TestCase testCase = new TestCaseImplement(test);

		int fault = 3;

		TransILP ilp = new TransILP(param, Vindex, mm, fault, testCase, fixed,
				new ArrayList<TestCase>());

		ilp.run();
	}

	public static void simpleExample(int fault) {
		TransILP ilp = new TransILP();
		int[] param = new int[] { 2, 2, 2, 2 };
		int[] Vindex = new int[param.length];
		int curr = 0;
		for (int i = 0; i < Vindex.length; i++) {
			Vindex[i] = curr;
			curr += param[i];
		}

		double[][] matrix = new double[][] {
				{ 0.0, 0.8, 0.6, 0.3, 0.7, 0.5, 0.1, 0.9 },
				{ 0.5, 0.7, 0.4, 0.3, 0.9, 0.5, 0.2, 0.3 },
				{ 0.8, 0.3, 0.3, 0.5, 0.9, 0.1, 0.4, 0.8 },
				{ 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0 } };
		HashMap<Integer, double[]> mm = new HashMap<Integer, double[]>();
		for (int i = 1; i <= matrix.length; i++) {
			mm.put(i, matrix[i - 1]);
		}
		// int fault = 1;

		int[] fixed = new int[] { 1, 4 };

		ilp.setup(param, Vindex, mm, fault, -1, fixed, matrix[0].length);

		ilp.solve(Vindex, matrix[0].length);
	}
}
