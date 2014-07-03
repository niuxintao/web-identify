package maskTool;

import com.fc.coveringArray.CoveringManagementInf;
import com.fc.coveringArray.DataCenter;
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

	public int rmCover(Integer unCovered, int[] coveringArray, int[] row,
			Tuple mfs) {
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
