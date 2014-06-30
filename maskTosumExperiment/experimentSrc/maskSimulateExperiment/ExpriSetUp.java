package maskSimulateExperiment;

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

	public ExpriSetUp() {
		records = new ArrayList<DataRecord>();

		DataRecord record = new DataRecord();
		int[] param = new int[] { 2, 2, 2, 2, 2, 2, 2, 2, 2, 3, 3, 4 };
		int[][] wrongs = new int[][] { {}, {} };
		int[][] bugs = new int[][] { {}, {} , {} , {} , {} };
		int[] faults = new int[] { 1, 2, 3 };
		int[][] priority = new int[][] { {}, { 1 }, { 1 } };
		record.set(param, wrongs, bugs, faults, priority);
		records.add(record);

		record = new DataRecord();
		param = new int[] { 2, 2, 2, 2, 2, 2, 2, 2, 2, 3, 3, 4 };
		wrongs = new int[][] { {}, {} };
		bugs = new int[][] { {}, {} };
		faults = new int[] {};
		priority = new int[][] { {}, {} };
		record.set(param, wrongs, bugs, faults, priority);
		records.add(record);

		record = new DataRecord();
		param = new int[] { 2, 2, 2, 2, 2, 2, 2, 2, 2, 3, 3, 4 };
		wrongs = new int[][] { {}, {} };
		bugs = new int[][] { {}, {} };
		faults = new int[] {};
		priority = new int[][] { {}, {} };
		record.set(param, wrongs, bugs, faults, priority);
		records.add(record);

		record = new DataRecord();
		param = new int[] { 2, 2, 2, 2, 2, 2, 2, 2, 2, 3, 3, 4 };
		wrongs = new int[][] { {}, {} };
		bugs = new int[][] { {}, {} };
		faults = new int[] {};
		priority = new int[][] { {}, {} };
		record.set(param, wrongs, bugs, faults, priority);
		records.add(record);

		record = new DataRecord();
		param = new int[] { 2, 2, 2, 2, 2, 2, 2, 2, 2, 3, 3, 4 };
		wrongs = new int[][] { {}, {} };
		bugs = new int[][] { {}, {} };
		faults = new int[] {};
		priority = new int[][] { {}, {} };
		record.set(param, wrongs, bugs, faults, priority);
		records.add(record);

		record = new DataRecord();
		param = new int[] { 2, 2, 2, 2, 2, 2, 2, 2, 2, 3, 3, 4 };
		wrongs = new int[][] { {}, {} };
		bugs = new int[][] { {}, {} };
		faults = new int[] {};
		priority = new int[][] { {}, {} };
		record.set(param, wrongs, bugs, faults, priority);
		records.add(record);

		record = new DataRecord();
		param = new int[] { 2, 2, 2, 2, 2, 2, 2, 2, 2, 3, 3, 4 };
		wrongs = new int[][] { {}, {} };
		bugs = new int[][] { {}, {} };
		faults = new int[] {};
		priority = new int[][] { {}, {} };
		record.set(param, wrongs, bugs, faults, priority);
		records.add(record);

		record = new DataRecord();
		param = new int[] { 2, 2, 2, 2, 2, 2, 2, 2, 2, 3, 3, 4 };
		wrongs = new int[][] { {}, {} };
		bugs = new int[][] { {}, {} };
		faults = new int[] {};
		priority = new int[][] { {}, {} };
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

	public void set1() {
		int[] param = new int[] { 2, 2, 2, 2, 2, 2, 2, 2, 2, 3, 3, 4 };

		int[] wrong = new int[] { 1, 1, 1, 1, 1, 1, 1 };

		TestCase wrongCase = new TestCaseImplement();
		wrongCase.setTestState(TestCase.FAILED);
		((TestCaseImplement) wrongCase).setTestCase(wrong);

		int[] wrong2 = new int[] { 2, 2, 2, 2, 2, 2, 2 };

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

	}

	public void set2() {
		int[] param = new int[] { 2, 2, 2, 2, 2, 2, 2, 2, 3, 3, 3 };

	}

	public void set3() {
		int[] param = new int[] { 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 3, 3, 4 };

	}

	public void setData() {

	}
}

class DataRecord {
	int[] param;
	int[][] wrongs;
	int[][] bugs;
	int[] faults;
	int[][] priority;

	public void set(int[] param, int[][] wrongs, int[][] bugs, int[] faults,
			int[][] priority) {
		this.param = param;
		this.wrongs = wrongs;
		this.bugs = bugs;
		this.faults = faults;
		this.priority = priority;
	}
}
