package test;

import com.fc.process.DriverForAnalyse;
import com.fc.testObject.TestCaseImplement;
import com.fc.testObject.TestSuite;
import com.fc.testObject.TestSuiteImplement;
import com.fc.tuple.Tuple;
import org.junit.Before;
import org.junit.Test;

public class coverageTest {
	private DriverForAnalyse driver;
	private int[] param;
	private TestSuite suite;
	private Tuple tuple;

	@Before
	public void set() {
		this.driver = new DriverForAnalyse();
		byte paramNum = 3;
		this.param = new int[paramNum];

		for (int data = 0; data < paramNum; ++data) {
			this.param[data] = 2;
		}

		this.suite = new TestSuiteImplement();
		int[][] arg4 = new int[][]{ {0, 0, 0}, {1, 1, 1},
				{1, 0, 1}, {0, 1, 0}, {0, 1, 1}, {1, 0, 0}};

		for (int i = 0; i < arg4.length; ++i) {
			TestCaseImplement testCase = new TestCaseImplement();
			((TestCaseImplement) testCase).setTestCase(arg4[i]);
			this.suite.addTest(testCase);
		}
		
		int[] bugTest = {0, 0, 0};
		TestCaseImplement testCase = new TestCaseImplement();
		testCase.setTestCase(bugTest);
		this.tuple = new Tuple(3, testCase);
		tuple.setParamIndex(new int[]{0, 1, 2});

	}

	@Test
	public void test() {
		byte degree = 3;
		this.driver.set(this.param, degree, this.suite, this.tuple);
		System.out.println("coverage : " + this.driver.analyseCoverage());
		System.out.println("diversity : " + this.driver.analyseDiversity());
		System.out.println("detection rate : " + this.driver.analyseDetectionRate());
		System.out.println("bug detect : " + (this.driver.analyseBugModeDetect()));
	}
}