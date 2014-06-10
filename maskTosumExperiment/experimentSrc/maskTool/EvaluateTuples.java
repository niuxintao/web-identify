package maskTool;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import com.fc.testObject.TestCase;
import com.fc.testObject.TestCaseImplement;
import com.fc.tuple.Tuple;

public class EvaluateTuples {
	private HashSet<Tuple> fatherTuples;
	private HashSet<Tuple> ChildTuples;
	private HashSet<Tuple> missTuples;
	private HashSet<Tuple> redundantTuples;
	private HashSet<Tuple> accurateTuples;

	public HashSet<Tuple> getFatherTuples() {
		return fatherTuples;
	}

	public HashSet<Tuple> getChildTuples() {
		return ChildTuples;
	}

	public HashSet<Tuple> getMissTuples() {
		return missTuples;
	}

	public HashSet<Tuple> getRedundantTuples() {
		return redundantTuples;
	}

	public HashSet<Tuple> getAccurateTuples() {
		return accurateTuples;
	}

	public EvaluateTuples() {
		init();
	}

	private void init() {
		fatherTuples = new HashSet<Tuple>();
		ChildTuples = new HashSet<Tuple>();
		missTuples = new HashSet<Tuple>();
		redundantTuples = new HashSet<Tuple>();
		accurateTuples = new HashSet<Tuple>();
	}

	public void evaluate(List<Tuple> bench, List<Tuple> challenger) {
		init();
		for (Tuple ben : bench) {
			Iterator<Tuple> iter = challenger.iterator();
			boolean hasRelated = false;
			while (iter.hasNext()) {
				Tuple cha = iter.next();
				if (ben.contains(cha)) {
					if (cha.contains(ben)) {
						this.accurateTuples.add(cha);
						iter.remove();
					} else {
						this.ChildTuples.add(cha);
						// iter.remove();
					}
					hasRelated = true;
				} else if (cha.contains(ben)) {
					this.fatherTuples.add(cha);
					// iter.remove();
					hasRelated = true;
				}
			}
			if (hasRelated == false)
				this.missTuples.add(ben);
		}

		Iterator<Tuple> iter = challenger.iterator();
		while (iter.hasNext()) {
			Tuple cha = iter.next();
			boolean find = false;
			// for (Tuple tuple : this.accurateTuples)
			// if (cha.equals(tuple)) {
			// iter.remove();
			// break;
			// }

			for (Tuple tuple : this.ChildTuples)
				if (cha.equals(tuple)) {
					iter.remove();
					find = true;
					break;
				}
			
			if (find == true)
				continue;
			
			for (Tuple tuple : this.fatherTuples)
				if (cha.equals(tuple)) {
					iter.remove();
					break;
				}
		}

		for (Tuple tuple : challenger)
			this.redundantTuples.add(tuple);
	}
	
	
	public static void main(String[] argv){
		List<Tuple> bench = new ArrayList<Tuple>();
		List<Tuple> challenger = new ArrayList<Tuple>();
		

		int[] wrong = new int[] { 1, 1, 1, 1, 1, 1, 1, 1 };

		TestCase wrongCase = new TestCaseImplement();
		wrongCase.setTestState(TestCase.FAILED);
		((TestCaseImplement) wrongCase).setTestCase(wrong);

		Tuple bug1 = new Tuple(2, wrongCase);
		bug1.set(0, 0);
		bug1.set(1, 1);
		
		Tuple bug4 = new Tuple(2, wrongCase);
		bug4.set(0, 3);
		bug4.set(1, 4);
		
		Tuple bug5 = new Tuple(2, wrongCase);
		bug5.set(0, 3);
		bug5.set(1, 5);
		
		Tuple bug6= new Tuple(2, wrongCase);
		bug6.set(0, 3);
		bug6.set(1, 6);
		
		
		Tuple bug9= new Tuple(2, wrongCase);
		bug9.set(0, 6);
		bug9.set(1, 7);
		
		
		Tuple bug10= new Tuple(2, wrongCase);
		bug10.set(0, 5);
		bug10.set(1, 7);
		
		bench.add(bug1);
		bench.add(bug4);
		bench.add(bug5);
		bench.add(bug6);
		bench.add(bug9);
		bench.add(bug10);
		
		
		Tuple bug3 = new Tuple(3, wrongCase);
		bug3.set(0, 0);
		bug3.set(1, 1);
		bug3.set(2, 2);
		
		
		Tuple bug8 = new Tuple(3, wrongCase);
		bug8.set(0, 0);
		bug8.set(1, 1);
		bug8.set(2, 5);


		Tuple bug2 = new Tuple(1, wrongCase);
		bug2.set(0, 3);
		
		Tuple bug7= new Tuple(2, wrongCase);
		bug7.set(0, 4);
		bug7.set(1, 6);
		
		Tuple bug11= new Tuple(2, wrongCase);
		bug11.set(0, 5);
		bug11.set(1, 7);
		
		challenger.add(bug7);
		challenger.add(bug8);
		challenger.add(bug2);
		challenger.add(bug3);
		challenger.add(bug11);
		
		
		EvaluateTuples eva = new EvaluateTuples();
		eva.evaluate(bench, challenger);
		
		System.out.println("accuate");
		for(Tuple tuple : eva.accurateTuples)
			System.out.println(tuple.toString());
		System.out.println("child");
		for(Tuple tuple : eva.ChildTuples)
			System.out.println(tuple.toString());
		System.out.println("father");
		for(Tuple tuple : eva.fatherTuples)
			System.out.println(tuple.toString());
		System.out.println("miss");
		for(Tuple tuple : eva.missTuples)
			System.out.println(tuple.toString());
		System.out.println("reduntent");
		for(Tuple tuple : eva.redundantTuples)
			System.out.println(tuple.toString());
		
	}

}
