package com.fc.forbiddnen;

import java.util.ArrayList;
import java.util.List;

import com.fc.testObject.TestCase;
import com.fc.testObject.TestCaseImplement;
import com.fc.tuple.Tuple;

public class GetAlltheForbidden {

	// get then shorten, evey time.

	public List<Tuple> addAndShorten(List<Tuple> tuples, Tuple needAdded) {
		List<Tuple> newArray = new ArrayList<Tuple>();
		for (Tuple tu : tuples) {
			if (!tu.contains(needAdded))
				newArray.add(tu);
		}
		boolean added = true;
		for (Tuple tu : newArray) {
			if (needAdded.contains(tu)) {
				added = false;
				break;
			}
		}
		
//		System.out.println(added);
		if (added)
			newArray.add(needAdded);

		return newArray;
	}

	public int getValue(Tuple tuple, int index) {
		for (int i = 0; i < tuple.getDegree(); i++) {
			if (tuple.getParamIndex()[i] == index)
				return tuple.getParamValue()[i];
		}
		return -1;
	}

	/**
	 * 
	 * generate the following types:
	 * 
	 * 
	 * index 0 : value 1 (1, 1 , -), (1,-, 2 ) value 2 (2, -, 1), (2, -, 3)
	 * value 3 (3, -, -)
	 * 
	 * index 1 : value 1 (-, 1 , 2), (1, 1, - ) value 2 (-, 2, 1), (2, 2, -)
	 * value 3 (-, 3, -)
	 * 
	 * then for each index, for each possible combiantion, (they must be
	 * compatitable) :
	 * 
	 * for example, index 0 can be (1, 1 , -) (2, -, 1), and (3, -, -), Or (1, 1
	 * , -), (2, -, 3), (3, -, -), but cannot be (1, -, 2), (2, -, 3), (3, -, -)
	 * 
	 * @param tuples
	 * @param index
	 * @param param
	 * @return
	 */
	public List<List<Tuple>> getIndex(List<Tuple> tuples, int index, int[] param) {
		List<List<Tuple>> result = new ArrayList<List<Tuple>>();
		for (int i = 0; i < param[index]; i++) {
			List<Tuple> tus = new ArrayList<Tuple>();
			result.add(tus);
		}
		for (Tuple tuple : tuples) {
			int value = getValue(tuple, index);
			if (value != -1) {
				result.get(value).add(tuple);
			}
		}
		
//		System.out.println("index "+ index);
//		for(List<Tuple> tt : result){
//			System.out.println("v");
//			for(Tuple t: tt){
//				System.out.println(t.toString());
//			}
//		}

		return result;
	}

	public boolean isCompatiable(Tuple A, Tuple B) {

		for (int i = 0; i < A.getDegree(); i++) {
			for (int j = 0; j < B.getDegree(); j++) {
				if (B.getParamIndex()[j] == A.getParamIndex()[i]) {
					if (B.getParamValue()[j] != A.getParamValue()[i])
						return false;
				}
			}
		}

		return true;
	}

	public boolean isCompatiable(Tuple A, List<Tuple> list) {
		for (Tuple B : list)
			if (!this.isCompatiable(A, B))
				return false;
		return true;
	}
	
//	public Tuple merge(Tuple A, Tuple B){
//		
//	}

	public Tuple merge(List<Tuple> tuples) {
		
//		System.out.println("merge");
//		for(Tuple t : tuples){
//			System.out.println(t);
//		}
//		System.out.println("");
		
		Tuple result = tuples.get(0);
		for (int i = 1; i < tuples.size(); i++) {
			if (result == null || tuples.get(i) == null)
				return null;
			result = result.catComm2(result, tuples.get(i));
		}
		
//		System.out.println("r" + result.toString());
		return result;
	}
	
	public List<Tuple> get(int index, List<Tuple> temp, List<List<Tuple>> tupleindex, int location){
		List<Tuple> result = new ArrayList<Tuple> ();
		
		
		List<Tuple> listTuple = tupleindex.get(location);
		if(listTuple == null || listTuple.size() == 0)
			return result;
		
		//last one

		
		for(Tuple tuple: listTuple){
//			System.out.println("this " + tuple.toString() + "location " + location);
			Tuple tcopy = tuple.copy();
			tcopy.setDegree(tuple.getDegree() - 1);
			int[] indextamp = new int[tuple.getDegree() - 1];
			int tm = 0;
			for(int k = 0; k < tuple.getDegree(); k++)
				if(tuple.getParamIndex()[k] == index)
					continue;
				else{
					indextamp[tm] =  tuple.getParamIndex()[k];
					tm ++;
				}
			tcopy.setParamIndex(indextamp);
			
				
			if(!isCompatiable(tcopy, temp))
				continue;
			List<Tuple> tttemp = new ArrayList<Tuple> ();
			for(Tuple tp : temp){
				tttemp.add(tp);
			}
			tttemp.add(tcopy);
			
			if(location == tupleindex.size() - 1){
				Tuple mr  = merge(tttemp);
				//List<Tuple> result = new ArrayList<Tuple> ();
				result.add(mr);
				//return result;
			}else{
				result.addAll(get(index, tttemp, tupleindex, location + 1));
			}
		}
		
		
		return result;
		
		
	}

	public List<Tuple> getAppendingTuples(List<Tuple> currentForbidden,
			int[] param, int index) {
		List<List<Tuple>> tupleindex = this.getIndex(currentForbidden, index,
				param);
		
		List<Tuple> temp = new ArrayList<Tuple> ();
		List<Tuple> appended = this.get(index, temp, tupleindex, 0);
		return appended;
	}
	
	public List<Tuple> getAndAddAppendingTuples(List<Tuple> currentForbidden,
			int[] param, int index) {
		List<Tuple> result = currentForbidden;

		// get the needed appened tuples (implict tuples)
		List<Tuple> neededAdded = getAppendingTuples(currentForbidden, param,
				index);
		
//		System.out.println("need to added");
//		for(Tuple t : neededAdded  )
//			System.out.println(t.toString());
//		System.out.println();
		// shorten and add
		if (neededAdded != null) {
			for (Tuple tuple : neededAdded)
				result = this.addAndShorten(result, tuple);
		}
//		
//		for(Tuple t : result)
//			System.out.println(t);
//		System.out.println("assss");
		return result;
	}

	public List<Tuple> getAlltheForbidden(List<Tuple> currentForbidden,
			int[] param) {

		List<Tuple> result = currentForbidden;
		for (int index = 0; index < param.length; index++) {
//			System.out.println("index" + index);
			result = getAndAddAppendingTuples(result, param, index);
		}

		return result;
	}
	
	public static void main(String[] args){
		int[] param = new int[4];
		for(int i = 0; i < param.length; i++)
			param[i] = 3;
		
		
		int[] test = new int[4];
		for(int i = 0; i  <  4; i++)
			test[i] = 0;
		TestCase te = new TestCaseImplement(test);
		Tuple tuple1 = new Tuple(2,te);
		tuple1.set(0, 0);
		tuple1.set(1, 2);  // (0, -, 0, -)
		
		test = new int[4];
		for(int i = 0; i  <  4; i++)
			test[i] = 1;

		te = new TestCaseImplement(test);
		Tuple tuple2 = new Tuple(2,te);
		tuple2.set(0, 0);
		tuple2.set(1, 2);  // (1, -, 1, -)
		
		
		test = new int[4];
		for(int i = 0; i  <  4; i++)
			test[i] = 1;
		
		test[2] = 0;
		test[3] = 0;  //(1, 1, 0, 0)
		te = new TestCaseImplement(test);
		Tuple tuple3 = new Tuple(2,te);
		tuple3.set(0, 0);
		tuple3.set(1, 1);  // (1, 1, -, -)
		
		
		test = new int[4];
		for(int i = 0; i  <  4; i++)
			test[i] = 0;
	
		te = new TestCaseImplement(test);
		Tuple tuple4 = new Tuple(2,te);
		tuple4.set(0, 0);
		tuple4.set(1, 3);  // (0, -, -, 0)
		
		
		test = new int[4];
		for(int i = 0; i  <  4; i++)
			test[i] = 2;
	
		te = new TestCaseImplement(test);
		Tuple tuple5 = new Tuple(2,te);
		tuple5.set(0, 0);
		tuple5.set(1, 3);  // (2, -, -, 2)
		
		
		
		List<Tuple> tuples = new ArrayList<Tuple> ();
		tuples.add(tuple1);
		tuples.add(tuple2);
		tuples.add(tuple3);
		tuples.add(tuple4);
		tuples.add(tuple5);
		
		
		GetAlltheForbidden gaf = new GetAlltheForbidden();
		
		List<Tuple> allMFS = gaf.getAlltheForbidden(tuples, param);
		
		for(Tuple t : allMFS){
			System.out.println(t.toString());
		}
		
//		tup
	}

}
