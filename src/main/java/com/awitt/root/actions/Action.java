package com.awitt.root.actions;

import java.time.format.DateTimeParseException;
import java.util.EnumSet;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.awitt.root.DriverTracker;
import com.awitt.root.model.Driver;
import com.awitt.root.model.Trip;

/**
 * All possible actions that can be processed by {@link DriverTracker}.
 */
public enum Action {

	/** Registers a driver in the application **/
	DRIVER("Driver") {
		@Override
		public Optional<String> doCommand(final String line, final Map<String, Driver> drivers) {
			Validate.notBlank(line, "line cannot be blank");
			Validate.notNull(drivers, "drivers cannot be null");

			final String name = line.substring(line.indexOf(" "));

			if (drivers.get(name.toLowerCase()) == null) {
				LOGGER.info("registering {}", name);
				drivers.put(name.toLowerCase(), new Driver(name));
			} else {
				LOGGER.warn("driver already registered: {}", name);
			}

			return Optional.empty();
		}
	},

	/** Records a trip attributed to a registered driver **/
	TRIP("Trip") {
		@Override
		public Optional<String> doCommand(final String line, final Map<String, Driver> drivers) {
			Validate.notBlank(line, "line cannot be blank");
			Validate.notNull(drivers, "drivers cannot be null");

			final String[] metrics = line.split(" ");

			if (metrics.length != 5) {
				throw new RuntimeException("Trip data in an unexpected format: " + line);
			}

			final String name = metrics[1];
			final Optional<Driver> driver = getDriver(name, drivers);

			if (!driver.isPresent()) {
				return Optional.of(line);
			}

			final String start = metrics[2];
			final String end = metrics[3];
			final String distance = metrics[4];

			try {
				driver.get().getTrips().add(new Trip(start, end, distance));
			} catch (DateTimeParseException | NumberFormatException e) {
				throw new RuntimeException("Trip data in an unexpected format: " + line, e);
			}

			return Optional.empty();
		}
	};

	private static final Logger LOGGER = LoggerFactory.getLogger(Action.class);

	private final String command;

	public abstract Optional<String> doCommand(String line, Map<String, Driver> drivers);

	private Action(final String command) {
		assert StringUtils.isNotBlank(command) : "command cannot be blank";
		this.command = command;
	}

	public boolean isCommand(final String line) {
		return StringUtils.isNotBlank(line) && line.split(" ")[0].equalsIgnoreCase(getCommand());
	}

	public String getCommand() {
		return this.command;
	}

	public static Optional<Action> fromString(final String metricAction) {
		return EnumSet.allOf(Action.class).stream().filter(action -> action.getCommand().equalsIgnoreCase(metricAction))
				.findFirst();
	}

	public Optional<Driver> getDriver(final String name, final Map<String, Driver> drivers) {

		assert StringUtils.isNotBlank(name) : "name cannot be blank";
		assert drivers != null : "drivers cannot be null";

		return Optional.ofNullable(drivers.get(name.toLowerCase()));
	}
}