package incremental;

import java.util.ArrayList;
import java.util.List;

import com.fc.DataCenter.DataCenter;
import com.fc.testObject.TestCase;

public class GetBestFromExistingHaiming {

	// 原来的
	private List<TestCase> original;
	
	private List<TestCase> originalTemp;

	// extracting的
	private List<TestCase> selected;

//	private List<Tuple> lastUpdated;

//	private HashMap<TestCase, Integer> coveredNum;

	private DataCenter dataCenter;
//	private LongBitSet coveringArray;

//	private BasicAnalyse ba;

	public GetBestFromExistingHaiming(DataCenter dataCenter, List<TestCase> original) {
		this.dataCenter = dataCenter;
		this.original = original;

		selected = new ArrayList<TestCase>();
		
		originalTemp = new ArrayList<TestCase>();
		for(TestCase t: original){
			originalTemp.add(t);
		}
		
//		lastUpdated = new ArrayList<Tuple>();
//		ba = new BasicAnalyse();

//		coveringArray = new LongBitSet(dataCenter.getCoveringArrayNum());

//		coveredNum = new HashMap<TestCase, Integer>();
//		int value = dataCenter.getIndex().length;
//		for (TestCase testCase : original) {
//			coveredNum.put(testCase, value);
//		}
		
		init();
	}
	
	
	public int haimingDistance(TestCase A, TestCase B){
		int all = 0;
//		int different  = 0;
		for(int i = 0; i < A.getLength(); i++){
			if(A.getAt(i) != B.getAt(i)){
				all += 1;
			}
		}
		return all; 
	}
	
	public int haimingDistance(TestCase A, List<TestCase> Bset){
		int all =  0;
		for(TestCase B : Bset){
			all += this.haimingDistance(A, B);
		}
		return all;
	}
	
	public TestCase getBest(List<TestCase> selected, List<TestCase> candidates){
		int best = -1;
		TestCase bestTestCase = null; 
		for(TestCase can :candidates ){
			int value = this.haimingDistance(can, selected);
			if(value > best){
				best = value;
				bestTestCase = can;
			}
		}
		return bestTestCase;
	}
	
	
	public List<TestCase> getSelected() {
		return selected;
	}


	public DataCenter getDataCenter() {
		return dataCenter;
	}


	//add the first test case
	public void init(){
		TestCase theFirstOne = original.get(0);
		this.addNewTestCase(theFirstOne);
	}


	/**
	 * get the best test cases
	 * @return
	 */
	public TestCase getNextBest() {
		TestCase result = null;
		result = this.getBest(selected, originalTemp);
		return result;
	}

	/**
	 * add the newly test cases and update the newly covered tuples
	 * @param testCase
	 */
	public void addNewTestCase(TestCase testCase) {
		this.selected.add(testCase);
//		this.getCovered(testCase);
		this.originalTemp.remove(testCase);
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
