package ct;

class MyStack {
	public int size;
	public int currentIndex;
	public int[] dataIndexs;

	public MyStack(int size) {
		this.size = size;
		currentIndex = 0;
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

	public void push(int dataIndex) {
		dataIndexs[this.currentIndex] = dataIndex;
		this.currentIndex++;
	}

	public void pop() {
		this.currentIndex--;
	}
}