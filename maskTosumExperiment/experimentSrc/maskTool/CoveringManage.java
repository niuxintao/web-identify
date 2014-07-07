package maskTool;

import java.util.HashSet;
import java.util.List;

import com.fc.coveringArray.CoveringManagementInf;
import com.fc.coveringArray.DataCenter;
import com.fc.testObject.TestCaseImplement;
import com.fc.tuple.Tuple;

public class CoveringManage implements CoveringManagementInf {

	@Override
	public int rmCover(Integer unCovered, int[] coveringArray, int[] row) {
		// TODO Auto-generated method stub
		myStack stack = new myStack(DataCenter.degree);
		int i = 0;
		int currentPoint = 0;
		while (true) {
			if (stack.isFull()) {
				int thisIndex = DataCenter.index[i];
				for (int j = 0; j < DataCenter.degree; j++) {
					int k = j + 1;
					int temR = row[stack.dataIndexs[j]];
					while (k < DataCenter.degree) {
						temR *= stack.data[k];
						k++;
					}
					thisIndex += temR;
				}
				if (coveringArray[thisIndex] == 1)
					unCovered++;
				coveringArray[thisIndex] = coveringArray[thisIndex] - 1;
				i++;
				stack.pop();
			} else if (currentPoint == DataCenter.param.length) {
				if (stack.isEmpty())
					break;
				stack.pop();
				currentPoint = stack.dataIndexs[stack.currentIndex] + 1;

			} else {
				stack.push(DataCenter.param[currentPoint], currentPoint);
				currentPoint++;
			}
		}
		return unCovered;
	}

	public int rmCover(Integer unCovered, int[][] table, int[] coveringArray,
			Tuple mfs) {
		// TODO Auto-generated method stub
		for (int[] row : table) {
			if (this.containMFS(row, mfs)) {
				unCovered = this.rmCover(unCovered, coveringArray, row);
			}
		}

		this.setCover(unCovered, coveringArray, mfs);

		return unCovered;
	}

	public int rmCover(Integer unCovered, List<int[]> table,
			int[] coveringArray, HashSet<Tuple> mfs) {
		// TODO Auto-generated method stub
		for (int[] row : table) {
			boolean contain = false;
			for (Tuple m : mfs) {
				if (this.containMFS(row, m)) {
					contain = true;
					break;
					// unCovered = this.rmCover(unCovered, coveringArray, row);
				}
			}
			if (contain) {
				unCovered = this.rmCover(unCovered, coveringArray, row);
			}
		}
		for (Tuple m : mfs)
			this.setCover(unCovered, coveringArray, m);

		return unCovered;
	}

	public boolean containMFS(int[] testCase, Tuple tuple) {
		int[] index = tuple.getParamIndex();
		int[] value = tuple.getParamValue();

		for (int i = 0; i < tuple.getDegree(); i++) {
			if (index[i] >= testCase.length)
				return false;
			if (testCase[index[i]] != value[i])
				return false;
		}
		return true;
	}

	public int getC(int k, int m) {
		int zi = 1;
		for (int i = 0; i < m; i++)
			zi *= (k - i);

		for (int i = 0; i < m; i++)
			zi /= (m - i);

		return zi;
	}

	public int setCover(Integer unCovered, int[] coveringArray, Tuple tuple) {
		int num = getIndex(tuple);

		int thisIndex = DataCenter.index[num];

		for (int i = 0; i < tuple.getDegree(); i++) {

			int k = i + 1;
			int temR = tuple.getParamValue()[tuple.getParamIndex()[i]];
			while (k < tuple.getDegree()) {
				temR *= DataCenter.param[tuple.getParamIndex()[k]];
				k++;
			}

			thisIndex += temR;
		}

		if (coveringArray[thisIndex] == 0)
			unCovered--;
		coveringArray[thisIndex] = coveringArray[thisIndex] + 1;

		return unCovered;
	}

	private int getIndex(Tuple tuple) {
		int beforeIndex = 0;

		int num = 0;
		for (int i = 0; i < tuple.getDegree(); i++) {
			int theIndex = tuple.getParamIndex()[i];

			int curLeft = tuple.getDegree() - i - 1;

			for (int j = beforeIndex; j < theIndex; j++) {
				num += this.getC(DataCenter.param.length - j - 1, curLeft);
			}

			beforeIndex = theIndex + 1;
		}
		return num;
	}

	public int getIndex2(Tuple tuple) {
		// TODO Auto-generated method stub
		myStack stack = new myStack(DataCenter.degree);
		int i = 0;
		int currentPoint = 0;
		while (true) {
			if (stack.isFull()) {
				boolean flag = true;
				for (int j = 0; j < DataCenter.degree; j++) {
					if (tuple.getParamIndex()[j] != stack.dataIndexs[j]) {
						flag = false;
						break;
					}
				}
				if (flag)
					return i;

				i++;
				stack.pop();
			} else if (currentPoint == DataCenter.param.length) {
				if (stack.isEmpty())
					break;
				stack.pop();
				currentPoint = stack.dataIndexs[stack.currentIndex] + 1;

			} else {
				stack.push(DataCenter.param[currentPoint], currentPoint);
				currentPoint++;
			}
		}
		return -1;
	}

	public static void main(String[] args) {
		DataCenter.init(new int[] { 1, 2, 3, 3, 3, 8, 3 }, 2);
		int[] test = { 1, 2, 1, 2 };
		TestCaseImplement testCase = new TestCaseImplement(test);
		Tuple tuple = new Tuple(2, testCase);
		tuple.setParamIndex(new int[] { 3, 6 });
		CoveringManage cm = new CoveringManage();

		System.out.println(cm.getIndex(tuple));
		System.out.println(cm.getIndex2(tuple));
	}

	@Override
	public int setCover(Integer unCovered, int[] coveringArray, int[] row) {
		// TODO Auto-generated method stub
		myStack stack = new myStack(DataCenter.degree);
		int i = 0;
		int currentPoint = 0;
		while (true) {
			if (stack.isFull()) {
				int thisIndex = DataCenter.index[i];
				for (int j = 0; j < DataCenter.degree; j++) {
					int k = j + 1;
					int temR = row[stack.dataIndexs[j]];
					while (k < DataCenter.degree) {
						temR *= stack.data[k];
						k++;
					}
					thisIndex += temR;
				}
				if (coveringArray[thisIndex] == 0)
					unCovered--;
				coveringArray[thisIndex] = coveringArray[thisIndex] + 1;
				i++;
				stack.pop();
			} else if (currentPoint == DataCenter.param.length) {
				if (stack.isEmpty())
					break;
				stack.pop();
				currentPoint = stack.dataIndexs[stack.currentIndex] + 1;

			} else {
				stack.push(DataCenter.param[currentPoint], currentPoint);
				currentPoint++;
			}
		}
		return unCovered;
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
