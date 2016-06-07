package clitest;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import output.OutputSet;

@SuppressWarnings("deprecation")
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

	public void test(boolean DefaultPareseOrNot, boolean withoutLast)
			throws Exception {
		Option TYPE1 = Option.builder("t1").hasArg().numberOfArgs(1)
				.optionalArg(true).argName("t1_path").build();
		Option TYPE2 = Option.builder("t2").hasArg().numberOfArgs(1)
				.optionalArg(true).argName("t2_path").build();
		Option LAST = Option.builder("last").hasArg(false).build();

		Options options = new Options();
		options.addOption(TYPE1);
		options.addOption(TYPE2);
		options.addOption(LAST);

		String[] args = null;
		if (!withoutLast)
			args = new String[] { "open", "-t1", "-last" };
		else
			args = new String[] { "open", "-t1", "path/to/my/db" };

		CommandLineParser parser = null;

		if (DefaultPareseOrNot)
			parser = new DefaultParser();
		else
			parser = new GnuParser();
		try {
			CommandLine cl = parser.parse(options, args);
			if (!withoutLast)
				if (!cl.hasOption("last"))
					throw new Exception(
							"incorrect, cannot find the option which is last");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// longnameOrNot = true . exception
	// isDash = true exception, which is constraint.
	public void testUnlimitedArgs(boolean havelistOrnot, boolean longnameOrnot,
			boolean isDash) throws Exception {
		String first = "";
		String second = "";
		// if (isDash)
		// first += "-";
		if (longnameOrnot) {
			first += "unlimitedOne";
			second += "unlimitedTwo";
		} else {
			first += "a";
			second += "b";
		}

		String firstReal = "";
		String secondReal = "";
		if (isDash) {
			firstReal += "-";
			secondReal += "-";
		}
		firstReal += first;
		secondReal += second;

		String[] args = null;
		if (havelistOrnot)
			args = new String[] { firstReal, "one", "two", secondReal, "alpha" };
		else
			args = new String[] { firstReal, "one", secondReal, "two" };

		Options options = new Options();
		options.addOption(Option.builder(first).hasArgs().build());
		options.addOption(Option.builder(second).hasArgs().build());

		CommandLineParser parser = new DefaultParser();
		CommandLine cl = parser.parse(options, args);

		if (cl.hasOption(first)) {
			if (cl.getOptionValues(first).length != 2) {
				throw new Exception("number of arg for " + firstReal
						+ " should be 2");
			}
		}

		if (cl.hasOption(second)) {
			if (cl.getOptionValues(second).length != 1) {
				throw new Exception("number of arg for " + secondReal
						+ " should be 1");
			}
		}

	}

	public String test(int[] set) throws Exception {
		nonsenseParamters(set[0] == 0 ? false : true, set[1] == 0 ? false
				: true, set[2] == 0 ? false : true);

		test(set[3] == 0 ? false : true, set[4] == 0 ? false : true);

		testUnlimitedArgs(set[5] == 0 ? false : true, set[6] == 0 ? false
				: true, set[7] == 0 ? false : true);

		return OutputSet.PASS;
	}

	public static void main(String[] args) {
		CLI_Test cli = new CLI_Test();
		try {
			cli.test(true, false);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			cli.testUnlimitedArgs(false, false, false);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
