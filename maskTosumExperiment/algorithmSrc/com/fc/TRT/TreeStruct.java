package com.fc.TRT;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
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
public class TreeStruct {
	public static final int RIGHT = 1;
	public static final int WRONG = -1;
	public static final int UNDERTEST = 0;

	private TestCase wrongCase;
	private TestSuite rightSuite;

	/**
	 * the class only give a sort of tuples, how to rank, is the business of
	 * alogrithm
	 */
	private LinkedHashSet<Tuple> untestedTuple;

	private HashSet<Tuple> wrongTuple;

	private Tuple root = null;

	public LinkedHashSet<Tuple> getUntestedTuple() {
		return untestedTuple;
	}

	public void setUntestedTuple(LinkedHashSet<Tuple> untestedTuple) {
		this.untestedTuple = untestedTuple;
	}

	/**
	 * use the runned testsuite as the input to inital the treeStruct
	 * 
	 * @param suite
	 */
	public TreeStruct(TestCase wrongCase, TestSuite rightSuite) {
		this.wrongCase = wrongCase;
		this.rightSuite = rightSuite;
		untestedTuple = new LinkedHashSet<Tuple>();
		wrongTuple = new HashSet<Tuple>();
	}

	/**
	 * construct the tree
	 */
	public void constructTree() {
		// first construct the root
		Tuple root = this.getRoot();
		this.untestedTuple.add(root);
		List<Tuple> children = root.getAllChilds();
		for (Tuple child : children) {
			this.untestedTuple.add(child);
		}
	}

	/**
	 * inital the wrong tuple and right tuple
	 */
	public void init() {
		Tuple root = this.getRoot();
		List<Tuple> children = root.getAllChilds();
		// the root wrong tuple
		this.wrongTuple.add(root);
		this.untestedTuple.remove(root);
		// init pass tuple
		for (Tuple child : children) {
			for (int i = 0; i < this.rightSuite.getTestCaseNum(); i++) {
				TestCase rightCase = rightSuite.getAt(i);
				if (rightCase.containsOf(child))
					this.untestedTuple.remove(child);
			}
		}

	}

	public void removeFoundBug(List<Tuple> bugs) {
		Tuple root = this.getRoot();
		List<Tuple> children = root.getAllChilds();
		for (Tuple child : children) {
			for (Tuple bug : bugs) {
				if (child.contains(bug) || child.equals(bug)) {
					this.wrongTuple.add(child);
					this.untestedTuple.remove(child);
				}
			}
		}
	}

	/**
	 * when all tuple is identified, then will give the minimal schema
	 * 
	 * @return
	 */
	public List<Tuple> getBugModes() {
		List<Tuple> bugs = new ArrayList<Tuple>();
		for (Tuple tuple : this.wrongTuple) {
			boolean isBug = true;
			List<Tuple> children = tuple.getAllChilds();
			// all its child must be right
			for (Tuple child : children) {
				if (this.wrongTuple.contains(child)) {
					isBug = false;
					break;
				}
			}
			if (isBug)
				bugs.add(tuple);
		}
		return bugs;
	}

	/**
	 * is there any tuple under test
	 * 
	 * @return
	 */
	public boolean isNodeUnderTest() {
		return this.untestedTuple.size() != 0;
	}

	/**
	 * get all the fathers of a tuple
	 * 
	 * @param tuple
	 * @return
	 */
	public List<Tuple> getAllFathers(Tuple tuple) {
		return tuple.getAllFathers();

	}

	/**
	 * get all the chidren of a tuple
	 * 
	 * @param tuple
	 * @return
	 */
	public List<Tuple> getAllchidren(Tuple tuple) {
		return tuple.getAllChilds();
	}

	/**
	 * set the node state: 0:underTest 1:pass -1:fail
	 * 
	 * @param tuple
	 * @param state
	 */
	public void setNodeState(Tuple tuple, int state) {
		if (state == TreeStruct.WRONG) {
			this.wrongTuple.add(tuple);
		}
		this.untestedTuple.remove(tuple);
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

}
