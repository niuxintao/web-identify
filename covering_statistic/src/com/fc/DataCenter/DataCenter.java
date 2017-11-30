package com.fc.DataCenter;

public class DataCenter {
	// static public int N;
	private int[] param;
	private int degree = -1;
	private long maxFreeing;
	private long coveringArrayNum;
	private long[] index;
	private int n;

	public int[] getParam() {
		return param;
	}

	public int getDegree() {
		return degree;
	}

	public long getCoveringArrayNum() {
		return coveringArrayNum;
	}

	public long[] getIndex() {
		return index;
	}
	
	public void reset(int[] param, int degree){
		init(param, degree);
	}

	public long getMaxFreeing() {
		return maxFreeing;
	}

	public int getN() {
		return n;
	}

	public void init(int[] param, int degree) {
//		System.out.println(param.length + "  " + degree );
		this.param = param.clone();
		this.n = param.length;
		this.degree = degree;
		MyStack stack = new MyStack(degree);
		int indexNum = 1;
//		int i = 1;
		for (int k = 0; k < degree; k++) {
			indexNum *= param.length - degree + k + 1;
			indexNum /= (k+1);
		}

//		for (int k = 1; k <= degree; k++) {
//			indexNum /= k;
//		}
//		System.out.println("indexNum:" + indexNum);
		this.index = new long[indexNum];
		int currentPoint = 0;
		long allNum = 0;
		int i = 0;
		while (true) {
			if (stack.isFull()) {
				this.index[i] = allNum;
				allNum += stack.mutli();
				i++;
				stack.pop();
			} else if (currentPoint == param.length) {
				if (stack.isEmpty())
					break;
				stack.pop();
				currentPoint = stack.dataIndexs[stack.currentIndex] + 1;

			} else {
				stack.push(param[currentPoint], currentPoint);
				currentPoint++;
			}
		}
		//System.out.println(allNum);
		this.coveringArrayNum = allNum;
		this.maxFreeing = allNum;
	}
}