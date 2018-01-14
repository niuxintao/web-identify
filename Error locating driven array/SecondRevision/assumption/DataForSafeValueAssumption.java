package assumption;

import interaction.DataCenter;

import java.util.ArrayList;
import java.util.List;

import com.fc.caseRunner.CaseRunner;
import com.fc.caseRunner.CaseRunnerWithBugInject;
import com.fc.testObject.TestCase;
import com.fc.testObject.TestCaseImplement;
import com.fc.tuple.Tuple;

import experimentData.ExperimentData;

public class DataForSafeValueAssumption implements ExperimentData {

	private int[] param;
	private CaseRunner caseRunner;
	private List<Tuple> realMFS;
	private DataCenter dataCenter;
	// private List<Tuple> allMFS;

	private List<Tuple> UNsafeMFS;

	public List<Tuple> getUnSafeMFS() {
		return UNsafeMFS;
	}

	public DataForSafeValueAssumption(int param_length, int degree) {
		this.init(param_length, degree);

	}

	// DataCenter parameters  4^16
	public void init(int param_length, int id) {
		// TODO Auto-generated method stub
		this.param = new int[param_length]; // new int[] { 3, 3, 3, 3, 3, 3, 3,
											// 3 };//
		// parameters
		for (int i = 0; i < param_length; i++)
			param[i] = 4;
		// this.getAllMFS();
		
		realMFS  = this.getBugList().get(id);
		caseRunner = new CaseRunnerWithBugInject(); 
		this.insertBugModes(realMFS);
//		UNsafeMFS = new ArrayList<Tuple>();

//		this.setMFS(degree);
	}

	// /**
	/*
	 * add n MFS
	 * 
	 * @param n
	 */
//	public void setMFS(int degree) {
//		realMFS = new ArrayList<Tuple>();
//		caseRunner = new CaseRunnerWithBugInject();
//		//
//		// int count = 0;
//		//
//
//		// first set
//		int[] wrong = new int[param.length];
//		for (int i = 0; i < param.length; i++)
//			wrong[i] = 0;
//		wrong[4] = 1;
		// TestCase wrongCase = new TestCaseImplement();
		// ((TestCaseImplement) wrongCase).setTestCase(wrong);
		// Tuple bugMode = new Tuple(2, wrongCase);
		//
		// bugMode.set(0, 1);
		// bugMode.set(1, 5);
		//
		// realMFS.add(bugMode);
		// ((CaseRunnerWithBugInject) caseRunner).inject(bugMode);
		//
//		TestCase wrongCase = new TestCaseImplement();
//		((TestCaseImplement) wrongCase).setTestCase(wrong);
//
//		Tuple bugModel = new Tuple(2, wrongCase);
//		bugModel.set(0, 0);
//		bugModel.set(1, 4);
//		// bugModel1.set(2, 4);
//		// bugModel1.set(1, 2);

		/**
		 * *********************************************************** note that
		 * we have not done all the schemas.
		 * ***********************************************************
		 */

//		int[] wrong2 = new int[param.length]; // MFS
//		for (int i = 0; i < wrong2.length; i++)
//			wrong2[i] = 1;
//
//		wrong2[3] = 0;
//
//		TestCase wrongCase2 = new TestCaseImplement();
//		((TestCaseImplement) wrongCase2).setTestCase(wrong2);
//
//		Tuple bugModel2 = new Tuple(2, wrongCase2);
//		bugModel2.set(0, 1);
//		bugModel2.set(1, 3);
//
//		realMFS.add(bugModel);
//
//		realMFS.add(bugModel2); // add or not add
//
//		((CaseRunnerWithBugInject) caseRunner).inject(bugModel);
//
//		((CaseRunnerWithBugInject) caseRunner).inject(bugModel2);
//
//		for (int i = 0; i < param[0]; i++) {
//			setForDegreeAndValues(i, degree);
//		}
		// setForDegreeAndValues(1, degree);
		// setForDegreeAndValues(2, degree);

		// count = onceLoop(n, count, wrong);
		//
		// while (count < n) {
		// base++;
		// wrong = new int[param.length];
		// for (int i = 0; i < param.length; i++)
		// wrong[i] = base;
		// count = onceLoop(n, count, wrong);
		// // System.out.println(count + " + " + n);
		// }
//	}

	// 4 维
//	public void setForDegreeAndValues(int value, int degree) {
//		int base = value;
//		int[] wrong = new int[param.length];
//		for (int i = 0; i < param.length; i++)
//			wrong[i] = base;
//
//		// degree = 4;
//
//		for (int i = 0; i < param.length / degree; i++) {
//			TestCase wrongCase = new TestCaseImplement();
//			((TestCaseImplement) wrongCase).setTestCase(wrong);
//			Tuple bugMode = new Tuple(degree, wrongCase);
//			for (int j = 0; j < degree; j++) {
//				bugMode.set(j, i * degree + j);
//			}
//			// realMFS.add(bugMode);
//			UNsafeMFS.add(bugMode);
//			((CaseRunnerWithBugInject) caseRunner).inject(bugMode);
//		}
//	}

	public void insertBugModes(List<Tuple> modes) {
		for (Tuple bugMode : modes)
			((CaseRunnerWithBugInject) caseRunner).inject(bugMode);
	}
    
	/**
	 * 1. 16
	 * 2. 8 , 2
	 * 3. 8, 4, 4
	 * 4. 8, 6, 2
	 * 5. 8, 3, 3, 2
	 * 6. 6, 6, 4
	 * 7. 6, 4, 4, 2
	 * 8. 4, 4, 4, 4
	 * 9. 4, 3, 3, 3, 3
	 * 10. 4, 3, 3, 2, 2, 2
	 * 11. 3, 3, 3, 3, 2, 2
	 * 
	 * @return
	 */
	public List<List<Tuple>> getBugList() {
		List<List<Tuple>> result = new ArrayList<List<Tuple>>();
		List<Tuple> temp = null;

		// 第一个
		// 16
		temp = new ArrayList<Tuple>();
		result.add(temp);

		for (int base = 0; base < param[0]; base++) {
			int[] wrong = new int[param.length];
			for (int j = 0; j < param.length; j++)
				wrong[j] = base;

			TestCase wrongCase = new TestCaseImplement();
			((TestCaseImplement) wrongCase).setTestCase(wrong);

			int degree = 16;
			Tuple bugMode = new Tuple(degree, wrongCase);
			for (int j = 0; j < degree; j++) {
				bugMode.set(j, j);
			}

			temp.add(bugMode);
			//
		}

		// 第二个 8 * 2
		temp = new ArrayList<Tuple>();
		result.add(temp);
		

		for (int base = 0; base < param[0]; base++) {
			int[] wrong = new int[param.length];
			for (int j = 0; j < param.length; j++)
				wrong[j] = base;

			TestCase wrongCase = new TestCaseImplement();
			((TestCaseImplement) wrongCase).setTestCase(wrong);

			int degree = 8;
			Tuple bugMode = new Tuple(degree, wrongCase);
			for (int j = 0; j < degree; j++) {
				bugMode.set(j, j);
			}
			temp.add(bugMode);
			
			bugMode = new Tuple(degree, wrongCase);
			for (int j = 0; j < degree; j++) {
				bugMode.set(j, j+8);
			}
			temp.add(bugMode);
			//
		}

		// 第三个 8, 4, 4
		
		temp = new ArrayList<Tuple>();
		result.add(temp);

		for (int base = 0; base < param[0]; base++) {
			int[] wrong = new int[param.length];
			for (int j = 0; j < param.length; j++)
				wrong[j] = base;

			TestCase wrongCase = new TestCaseImplement();
			((TestCaseImplement) wrongCase).setTestCase(wrong);

			int degree = 8;
			Tuple bugMode = new Tuple(degree, wrongCase);
			for (int j = 0; j < degree; j++) {
				bugMode.set(j, j);
			}
			temp.add(bugMode);
			
			degree = 4;
			bugMode = new Tuple(degree, wrongCase);
			for (int j = 0; j < degree; j++) {
				bugMode.set(j, j+8);
			}
			temp.add(bugMode);
			
			
			degree = 4;
			bugMode = new Tuple(degree, wrongCase);
			for (int j = 0; j < degree; j++) {
				bugMode.set(j, j+8+4);
			}
			temp.add(bugMode);
			//
		}
		

		// 第四个 8, 6, 2
		temp = new ArrayList<Tuple>();
		result.add(temp);

		for (int base = 0; base < param[0]; base++) {
			int[] wrong = new int[param.length];
			for (int j = 0; j < param.length; j++)
				wrong[j] = base;

			TestCase wrongCase = new TestCaseImplement();
			((TestCaseImplement) wrongCase).setTestCase(wrong);

			int degree = 8;
			Tuple bugMode = new Tuple(degree, wrongCase);
			for (int j = 0; j < degree; j++) {
				bugMode.set(j, j);
			}
			temp.add(bugMode);
			
			degree = 6;
			bugMode = new Tuple(degree, wrongCase);
			for (int j = 0; j < degree; j++) {
				bugMode.set(j, j+8);
			}
			temp.add(bugMode);
			
			
			degree = 2;
			bugMode = new Tuple(degree, wrongCase);
			for (int j = 0; j < degree; j++) {
				bugMode.set(j, j+8+6);
			}
			temp.add(bugMode);
			//
		}
		// 第5个 8， 3， 3， 2
		temp = new ArrayList<Tuple>();
		result.add(temp);

		for (int base = 0; base < param[0]; base++) {
			int[] wrong = new int[param.length];
			for (int j = 0; j < param.length; j++)
				wrong[j] = base;

			TestCase wrongCase = new TestCaseImplement();
			((TestCaseImplement) wrongCase).setTestCase(wrong);

			int degree = 8;
			Tuple bugMode = new Tuple(degree, wrongCase);
			for (int j = 0; j < degree; j++) {
				bugMode.set(j, j);
			}
			temp.add(bugMode);
			
			degree = 3;
			bugMode = new Tuple(degree, wrongCase);
			for (int j = 0; j < degree; j++) {
				bugMode.set(j, j+8);
			}
			temp.add(bugMode);
			
			
			degree = 3;
			bugMode = new Tuple(degree, wrongCase);
			for (int j = 0; j < degree; j++) {
				bugMode.set(j, j+8+3);
			}
			temp.add(bugMode);
			
			degree = 2;
			bugMode = new Tuple(degree, wrongCase);
			for (int j = 0; j < degree; j++) {
				bugMode.set(j, j+8+3+3);
			}
			temp.add(bugMode);
			//
		}
		
		

		// 第五个6， 6， 4
		temp = new ArrayList<Tuple>();
		result.add(temp);

		for (int base = 0; base < param[0]; base++) {
			int[] wrong = new int[param.length];
			for (int j = 0; j < param.length; j++)
				wrong[j] = base;

			TestCase wrongCase = new TestCaseImplement();
			((TestCaseImplement) wrongCase).setTestCase(wrong);

			int degree = 6;
			Tuple bugMode = new Tuple(degree, wrongCase);
			for (int j = 0; j < degree; j++) {
				bugMode.set(j, j);
			}
			temp.add(bugMode);
			
			degree = 6;
			bugMode = new Tuple(degree, wrongCase);
			for (int j = 0; j < degree; j++) {
				bugMode.set(j, j+6);
			}
			temp.add(bugMode);
			
			
			degree = 4;
			bugMode = new Tuple(degree, wrongCase);
			for (int j = 0; j < degree; j++) {
				bugMode.set(j, j+6+6);
			}
			temp.add(bugMode);
			//
		}
		
		
		// 第6个6， 4， 4， 2
		
		temp = new ArrayList<Tuple>();
		result.add(temp);

		for (int base = 0; base < param[0]; base++) {
			int[] wrong = new int[param.length];
			for (int j = 0; j < param.length; j++)
				wrong[j] = base;

			TestCase wrongCase = new TestCaseImplement();
			((TestCaseImplement) wrongCase).setTestCase(wrong);

			int degree = 6;
			Tuple bugMode = new Tuple(degree, wrongCase);
			for (int j = 0; j < degree; j++) {
				bugMode.set(j, j);
			}
			temp.add(bugMode);
			
			degree = 4;
			bugMode = new Tuple(degree, wrongCase);
			for (int j = 0; j < degree; j++) {
				bugMode.set(j, j+6);
			}
			temp.add(bugMode);
			
			
			degree = 4;
			bugMode = new Tuple(degree, wrongCase);
			for (int j = 0; j < degree; j++) {
				bugMode.set(j, j+6+4);
			}
			temp.add(bugMode);
			
			degree = 2;
			bugMode = new Tuple(degree, wrongCase);
			for (int j = 0; j < degree; j++) {
				bugMode.set(j, j+6+4+4);
			}
			temp.add(bugMode);
			//
		}
		

		// 第七个 6， 4， 3， 3
		temp = new ArrayList<Tuple>();
		result.add(temp);

		for (int base = 0; base < param[0]; base++) {
			int[] wrong = new int[param.length];
			for (int j = 0; j < param.length; j++)
				wrong[j] = base;

			TestCase wrongCase = new TestCaseImplement();
			((TestCaseImplement) wrongCase).setTestCase(wrong);

			int degree = 6;
			Tuple bugMode = new Tuple(degree, wrongCase);
			for (int j = 0; j < degree; j++) {
				bugMode.set(j, j);
			}
			temp.add(bugMode);
			
			degree = 4;
			bugMode = new Tuple(degree, wrongCase);
			for (int j = 0; j < degree; j++) {
				bugMode.set(j, j+6);
			}
			temp.add(bugMode);
			
			
			degree = 3;
			bugMode = new Tuple(degree, wrongCase);
			for (int j = 0; j < degree; j++) {
				bugMode.set(j, j+6+4);
			}
			temp.add(bugMode);
			
			degree = 3;
			bugMode = new Tuple(degree, wrongCase);
			for (int j = 0; j < degree; j++) {
				bugMode.set(j, j+6+4+3);
			}
			temp.add(bugMode);
			//
		}
		
		
		
		// 第8个 4， 4，4，4
		
		temp = new ArrayList<Tuple>();
		result.add(temp);

		for (int base = 0; base < param[0]; base++) {
			int[] wrong = new int[param.length];
			for (int j = 0; j < param.length; j++)
				wrong[j] = base;

			TestCase wrongCase = new TestCaseImplement();
			((TestCaseImplement) wrongCase).setTestCase(wrong);

			int degree = 4;
			Tuple bugMode = new Tuple(degree, wrongCase);
			for (int j = 0; j < degree; j++) {
				bugMode.set(j, j);
			}
			temp.add(bugMode);
			
			degree = 4;
			bugMode = new Tuple(degree, wrongCase);
			for (int j = 0; j < degree; j++) {
				bugMode.set(j, j+4);
			}
			temp.add(bugMode);
			
			
			degree = 4;
			bugMode = new Tuple(degree, wrongCase);
			for (int j = 0; j < degree; j++) {
				bugMode.set(j, j+4+4);
			}
			temp.add(bugMode);
			
			degree = 4;
			bugMode = new Tuple(degree, wrongCase);
			for (int j = 0; j < degree; j++) {
				bugMode.set(j, j+4+4+4);
			}
			temp.add(bugMode);
			//
		}
		
		

		// 第9 个 4，3，3，3，3
		temp = new ArrayList<Tuple>();
		result.add(temp);

		for (int base = 0; base < param[0]; base++) {
			int[] wrong = new int[param.length];
			for (int j = 0; j < param.length; j++)
				wrong[j] = base;

			TestCase wrongCase = new TestCaseImplement();
			((TestCaseImplement) wrongCase).setTestCase(wrong);

			int degree = 4;
			Tuple bugMode = new Tuple(degree, wrongCase);
			for (int j = 0; j < degree; j++) {
				bugMode.set(j, j);
			}
			temp.add(bugMode);
			
			degree = 3;
			bugMode = new Tuple(degree, wrongCase);
			for (int j = 0; j < degree; j++) {
				bugMode.set(j, j+4);
			}
			temp.add(bugMode);
			
			
			degree = 3;
			bugMode = new Tuple(degree, wrongCase);
			for (int j = 0; j < degree; j++) {
				bugMode.set(j, j+4+3);
			}
			temp.add(bugMode);
			
			degree = 3;
			bugMode = new Tuple(degree, wrongCase);
			for (int j = 0; j < degree; j++) {
				bugMode.set(j, j+4+3+3);
			}
			temp.add(bugMode);
			
			degree = 3;
			bugMode = new Tuple(degree, wrongCase);
			for (int j = 0; j < degree; j++) {
				bugMode.set(j, j+4+3+3+3);
			}
			temp.add(bugMode);
			//
		}
		

		// 第10个4， 3， 3， 2， 2，2
		temp = new ArrayList<Tuple>();
		result.add(temp);

		for (int base = 0; base < param[0]; base++) {
			int[] wrong = new int[param.length];
			for (int j = 0; j < param.length; j++)
				wrong[j] = base;

			TestCase wrongCase = new TestCaseImplement();
			((TestCaseImplement) wrongCase).setTestCase(wrong);

			int degree = 4;
			Tuple bugMode = new Tuple(degree, wrongCase);
			for (int j = 0; j < degree; j++) {
				bugMode.set(j, j);
			}
			temp.add(bugMode);
			
			degree = 3;
			bugMode = new Tuple(degree, wrongCase);
			for (int j = 0; j < degree; j++) {
				bugMode.set(j, j+4);
			}
			temp.add(bugMode);
			
			
			degree = 3;
			bugMode = new Tuple(degree, wrongCase);
			for (int j = 0; j < degree; j++) {
				bugMode.set(j, j+4+3);
			}
			temp.add(bugMode);
			
			degree = 2;
			bugMode = new Tuple(degree, wrongCase);
			for (int j = 0; j < degree; j++) {
				bugMode.set(j, j+4+3+3);
			}
			temp.add(bugMode);
			
			degree = 2;
			bugMode = new Tuple(degree, wrongCase);
			for (int j = 0; j < degree; j++) {
				bugMode.set(j, j+4+3+3+2);
			}
			temp.add(bugMode);
			
			
			degree = 2;
			bugMode = new Tuple(degree, wrongCase);
			for (int j = 0; j < degree; j++) {
				bugMode.set(j, j+4+3+3+2+2);
			}
			temp.add(bugMode);
			//
		}
		
		// 第11个 3， 3，3，3，2， 2
		
		temp = new ArrayList<Tuple>();
		result.add(temp);

		for (int base = 0; base < param[0]; base++) {
			int[] wrong = new int[param.length];
			for (int j = 0; j < param.length; j++)
				wrong[j] = base;

			TestCase wrongCase = new TestCaseImplement();
			((TestCaseImplement) wrongCase).setTestCase(wrong);

			int degree = 3;
			Tuple bugMode = new Tuple(degree, wrongCase);
			for (int j = 0; j < degree; j++) {
				bugMode.set(j, j);
			}
			temp.add(bugMode);
			
			degree = 3;
			bugMode = new Tuple(degree, wrongCase);
			for (int j = 0; j < degree; j++) {
				bugMode.set(j, j+3);
			}
			temp.add(bugMode);
			
			
			degree = 3;
			bugMode = new Tuple(degree, wrongCase);
			for (int j = 0; j < degree; j++) {
				bugMode.set(j, j+3+3);
			}
			temp.add(bugMode);
			
			degree = 3;
			bugMode = new Tuple(degree, wrongCase);
			for (int j = 0; j < degree; j++) {
				bugMode.set(j, j+3+3+3);
			}
			temp.add(bugMode);
			
			degree = 2;
			bugMode = new Tuple(degree, wrongCase);
			for (int j = 0; j < degree; j++) {
				bugMode.set(j, j+3+3+3+3);
			}
			temp.add(bugMode);
			
			
			degree = 2;
			bugMode = new Tuple(degree, wrongCase);
			for (int j = 0; j < degree; j++) {
				bugMode.set(j, j+3+3+3+3+2);
			}
			temp.add(bugMode);
			//
		}

		return result;
	}

	// public int onceLoop(int n, int count, int[] wrong) {
	// for (int i = 0; i < this.param.length; i++) {
	// for (int j = i + 1; j < this.param.length; j++) {
	// TestCase wrongCase = new TestCaseImplement();
	// ((TestCaseImplement) wrongCase).setTestCase(wrong);
	// Tuple bugMode = new Tuple(2, wrongCase);
	// bugMode.set(0, i);
	// bugMode.set(1, j);
	// realMFS.add(bugMode);
	// ((CaseRunnerWithBugInject) caseRunner).inject(bugMode);
	// count++;
	// if (count >= n)
	// return count;
	// }
	// }
	// return count;
	// }

	public void setDegree(int degree) {
		// TODO Auto-generated method stub
		this.dataCenter = new DataCenter(param, degree);

	}

	public int[] getParam() {
		// TODO Auto-generated method stub
		return param;
	}

	public CaseRunner getCaseRunner() {
		// TODO Auto-generated method stub
		return caseRunner;
	}

	public List<Tuple> getRealMFS() {
		// TODO Auto-generated method stub
		return realMFS;
	}

	public DataCenter getDataCenter() {
		// TODO Auto-generated method stub
		return dataCenter;

	}

	@Override
	public void init() {
		// TODO Auto-generated method stub

	}

}
