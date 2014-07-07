package maskTool;

import java.util.HashSet;
import java.util.Random;

import com.fc.coveringArray.AnnelInf;
import com.fc.coveringArray.CoveringManagementInf;
import com.fc.coveringArray.DataCenter;
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

//	public AnnelProcessFeedBack(int N, double T, double decrement) {
//		this.N = N;
//		this.T = T;
//		this.decrement = decrement;
//
//		this.initAnneling();
//	}
	
	private HashSet<Tuple> mfs;



	public AnnelProcessFeedBack(HashSet<Tuple> mfs, int N, double T, int[] coveringArray,
			Integer unCovered, double decrement) {
		// TODO Auto-generated constructor stub
		this.N = N;
		this.T = T;
		this.decrement = decrement;
		this.unCovered = unCovered;
		this.coveringArray = coveringArray;
		this.mfs = mfs;
		
		this.initAnneling();
	}

	@Override
	public void initAnneling() {
		// TODO Auto-generated method stub
		// 随机生成一个N*K的表`
		this.feasiable = false;
		CoveringManagementInf cm = new CoveringManage();
		// 随机生成一个N*K的表
		// 初始化coveringArray
//		this.coveringArray = new int[DataCenter.coveringArrayNum];
//		unCovered = this.coveringArray.length;
		this.freezingTimes = 0;
		table = new int[N][DataCenter.param.length];
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < DataCenter.param.length; j++)
				table[i][j] = randomGenerator.nextInt(DataCenter.param[j]);
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
		
		int col = randomGenerator.nextInt(DataCenter.param.length);
		int newValue = (table[rowChange][col] + 1) % DataCenter.param[col];
		newRow[col] = newValue;
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
	
	public boolean containsMFS(int[] test){
		boolean result = false;
		for(Tuple mfs : this.mfs){
			if(this.testContainSchema(test, mfs)){
				result = true;
				break;
			}
		}
		return result;
	}
	
	public boolean testContainSchema(int[] testCase, Tuple tuple){
		int[] index = tuple.getParamIndex();
		int[] value = tuple.getParamValue();

		for (int i = 0; i < tuple.getDegree(); i++) {
			if (index[i] >=  testCase.length)
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

	}

}