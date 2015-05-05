package experiment;

public class SynexExperiment {
	public static void main(String[] args) {
		SimpleExperiment ex = new SimpleExperiment();
		ex.testJFlex();
		ex.testGcc();
		ex.testHSQLDB();
		ex.testTomcat();
		ex.testTcas();

		SimpleExperiment_TLandCB ex2 = new SimpleExperiment_TLandCB();
		ex2.testJFlex();
		ex2.testGcc();
		ex2.testHSQLDB();
		ex2.testTomcat();
		ex2.testTcas();

		SimpleExperiment_FD_CIT ex3 = new SimpleExperiment_FD_CIT();
		ex3.testJFlex();
		ex3.testGcc();
		ex3.testHSQLDB();
		ex3.testTomcat();
		ex3.testTcas();

	}
}
