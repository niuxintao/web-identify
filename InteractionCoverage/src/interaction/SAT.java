package interaction;

import org.sat4j.core.VecInt;
import org.sat4j.pb.SolverFactory;
import org.sat4j.specs.ContradictionException;
import org.sat4j.specs.IProblem;
import org.sat4j.specs.ISolver;
import org.sat4j.specs.TimeoutException;

public class SAT {

	/**
	 * 
	 * @param valueNum
	 * @param Clauses
	 *            a, b, c ,d object: (a || b) && (!b || c) && (d || c || a)
	 *            clauses: {1, 2} {-2, 3} {4, 3, 1}
	 * 
	 * @return
	 */
	boolean issatisfied(int valueNum, int[][] Clauses) {
		ISolver solver = SolverFactory.newDefault();
		// prepare the solver to accept MAXVAR variables . MANDATORY
		solver.newVar(valueNum);
		// not mandatory for SAT solving . MANDATORY for MAXSAT solving
		solver.setExpectedNumberOfClauses(Clauses.length);
		for (int[] cla : Clauses) {
			try {
				solver.addClause(new VecInt(cla));

			} catch (ContradictionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		IProblem problem = solver;
		try {
			return problem.isSatisfiable();
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 
	 * @param valueNum
	 * @param Clauses
	 * @param combination
	 *            the combination you test is whether consistent (a, b ,c )
	 * 
	 *            {1, 2, 3}
	 * @return
	 */
	boolean issatisfied(int valueNum, int[][] Clauses, int[] combination) {
		ISolver solver = SolverFactory.newDefault();
		// prepare the solver to accept MAXVAR variables . MANDATORY
		solver.newVar(valueNum);
		// not mandatory for SAT solving . MANDATORY for MAXSAT solving
		solver.setExpectedNumberOfClauses(Clauses.length + combination.length);
		try {
			for (int[] cla : Clauses) {

				solver.addClause(new VecInt(cla));
			}

			// note that each combination's factor need to be 1
			for (int i : combination) {
				int[] c = new int[1];
				c[0] = i;
				// System.out.println("c" + " " +c[0]);
				solver.addClause(new VecInt(c));
			}
		} catch (ContradictionException e1) {
			// TODO Auto-generated catch block
			// e1.printStackTrace();
			return false;
		}

		IProblem problem = solver;
		try {
			return problem.isSatisfiable();
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
}
