package com.awitt.root.model;

import java.time.LocalTime;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * POJO representing a driving Trip.
 */
public class Trip {

	private LocalTime start;
	private LocalTime end;
	private double distance;

	public Trip(final String start, final String end, final String distance) {

		this.start = parseTime(start);
		this.end = parseTime(end);
		this.distance = parseDistance(distance);
	}

	private LocalTime parseTime(final String time) {
		assert StringUtils.isNotBlank(time) : "time cannot be blank";
		return LocalTime.parse(time);
	}

	private Double parseDistance(final String distance) {
		assert StringUtils.isNotBlank(distance) : "distance cannot be blank";
		return Double.valueOf(distance);
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
		builder.append("start", this.start).append("end", this.end).append("distance", this.distance);
		return builder.build();
	}
}