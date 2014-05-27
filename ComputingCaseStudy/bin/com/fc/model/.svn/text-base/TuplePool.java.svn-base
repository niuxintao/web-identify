package com.fc.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.fc.testObject.TestCase;
import com.fc.testObject.TestSuite;
import com.fc.tuple.Tuple;

/**
 * A class that organize all the tuple(in fact,tuples are organized theirself)
 * and record all tuples info in a wrong test case
 * 
 * @author Xintao Niu
 * 
 */
public class TuplePool {
	public static final int RIGHT = 1;
	public static final int WRONG = -1;
	public static final int UNDERTEST = 0;

	private TestCase wrongCase;
	private TestSuite rightSuite;

	private List<Tuple> existedBugTuples;
	private List<Tuple> existedRightTuples;

	public boolean isMinimal(Tuple tuple, List<Tuple> tuples) {
		boolean result = true;
		for (Tuple cu : tuples) {
			if (tuple == cu)
				continue;
			if (tuple.contains(cu)) {
				result = false;
				break;
			}
		}
		return result;
	}

	public void compress(List<Tuple> tuples) {
		Iterator<Tuple> iter = tuples.iterator();
		while (iter.hasNext()) {
			Tuple tuple = iter.next();
			if (!isMinimal(tuple, tuples)) {
				iter.remove();
			}
		}
	}

	public boolean isMaximal(Tuple tuple, List<Tuple> tuples) {
		boolean result = true;
		for (Tuple cu : tuples) {
			if (tuple == cu)
				continue;
			if (cu.contains(tuple)) {
				result = false;
				break;
			}
		}
		return result;
	}

	public void compress_r(List<Tuple> tuples) {
		Iterator<Tuple> iter = tuples.iterator();
		while (iter.hasNext()) {
			Tuple tuple = iter.next();
			if (!isMaximal(tuple, tuples)) {
				iter.remove();
			}
		}
	}

	public void getItr(Tuple tuple, List<Tuple> lastItr, List<Tuple> thisItr) {
		List<Tuple> childs = tuple.getChildTuplesByDegree(1);
		for (Tuple child : childs) {
			if (lastItr.isEmpty()) {
				thisItr.add(child);
			} else {
				for (Tuple last : lastItr) {
					Tuple catC = child.catComm(child, last);
					thisItr.add(catC);
				}
			}
		}
	}

	public List<Tuple> getCandidateBugHeads() {
		List<Tuple> result = new ArrayList<Tuple>();
		List<Tuple> lastItr = new ArrayList<Tuple>();
		for (Tuple bug : existedBugTuples) {
			List<Tuple> thisItr = new ArrayList<Tuple>();
			getItrp(bug, lastItr, thisItr);
			//compress(thisItr);
			lastItr = thisItr;
		}
		for (Tuple tuple : lastItr) {
			Tuple reverse = tuple.getReverseTuple();
			result.add(reverse);
		}
		return result;
	}

	public List<Tuple> getCandidateRightTails() {
		List<Tuple> result = new ArrayList<Tuple>();
		List<Tuple> lastItr = new ArrayList<Tuple>();
		for (Tuple right : existedRightTuples) {
			List<Tuple> thisItr = new ArrayList<Tuple>();
			Tuple reverse = right.getReverseTuple();
			getItrp(reverse, lastItr, thisItr);
		//	System.out.println(thisItr.size());
		//	compress(thisItr);
		//	System.out.println(thisItr.size());
			lastItr = thisItr;
		}
		for (Tuple tuple : lastItr) {
			result.add(tuple);
		}
		return result;
	}


	public void getItrp(Tuple tuple, List<Tuple> lastItr, List<Tuple> thisItr) {
		List<Tuple> childs = tuple.getChildTuplesByDegree(1);
		if (lastItr.isEmpty())
			for (Tuple child : childs) {
				thisItr.add(child);
			}
		else {
			Iterator<Tuple> ich = childs.iterator();
			while (ich.hasNext()) {
				Tuple t = ich.next();
				int index = lastItr.lastIndexOf(t);
				if (index != -1) {
					thisItr.add(t);
					ich.remove();
					lastItr.remove(index);
				}
			}
			for (Tuple child : childs) {
				for (Tuple last : lastItr) {
					Tuple catC = child.catComm(child, last);
					thisItr.add(catC);
				}
			}
		}
	}

	public List<Tuple> getLongestPath(Tuple head, Tuple tail) {
		if (head == null || tail == null)
			return null;

		List<Tuple> result = new ArrayList<Tuple>();
		int headSize = head.getDegree();
		int tailSize = tail.getDegree();
		int size = headSize;
		while (size >= tailSize) {
			Tuple tuple = new Tuple(size, wrongCase);

			int shSize = 0;
			int Aindex = 0;
			int Bindex = 0;
			int i;
			for (i = 0; i < size; i++) {
				if (shSize == size - tailSize) {
					tuple.getParamIndex()[i] = tail.getParamIndex()[Bindex];
					Bindex++;
					continue;
				}
				if (Bindex == tailSize) {
					tuple.getParamIndex()[i] = head.getParamIndex()[Aindex];
					Aindex++;
					shSize++;
					continue;
				}
				if (head.getParamIndex()[Aindex] == tail.getParamIndex()[Bindex]) {
					tuple.getParamIndex()[i] = tail.getParamIndex()[Bindex];
					Bindex++;
					Aindex++;
				} else {
					tuple.getParamIndex()[i] = head.getParamIndex()[Aindex];
					Aindex++;
					shSize++;
				}
			}
			result.add(tuple);
			size--;
		}
		return result;

	}

	int lenBetween(Tuple A, Tuple B) {
		if (!A.contains(B))
			return -1;
		int size = A.getDegree() - B.getDegree();
		return size;
	}

	public List<Tuple> getLongestPath() {
		List<Tuple> result = new ArrayList<Tuple>();
		List<Tuple> heads = this.getCandidateBugHeads();
		List<Tuple> tails = this.getCandidateRightTails();
		Tuple canHead = null;
		Tuple canTail = null;
		int max = -1;
		for (Tuple head : heads) {
			for (Tuple tail : tails) {
				int size = lenBetween(head, tail);
				if (size > max) {
					max = size;
					canHead = head;
					canTail = tail;
				}
			}
		}
		result = this.getLongestPath(canHead, canTail);
		return result;

	}

	private Tuple root = null;

	/**
	 * use the runned testsuite as the input to inital the treeStruct
	 * 
	 * @param suite
	 */
	public TuplePool(TestCase wrongCase, TestSuite rightSuite) {
		this.wrongCase = wrongCase;
		this.setRightSuite(rightSuite);
		existedBugTuples = new ArrayList<Tuple>();
		existedRightTuples = new ArrayList<Tuple>();
		existedBugTuples.add(getRoot());
		existedRightTuples.add(new Tuple(0, wrongCase));
	}

	/*
	 * public int getTupleState(Tuple tuple) { // TODO Auto-generated method
	 * stub return this.tree.get(tuple).getState(); }
	 */

	public Tuple getRoot() {
		if (this.root == null) {
			root = new Tuple(this.wrongCase.getLength(), this.wrongCase);
			for (int i = 0; i < root.getDegree(); i++) {
				root.set(i, i);
			}
		}
		return root;
	}

	public List<Tuple> getPaths(Tuple candidateBug) {
		List<Tuple> result = new ArrayList<Tuple>();
		List<Tuple> heads = this.getCandidateBugHeads();
		List<Tuple> childs = candidateBug.getChildTuplesByDegree(candidateBug
				.getDegree() - 1);
		List<Tuple> candidateHeads = new ArrayList<Tuple>();
		for (Tuple child : childs) {
			boolean containFlag = false;
			for (Tuple bug : heads) {
				if (bug.contains(child) || bug.equals(child)) {
					containFlag = true;
					break;
				}
			}
			if (containFlag) {
				candidateHeads.add(child);
			}
		}
		List<Tuple> tails = this.getCandidateRightTails();

		Tuple canHead = null;
		Tuple canTail = null;
		int max = -1;
		for (Tuple head : candidateHeads) {
			for (Tuple tail : tails) {
				int size = lenBetween(head, tail);
				if (size > max) {
					max = size;
					canHead = head;
					canTail = tail;
				}
			}
		}
		result = this.getLongestPath(canHead, canTail);
		return result;

	}

	public List<Tuple> getExistedBugTuples() {
		return existedBugTuples;
	}

	public void setExistedBugTuples(List<Tuple> existedBugTuples) {
		this.existedBugTuples = existedBugTuples;
	}

	public List<Tuple> getExistedRightTuples() {
		return existedRightTuples;
	}

	public void setExistedRightTuples(List<Tuple> existedRightTuples) {
		this.existedRightTuples = existedRightTuples;
	}

	public TestSuite getRightSuite() {
		return rightSuite;
	}

	public void setRightSuite(TestSuite rightSuite) {
		this.rightSuite = rightSuite;
	}

}
