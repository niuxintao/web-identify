package com.fc.simulateAnnelingSeedAndConstraints;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.fc.simulateAnneling.DataCenter;
import com.fc.forbiddnen.GetAlltheForbidden;
import com.fc.forbiddnen.IsContainForbidden;
import com.fc.simulateAnneling.AnnelInf;
import com.fc.simulateAnneling.CoveringManage;
import com.fc.simulateAnneling.CoveringManagementInf;
import com.fc.testObject.TestCase;
import com.fc.testObject.TestCaseImplement;
import com.fc.tuple.Tuple;

public class AnnelProcessSeedConstraint implements AnnelInf {
	public int[] coveringArray; // 覆盖记录，在�?个点的数值意味着被覆盖的次数
	public Integer unCovered;// 未被覆盖�?
	public int freezingTimes;// 僵住的次�?
	public int[][] table; // N*K的表
	private Random randomGenerator = new Random(); // 随机生成�?
	private int rowChange;// 改变的行
	private int[] oldRow; // 改变前的�?
	private int[] newRow; // 改变后的�?
	private boolean feasiable;// 是否可行
	private int N; // 给定大小N
	private double T;// 温度T
	private double decrement;// 控温下降

	private int[][] seed;
	private List<Tuple> allForbidden;

	private IsContainForbidden isfor;

	protected DealTupleOfIndex DOI;

	public AnnelProcessSeedConstraint(int N, double T, double decrement,
			int[][] seed, List<Tuple> givenForbidden) {
		this.N = N;
		this.T = T;
		this.decrement = decrement;
		this.seed = seed;

		GetAlltheForbidden gaf = new GetAlltheForbidden();
		isfor = new IsContainForbidden();
		this.allForbidden = gaf.getAlltheForbidden(givenForbidden,
				DataCenter.param);

		DOI = new DealTupleOfIndex(new interaction.DataCenter(DataCenter.param,
				DataCenter.degree));
	}

	public AnnelProcessSeedConstraint(int N, int[][] seed,
			List<Tuple> givenForbidden) {
		this.N = N;
		this.T = 2;
		this.decrement = 0.9998;
		this.seed = seed;

		GetAlltheForbidden gaf = new GetAlltheForbidden();
		isfor = new IsContainForbidden();
		this.allForbidden = gaf.getAlltheForbidden(givenForbidden,
				DataCenter.param);

		DOI = new DealTupleOfIndex(new interaction.DataCenter(DataCenter.param,
				DataCenter.degree));
	}

	public int[] randomGenerateTestcaseWithoutConst() {
		int[] row = new int[DataCenter.param.length];
		int[] temp = new int[0];
		for (int j = 0; j < DataCenter.param.length; j++) {

			List<Integer> values = new ArrayList<Integer>();
			for (int i = 0; i < DataCenter.param[j]; i++)
				values.add(i);

			int index = randomGenerator.nextInt(values.size());
			int value = values.get(index);

			while (isfor.isPossibleForTheCurrentValueContainForbidden(temp,
					value, DataCenter.param, this.allForbidden)) {
				values.remove(index);
				if (values.size() == 0)
					return null;
				index = randomGenerator.nextInt(values.size());
				value = values.get(index);
			}

			int[] ttmp = new int[temp.length + 1];
			for (int i = 0; i < temp.length; i++)
				ttmp[i] = temp[i];
			ttmp[temp.length] = value;
			temp = ttmp;

			row[j] = value;
		}

//		for (int i : row)
//			System.out.print(i + ", ");
//		System.out.println();
		return row;
	}

	public int setCoverage(Tuple tuple, Integer unCovered, int[] coveringArray) {
		int index = DOI.getIndexOfTuple(tuple);
		if (coveringArray[index] == 0) {
			coveringArray[index] = 1;
			unCovered--;
		}
		return unCovered;
	}

	public Integer setCoverage(List<Tuple> newlyMFS, Integer unCovered,
			int[] coveringArray) {
		// itself
		for (Tuple mfs : newlyMFS) {
			if (mfs.getDegree() == DataCenter.degree) {
				unCovered = setCoverage(mfs, unCovered, coveringArray);
			} else if (mfs.getDegree() < DataCenter.degree) {
				// the parent
				List<Tuple> parentT = mfs
						.getFatherTuplesByDegree(DataCenter.degree, DataCenter.param);
				
				for (Tuple parent : parentT) {
					unCovered = setCoverage(parent, unCovered, coveringArray);
				}
				
				
			}
		}
		return unCovered;
	}

	@Override
	public void initAnneling() {
		// TODO Auto-generated method stub
		// 随机生成�?个N*K的表`
		this.feasiable = false;
		CoveringManagementInf cm = new CoveringManage();
		// 随机生成�?个N*K的表
		// 初始化coveringArray
		this.coveringArray = new int[DataCenter.coveringArrayNum];
		unCovered = this.coveringArray.length;
		this.freezingTimes = 0;
		table = new int[N][DataCenter.param.length];

		// set forbidden coverage
//		 System.out.println(unCovered);
		unCovered = setCoverage(this.allForbidden, unCovered, coveringArray);
//		 System.out.println(unCovered);

		if (seed.length <= N)
			for (int i = 0; i < seed.length; i++) {
				table[i] = seed[i];
				unCovered = cm.setCover(unCovered, coveringArray, table[i]);
			}

//		 System.out.println(unCovered);

		for (int i = seed.length; i < N; i++) {
			table[i] = randomGenerateTestcaseWithoutConst();
			if (table[i] != null)
				unCovered = cm.setCover(unCovered, coveringArray, table[i]);
		}

//		 System.out.println(unCovered);
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
		// 任意挑出�?个cell改变其�??
		// this.rowChange = randomGenerator.nextInt(N - seed.length);
		// rowChange += seed.length;

		List<Integer> values = new ArrayList<Integer>();
		for (int i = seed.length; i < N; i++)
			values.add(i);

		int index = randomGenerator.nextInt(values.size());
		int row = values.get(index);
		int[] temp = table[row];
		int[] result = getIndexAndValue(temp);

		while (result[0] == -1) {
			values.remove(index);
			if (values.size() == 0) {
				return;
				// break;
			}
			index = randomGenerator.nextInt(values.size());
			row = values.get(index);
			temp = table[row];
			result = getIndexAndValue(temp);
		}

		rowChange = row;
		int col = result[0];
		int newValue = result[1];

		oldRow = table[rowChange];
		newRow = table[rowChange].clone();
		newRow[col] = newValue;
	}

	public int[] getIndexAndValue(int[] test) {
		int[] result = new int[2];

		List<Integer> values = new ArrayList<Integer>();
		for (int i = 0; i < DataCenter.param.length; i++)
			values.add(i);

		int index = randomGenerator.nextInt(values.size());
		int value = values.get(index);

		int vvalue = getValue(value, test);

		while (vvalue == -1) {
			values.remove(index);
			if (values.size() == 0) {
				result[0] = -1;
				result[1] = -1;
				break;
			}
			index = randomGenerator.nextInt(values.size());
			value = values.get(index);

			vvalue = getValue(value, test);
		}

		result[0] = value;
		result[1] = vvalue;

		return result;
	}

	public int getValue(int j, int[] test) {
		List<Integer> values = new ArrayList<Integer>();
		for (int i = 0; i < DataCenter.param[j]; i++)
			values.add(i);

		int index = randomGenerator.nextInt(values.size());
		int value = values.get(index);

		while (isfor.isPossibleForTheCurrentMutationContainForbidden(test, j,
				value, DataCenter.param, this.allForbidden)) {
			values.remove(index);
			if (values.size() == 0)
				return -1;
			index = randomGenerator.nextInt(values.size());
			value = values.get(index);
		}

		return value;
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
				unCovered = cm.setCover(unCovered, coveringArray, oldRow); // 回�??
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
		int[] param = new int[4];
		for (int i = 0; i < param.length; i++)
			param[i] = 3;

		int[] test = new int[4];
		for (int i = 0; i < 4; i++)
			test[i] = 0;
		TestCase te = new TestCaseImplement(test);
		Tuple tuple1 = new Tuple(2, te);
		tuple1.set(0, 0);
		tuple1.set(1, 2); // (0, -, 0, -)

		test = new int[4];
		for (int i = 0; i < 4; i++)
			test[i] = 1;

		te = new TestCaseImplement(test);
		Tuple tuple2 = new Tuple(2, te);
		tuple2.set(0, 0);
		tuple2.set(1, 2); // (1, -, 1, -)

		test = new int[4];
		for (int i = 0; i < 4; i++)
			test[i] = 1;

		test[2] = 0;
		test[3] = 0; // (1, 1, 0, 0)
		te = new TestCaseImplement(test);
		Tuple tuple3 = new Tuple(2, te);
		tuple3.set(0, 0);
		tuple3.set(1, 1); // (1, 1, -, -)

		test = new int[4];
		for (int i = 0; i < 4; i++)
			test[i] = 0;

		te = new TestCaseImplement(test);
		Tuple tuple4 = new Tuple(2, te);
		tuple4.set(0, 0);
		tuple4.set(1, 3); // (0, -, -, 0)

		test = new int[4];
		for (int i = 0; i < 4; i++)
			test[i] = 2;

		te = new TestCaseImplement(test);
		Tuple tuple5 = new Tuple(2, te);
		tuple5.set(0, 0);
		tuple5.set(1, 3); // (2, -, -, 2)

		List<Tuple> tuples = new ArrayList<Tuple>();
		tuples.add(tuple1);
//		tuples.add(tuple2);
//		tuples.add(tuple3);
//		tuples.add(tuple4);
//		tuples.add(tuple5);

		DataCenter.init(param, 3);
		int[][] seed = new int[][] { { 0, 1, 2, 2 } };

		AnnelProcessSeedConstraint apsc = new AnnelProcessSeedConstraint(600,
				seed, tuples);

		// apsc.initAnneling();
		apsc.startAnneling();
		if (apsc.isOk()) {
			for (int[] testcase : apsc.table) {
				for (int i : testcase)
					System.out.print(i + " ");
				System.out.println();
			}
		}
	}

}