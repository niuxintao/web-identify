package com.fc.process;

import com.fc.DataCenter.DataCenter;
import com.fc.DataCenter.LongBitSet;
import com.fc.testObject.TestCase;
import com.fc.testObject.TestSuite;
import com.fc.tuple.Tuple;

import java.util.BitSet;
import java.util.List;

public class BasicAnalyse {
	public int getTupleIndex(Tuple tuple, int baseIndex, int[] param) {
		int index = 0;

		for (int i = 0; i < tuple.getDegree(); ++i) {
			int temR = tuple.getParamValue()[i];

			for (int j = i + 1; j < tuple.getDegree(); ++j) {
				temR *= param[tuple.getParamIndex()[j]];
			}

			index += temR;
		}

		index += baseIndex;
		return index;
	}
	
	public long getTupleIndex(Tuple tuple, long baseIndex, int[] param) {
		long index = 0;

		for (int i = 0; i < tuple.getDegree(); ++i) {
			int temR = tuple.getParamValue()[i];

			for (int j = i + 1; j < tuple.getDegree(); ++j) {
				temR *= param[tuple.getParamIndex()[j]];
			}

			index += temR;
		}

		index += baseIndex;
		return index;
	}


	public List<Tuple> getTuplesFromTestCase(TestCase testCase, int degree){
		Tuple tuple = new Tuple(testCase.getLength(), testCase);
		int[] a = new int[testCase.getLength()];
		for(int i = 0; i < a.length; i++)
			a[i] = i;
		tuple.setParamIndex(a);
		return tuple.getChildTuplesByDegree(degree);
	}
	public int getCovered(int start, int end, TestSuite suite,
			DataCenter dataCenter) {
		//int[] coveringArray = new int[(int) dataCenter.getCoveringArrayNum()];
		int covered = 0;
		
		LongBitSet coveringArray = new LongBitSet(dataCenter.getCoveringArrayNum());

		for (int i = start; i < end; ++i) {
			List<Tuple> tuples = getTuplesFromTestCase(suite.getAt(i),
					dataCenter.getDegree());

			for (int j = 0; j < tuples.size(); ++j) {
				long index = this.getTupleIndex((Tuple) tuples.get(j),
						dataCenter.getIndex()[j], dataCenter.getParam());
				if (coveringArray.get(index) == false) {
					++covered;
				}

				coveringArray.set(index);
			}
		}

//		System.out.println("covered: "+ covered);
		return covered;
		
	}

	public int[] dealDetection(int start, int end, TestSuite suite,
			DataCenter dataCenter) {
		int[] result = new int[end - start];
		int covered = 0;
		//System.out.println("all : "+ dataCenter.getCoveringArrayNum());
		LongBitSet coveringArray = new LongBitSet(dataCenter.getCoveringArrayNum());
		
		
//		System.out.println("all : " + result.length);
		for (int i = start; i < end; ++i) {
//			long time = System.currentTimeMillis();
//			System.out.println("test case : " + i);
			List<Tuple> tuples = getTuplesFromTestCase(suite.getAt(i),
					dataCenter.getDegree());

//			System.out.println(tuples.size());
			for (int j = 0; j < tuples.size(); ++j) {
				long index = this.getTupleIndex((Tuple) tuples.get(j),
						dataCenter.getIndex()[j], dataCenter.getParam());
//				System.out.println("the " + j + "index  : " + index);
				if (coveringArray.get(index) == false) {
					++covered;
				}

				coveringArray.set(index);
			}

			result[i - start] = covered;
			
//			long time2 = System.currentTimeMillis();
//			time2 -= time;
//			System.out.println("time (s): " + (double)time2/1000);
		}

		return result;
	}
	
	public static void main(String[] args){
		BitSet coveringArray = new BitSet(10);
		System.out.println(coveringArray.toString());
		for(int i = 0; i < coveringArray.length(); i++)
			System.out.print(coveringArray.get(i) + " ");
		System.out.println();
		
		if(coveringArray.get(11) == false)
			coveringArray.set(11);
	//	coveringArray.set(7);
		System.out.println(coveringArray.toString());
		for(int i = 0; i < coveringArray.length(); i++)
			System.out.print(coveringArray.get(i)  + " " );
		
		System.out.println();
		
	}
}