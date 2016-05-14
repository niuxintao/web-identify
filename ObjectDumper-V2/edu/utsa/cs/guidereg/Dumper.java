package edu.utsa.cs.guidereg;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import com.google.gson.Gson;

public class Dumper {

	public static void dump(Object obj, String methodName) {
		
		try {
			File file = new File("output" + obj.getClass().getName() + methodName + ".json");
			
			System.out.println(obj.getClass().getName() + "  " + obj.toString());
			
			FileWriter output = new FileWriter(file, true);
			if (!file.exists()) {
				file.createNewFile();
			}
			
			Gson gson = new Gson(); // ?????????????????
			String objOutput = gson.toJson(obj);
			output.write(objOutput);
			output.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

//	String getObjectType(String className, String packageName) {
//		className = className.replace("Test", ""); // ??why
//		String[] split = className.split("_");
//		return packageName + "." + split[0];
//	}

	// @SuppressWarnings({ "static-access", "deprecation" })
	public static void main(String[] args) {
		// create Options object
		Options options = new Options();

		// add t option
		Option obj = new Option("o", true, "the object name");
		obj.setRequired(true);

		Option met = new Option("m", true, "the method name");
		met.setRequired(true);

		options.addOption(obj);
		options.addOption(met);

		String header = "This command is to dump the local varibles in a program when invoking is happened \n\n";
		String footer = "\nPlease report issues to xiaoyin.wang AT utsa.edu";

		HelpFormatter formatter = new HelpFormatter();
		// formatter.printHelp("myapp", header, options, footer, true);

		CommandLineParser parser = new DefaultParser();
		CommandLine cmd = null;
		try {
			cmd = parser.parse(options, args);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			formatter.printHelp("java -jar Dumper.jar ", header, options, footer, true);
			return;
		}

		// get c option value
		String objectName = cmd.getOptionValue("o");
		String methodName = cmd.getOptionValue("m");

		if (objectName == null || methodName == null) {
			formatter.printHelp("java -jar Dumper.jar ", header, options, footer, true);
		} else {

		}

//		Dumper dumper = new Dumper();
		dump(objectName, methodName);

		// use
		// System.out.println(dump.getObjectType("TestMonths_sdkhksdj",
		// "org.time"));

	}
	// DateTime date = new DateTime();
	// Property date = test.dayOfMonth();
	// System.out.println("The Hour of the day is :"+date.getHourOfDay());
	// System.out.println("The minitue of the day is
	// :"+date.getSecondOfMinute());
	// System.out.println("The day of the year is :"+date.getDayOfYear());

}
