package interaction;

public class DataCenter {
	private int[] param;
	private int degree = -1;
	private int coveringArrayNum; // all the possible t-way combiantions
	private int[] index; // the index of each possible combination

	public DataCenter(int[] param, int degree) {
		this.init(param, degree);
	}

	public static void main(String[] args) {
		int[] param = { 2, 2, 2, 2 };
		int[] index = { 0, 1 };
		int[] value = { 1, 0 };
		DataCenter dc = new DataCenter(param, 2);
		System.out.println(dc.getIndexInArray(index, value));
	}

	// the index = (C(n - (start = -1) - 1, d) - C(n - 1th, d) ) + (C(n - (start
	// = 1th) - 1), d - 1 ) - (C(n- 2th), d - 1) + .... + (C(n- (last-1)th - 1,
	// 1) - C(n - last, 1) )
	public int getIndexInArray(int[] index, int[] value) {
		int start = -1;
		int n = this.param.length;
		int d = index.length;

		int indexFirst = 0;
		for (int i = 0; i < index.length; i++) {
			int cur = C(n - start - 1, d) - C(n - index[i], d);
			indexFirst += cur;
			start = index[i];
			d = d - 1;
		}

		int indInd = this.index[indexFirst];

		int valueInd = 0;
		for (int i = 0; i < value.length; i++) {
			int cur = value[i];
			int j = i + 1;
			while (j < value.length) {
				cur *= this.param[j];
				j++;
			}
			valueInd += cur;
		}

		int result = indInd + valueInd;
		return result;

	}

	public int[][] fromArrayIndexToCombination(int array) {
		int[][] result = new int[2][];

		// binary search to get the index

		// to get the i that index[i] < array, and index[i+1] > array

		// than break i to the indexes:
		// loop the first :
		// i > C(1th, d) and i < C(2th,d)
		// the the second
		// i > C(1th, d - 1 ) and i < C(2th, d - 1)

		// get all the indexes
		// then the values
		// ( array - index[i] ) (param[j] * param[j + 1])
		// .....

		return result;

	}

	public int C(int n, int d) {
		int up = 1;
		int lower = 1;
		for (int i = 0; i < d; i++) {
			up *= (n - i);
			lower *= (i + 1);
		}
		int result = up / lower;
		return result;
	}

	public int[] getParam() {
		return param;
	}

	public int getDegree() {
		return degree;
	}

	public int getCoveringArrayNum() {
		return coveringArrayNum;
	}

	public int[] getIndex() {
		return index;
	}

	public void init(int[] param, int degree) {
		this.param = param.clone();
		this.degree = degree;
		myStack stack = new myStack(degree);
		int indexNum = 1;
		for (int k = 0; k < degree; k++) {
			indexNum *= param.length - degree + k + 1;
		}
		for (int k = 1; k <= degree; k++) {
			indexNum /= k;
		}

		this.index = new int[indexNum];
		int currentPoint = 0;
		int allNum = 0;
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
		this.coveringArrayNum = allNum;
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