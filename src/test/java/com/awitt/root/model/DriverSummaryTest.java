package com.awitt.root.model;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import java.time.LocalTime;

import org.junit.Before;
import org.junit.Test;

public class DriverSummaryTest {

	private DrivingSummary subject;
	private Trip trip;

	@Before
	public void init() {
		this.subject = new DrivingSummary();
		this.trip = mock(Trip.class);
	}

	@Test
	public void addTripInfo_SingleTrip_TripInfoAddedCorrectly() {

		final LocalTime start = LocalTime.of(5, 0);
		final LocalTime end = LocalTime.of(6, 0);

		doReturn(5.0).when(this.trip).getDistance();
		doReturn(start).when(this.trip).getStart();
		doReturn(end).when(this.trip).getEnd();

		this.subject.addTripInfo(this.trip);

		assertEquals(5.0, this.subject.getTotalDistance(), 0.01);
		assertEquals(60 * 60, this.subject.getTotalDuration());
		assertEquals(5.0, this.subject.getAverageSpeed(), 0.01);
	}

	@Test
	public void addTripInfo_MultipleTrip_TripInfoAddedCorrectly() {

		LocalTime start = LocalTime.of(5, 0);
		LocalTime end = LocalTime.of(6, 0);

		doReturn(5.0).when(this.trip).getDistance();
		doReturn(start).when(this.trip).getStart();
		doReturn(end).when(this.trip).getEnd();

		this.subject.addTripInfo(this.trip);

		start = LocalTime.of(12, 0);
		end = LocalTime.of(14, 0);

		doReturn(100.0).when(this.trip).getDistance();
		doReturn(start).when(this.trip).getStart();
		doReturn(end).when(this.trip).getEnd();

		this.subject.addTripInfo(this.trip);

		assertEquals(105.0, this.subject.getTotalDistance(), 0.01);
		assertEquals((2 * 60 * 60) + (60 * 60), this.subject.getTotalDuration());
		assertEquals(35.0, this.subject.getAverageSpeed(), 0.01);
	}
}