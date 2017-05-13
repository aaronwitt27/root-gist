package com.awitt.root.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class TripTest {

	@Test
	public void parseTime_Start0715_Success() {

		final String start = "07:15";
		final String end = "07:55";
		final String distance = "3.0";

		final Trip result = new Trip(start, end, distance);

		assertNotNull(result);
		assertEquals(start, result.getStart().toString());
		assertEquals(end, result.getEnd().toString());
		assertEquals(3.0, result.getDistance(), .01);
	}
}