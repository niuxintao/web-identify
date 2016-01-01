package syn;


import com.fc.testObject.TestCaseImplement;

public class BenchExecute {

	int[] param;
	BasicRunner runner;

	public BenchExecute(int[] param, BasicRunner runner) {
		this.param = param;
		this.runner = runner;
	}

	public String test(int[] set) {
		TestCaseImplement testCase = new TestCaseImplement(set);
		return this.runner.runTestCase(testCase) + " ";
	}

	public void bench() {
		int[] part = new int[param.length];
		for (int i = 0; i < part.length; i++)
			part[i] = 0;
		int index = 0;
		this.testBench(param, part, index);
	}

	public void testBench(int[] set, int[] part, int index) {

		int nextIndex = index + 1;
		for (int i = 0; i < set[index]; i++) {
			int[] partCur = new int[set.length];
			System.arraycopy(part, 0, partCur, 0, set.length);
			partCur[index] = i;
			if (nextIndex == set.length) {
				String str;
				str = test(partCur);
				for (int op : partCur)
					System.out.print(op + " ");
				System.out.print(":");
				System.out.print(str + " ");
				System.out.println();

			} else {
				testBench(set, partCur, nextIndex);
			}
		}
	}
}
