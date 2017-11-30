package getCovered;

import java.util.ArrayList;
import java.util.List;


/**
 * 
 * any tuple paramIndex is Ascending
 * 
 * @author XinTao Niu
 * 
 */
public class Tuple {

	private int[] paramIndex;
	private TestCase testCase;
	private int degree;

	@Override
	public String toString() {

		int[] data = new int[testCase.getLength()];
		for (int i = 0; i < testCase.getLength(); i++) {
			data[i] = -1;
		}
		for (int i = 0; i < degree; i++) {
			data[paramIndex[i]] = testCase.getAt(paramIndex[i]);
		}

		String rs = "";
		rs += "[";
		for (int i = 0; i < testCase.getLength(); i++) {
			if (data[i] == -1)
				rs += " - ";
			else
				rs += " " + data[i] + " ";
			if (i != testCase.getLength() - 1)
				rs += ",";
		}
		rs += "]";
		return rs;
	}

	public int getCaseLen() {
		return this.testCase.getLength();
	}

	public Tuple(int degree, TestCase testCase) {
		paramIndex = new int[degree];
		this.testCase = testCase;
		this.degree = degree;
	}

	@Override
	public boolean equals(Object tuple) {
		if (!(tuple instanceof Tuple))
			return false;

		if (this.degree == ((Tuple) tuple).getDegree()) {
			for (int i = 0; i < degree; i++) {
				if (paramIndex[i] != ((Tuple) tuple).getParamIndex()[i]
						|| this.getParamValue()[i] != ((Tuple) tuple)
								.getParamValue()[i])
					return false;
			}
			return true;
		} else
			return false;
	}

	@Override
	public int hashCode() {
		String string = "";
		for (int i : this.paramIndex) {
			string += i;
		}
		for (int i : this.getParamValue()) {
			string += i;
		}
		return string.hashCode();

	}

	public Tuple copy() {
		Tuple tuple = new Tuple(this.degree, this.testCase);
		for (int i = 0; i < this.degree; i++) {
			tuple.set(i, this.paramIndex[i]);
		}
		return tuple;
	}

	public void set(int location, int index) {
		paramIndex[location] = index;
	}

	public int[] getParamIndex() {
		return paramIndex;
	}

	public void setParamIndex(int[] paramIndex) {
		this.paramIndex = paramIndex;
	}

	public int[] getParamValue() {
		int[] paramValue = new int[this.degree];
		for (int i = 0; i < degree; i++) {
			paramValue[i] = this.testCase.getAt(this.paramIndex[i]);
		}
		return paramValue;
	}

	public void setDegree(int degree) {
		this.degree = degree;
	}

	public int getDegree() {
		return degree;
	}

	public boolean contains(Tuple tuple) {
		// TODO Auto-generated method stub
		if (this.degree < tuple.getDegree())
			return false;
		else {
			for (int i = 0; i < tuple.getDegree(); i++) {
				if (!hasElement(tuple, i)) {
					return false;
				}
			}
			return true;

		}
	}

	private boolean hasElement(Tuple tuple, int i) {
		for (int j = 0; j < this.degree; j++) {
			if (this.getParamIndex()[j] == tuple.getParamIndex()[i]
					&& this.getParamValue()[j] == tuple.getParamValue()[i]) {
				return true;
			}
		}
		return false;
	}

	public List<Tuple> getDirectTuples() {
		List<Tuple> result = new ArrayList<Tuple>();
		for (int i = 0; i < this.degree; i++) {
			Tuple temp = new Tuple(this.degree - 1, testCase);
			int k = 0;
			for (int j = 0; j < this.degree; j++) {
				if (j == i) {
					continue;
				} else {
					temp.set(k, this.getParamIndex()[j]);
					k++;
				}
			}
			result.add(temp);
		}
		return result;
	}

	public List<Tuple> getChildTuplesByDegree(int degree) {
		// TODO Auto-generated method stub
		List<Tuple> result = new ArrayList<Tuple>();
		CombineStack stack = new CombineStack(degree); // stack info

		if (degree == 0)
			return result;

		int currentIndex = 0; // current index need to push to the stack

		while (true) {
			if (stack.isFull()) {
				result.add(stack.generateTuple(this.paramIndex, this.testCase));
				stack.pop();
			} 
//			else if (currentIndex == this.degree) {
//				if (stack.isEmpty()){
//					System.out.println("end1");
//					break;
//				}
//				currentIndex = stack.pop() + 1;
//				System.out.println("pop and plus 1");
//			} 
			else {
				if(stack.isEmpty() && degree > this.degree
						- currentIndex){
					break;
				}
				else if (degree > stack.stackSize + this.degree
						- currentIndex){
					currentIndex = stack.pop() + 1;
				}
				else {
					stack.push(currentIndex);
					currentIndex++;
				}
			}
		}

		return result;
	}

	public List<Tuple> getFatherTuplesByDegree(int degree) {
		List<Tuple> result = new ArrayList<Tuple>();
		// get the reverse tuple of the current tuple
		Tuple reverseTuple = this.getReverseTuple();
		// get the degree - current_tuple_degree child tuple of the reverse
		// tuple
		List<Tuple> reverseChildren = reverseTuple
				.getChildTuplesByDegree(degree - this.degree);
		// cat the getted child tuple and the current tuple
		for (Tuple reverseChild : reverseChildren) {
			result.add(this.cat(this, reverseChild));
		}
		return result;
	}

	public List<Tuple> getAllChilds() {
		List<Tuple> result = new ArrayList<Tuple>();
		// get the degree little than the orignal tuple
		for (int i = this.degree - 1; i > 0; i--) {
			result.addAll(this.getChildTuplesByDegree(i));
		}
		return result;

	}

	public List<Tuple> getAllFathers() {
		List<Tuple> result = new ArrayList<Tuple>();
		// get the degree little than the orignal tuple
		for (int i = this.degree + 1; i <= testCase.getLength(); i++) {
			result.addAll(this.getFatherTuplesByDegree(i));
		}
		return result;

	}

	public Tuple getReverseTuple() {
		Tuple result = new Tuple(testCase.getLength() - this.degree, testCase);
		int currentIndex = 0;
		int tupleIndex = 0;
		for (int i = 0; i < testCase.getLength(); i++) {
			// filter the element in the tuple
			if (tupleIndex == this.degree
					|| i != this.getParamIndex()[tupleIndex]) {
				result.set(currentIndex, i);
				currentIndex++;
			} else {
				tupleIndex++;
			}
		}
		return result;
	}

	public Tuple cat(Tuple A, Tuple B) {
		Tuple result = new Tuple(A.degree + B.degree, A.testCase);
		int Aindex = 0;
		int Bindex = 0;
		for (int i = 0; i < result.degree; i++) {
			if (Aindex == A.degree) {
				result.set(i, B.paramIndex[Bindex]);
				Bindex++;
				continue;
			}
			if (Bindex == B.degree) {
				result.set(i, A.paramIndex[Aindex]);
				Aindex++;
				continue;
			}
			if (A.paramIndex[Aindex] > B.paramIndex[Bindex]) {
				result.set(i, B.paramIndex[Bindex]);
				Bindex++;
			} else {
				result.set(i, A.paramIndex[Aindex]);
				Aindex++;
			}
		}
		return result;
	}

	public Tuple catComm(Tuple A, Tuple B) {
		int Aindex = 0;
		int Bindex = 0;
		int size = 0;
		int[] temp = new int[A.degree + B.degree];

		while (Aindex < A.degree || Bindex < B.degree) {
			if (Aindex == A.degree) {
				temp[size] = B.paramIndex[Bindex];
				Bindex++;
			} else if (Bindex == B.degree) {
				temp[size] = A.paramIndex[Aindex];
				Aindex++;
			} else if (A.paramIndex[Aindex] > B.paramIndex[Bindex]) {
				temp[size] = B.paramIndex[Bindex];
				Bindex++;
			} else if (A.paramIndex[Aindex] < B.paramIndex[Bindex]) {
				temp[size] = A.paramIndex[Aindex];
				Aindex++;
			} else if (A.paramIndex[Aindex] == B.paramIndex[Bindex]) {
				temp[size] = A.paramIndex[Aindex];
				Aindex++;
				Bindex++;
			}
			size++;
		}
		Tuple result = new Tuple(size, A.testCase);
		System.arraycopy(temp, 0, result.paramIndex, 0, size);

		return result;
	}
}

class CombineStack {
	int stackSize; // stack current index,or size
	int maxSize; // stack max size
	int[] stack; // the stack

	public CombineStack(int degree) {
		this.stackSize = 0;
		this.maxSize = degree;
		stack = new int[maxSize];
		// this.tupletemporary = new Tuple(degree, testCase);
	}

	public boolean isFull() {
		return (stackSize == maxSize);
	}

	public boolean isEmpty() {
		return (stackSize == 0);
	}

	public void push(int value) {
		if (!isFull()) {
			stack[stackSize] = value;
			stackSize++;
		}
	}

	public int pop() {
		if (!isEmpty())
			stackSize--;
		return stack[stackSize]; // return the pop value
	}

	public Tuple generateTuple(int[] indexVaule, TestCase testCase) {
		Tuple result = new Tuple(maxSize, testCase);
		for (int i = 0; i < maxSize; i++) {
			result.set(i, indexVaule[stack[i]]);
		}
		return result;
	}

}