package maskTool;

import java.util.Random;

import com.fc.coveringArray.AnnelInf;
import com.fc.coveringArray.CoveringManage;
import com.fc.coveringArray.CoveringManagementInf;
import com.fc.coveringArray.DataCenter;
import com.fc.caseRunner.CaseRunner;
import com.fc.testObject.TestCase;
import com.fc.testObject.TestCaseImplement;

public class MaskAnnelProcess implements AnnelInf {
	public int[] coveringArray; // 覆盖记录，在一个点的数值意味着被覆盖的次数
	public Integer unCovered;// 未被覆盖对
	public int freezingTimes;// 僵住的次数
	public int[][] table; // N*K的表
	private Random randomGenerator = new Random(); // 随机生成器
	private int rowChange;// 改变的行
	private int[] oldRow; // 改变前的行
	private int[] newRow; // 改变后的行
	private boolean feasiable;// 是否可行
	private int N; // 给定大小N
	private double T;// 温度T
	private double decrement;// 控温下降

	// private int wrongCode; // 错误码
	private CaseRunner runner; //

	public MaskAnnelProcess(int N, double T, double decrement, CaseRunner runner) {
		this.N = N;
		this.T = T;
		this.decrement = decrement;
		this.runner = runner;
	}

	@Override
	public void initAnneling() {
		// TODO Auto-generated method stub
		// 随机生成一个N*K的表`
		this.feasiable = false;
		CoveringManagementInf cm = new CoveringManage();
		// 随机生成一个N*K的表
		// 初始化coveringArray
		this.coveringArray = new int[DataCenter.coveringArrayNum];
		unCovered = this.coveringArray.length;
		this.freezingTimes = 0;
		table = new int[N][DataCenter.param.length];
		for (int i = 0; i < N; i++) {
			int[] temp = new int[DataCenter.param.length];

			while (true) {
				for (int j = 0; j < DataCenter.param.length; j++)
					temp[j] = randomGenerator.nextInt(DataCenter.param[j]);
				TestCaseImplement testCase = new TestCaseImplement();
				testCase.setTestCase(temp);
				int runresult = runner.runTestCase(testCase);
				if (runresult == TestCase.FAILED
						|| runresult == TestCase.PASSED)
					break;
			}

			table[i] = temp;
			unCovered = cm.setCover(unCovered, coveringArray, table[i]);
		}
	}

	@Override
	public boolean isAccept() {
		// TODO Auto-generated method stub

		TestCaseImplement testCase = new TestCaseImplement();
		testCase.setTestCase(newRow);
		int runresult = runner.runTestCase(testCase);
		if (runresult != TestCase.FAILED && runresult != TestCase.PASSED)
			return false;

		CoveringManagementInf cm = new CoveringManage();
		int oldUncovered = unCovered.intValue();
		unCovered = cm.rmCover(unCovered, coveringArray, oldRow);
		unCovered = cm.setCover(unCovered, coveringArray, newRow);
		if (oldUncovered > unCovered) {
			freezingTimes = 0;
			return true;
		} else if (oldUncovered == unCovered) {
			freezingTimes++;
			return true;
		} else // 以概率p接受
		{
			freezingTimes++;
			double p = Math.pow(Math.E, -(unCovered - oldUncovered) / this.T);
			double exa = randomGenerator.nextDouble();
			if (exa < p)
				return true;
			else
				return false;
		}
	}

	@Override
	public boolean isEnd() {
		// TODO Auto-generated method stub
		if (unCovered == 0) {
			this.feasiable = true;
			return true;
		} else if (freezingTimes > DataCenter.maxFreeing) {
			this.feasiable = false;
			return true;
		}
		return false;
	}

	@Override
	public void makeChange() {
		// TODO Auto-generated method stub
		// 任意挑出一个cell改变其值
		this.rowChange = randomGenerator.nextInt(N);
		int col = randomGenerator.nextInt(DataCenter.param.length);
		int newValue = (table[rowChange][col] + 1) % DataCenter.param[col];
		oldRow = table[rowChange];
		newRow = table[rowChange].clone();
		newRow[col] = newValue;
	}

	@Override
	public void startAnneling() {
		// TODO Auto-generated method stub
		// init
		this.initAnneling();
		// do
		while (!isEnd())// 如果没有结束就一直做
		{
			this.makeChange();
			if (this.isAccept())// 如果接受
				table[rowChange] = this.newRow;
			else {
				CoveringManagementInf cm = new CoveringManage();
				unCovered = cm.rmCover(unCovered, coveringArray, newRow);
				unCovered = cm.setCover(unCovered, coveringArray, oldRow); // 回退
			}
			this.T = this.T * this.decrement;
		}
	}

	@Override
	public boolean isOk() {
		// TODO Auto-generated method stub
		return this.feasiable;
	}

	public static void main(String[] args) {

	}

}