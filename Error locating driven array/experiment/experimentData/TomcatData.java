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
	
	public TomcatData(){
		this.init();
	
	}

	public void init() {
		// TODO Auto-generated method stub
		this.param = new int[] { 2, 2, 2, 2, 2, 2, 2, 2,3 };//parameters

		realMFS = new ArrayList<Tuple>();

		int[] wrong = new int[] { 0, 0, 1, 0, 0, 0, 0, 0, 0 };//MFS
		TestCase wrongCase = new TestCaseImplement();
		((TestCaseImplement) wrongCase).setTestCase(wrong);

		Tuple bugModel1 = new Tuple(1, wrongCase);
		bugModel1.set(0, 2);
		//bugModel1.set(1, 2);

		realMFS.add(bugModel1);

		this.caseRunner = new CaseRunnerWithBugInject();
		((CaseRunnerWithBugInject) caseRunner).inject(bugModel1);
		
		
		
		
		
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
