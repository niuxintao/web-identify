package com.fc.coveringArray;

public class DataCenter {
	// static public int N;
	static public int[] param;
	static public int degree = -1;
	static public int maxFreeing;
	static public int coveringArrayNum;
	static public int[] index;

	static public void init(int[] param, int degree) {
		DataCenter.param = param.clone();
		DataCenter.degree = degree;
		myStack stack = new myStack(degree);
		int indexNum = 1;
		for (int k = 0; k < degree; k++) {
			indexNum *= param.length - degree + k + 1;
		}

		for (int k = 1; k <= degree; k++) {
			indexNum /= k;
		}
		DataCenter.index = new int[indexNum];
		int currentPoint = 0;
		int allNum = 0;
		int i = 0;
		while (true) {
			if (stack.isFull()) {
				DataCenter.index[i] = allNum;
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
//		System.out.println(i);
		DataCenter.coveringArrayNum = allNum;
		DataCenter.maxFreeing = allNum;
	}
}

class myStack {
	public int size;
	public int currentIndex;
	public int[] dataIndexs;
	public int[] data;

	public myStack(int size) {
		this.size = size;
		data = new int[size];
		dataIndexs = new int[size];
	}

	public boolean isFull() {
		if (this.currentIndex == size)
			return true;
		else
			return false;
	}

	public boolean isEmpty() {
		if (this.currentIndex == 0)
			return true;
		else
			return false;
	}

	public void push(int num, int dataIndex) {
		data[this.currentIndex] = num;
		dataIndexs[this.currentIndex] = dataIndex;
		this.currentIndex++;
	}

	public void pop() {
		this.currentIndex--;
	}

	public int mutli() {
		int result = 1;
		for (int i = 0; i < size; i++)
			result *= data[i];
		return result;
	}
}