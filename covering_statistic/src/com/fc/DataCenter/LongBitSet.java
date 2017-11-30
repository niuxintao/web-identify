package com.fc.DataCenter;

import java.util.BitSet;




public class LongBitSet {
	private BitSet[] data;
//    private int MAX_VALUE = 10000;
	public LongBitSet(long size){
		int max = Integer.MAX_VALUE;
		int times = (int) (size / max);
		times += 1;
		data = new BitSet[times];
		for(int i = 0; i < times - 1; i++){
			data[i] = new BitSet(Integer.MAX_VALUE);
		}
		long chengshu = (times-1) * Integer.MAX_VALUE;
		int next = (int) (size - chengshu);
		data[times - 1] = new BitSet(next);
	}
	
	public void set(long index){
		int max = Integer.MAX_VALUE;
		int times = (int) (index / max);
		int next = (int) (index -  (times * Integer.MAX_VALUE));
		data[times].set(next);
	}
	
	public boolean get(long index){
		int max = Integer.MAX_VALUE;
		int times = (int) (index / max);
		int next = (int) (index -  (times * Integer.MAX_VALUE));
		return data[times].get(next);
	}
	
}
