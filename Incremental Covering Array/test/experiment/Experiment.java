package experiment;

import incremental.Decrease;
import incremental.Increase;

public class Experiment {
	public final static int ITE = 30;

	public int[][] execute(int[] paramter, int min, int max) {
		int[][] result = new int[2][];

		Increase incease = new Increase();
		incease.execute(paramter, min, max);

		Decrease decrease = new Decrease();
		decrease.execute(paramter, min, max);

		result[0] = incease.getSize();
		result[1] = decrease.getSize();

		// System.out.println("increase");
		// incease.outputSize();
		// System.out.println("decrease");
		// decrease.outputSize();

		return result;
	}

	public void execute(int[] parameter) {
		int[][] increase = new int[4][];
		int[][] decrease = new int[4][];

		for (int i = 0; i < ITE; i++) {
			System.out.println("the " + i + " th");
			System.out.println("2 - 3");
			int[][] te23 = execute(parameter, 2, 3);
			increase[0] = this.addOrAssign(increase[0], te23[0]);
			decrease[0] = this.addOrAssign(decrease[0], te23[1]);
			System.out.println("2 - 4");
			int[][] te24 = execute(parameter, 2, 4);
			increase[1] = this.addOrAssign(increase[1], te24[0]);
			decrease[1] = this.addOrAssign(decrease[1], te24[1]);
			System.out.println("2 - 5");
			int[][] te25 = execute(parameter, 2, 5);
			increase[2] = this.addOrAssign(increase[2], te25[0]);
			decrease[2] = this.addOrAssign(decrease[2], te25[1]);
			System.out.println("2 - 6");
			int[][] te26 = execute(parameter, 2, 6);
			increase[3] = this.addOrAssign(increase[3], te26[0]);
			decrease[3] = this.addOrAssign(decrease[3], te26[1]);
		}

		double[][] inc = new double[4][];
		double[][] dec = new double[4][];
		for (int i = 0; i < 4; i++) {
			inc[i] = this.getAvg(increase[i]);
			print(inc[i]);
			dec[i] = this.getAvg(decrease[i]);
			print(dec[i]);
		}
	}

	public void print(double[] a) {
		for (double e : a)
			System.out.print(e + "  ");
		System.out.println();
	}

	public int[] addOrAssign(int[] a, int[] b) {
		if (a == null)
			return b;
		else
			return this.add(a, b);
	}

	public int[] add(int[] a, int[] b) {
		int[] result = new int[a.length];
		for (int i = 0; i < a.length; i++)
			result[i] = a[i] + b[i];

		return result;
	}

	public double[] getAvg(int[] a) {
		double[] result = new double[a.length];
		for (int i = 0; i < a.length; i++) {
			result[i] = (float) a[i] / (float) ITE;
		}

		return result;
	}

	public static void main(String[] args) {

		Experiment ex = new Experiment();
		int[] param = new int[30];
		for (int i = 0; i < 30; i ++ )
			param[i] = 2;
		ex.execute(param);
	}
}
