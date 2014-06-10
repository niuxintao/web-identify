package maskSimulateExperiment;

import java.util.HashMap;
import java.util.Iterator;

import com.fc.testObject.TestCase;
import com.fc.tuple.Tuple;

public class BasicRunnerInMask {

	// Integer, means level and also indicate the fault. In practical,

	// we should be that : HashMap<Tuple, Fault> and HashMap<Fault, level>

	private HashMap<Tuple, Integer> bugModel;

	public BasicRunnerInMask(HashMap<Tuple, Integer> bugModel) {
		this.bugModel = bugModel;
	}

	public BasicRunnerInMask() {
		bugModel = new HashMap<Tuple, Integer>();
	}

	public void inject(Tuple bug, int level) {
		this.bugModel.put(bug, level);
	}

	public void setBugMode(HashMap<Tuple, Integer> bugModel) {
		this.bugModel = bugModel;
	}

	public int runTestCase(TestCase testCase) {
		// TODO Auto-generated method stub
		Iterator<Tuple> itr = bugModel.keySet().iterator();
		int maxLevel = 0;
		while (itr.hasNext()) {
			Tuple next = itr.next();
			if (testCase.containsOf(next)) {
				int level = bugModel.get(next);
				if (level > maxLevel)
					maxLevel = level;
			}
		}
		return maxLevel;

	}
}
