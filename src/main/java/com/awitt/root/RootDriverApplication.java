package com.awitt.root;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

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
			file = parseCommandLineArgs(args).getOptionValue(OPT_FILE_PATH_SHORT);
			LOGGER.info("loading {}", file);
		} catch (ParseException e) {
			LOGGER.error("An exception occurred while attempting to parse command line options: {}", args, e);
			System.exit(1);
			return;
		}

		try (final BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
			final DriverTracker tracker = new DriverTracker(bufferedReader);
			tracker.doThing();

			// TODO:
		} catch (IOException e) {
			LOGGER.error("An exception occurred while attempting to load file", e);
			System.exit(1);
			return;
		}

		System.exit(0);
	}

	private static CommandLine parseCommandLineArgs(final String[] args) throws ParseException {
		return new DefaultParser().parse(buildOptions(), args);
	}

	private static Options buildOptions() {
		return new Options().addOption(Option.builder(OPT_FILE_PATH_SHORT).longOpt(OPT_FILE_PATH_LONG)
				.desc("the path to the file containing driver data to be processed").hasArg().numberOfArgs(1).build());
	}
}