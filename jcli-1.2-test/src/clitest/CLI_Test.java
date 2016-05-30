package clitest;

import java.util.List;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class CLI_Test {

	public void test(boolean BasicPareseOrNot, boolean addtionalSpace,
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
			System.out.println(cl.getOptionValue("e").contains("\"dhawani\""));
		else
			System.out.println(cl.getOptionValue("e").contains("\'dhawani\'"));

	}

	public void test2(String[] argv) {
		Option opt_foo = OptionBuilder.hasArg(false).isRequired(true)
				.withDescription("option foo").create("foo");

		Option opt_bar = OptionBuilder.hasArg(false).isRequired(false)
				.withDescription("option bar").create("bar");

		Option opt_baz = OptionBuilder.hasArg(false).isRequired(false)
				.withDescription("option baz").create("baz");

		OptionGroup optgrp = new OptionGroup();
		optgrp.setRequired(true);
		optgrp.addOption(opt_bar).addOption(opt_baz);

		Options optsdef = new Options();
		optsdef.addOption(opt_foo).addOptionGroup(optgrp);

		try {
			CommandLineParser parser = new GnuParser();
			CommandLine cmdline = parser.parse(optsdef, argv);
		}

		/**
		 * 
		 * this is where the exception happens
		 * */
		catch (MissingOptionException ex) {
			List opts = ex.getMissingOptions();

			for (Object option : opts) {
				System.out.println("OPT: " + option.getClass().getName());
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			System.exit(1);
		}
	}

	public static void main(String[] argv) {
		CLI_Test ct = new CLI_Test();
		
		
		
		ct.test2(argv);
		
		//only (-, false, true) will print the false
		ct.test(false, false, true);
		
	
	}
}