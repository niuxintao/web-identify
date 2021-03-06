package gandi;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import interaction.DataCenter;

import com.fc.caseRunner.CaseRunner;
import com.fc.testObject.TestCase;
import com.fc.testObject.TestCaseImplement;
import com.fc.tuple.Tuple;

import ct.AETG_Constraints;

//import com.fc.coveringArray.CoveringManage;
/**
 * the error can also getting back to the failing test cases
 * @author xintao
 *
 */
public class ErrorLocatingDrivenArray_TL extends ErrorLocatingDrivenArray {

	public ErrorLocatingDrivenArray_TL(DataCenter dataCenter,
			CaseRunner caseRunner) {
		super(dataCenter, caseRunner);
		// TODO Auto-generated constructor stub
	}
	
	public void run() {

		AETG_Constraints ac = new AETG_Constraints(dataCenter);

		// coverage is equal to 0 is ending
		while (ac.unCovered > 0) {
			long allTime = System.currentTimeMillis();
			long geTime = System.currentTimeMillis();

			int[] test = ac.getNextTestCase();

			geTime = System.currentTimeMillis() - geTime;
			this.timeGen += geTime;

			TestCase testCase = new TestCaseImplement(test);
			overallTestCases.add(testCase);
			regularCTCases.add(testCase);
			// System.out.println("aetg" + testCase.getStringOfTest() +
			// " "+ac.unCovered);

			if (caseRunner.runTestCase(testCase) == TestCase.PASSED) {
				ac.unCovered = cm.setCover(ac.unCovered, ac.coveredMark, test);
				
				Iterator<Tuple> it = MFS.iterator();
				while (it.hasNext()) {
					Tuple t = it.next();
					if (testCase.containsOf(t))
						it.remove();
				}
				
			} else {
				this.failTestCase.add(testCase);
				
				long ideTime = System.currentTimeMillis();

				List<Tuple> mfs = getMFS(ac, testCase);
				
				ideTime = System.currentTimeMillis() - ideTime;
				this.timeIden += ideTime;
				
				List<Tuple> doubleIdentified = new ArrayList<Tuple> ();
				for(Tuple newI: mfs){
					if(MFS.contains(newI)){
						doubleIdentified.add(newI);
					}
				}
				ac.addConstriants(doubleIdentified);
				this.MFS.addAll(mfs);
				// setCoverage(mfs);
			}

			allTime = System.currentTimeMillis() - allTime;
			this.timeAll += allTime;
		}

		this.coveredMark = ac.coveredMark;
	}

}
