package com.fc.TRT;

import java.util.List;
import java.util.Random;


import com.fc.testObject.TestCase;
import com.fc.testObject.TestSuite;
import com.fc.testObject.TestSuiteImplement;
import com.fc.tuple.Tuple;

/**
 * the class describle the basic workflow
 * 
 * @author xintao Niu
 * 
 */
public class CharacterWorkMachine {

	protected TreeStruct tree;
	protected CaseRunner runner;
	protected CorpTupleWithTestCase generate;
	
	private Random random;
	
	protected TestSuite extraCases;

	public CharacterWorkMachine(TreeStruct tree,CaseRunner runner,CorpTupleWithTestCase generate){
		this.tree = tree;
		this.runner = runner;
		this.generate = generate;
		
		extraCases = new TestSuiteImplement();
		random = new Random();
	}
	/**
	 * inital task
	 */
	protected void inital() {
		
	}

	/**
	 * detect any tuple that is not detemine right or bug exit
	 * 
	 * @return
	 */
	protected boolean isTupleUnderTestExist() {
		return this.tree.isNodeUnderTest();
	}

	/**
	 * select a tuple that under test
	 * 
	 * @return
	 */
	protected Tuple seletctTupleUnderTest() {
		// a banch mark random select
		Tuple[] tuples = (Tuple[]) this.tree.getUntestedTuple().toArray(new Tuple[tree.getUntestedTuple().size()]);
		int index = random.nextInt(tuples.length);
		return tuples[index];
	}

	/**
	 * generate test case that contain the specific tuple
	 * 
	 * @param tuple
	 * @return
	 */
	protected TestCase generateTestCaseContainTuple(Tuple tuple) {
		// choose as different possible as from the original test case
		TestCase gen =  generate.generateTestCaseContainTuple(tuple);
		extraCases.addTest(gen);
		return gen;
	}

	public TestSuite getExtraCases() {
		return extraCases;
	}
	public void setExtraCases(TestSuite extraCases) {
		this.extraCases = extraCases;
	}
	/**
	 * run the test and return the result
	 * 
	 * @param testCase
	 * @return
	 */
	protected boolean runTest(TestCase testCase) {
		runner.runTestCase(testCase);
		return testCase.testDescription() == TestCase.PASSED;
	}

	/**
	 * if the case is failed,then this method make all its fathers and Itself
	 * bug
	 * 
	 * @param tuple
	 */
	protected void setAllFathersAndItselfBug(Tuple tuple) {
		tree.setNodeState(tuple, TreeStruct.WRONG);
		List<Tuple> fathers = tree.getAllFathers(tuple);
		for (Tuple father : fathers) {
			tree.setNodeState(father, TreeStruct.WRONG);
		}
	}

	/**
	 * if the case passed , then this method make all its children and Itself
	 * pass
	 * 
	 * @param tuple
	 */
	protected void setAllChildrenAndItselfRight(Tuple tuple) {
		tree.setNodeState(tuple, TreeStruct.RIGHT);
		List<Tuple> children = tree.getAllchidren(tuple);
		for (Tuple child : children) {
			tree.setNodeState(child, TreeStruct.RIGHT);
		}
	}

	/**
	 * extra process after the case is fail
	 * 
	 * @param tuple
	 */
	protected void extraDealAfterBug(Tuple tuple) {

	}

	/**
	 * extra process after the case is pass
	 * 
	 * @param tuple
	 */
	protected void extraDealAfterRight(Tuple tuple) {

	}

	/**
	 * basic process work flow
	 */
	public void process() {
		this.inital();
		while (this.isTupleUnderTestExist()) {
			Tuple tupleUnderTest = this.seletctTupleUnderTest();
			TestCase testCase = this
					.generateTestCaseContainTuple(tupleUnderTest);
			if (this.runTest(testCase)) {
				/* testcase pass */
			//	System.out.println("right");
				this.setAllChildrenAndItselfRight(tupleUnderTest);
				this.extraDealAfterRight(tupleUnderTest);
			} else {
				/* testcase fail */
				//System.out.println("bug");
				this.setAllFathersAndItselfBug(tupleUnderTest);
				this.extraDealAfterBug(tupleUnderTest);
			}
		}
	}

}
