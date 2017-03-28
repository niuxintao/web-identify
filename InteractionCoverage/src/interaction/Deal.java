package interaction;

import java.util.ArrayList;
import java.util.List;

public class Deal {
	List<int[][]> path;
	List<int[]> cov;

	SAT sat;
	InteractionCov icov;
	int[] param;
	Data data;

	List<HavingCovrs> havingCovrs;

	// InteractionRelation ir;
	InputToClauses ic;

	public Deal(int[] param, List<int[][]> path, List<int[]> cov) {
		this.param = param;
		this.path = path;
		this.cov = cov;
		sat = new SAT();
		icov = new InteractionCov();
		data = new Data(param);

		// ir = new InteractionRelation(param);

		ic = new InputToClauses(param);

		havingCovrs = new ArrayList<HavingCovrs>();
	}

	public int[] getGuratte(int[] combination) {
		// this.printInt(combination);
		List<int[]> paCov = new ArrayList<int[]>();
		for (int i = 0; i < path.size(); i++) {
			// System.out.println(i);
			int[][] pa = path.get(i);
			List<int[]> SUTconstraints = ic.getClauses();
			int[][] newPa = getPathWithSUTConst(pa, SUTconstraints);
			if (sat.issatisfied(ic.getAllValue(), newPa, combination)) {
				// System.out.println(i);
				int[] co = cov.get(i);
				paCov.add(co);
			}
		}
		// System.out.println("size " + paCov.size());
		return icov.interact(paCov);
	}

	private int[][] getPathWithSUTConst(int[][] pa, List<int[]> SUTconstraints) {
		int[][] newPa = new int[pa.length + SUTconstraints.size()][];
		for (int j = 0; j < newPa.length; j++) {
			if (j < pa.length)
				newPa[j] = pa[j];
			else
				newPa[j] = SUTconstraints.get(j - pa.length);
		}
		return newPa;
	}

	public void guarAndNessForT0() {
		List<int[]> paCov = new ArrayList<int[]>();
		for (int[] co : cov)
			paCov.add(co);

		int[] gurCov = icov.interact(paCov);

		System.out.println("0 degree guar covage");
		printInt(gurCov);

		int[] ness = gurCov.clone();
		this.data.getGuarate(0).add(gurCov);
		this.data.nessary.get(0).add(ness);

		if (ness.length > 0) {
			HavingCovrs havingCov = new HavingCovrs();
			havingCov.index = new int[0];
			havingCov.value = new int[0];
			havingCov.covrage = ness;
			this.havingCovrs.add(havingCov);
		}
	}

	public void getGuratee(int t) {
		// InteractionRelation ir = new InteractionRelation(param);
		// InputToClauses ic = ir.getIc();

		// ir.getTwayInetarction(t);

		// get all the t-way combiantion
		List<int[]> t_combination = data.interactions.get(t);
		// for each combiation add the covage
		for (int[] com : t_combination) {
			int[] gurCov = this.getGuratte(com);
			this.data.getGuarate(t).add(gurCov);
			System.out.println("combiation");
			printInt(com);
			System.out.println("guar covage");
			printInt(gurCov);
			// gurCov =
		}
	}

	public void getNessary(int t) {
		List<int[]> t_combination = data.interactions.get(t);

		// DataCenter dataceneter = this.guarate.dataCeneters.get(t);

		DataCenter datacenetert_1 = this.data.dataCeneters.get(t - 1);

		for (int i = 0; i < t_combination.size(); i++) {
			int[] com = t_combination.get(i);
			int[] guarate = this.data.getGuarate(t).get(i); // ith
															// combination's
															// guarate
			int[] nessary = guarate.clone();
			System.out.println("combiation");
			printInt(com);
			if (t == 1) {
				int[] guaratet_1 = this.data.getGuarate(0).get(0);
				nessary = icov.removeSameElements(nessary, guaratet_1);
			} else {

				List<int[]> childs = ic.getChildCombination(com);
				for (int[] child : childs) {
					// this.printInt(child);
					List<int[]> in = this.ic.clauseToCombination(child);
					// System.out.println("in");
					// for (int[] j : in) {
					// this.printInt(j);
					// }
					int index = datacenetert_1.getIndexInArray(in.get(0),
							in.get(1));
					int[] guaratet_1 = this.data.getGuarate(t - 1).get(index);

					// execute the minus operation
					nessary = icov.removeSameElements(nessary, guaratet_1);

				}
			}

			data.nessary.get(t).add(nessary);

			if (nessary.length > 0) {
				List<int[]> in = this.ic.clauseToCombination(com);
				HavingCovrs havingCov = new HavingCovrs();
				havingCov.index = in.get(0);
				havingCov.value = in.get(1);
				havingCov.covrage = nessary;
				this.havingCovrs.add(havingCov);
			}

			System.out.println("nessary, covage");
			printInt(nessary);
		}

	}

	public void process() {
		guarAndNessForT0();
		for (int t = 1; t <= param.length; t++) {
			this.getGuratee(t);
		}
		for (int t = 1; t <= param.length; t++) {
			this.getNessary(t);
		}
	}

	public void outputStatistic() {
		for (HavingCovrs cov : havingCovrs) {
			System.out.println("index");
			this.printInt(cov.index);
			System.out.println("value");
			this.printInt(cov.value);
			System.out.println("covrage");
			this.printInt(cov.covrage);
		}
	}

	public void printInt(int[] combination) {
		for (int i : combination) {
			System.out.print(i + " ");
		}

		System.out.println();
	}

	public static void main(String[] args) {
		// four parameter: a b c d
		// a = 0 : 1
		// a = 1 : 2
		// b = 0 : 3
		// b = 1 : 4
		// c = 0 : 5
		// c = 1 : 6
		// d = 0 : 7
		// d = 1 : 8
		int[] param = new int[] { 2, 2, 2, 2 };
		List<int[][]> path = new ArrayList<int[][]>();
		// paths
		int[][] path1 = new int[][] { { 2 } }; 
		int[][] path2 = new int[][] { { 1 }, { 4 }, { 6, 8 } };
		int[][] path3 = new int[][] { { 1 }, { 4 }, { 5 }, { 7 } };
		int[][] path4 = new int[][] { { 1 }, { 3 } };
		path.add(path1);
		path.add(path2);
		path.add(path3);
		path.add(path4);
		List<int[]> cov = new ArrayList<int[]>();
		// the entities covered in the paths
		int[] cov1 = new int[] { 1 };  
		int[] cov2 = new int[] { 2, 4, 5 };
		int[] cov3 = new int[] { 2, 4 };
		int[] cov4 = new int[] {};
		cov.add(cov1);
		cov.add(cov2);
		cov.add(cov3);
		cov.add(cov4);

		Deal deal = new Deal(param, path, cov);
		deal.process();
		deal.outputStatistic();
	}
}

class Data {
	public List<List<int[]>> interactions;
	public List<DataCenter> dataCeneters;

	public List<List<int[]>> guarate;
	public List<List<int[]>> nessary;

	public Data() {
		guarate = new ArrayList<List<int[]>>();
	}

	public Data(int[] param) {
		guarate = new ArrayList<List<int[]>>();
		nessary = new ArrayList<List<int[]>>();
		interactions = new ArrayList<List<int[]>>();
		dataCeneters = new ArrayList<DataCenter>();

		InteractionRelation ir = new InteractionRelation(param);
		// from 0 way to K way
		for (int t = 0; t <= param.length; t++) {
			List<int[]> gcov = new ArrayList<int[]>();
			guarate.add(gcov);

			List<int[]> ness = new ArrayList<int[]>();
			nessary.add(ness);

			if (t != 0) {
				DataCenter dataCenter = new DataCenter(param, t);
				dataCeneters.add(dataCenter);

				ir.getTwayInetarction(t);
				interactions.add(ir.getInteractions());
			} else {
				dataCeneters.add(null);
				interactions.add(new ArrayList<int[]>());
			}
		}
	}

	public List<int[]> getGuarate(int t) {
		return this.guarate.get(t);
	}
}

class HavingCovrs {
	public int[] index;
	public int[] value;
	public int[] covrage;
}
