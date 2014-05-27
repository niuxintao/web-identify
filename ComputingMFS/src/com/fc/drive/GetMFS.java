package com.fc.drive;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import com.fc.testObject.TestCase;
import com.fc.testObject.TestCaseImplement;
import com.fc.testObject.TestSuite;
import com.fc.testObject.TestSuiteImplement;
import com.fc.tuple.Tuple;

public class GetMFS {

	public GetMFS() {

	}

	public void outputMFS(int[] param, TestSuite suite) {
		HashSet<Tuple> minimalSchemas = new HashSet<Tuple>();
		for (int i = 0; i < suite.getTestCaseNum(); i++) {
			Tuple root = this.getRoot(suite.getAt(i));
			if (!minimalSchemas.contains(root)) {
				minimalSchemas.add(root);
				List<Tuple> tuples = root.getAllChilds();
				for (Tuple tuple : tuples) {
					int shouldHit = this.getHitAll(tuple, param);
					int actual = this.getHitted(tuple, suite);
					if (shouldHit == actual)
						minimalSchemas.add(tuple);
				}
			}
		}

		this.compress(minimalSchemas);

		for (Tuple tuple : minimalSchemas)
			System.out.println(tuple.toString());

	}

	public void compress(HashSet<Tuple> mfss) {
		Iterator<Tuple> itr = mfss.iterator();
		while (itr.hasNext()) {
			Tuple tuple = itr.next();
			for (Tuple tu : mfss) {
				if (tuple.equals(tu))
					continue;
				if (tuple.contains(tu)) {
					itr.remove();
					break;
				}
			}
		}
	}

	public Tuple getRoot(TestCase testCase) {
		Tuple root = new Tuple(testCase.getLength(), testCase);
		for (int i = 0; i < root.getDegree(); i++)
			root.set(i, i);
		return root;
	}

	public int getHitted(Tuple tuple, TestSuite suite) {
		int result = 0;
		for (int i = 0; i < suite.getTestCaseNum(); i++)
			if (suite.getAt(i).containsOf(tuple))
				result++;
		return result;
	}

	public int getHitAll(Tuple tuple, int[] param) {
		int result = 1;
		Tuple reverse = tuple.getReverseTuple();
		for (int i = 0; i < reverse.getDegree(); i++)
			result *= param[reverse.getParamIndex()[i]];

		return result;
	}

	public TestSuite getSuite(int[][] suiteData) {
		TestSuite result = new TestSuiteImplement();
		HashSet<TestCase> testCases = new HashSet<TestCase>();
		for (int[] data : suiteData) {
			TestCaseImplement testCase = new TestCaseImplement();
			testCase.setTestCase(data);
			testCases.add(testCase);
		}

		for (TestCase testCase : testCases)
			result.addTest(testCase);
		return result;
	}

	public void test() {
		int[] param = new int[] { 2, 2, 2, 2, 2, 2 };
		int[][] suiteData = new int[][] { { 0, 0, 0, 1, 1, 0 },
				{ 0, 0, 1, 1, 0, 0 }, { 0, 0, 1, 0, 1, 0 },
				{ 0, 0, 1, 0, 0, 0 }, { 1, 1, 0, 0, 0, 0 },
				{ 0, 0, 1, 1, 0, 1 },

				{ 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 1, 0 },
				{ 0, 0, 0, 1, 0, 0 }, { 1, 1, 1, 1, 1, 0 },
				{ 1, 1, 0, 0, 1, 0 }, { 1, 1, 0, 1, 1, 0 },
				{ 1, 1, 1, 0, 0, 0 }, { 1, 1, 1, 1, 0, 0 },
				{ 1, 1, 1, 0, 1, 0 }, { 1, 0, 1, 1, 1, 1 },
				{ 0, 0, 0, 0, 1, 1 }, { 1, 0, 0, 0, 1, 1 },

				{ 0, 0, 1, 1, 1, 0 }, { 1, 1, 0, 1, 0, 0 },
				{ 1, 0, 1, 1, 0, 1 }, { 0, 0, 1, 1, 1, 1 },
				{ 1, 1, 0, 0, 1, 1 }, { 0, 1, 0, 0, 1, 1 },
				{ 1, 0, 1, 0, 1, 1 },

		};

		System.out.println("knowing masking effects");
		TestSuite suite = getSuite(suiteData);
		outputMFS(param, suite);

		int[][] suiteData2 = new int[][] { { 0, 0, 0, 0, 0, 0 },
				{ 0, 0, 0, 0, 1, 0 }, { 0, 0, 0, 1, 0, 0 },
				{ 1, 1, 1, 1, 1, 0 }, { 1, 1, 0, 0, 1, 0 },
				{ 1, 1, 0, 1, 1, 0 }, { 1, 1, 1, 0, 0, 0 },
				{ 1, 1, 1, 1, 0, 0 }, { 1, 1, 1, 0, 1, 0 },
				{ 1, 0, 1, 1, 1, 1 }, { 0, 0, 0, 0, 1, 1 },
				{ 1, 0, 0, 0, 1, 1 },

				{ 0, 0, 1, 1, 1, 0 }, { 1, 1, 0, 1, 0, 0 },
				{ 1, 0, 1, 1, 0, 1 }, { 0, 0, 1, 1, 1, 1 },
				{ 1, 1, 0, 0, 1, 1 }, { 0, 1, 0, 0, 1, 1 },
				{ 1, 0, 1, 0, 1, 1 },

				{ 0, 1, 1, 0, 0, 0 }, { 0, 1, 1, 0, 1, 0 },
				{ 0, 1, 1, 1, 0, 0 }, { 0, 1, 1, 1, 1, 0 },
				{ 1, 1, 1, 1, 1, 1 }, { 0, 1, 1, 1, 1, 1 },
				{ 1, 1, 1, 1, 0, 1 }, { 1, 0, 0, 1, 1, 1 }

		};
		System.out.println("actual MFS");
		suite = getSuite(suiteData2);
		outputMFS(param, suite);

		int[][] suiteData3 = new int[][] { { 0, 0, 0, 0, 0, 0 },
				{ 0, 0, 0, 0, 1, 0 }, { 0, 0, 0, 1, 0, 0 },
				{ 1, 1, 1, 1, 1, 0 }, { 1, 1, 0, 0, 1, 0 },
				{ 1, 1, 0, 1, 1, 0 }, { 1, 1, 1, 0, 0, 0 },
				{ 1, 1, 1, 1, 0, 0 }, { 1, 1, 1, 0, 1, 0 },
				{ 1, 0, 1, 1, 1, 1 }, { 0, 0, 0, 0, 1, 1 },
				{ 1, 0, 0, 0, 1, 1 },

				{ 0, 0, 0, 1, 1, 0 }, { 0, 0, 1, 1, 0, 0 },
				{ 0, 0, 1, 0, 1, 0 }, { 0, 0, 1, 0, 0, 0 },
				{ 1, 1, 0, 0, 0, 0 }, { 0, 0, 1, 1, 0, 1 },

		};
		System.out.println("distinguish faults");
		suite = getSuite(suiteData3);
		outputMFS(param, suite);
	}

	public void test2() {
		int[] param = new int[] { 2, 2, 2, 2, 2, 2 };
		int[][] suiteData = new int[][] { { 0, 0, 0, 1, 0, 0 },
				{ 0, 0, 0, 1, 1, 0 }, { 0, 0, 1, 0, 0, 0 },
				{ 0, 0, 1, 1, 0, 0 },

				{ 1, 1, 1, 0, 0, 0 }, { 1, 1, 1, 0, 1, 0 },
				{ 1, 1, 1, 1, 0, 0 }, { 1, 1, 1, 1, 1, 0 },
				{ 1, 0, 1, 1, 0, 0 }, { 1, 0, 1, 1, 1, 0 },
				{ 0, 0, 0, 0, 1, 0 }, { 0, 0, 0, 0, 0, 0 },
				{ 0, 0, 1, 1, 1, 1 }, { 0, 1, 1, 1, 1, 1 },

		};

		System.out.println("knowing masking effects");
		TestSuite suite = getSuite(suiteData);
		outputMFS(param, suite);

		int[][] suiteData2 = new int[][] { { 1, 1, 1, 0, 0, 0 },
				{ 1, 1, 1, 0, 1, 0 }, { 1, 1, 1, 1, 0, 0 },
				{ 1, 1, 1, 1, 1, 0 }, { 1, 0, 1, 1, 0, 0 },
				{ 1, 0, 1, 1, 1, 0 }, { 0, 0, 0, 0, 1, 0 },
				{ 0, 0, 0, 0, 0, 0 }, { 0, 0, 1, 1, 1, 1 },
				{ 0, 1, 1, 1, 1, 1 },

				{ 1, 1, 0, 0, 0, 0 }, { 1, 1, 0, 0, 1, 0 },
				{ 1, 1, 0, 1, 0, 0 }, { 1, 1, 0, 1, 1, 0 },
				{ 0, 0, 1, 1, 0, 1 }, { 0, 1, 1, 1, 0, 1 },

		};
		System.out.println("actual MFS");
		suite = getSuite(suiteData2);
		outputMFS(param, suite);

		int[][] suiteData3 = new int[][] { { 0, 0, 0, 1, 0, 0 },
				{ 0, 0, 0, 1, 1, 0 }, { 0, 0, 1, 0, 0, 0 },
				{ 0, 0, 1, 1, 0, 0 },

				{ 1, 1, 1, 0, 0, 0 }, { 1, 1, 1, 0, 1, 0 },
				{ 1, 1, 1, 1, 0, 0 }, { 1, 1, 1, 1, 1, 0 },
				{ 1, 0, 1, 1, 0, 0 }, { 1, 0, 1, 1, 1, 0 },
				{ 0, 0, 0, 0, 1, 0 }, { 0, 0, 0, 0, 0, 0 },
				{ 0, 0, 1, 1, 1, 1 }, { 0, 1, 1, 1, 1, 1 },

				{ 1, 0, 1, 0, 0, 0 }, { 1, 0, 1, 0, 1, 0 },
				{ 0, 0, 1, 0, 1, 0 }, { 0, 0, 1, 1, 1, 0 },
				{ 0, 1, 0, 0, 0, 0 }, { 0, 1, 0, 1, 0, 0 },
				{ 0, 1, 1, 0, 0, 0 }, { 0, 1, 1, 1, 0, 0 },
				{ 1, 0, 0, 0, 0, 0 }, { 1, 0, 0, 0, 1, 0 },
				{ 1, 1, 1, 1, 1, 1 }, { 1, 0, 1, 1, 1, 1 }

		};
		System.out.println("distinguish faults");
		suite = getSuite(suiteData3);
		outputMFS(param, suite);
	}

	public void te() {
		int[] param = new int[] { 2, 2, 2, 2 };
		int[][] suiteData = new int[][] { { 1, 1, 1, 1 }, { 1, 1, 1, 0 }};

		TestSuite suite = getSuite(suiteData);
		outputMFS(param, suite);
	}

	public static void main(String[] args) {
		GetMFS gmfs = new GetMFS();
		gmfs.test2();
	}
}
