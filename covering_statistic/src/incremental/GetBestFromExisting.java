package incremental;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.fc.DataCenter.DataCenter;
import com.fc.DataCenter.LongBitSet;
import com.fc.process.BasicAnalyse;
import com.fc.testObject.TestCase;
import com.fc.tuple.Tuple;

public class GetBestFromExisting {

	// 原来的
	private List<TestCase> original;
	
	private List<TestCase> originalTemp;

	// extracting的
	private List<TestCase> selected;

	private List<Tuple> lastUpdated;

	private HashMap<TestCase, Integer> coveredNum;

	private DataCenter dataCenter;
	private LongBitSet coveringArray;

	private BasicAnalyse ba;

	public GetBestFromExisting(DataCenter dataCenter, List<TestCase> original) {
		this.dataCenter = dataCenter;
		this.original = original;

		selected = new ArrayList<TestCase>();
		
		originalTemp = new ArrayList<TestCase>();
		for(TestCase t: original){
			originalTemp.add(t);
		}
		
		
		lastUpdated = new ArrayList<Tuple>();
		ba = new BasicAnalyse();

		coveringArray = new LongBitSet(dataCenter.getCoveringArrayNum());

		coveredNum = new HashMap<TestCase, Integer>();
		int value = dataCenter.getIndex().length;
		for (TestCase testCase : original) {
			coveredNum.put(testCase, value);
		}
		
		init();
	}
	
	
	public List<TestCase> getSelected() {
		return selected;
	}


	public DataCenter getDataCenter() {
		return dataCenter;
	}


	//add the first test case
	public void init(){
		TestCase theFirstOne = (TestCase) original.toArray()[0];
		this.addNewTestCase(theFirstOne);
	}



	/**
	 * get the best test cases
	 * @return
	 */
	public TestCase getNextBest() {
		TestCase result = null;
		int covered = 0;
		for (TestCase temp : originalTemp) {
			int tN = coveredNum.get(temp);
			if (tN == 0)
				continue;
			for (Tuple tuple : lastUpdated) {
				if (temp.containsOf(tuple)) {
					tN--;
				}
			}
			coveredNum.put(temp, tN);
			if (tN > covered) {
				covered = tN;
				result = temp;
			}
		}
		return result;
	}

	/**
	 * add the newly test cases and update the newly covered tuples
	 * @param testCase
	 */
	public void addNewTestCase(TestCase testCase) {
		this.selected.add(testCase);
		this.getCovered(testCase);
		this.originalTemp.remove(testCase);
	}

	/**
	 * compute the newly covered  tuples
	 * 
	 * 
	 * @param testCase
	 * @return
	 */
	public int getCovered(TestCase testCase) {
		// int[] coveringArray = new int[(int)
		// dataCenter.getCoveringArrayNum()];
		List<Tuple> updatedNow = new ArrayList<Tuple> ();
		int covered = 0;

		List<Tuple> tuples = ba.getTuplesFromTestCase(testCase,
				dataCenter.getDegree());

		for (int j = 0; j < tuples.size(); ++j) {
			long index = ba.getTupleIndex(tuples.get(j),
					dataCenter.getIndex()[j], dataCenter.getParam());
			if (coveringArray.get(index) == false) {
				updatedNow.add(tuples.get(j));
				++covered;
			}
			coveringArray.set(index);
		}
		this.lastUpdated = updatedNow;
		
		return covered;

	}

	// 这是上一个 新加入的 测试用例 含有 index （即新的index），下面每组只要 跟新 这个 新的 index 就行（数字，就是数字， 含有
	// 就减少，不含有就不变）
	public void process (){
		while(true){
			TestCase best = this.getNextBest();
			if(best == null)
				break;
			this.addNewTestCase(best);
		}
	}
}
