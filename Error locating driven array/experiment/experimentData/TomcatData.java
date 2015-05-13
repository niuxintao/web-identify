package experimentData;

import interaction.DataCenter;

import java.util.ArrayList;
import java.util.List;

import com.fc.caseRunner.CaseRunner;
import com.fc.caseRunner.CaseRunnerWithBugInject;
import com.fc.testObject.TestCase;
import com.fc.testObject.TestCaseImplement;
import com.fc.tuple.Tuple;

public class TomcatData implements ExperimentData {

	private int[] param;
	private CaseRunner caseRunner;
	private List<Tuple> realMFS;
	private DataCenter dataCenter;

	public TomcatData() {
		this.init();

	}

	public void init() {
		// TODO Auto-generated method stub
//	0	// 0 0
		// 0 1
//	1	// 1 0
		// 1 1
		
		
		
		this.param = new int[] {2,  2, 2, 4, 2, 2, 2, 2, 2, 3 };// parameters

		realMFS = new ArrayList<Tuple>();
		
		
		/** 1. [ 1 , - , - , 3 , - , - , - , - , - , - ] **/
		
		/** 2. [ 1 , - , - , 1 , - , - , - , - , - , - ] **/
		
		/** 3. [ - , - , - , - , - , - , - , - , - , - ] **/

		int[] wrong = new int[] {1, 0, 0, 3, 0, 0, 0, 0, 0, 0 };// MFS
		TestCase wrongCase = new TestCaseImplement();
		((TestCaseImplement) wrongCase).setTestCase(wrong);

		
		Tuple bugModel1 = new Tuple(2, wrongCase);
		bugModel1.set(0, 0);
		bugModel1.set(1, 3);
//		bugModel1.set(2, 4);
		// bugModel1.set(1, 2);

		int[] wrong2 = new int[] {1, 0, 0, 1, 0, 0, 0, 0, 0, 0};// MFS
		TestCase wrongCase2 = new TestCaseImplement();
		((TestCaseImplement) wrongCase2).setTestCase(wrong2);

		Tuple bugModel2 = new Tuple(2, wrongCase2);
		bugModel2.set(0, 0);
		bugModel2.set(1, 3);
//		bugModel2.set(2, 4);

		
		
		int[] wrong3 = new int[] {1, 0, 0, 1, 0, 0, 0, 0, 0, 0};// MFS
		TestCase wrongCase3 = new TestCaseImplement();
		((TestCaseImplement) wrongCase3).setTestCase(wrong3);

		Tuple bugModel3 = new Tuple(1, wrongCase3);
//		bugModel3.set(0, 0);
//		bugModel3.set(1, 3);
		bugModel3.set(0, 8);
		
		
		
		realMFS.add(bugModel1);
		realMFS.add(bugModel2);
		realMFS.add(bugModel3);

		this.caseRunner = new CaseRunnerWithBugInject();
		((CaseRunnerWithBugInject) caseRunner).inject(bugModel1);
		((CaseRunnerWithBugInject) caseRunner).inject(bugModel2);
		((CaseRunnerWithBugInject) caseRunner).inject(bugModel3);

	}

	public void setDegree(int degree) {
		// TODO Auto-generated method stub
		this.dataCenter = new DataCenter(param, degree);

	}

	public int[] getParam() {
		// TODO Auto-generated method stub
		return param;
	}

	public CaseRunner getCaseRunner() {
		// TODO Auto-generated method stub
		return caseRunner;
	}

	public List<Tuple> getRealMFS() {
		// TODO Auto-generated method stub
		return realMFS;
	}

	public DataCenter getDataCenter() {
		// TODO Auto-generated method stub
		return dataCenter;

	}

}
