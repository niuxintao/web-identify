package example;

import incremental.Decrease;
import incremental.Increase;

public class Experiment {

	public static void main(String[] args){
		
		Increase incease = new Increase();
		int[] param = new int[] {2, 2, 2, 2};
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
