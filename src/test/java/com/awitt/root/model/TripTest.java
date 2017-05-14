package com.awitt.root.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.time.format.DateTimeParseException;

import org.junit.Test;

public class TripTest {

	@Test
	public void parseTime_GoodData_Success() {

		final String start = "07:15";
		final String end = "07:55";
		final String distance = "3.0";

		final Trip result = new Trip(start, end, distance);

		assertNotNull(result);
		assertEquals(start, result.getStart().toString());
		assertEquals(end, result.getEnd().toString());
		assertEquals(3.0, result.getDistance(), .01);
		assertEquals(4.5, result.getAverageSpeed(), 0.01);
	}

	@Test(expected = DateTimeParseException.class)
	public void parseTime_BadStart_DTPE() {

		final String start = "not gonna work";
		final String end = "07:55";
		final String distance = "3.0";

		new Trip(start, end, distance);
	}

	@Test(expected = DateTimeParseException.class)
	public void parseTime_BadEnd_DTPE() {

		final String start = "08:44";
		final String end = "still shouldn't work";
		final String distance = "3.0";

		new Trip(start, end, distance);
	}

	@Test(expected = NumberFormatException.class)
	public void parseTime_BadDistance_NFE() {

		final String start = "08:44";
		final String end = "02:22";
		final String distance = "no way, it can't work";

		new Trip(start, end, distance);
	}

	@Test
	public void meetsTripRequirements_Meets_True() {
		final Trip trip = new Trip("12:00", "14:00", "120.0");

		assertTrue(trip.meetsTripRequirements());
	}

	@Test
	public void meetsTripRequirements_TooSlow_False() {
		final Trip trip = new Trip("12:00", "14:00", "5.0");

		assertFalse(trip.meetsTripRequirements());
	}

	@Test
	public void meetsTripRequirements_TooFast_False() {
		final Trip trip = new Trip("12:00", "14:00", "1200.0");

		assertFalse(trip.meetsTripRequirements());
	}
}