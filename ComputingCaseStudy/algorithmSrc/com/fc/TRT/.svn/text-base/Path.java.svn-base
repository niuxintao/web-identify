package com.fc.TRT;

import java.util.ArrayList;
import java.util.List;

import com.fc.tuple.Tuple;

public class Path extends CharacterWorkMachine {

	private List<Tuple> currentPaths;
	private int head;
	private int tail;
	private int middle;

	private int[] maxLenRecord;

	public Path(TreeStruct tree, CaseRunner runner,
			CorpTupleWithTestCase generate) {
		super(tree, runner, generate);
		// TODO Auto-generated constructor stub
		int allNum = 1;
		allNum = allNum << this.tree.getRoot().getCaseLen();
		maxLenRecord = new int[allNum];
	}

	@Override
	protected void inital() {
		maxLenRecord[CorpTupleWithNum.getIndex(this.tree.getRoot())] = 0;
		reset();
	}

	@Override
	protected void extraDealAfterBug(Tuple tuple) {
		head = middle + 1;
		if (head > tail)
			this.reset();
		else
			middle = (head + tail) / 2;
	}

	@Override
	protected void extraDealAfterRight(Tuple tuple) {
		tail = middle - 1;
		if (tail < head)
			this.reset();
		else
			middle = (head + tail) / 2;
	}

	@Override
	protected Tuple seletctTupleUnderTest() {
		return currentPaths.get(middle);
	}

	public void reset() {
		this.resetMaxLengthRecord();
		this.currentPaths = this.getMaxLengthPath();
		this.head = 0;
		this.middle = 0;
		this.tail = currentPaths.size() - 1;
	}

	public void resetMaxLengthRecord() {
		Tuple root = this.tree.getRoot();
		for (int degree = root.getDegree() - 1; degree > 0; degree--) {
			List<Tuple> children = root.getChildTuplesByDegree(degree);
			for (Tuple child : children) {
				int maxLength = this.getMaxLength(child);
				maxLenRecord[CorpTupleWithNum.getIndex(child)] = maxLength;
			}
		}
	}

	private int getMaxLength(Tuple tuple) {
		int result = 0;
		Tuple maxFather = getMaxFather(tuple);
		if (maxFather != null)
			result = maxLenRecord[CorpTupleWithNum.getIndex(maxFather)];
		if (this.tree.getUntestedTuple().contains(tuple))
			result += 1;
		return result;
	}

	private Tuple getMaxFather(Tuple tuple) {
		int degree = tuple.getDegree();
		if (degree == this.tree.getRoot().getDegree())
			return null;
		degree += 1;
		List<Tuple> fathers = tuple.getFatherTuplesByDegree(degree);
		return getMaxlenTuple(fathers);
	}

	private Tuple getMaxlenTuple(List<Tuple> tuples) {
		Tuple maxTuple = null;
		int maxNum = -1;
		for (Tuple father : tuples) {
			int current =maxLenRecord[CorpTupleWithNum.getIndex(father)];
			if (current > maxNum) {
				maxTuple = father;
				maxNum = current;
			}
		}
		return maxTuple;
	}

	private List<Tuple> getMaxPath(Tuple tuple) {
		List<Tuple> result = new ArrayList<Tuple>();
		Tuple maxFather = this.getMaxFather(tuple);
		if (maxFather != null) {
			result.addAll(this.getMaxPath(maxFather));
		}

		if (this.tree.getUntestedTuple().contains(tuple)) {
			result.add(tuple);
		}
		return result;
	}

	public List<Tuple> getMaxLengthPath() {
		Tuple root = this.tree.getRoot();
		List<Tuple> leaves = root.getChildTuplesByDegree(1);
		Tuple maxLeaf = getMaxlenTuple(leaves);
		List<Tuple> maxPath = getMaxPath(maxLeaf);
		return maxPath;
	}
}
