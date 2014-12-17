package example;

import incremental.Decrease;
import incremental.Increase;

public class TestInAndDe {
	
	public static void main(String[] args){
		
		Increase incease = new Increase();
		int[] param = new int[] {2, 2, 2, 2, 2};
		incease.execute(param, 2, 4);
		incease.output();
		
		
		Decrease decrease = new Decrease();
		decrease.execute(param, 2, 4);
		decrease.output();
		
		
		incease.outputSize();
		decrease.outputSize();
	}
}
