package com.awitt.root.model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * POJO representing a Root Driver and their associated metrics.
 */
public class Driver implements Comparator<Driver> {

	private final String name;
	private final List<Trip> trips;
	private DrivingSummary drivingSummary;

	public Driver(final String name) {
		Validate.notBlank(name, "name cannot be blank");

		this.name = name;
		this.trips = new ArrayList<>();
		this.drivingSummary = DrivingSummary.noData();
	}

	public DrivingSummary getDrivingSummary() {
		return this.drivingSummary;
	}

	public void setDrivingSummary(final DrivingSummary drivingSummary) {
		Validate.notNull(drivingSummary, "drivingSummary cannot be null");
		this.drivingSummary = drivingSummary;
	}

	public String getName() {
		return this.name;
	}

	public List<Trip> getTrips() {
		return this.trips;
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

	@Override
	public int compare(final Driver driver1, final Driver driver2) {

		if (driver1 == null && driver2 == null) {
			return 0;
		} else if (driver1 == null) {
			return -1;
		} else if (driver2 == null) {
			return 1;
		}

		return Integer.compare(driver1.getDrivingSummary().getDistance(), driver2.getDrivingSummary().getDistance());
	}
}