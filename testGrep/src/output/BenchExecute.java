package output;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

public class BenchExecute   {
	
	public BenchExecute(){
		this.outputRecord = new OutputSet();
	}


	private OutputSet outputRecord;


	public String test(int[] set) {
		return OutputSet.PASS;
	}

	public void bench(int[] param) {
		int[] part = new int[param.length];
		for (int i = 0; i < part.length; i++)
			part[i] = 0;
		int index = 0;

		try {
			FileWriter fw = new FileWriter("result_of_testCase.txt");
			BufferedWriter bw = new BufferedWriter(fw);
			try {
				this.testBench(param, part, index, bw);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			bw.flush();
			bw.close();
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void testBench(int[] set, int[] part, int index, BufferedWriter bw) {
		int nextIndex = index + 1;
		for (int i = 0; i < set[index]; i++) {
			int[] partCur = new int[set.length];
			System.arraycopy(part, 0, partCur, 0, set.length);
			partCur[index] = i;
			if (nextIndex == set.length) {
				String str;
				try {
					str = test(partCur);
					this.outputRecord.pendingStr(str);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					Writer writer = new StringWriter();
					PrintWriter printWriter = new PrintWriter(writer);
					e.printStackTrace(printWriter);
					String s = writer.toString();
					this.outputRecord.pendingStr(s);
					str = s;
				}
				try {

					for (int op : partCur)
						bw.write(op + " ");
					bw.write(":");
					bw.write(this.outputRecord.get(str) + " ");
					bw.newLine();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} else {
				testBench(set, partCur, nextIndex, bw);
			}
		}
	}

	public void showResult() {
		try {
			FileWriter fw = new FileWriter("bugInfo.txt");
			BufferedWriter bw = new BufferedWriter(fw);
			for (int i = 0; i < this.outputRecord.getCurIndex(); i++) {
				bw.write(i + " : " + this.outputRecord.get(i));
				bw.newLine();
			}
			bw.flush();
			bw.close();
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] argv) {
		BenchExecute tj = new BenchExecute();
		int[] param = new int[] { 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 4, 3 };
		tj.bench(param);
		tj.showResult();

		// tj.testBench(set, part, 0);
	}
}
