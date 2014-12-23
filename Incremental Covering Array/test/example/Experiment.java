package example;

import incremental.Decrease;
import incremental.Increase;

public class Experiment {

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
		execute(parameter, 2, 3);
		execute(parameter, 2, 4);
		execute(parameter, 2, 5);
		execute(parameter, 2, 6);
	}

	public static void main(String[] args) {

		Increase incease = new Increase();
		int[] param = new int[] { 2, 2, 2, 2 };
		incease.execute(param, 2, 3);
		System.out.println("increase");
		incease.output();

		System.out.println();
		Decrease decrease = new Decrease();
		decrease.execute(param, 2, 3);
		System.out.println("decrease");
		decrease.output();

		System.out.println();
		decrease.outputRaw();

		incease.outputSize();
		decrease.outputSize();
	}
}
