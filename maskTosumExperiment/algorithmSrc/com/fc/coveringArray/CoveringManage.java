package com.fc.coveringArray;

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
					thisIndex+=temR;
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
					thisIndex+=temR;
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
