package output;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class OutPut {

	private String file;
	BufferedWriter outSta;

	public OutPut(String output_file) {
		file = output_file;
		try {
			outSta = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(file)));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			outSta = new BufferedWriter((new OutputStreamWriter(System.out)));
		}
	}

	public void println(String content) {
		try {
			outSta.write(content);
			outSta.newLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println(content);
			e.printStackTrace();
		}
	}
	
	public void print(String content) {
		try {
			outSta.write(content);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println(content);
			e.printStackTrace();
		}
	}
}
