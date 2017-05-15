package com.awitt.root;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import java.io.BufferedReader;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Stream;

import org.junit.Test;

import com.awitt.root.model.Driver;

public class DriverTrackerTest {

	@Test(expected = NullPointerException.class)
	public void driverTracker_NullBufferedReader_NPE() {
		new DriverTracker(null);
	}

	@Test
	public void process_MockedReader_NoExceptionsResultsPrinted() {

		final String line1 = "Driver Yossarian";
		final String line2 = "Trip Yossarian 00:00 05:00 100.0";
		final String[] lines = new String[] { line1, line2 };
		final BufferedReader reader = mock(BufferedReader.class);
		AtomicBoolean printed = new AtomicBoolean(false);

		doReturn(Stream.of(lines)).when(reader).lines();

		final DriverTracker subject = new DriverTracker(reader) {
			@Override
			void printResults(final Driver driver) {
				assertEquals("Yossarian", driver.getName());
				assertEquals(1, driver.getTrips().size());
				printed.set(true);
			}
		};

		subject.process();

		assertTrue(printed.get());
	}
}