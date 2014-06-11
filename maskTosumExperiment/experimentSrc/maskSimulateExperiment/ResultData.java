package maskSimulateExperiment;

import java.util.HashSet;
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

	public ResultData(List<Tuple> realMFS, List<Tuple> practiceBugs) {
		this.setData(realMFS, practiceBugs);
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
		HashSet<Tuple> reveseTuple = new HashSet<Tuple>();
		for (Tuple tuple : praticeBugs) {
			boolean flag = true;
			for (Tuple mfs : wrongBugs) {
				if (tuple.equals(mfs)) {
					this.accurate++;
					flag = false;
					reveseTuple.add(mfs);
					break;
				} else if (tuple.contains(mfs)) {
					this.parent++;
					this.parentRelated += this.computeRelated(tuple, mfs);
					reveseTuple.add(mfs);
					flag = false;
					break;
				} else if (mfs.contains(tuple)) {
					this.sub++;
					this.subRelated += this.computeRelated(tuple, mfs);
					flag = false;
					reveseTuple.add(mfs);
					break;
				}
			}
			if (flag)
				this.irrelevant++;
		}
		this.ignore = wrongBugs.size() - reveseTuple.size();
		computeIntegratedMetric();
	}
}
