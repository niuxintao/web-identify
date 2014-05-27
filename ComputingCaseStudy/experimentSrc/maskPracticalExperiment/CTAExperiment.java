package maskPracticalExperiment;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import maskTool.EvaluateTuples;

import com.fc.tuple.Tuple;

public class CTAExperiment {

	public void outPutAlo(BufferedWriter out, String aloName, double accurate,
			double father, double child, double miss, double reduntant)
			throws IOException {
		out.write(aloName);
		out.newLine();
		out.write("accurate :" + accurate);
		out.newLine();
		out.write("father :" + father);
		out.newLine();
		out.write("child :" + child);
		out.newLine();
		out.write("miss :" + miss);
		out.newLine();
		out.write("reduntant :" + reduntant);
		out.newLine();
		out.newLine();
	}

	public double similar(Tuple A, Tuple B) {
		int Aindex = 0;
		int Bindex = 0;
		double result = 0;

		// A change to B
		while (Aindex < A.getDegree() && Bindex < B.getDegree()) {
			if (A.getParamIndex()[Aindex] > B.getParamIndex()[Bindex]) {
				// A large than B , so now is B need to increase the index to
				// have a chance to meet A
				Bindex++;
			} else if (A.getParamIndex()[Aindex] < B.getParamIndex()[Bindex]) {
				// A smaller than B, so now is A need to increase the index to
				// have a chance to meet B
				Aindex++;
			} else if (A.getParamIndex()[Aindex] == B.getParamIndex()[Bindex]) {
				if (A.getParamValue()[Aindex] == B.getParamValue()[Bindex])// equal
																			// element
					result++;
				Aindex++;
				Bindex++;
			}
		}

		int maxDegree = A.getDegree() > B.getDegree() ? A.getDegree() : B
				.getDegree();

		return result / ((double) maxDegree);

	}

	public double similar(List<Tuple> setA, HashSet<Tuple> setB) {
		double similarNum = 0;
		for (Tuple A : setA) {
			double max = 0;
			for (Tuple B : setB) {
				double temp = similar(A, B);
				if (temp > max)
					max = temp;
			}
			similarNum += max;
		}
		return similarNum / ((double) setA.size());
	}

	public double similar(List<Tuple> setA, List<Tuple> setB) {
		double similarNum = 0;
		for (Tuple A : setA) {
			double max = 0;
			for (Tuple B : setB) {
				double temp = similar(A, B);
				if (temp > max)
					max = temp;
			}
			similarNum += max;
		}
		return similarNum / ((double) setA.size());
	}

	public void test() throws Exception {
		CTAbasic tc = new CTAbasic();
		Statistics statistic = new Statistics();
		statistic.readTestCases("./resultNew.txt");
		statistic.readBugCodeAndLowePriority("./FaultLevel.txt");
		List<Tuple> accurateBugs = new ArrayList<Tuple>();
		for (int wrongCode : statistic.getBugCode()) {
			ReadInput in = new ReadInput();
			in.readBugs("./bug_ot" + wrongCode + ".txt");
			List<Tuple> bugs = in.getBugs();
			accurateBugs.addAll(bugs);
		}

		double[] accurate = new double[3];
		double[] father = new double[3];
		double[] child = new double[3];
		double[] miss = new double[3];
		double[] redutant = new double[3];

		for (int i = 0; i < 3; i++) {
			accurate[i] = 0;
			father[i] = 0;
			child[i] = 0;
			miss[i] = 0;
			redutant[i] = 0;
		}

		int allNum = 30;

		for (int iter = 0; iter < allNum; iter++) {
			HashMap<Integer, List<Tuple>> ignoreTuples = tc
					.expCTAIngore(statistic);
			outputAlogirthmTuples("ignore", ignoreTuples, iter);
			List<Tuple> ingoreList = this.merge(ignoreTuples);
			EvaluateTuples eva = new EvaluateTuples();
			eva.evaluate(accurateBugs, ingoreList);
			accurate[0] += eva.getAccurateTuples().size();
			father[0] += eva.getFatherTuples().size();
			child[0] += eva.getChildTuples().size();
			miss[0] += eva.getMissTuples().size();
			redutant[0] += eva.getRedundantTuples().size();

			HashMap<Integer, List<Tuple>> oneTuples = tc.expCTAOne(statistic);
			outputAlogirthmTuples("one", oneTuples, iter);
			List<Tuple> oneList = this.merge(oneTuples);
			eva.evaluate(accurateBugs, oneList);
			accurate[1] += eva.getAccurateTuples().size();
			father[1] += eva.getFatherTuples().size();
			child[1] += eva.getChildTuples().size();
			miss[1] += eva.getMissTuples().size();
			redutant[1] += eva.getRedundantTuples().size();

			HashMap<Integer, List<Tuple>> ourTuples = tc.expCTAOur(statistic);
			outputAlogirthmTuples("our", ourTuples, iter);
			List<Tuple> ourList = this.merge(ourTuples);
			eva.evaluate(accurateBugs, ourList);
			accurate[2] += eva.getAccurateTuples().size();
			father[2] += eva.getFatherTuples().size();
			child[2] += eva.getChildTuples().size();
			miss[2] += eva.getMissTuples().size();
			redutant[2] += eva.getRedundantTuples().size();
		}

		for (int i = 0; i < 3; i++) {
			accurate[i] /= (float) allNum;
			father[i] /= (float) allNum;
			child[i] /= (float) allNum;
			miss[i] /= (float) allNum;
			redutant[i] /= (float) allNum;
		}

		String path = "./alogrithm/CTA/revAl_CTA_AVG_" + "ignore" + ".txt";

		BufferedWriter outStatis = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(path)));

		outPutAlo(outStatis, "CTA", accurate[0],father[0], child[0], miss[0], redutant[0]);
		outStatis.close();

		path = "./alogrithm/CTA/revAl_CTA_AVG_" + "one" + ".txt";
		outStatis = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(path)));

		outPutAlo(outStatis, "CTA",accurate[1], father[1], child[1], miss[1], redutant[1]);
		outStatis.close();

		path = "./alogrithm/CTA/revAl_CTA_AVG_" + "our" + ".txt";
		outStatis = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(path)));

		outPutAlo(outStatis, "CTA",accurate[2], father[2], child[2], miss[2], redutant[2]);
		outStatis.close();

	}

	public List<Tuple> merge(HashMap<Integer, List<Tuple>> hashTuples) {
		HashSet<Tuple> temp = new HashSet<Tuple>();
		List<Tuple> bugs = new ArrayList<Tuple>();

		for (Entry<Integer, List<Tuple>> entry : hashTuples.entrySet()) {
			temp.addAll(entry.getValue());
		}

		for (Tuple tuple : temp)
			bugs.add(tuple);

		return bugs;
	}

	public void outputAlogirthmTuples(String ID,
			HashMap<Integer, List<Tuple>> tuples, int iter) throws Exception {
		Set<Integer> wrongCodes = tuples.keySet();
		for (int wrongCode : wrongCodes) {
			List<Tuple> bugs = tuples.get(wrongCode);
			String partPath = "./alogrithm/CTA/" + "bug" + "/";
			// String path = partPath + "revAl_CTA_" + ID + "iter" + iter +
			// ".txt";
			BufferedWriter outCTA = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(partPath + "CTA_" + ID + ""
							+ wrongCode + "iter" + iter + ".txt")));
			if (bugs != null) {
				for (Tuple bug : bugs) {
					outCTA.write(toStringTuple(bug) + ";");
					outCTA.newLine();
				}
			}
			outCTA.close();
		}
	}

	public String toStringTuple(Tuple tuple) {
		int len = tuple.getCaseLen();
		int[] data = new int[len];
		for (int i = 0; i < len; i++) {
			data[i] = -1;
		}
		for (int i = 0; i < tuple.getDegree(); i++) {
			data[tuple.getParamIndex()[i]] = tuple.getParamValue()[i];
		}

		String rs = "";
		for (int i = 0; i < len; i++) {
			if (data[i] == -1)
				rs += "-";
			else
				rs += data[i];
			if (i != len - 1)
				rs += " ";
		}
		return rs;
	}

	public static void main(String[] args) throws Exception {
		CTAExperiment cx = new CTAExperiment();
		cx.test();
	}
}
