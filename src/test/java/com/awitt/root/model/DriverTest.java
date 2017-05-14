package com.awitt.root.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class DriverTest {

	@Test(expected = NullPointerException.class)
	public void driver_NullName_NPE() {
		new Driver(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void driver_BlankName_NPE() {
		new Driver("");
	}

	@Test
	public void driver_ValidName_DriverCreatedWithName() {

		final String name = "irving washington";

		final Driver driver = new Driver(name);

		assertEquals(name, driver.getName());
	}

	@Test
	public void aggregateTrips_TwoValidTrips_ValidSummary() {

		final Trip trip1 = new Trip("07:15", "07:45", "17.3");
		final Trip trip2 = new Trip("06:12", "06:32", "21.8");

		final Driver driver = new Driver("washington irving");
		driver.getTrips().add(trip1);
		driver.getTrips().add(trip2);

		final Driver sameDriver = driver.aggregateTrips();
		final DrivingSummary summary = driver.getDrivingSummary();

		assertEquals(driver, sameDriver);
		assertNotNull(summary);
		assertEquals(47, summary.getAverageSpeed());
		assertEquals(39.1, summary.getTotalDistance(), 0.01);
		assertEquals(3000, summary.getTotalDuration());
	}

	@Test
	public void aggregateTrips_TwoValidTripsTwoInvalidTrips_ValidSummary() {

		final Trip trip1 = new Trip("07:15", "07:45", "17.3");
		final Trip trip2 = new Trip("06:12", "06:32", "21.8");
		final Trip tripTooSlow = new Trip("00:12", "06:32", "21.8");
		final Trip tripTooFast = new Trip("06:12", "06:32", "1021.8");

		final Driver driver = new Driver("washington irving");
		driver.getTrips().add(trip1);
		driver.getTrips().add(trip2);
		driver.getTrips().add(tripTooSlow);
		driver.getTrips().add(tripTooFast);

		final Driver sameDriver = driver.aggregateTrips();
		final DrivingSummary summary = driver.getDrivingSummary();

		assertEquals(driver, sameDriver);
		assertNotNull(summary);
		assertEquals(47, summary.getAverageSpeed());
		assertEquals(39.1, summary.getTotalDistance(), 0.01);
		assertEquals(3000, summary.getTotalDuration());
	}
}