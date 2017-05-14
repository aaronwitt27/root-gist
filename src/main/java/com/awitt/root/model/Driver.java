package com.awitt.root.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * POJO representing a Root Driver and their associated metrics.
 */
public class Driver implements Comparable<Driver> {

	private final String name;
	private final List<Trip> trips;
	private DrivingSummary drivingSummary;

	/**
	 * Creates a new instance of this class.
	 * 
	 * @param name
	 *            the name of the Driver
	 */
	public Driver(final String name) {
		Validate.notBlank(name, "name cannot be blank");

		this.name = name;
		this.trips = new ArrayList<>();
		this.drivingSummary = new DrivingSummary();
	}

	/**
	 * This method filters out all {@link Trip trips} that
	 * {@link Trip#meetsTripRequirements() do not meet} Trip requirements and
	 * then adds each valid Trip's info the this Driver's
	 * {@link DrivingSummary}.
	 * 
	 * @return {@code this} {@link Driver}, for chaining
	 */
	public Driver aggregateTrips() {

		// RESET PREVIOUSLY-AGGREGATED DATA BEFORE RE-AGGREGATING
		if (!this.drivingSummary.equals(DrivingSummary.NO_DATA)) {
			this.drivingSummary = new DrivingSummary();
		}

		// FILTER OUT INVALID TRIPS AND ADD THE OTHERS' INFO TO THE SUMMARY
		this.trips.stream().filter(Trip::meetsTripRequirements).forEach(this.drivingSummary::addTripInfo);

		return this;
	}

	public List<Trip> getTrips() {
		return this.trips;
	}

	public DrivingSummary getDrivingSummary() {
		return this.drivingSummary;
	}

	public String getName() {
		return this.name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.drivingSummary == null) ? 0 : this.drivingSummary.hashCode());
		result = prime * result + ((this.name == null) ? 0 : this.name.hashCode());
		result = prime * result + ((this.trips == null) ? 0 : this.trips.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Driver other = (Driver) obj;
		if (this.drivingSummary == null) {
			if (other.drivingSummary != null)
				return false;
		} else if (!this.drivingSummary.equals(other.drivingSummary))
			return false;
		if (this.name == null) {
			if (other.name != null)
				return false;
		} else if (!this.name.equals(other.name))
			return false;
		if (this.trips == null) {
			if (other.trips != null)
				return false;
		} else if (!this.trips.equals(other.trips))
			return false;
		return true;
	}

	@Override
	public String toString() {
		final int maxLen = 10;
		ToStringBuilder builder = new ToStringBuilder(this, ToStringStyle.JSON_STYLE);
		builder.append("name", this.name)
				.append("trips", this.trips != null ? this.trips.subList(0, Math.min(this.trips.size(), maxLen)) : null)
				.append("drivingSummary", this.drivingSummary);
		return builder.build();
	}

	/**
	 * Defines natural ordering of this class as the total distance. If this
	 * driver's {@link DrivingSummary#getTotalDistance() total distance} is
	 * greater than the {@code other}'s, {@code 1} is returned; if less,
	 * {@code -1} is returned; and if equal, {@code 0} is returned.
	 * 
	 * @param otherDriver
	 *            the other {@link Driver} against which to compare
	 */
	@Override
	public int compareTo(final Driver otherDriver) {
		return -1 * ((otherDriver == null) ? 1
				: (Integer.compare((int) Math.rint(this.getDrivingSummary().getTotalDistance()),
						(int) Math.rint(otherDriver.getDrivingSummary().getTotalDistance()))));
	}
}