package interaction;

import org.sat4j.core.VecInt;
import org.sat4j.pb.SolverFactory;
import org.sat4j.specs.ContradictionException;
import org.sat4j.specs.IProblem;
import org.sat4j.specs.ISolver;
import org.sat4j.specs.TimeoutException;

public class TestMiniSat {

	public static void main(String[] args) throws ContradictionException,
			TimeoutException {
		final int MAXVAR = 8;
		final int NBCLAUSES = 1;
		ISolver solver = SolverFactory.newDefault();
		// prepare the solver to accept MAXVAR variables . MANDATORY
		solver.newVar(MAXVAR);
		// not mandatory for SAT solving . MANDATORY for MAXSAT solving
		solver.setExpectedNumberOfClauses(1);
		// Feed the solver using Dimacs format , using arrays of int
		// ( best option to avoid dependencies on SAT4J IVecInt )
		int[][] path2 = new int[][] { { 2 } };
		for (int i = 0; i < NBCLAUSES; i++) {
			int[] clause = path2[i]; // get the clause from somewhere

			// the clause should not contain a 0,
			// only integer ( positive or negative )
			// with absolute values less or equal to MAXVAR
			// e.g. int [] clause = {1, -3, 7}; is fine
			// while int [] clause = {1, -3, 7, 0}; is not fine
			solver.addClause(new VecInt(clause)); // adapt Array to IVecInt
		}

		solver.addClause(new VecInt(new int[] { 1, 2 }));
		solver.addClause(new VecInt(new int[] { 3, 4 }));
		solver.addClause(new VecInt(new int[] { 5, 6 }));
		solver.addClause(new VecInt(new int[] { 7, 8 }));

		solver.addClause(new VecInt(new int[] { -1, -2 }));
		solver.addClause(new VecInt(new int[] { -3, -4 }));
		solver.addClause(new VecInt(new int[] { -5, -6 }));
		solver.addClause(new VecInt(new int[] { -7, -8 }));

		solver.addClause(new VecInt(new int[] { 1 }));
		solver.addClause(new VecInt(new int[] { 3 }));
		solver.addClause(new VecInt(new int[] { 5 }));

		// we are done . Working now on the IProblem interface
		IProblem problem = solver;
		if (problem.isSatisfiable()) {
			System.out.println("ok");
		} else {

		}
	}
}
