package com.fc.DataCenter;

class MyStack {
	public int size;
	public int currentIndex;
	public int[] dataIndexs;
	public int[] data;

	public MyStack(int size) {
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