package maskSimulateExperiment;

import java.util.List;

import com.fc.tuple.Tuple;

public class ResultData {

	private int accurate;
	private int parent;
	private int sub;
	private int irrelevant;
	private int ignore;

	private double parentRelated;
	private double subRelated;

	private double interatgedMetirc;

	public ResultData() {

	}

	public double getInteratgedMetirc() {
		return interatgedMetirc;
	}

	public int getAccurate() {
		return accurate;
	}

	public int getParent() {
		return parent;
	}

	public int getSub() {
		return sub;
	}

	public int getIrrelevant() {
		return irrelevant;
	}

	public int getIgnore() {
		return ignore;
	}

	public void computeIntegratedMetric() {
		double result = 0;
		result += (accurate + parentRelated + subRelated);

		result /= (double) (accurate + parent + sub + irrelevant + ignore);

		this.interatgedMetirc = result;
	}

	public double computeRelated(Tuple A, Tuple B) {
		double degree = A.getDegree() > B.getDegree() ? A.getDegree() : B
				.getDegree();
		double same = 0;
		int Aindex = 0;
		int Bindex = 0;

		while (Aindex < A.getDegree() && Bindex < B.getDegree()) {
			if (A.getParamIndex()[Aindex] > B.getParamIndex()[Bindex])
				Bindex++;
			else if (A.getParamIndex()[Aindex] < B.getParamIndex()[Bindex])
				Aindex++;
			else if (A.getParamIndex()[Aindex] == B.getParamIndex()[Bindex]) {
				if (A.getParamValue()[Aindex] == B.getParamValue()[Bindex]) {
					same++;
				}
				Aindex++;
				Bindex++;
			}
		}
		return same / degree;
	}

	public void setData(List<Tuple> wrongBugs, List<Tuple> praticeBugs) {

	}
}
