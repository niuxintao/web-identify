package experimentDataMultipleDefects;

import interaction.DataCenter;

import java.util.ArrayList;
import java.util.List;

import com.fc.caseRunner.BasicRunner;
import com.fc.caseRunner.CaseRunner;
import com.fc.tuple.Tuple;

import experimentData.ExperimentData;

public class MultipleDefectsData implements ExperimentData {

	private int[] param;
	private BasicRunner caseRunner;
	private List<Tuple> realMFS;
	private DataCenter dataCenter;
	
	private ExpriSetUp setup; 
	private DataRecord record;

	public MultipleDefectsData(int subject) {
		setup = new ExpriSetUp();
		this.record = setup.getRecords().get(subject);
		
		setup.set(record.param, record.wrongs, record.bugs, record.faults,
				record.priority);
		this.init();
	}

	public DataCenter getDataCenter() {
		return dataCenter;
	}

	public void init() {
		
		this.param = record.param;


		realMFS = new ArrayList<Tuple>();

		this.caseRunner = new BasicRunner(setup.getPriorityList(), setup.getBugsList());
		
		for(Integer i : setup.getBugsList().keySet()){
			for(Tuple t : setup.getBugsList().get(i)){
				realMFS.add(t);
			}
		}
	}

	public void setDegree(int degree) {
		this.dataCenter = new DataCenter(param, degree);
	}

	public int[] getParam() {
		return param;
	}

	public CaseRunner getCaseRunner() {
		return caseRunner;
	}

	public List<Tuple> getRealMFS() {
		return realMFS;
	}

}
