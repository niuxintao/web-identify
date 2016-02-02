package expForRevision;

import interaction.DataCenter;

import java.util.ArrayList;
import java.util.List;

import com.fc.caseRunner.CaseRunner;
import com.fc.caseRunner.CaseRunnerWithBugInject;
import com.fc.testObject.TestCase;
import com.fc.testObject.TestCaseImplement;
import com.fc.tuple.Tuple;

import experimentData.ExperimentData;

public class DataForNumberOfMFS implements ExperimentData {

	private int[] param;
	private CaseRunner caseRunner;
	private List<Tuple> realMFS;
	private DataCenter dataCenter;

	public DataForNumberOfMFS() {
		this.init();

	}

	public void init() {
		// TODO Auto-generated method stub
		this.param = new int[] { 3, 3, 3, 3, 3, 3, 3, 3 };// parameters
		realMFS = new ArrayList<Tuple>();
		caseRunner = new CaseRunnerWithBugInject();
	}

	/**
	 * add n MFS
	 * 
	 * @param n
	 */

	public void setMFS(int n) {
		realMFS = new ArrayList<Tuple>();
		caseRunner = new CaseRunnerWithBugInject();

		int count = 0;

		int base = 0;
		int[] wrong = new int[param.length];
		for (int i = 0; i < param.length; i++)
			wrong[i] = base;

		count = onceLoop(n, count, wrong);

		while (count < n) {
			base++;
			wrong = new int[param.length];
			for (int i = 0; i < param.length; i++)
				wrong[i] = base;
			count = onceLoop(n, count, wrong);
		}
	}

	public int onceLoop(int n, int count, int[] wrong) {
		for (int i = 0; i < this.param.length; i++) {
			for (int j = i + 1; j < this.param.length; j++) {
				TestCase wrongCase = new TestCaseImplement();
				((TestCaseImplement) wrongCase).setTestCase(wrong);
				Tuple bugMode = new Tuple(2, wrongCase);
				bugMode.set(0, i);
				bugMode.set(1, j);
				realMFS.add(bugMode);
				((CaseRunnerWithBugInject) caseRunner).inject(bugMode);
				count++;
				if (count >= n)
					return count;
			}
		}
		return count;
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
