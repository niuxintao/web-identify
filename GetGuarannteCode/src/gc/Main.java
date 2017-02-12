package gc;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.List;

import com.fc.tuple.Tuple;

import InputOutput.ReadInput;

public class Main {
	// weak
	ReadInput in = new ReadInput();

	public void print(String folder, String pathOftestCase, String pathOfMFS,
			String pathofcoveredLines, String faultyinfo, int v) {
		in.readParam(pathOftestCase);
		in.readBugs(pathOfMFS);
		in.readCoveredLines(pathofcoveredLines);
		in.readFaulty(faultyinfo, v);

		GetTheWeakGuannteedCodeOfSchema gwg = new GetTheWeakGuannteedCodeOfSchema();
		GetTheStrongGuannteedCodeOfSchema gsg = new GetTheStrongGuannteedCodeOfSchema();

		String file = folder + "/guaranteed" + ".txt";
		try {
			BufferedWriter outSta = null;

			outSta = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(file)));
			// outSta.write("all : ");
			// outSta.write(allTestCase + "");
			// outSta.newLine();

			outSta.write("tuple\tdegree\tsoftw\twg\tsg\trewg\tresg\tsoc\tfautype");
			outSta.newLine();
			for (int j = 0; j < in.getBugs().size(); j++) {
				Tuple tuple = in.getBugs().get(j);
				outSta.write(in.getBugsString().get(j) + "\t");
				outSta.write(tuple.getDegree() + "\t");
				int[] wg = gwg.wgc(tuple, in.getParam(), in.getCoveredLines());
				for (int i : wg)
					outSta.write(i + ",");
				outSta.write("\t");
				int[] sg = gsg.sgc(tuple, in.getParam(), in.getCoveredLines());
				for (int i : sg)
					outSta.write(i + ",");
				outSta.write("\t");

				outSta.write(this.related(in.getRealFautyLines(), wg) + "");
				outSta.write("\t");
				outSta.write(this.related(in.getRealFautyLines(), sg) + "");
				outSta.write("\t");

				outSta.write(in.getSocType());
				outSta.write("\t");
				outSta.write(in.getFaultyType());
				outSta.write("\t");

				outSta.newLine();
			}

			outSta.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public double related(List<Integer> real, int[] obtain) {
		int common = 0;
		for (Integer i : real) {
			boolean isIn = false;
			for (int j : obtain) {
				if (i.intValue() == j) {
					isIn = true;
				}
				break;
			}
			if (isIn)
				common++;
		}
		int denominator = (real.size() - common) + (obtain.length - common)
				+ common;
		return (double) common / (double) denominator;
	}

	public void print(String folder, String pathOftestCase, String pathOfMFS,
			String pathofcoveredLines) {
		in.readParam(pathOftestCase);
		in.readBugs(pathOfMFS);
		in.readCoveredLines(pathofcoveredLines);
		GetTheWeakGuannteedCodeOfSchema gwg = new GetTheWeakGuannteedCodeOfSchema();
		GetTheStrongGuannteedCodeOfSchema gsg = new GetTheStrongGuannteedCodeOfSchema();

		String file = folder + "/guaranteed" + ".txt";
		try {
			BufferedWriter outSta = null;

			outSta = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(file)));
			// outSta.write("all : ");
			// outSta.write(allTestCase + "");
			// outSta.newLine();

			outSta.write("tuple\tdegree\tsoftw\twg\tsg");
			outSta.newLine();
			for (Tuple tuple : in.getBugs()) {
				outSta.write(tuple.toString() + "\t");
				outSta.write(tuple.getDegree() + "\t");
				int[] wg = gwg.wgc(tuple, in.getParam(), in.getCoveredLines());
				for (int i : wg)
					outSta.write(i + ",");
				outSta.write("\t");
				int[] sg = gsg.sgc(tuple, in.getParam(), in.getCoveredLines());
				for (int i : sg)
					outSta.write(i + ",");
				outSta.write("\t");
				outSta.newLine();
			}

			outSta.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		Main m = new Main();
		// m.print("v1", "v1" + "/result_of_testCase.txt", "v1" +
		// "/bug_ot1.txt",
		// "v1" + "/spectra.txt", "v1/faulty.txt");

		if (args.length >= 2) {
			Integer v = Integer.parseInt(args[2]);
			m.print(args[0], args[0] + "/result_of_testCase.txt", args[0]
					+ "/bug_ot1.txt", args[0] + "/spectra.txt", args[1], v.intValue());
		} else
			m.print(args[0], args[0] + "/result_of_testCase.txt", args[0]
					+ "/bug_ot1.txt", args[0] + "/spectra.txt");

	}
	// strong
}
