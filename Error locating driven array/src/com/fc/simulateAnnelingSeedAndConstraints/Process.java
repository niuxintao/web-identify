package com.fc.simulateAnnelingSeedAndConstraints;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fc.simulateAnneling.DataCenter;
import com.fc.testObject.TestCase;
import com.fc.testObject.TestCaseImplement;
import com.fc.tuple.Tuple;

public class Process {
	public double T;
	public double decrement;
	public long time;
	public int[][] rsTable;
	public int[][] seed;
	public List<Tuple> constraints;

	public Process(double T, double decrement, int[][] seed, List<Tuple> constraints) {
		this.T = T;
		this.constraints = constraints;
		this.decrement = decrement;
		this.seed = seed;
	}
	
	public Process(int[][] seed, List<Tuple> constraints) {
		this.constraints = constraints;
		this.T = 2;
		this.decrement = 0.9998;
		this.seed = seed;
	}

	public void process() {
		int start = DataCenter.coveringArrayNum;
		int end = 0;
		boolean flag = false;
		long starttime = new Date().getTime();
		while (start > end || !flag) {
			if (start <= end)// 
			{
				end = start;
				start *= 2;
			}
			int middle = (start + end) / 2;
//			System.out.println(middle);
			AnnelProcessSeedConstraint al = new AnnelProcessSeedConstraint(middle, T,decrement, seed, constraints);
			al.startAnneling();
			if (al.isOk()) {
				start = middle - 1;
				rsTable = al.table;
				flag = true;
			} else
				end = middle + 1;
		}
		long endtime = new Date().getTime();
		time = endtime - starttime;
	}

	static public void main(String[] args) {
		int param[] = { 3, 3, 3, 3 };
		int[][] seed = new int[][] {{0, 1, 2, 2}};
		DataCenter.init(param, 3);
//		System.out.println(DataCenter.index.length);
//		System.out.println(DataCenter.coveringArrayNum);
		
		
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
		
		//
		Process t = new Process(seed, tuples );
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
