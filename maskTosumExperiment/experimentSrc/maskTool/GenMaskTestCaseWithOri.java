package maskTool;

import com.fc.testObject.TestCase;
import com.fc.testObject.TestCaseImplement;
import com.fc.tuple.Tuple;

public class GenMaskTestCaseWithOri {
	private int[] param;

	private TestCase wrongCase; // current wrong Case

	private int[] addedIndex;

	private int[] addedParam;

	private int nowStep;

	private int allStep;

	private Tuple fixTuple;

	public TestCase getWrongCase() {
		return wrongCase;
	}

	private void init(TestCase wrongCase, int[] param, Tuple tuple) {
		this.wrongCase = wrongCase;
		this.param = param;
		this.fixTuple = tuple;

		addedIndex = new int[param.length - fixTuple.getDegree()];
		addedParam = new int[addedIndex.length];

		for (int i = 0; i < addedIndex.length; i++)
			addedIndex[i] = 0;

		int j = 0;
		for (int i = 0; i < param.length; i++) {
			if (!this.isTheIndexInTuple(i, tuple)) {
				addedParam[j] = param[i] ;
				j++;
			}
		}

		nowStep = 0;

		allStep = 1;
		
		for (int i : addedParam)
			allStep *= i;
	}

	public boolean isStop() {
		return nowStep >= allStep;
	}

	public void step() {
		int i = addedIndex.length - 1;
		addedIndex[i] += 1;

		while (addedIndex[i] >= addedParam[i]) {
			addedIndex[i] = 0;
			i--;
			if (i < 0)
				break;
			addedIndex[i] += 1;
		}
		nowStep++;
	}

	public TestCase genenteTestCase(int[] added, Tuple tuple, TestCase wrongCase) {

		int[] t = ((TestCaseImplement) wrongCase).getTestCase();
		int[] n = new int[t.length];

		int j = 0;

		for (int i = 0; i < t.length; i++) {
			if (this.isTheIndexInTuple(i, tuple))
				n[i] = t[i];
			else {
				n[i] = (t[i] + added[j]) % param[i];
				j++;
			}
		}

		TestCaseImplement gen = new TestCaseImplement();
		gen.setTestCase(n);

		return gen;
	}

	public GenMaskTestCaseWithOri(TestCase wrongCase, int[] param, Tuple tuple) {
		init(wrongCase, param, tuple);
	}


	public TestCase generateTestCaseContainTuple(Tuple tuple) {
		/* generate a part test case just contain the tuple */
		TestCase result = this.genenteTestCase(addedIndex, tuple, wrongCase);
		this.step();
		
		return result;
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
