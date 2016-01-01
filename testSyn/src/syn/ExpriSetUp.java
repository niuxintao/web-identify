package syn;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.fc.testObject.TestCase;
import com.fc.testObject.TestCaseImplement;
import com.fc.tuple.Tuple;

public class ExpriSetUp {

	private HashMap<Integer, List<Tuple>> bugsList;
	private HashMap<Integer, List<Integer>> priorityList;
	private int[] param;

	private List<DataRecord> records;
	public static String[] SYN = { "SYN_1", "SYN_2", "SYN_3", "SYN_4", "SYN_5" };

	public ExpriSetUp() {
		records = new ArrayList<DataRecord>();

		/**/
		// the synthesizing 10 group // synthesiz 1
		// synthesiz 1
		DataRecord record = new DataRecord();
		int[] param = new int[] { 2, 2, 2, 2, 2, 2, 2, 3, 3, 4 };
		int[][] wrongs = new int[][] { { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
				{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 },
				{ 1, 1, 1, 1, 0, 1, 1, 1, 1, 1 },
				{ 1, 1, 1, 1, 1, 0, 0, 2, 2, 3 },
				{ 1, 1, 1, 1, 1, 0, 1, 2, 2, 3 } };
		int[][] bugs = new int[][] { { 2, 7 }, { 3, 5 }, { 4 }, { 6, 7 },
				{ 6, 8 } };
		int[] faults = new int[] { 1, 2, 2, 3, 4 };
		int[][] priority = new int[][] { { 2, 3, 4 }, { 3, 4 }, { 4 }, {} };
		record.set(param, wrongs, bugs, faults, priority);
		records.add(record);

		// synthesiz 2
		record = new DataRecord();
		param = new int[] { 3, 3, 3, 3, 4, 3, 3, 2, 2 };
		wrongs = new int[][] { { 0, 0, 0, 0, 0, 0, 0, 0, 0 },
				{ 1, 1, 0, 1, 1, 1, 1, 1, 1 }, { 1, 1, 1, 1, 0, 1, 1, 1, 1 },
				{ 1, 1, 1, 1, 3, 0, 0, 1, 1 }, { 2, 2, 2, 2, 3, 2, 2, 1, 1 } };
		bugs = new int[][] { { 2, 3 }, { 2, 5 }, { 4, 6 }, { 3, 6 }, { 2, 4 } };
		faults = new int[] { 1, 1, 2, 2, 3 };
		priority = new int[][] { { 2, 3 }, { 3 }, {} };
		record.set(param, wrongs, bugs, faults, priority);
		records.add(record);

		// synthesiz 3
		record = new DataRecord();
		param = new int[] { 2, 2, 2, 2, 3, 3, 3, 4 };
		wrongs = new int[][] { { 0, 0, 0, 0, 0, 0, 0, 0 },
				{ 1, 1, 1, 1, 1, 1, 1, 1 }, { 1, 1, 1, 1, 1, 1, 1, 1 },
				{ 1, 1, 1, 1, 1, 1, 1, 1 }, { 1, 1, 1, 1, 2, 2, 2, 3 } };
		bugs = new int[][] { { 0, 1 }, { 1, 2 }, { 2, 3 }, { 4, 7 }, { 5, 6 } };
		faults = new int[] { 1, 2, 2, 3, 4 };
		priority = new int[][] { { 2, 3, 4 }, { 3, 4 }, { 4 }, {} };
		record.set(param, wrongs, bugs, faults, priority);
		records.add(record);

		// synthesiz 4
		record = new DataRecord();
		param = new int[] { 2, 3, 3, 3, 2, 3, 2 };
		wrongs = new int[][] { { 0, 1, 1, 0, 1, 1, 1 },
				{ 1, 1, 0, 0, 1, 1, 0 }, { 1, 1, 1, 1, 1, 0, 0 },
				{ 0, 0, 0, 0, 0, 0, 1 }, { 1, 1, 0, 1, 1, 0, 0 } };
		bugs = new int[][] { { 0 }, { 1, 3 }, { 2 }, { 4, 5 }, { 6 } };
		faults = new int[] { 1, 2, 3, 4, 5 };
		priority = new int[][] { { 2, 3, 4, 5 }, { 3, 4, 5 }, { 4, 5 }, { 5 },
				{} };
		record.set(param, wrongs, bugs, faults, priority);
		records.add(record);

		// synthesiz 5
		record = new DataRecord();
		param = new int[] { 3, 3, 3, 3, 3, 3, 3 };
		wrongs = new int[][] { { 0, 1, 1, 0, 1, 1, 1 },
				{ 1, 1, 0, 0, 1, 1, 0 }, { 1, 1, 1, 1, 1, 0, 0 },
				{ 2, 2, 2, 2, 2, 0, 1 }, { 2, 1, 0, 2, 1, 0, 0 } };
		bugs = new int[][] { { 0 }, { 2, 3 }, { 2, 4 }, { 1, 2 }, { 0, 6 } };
		faults = new int[] { 1, 2, 2, 3, 4 };
		priority = new int[][] { { 2, 3, 4 }, { 3, 4 }, { 4 }, {} };
		record.set(param, wrongs, bugs, faults, priority);
		records.add(record);

	}

	public HashMap<Integer, List<Tuple>> getBugsList() {
		return bugsList;
	}

	public HashMap<Integer, List<Integer>> getPriorityList() {
		return priorityList;
	}

	public int[] getParam() {
		return param;
	}

	public List<DataRecord> getRecords() {
		return records;
	}

	public void set(int[] param, int[][] wrongs, int[][] bugs, int[] faults,
			int[][] higherPriority) {
		this.param = param;
		bugsList = new HashMap<Integer, List<Tuple>>();
		priorityList = new HashMap<Integer, List<Integer>>();

		// set the priority
		for (int i = 0; i < higherPriority.length; i++) {
			int key = i + 1;
			int[] higher = higherPriority[i];
			List<Integer> priority = new ArrayList<Integer>();
			for (int j : higher)
				priority.add(j);
			priorityList.put(key, priority);
		}

		// set the bugs as well as its fault

		for (int i = 0; i < wrongs.length; i++) {
			int[] wrong = wrongs[i];
			int[] bug = bugs[i];

			int key = faults[i];

			TestCase wrongCase = new TestCaseImplement();
			wrongCase.setTestState(TestCase.FAILED);
			((TestCaseImplement) wrongCase).setTestCase(wrong);

			Tuple bugTuple = new Tuple(bug.length, wrongCase);
			bugTuple.setParamIndex(bug);

			if (bugsList.get(key) == null) {
				List<Tuple> tuples = new ArrayList<Tuple>();
				bugsList.put(key, tuples);
			}

			bugsList.get(key).add(bugTuple);
		}

	}

	public static void main(String[] args) {
		PrintStream ps;
		try {
			ps = new PrintStream(new FileOutputStream("result.txt"));
			System.setOut(ps);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		for (int k = 0; k < 5; k++) {
			System.out.println(SYN[k]);
			ExpriSetUp ex = new ExpriSetUp();
			DataRecord record = ex.getRecords().get(k);
			ex.set(record.param, record.wrongs, record.bugs, record.faults,
					record.priority);
			for (int i : ex.getParam())
				System.out.print(i + " ");
			System.out.println();

			BasicRunner basicRunner = new BasicRunner(ex.getPriorityList(),
					ex.getBugsList());
			BenchExecute becnch = new BenchExecute(record.param, basicRunner);
			becnch.bench();
		}

	}

}
