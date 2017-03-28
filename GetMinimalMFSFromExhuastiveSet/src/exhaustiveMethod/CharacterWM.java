package exhaustiveMethod;

import com.fc.model.TuplePool;
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
public class CharacterWM {

	protected TuplePool pool;
	protected CorpTupleWithTestCase generate;

	protected TestSuite extraCases;

	protected TestCase lastTestCase;
	protected Tuple tupleUnderTest;
	
	protected boolean sure = true;

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
	protected TestCase generateTestCaseContainTuple(Tuple tuple) {
		// choose as different possible as from the original test case
		TestCase gen = generate.generateTestCaseContainTuple(tuple);
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
//		System.out.println("the tuple : "+ tupleUnderTest.toString());
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
	 * 
	 * @param tuple
	 */
	protected void extraDealAfterRight(Tuple tuple) {

	}

	/**
	 * extra process after the case is fail
	 * 
	 * @param tuple
	 */
	protected void extraDealAfterFail() {
		if (this.generate.tryGenerateNext(tupleUnderTest).equals(
				generate.getWrongCase())) {
			this.sure = true;
			this.extraDealAfterBug(tupleUnderTest);
		}else{
			this.sure = false;
		}
	}

	public TuplePool getPool() {
		return pool;
	}
}
