package experiment;

public class ExperimentData {
	public int[][] param;
	public int SUTNUM;
	
	public ExperimentData(){
		SUTNUM = 9;
		param = new int[SUTNUM][];
		
		int[] param1 = new int[7];
		for(int i = 0; i < 7; i ++)
			param1[i] = 4;
		param[0] = param1;
		
		
		int[] param2 = new int[15];
		for(int i = 0; i < 15; i ++)
			param2[i] = 2;
		param[1] = param2;
		
		
		int[] param3 = new int[8];
		for(int i = 0; i < 5; i ++)
			param3[i] = 2;
		for(int i = 5; i < 7; i ++)
			param3[i] = 3;
		for(int i = 7; i < 8; i ++)
			param3[i] = 5;
		param[2] = param3;
		
		int[] param4 = new int[12];
		for(int i = 0; i < 10; i ++)
			param4[i] = 2;
		for(int i = 10; i < 12; i ++)
			param4[i] = 3;
		param[3] = param4;
		
		int[] param5 = new int[9];
		for(int i = 0; i < 7; i ++)
			param5[i] = 3;
		for(int i = 7; i < 9; i ++)
			param5[i] = 4;
		param[4] = param5;
		
		int[] param6 = new int[10];
		for(int i = 0; i < 8; i ++)
			param6[i] = 2;
		for(int i = 8; i < 10; i ++)
			param6[i] = 9;
		param[5] = param6;
		
		
		int[] param7 = new int[13];
		for(int i = 0; i < 9; i ++)
			param7[i] = 2;
		for(int i = 9; i < 11; i ++)
			param7[i] = 3;
		for(int i = 11; i < 13; i ++)
			param7[i] = 5;
		param[6] = param7;
		
		
		int[] param8 = new int[11];
		for(int i = 0; i < 8; i ++)
			param8[i] = 2;
		for(int i = 8; i < 11; i ++)
			param8[i] = 4;
		param[7] = param8;
		
		
		int[] param9 = new int[12];
		for(int i = 0; i < 8; i ++)
			param9[i] = 2;
		for(int i = 8; i < 11; i ++)
			param9[i] = 3;
		for(int i = 11; i < 12; i ++)
			param9[i] = 4;
		param[8] = param9;
		
		
	}
}
