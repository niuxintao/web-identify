package getResult;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.math3.stat.inference.TTest;

public class ReadInput {
	List<Item> items;

	HashMap<Integer, List<Item>> degreeID;

	HashMap<String, List<Item>> subjectID;

	HashMap<String, List<Item>> socID;

	HashMap<String, List<Item>> typeID;

	List<Item> _1to3;

	List<Item> _4to6;
	
	List<Item> _7to12;

	List<Item> _10to12;

	public List<Item> getItems() {
		return this.items;
	}

	public ReadInput() {
		this.items = new ArrayList<Item>();
		_1to3 = new ArrayList<Item>();

		_4to6 = new ArrayList<Item>();
		
		_7to12 = new ArrayList<Item>();

//		_10to12 = new ArrayList<Item>();

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
		
//		if (item.degree > 9)
//			_10to12.add(item);
//		else 
		if(item.degree > 6)
			_7to12.add(item);
		else if (item.degree > 3)
			_4to6.add(item);
		else
			_1to3.add(item);

		if (!subjectID.containsKey(subject)) {
			List<Item> itemsFor = new ArrayList<Item>();
			subjectID.put(subject, itemsFor);
		}
		subjectID.get(subject).add(item);

		// split soc
		// System.out.println(item.soc);
		if (!subject.equals("tcas")) {
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

	public void printDegeee(List<Item> items) {
		HashMap<Integer, List<Item>> num = new HashMap<Integer, List<Item>>();
		for (Item item : items) {
			if (!num.containsKey(item.degree)) {
				List<Item> itemsFor = new ArrayList<Item>();
				num.put(item.degree, itemsFor);
			}
			num.get(item.degree).add(item);
		}

		for (Integer key : num.keySet()) {
			System.out.println(key);
			System.out.println("size:" + degreeID.get(key).size());
			System.out.println("avg wg sg: ("
					+ String.format("%.2f", getAvgWg(degreeID.get(key))) + ","
					+ String.format("%.2f", getAvgSg(degreeID.get(key))) + ")");
		}
	}

	public void outputResult() {
		System.out.println("subject");
		for (String key : this.subjectID.keySet()) {
			System.out.println(key);
			outp(subjectID.get(key));
			System.out.println();
			System.out.println("degree for this subject");
			printDegeee(subjectID.get(key));
		}

		System.out.println("Degree");
		for (Integer key : this.degreeID.keySet()) {
			System.out.println(key);
			outp(degreeID.get(key));
			System.out.println();
		}

		System.out.println("DegreeDistrui");
		System.out.println("1 to 3");
		outp(this._1to3);
		System.out.println();
		System.out.println("4 to 6");
		outp(this._4to6);
		System.out.println();
		System.out.println("7 to 12");
		outp(this._7to12);
		System.out.println();
//		System.out.println("10 to 12");
//		outp(this._10to12);
//		System.out.println();

		System.out.println("t-test wg 1, 2");
		double[] a = this.getWglist(this._1to3);
		double[] b = this.getWglist(this._4to6);
		double[] c = this.getWglist(this._7to12);
//		double[] d = this.getWglist(this._10to12);
		System.out.print(getTtest(a, b) + " & ");
		System.out.print(getTtest(a, c) + " & ");
//		System.out.print(getTtest(a, d) + " & ");
		System.out.print(getTtest(b, c) + " & ");
//		System.out.print(getTtest(b, d) + " & ");
//		System.out.print(getTtest(c, d) + " & ");
		System.out.println();

		System.out.println("t-test sg 1, 2");
		a = this.getSglist(this._1to3);
		b = this.getSglist(this._4to6);
		c = this.getSglist(this._7to12);
//		d = this.getSglist(this._10to12);
		System.out.print(getTtest(a, b) + " & ");
		System.out.print(getTtest(a, c) + " & ");
//		System.out.print(getTtest(a, d) + " & ");
		System.out.print(getTtest(b, c) + " & ");
//		System.out.print(getTtest(b, d) + " & ");
//		System.out.print(getTtest(c, d) + " & ");
		System.out.println();

		System.out.println("Soc");
		// System.out.println("wg");
		for (String key : this.socID.keySet()) {
			System.out.println(key);
			outp(socID.get(key));
			System.out.println();
		}

		System.out.println("t-test wg soc");
		Object[] keys = this.socID.keySet().toArray();
		for (int i = 0; i < keys.length; i++) {
			String key1 = (String) keys[i];
			a = this.getWglist(socID.get(key1));
			for (int j = i + 1; j < keys.length; j++) {
				String key2 = (String) keys[j];
				System.out.println(key1 + " vs " + key2);
				b = this.getWglist(socID.get(key2));
				System.out.println(getTtest(a, b));
			}
		}
		System.out.println("t-test sg soc");
		keys = this.socID.keySet().toArray();
		for (int i = 0; i < keys.length; i++) {
			String key1 = (String) keys[i];
			a = this.getSglist(socID.get(key1));
			for (int j = i + 1; j < keys.length; j++) {
				String key2 = (String) keys[j];
				System.out.println(key1 + " vs " + key2);
				b = this.getSglist(socID.get(key2));
				System.out.println(getTtest(a, b));
			}
		}

		System.out.println("type");
		// System.out.println("wg");
		for (String key : this.typeID.keySet()) {
			System.out.println(key);
			outp(typeID.get(key));
			System.out.println();
		}

		System.out.println("t-test wg type");
		keys = this.typeID.keySet().toArray();
		for (int i = 0; i < keys.length; i++) {
			String key1 = (String) keys[i];
			a = this.getWglist(typeID.get(key1));
			for (int j = i + 1; j < keys.length; j++) {
				String key2 = (String) keys[j];
				System.out.println(key1 + " vs " + key2);
				b = this.getWglist(typeID.get(key2));
				System.out.println(getTtest(a, b));
			}
		}

		System.out.println("t-test sg type");
		keys = this.typeID.keySet().toArray();
		for (int i = 0; i < keys.length; i++) {
			String key1 = (String) keys[i];
			a = this.getSglist(typeID.get(key1));
			for (int j = i + 1; j < keys.length; j++) {
				String key2 = (String) keys[j];
				System.out.println(key1 + " vs " + key2);
				b = this.getSglist(typeID.get(key2));
				System.out.println(getTtest(a, b));
			}
		}

		System.out.println();
		// }
	}

	public static double getTtest(double[] a, double[] b) {
		// OneWayAnova anova = new OneWayAnova();
		if (a == null || b == null || a.length == 0 || b.length == 0)
			return -1;
		TTest ttest = new TTest();
		return ttest.tTest(a, b);

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

	public static void main(String[] args) throws FileNotFoundException {
		System.setOut(new PrintStream(new FileOutputStream("./result.txt")));
		ReadInput test = new ReadInput();

		String[] folders = { "tcas_model1_result", "totinfo_model1_result",
				"p1", "p2", "replace_experiment", "s1", "s2", "grep_single" };
		String[] subject = { "tcas", "totinfo", "printtokens", "printtokens2", "replace", "schedule", "schedule2", "grep" };
		int[] loop = { 41, 23, 7, 10, 32, 9, 10, 18 };

		for (int j = 0; j < folders.length; j++) {
			String folder = folders[j];
			for (int i = 1; i <= loop[j]; i++)
				test.read(folder + "/v" + i + "/guaranteed.txt", subject[j]);
		}

		test.outputResult();
	}
}


