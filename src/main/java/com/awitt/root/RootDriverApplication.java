package com.awitt.root;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RootDriverApplication {

	private static final Logger LOGGER = LoggerFactory.getLogger(RootDriverApplication.class);
	private static final String OPT_FILE_PATH_SHORT = "f";
	private static final String OPT_FILE_PATH_LONG = "file";

	public static void main(String[] args) {
		final String file;

		try {
			file = getCommandLineArgs(args).getOptionValue(OPT_FILE_PATH_SHORT);
		} catch (ParseException e) {
			LOGGER.error("An exception occurred while attempting to parse command line options: {}", args, e);
			System.exit(1);
			return;
		}

		LOGGER.info("loading {}", file);
	}

	private static CommandLine getCommandLineArgs(final String[] args) throws ParseException {
		return new DefaultParser().parse(buildOptions(), args);
	}

	private static Options buildOptions() {
		return new Options().addOption(Option.builder(OPT_FILE_PATH_SHORT).longOpt(OPT_FILE_PATH_LONG)
				.desc("the path to the file containing driver data to be processed").hasArg().numberOfArgs(1).build());
	}
}