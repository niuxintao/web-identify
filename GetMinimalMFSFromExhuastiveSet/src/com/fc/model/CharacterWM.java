package com.fc.model;

import com.fc.testObject.TestCase;
import com.fc.testObject.TestSuite;
import com.fc.testObject.TestSuiteImplement;
import com.fc.tuple.CorpTupleWithTestCase;
import com.fc.tuple.Tuple;

/**
 * the class describle the basic workflow
 * 
 * @author xintao Niu
 * 
 */
public class CharacterWM {

	protected TuplePool pool;
	protected CorpTupleWithTestCase generate;

	protected TestSuite extraCases;
	protected TestCase lastTestCase;
	protected Tuple tupleUnderTest;

	public CharacterWM(TuplePool pool, CorpTupleWithTestCase generate) {
		this.pool = pool;
		this.generate = generate;
		extraCases = new TestSuiteImplement();
	}

	/**
	 * inital task
	 */
	protected void inital() {

	}

	/**
	 * select a tuple that under test
	 * 
	 * @return
	 */
	protected Tuple seletctTupleUnderTest() {
		// a banch mark random select
		return null;
	}

	/**
	 * generate test case that contain the specific tuple
	 * 
	 * @param tuple
	 * @return
	 */
	public TestCase generateTestCaseContainTuple(Tuple tuple) {
		// choose as different possible as from the original test case
		//System.out.println("gen");
		TestCase gen = generate.generateTestCaseContainTuple(tuple);
		//System.out.println("end");
		extraCases.addTest(gen);
		return gen;
	}

	public TestSuite getExtraCases() {
		return extraCases;
	}

	public void setExtraCases(TestSuite extraCases) {
		this.extraCases = extraCases;
	}

	public void setLastTestCase(boolean isPass) {
		if (isPass == true) {
			lastTestCase.setTestState(TestCase.PASSED);
			this.extraDealAfterRight(tupleUnderTest);
		} else {
			this.extraDealAfterBug(tupleUnderTest);
			lastTestCase.setTestState(TestCase.FAILED);
		}
	}

	public TestCase genNextTest() {
		tupleUnderTest = this.seletctTupleUnderTest();
		if (tupleUnderTest == null)
			lastTestCase = null;
		else
			lastTestCase = this.generateTestCaseContainTuple(tupleUnderTest);
		return lastTestCase;
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

	public TuplePool getPool() {
		return pool;
	}
}
