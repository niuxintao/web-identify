package maskTool;

import java.util.Date;

import maskSimulateExperiment.BasicRunnerInMask;
import maskSimulateExperiment.MaskRunner;

import com.fc.caseRunner.CaseRunner;
import com.fc.coveringArray.DataCenter;
import com.fc.testObject.TestCase;
import com.fc.testObject.TestCaseImplement;
import com.fc.tuple.Tuple;

public class MaskANProcess {
	public double T;
	public double decrement;
	public long time;
	public int[][] rsTable;
	private CaseRunner runner;
	public static int maxTimes = 3;

	public MaskANProcess(double T, double decrement, CaseRunner runner) {
		this.T = T;
		this.decrement = decrement;
		this.runner = runner;
	}

	// every time set a number, the number is not change is ok, let the value
	public void process() {
		int start = DataCenter.coveringArrayNum;
		int end = 0;
		boolean flag = false;
		long starttime = new Date().getTime();

		int maxUncover = DataCenter.coveringArrayNum;

		int times = 0;

		while (start > end || !flag) {
			if (start <= end) {
				end = start;
				start *= 2;
			}
			int middle = (start + end) / 2;
			MaskAnnelProcess al = new MaskAnnelProcess(middle, T, decrement,
					runner);
			al.startAnneling();

			if (maxUncover != 0 && al.unCovered == maxUncover) {
				times++;
				if (times >= maxTimes)
					break;
			}
			if (al.isOk()) {
				start = middle - 1;
				rsTable = al.table;
				flag = true;
			} else {
				end = middle + 1;
			}
			if (al.unCovered < maxUncover)
				maxUncover = al.unCovered;
		}

		if (maxUncover != 0) {
			start = (start + end) / 2;
			end = 0;
			while (start > end || !flag) {
				if (start <= end) {
					end = start;
					start *= 2;
				}
				int middle = (start + end) / 2;
				MaskAnnelProcess al = new MaskAnnelProcess(middle, T,
						decrement, runner);
				al.startAnneling();

				if (al.unCovered == maxUncover) {
					start = middle - 1;
					rsTable = al.table;
					flag = true;
				} else
					end = middle + 1;
			}
		}
		long endtime = new Date().getTime();
		time = endtime - starttime;
	}

	static public void main(String[] args) {

		int[] param = new int[] { 3, 3, 3, 3, 3, 3, 3, 3 };

		int[] wrong = new int[] { 1, 1, 1, 1, 1, 1, 1, 1 };

		TestCase wrongCase = new TestCaseImplement();
		wrongCase.setTestState(TestCase.FAILED);
		((TestCaseImplement) wrongCase).setTestCase(wrong);

		int[] wrong2 = new int[] { 2, 2, 2, 2, 2, 2, 2, 2 };

		TestCase wrongCase2 = new TestCaseImplement();
		wrongCase2.setTestState(TestCase.FAILED);
		((TestCaseImplement) wrongCase2).setTestCase(wrong2);

		Tuple bug1 = new Tuple(2, wrongCase);
		bug1.set(0, 0);
		bug1.set(1, 1);

		Tuple bug2 = new Tuple(1, wrongCase2);
		bug2.set(0, 3);

		BasicRunnerInMask runner = new BasicRunnerInMask();
		runner.inject(bug1, 1);
		runner.inject(bug2, 2);

		MaskRunner maskRunner = new MaskRunner(runner, 1);
		DataCenter.init(param, 2);
		System.out.println(DataCenter.coveringArrayNum);
		MaskANProcess t = new MaskANProcess(2, 0.9998, maskRunner);
		t.process();
		for (int[] row : t.rsTable) {
			for (int i : row)
				System.out.print(i + " ");
			System.out.println();
		}
		System.out.println("arrayLength: " + t.rsTable.length);
		System.out.println("time: " + t.time + " ms");
	}
}
