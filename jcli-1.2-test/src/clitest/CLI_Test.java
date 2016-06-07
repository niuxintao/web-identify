package clitest;


import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import output.OutputSet;

public class CLI_Test {

	@SuppressWarnings("static-access")
	public void nonsenseParamters(boolean argMutiple, boolean optionMutiple,
			boolean getArgOrNot) {

		String[] args = null;
		if (argMutiple)
			args = new String[] { "--a" };
		else
			args = new String[] { "--a", "--a" };

		Options options = new Options();
		if (optionMutiple)
			options.addOption(OptionBuilder.withLongOpt("ab").create());
		options.addOption(OptionBuilder.withLongOpt("a").create());
		CommandLineParser parser = new GnuParser();
		try {
			CommandLine cl = parser.parse(options, args);
			if (getArgOrNot)
				cl.getArgList();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void testMissingRequired(boolean isRequired, boolean supplyOrNot)
			throws Exception {

		String[] args = null;

		if (supplyOrNot)
			args = new String[] {};
		else
			args = new String[] { "-f" };

		Options options = new Options();
		options.addOption("f", "foo", false, "");

		if (isRequired)
			options.getOption("f").setRequired(true);
		else
			options.getOption("f").setRequired(false);

		GnuParser parser = new GnuParser();
		try {
			parser.parse(options, args);
			if (isRequired && !supplyOrNot)
				throw new Exception(
						"an exception is expected, but actually not triggered");

		} catch (ParseException e) {
			// expected to get MissingOptionException here
			e.printStackTrace();
		}
	}

	@SuppressWarnings("static-access")
	public boolean testQuotes(boolean BasicPareseOrNot, boolean addtionalSpace,
			boolean doubleQuotes) {

		String[] args = null;
		if (!addtionalSpace) {
			String ar = "";
			if (doubleQuotes)
				ar = "fname=\"vishal\" and lname=\"dhawani\"";
			else
				ar = "fname=\'vishal\' and lname=\'dhawani\'";

			args = new String[] { "Hello", "-e", ar };
		} else {
			String ar = "";
			if (doubleQuotes)
				ar = "fname=\"vishal\" and lname=\"dhawani\" ";
			else
				ar = "fname=\'vishal\' and lname=\'dhawani\' ";

			args = new String[] { "Hello", "-e", ar };
		}

		Option opt1 = OptionBuilder.hasArg(true).create("e");
		Options opts = new Options();
		opts.addOption(opt1);

		CommandLineParser parser = null;

		if (BasicPareseOrNot)
			parser = new BasicParser();
		else
			parser = new GnuParser();

		CommandLine cl = null;
		try {
			cl = parser.parse(opts, args);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (doubleQuotes)
			return cl.getOptionValue("e").contains("\"dhawani\"");
		else
			return cl.getOptionValue("e").contains("\'dhawani\'");

	}

	public String test(int[] set) throws Exception {
		nonsenseParamters(set[0] == 0 ? false : true, set[1] == 0 ? false
				: true, set[2] == 0 ? false : true);

		testMissingRequired(set[3] == 0 ? false : true, set[4] == 0 ? false
				: true);

		if (testQuotes(set[5] == 0 ? false : true, set[6] == 0 ? false : true,
				set[7] == 0 ? false : true)) {
			throw new Exception("Quoted string parsing Error");
		}

		return OutputSet.PASS;
	}

	public static void main(String[] argv) throws Exception {
		CLI_Test ct = new CLI_Test();

		ct.nonsenseParamters(true, true, true);

		ct.testMissingRequired(true, false);
		// only (-, false, true) will print the false
		ct.testQuotes(false, false, false);

	}
}