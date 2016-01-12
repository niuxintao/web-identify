package runGrep;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

public class CMD {
	public static String execute(String[] cmd) {
		String result = "";
		try {

			Process ps = Runtime.getRuntime().exec(cmd);

			BufferedReader br = new BufferedReader(new InputStreamReader(
					ps.getInputStream()));
			StringBuffer sb = new StringBuffer();
			String line;
			while ((line = br.readLine()) != null) {
				sb.append(line).append("\n");
			}
			result = sb.toString();

//			System.out.println(result);
		} catch (Exception e) {
			Writer writer = new StringWriter();
			PrintWriter printWriter = new PrintWriter(writer);
			e.printStackTrace(printWriter);
			String s = writer.toString();
			result = s;
			// e.printStackTrace();
		}

		return result;

	}

	public static String execute(String cmd) {
		String[] cmdCompleted = { "/bin/sh", "-c", cmd };
		return execute(cmdCompleted);
	}

	public static void main(String[] str) {
		String[] cmd = new String[] { "/bin/sh", "-c", "ls -l" };
		CMD.execute(cmd);
	}
}
