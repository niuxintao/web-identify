package org.jflex.test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Setup {
	private String base;
	private String outPath;

	public Setup(String inputFile, String outFile) throws IOException {
		FileInputStream fstream = new FileInputStream(inputFile);
		// Get the object of DataInputStream
		DataInputStream in = new DataInputStream(fstream);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String temp = null;

		StringBuffer sb = new StringBuffer();
		temp = br.readLine();
		while (temp != null) {
			sb.append(temp + "\n");
			temp = br.readLine();
		}
		br.close();
		base = sb.toString();

		outPath = outFile;
	}

	public void removeAll(List<String> strs) throws IOException {
		base = this.removeALL(strs, base);
		// BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
		// new FileOutputStream(outPath)));
		// out.write(output);
		// out.close();
	}

	public void appendAll(List<String> strs) throws IOException {
		String output = this.appendAll(strs, base);

		File file = new File(outPath);
		if (!file.exists())
			file.createNewFile();

		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(file,false)));
		out.write(output);
		out.close();
	}

	public boolean find(String patternStr, String base) {
		Pattern pattern = Pattern.compile(patternStr);
		Matcher matcher = pattern.matcher(base);
		return matcher.find();
	}

	public boolean find(String patternStr) {
		Pattern pattern = Pattern.compile(patternStr);
		Matcher matcher = pattern.matcher(base);
		return matcher.find();
	}
	
	public String replace(String patternStr, String replace, String base) {
		Pattern pattern = Pattern.compile(patternStr);
		Matcher matcher = pattern.matcher(base);
		String str =matcher.replaceAll(replace);
//		System.out.println(str);
		return str;
	}

	public String removeALL(List<String> strs, String base) {
		for (String str : strs) {
			base = this.replace(str, "", base);
		}
//		System.out.println(base);
		return base;
	}

	public String appendAll(List<String> strs, String base) {
		for (String str : strs) {
			if (!this.find(str, base)) {
				base = str + "\n" + base;
			}
		}
		return base;
	}

	public static void main(String[] args) throws IOException {
		Setup set = new Setup("", "");
		System.out.println(set.find("Java", "Java不是人是什么\nJava"));
		System.out.println(set.replace("Java", "cao", "Java不是人\nJava"));
	}
}
