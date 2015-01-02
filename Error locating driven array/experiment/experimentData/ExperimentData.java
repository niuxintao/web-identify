package experimentData;

import java.util.List;

import com.fc.caseRunner.CaseRunner;
import com.fc.tuple.Tuple;

public interface ExperimentData {

	public abstract void init();

	public abstract void setDegree(int degree);

	public abstract int[] getParam();

	public abstract CaseRunner getCaseRunner();

	public abstract List<Tuple> getRealMFS();

	public abstract int getDegree();

}