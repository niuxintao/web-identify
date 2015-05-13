package experimentData;

import interaction.DataCenter;

import java.util.ArrayList;
import java.util.List;

import com.fc.caseRunner.CaseRunner;
import com.fc.caseRunner.CaseRunnerWithBugInject;
import com.fc.testObject.TestCase;
import com.fc.testObject.TestCaseImplement;
import com.fc.tuple.Tuple;

public class TcasData implements ExperimentData {

	private int[] param;
	private CaseRunner caseRunner;
	private List<Tuple> realMFS;
	private DataCenter dataCenter;

	public TcasData() {
		this.init();
	}

	public DataCenter getDataCenter() {
		return dataCenter;
	}

	public void init() {

		this.param = new int[] { 3, 2, 2, 2, 2, 2, 4, 10, 10, 3, 2, 2 };

		realMFS = new ArrayList<Tuple>();

		this.caseRunner = new CaseRunnerWithBugInject();
		
		/***
		 * 
[ 2 , 1 , - , 0 , 0 , 1 , 0 , 3 , 2 , - , 1 , - ]
[ 2 , 1 , - , 0 , 0 , 1 , 0 , 4 , 2 , - , 1 , - ]
[ 2 , 1 , - , 0 , 0 , 1 , 0 , 5 , 2 , - , 1 , - ]
[ 2 , 1 , - , 0 , 0 , 1 , 0 , 6 , 2 , - , 1 , - ]
[ 2 , 1 , - , 0 , 0 , 1 , 0 , 7 , 2 , - , 1 , - ]
[ 2 , 1 , - , 0 , 0 , 1 , 0 , 8 , 2 , - , 1 , - ]
[ 2 , 1 , - , 0 , 0 , 1 , 0 , 9 , 2 , - , 1 , - ]
[ 2 , 1 , - , 0 , 0 , 1 , 0 , 1 , 2 , - , 1 , 1 ]
[ 2 , 1 , - , 0 , 0 , 1 , 0 , 2 , 2 , - , 1 , 1 ]
[ 2 , 1 , - , 0 , 0 , 1 , 1 , 5 , 4 , - , 1 , - ]
[ 2 , 1 , - , 0 , 0 , 1 , 1 , 6 , 4 , - , 1 , - ]
[ 2 , 1 , - , 0 , 0 , 1 , 1 , 7 , 4 , - , 1 , - ]
[ 2 , 1 , - , 0 , 0 , 1 , 1 , 8 , 4 , - , 1 , - ]
[ 2 , 1 , - , 0 , 0 , 1 , 1 , 9 , 4 , - , 1 , - ]
[ 2 , 1 , - , 0 , 0 , 1 , 1 , 3 , 4 , - , 1 , 1 ]
[ 2 , 1 , - , 0 , 0 , 1 , 1 , 4 , 4 , - , 1 , 1 ]
[ 2 , 1 , - , 0 , 0 , 1 , 2 , 7 , 6 , - , 1 , - ]
[ 2 , 1 , - , 0 , 0 , 1 , 2 , 8 , 6 , - , 1 , - ]
[ 2 , 1 , - , 0 , 0 , 1 , 2 , 9 , 6 , - , 1 , - ]
[ 2 , 1 , - , 0 , 0 , 1 , 2 , 5 , 6 , - , 1 , 1 ]
[ 2 , 1 , - , 0 , 0 , 1 , 2 , 6 , 6 , - , 1 , 1 ]
[ 2 , 1 , - , 0 , 0 , 1 , 3 , 9 , 8 , - , 1 , - ]
[ 2 , 1 , - , 0 , 0 , 1 , 3 , 7 , 8 , - , 1 , 1 ]
[ 2 , 1 , - , 0 , 0 , 1 , 3 , 8 , 8 , - , 1 , 1 ]
[ 2 , 1 , 1 , 0 , 0 , 1 , 0 , 3 , 2 , 0 , 0 , - ]
[ 2 , 1 , 1 , 0 , 0 , 1 , 0 , 4 , 2 , 0 , 0 , - ]
[ 2 , 1 , 1 , 0 , 0 , 1 , 0 , 5 , 2 , 0 , 0 , - ]
[ 2 , 1 , 1 , 0 , 0 , 1 , 0 , 6 , 2 , 0 , 0 , - ]
[ 2 , 1 , 1 , 0 , 0 , 1 , 0 , 7 , 2 , 0 , 0 , - ]
[ 2 , 1 , 1 , 0 , 0 , 1 , 0 , 8 , 2 , 0 , 0 , - ]
[ 2 , 1 , 1 , 0 , 0 , 1 , 0 , 9 , 2 , 0 , 0 , - ]
[ 2 , 1 , 1 , 0 , 0 , 1 , 0 , 1 , 2 , 0 , 0 , 1 ]
[ 2 , 1 , 1 , 0 , 0 , 1 , 0 , 2 , 2 , 0 , 0 , 1 ]
[ 2 , 1 , 1 , 0 , 0 , 1 , 1 , 5 , 4 , 0 , 0 , - ]
[ 2 , 1 , 1 , 0 , 0 , 1 , 1 , 6 , 4 , 0 , 0 , - ]
[ 2 , 1 , 1 , 0 , 0 , 1 , 1 , 7 , 4 , 0 , 0 , - ]
[ 2 , 1 , 1 , 0 , 0 , 1 , 1 , 8 , 4 , 0 , 0 , - ]
[ 2 , 1 , 1 , 0 , 0 , 1 , 1 , 9 , 4 , 0 , 0 , - ]
[ 2 , 1 , 1 , 0 , 0 , 1 , 1 , 3 , 4 , 0 , 0 , 1 ]
[ 2 , 1 , 1 , 0 , 0 , 1 , 1 , 4 , 4 , 0 , 0 , 1 ]
[ 2 , 1 , 1 , 0 , 0 , 1 , 2 , 7 , 6 , 0 , 0 , - ]
[ 2 , 1 , 1 , 0 , 0 , 1 , 2 , 8 , 6 , 0 , 0 , - ]
[ 2 , 1 , 1 , 0 , 0 , 1 , 2 , 9 , 6 , 0 , 0 , - ]
[ 2 , 1 , 1 , 0 , 0 , 1 , 2 , 5 , 6 , 0 , 0 , 1 ]
[ 2 , 1 , 1 , 0 , 0 , 1 , 2 , 6 , 6 , 0 , 0 , 1 ]
[ 2 , 1 , 1 , 0 , 0 , 1 , 3 , 9 , 8 , 0 , 0 , - ]
[ 2 , 1 , 1 , 0 , 0 , 1 , 3 , 7 , 8 , 0 , 0 , 1 ]
[ 2 , 1 , 1 , 0 , 0 , 1 , 3 , 8 , 8 , 0 , 0 , 1 ]
		 * 
		 */

		// 7
		for (int i = 3; i < 10; i++) {
			int[] wrong = new int[] { 2, 1, 1, 0, 0, 1, 0, i, 2, 0, 1, 1 };
			TestCase wrongCase = new TestCaseImplement();
			((TestCaseImplement) wrongCase).setTestCase(wrong);
			Tuple bugModel1 = new Tuple(9, wrongCase);
			bugModel1.set(0, 0);
			bugModel1.set(1, 1);
			bugModel1.set(2, 3);
			bugModel1.set(3, 4);
			bugModel1.set(4, 5);
			bugModel1.set(5, 6);
			bugModel1.set(6, 7);
			bugModel1.set(7, 8);
			bugModel1.set(8, 10);

			realMFS.add(bugModel1);
			((CaseRunnerWithBugInject) caseRunner).inject(bugModel1);
		}

		// 2
		for (int i = 1; i < 3; i++) {
			int[] wrong = new int[] { 2, 1, 1, 0, 0, 1, 0, i, 2, 0, 1, 1 };
			TestCase wrongCase = new TestCaseImplement();
			((TestCaseImplement) wrongCase).setTestCase(wrong);
			Tuple bugModel1 = new Tuple(10, wrongCase);
			bugModel1.set(0, 0);
			bugModel1.set(1, 1);
			bugModel1.set(2, 3);
			bugModel1.set(3, 4);
			bugModel1.set(4, 5);
			bugModel1.set(5, 6);
			bugModel1.set(6, 7);
			bugModel1.set(7, 8);
			bugModel1.set(8, 10);
			bugModel1.set(9, 11);
			realMFS.add(bugModel1);
			((CaseRunnerWithBugInject) caseRunner).inject(bugModel1);
		}

		// 5
		for (int i = 5; i < 10; i++) {
			int[] wrong = new int[] { 2, 1, 1, 0, 0, 1, 1, i, 4, 0, 1, 1 };
			TestCase wrongCase = new TestCaseImplement();
			((TestCaseImplement) wrongCase).setTestCase(wrong);
			Tuple bugModel1 = new Tuple(9, wrongCase);
			bugModel1.set(0, 0);
			bugModel1.set(1, 1);
			bugModel1.set(2, 3);
			bugModel1.set(3, 4);
			bugModel1.set(4, 5);
			bugModel1.set(5, 6);
			bugModel1.set(6, 7);
			bugModel1.set(7, 8);
			bugModel1.set(8, 10);

			realMFS.add(bugModel1);
			((CaseRunnerWithBugInject) caseRunner).inject(bugModel1);
		}

		// 2
		for (int i = 3; i < 5; i++) {
			int[] wrong = new int[] { 2, 1, 1, 0, 0, 1, 1, i, 4, 0, 1, 1 };
			TestCase wrongCase = new TestCaseImplement();
			((TestCaseImplement) wrongCase).setTestCase(wrong);
			Tuple bugModel1 = new Tuple(10, wrongCase);
			bugModel1.set(0, 0);
			bugModel1.set(1, 1);
			bugModel1.set(2, 3);
			bugModel1.set(3, 4);
			bugModel1.set(4, 5);
			bugModel1.set(5, 6);
			bugModel1.set(6, 7);
			bugModel1.set(7, 8);
			bugModel1.set(8, 10);
			bugModel1.set(9, 11);
			realMFS.add(bugModel1);
			((CaseRunnerWithBugInject) caseRunner).inject(bugModel1);
		}

		// 3
		for (int i = 7; i < 10; i++) {
			int[] wrong = new int[] { 2, 1, 1, 0, 0, 1, 2, i, 6, 0, 1, 1 };
			TestCase wrongCase = new TestCaseImplement();
			((TestCaseImplement) wrongCase).setTestCase(wrong);
			Tuple bugModel1 = new Tuple(9, wrongCase);
			bugModel1.set(0, 0);
			bugModel1.set(1, 1);
			bugModel1.set(2, 3);
			bugModel1.set(3, 4);
			bugModel1.set(4, 5);
			bugModel1.set(5, 6);
			bugModel1.set(6, 7);
			bugModel1.set(7, 8);
			bugModel1.set(8, 10);

			realMFS.add(bugModel1);
			((CaseRunnerWithBugInject) caseRunner).inject(bugModel1);
		}

		// 2
		for (int i = 5; i < 7; i++) {
			int[] wrong = new int[] { 2, 1, 1, 0, 0, 1, 2, i, 6, 0, 1, 1 };
			TestCase wrongCase = new TestCaseImplement();
			((TestCaseImplement) wrongCase).setTestCase(wrong);
			Tuple bugModel1 = new Tuple(10, wrongCase);
			bugModel1.set(0, 0);
			bugModel1.set(1, 1);
			bugModel1.set(2, 3);
			bugModel1.set(3, 4);
			bugModel1.set(4, 5);
			bugModel1.set(5, 6);
			bugModel1.set(6, 7);
			bugModel1.set(7, 8);
			bugModel1.set(8, 10);
			bugModel1.set(9, 11);
			realMFS.add(bugModel1);
			((CaseRunnerWithBugInject) caseRunner).inject(bugModel1);
		}

		// 1
		for (int i = 9; i < 10; i++) {
			int[] wrong = new int[] { 2, 1, 1, 0, 0, 1, 3, i, 8, 0, 1, 1 };
			TestCase wrongCase = new TestCaseImplement();
			((TestCaseImplement) wrongCase).setTestCase(wrong);
			Tuple bugModel1 = new Tuple(9, wrongCase);
			bugModel1.set(0, 0);
			bugModel1.set(1, 1);
			bugModel1.set(2, 3);
			bugModel1.set(3, 4);
			bugModel1.set(4, 5);
			bugModel1.set(5, 6);
			bugModel1.set(6, 7);
			bugModel1.set(7, 8);
			bugModel1.set(8, 10);

			realMFS.add(bugModel1);
			((CaseRunnerWithBugInject) caseRunner).inject(bugModel1);
		}

		// 2
		for (int i = 7; i < 9; i++) {
			int[] wrong = new int[] { 2, 1, 1, 0, 0, 1, 3, i, 8, 0, 1, 1 };
			TestCase wrongCase = new TestCaseImplement();
			((TestCaseImplement) wrongCase).setTestCase(wrong);
			Tuple bugModel1 = new Tuple(10, wrongCase);
			bugModel1.set(0, 0);
			bugModel1.set(1, 1);
			bugModel1.set(2, 3);
			bugModel1.set(3, 4);
			bugModel1.set(4, 5);
			bugModel1.set(5, 6);
			bugModel1.set(6, 7);
			bugModel1.set(7, 8);
			bugModel1.set(8, 10);
			bugModel1.set(9, 11);
			realMFS.add(bugModel1);
			((CaseRunnerWithBugInject) caseRunner).inject(bugModel1);
		}

		// another
		// 7
		for (int i = 3; i < 10; i++) {
			int[] wrong = new int[] { 2, 1, 1, 0, 0, 1, 0, i, 2, 0, 0, 1 };
			TestCase wrongCase = new TestCaseImplement();
			((TestCaseImplement) wrongCase).setTestCase(wrong);
			Tuple bugModel1 = new Tuple(11, wrongCase);
			bugModel1.set(0, 0);
			bugModel1.set(1, 1);
			bugModel1.set(2, 2);
			bugModel1.set(3, 3);
			bugModel1.set(4, 4);
			bugModel1.set(5, 5);
			bugModel1.set(6, 6);
			bugModel1.set(7, 7);
			bugModel1.set(8, 8);
			bugModel1.set(9, 9);
			bugModel1.set(10, 10);

			realMFS.add(bugModel1);
			((CaseRunnerWithBugInject) caseRunner).inject(bugModel1);
		}

		// 2
		for (int i = 1; i < 3; i++) {
			int[] wrong = new int[] { 2, 1, 1, 0, 0, 1, 0, i, 2, 0, 0, 1 };
			TestCase wrongCase = new TestCaseImplement();
			((TestCaseImplement) wrongCase).setTestCase(wrong);
			Tuple bugModel1 = new Tuple(12, wrongCase);
			bugModel1.set(0, 0);
			bugModel1.set(1, 1);
			bugModel1.set(2, 2);
			bugModel1.set(3, 3);
			bugModel1.set(4, 4);
			bugModel1.set(5, 5);
			bugModel1.set(6, 6);
			bugModel1.set(7, 7);
			bugModel1.set(8, 8);
			bugModel1.set(9, 9);
			bugModel1.set(10, 10);
			bugModel1.set(11, 11);

			realMFS.add(bugModel1);
			((CaseRunnerWithBugInject) caseRunner).inject(bugModel1);
		}

		// 5
		for (int i = 5; i < 10; i++) {
			int[] wrong = new int[] { 2, 1, 1, 0, 0, 1, 1, i, 4, 0, 0, 1 };
			TestCase wrongCase = new TestCaseImplement();
			((TestCaseImplement) wrongCase).setTestCase(wrong);
			Tuple bugModel1 = new Tuple(11, wrongCase);
			bugModel1.set(0, 0);
			bugModel1.set(1, 1);
			bugModel1.set(2, 2);
			bugModel1.set(3, 3);
			bugModel1.set(4, 4);
			bugModel1.set(5, 5);
			bugModel1.set(6, 6);
			bugModel1.set(7, 7);
			bugModel1.set(8, 8);
			bugModel1.set(9, 9);
			bugModel1.set(10, 10);

			realMFS.add(bugModel1);
			((CaseRunnerWithBugInject) caseRunner).inject(bugModel1);
		}

		// 2
		for (int i = 3; i < 5; i++) {
			int[] wrong = new int[] { 2, 1, 1, 0, 0, 1, 1, i, 4, 0, 0, 1 };
			TestCase wrongCase = new TestCaseImplement();
			((TestCaseImplement) wrongCase).setTestCase(wrong);
			Tuple bugModel1 = new Tuple(12, wrongCase);
			bugModel1.set(0, 0);
			bugModel1.set(1, 1);
			bugModel1.set(2, 2);
			bugModel1.set(3, 3);
			bugModel1.set(4, 4);
			bugModel1.set(5, 5);
			bugModel1.set(6, 6);
			bugModel1.set(7, 7);
			bugModel1.set(8, 8);
			bugModel1.set(9, 9);
			bugModel1.set(10, 10);
			bugModel1.set(11, 11);
			realMFS.add(bugModel1);
			((CaseRunnerWithBugInject) caseRunner).inject(bugModel1);
		}

		// 3
		for (int i = 7; i < 10; i++) {
			int[] wrong = new int[] { 2, 1, 1, 0, 0, 1, 2, i, 6, 0, 0, 1 };
			TestCase wrongCase = new TestCaseImplement();
			((TestCaseImplement) wrongCase).setTestCase(wrong);
			Tuple bugModel1 = new Tuple(11, wrongCase);
			bugModel1.set(0, 0);
			bugModel1.set(1, 1);
			bugModel1.set(2, 2);
			bugModel1.set(3, 3);
			bugModel1.set(4, 4);
			bugModel1.set(5, 5);
			bugModel1.set(6, 6);
			bugModel1.set(7, 7);
			bugModel1.set(8, 8);
			bugModel1.set(9, 9);
			bugModel1.set(10, 10);

			realMFS.add(bugModel1);
			((CaseRunnerWithBugInject) caseRunner).inject(bugModel1);
		}

		// 2
		for (int i = 5; i < 7; i++) {
			int[] wrong = new int[] { 2, 1, 1, 0, 0, 1, 2, i, 6, 0, 0, 1 };
			TestCase wrongCase = new TestCaseImplement();
			((TestCaseImplement) wrongCase).setTestCase(wrong);
			Tuple bugModel1 = new Tuple(12, wrongCase);
			bugModel1.set(0, 0);
			bugModel1.set(1, 1);
			bugModel1.set(2, 2);
			bugModel1.set(3, 3);
			bugModel1.set(4, 4);
			bugModel1.set(5, 5);
			bugModel1.set(6, 6);
			bugModel1.set(7, 7);
			bugModel1.set(8, 8);
			bugModel1.set(9, 9);
			bugModel1.set(10, 10);
			bugModel1.set(11, 11);
			realMFS.add(bugModel1);
			((CaseRunnerWithBugInject) caseRunner).inject(bugModel1);
		}

		// 1
		for (int i = 9; i < 10; i++) {
			int[] wrong = new int[] { 2, 1, 1, 0, 0, 1, 3, i, 8, 0, 0, 1 };
			TestCase wrongCase = new TestCaseImplement();
			((TestCaseImplement) wrongCase).setTestCase(wrong);
			Tuple bugModel1 = new Tuple(11, wrongCase);
			bugModel1.set(0, 0);
			bugModel1.set(1, 1);
			bugModel1.set(2, 2);
			bugModel1.set(3, 3);
			bugModel1.set(4, 4);
			bugModel1.set(5, 5);
			bugModel1.set(6, 6);
			bugModel1.set(7, 7);
			bugModel1.set(8, 8);
			bugModel1.set(9, 9);
			bugModel1.set(10, 10);

			realMFS.add(bugModel1);
			((CaseRunnerWithBugInject) caseRunner).inject(bugModel1);
		}

		// 2
		for (int i = 7; i < 9; i++) {
			int[] wrong = new int[] { 2, 1, 1, 0, 0, 1, 3, i, 8, 0, 0, 1 };
			TestCase wrongCase = new TestCaseImplement();
			((TestCaseImplement) wrongCase).setTestCase(wrong);
			Tuple bugModel1 = new Tuple(12, wrongCase);
			bugModel1.set(0, 0);
			bugModel1.set(1, 1);
			bugModel1.set(2, 2);
			bugModel1.set(3, 3);
			bugModel1.set(4, 4);
			bugModel1.set(5, 5);
			bugModel1.set(6, 6);
			bugModel1.set(7, 7);
			bugModel1.set(8, 8);
			bugModel1.set(9, 9);
			bugModel1.set(10, 10);
			bugModel1.set(11, 11);
			realMFS.add(bugModel1);
			((CaseRunnerWithBugInject) caseRunner).inject(bugModel1);
		}

	}

	public static void main(String[] str) {
		TcasData tcasData = new TcasData();
		tcasData.init();
		System.out.println(tcasData.realMFS.size());
		for (Tuple mfs : tcasData.realMFS)
			System.out.println(mfs);
	}

	public void setDegree(int degree) {
		this.dataCenter = new DataCenter(param, degree);
	}

	public int[] getParam() {
		return param;
	}

	public CaseRunner getCaseRunner() {
		return caseRunner;
	}

	public List<Tuple> getRealMFS() {
		return realMFS;
	}

}
