package experimentData;

import interaction.DataCenter;

import java.util.ArrayList;
import java.util.List;

import com.fc.caseRunner.CaseRunner;
import com.fc.caseRunner.CaseRunnerWithBugInject;
import com.fc.testObject.TestCase;
import com.fc.testObject.TestCaseImplement;
import com.fc.tuple.Tuple;

public class GccData implements ExperimentData {

	private int[] param;
	private CaseRunner caseRunner;
	private List<Tuple> realMFS;
	private DataCenter dataCenter;

	public GccData() {
		this.init();
	}

	public DataCenter getDataCenter() {
		return dataCenter;
	}

	public void init() {

		this.param = new int[] { 6, 2, 2, 2, 2, 2, 2, 2, 2, 2 };

		realMFS = new ArrayList<Tuple>();

		this.caseRunner = new CaseRunnerWithBugInject();

		int[] wrong1 = new int[] { 2, 0, 0, 0, 0, 1, 0, 0, 0, 0 };// MFS
		TestCase wrongCase1 = new TestCaseImplement();
		((TestCaseImplement) wrongCase1).setTestCase(wrong1);

		Tuple bugModel1 = new Tuple(3, wrongCase1);
		bugModel1.set(0, 0);
		bugModel1.set(1, 5);
		bugModel1.set(2, 9);
		
		int[] wrong2 = new int[] { 2, 0, 0, 1, 0, 0, 0, 0, 0, 0 };// MFS
		TestCase wrongCase2 = new TestCaseImplement();
		((TestCaseImplement) wrongCase2).setTestCase(wrong2);

		Tuple bugModel2 = new Tuple(3, wrongCase2);
		bugModel2.set(0, 0);
		bugModel2.set(1, 3);
		bugModel2.set(2, 9);


		int[] wrong3 = new int[] { 3, 0, 0, 0, 0, 1, 0, 0, 0, 0 };// MFS
		TestCase wrongCase3 = new TestCaseImplement();
		((TestCaseImplement) wrongCase3).setTestCase(wrong3);

		Tuple bugModel3 = new Tuple(3, wrongCase3);
		bugModel3.set(0, 0);
		bugModel3.set(1, 5);
		bugModel3.set(2, 9);

		int[] wrong4 = new int[] { 3, 0, 0, 1, 0, 0, 0, 0, 0, 0 };// MFS
		TestCase wrongCase4 = new TestCaseImplement();
		((TestCaseImplement) wrongCase4).setTestCase(wrong4);

		Tuple bugModel4 = new Tuple(3, wrongCase4);
		bugModel4.set(0, 0);
		bugModel4.set(1, 3);
		bugModel4.set(2, 9);

//		int[] wrong5 = new int[] { 2, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0 };// MFS
//		TestCase wrongCase5 = new TestCaseImplement();
//		((TestCaseImplement) wrongCase5).setTestCase(wrong5);
//
//		Tuple bugModel5 = new Tuple(3, wrongCase5);
//		bugModel5.set(0, 0);
//		bugModel5.set(1, 4);
//		bugModel5.set(2, 11);
//		
//		int[] wrong6 = new int[] { 2, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0 };// MFS
//		TestCase wrongCase6 = new TestCaseImplement();
//		((TestCaseImplement) wrongCase6).setTestCase(wrong6);
//
//		Tuple bugModel6 = new Tuple(3, wrongCase6);
//		bugModel6.set(0, 0);
//		bugModel6.set(1, 2);
//		bugModel6.set(2, 11);

//		int[] wrong7 = new int[] { 2, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };// MFS
//		TestCase wrongCase7 = new TestCaseImplement();
//		((TestCaseImplement) wrongCase7).setTestCase(wrong7);
//
//		Tuple bugModel7 = new Tuple(3, wrongCase7);
//		bugModel7.set(0, 0);
//		bugModel7.set(1, 1);
//		bugModel7.set(2, 11);
//
//		int[] wrong8 = new int[] { 3, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0 };// MFS
//		TestCase wrongCase8 = new TestCaseImplement();
//		((TestCaseImplement) wrongCase8).setTestCase(wrong8);
//
//		Tuple bugModel8 = new Tuple(3, wrongCase8);
//		bugModel8.set(0, 0);
//		bugModel8.set(1, 6);
//		bugModel8.set(2, 11);
//
//		int[] wrong9 = new int[] { 3, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0 };// MFS
//		TestCase wrongCase9 = new TestCaseImplement();
//		((TestCaseImplement) wrongCase9).setTestCase(wrong9);
//
//		Tuple bugModel9 = new Tuple(3, wrongCase9);
//		bugModel9.set(0, 0);
//		bugModel9.set(1, 5);
//		bugModel9.set(2, 11);
//
//		int[] wrong10 = new int[] { 3, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0 };// MFS
//		TestCase wrongCase10 = new TestCaseImplement();
//		((TestCaseImplement) wrongCase10).setTestCase(wrong10);
//
//		Tuple bugModel10 = new Tuple(3, wrongCase10);
//		bugModel10.set(0, 0);
//		bugModel10.set(1, 4);
//		bugModel10.set(2, 11);
//		
//		int[] wrong11 = new int[] { 3, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0 };// MFS
//		TestCase wrongCase11 = new TestCaseImplement();
//		((TestCaseImplement) wrongCase11).setTestCase(wrong11);
//
//		Tuple bugModel11 = new Tuple(3, wrongCase11);// 1表几个故障模式
//		bugModel11.set(0, 0);
//		bugModel11.set(1, 7);
//		bugModel11.set(2, 11);
//
//		int[] wrong12 = new int[] { 3, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0 };// MFS
//		TestCase wrongCase12 = new TestCaseImplement();
//		((TestCaseImplement) wrongCase12).setTestCase(wrong12);
//
//		Tuple bugModel12 = new Tuple(3, wrongCase12);
//		bugModel12.set(0, 0);
//		bugModel12.set(1, 2);
//		bugModel12.set(2, 11);
//
//		int[] wrong13 = new int[] { 3, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };// MFS
//		TestCase wrongCase13 = new TestCaseImplement();
//		((TestCaseImplement) wrongCase13).setTestCase(wrong13);
//
//		Tuple bugModel13 = new Tuple(3, wrongCase13);
//		bugModel13.set(0, 0);
//		bugModel13.set(1, 1);
//		bugModel13.set(2, 11);
//
//		int[] wrong14 = new int[] { 3, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0 };// MFS
//		TestCase wrongCase14 = new TestCaseImplement();
//		((TestCaseImplement) wrongCase14).setTestCase(wrong14);
//
//		Tuple bugModel14 = new Tuple(3, wrongCase14);
//		bugModel14.set(0, 0);
//		bugModel14.set(1, 3);
//		bugModel14.set(2, 11);

		realMFS.add(bugModel1);
		realMFS.add(bugModel2);
		realMFS.add(bugModel3);
		realMFS.add(bugModel4);
//		realMFS.add(bugModel5);
//		realMFS.add(bugModel6);
//		realMFS.add(bugModel7);
//		realMFS.add(bugModel8);
//		realMFS.add(bugModel9);
//		realMFS.add(bugModel10);
//		realMFS.add(bugModel11);
//		realMFS.add(bugModel12);
//		realMFS.add(bugModel13);
//		realMFS.add(bugModel14);

		((CaseRunnerWithBugInject) caseRunner).inject(bugModel1);
		((CaseRunnerWithBugInject) caseRunner).inject(bugModel2);
		((CaseRunnerWithBugInject) caseRunner).inject(bugModel3);
		((CaseRunnerWithBugInject) caseRunner).inject(bugModel4);
//		((CaseRunnerWithBugInject) caseRunner).inject(bugModel5);
//		((CaseRunnerWithBugInject) caseRunner).inject(bugModel6);
//		((CaseRunnerWithBugInject) caseRunner).inject(bugModel7);
//		((CaseRunnerWithBugInject) caseRunner).inject(bugModel8);
//		((CaseRunnerWithBugInject) caseRunner).inject(bugModel9);
//		((CaseRunnerWithBugInject) caseRunner).inject(bugModel10);
//		((CaseRunnerWithBugInject) caseRunner).inject(bugModel11);
//		((CaseRunnerWithBugInject) caseRunner).inject(bugModel12);
//		((CaseRunnerWithBugInject) caseRunner).inject(bugModel13);
//		((CaseRunnerWithBugInject) caseRunner).inject(bugModel14);

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
