package getResult;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.math3.stat.descriptive.moment.Mean;
//import org.apache.commons.math3.linear.Array2DRowRealMatrix;
//import org.apache.commons.math3.linear.RealMatrix;
//import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.apache.commons.math3.stat.inference.TTest;

public class CompareDifferentModel {
	List<Item> items;

	HashMap<Integer, List<Item>> degreeID;

	HashMap<String, List<Item>> subjectID;

	HashMap<String, List<Item>> socID;

	HashMap<String, List<Item>> typeID;

	public List<Item> getItems() {
		return this.items;
	}

	public CompareDifferentModel() {
		this.items = new ArrayList<Item>();
		degreeID = new HashMap<Integer, List<Item>>();
		subjectID = new HashMap<String, List<Item>>();
		socID = new HashMap<String, List<Item>>();
		typeID = new HashMap<String, List<Item>>();
	}

	public void idAddItem(Item item, String subject) {
		if (!degreeID.containsKey(item.degree)) {
			List<Item> itemsFor = new ArrayList<Item>();
			degreeID.put(item.degree, itemsFor);
		}
		degreeID.get(item.degree).add(item);

		if (!subjectID.containsKey(subject)) {
			List<Item> itemsFor = new ArrayList<Item>();
			subjectID.put(subject, itemsFor);
		}
		subjectID.get(subject).add(item);

		// split soc
		// System.out.println(item.soc);
		String[] tokens = item.soc.split("\\|");
		HashSet<String> keys = new HashSet<String>();
		for (String tok : tokens) {
			keys.add(tok);
			// System.out.println(tok);
		}
		for (String k : keys) {
			if (!socID.containsKey(k)) {
				List<Item> itemsFor = new ArrayList<Item>();
				socID.put(k, itemsFor);
			}
			socID.get(k).add(item);
		}
		// split type
		tokens = item.type.split("\\|");
		keys = new HashSet<String>();
		for (String tok : tokens)
			keys.add(tok);
		for (String k : keys) {
			if (!typeID.containsKey(k)) {
				List<Item> itemsFor = new ArrayList<Item>();
				typeID.put(k, itemsFor);
			}
			typeID.get(k).add(item);
		}
	}

	public int getNumOfLinesOfCode(String line) {
		int result = 0;
		for (int i = 0; i < line.length(); i++)
			if (line.charAt(i) == ',')
				result++;
		return result;
	}

	public double getAvgWg(List<Item> items) {
		double re = 0;
		for (Item item : items)
			re += item.numOfwg;
		re /= (double) items.size();
		return re;
	}

	public double getAvgSg(List<Item> items) {
		double re = 0;
		for (Item item : items)
			re += item.numOfStr;
		re /= (double) items.size();
		return re;
	}

	public double[] getWglist(List<Item> items) {
		double[] result = new double[items.size()];
		for (int i = 0; i < result.length; i++)
			result[i] = items.get(i).relateWg;
		return result;
	}

	public double[] getSglist(List<Item> items) {
		double[] result = new double[items.size()];
		for (int i = 0; i < result.length; i++)
			result[i] = items.get(i).relateSg;
		return result;
	}

	public void outputResult() {
		System.out.println("all");
		outp(items);
		System.out.println();

		System.out.println("Degree");
		// System.out.println("wg");
		for (Integer key : this.degreeID.keySet()) {
			System.out.println(key);
			System.out.println("size:" + degreeID.get(key).size());
			System.out.println("avg wg sg: ("
					+ String.format("%.2f", getAvgWg(degreeID.get(key))) + ","
					+ String.format("%.2f", getAvgSg(degreeID.get(key))) + ")");
			// for (Item d : degreeID.get(key)) {
			// System.out.print(d.relateWg + ",");
			// }
			System.out.println();
		}
		// System.out.println("sg");
		// for (Integer key : this.degreeID.keySet()) {
		// System.out.println(key);
		// for (Item d : degreeID.get(key)) {
		// System.out.print(d.relateSg + ",");
		// }
		// System.out.println();
		// }

		System.out.println("Soc");
		System.out.println("list soc");
		for (String key : this.socID.keySet()) {
			System.out.println(key);
			outp(socID.get(key));
			System.out.println();
		}
		System.out.println("t-test wg soc");
		Object[] keys = this.socID.keySet().toArray();
		for (int i = 0; i < keys.length; i++) {
			String key1 = (String) keys[i];
			double[] a = this.getWglist(socID.get(key1));
			for (int j = i + 1; j < keys.length; j++) {
				String key2 = (String) keys[j];
				System.out.println(key1 + " vs " + key2);
				double[] b = this.getWglist(socID.get(key2));
				System.out.println(getTtest(a, b));
			}
		}

		System.out.println("t-test sg soc");
		keys = this.socID.keySet().toArray();
		for (int i = 0; i < keys.length; i++) {
			String key1 = (String) keys[i];
			double[] a = this.getSglist(socID.get(key1));
			for (int j = i + 1; j < keys.length; j++) {
				String key2 = (String) keys[j];
				System.out.println(key1 + " vs " + key2);
				double[] b = this.getSglist(socID.get(key2));
				System.out.println(getTtest(a, b));
			}
		}
		// System.out.println("sg");
		// for (String key : this.socID.keySet()) {
		// System.out.println(key);
		// for (Item d : socID.get(key)) {
		// System.out.print(d.relateSg + ",");
		// }
		// System.out.println();
		// }

		System.out.println("type");
		// System.out.println("wg");
		for (String key : this.typeID.keySet()) {
			System.out.println(key);
			outp(typeID.get(key));
			System.out.println();
		}
		// System.out.println("sg");
		// for (String key : this.typeID.keySet()) {
		// System.out.println(key);
		// for (Item d : typeID.get(key)) {
		// System.out.print(d.relateSg + ",");
		// }
		System.out.println();

		System.out.println("t-test wg type");
		keys = this.typeID.keySet().toArray();
		for (int i = 0; i < keys.length; i++) {
			String key1 = (String) keys[i];
			double[] a = this.getWglist(typeID.get(key1));
			for (int j = i + 1; j < keys.length; j++) {
				String key2 = (String) keys[j];
				System.out.println(key1 + " vs " + key2);
				double[] b = this.getWglist(typeID.get(key2));
				System.out.println(getTtest(a, b));
			}
		}

		System.out.println("t-test sg type");
		keys = this.typeID.keySet().toArray();
		for (int i = 0; i < keys.length; i++) {
			String key1 = (String) keys[i];
			double[] a = this.getSglist(typeID.get(key1));
			for (int j = i + 1; j < keys.length; j++) {
				String key2 = (String) keys[j];
				System.out.println(key1 + " vs " + key2);
				double[] b = this.getSglist(typeID.get(key2));
				System.out.println(getTtest(a, b));
			}
		}
		// }
	}

	public static double getTtest(double[] a, double[] b) {
		// OneWayAnova anova = new OneWayAnova();
		TTest ttest = new TTest();
		return ttest.tTest(a, b);

	}

	public void outp(List<Item> items) {
		System.out.print("[[");
		for (Item item : items)
			System.out.print(item.relateWg + ",");
		System.out.print("],[");
		for (Item item : items)
			System.out.print(item.relateSg + ",");
		System.out.print("]]");
	}

	public void read(String path, String subject) {
		try {
			FileInputStream fstream = new FileInputStream(path);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			int n = 0;
			while ((strLine = br.readLine()) != null) {
				if (strLine.length() > 0) {
					n++;
					if (n > 1) {
						Item item = new Item();
						// System.out.println(strLine);
						String[] tokens = strLine.split("\t");
						item.degree = Integer.parseInt(tokens[1]);
						item.relateWg = Double.parseDouble(tokens[4]);
						item.relateSg = Double.parseDouble(tokens[5]);
						item.numOfwg = this.getNumOfLinesOfCode(tokens[2]);
						item.numOfStr = this.getNumOfLinesOfCode(tokens[3]);
						item.soc = tokens[6];
						item.type = tokens[7];
						items.add(item);
						idAddItem(item, subject);
					}
				}
			}
			if (n < 2)
				System.out.println(path);
			// Close the input stream
			in.close();
		} catch (Exception e) {// Catch exception if any
			e.printStackTrace();
			System.err.println("Error: " + e.getMessage());
		}
	}

	public double[] getDegree() {
		double[] it = new double[items.size()];
		for (int i = 0; i < it.length; i++) {
			it[i] = items.get(i).degree;
		}
		return it;
	}

	public double[] getWg() {
		double[] it = new double[items.size()];
		for (int i = 0; i < it.length; i++) {
			it[i] = items.get(i).relateWg;
		}
		return it;
	}

	public double[] getSg() {
		double[] it = new double[items.size()];
		for (int i = 0; i < it.length; i++) {
			it[i] = items.get(i).relateSg;
		}
		return it;
	}

	public void outputTwoDouble(String flag, double[] a, double[] b) {
		System.out.println(flag);

		Mean mean = new Mean();
		TTest ttest = new TTest();

		System.out.println("average, gap, p-value");
		System.out.println(mean.evaluate(b) + "&"
				+ (mean.evaluate(b) - mean.evaluate(a)) + "&"
				+ ttest.tTest(a, b));
		System.out.println();
	}

	public void outputCompare(CompareDifferentModel t2) {
		outputTwoDouble("degree", this.getDegree(), t2.getDegree());
		outputTwoDouble("wg", this.getWg(), t2.getWg());
		outputTwoDouble("sg", this.getSg(), t2.getSg());
	}

	public static void main(String[] args) {
		CompareDifferentModel test = new CompareDifferentModel();
		String[] folders = { "tcas_model1_result" };
		for (String folder : folders) {
			for (int i = 1; i <= 41; i++)
				test.read(folder + "/v" + i + "/guaranteed.txt", "tcas");
		}

		CompareDifferentModel test2 = new CompareDifferentModel();
		String[] folders2 = { "tcas_model2_result" };
		for (String folder : folders2) {
			for (int i = 1; i <= 41; i++)
				test2.read(folder + "/v" + i + "/guaranteed.txt", "tcas");
		}

		CompareDifferentModel test3 = new CompareDifferentModel();
		String[] folders3 = { "tcas_model3_result" };
		for (String folder : folders3) {
			for (int i = 1; i <= 41; i++)
				test3.read(folder + "/v" + i + "/guaranteed.txt", "tcas");
		}

		test.outputResult();
		System.out.println("compare model2");
		test.outputCompare(test2);
		System.out.println("compare model3");
		test.outputCompare(test3);

	}
}
