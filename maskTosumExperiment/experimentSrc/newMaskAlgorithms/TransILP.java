package newMaskAlgorithms;

//import com.fc.tuple.Tuple;

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
	private double[][] allMaxtrix;

	// private int[] param;

	public TransILP(int[] param, int[] Vindex, double[][] allMaxtrix,
			int fault, int[] fixed) {
		factory = new SolverFactoryLpSolve(); // use lp_solve
		factory.setParameter(Solver.VERBOSE, 0);
		factory.setParameter(Solver.TIMEOUT, 100); // set timeout to 100 seconds
		this.param = param;
		this.Vindex = Vindex;
		this.allMaxtrix = allMaxtrix;
		this.fault = fault;
		this.fixed = fixed;
		this.allNum = allMaxtrix[0].length;
	}

	public TransILP() {
		factory = new SolverFactoryLpSolve(); // use lp_solve
		factory.setParameter(Solver.VERBOSE, 0);
		factory.setParameter(Solver.TIMEOUT, 100); // set timeout to 100 seconds
	}

	public void run() {
		int faultNum = allMaxtrix.length;
		Result last = null;
		int faultM = -1;
		for (int i = 0; i < faultNum; i++) {
			if (i != fault) {
				this.setup(param, Vindex, allMaxtrix, i, fixed, allNum);
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
		System.out.println(last);
		System.out.println(faultM);
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
	public void setup(int[] param, int[] Vindex, double[][] allMaxtrix,
			int fault, int[] fixed, int allNum) {
		problem = new Problem();
		// if (allMaxtrix.length <= 0)
		// throw new Exception("Matrix is not permitted to be null");

		// allNum = allMaxtrix[0].length;
		// if (Vindex != null)
		// this.Vindex = Vindex;

		// set the object
		Linear linear = new Linear();
		for (int i = 0; i < allNum; i++) {
			linear.add(allMaxtrix[fault][i], "x" + i);
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
		for (int i = 0; i < allMaxtrix.length; i++) {
			if (i == fault)
				continue;
			double[] other = allMaxtrix[i];
			double[] mMaxt = allMaxtrix[fault];
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
		System.out.println(result);
		if (result != null) {
			int[] num = new int[allNum];
			for (int i = 0; i < allNum; i++) {
				System.out.print("x" + i + ":" + result.get("x" + i) + "  ");
				num[i] = (Integer) result.get("x" + i);
			}
			System.out.println();

			int[] test = new int[Vindex.length];
			int cur = 0;
			for (int i = 0; i < num.length; i++) {
				if (num[i] == 1) {
					test[cur] = i - Vindex[cur];
					cur++;
				}
			}

			System.out.println("test");
			for (int i : test) {
				System.out.print(i + " ");
			}

			System.out.println();

			System.out.println(result.getObjective());
		}
		return result;

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
		TransILP.test();

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

		int fault = 1;

		TransILP ilp = new TransILP(param, Vindex, matrix, fault, Vindex);

		ilp.run();
	}

	private static void simpleExample() {
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

		int fault = 1;

		int[] fixed = new int[] { 1, 4 };

		ilp.setup(param, Vindex, matrix, fault, fixed, matrix[0].length);

		ilp.solve(Vindex, matrix[0].length);
	}
}
