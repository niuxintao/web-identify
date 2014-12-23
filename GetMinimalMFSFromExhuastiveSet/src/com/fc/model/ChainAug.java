package com.fc.model;

import java.util.List;

import com.fc.tuple.CorpTupleWithTestCase;
import com.fc.tuple.Tuple;

public class ChainAug extends CharacterWM {
	private List<Tuple> currentChain;
	private Tuple currCandidate;
	private int head;
	private int tail;
	private int middle;

	public ChainAug(TuplePool pool, CorpTupleWithTestCase generate) {
		super(pool, generate);
		// TODO Auto-generated constructor stub
		reset();

	}

	@Override
	protected void inital() {
		reset();
	}

	// first add the
	@Override
	protected void extraDealAfterBug(Tuple tuple) {
		head = middle + 1;
		if (head > tail) {
			this.pool.getExistedBugTuples().add(tuple);
			this.pool.compress(this.pool.getExistedBugTuples());
			int last = middle + 1;
			if (last < this.currentChain.size()) {
				this.pool.getExistedRightTuples().add(currentChain.get(last));
				this.pool.compress_r(this.pool.getExistedRightTuples());
			}
			this.currCandidate = tuple;
			suc(currCandidate);
		} else
			middle = (head + tail) / 2;
	}

	@Override
	protected void extraDealAfterRight(Tuple tuple) {
		tail = middle - 1;
		if (tail < head) {
			this.pool.getExistedRightTuples().add(tuple);
			this.pool.compress_r(this.pool.getExistedRightTuples());
			int last = middle - 1;
			if (last >= 0) {
				this.pool.getExistedBugTuples().add(currentChain.get(last));
				this.pool.compress(this.pool.getExistedBugTuples());

				this.currCandidate = currentChain.get(last);
				suc(currCandidate);
			} else {
				suc(currCandidate);
			}
		} else
			middle = (head + tail) / 2;
	}

	public void suc(Tuple candidateBug) {
		if (candidateBug == null)
			this.reset();
		else {
			//System.out.println("sc");
			this.currentChain = this.pool.getPaths(candidateBug);
			//System.out.println("scend");
			this.head = 0;
			this.middle = 0;
			if (currentChain != null)
				this.tail = currentChain.size() - 1;
			else
				this.reset();
		}
	}

	@Override
	protected Tuple seletctTupleUnderTest() {
		if (currentChain == null)
			return null;
		//System.out.println(currentChain.get(middle).toString());
		return currentChain.get(middle);
	}

	public void reset() {
		this.currCandidate = null;
		//System.out.println("start");
		this.currentChain = pool.getLongestPath();
		//System.out.println("end");
		this.head = 0;
		this.middle = 0;
		if (currentChain != null)
			this.tail = currentChain.size() - 1;
		else
			this.tail = -1;
	}
}
