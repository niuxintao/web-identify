package getCovered;

import java.util.HashSet;
import java.util.List;


public class CoveringManage  {
	
	private DataCenter dataCenter;
	
	public CoveringManage(DataCenter dataCenter){
		this.dataCenter = dataCenter;
	}

	public int rmCover(Integer unCovered, int[] coveringArray, int[] row) {
		// TODO Auto-generated method stub
		myStack2 stack = new myStack2(dataCenter.degree);
		int i = 0;
		int currentPoint = 0;
		while (true) {
			if (stack.isFull()) {
				int thisIndex = dataCenter.index[i];
				for (int j = 0; j < dataCenter.degree; j++) {
					int k = j + 1;
					int temR = row[stack.dataIndexs[j]];
					while (k < dataCenter.degree) {
						temR *= stack.data[k];
						k++;
					}
					thisIndex += temR;
				}
//				System.out.println(coveringArray[thisIndex]);
				if (coveringArray[thisIndex] == 1)
					unCovered++;
				coveringArray[thisIndex] = coveringArray[thisIndex] - 1;
				i++;
				stack.pop();
			} else if (currentPoint == dataCenter.param.length) {
				if (stack.isEmpty())
					break;
				stack.pop();
				currentPoint = stack.dataIndexs[stack.currentIndex] + 1;

			} else {
				stack.push(dataCenter.param[currentPoint], currentPoint);
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
//			for (int i : row)
//				System.out.print(i + " ");
//			System.out.println();
			boolean contain = false;
			for (Tuple m : mfs) {
				if (this.containMFS(row, m)) {
//					System.out.println("contain");
					contain = true;
					break;
					// unCovered = this.rmCover(unCovered, coveringArray, row);
				}
			}
			if (contain) {
				unCovered = this.rmCover(unCovered, coveringArray, row);
//				System.out.println("rm " + unCovered);
			}
		}
		for (Tuple m : mfs)
			unCovered = this.setCover(unCovered, coveringArray, m);

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

	public static int  getC(int k, int m) {
		int zi = 1;
		for (int i = 0; i < m; i++)
			zi *= (k - i);

		for (int i = 0; i < m; i++)
			zi /= (m - i);

		return zi;
	}

	public int setCover(Integer unCovered, int[] coveringArray, Tuple tuple) {
		// System.out.println("setCover:¡¡"+ tuple.toString());
		if(tuple.getDegree() != dataCenter.degree)
			return unCovered;
		
		int num = getIndex(tuple);

		int thisIndex = dataCenter.index[num];

		for (int i = 0; i < tuple.getDegree(); i++) {

			int k = i + 1;
			int temR = tuple.getParamValue()[i];
			while (k < tuple.getDegree()) {
				temR *= dataCenter.param[tuple.getParamIndex()[k]];
				k++;
			}

			thisIndex += temR;
		}

		if (coveringArray[thisIndex] == 0)
			unCovered--;
		coveringArray[thisIndex] = coveringArray[thisIndex] + 1;

		return unCovered;
	}

	public  int getIndex(Tuple tuple) {
		int beforeIndex = 0;

		int num = 0;
		for (int i = 0; i < tuple.getDegree(); i++) {
			int theIndex = tuple.getParamIndex()[i];

			int curLeft = tuple.getDegree() - i - 1;

			for (int j = beforeIndex; j < theIndex; j++) {
				num += getC(dataCenter.param.length - j - 1, curLeft);
			}

			beforeIndex = theIndex + 1;
		}
		return num;
	}


	public static void main(String[] args) {
		DataCenter dataCenter = new DataCenter(new int[] { 2,2,2 }, 2);
		int[] test = { 1, 1, 1 };
		TestCaseImplement testCase = new TestCaseImplement(test);
		Tuple tuple = new Tuple(2, testCase);
		tuple.setParamIndex(new int[] { 0, 1 });
		CoveringManage cm = new CoveringManage(dataCenter);

		System.out.println(cm.getIndex(tuple));
	}

	public int setCover(Integer unCovered, int[] coveringArray, int[] row) {
		// TODO Auto-generated method stub
		myStack2 stack = new myStack2(dataCenter.degree);
		int i = 0;
		int currentPoint = 0;
		while (true) {
			if (stack.isFull()) {
				int thisIndex = dataCenter.index[i];
				for (int j = 0; j < dataCenter.degree; j++) {
					int k = j + 1;
					int temR = row[stack.dataIndexs[j]];
					while (k < dataCenter.degree) {
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
			} else if (currentPoint == dataCenter.param.length) {
				if (stack.isEmpty())
					break;
				stack.pop();
				currentPoint = stack.dataIndexs[stack.currentIndex] + 1;

			} else {
				stack.push(dataCenter.param[currentPoint], currentPoint);
				currentPoint++;
			}
		}
		return unCovered;
	}
}

class myStack2 {
	public int size;
	public int currentIndex;
	public int[] dataIndexs;
	public int[] data;

	public myStack2(int size) {
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
