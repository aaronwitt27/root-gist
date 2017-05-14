package com.awitt.root.model;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class DrivingSummary {

	public static final int MIN_AVERAGE_SPEED = 5;
	public static final int MAX_AVERAGE_SPEED = 100;

	private static final DrivingSummary NO_DATA = new DrivingSummary(0, 0);

	private final int distance;
	private final int averageSpeed;

	private DrivingSummary(final int distance, final int averageSpeed) {

		assert distance >= 0 : "distance cannot be negative";
		assert averageSpeed >= 0 : "averageSpeed cannot be negative";

		this.distance = distance;
		this.averageSpeed = averageSpeed;
	}

	public static DrivingSummary noData() {
		return NO_DATA;
	}

	public static DrivingSummary withMetrics(final int distance, final int averageSpeed) {

		Validate.validState(distance >= 0, "distance cannot be negative");
		Validate.inclusiveBetween(MIN_AVERAGE_SPEED, MAX_AVERAGE_SPEED, averageSpeed,
				"averageSpeed must be between [%d,%d]", MIN_AVERAGE_SPEED, MAX_AVERAGE_SPEED);

		return new DrivingSummary(distance, averageSpeed);
	}

	public int getDistance() {
		return this.distance;
	}

	public int getAverageSpeed() {
		return this.averageSpeed;
	}

	@Override
	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this, ToStringStyle.JSON_STYLE);
		builder.append("distance", this.distance).append("averageSpeed", this.averageSpeed);
		return builder.build();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + this.averageSpeed;
		result = prime * result + this.distance;
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
		if (this.averageSpeed != other.averageSpeed)
			return false;
		if (this.distance != other.distance)
			return false;
		return true;
	}
}