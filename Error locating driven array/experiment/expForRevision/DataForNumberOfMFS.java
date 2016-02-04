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
	private List<Tuple> allMFS;

	public DataForNumberOfMFS(int[] param, int num) {
		this.init(param, num);

	}

	public void init(int[] param, int num) {
		// TODO Auto-generated method stub
		this.param = param; // new int[] { 3, 3, 3, 3, 3, 3, 3, 3 };//
							// parameters

		// this.getAllMFS();

		this.setMFS(num);
	}

	// public void setMFS(int n) {
	// realMFS = new ArrayList<Tuple>();
	// caseRunner = new CaseRunnerWithBugInject();
	//
	// Random rng = new Random(); // Ideally just create one instance globally
	// // Note: use LinkedHashSet to maintain insertion order
	// HashSet<Integer> generated = new HashSet<Integer>();
	// while (generated.size() < n) {
	// Integer next = rng.nextInt(this.allMFS.size());
	// // As we're adding to a set, this will automatically do a
	// // containment check
	// generated.add(next);
	// }
	//
	// for (Integer index : generated) {
	// Tuple bugMode = this.allMFS.get(index);
	// realMFS.add(bugMode);
	// ((CaseRunnerWithBugInject) caseRunner).inject(bugMode);
	// }
	// }

	// /**
	/*
	 * add n MFS
	 *
	 * @param n
	 */
	public void setMFS(int n) {
		realMFS = new ArrayList<Tuple>();
		caseRunner = new CaseRunnerWithBugInject();
		//
		int count = 0;
		//
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
			// System.out.println(count + " + " + n);
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

	public void getAllMFS() {
		allMFS = new ArrayList<Tuple>();
		int[] wrong = new int[param.length];
		for (int i = 0; i < param.length; i++)
			wrong[i] = 0;
		for (int i = 0; i < this.param.length; i++) {
			for (int j = i + 1; j < this.param.length; j++) {
				TestCase wrongCase = new TestCaseImplement();
				((TestCaseImplement) wrongCase).setTestCase(wrong);
				Tuple bugMode = new Tuple(2, wrongCase);
				bugMode.set(0, i);
				bugMode.set(1, j);
				allMFS.add(bugMode);
			}
		}

		wrong = new int[param.length];
		for (int i = 0; i < param.length; i++)
			wrong[i] = 1;
		for (int i = 0; i < this.param.length; i++) {
			for (int j = i + 1; j < this.param.length; j++) {
				TestCase wrongCase = new TestCaseImplement();
				((TestCaseImplement) wrongCase).setTestCase(wrong);
				Tuple bugMode = new Tuple(2, wrongCase);
				bugMode.set(0, i);
				bugMode.set(1, j);
				allMFS.add(bugMode);
			}
		}
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
