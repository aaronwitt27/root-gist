package com.awitt.root.model;

import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * POJO representing a driving Trip.
 */
public class Trip {

	private final LocalTime start;
	private final LocalTime end;
	private final double distance;
	private double averageSpeed;

	/**
	 * Creates a new instance of this class from the provided parameters,
	 * parsing their String representations to their corresponding
	 * strongly-typed class member instances.
	 * 
	 * @param start
	 *            the time at which the trip started; in format {@code hh:mm};
	 *            cannot be after or same as {@code end}
	 * @param end
	 *            the time at which the trip ended; in format {@code hh:mm};
	 *            cannot be before or same as {@code start}
	 * @param distance
	 *            the distance of the trip; in Miles
	 * @throws DateTimeParseException
	 *             if something goes wrong while parsing {@code start} or
	 *             {@code end} to an instance of {@link LocalTime}
	 * @throws NumberFormatException
	 *             if distance is not a valid double
	 */
	public Trip(final String start, final String end, final String distance) {

		this.start = parseTime(start);
		this.end = parseTime(end);
		this.distance = parseDistance(distance);
		this.averageSpeed = calculateAverageSpeed();
	}

	private LocalTime parseTime(final String time) {
		assert StringUtils.isNotBlank(time) : "time cannot be blank";
		return LocalTime.parse(time);
	}

	private Double parseDistance(final String distance) {
		assert StringUtils.isNotBlank(distance) : "distance cannot be blank";
		return Double.valueOf(distance);
	}

	/**
	 * Checks all requirements for a Trip to be valid. Currently, any Trips
	 * which have average speeds outside the range {@code [5,100]} mph are
	 * invalid.
	 * 
	 * @return a boolean value indicating whether or not this Trip meets the
	 *         requirements for a valid Trip
	 */
	public boolean meetsTripRequirements() {
		return meetsAverageSpeedThreshold();
	}

	private boolean meetsAverageSpeedThreshold() {
		return Double.compare(this.averageSpeed, 5.0) >= 0 && Double.compare(this.averageSpeed, 100.0) <= 0;
	}

	public double getAverageSpeed() {
		return this.averageSpeed;
	}

	private double calculateAverageSpeed() {
		return this.distance / (Duration.between(this.start, this.end).getSeconds() / 60.0 / 60.0);
	}

	public LocalTime getStart() {
		return this.start;
	}

	public LocalTime getEnd() {
		return this.end;
	}

	public double getDistance() {
		return this.distance;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(this.distance);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((this.end == null) ? 0 : this.end.hashCode());
		result = prime * result + ((this.start == null) ? 0 : this.start.hashCode());
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
		Trip other = (Trip) obj;
		if (Double.doubleToLongBits(this.distance) != Double.doubleToLongBits(other.distance))
			return false;
		if (this.end == null) {
			if (other.end != null)
				return false;
		} else if (!this.end.equals(other.end))
			return false;
		if (this.start == null) {
			if (other.start != null)
				return false;
		} else if (!this.start.equals(other.start))
			return false;
		return true;
	}

	@Override
	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this, ToStringStyle.JSON_STYLE);
		builder.append("start", this.start).append("end", this.end).append("distance", this.distance)
				.append("averageSpeed", this.averageSpeed);
		return builder.build();
	}
}