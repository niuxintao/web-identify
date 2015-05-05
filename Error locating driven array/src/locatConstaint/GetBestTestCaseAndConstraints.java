package locatConstaint;

import com.fc.testObject.TestCase;
import com.fc.tuple.Tuple;

import ct.AETG_Constraints;

public class GetBestTestCaseAndConstraints {
	private AETG_Constraints ac;

	
	/*******************************************************/
	/*****************************************************/
	@SuppressWarnings("unused")
	private TestCase original;

	public GetBestTestCaseAndConstraints(AETG_Constraints ac, TestCase original) {
		this.ac = ac;
		this.original = original;
	}

	public int[] getTestCase(Tuple partial) {
		int[] testCase = ac.getBestTestCase(partial);

		return testCase;
	}
}
