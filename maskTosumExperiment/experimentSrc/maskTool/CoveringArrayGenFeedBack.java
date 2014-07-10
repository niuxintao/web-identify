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
import maskSimulateExperiment.DataRecord;
import maskSimulateExperiment.ExpriSetUp;




//import com.fc.coveringArray.CoveringManagementInf;
import com.fc.coveringArray.DataCenter;
import com.fc.testObject.TestCase;
//import com.fc.coveringArray.Process;
import com.fc.testObject.TestCaseImplement;
import com.fc.testObject.TestSuite;
import com.fc.testObject.TestSuiteImplement;
import com.fc.tuple.Tuple;

public class CoveringArrayGenFeedBack {
	public double T;
	public double decrement;
	public long time;
	public static int maxTimes = 3;

	public List<int[]> rsTable;

	public List<int[]> currentTable;

	private HashSet<Tuple> mfss;

	private HashSet<Tuple> currentNewlyMFS;

	private int[] coveringArray;

	private Integer unCovered;

	public HashSet<Tuple> getMfss() {
		return mfss;
	}

	public HashSet<Tuple> getCurrentNewlyMFS() {
		return currentNewlyMFS;
	}

	public List<int[]> getFirstCoveringArray() {
		return firstCoveringArray;
	}

	private List<int[]> firstCoveringArray;

	private int OldMaxUnCover;
	private int NewMaxUnCover;

	public CoveringArrayGenFeedBack(double T, double decrement) {
		rsTable = new ArrayList<int[]>();
		currentTable = new ArrayList<int[]>();

		firstCoveringArray = new ArrayList<int[]>();

		mfss = new HashSet<Tuple>();
		currentNewlyMFS = new HashSet<Tuple>();

		// CoveringManagementInf cm = new CoveringManage();
		this.coveringArray = new int[DataCenter.coveringArrayNum];
		unCovered = this.coveringArray.length;

		OldMaxUnCover = 0;

		NewMaxUnCover = this.coveringArray.length;

		this.T = T;
		this.decrement = decrement;
	}

	public int getCoverLeft() {
		int result = 0;
		for (int i : this.coveringArray) {
			if (i == 0)
				result++;
		}
		return result;
	}

	public boolean process() {
		// N��ʼ��Ϊ���Ƕ�
		int start = unCovered;// DataCenter.coveringArrayNum;
		int end = 0;
		boolean flag = false;
		long starttime = new Date().getTime();
		int[][] rstable = null;
		Integer unc = unCovered;
		int[] coveri = null;
		// ���ַ����ҵ���С��N
//		 System.out.println("uncover :" + unCovered + " covrRemained :"
//		 + this.getCoverLeft());

		int maxUncover = unCovered;
		int times = 0;

		// start > end go on
		// flag: cannot find a satisfied go on
		while (start > end || !flag) {
			if (start <= end)// ��һ�������ҵ����ʵ�start��end
			{
				end = start;
				start *= 2;
			}
			int middle = (start + end) / 2;
			middle = middle > 0 ? middle : 1;
			AnnelProcessFeedBack al = new AnnelProcessFeedBack(mfss, middle, T,
					coveringArray, unCovered, decrement);
			if(!al.isGenerated()){
				return false;
			}
			al.startAnneling();
			// unCovered = al.unCovered;
			if (maxUncover != 0 && al.unCovered == maxUncover) {
				times++;
				if (times >= maxTimes)
					break;
			}

			if (al.isOk()) {
				start = middle - 1;
				rstable = al.table;
				unc = al.unCovered;
				coveri = al.coveringArray;
				// this.currentTable = new ArrayList<int[]>();
				//
				// for (int[] a : al.table) {
				// rsTable.add(a);
				// currentTable.add(a);
				// }

				flag = true;
			} else
				end = middle + 1;

			if (al.unCovered < maxUncover)
				maxUncover = al.unCovered;

			// System.out.println("start: " + start + " end :" + end);
		}

		// System.out.println("out");

		if (maxUncover != 0) {
			start = (start + end) / 2;
			end = 0;
			while (start > end || !flag) {
				if (start <= end) {
					end = start;
					start *= 2;
				}
				int middle = (start + end) / 2;
				middle = middle > 0 ? middle : 1;
				AnnelProcessFeedBack al = new AnnelProcessFeedBack(mfss,
						middle, T, coveringArray, unCovered, decrement);
				al.startAnneling();
				if (al.unCovered == maxUncover) {
					start = middle - 1;
					rstable = al.table;
					unc = al.unCovered;
					coveri = al.coveringArray;
					flag = true;
				} else
					end = middle + 1;
			}
		}

		long endtime = new Date().getTime();
		time = endtime - starttime;

		this.OldMaxUnCover = this.NewMaxUnCover;
		this.NewMaxUnCover = maxUncover;

		this.currentTable = new ArrayList<int[]>();

		for (int[] a : rstable) {
			rsTable.add(a);
			currentTable.add(a);
		}

		this.unCovered = unc;

		this.coveringArray = coveri;
		
		return true;

	}

	public void get(BasicRunner basicRunner) throws Exception {
		int i = 0;
		while (!feedBackCritiria()) {
			// generate
			// System.out.println("handle");
			boolean result = process();
			if(!result){
				break;
			}

			if (i == 0) {
				this.firstCoveringArray = this.currentTable;
				i++;
			}

			// execute
			HashMap<Integer, List<Tuple>> bugs = executeAndCharactersiz(
					this.rsTable, basicRunner);
			// get the bug
			this.currentNewlyMFS = new HashSet<Tuple>();
			for (Integer key : bugs.keySet()) {
				if (key != 0) {
					List<Tuple> curBugs = bugs.get(key);
					for (Tuple bug : curBugs) {
//						 if (!this.mfss.contains(bug)) {
//						 System.out.println(bug.toString());
						mfss.add(bug);
						this.currentNewlyMFS.add(bug);
//						 }
					}
				}
			}

			// rm the false confidenced covered
			CoveringManage cm = new CoveringManage();
			unCovered = cm.rmCover(unCovered, currentTable, coveringArray,
					this.currentNewlyMFS);

			// reset;
			// set the the bugs removed;
			// repeat;

		}
	}

	public boolean feedBackCritiria() {
		return this.unCovered == 0 || this.NewMaxUnCover == this.OldMaxUnCover;
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
	
	public void test(){
		ExpriSetUp setup = new ExpriSetUp();
		DataRecord record = setup.getRecords().get(7);
		setup.set(record.param, record.wrongs, record.bugs, record.faults,
				record.priority);

		List<Tuple> bench = new ArrayList<Tuple>();
		for (Integer key : setup.getBugsList().keySet()) {
			bench.addAll(setup.getBugsList().get(key));
		}

		BasicRunner basicRunner = new BasicRunner(setup.getPriorityList(),
				setup.getBugsList());
		
		DataCenter.init(setup.getParam(), 2);
		CoveringArrayGenFeedBack t = new CoveringArrayGenFeedBack(2, 0.9998);
		try {
			
			t.get(basicRunner);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		for (Tuple tuple : t.currentNewlyMFS)
			System.out.println(tuple.toString());

		System.out.println("**********************");

		for (Tuple tuple : t.mfss)
			System.out.println(tuple.toString());

		for (int[] row : t.rsTable) {
			for (int i : row)
				System.out.print(i + " ");
			System.out.println();
		}

		System.out.println("arrayLength: " + t.rsTable.size());
		System.out.println("time: " + t.time + " ms");
	}

	static public void main(String[] args) {
		for(int i = 0; i < 30; i++){
		CoveringArrayGenFeedBack t = new CoveringArrayGenFeedBack(2, 0.9998);
		t.test();
		}
		
	}

	public static void testSimple() {
		int[] param = new int[] { 2, 2, 2, 2, 2 };
		DataCenter.init(param, 2);
		// System.out.println(DataCenter.coveringArrayNum);

		CoveringArrayGenFeedBack t = new CoveringArrayGenFeedBack(2, 0.9998);

		int[] wrong = new int[] { 0, 0, 0, 0, 0 };

		TestCase wrongCase = new TestCaseImplement();
		wrongCase.setTestState(TestCase.FAILED);
		((TestCaseImplement) wrongCase).setTestCase(wrong);

		int[] wrong2 = new int[] { 1, 1, 1, 1, 1 };

		TestCase wrongCase2 = new TestCaseImplement();
		wrongCase2.setTestState(TestCase.FAILED);
		((TestCaseImplement) wrongCase2).setTestCase(wrong2);

		Tuple bug1 = new Tuple(2, wrongCase);
		bug1.set(0, 0);
		bug1.set(1, 1);

		List<Tuple> bugs1 = new ArrayList<Tuple>();
		bugs1.add(bug1);

		Tuple bug2 = new Tuple(2, wrongCase2);
		bug2.set(0, 3);
		bug2.set(1, 4);

		List<Tuple> bugs2 = new ArrayList<Tuple>();
		bugs2.add(bug2);

		List<Integer> priority1 = new ArrayList<Integer>();
		priority1.add(2);
		List<Integer> priority2 = new ArrayList<Integer>();

		HashMap<Integer, List<Tuple>> bugs = new HashMap<Integer, List<Tuple>>();
		bugs.put(1, bugs1);
		bugs.put(2, bugs2);

		HashMap<Integer, List<Integer>> priority = new HashMap<Integer, List<Integer>>();

		priority.put(1, priority1);
		priority.put(2, priority2);

		BasicRunner basicRunner = new BasicRunner(priority, bugs);

		// System.out.println(t.unCovered);
		try {
			t.get(basicRunner);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		for (Tuple tuple : t.currentNewlyMFS)
			System.out.println(tuple.toString());

		System.out.println("**********************");

		for (Tuple tuple : t.mfss)
			System.out.println(tuple.toString());

		for (int[] row : t.rsTable) {
			for (int i : row)
				System.out.print(i + " ");
			System.out.println();
		}

		System.out.println("arrayLength: " + t.rsTable.size());
		System.out.println("time: " + t.time + " ms");
	}
}
