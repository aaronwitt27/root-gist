package com.awitt.root;

import static com.awitt.root.actions.Action.DRIVER;
import static com.awitt.root.actions.Action.TRIP;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.awitt.root.actions.Action;
import com.awitt.root.model.Driver;

public class DriverTracker {
	private static final Logger LOGGER = LoggerFactory.getLogger(DriverTracker.class);

	private final Map<String, Driver> drivers;
	private final BufferedReader reader;

	public DriverTracker(final BufferedReader reader) {
		Validate.notNull(reader, "reader cannot be null");

		this.reader = reader;
		this.drivers = new HashMap<>();
	}

	public void process() throws IOException {
		final List<String> toReprocess = new ArrayList<>();

		// FIRST PASS, PROCESS ANY COMMANDS WE CAN
		this.reader.lines().forEach(line -> processLine(line, toReprocess));

		// REPROCESS UNTIL ALL COMMANDS DONE
		reprocess(toReprocess, 1);

		this.drivers.values().stream().forEach(System.out::println);
	}

	private void processLine(final String line, final List<String> toReprocess) {
		final Optional<String> sameLineNeedsToBeReprocessed = processLine(line);

		if (sameLineNeedsToBeReprocessed.isPresent()) {
			toReprocess.add(line);
		}
	}

	private Optional<String> processLine(final String line) {

		if (StringUtils.isNotBlank(line)) {
			return pickStategy(line).doCommand(line, this.drivers);
		}

		return Optional.empty();
	}

	private void reprocess(final List<String> reprocess, int count) {

		if (!reprocess.isEmpty() && count <= Action.values().length) {
			final List<String> toReprocess = new ArrayList<>();

			reprocess.stream().forEach(line -> processLine(line, toReprocess));
			reprocess(toReprocess, count++);
		}
	}

	private Action pickStategy(final String line) {

		if (DRIVER.isCommand(line)) {
			return DRIVER;
		} else if (TRIP.isCommand(line)) {
			return TRIP;
		}

		throw new IllegalArgumentException("Unexpected command: " + line);
	}
}