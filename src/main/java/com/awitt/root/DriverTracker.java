package com.awitt.root;

import static com.awitt.root.actions.Action.DRIVER;
import static com.awitt.root.actions.Action.TRIP;

import java.io.BufferedReader;
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
import com.awitt.root.model.Trip;

/**
 * Stateful class that processes a file of ROOT {@link Action driver metric
 * commands}.
 * <p>
 * Each line is processed sequentially, with each {@link Action} encapsulating
 * the logic necessary to perform its function. If an Action cannot perform its
 * function when called, it returns the same line so that it can be reprocessed.
 * <p>
 * Reprocessing occurs for as long as there are lines to be reprocessed and as
 * long as reprocessing has not occurred more times than there are Actions.
 * <p>
 * Once the file has been completely processed, each {@link Driver}'s
 * {@link Trip}s are validated and aggregated, resulting in a summary of each
 * Driver's total distance driven and their average speed, ordered by total
 * distance.
 */
public class DriverTracker {
	private static final Logger LOGGER = LoggerFactory.getLogger(DriverTracker.class);

	/**
	 * Map of lower-cased {@link Driver#getName() driver name} and
	 * {@link Driver}
	 **/
	private final Map<String, Driver> drivers;

	/** The file to be processed **/
	private final BufferedReader reader;

	/**
	 * Instantiates this class to process a file of Driver data.
	 * 
	 * @param reader
	 *            the {@link BufferedReader} containing a handle to the Driver
	 *            data file to be processed
	 */
	public DriverTracker(final BufferedReader reader) {
		Validate.notNull(reader, "reader cannot be null");

		this.reader = reader;
		this.drivers = new HashMap<>();
	}

	/**
	 * Main method of this class that completely processes the Driver data file,
	 * resulting in a summary of {@link Driver} {@link Trip}s.
	 */
	public void process() {
		final List<String> toReprocess = new ArrayList<>();

		// FIRST PASS, PROCESS ANY COMMANDS WE CAN
		this.reader.lines().forEach(line -> processLine(line, toReprocess));

		// REPROCESS UNTIL ALL COMMANDS DONE
		reprocess(toReprocess, 1);

		this.drivers.values().stream().map(Driver::aggregateTrips).sorted()
				.forEach(driver -> LOGGER.info(driver.getName() + ": " + driver.getDrivingSummary().toString()));
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