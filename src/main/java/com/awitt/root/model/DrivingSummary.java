package com.awitt.root.model;

import java.time.Duration;

import org.apache.commons.lang3.Validate;

/**
 * POJO representing a summary of a {@link Driver}'s {@link Trip}'s.
 */
public class DrivingSummary {

	/** The minimum average speed of a valid Trip **/
	public static final int MIN_AVERAGE_SPEED = 5;

	/** The maximum average speed of a valid Trip **/
	public static final int MAX_AVERAGE_SPEED = 100;

	/**
	 * A newly initialized instance of {@link DrivingSummary} with values set to
	 * defaults
	 **/
	public static final DrivingSummary NO_DATA = new DrivingSummary();

	private double totalDistance;
	private long totalDuration;
	private long averageSpeed;

	public DrivingSummary() {
		this.averageSpeed = -1;
		this.totalDistance = 0;
		this.totalDuration = 0;
	}

	/**
	 * Adds a {@link Trip}'s info to this summary.
	 * 
	 * @param trip
	 *            the Trip to add to this summary
	 */
	public void addTripInfo(final Trip trip) {
		Validate.notNull(trip, "trip cannot be null");

		addDistance(trip.getDistance());
		addDuration(Duration.between(trip.getStart(), trip.getEnd()).getSeconds());
	}

	private void addDistance(final double distance) {
		this.totalDistance += distance;
	}

	private void addDuration(final long duration) {
		this.totalDuration += duration;
	}

	private long calculateAverageSpeed() {
		return Math.round(this.totalDistance / (this.totalDuration / 60.0 / 60.0));
	}

	/**
	 * @return the total distance traveled by this {@link Driver} in miles
	 */
	public double getTotalDistance() {
		return this.totalDistance;
	}

	/**
	 * @return the total number of seconds traveled by this {@link Driver}
	 */
	public long getTotalDuration() {
		return this.totalDuration;
	}

	/**
	 * @return the average speed, in miles per hour, of this {@link Driver}
	 *         across all valid {@link Trip}s
	 */
	public long getAverageSpeed() {

		if (this.averageSpeed == -1) {
			this.averageSpeed = calculateAverageSpeed();
		}

		return this.averageSpeed;
	}

	@Override
	public String toString() {
		final long totalDistance = Math.round(this.totalDistance);

		if (totalDistance == 0) {
			return String.format("%s miles", totalDistance);
		}

		return String.format("%s miles @ %s mph", totalDistance, getAverageSpeed());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(this.totalDistance);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + (int) (this.totalDuration ^ (this.totalDuration >>> 32));
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
		DrivingSummary other = (DrivingSummary) obj;
		if (Double.doubleToLongBits(this.totalDistance) != Double.doubleToLongBits(other.totalDistance))
			return false;
		if (this.totalDuration != other.totalDuration)
			return false;
		return true;
	}
}