package exhaustiveMethod;

import java.util.List;

import com.fc.testObject.TestCase;
import com.fc.testObject.TestCaseImplement;
import com.fc.tuple.Tuple;

public class CorpTupleWithTestCase {
	private int[] param;

	private TestCase wrongCase; // current wrong Case

	public TestCase getWrongCase() {
		return wrongCase;
	}

	private TestCase lastCase;

	private void init(TestCase wrongCase, int[] param) {
		this.wrongCase = wrongCase;
		lastCase = wrongCase;
		this.param = param;
	}

	public void reset() {
		lastCase = wrongCase;
	}

	public CorpTupleWithTestCase(TestCase wrongCase, int[] param,
			List<Tuple> currentBugs) {
		init(wrongCase, param);
	}

	public CorpTupleWithTestCase(TestCase wrongCase, int[] param) {
		init(wrongCase, param);
	}

	public TestCase generateTestCaseContainTuple(Tuple tuple) {
		/* generate a part test case just contain the tuple */
		TestCase next = this.generateNextTestCase(lastCase, getParam(), tuple);
		lastCase = next;
		return next;
	}

	public TestCase tryGenerateNext(Tuple tuple) {
		TestCase next = this.generateNextTestCase(lastCase, getParam(), tuple);
		return next;
	}

	public TestCase generateNextTestCase(TestCase testCase, int[] param,
			Tuple tuple) {
		int[] t = ((TestCaseImplement) testCase).getTestCase();
		int[] n = new int[t.length];
		for (int i = 0; i < t.length; i++)
			n[i] = t[i];

		int currIndex = n.length - 1;

		while (this.isTheIndexInTuple(currIndex, tuple))
			currIndex--;

		if (currIndex < 0)
			return null;

		n[currIndex] += 1;

		while (n[currIndex] >= param[currIndex]) {
			n[currIndex] = 0;
			currIndex -= 1;

			while (this.isTheIndexInTuple(currIndex, tuple))
				currIndex--;

			if (currIndex < 0)
				break;
			n[currIndex] += 1;
		}

		TestCaseImplement next = new TestCaseImplement();
		next.setTestCase(n);
		return next;
	}

	public boolean isTheIndexInTuple(int index, Tuple tuple) {
		int[] indexes = tuple.getParamIndex();
		for (int i : indexes) {
			if (index > i) {
				continue;
			} else if (index == i) {
				return true;
			} else if (index < i) {
				break;
			}
		}
		return false;
	}

	public int[] getParam() {
		return param;
	}

	public void setParam(int[] param) {
		this.param = param;
	}
}
