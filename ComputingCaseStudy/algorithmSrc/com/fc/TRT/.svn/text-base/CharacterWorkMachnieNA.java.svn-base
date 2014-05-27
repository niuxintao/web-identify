package com.fc.TRT;

import com.fc.testObject.TestCase;
import com.fc.testObject.TestSuite;
import com.fc.testObject.TestSuiteImplement;
import com.fc.tuple.Tuple;

public class CharacterWorkMachnieNA extends CharacterWorkMachine {
	public static final double p_max = 0.9;
	public static final double p_import = 0.4;
	
	private TestSuite suiteContainNewBug;

	public CharacterWorkMachnieNA(TreeStruct tree, CaseRunner runner,
			CorpTupleWithTestCase generate) {
		super(tree, runner, generate);
		// TODO Auto-generated constructor stub
		suiteContainNewBug = new TestSuiteImplement();
	}

	/**
	 * @return the suiteContainNewBug
	 */
	public TestSuite getSuiteContainNewBug() {
		return suiteContainNewBug;
	}

	/**
	 * basic process work flow
	 */
	public void process() {
		this.inital();
		while (this.isTupleUnderTestExist()) {
			Tuple tupleUnderTest = this.seletctTupleUnderTest();
			TestSuite tempSuite = new TestSuiteImplement();
			double p_allImport = 1;
			TestCase testCase = this
					.generateTestCaseContainTuple(tupleUnderTest);
			boolean result = this.runTest(testCase);
			while (result == false) {
				tempSuite.addTest(testCase);
				p_allImport *= p_import;
				if (1 - p_allImport < p_max) {
					testCase = this
							.generateTestCaseContainTuple(tupleUnderTest,tempSuite);
					result = this.runTest(testCase);
				} else {
					break;
				}
			}
			if (1 - p_allImport < p_max) {
				/* testcase pass */
				this.setAllChildrenAndItselfRight(tupleUnderTest);
				this.extraDealAfterRight(tupleUnderTest);
				for (int i = 0; i < tempSuite.getTestCaseNum(); i++) {
					TestCase caseImport = tempSuite.getAt(i);
					suiteContainNewBug.addTest(caseImport);
				}

			} else {
				/* testcase fail */
				this.setAllFathersAndItselfBug(tupleUnderTest);
				this.extraDealAfterBug(tupleUnderTest);
			}

		}
	}
	
	
	
	protected TestCase generateTestCaseContainTuple(Tuple tuple, TestSuite suite) {
		// choose as different possible as from the original test case
		TestCase gen =  generate.generateTestCaseContainTuple(tuple,suite);
		extraCases.addTest(gen);
		return gen;
	}
}
