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

			final String name = line.substring(line.indexOf(" ") + 1);

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
				LOGGER.info("driver not registered yet: {}", name);
				return Optional.of(line);
			}

			final String start = metrics[2];
			final String end = metrics[3];
			final String distance = metrics[4];

			try {
				final Trip trip = new Trip(start, end, distance);
				LOGGER.info("adding new trip for {}: {}", name, trip);
				driver.get().getTrips().add(trip);
			} catch (DateTimeParseException | NumberFormatException e) {
				throw new RuntimeException("Trip data in an unexpected format: " + line, e);
			}

			return Optional.empty();
		}
	};

	private static final Logger LOGGER = LoggerFactory.getLogger(Action.class);

	private final String command;

	/**
	 * Performs the logic necessary for this Action's function. If the function
	 * cannot be performed, the provided {@code line} should be returned as an
	 * {@link Optional}, otherwise an {@link Optional#empty() empty optional}
	 * should be returned by the implementation.
	 **/
	public abstract Optional<String> doCommand(String line, Map<String, Driver> drivers);

	private Action(final String command) {
		assert StringUtils.isNotBlank(command) : "command cannot be blank";
		this.command = command;
	}

	/**
	 * @param line
	 *            a line of data to be processed
	 * @return whether or not this {@code line} is to be processed by this
	 *         {@link Action}
	 */
	public boolean isCommand(final String line) {
		return StringUtils.isNotBlank(line) && line.split(" ")[0].equalsIgnoreCase(getCommand());
	}

	/**
	 * @return the String representation of this {@link Action} mirroring the
	 *         command text in a ROOT Driver metric file
	 */
	public String getCommand() {
		return this.command;
	}

	/**
	 * @param metricAction
	 *            the string from which to get the corresponding {@link Action}
	 * @return the {@link Action} corresponding to the provided
	 *         {@code metricAction}
	 */
	public static Optional<Action> fromString(final String metricAction) {
		return EnumSet.allOf(Action.class).stream().filter(action -> action.getCommand().equalsIgnoreCase(metricAction))
				.findFirst();
	}

	/**
	 * @param name
	 *            the name of the {@link Driver} to retrieve
	 * @param drivers
	 *            a {@link Map} of Drivers
	 * @return an {@link Optional} of the {@link Driver}
	 */
	public Optional<Driver> getDriver(final String name, final Map<String, Driver> drivers) {

		assert StringUtils.isNotBlank(name) : "name cannot be blank";
		assert drivers != null : "drivers cannot be null";

		return Optional.ofNullable(drivers.get(name.toLowerCase()));
	}
}