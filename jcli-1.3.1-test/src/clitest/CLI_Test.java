package clitest;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

@SuppressWarnings("deprecation")
public class CLI_Test {
	public void test(boolean DefaultPareseOrNot, boolean withoutLast) {
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
				if (cl.hasOption("last"))
					System.out.println("correct");
				else
					System.out.println("incorrect");
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
			System.out.println("Confirm " + firstReal + " is set");
		}
		if (cl.getOptionValues(first).length == 2) {
			System.out.println("number of arg for " + firstReal + " is 2");
		}

		if (cl.hasOption(second)) {
			System.out.println("Confirm " + secondReal + " is set");
		}
		if (cl.getOptionValues(second).length == 1) {
			System.out.println("number of arg for " + secondReal + " is 1");
		}
	}

	public static void main(String[] args) {
		CLI_Test cli = new CLI_Test();
		cli.test(false, false);
		try {
			cli.testUnlimitedArgs(false, false, true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
