package experimentData;

import interaction.DataCenter;

import java.util.ArrayList;
import java.util.List;

import com.fc.caseRunner.CaseRunner;
import com.fc.caseRunner.CaseRunnerWithBugInject;
import com.fc.testObject.TestCase;
import com.fc.testObject.TestCaseImplement;
import com.fc.tuple.Tuple;

public class HsqlDBData implements ExperimentData {

	private int[] param;
	private CaseRunner caseRunner;
	private List<Tuple> realMFS;
	private DataCenter dataCenter;

	public DataCenter getDataCenter() {
		return dataCenter;
	}

	public HsqlDBData() {
		this.init();
	}

	public void init() {

		this.param = new int[] { 3, 2, 2, 2, 2, 2, 2, 2, 4, 3, 2, 2 };

		realMFS = new ArrayList<Tuple>();

		int[] wrong = new int[] { 0, 0, 0, 0, 0, 1, 0, 0, 2, 2, 0, 0 };
		TestCase wrongCase = new TestCaseImplement();
		((TestCaseImplement) wrongCase).setTestCase(wrong);

		int[] wrong2 = new int[] { 0, 0, 0, 0, 0, 1, 0, 0, 2, 1, 0, 0 };
		TestCase wrongCase2 = new TestCaseImplement();
		((TestCaseImplement) wrongCase2).setTestCase(wrong2);

		Tuple bugModel1 = new Tuple(3, wrongCase);
		bugModel1.set(0, 5);
		bugModel1.set(1, 8);
		bugModel1.set(2, 9);

		realMFS.add(bugModel1);

		Tuple bugModel2 = new Tuple(3, wrongCase2);
		bugModel2.set(0, 5);
		bugModel2.set(1, 8);
		bugModel2.set(2, 9);

		realMFS.add(bugModel2);

		this.caseRunner = new CaseRunnerWithBugInject();
		((CaseRunnerWithBugInject) caseRunner).inject(bugModel1);
		((CaseRunnerWithBugInject) caseRunner).inject(bugModel2);
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
