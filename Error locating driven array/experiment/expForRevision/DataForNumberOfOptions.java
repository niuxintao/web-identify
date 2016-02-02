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

public class DataForNumberOfOptions implements ExperimentData {
	
	
	public static int[] NUM = {1, 2, 4, 8, 100};

	private int[] param;
	private CaseRunner caseRunner;
	private List<Tuple> realMFS;
	private DataCenter dataCenter;

	public DataForNumberOfOptions(int[] param) {
		this.init(param);

	}

	public void init(int[] param) {
		// TODO Auto-generated method stub
		// 0 // 0 0
		// 0 1
		// 1 // 1 0
		// 1 1

		this.param = param;

		realMFS = new ArrayList<Tuple>();

		int[] wrong = new int[param.length]; // MFS
		for (int i = 0; i < wrong.length; i++)
			wrong[i] = 0;

		TestCase wrongCase = new TestCaseImplement();
		((TestCaseImplement) wrongCase).setTestCase(wrong);

		Tuple bugModel = new Tuple(2, wrongCase);
		bugModel.set(0, 0);
		bugModel.set(1, 3);
		// bugModel1.set(2, 4);
		// bugModel1.set(1, 2);

		realMFS.add(bugModel);

		this.caseRunner = new CaseRunnerWithBugInject();
		((CaseRunnerWithBugInject) caseRunner).inject(bugModel);
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

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

}
