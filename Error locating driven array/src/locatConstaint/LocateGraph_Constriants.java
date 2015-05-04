package locatConstaint;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.fc.caseRunner.CaseRunner;
import com.fc.caseRunner.CaseRunnerWithBugInject;
import com.fc.testObject.TestCase;
import com.fc.testObject.TestCaseImplement;
import com.fc.testObject.TestSuite;
import com.fc.testObject.TestSuiteImplement;
import com.fc.tuple.Tuple;

public class LocateGraph_Constriants {
	private CaseRunner caseRunner;
	private TestSuite addtionalTestSuite;

	public LocateGraph_Constriants(CaseRunner caseRunner) {
		this.caseRunner = caseRunner;
		addtionalTestSuite = new TestSuiteImplement();
	}

	public List<Tuple> locateErrorsInTest(TestCase safeTest,
			TestCase wrongCase, Tuple tuple) {
		if (tuple.getDegree() == 1) {
			List<Tuple> edge = new ArrayList<Tuple>();
			edge.add(tuple);
			return edge;
		} else {
			Tuple[] tuples = this.getSplit(tuple, wrongCase);
			TestCase case1 = this.generateTestCase(safeTest, tuples[0]);
			TestCase case2 = this.generateTestCase(safeTest, tuples[1]);
			List<Tuple> edge1 = new ArrayList<Tuple>();
			List<Tuple> edge2 = new ArrayList<Tuple>();
			if (!this.test(case1))
				edge1 = this.locateErrorsInTest(safeTest, case1, tuples[0]);
			if (!this.test(case2))
				edge2 = this.locateErrorsInTest(safeTest, case2, tuples[1]);

			List<Tuple> edge3 = new ArrayList<Tuple>();
			edge3 = this.acrossLocate(safeTest, wrongCase, tuples[0],
					tuples[1], edge1, edge2);

			List<Tuple> list = new ArrayList<Tuple>();
			list.addAll(edge1);
			list.addAll(edge2);
			list.addAll(edge3);
			return list;
		}

	}

	public List<Tuple> acrossLocate(TestCase safeTest, TestCase wrongCase,
			Tuple tuple1, Tuple tuple2, List<Tuple> edge1, List<Tuple> edge2) {
		List<Tuple> result = new ArrayList<Tuple>();

		Tuple B1 = this.getTupleByRemove(tuple1, edge1, wrongCase);
		Tuple B2 = this.getTupleByRemove(tuple2, edge2, wrongCase);

		List<Tuple> C1 = this.colouringEdges(B1, edge1, wrongCase);
		List<Tuple> C2 = this.colouringEdges(B2, edge2, wrongCase);

		for (Tuple c1 : C1)
			for (Tuple c2 : C2) {
				Tuple tuple = c1.catComm(c1, c2);
				TestCase testCase = this.generateTestCase(safeTest, tuple);
				if (!this.test(testCase))
					result.addAll(this.acrosslocateAux(safeTest, testCase, c1,
							c2));
			}

		return result;
	}

	public List<Tuple> acrosslocateAux(TestCase safeTest, TestCase testCase,
			Tuple c1, Tuple c2) {
		if (c1.getDegree() == 1 && c2.getDegree() == 1) {

			List<Tuple> tuples = new ArrayList<Tuple>();
			tuples.add(c1.catComm(c1, c2));
			return tuples;
		}

		Tuple parti = null;
		Tuple remain = null;
		if (c1.getDegree() == 1) {
			parti = c2;
			remain = c1;
		} else {
			parti = c1;
			remain = c2;
		}

		Tuple[] tuples = this.getSplit(parti, testCase);
		List<Tuple> E1 = new ArrayList<Tuple>();
		List<Tuple> E2 = new ArrayList<Tuple>();

		TestCase case1 = this.generateTestCaseReverse(testCase, safeTest,
				tuples[1]);
		if (!this.test(case1))
			E1 = this.acrosslocateAux(safeTest, case1, remain, tuples[0]);

		TestCase case2 = this.generateTestCaseReverse(testCase, safeTest,
				tuples[0]);
		if (!this.test(case2))
			E2 = this.acrosslocateAux(safeTest, case2, remain, tuples[1]);

		List<Tuple> result = new ArrayList<Tuple>();
		result.addAll(E1);
		result.addAll(E2);
		return result;
	}

	public Tuple[] getSplit(Tuple tuple, TestCase testCase) {
		int degree = tuple.getDegree();
		int bdegree = degree / 2;
		int adegree = degree - bdegree;
		Tuple A = new Tuple(adegree, testCase);
		Tuple B = new Tuple(bdegree, testCase);
		for (int i = 0; i < A.getDegree(); i++) {
			A.set(i, tuple.getParamIndex()[i]);
		}
		for (int i = 0; i < B.getDegree(); i++) {
			B.set(i, tuple.getParamIndex()[i + A.getDegree()]);
		}

		Tuple[] result = new Tuple[2];
		result[0] = A;
		result[1] = B;

		return result;
	}

	public Tuple getTupleByRemove(Tuple tuple, List<Tuple> tuples,
			TestCase wrongCase) {
		List<Integer> temp = new ArrayList<Integer>();
		for (int i = 0; i < tuple.getDegree(); i++)
			temp.add(tuple.getParamIndex()[i]);
		Iterator<Integer> itr = temp.iterator();
		while (itr.hasNext()) {
			int i = itr.next();
			if (hasOneFactorEdge(i, tuples))
				itr.remove();
		}
		Tuple result = new Tuple(temp.size(), wrongCase);
		for (int i = 0; i < temp.size(); i++) {
			result.set(i, temp.get(i));
		}
		return result;
	}

	public boolean hasOneFactorEdge(int i, List<Tuple> tuples) {
		for (Tuple tuple : tuples) {
			if (tuple.getDegree() == 1 && tuple.getParamIndex()[0] == i)
				return true;
		}
		return false;
	}

	public List<Tuple> colouringEdges(Tuple tuple, List<Tuple> edges,
			TestCase wrongCase) {
		List<Tuple> set = new ArrayList<Tuple>();
		for (int i : tuple.getParamIndex()) {
			// find its neigbours in set, j is the number
			boolean findNumber = false;
			Tuple temp = new Tuple(1, wrongCase);
			temp.set(0, i);
			int j = 0;
			for (; j < set.size(); j++) {
				Tuple tu = set.get(j);
				boolean findFlag = isInEdegesFromTuple(i, tu, edges);
				if (!findFlag) {
					findNumber = true;
					break;
				}
			}
			if (findNumber) {
				Tuple tu = set.get(j);
				tu = tu.cat(tu, temp);
				set.set(j, tu);
			} else
				set.add(temp);
		}

		return set;
	}

	private boolean isInEdegesFromTuple(int i, Tuple tu, List<Tuple> edges) {
		boolean findFlag = false;
		for (int k = 0; k < tu.getDegree(); k++) {
			if (this.isInEdges(i, tu.getParamIndex()[k], edges)) {
				findFlag = true;
				break;
			}
		}
		return findFlag;
	}

	public boolean isInEdges(int i, int j, List<Tuple> edges) {
		boolean result = false;
		for (Tuple tuple : edges) {
			if (this.isInEdge(i, j, tuple)) {
				result = true;
				break;
			}
		}
		return result;

	}

	public boolean isInEdge(int i, int j, Tuple tuple) {
		boolean result = false;
		if (tuple.getParamIndex()[0] == (i < j ? i : j)
				&& tuple.getParamIndex()[1] == (i < j ? j : i))
			result = true;
		return result;
	}

	public Tuple sequnce(Tuple tuple, List<Tuple> edges) {
		return tuple;
	}

	public boolean test(TestCase testCase) {
		testCase.setTestState(caseRunner.runTestCase(testCase));
		getAddtionalTestSuite().addTest(testCase);
		return testCase.testDescription() == TestCase.PASSED;
	}

	public TestCase generateTestCase(TestCase safeTest, Tuple tuple) {
		TestCase result = new TestCaseImplement(safeTest.getLength());
		for (int i = 0; i < safeTest.getLength(); i++)
			result.set(i, safeTest.getAt(i));
		for (int i = 0; i < tuple.getDegree(); i++)
			result.set(tuple.getParamIndex()[i], tuple.getParamValue()[i]);
		return result;
	}

	public TestCase generateTestCaseReverse(TestCase wrongCase,
			TestCase safeTest, Tuple tuple) {
		TestCase result = new TestCaseImplement(wrongCase.getLength());
		for (int i = 0; i < wrongCase.getLength(); i++)
			result.set(i, wrongCase.getAt(i));
		for (int i = 0; i < tuple.getDegree(); i++)
			result.set(tuple.getParamIndex()[i],
					safeTest.getAt(tuple.getParamIndex()[i]));
		return result;
	}

	public static void main(String[] args) {
		int[] wrong = new int[] { 1, 1, 1, 1, 1, 1, 1, 1 };
		TestCase wrongCase = new TestCaseImplement();
		((TestCaseImplement) wrongCase).setTestCase(wrong);

		int[] pass = new int[] { 0, 0, 0, 0, 0, 0, 0, 0 };
		TestCase rightCase = new TestCaseImplement();
		((TestCaseImplement) rightCase).setTestCase(pass);

		Tuple bugModel1 = new Tuple(2, wrongCase);
		bugModel1.set(0, 2);
		bugModel1.set(1, 7);

		Tuple bugModel2 = new Tuple(2, wrongCase);
		bugModel2.set(0, 3);
		bugModel2.set(1, 7);

		CaseRunner caseRunner = new CaseRunnerWithBugInject();
		((CaseRunnerWithBugInject) caseRunner).inject(bugModel1);
		((CaseRunnerWithBugInject) caseRunner).inject(bugModel2);
		LocateGraph_Constriants lg = new LocateGraph_Constriants(caseRunner);

		Tuple tuple = new Tuple(0, wrongCase);
		tuple = tuple.getReverseTuple();

		List<Tuple> bugs = lg.locateErrorsInTest(rightCase, wrongCase, tuple);
		System.out.println("bugs:");
		for (Tuple tu : bugs)
			System.out.println(tu.toString());

		System.out.println("testCases:");
		for (int i = 0; i < lg.getAddtionalTestSuite().getTestCaseNum(); i++)
			System.out.println(lg.getAddtionalTestSuite().getAt(i)
					.getStringOfTest());

	}

	public TestSuite getAddtionalTestSuite() {
		return addtionalTestSuite;
	}

}

