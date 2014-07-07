package maskTool;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import maskAlogrithms.CTA;
//import maskPracticalExperiment.Statistics;
//import maskPracticalExperiment.TableRunnerBasic;
import maskSimulateExperiment.BasicRunner;

//import com.fc.coveringArray.CoveringManagementInf;
import com.fc.coveringArray.DataCenter;
//import com.fc.coveringArray.Process;
import com.fc.testObject.TestCaseImplement;
import com.fc.testObject.TestSuite;
import com.fc.testObject.TestSuiteImplement;
import com.fc.tuple.Tuple;

public class CoveringArrayGenFeedBack {
	public double T;
	public double decrement;
	public long time;

	public List<int[]> rsTable;

	public List<int[]> currentTable;

	private HashSet<Tuple> mfss;

	private HashSet<Tuple> currentNewlyMFS;

	private int[] coveringArray;

	private Integer unCovered;

	public CoveringArrayGenFeedBack(double T, double decrement) {
		rsTable = new ArrayList<int[]>();
		currentTable = new ArrayList<int[]>();

		mfss = new HashSet<Tuple>();
		currentNewlyMFS = new HashSet<Tuple>();

		// CoveringManagementInf cm = new CoveringManage();
		this.coveringArray = new int[DataCenter.coveringArrayNum];
		unCovered = this.coveringArray.length;

		this.T = T;
		this.decrement = decrement;
	}

	public void process() {
		// N��ʼ��Ϊ���Ƕ�
		int start = unCovered;// DataCenter.coveringArrayNum;
		int end = 0;
		boolean flag = false;
		long starttime = new Date().getTime();
		// ���ַ����ҵ���С��N
		while (start > end || !flag) {
			if (start <= end)// ��һ�������ҵ����ʵ�start��end
			{
				end = start;
				start *= 2;
			}
			int middle = (start + end) / 2;
			AnnelProcessFeedBack al = new AnnelProcessFeedBack(mfss, middle, T,
					coveringArray, unCovered, decrement);
			al.startAnneling();
			if (al.isOk()) {
				start = middle - 1;
				this.currentTable = new ArrayList<int[]>();

				for (int[] a : al.table) {
					rsTable.add(a);
					currentTable.add(a);
				}

				flag = true;
			} else
				end = middle + 1;
		}
		long endtime = new Date().getTime();
		time = endtime - starttime;
	}

	public void get(BasicRunner basicRunner) throws Exception {
		while (feedBackCritiria()) {
			// generate
			process();

			// execute
			HashMap<Integer, List<Tuple>> bugs = executeAndCharactersiz(
					this.rsTable, basicRunner);
			// get the bug
			this.currentNewlyMFS = new HashSet<Tuple>();
			for (Integer key : bugs.keySet()) {
				if (key != 0) {
					List<Tuple> curBugs = bugs.get(key);
					for (Tuple bug : curBugs) {
						if (!this.mfss.contains(bug)) {
							mfss.add(bug);
							this.currentNewlyMFS.add(bug);
						}
					}
				}
			}

			// rm the false confidenced covered
			CoveringManage cm = new CoveringManage();
			cm.rmCover(unCovered, currentTable, coveringArray,
					this.currentNewlyMFS);


			// reset;
			// set the the bugs removed;
			// repeat;

		}
	}

	public boolean feedBackCritiria() {
		return this.unCovered == 0;
	}

	// public executeAndCharactersiz() {
	//
	// }

	public HashMap<Integer, List<Tuple>> executeAndCharactersiz(
			List<int[]> table, BasicRunner basicRunner) throws Exception {
		// System.out.println("Classified tree analysis");
		HashSet<Integer> result = new HashSet<Integer>();

		// statistic.getResult();

		TestSuite suite = new TestSuiteImplement();
		String[] state = new String[table.size()];
		int i = 0;
		for (int[] row : table) {
			TestCaseImplement testCase = new TestCaseImplement();
			testCase.setTestCase(row);
			int runresult = basicRunner.runTestCase(testCase);
			suite.addTest(testCase);
			state[i] = "" + runresult;
			result.add(runresult);
			// System.out.println(state[i]);
			i++;
		}

		String[] classes = new String[result.size()];
		// classes[0] = "0";
		int j = 0;
		for (Integer r : result) {
			classes[j] = r + "";
			j++;
		}
		// for (int j = 0; j < classes.length; j++)
		// classes[j] = result + "";

		CTA cta = new CTA();

		cta.process(DataCenter.param, classes, suite, state);

		return cta.getBugs();

	}

	static public void main(String[] args) {
		int param[] = { 10, 10, 10, 10, 10, 10, 10 };
		DataCenter.init(param, 2);
		System.out.println(DataCenter.coveringArrayNum);
		CoveringArrayGenFeedBack t = new CoveringArrayGenFeedBack(2, 0.9998);
		t.process();
		for (int[] row : t.rsTable) {
			for (int i : row)
				System.out.print(i + " ");
			System.out.println();
		}
		System.out.println("arrayLength: " + t.rsTable.size());
		System.out.println("time: " + t.time + " ms");
	}
}
