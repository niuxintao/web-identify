package experiment_for_assumption;

import interaction.DataCenter;

import java.util.ArrayList;
import java.util.List;

import com.fc.caseRunner.CaseRunner;
import com.fc.caseRunner.CaseRunnerWithBugInjectLikely;
import com.fc.testObject.TestCase;
import com.fc.testObject.TestCaseImplement;
import com.fc.tuple.Tuple;

import experimentData.ExperimentData;


// inject two mfs, 1 mfs with 100%per, the other one is 1, 5, 10, 15, 20, 25, 30, 35, 40, 45, 50,... 95, 99

// too low will nearly not appear, but 100 precise

// too high will 100 appear, and 100 percise and recall

public class DataForUndeterministic implements ExperimentData {
	
	private int[] param;
	private CaseRunner caseRunner;
	private List<Tuple> realMFS;
	private DataCenter dataCenter;

	public DataForUndeterministic(double likely) {
		this.init(likely);

	}

	public void init(double likely) {
		// TODO Auto-generated method stub
		// 0 // 0 0
		// 0 1
		// 1 // 1 0
		// 1 1

		this.param = new int[10];
		for(int i = 0; i < param.length; i++)
			param[i] = 4;

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
		
		
		/** ***********************************************************
		 *  note that we have not done all the schemas.
		 *  ***********************************************************
		 */

		int[] wrong2 = new int[param.length]; // MFS
		for (int i = 0; i < wrong2.length; i++)
			wrong2[i] = 1;
		TestCase wrongCase2 = new TestCaseImplement();
		((TestCaseImplement) wrongCase2).setTestCase(wrong2);
		
		Tuple bugModel2 = new Tuple(1, wrongCase2);
		bugModel2.set(0, 1);
//		bugModel2.set(1, 3);

		realMFS.add(bugModel);  
		
		realMFS.add(bugModel2); // add or not add

		this.caseRunner = new CaseRunnerWithBugInjectLikely();
		((CaseRunnerWithBugInjectLikely) caseRunner).inject(bugModel, 1);
		
		((CaseRunnerWithBugInjectLikely) caseRunner).inject(bugModel2, likely);
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
