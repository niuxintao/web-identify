package maskTool;

import java.util.HashSet;
import java.util.Random;

import com.fc.coveringArray.AnnelInf;
import com.fc.coveringArray.CoveringManagementInf;
import com.fc.coveringArray.DataCenter;
import com.fc.testObject.TestCase;
import com.fc.testObject.TestCaseImplement;
import com.fc.tuple.Tuple;

public class AnnelProcessFeedBack implements AnnelInf {
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

	// public AnnelProcessFeedBack(int N, double T, double decrement) {
	// this.N = N;
	// this.T = T;
	// this.decrement = decrement;
	//
	// this.initAnneling();
	// }

	private HashSet<Tuple> mfs;

	/**
	 * 
	 * @param mfs
	 *            : 已有的的MFS， 下面的的测试用例不能包含
	 * @param N
	 *            ： 给定的覆盖表大小
	 * @param T
	 *            ： 温度
	 * @param coveringArray
	 *            ： 已覆盖的组合对，相当于种子
	 * @param unCovered
	 *            ： 违背覆盖的个数， 可以用上面的表得出
	 * @param decrement
	 *            ： 步进大小
	 */
	public AnnelProcessFeedBack(HashSet<Tuple> mfs, int N, double T,
			int[] coveringArray, Integer unCovered, double decrement) {
		// TODO Auto-generated constructor stub
		this.N = N;
		this.T = T;
		this.decrement = decrement;
		this.unCovered = unCovered;
		this.coveringArray = coveringArray.clone();
		this.mfs = mfs;

		this.initAnneling();

//		System.out.println("start : " + N);
	}

	@Override
	public void initAnneling() {
		// TODO Auto-generated method stub
		// 随机生成一个N*K的表`
		this.feasiable = false;
		CoveringManagementInf cm = new CoveringManage();
		// 随机生成一个N*K的表
		// 初始化coveringArray
		// this.coveringArray = new int[DataCenter.coveringArrayNum];
		// unCovered = this.coveringArray.length;
		this.freezingTimes = 0;
		table = new int[N][DataCenter.param.length];
		for (int i = 0; i < N; i++) {
			while (true) {
				for (int j = 0; j < DataCenter.param.length; j++)
					table[i][j] = randomGenerator.nextInt(DataCenter.param[j]);
				// gen not contain the mfs
				if (!this.containsMFS(table[i]))
					break;

				// System.out.println("init ");
			}
			unCovered = cm.setCover(unCovered, coveringArray, table[i]);
		}
	}

	@Override
	public boolean isAccept() {
		// TODO Auto-generated method stub
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
		oldRow = table[rowChange];
		newRow = table[rowChange].clone();

		// System.out.println("get in ");

//		int tryT = 0;
//		int changeNum = 1;

		while (true) {
			// tryT++;
			//
			// for (int i = 0; i < changeNum; i++) {
			// int col = randomGenerator.nextInt(DataCenter.param.length);
			// int newValue = (table[rowChange][col] + 1)
			// % DataCenter.param[col];
			// newRow[col] = newValue;
			// }
			//
			// if (!this.containsMFS(newRow))
			// break;
			//
			// if (tryT > DataCenter.param.length * DataCenter.param[0]) {
			// tryT = 0;
			// changeNum++;
			// }

			//
			for (int j = 0; j < DataCenter.param.length; j++)
				newRow[j] = randomGenerator.nextInt(DataCenter.param[j]);
			if (!this.containsMFS(newRow))
				break;
			// gen not contain the mfs
		}

		// System.out.println("get out ");
		// unCovered = cm.setCover(unCovered, coveringArray, table[i]);
		//

		//
	}

	@Override
	public void startAnneling() {
		// TODO Auto-generated method stub
		// init
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

	public void reset() {
		freezingTimes = 0;
	}

	public void setUncovered(Tuple mfs) {
		for (int[] test : this.table) {
			TestCaseImplement testCase = new TestCaseImplement();
			testCase.setTestCase(test);
			if (testCase.containsOf(mfs)) {
				CoveringManagementInf cm = new CoveringManage();
				unCovered = cm.rmCover(unCovered, coveringArray, newRow);
				unCovered = cm.setCover(unCovered, coveringArray, oldRow); // 回退
			}
		}
	}

	public boolean containsMFS(int[] test) {
		boolean result = false;
		for (Tuple mfs : this.mfs) {
			if (this.testContainSchema(test, mfs)) {
				result = true;
				break;
			}
		}
		return result;
	}

	public boolean testContainSchema(int[] testCase, Tuple tuple) {
		int[] index = tuple.getParamIndex();
		int[] value = tuple.getParamValue();

		for (int i = 0; i < tuple.getDegree(); i++) {
			if (index[i] >= testCase.length)
				return false;
			if (testCase[index[i]] != value[i])
				return false;
		}
		return true;
	}

	@Override
	public boolean isOk() {
		// TODO Auto-generated method stub
		return this.feasiable;
	}

	public static void main(String[] args) {
		
		//[ - , - , - , 1 , 1 ]
		//[ 0 , - , - , 1 , 0 ]
		//[ 0 , - , - , 1 , 2 ]
		 
		int[] param = new int[] { 3, 3, 3, 3, 3 };
		DataCenter.init(param, 2);
		// System.out.println(DataCenter.coveringArrayNum);

		int[] coveringArray = new int[DataCenter.coveringArrayNum];
		Integer unCovered = coveringArray.length;
		
		for( int i : DataCenter.index)
			System.out.print(i +" ");
		System.out.println();
	
		
		int[] wrong = new int[] { 0, 0, 0, 1, 0 };

		TestCase wrongCase = new TestCaseImplement();
		wrongCase.setTestState(TestCase.FAILED);
		((TestCaseImplement) wrongCase).setTestCase(wrong);

		int[] wrong2 = new int[] { 1, 0, 1, 1, 1 };

		TestCase wrongCase2 = new TestCaseImplement();
		wrongCase2.setTestState(TestCase.FAILED);
		((TestCaseImplement) wrongCase2).setTestCase(wrong2);
		
		
		int[] wrong3 = new int[] { 0, 0, 1, 1, 2 };

		TestCase wrongCase3 = new TestCaseImplement();
		wrongCase3.setTestState(TestCase.FAILED);
		((TestCaseImplement) wrongCase3).setTestCase(wrong3);

		Tuple bug1 = new Tuple(3, wrongCase);
		bug1.set(0, 0);
		bug1.set(1, 3);
		bug1.set(1, 4);

		Tuple bug2 = new Tuple(2, wrongCase2);
		bug2.set(0, 3);
		bug2.set(1, 4);
		
		Tuple bug3 = new Tuple(3, wrongCase3);
		bug3.set(0, 0);
		bug3.set(1, 3);
		bug3.set(1, 4);
		
		HashSet<Tuple> bugs = new HashSet<Tuple>();
		bugs.add(bug1);
		bugs.add(bug2);
		bugs.add(bug3);
		
		unCovered -= 1;
		
		CoveringManage cm = new CoveringManage();
		int index = cm.getIndex(bug1);
		
		int thisIndex = DataCenter.index[index];
//		for (int i = 0; i < bug1.getDegree(); i++) {
//
//			int k = i + 1;
//			int temR = bug1.getParamValue()[i];
//			while (k < bug1.getDegree()) {
//				temR *= DataCenter.param[bug1.getParamIndex()[k]];
//				k++;
//			}
//
//			thisIndex += temR;
//		}
//
//		
//		coveringArray[thisIndex] = 1;
//		System.out.println("index :" + thisIndex);
		index = cm.getIndex(bug2);
		thisIndex = DataCenter.index[index];
		for (int i = 0; i < bug2.getDegree(); i++) {

			int k = i + 1;
			int temR = bug2.getParamValue()[i];
			while (k < bug2.getDegree()) {
				temR *= DataCenter.param[bug2.getParamIndex()[k]];
				k++;
			}

			thisIndex += temR;
		}

		System.out.println("index :" + thisIndex);
		coveringArray[thisIndex] = 1;
		
		
		
		AnnelProcessFeedBack ap = new AnnelProcessFeedBack(bugs, unCovered, 2, coveringArray, unCovered,  0.9998);
		

		
		
		ap.startAnneling();
		
		System.out.println(ap.isOk());
		for(int[] test : ap.table){
			for(int i : test){
				System.out.print(i + " ");
			}
			
			System.out.println();
		}
		
		for(int i : ap.coveringArray)
			System.out.print(i + " ");
		System.out.println();

	}

}