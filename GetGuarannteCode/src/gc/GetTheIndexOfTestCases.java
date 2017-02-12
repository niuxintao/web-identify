package gc;

import com.fc.testObject.TestCase;
import com.fc.testObject.TestCaseImplement;

public class GetTheIndexOfTestCases {

	public int[] getMulti(int[] param) {
		int[] result = new int[param.length];
		for (int i = 0; i < param.length; i++) {
			int ti = 1;
			for (int j = i + 1; j < param.length; j++)
				ti *= param[j];
			result[i] = ti;
		}
		return result;

	}

	public int getIndexofTestCase(TestCase testCase, int[] param) {
		int[] multi = this.getMulti(param);
		int result = 0;
		for (int i = 0; i < param.length; i++) {
			int cur = testCase.getAt(i);
			result += cur * multi[i];
//			System.out.print(cur + "*" + multi[i] + "=" + cur * multi[i] +" , ");
		}
//		System.out.println();
		return result;
	}
	
	public static void main(String[] args){
		int[] param = { 3, 2, 5};
		TestCase testCase = new TestCaseImplement(
				new int[] { 1, 1, 1});
		GetTheIndexOfTestCases a = new GetTheIndexOfTestCases();
		System.out.println(a.getIndexofTestCase(testCase, param));
	}
}
