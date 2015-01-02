package experimentData;

import interaction.DataCenter;

import java.util.ArrayList;
import java.util.List;

import com.fc.caseRunner.CaseRunner;
import com.fc.caseRunner.CaseRunnerWithBugInject;
import com.fc.testObject.TestCase;
import com.fc.testObject.TestCaseImplement;
import com.fc.tuple.Tuple;

public class JFlexData implements ExperimentData {

	private int[] param;
	private CaseRunner caseRunner;
	private List<Tuple> realMFS;
	private DataCenter dataCenter;

	public JFlexData() {
		this.init();
	}

	/* (non-Javadoc)
	 * @see experimentData.ExperimentData#init()
	 */
	@Override
	public void init() {

		this.param = new int[] { 3, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 4, 3 };

		realMFS = new ArrayList<Tuple>();

		int[] wrong = new int[] { 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		TestCase wrongCase = new TestCaseImplement();
		((TestCaseImplement) wrongCase).setTestCase(wrong);

		Tuple bugModel1 = new Tuple(2, wrongCase);
		bugModel1.set(0, 1);
		bugModel1.set(1, 2);

		realMFS.add(bugModel1);

		this.caseRunner = new CaseRunnerWithBugInject();
		((CaseRunnerWithBugInject) caseRunner).inject(bugModel1);
	}

	/* (non-Javadoc)
	 * @see experimentData.ExperimentData#setDegree(int)
	 */
	@Override
	public void setDegree(int degree) {
		dataCenter = new DataCenter(param, degree);
	}

	/* (non-Javadoc)
	 * @see experimentData.ExperimentData#getParam()
	 */
	@Override
	public int[] getParam() {
		return param;
	}

	/* (non-Javadoc)
	 * @see experimentData.ExperimentData#getCaseRunner()
	 */
	@Override
	public CaseRunner getCaseRunner() {
		return caseRunner;
	}

	/* (non-Javadoc)
	 * @see experimentData.ExperimentData#getRealMFS()
	 */
	@Override
	public List<Tuple> getRealMFS() {
		return realMFS;
	}

	@Override
	public DataCenter getDataCenter() {
		// TODO Auto-generated method stub
		return dataCenter;
	}

}
