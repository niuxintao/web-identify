package maskTool;

import java.util.HashSet;

import com.fc.testObject.TestCase;
import com.fc.testObject.TestCaseImplement;
import com.fc.tuple.Tuple;

public class GenTestCaseNotContainMFS {
	private HashSet<Tuple> mfss;
	public HashSet<Tuple> getMfss() {
		return mfss;
	}

	public int[] getParam() {
		return param;
	}

	private int[] param;

	public GenTestCaseNotContainMFS(HashSet<Tuple> mfss, int[] param) {
		this.mfss = mfss;
		this.param = param;
	}

	public TestCase genTestCase() {
		TestCaseImplement testCase = new TestCaseImplement();
		
		

		return testCase;
	}
	

}
