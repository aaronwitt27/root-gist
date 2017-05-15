package com.awitt.root.actions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.junit.Test;

import com.awitt.root.model.Driver;
import com.awitt.root.model.Trip;

public class ActionTest {

	@Test(expected = NullPointerException.class)
	public void doCommand_Driver_NullLine_NPE() {
		Action.DRIVER.doCommand(null, new HashMap<>());
	}

	@Test(expected = IllegalArgumentException.class)
	public void doCommand_Driver_BlankLine_IAE() {
		Action.DRIVER.doCommand("", new HashMap<>());
	}

	@Test(expected = NullPointerException.class)
	public void doCommand_Driver_NullDrivers_NPE() {
		Action.DRIVER.doCommand("line", null);
	}

	@Test(expected = NullPointerException.class)
	public void doCommand_Trip_NullLine_NPE() {
		Action.TRIP.doCommand(null, new HashMap<>());
	}

	@Test(expected = IllegalArgumentException.class)
	public void doCommand_Trip_BlankLine_IAE() {
		Action.TRIP.doCommand("", new HashMap<>());
	}

	@Test(expected = NullPointerException.class)
	public void doCommand_Trip_NullDrivers_NPE() {
		Action.TRIP.doCommand("line", null);
	}

	@Test
	public void doCommand_Driver_DriverCommand_DriverRegistered() {

		final Map<String, Driver> drivers = new HashMap<>();
		final String line = "Driver aaron";

		Action.DRIVER.doCommand(line, drivers);

		assertNotNull(drivers);
		assertEquals(1, drivers.size());
		assertNotNull(drivers.get("aaron"));
		assertEquals("aaron", drivers.get("aaron").getName());
	}

	@Test
	public void doCommand_Driver_DriverCommandDriverAlreadyRegistered_DriverNotReRegistered() {

		final Map<String, Driver> drivers = new HashMap<>();
		final String line = "Driver aaron";

		Action.DRIVER.doCommand(line, drivers);

		assertNotNull(drivers);
		assertEquals(1, drivers.size());
		assertNotNull(drivers.get("aaron"));
		assertEquals("aaron", drivers.get("aaron").getName());

		Action.DRIVER.doCommand(line, drivers);

		assertNotNull(drivers);
		assertEquals(1, drivers.size());
		assertNotNull(drivers.get("aaron"));
		assertEquals("aaron", drivers.get("aaron").getName());
	}

	@Test(expected = RuntimeException.class)
	public void doCommand_Trip_WrongNumberOfMetrics_RuntimeException() {

		final Map<String, Driver> drivers = new HashMap<>();
		final String line = "Trip metric1 metric2 metric5 metric3sir metric4";

		Action.TRIP.doCommand(line, drivers);
	}

	@Test
	public void doCommand_Trip_UnregisteredDriver_LineReturned() {

		final Map<String, Driver> drivers = new HashMap<>();
		final String line = "Trip freyja metric3 metric4 metric5";

		final Optional<String> result = Action.TRIP.doCommand(line, drivers);

		assertNotNull(result);
		assertTrue(result.isPresent());
		assertEquals(line, result.get());
	}

	@Test(expected = RuntimeException.class)
	public void doCommand_Trip_BadStart_RTE() {

		final Map<String, Driver> drivers = new HashMap<>();
		drivers.put("freyja", new Driver("freyja"));
		final String line = "Trip freyja metric3 metric4 metric5";

		Action.TRIP.doCommand(line, drivers);
	}

	@Test(expected = RuntimeException.class)
	public void doCommand_Trip_BadEnd_RTE() {

		final Map<String, Driver> drivers = new HashMap<>();
		drivers.put("freyja", new Driver("freyja"));
		final String line = "Trip freyja 07:15 metric4 metric5";

		Action.TRIP.doCommand(line, drivers);
	}

	@Test(expected = RuntimeException.class)
	public void doCommand_Trip_BadDistance_RTE() {

		final Map<String, Driver> drivers = new HashMap<>();
		drivers.put("freyja", new Driver("freyja"));
		final String line = "Trip freyja 07:15 08:15 metric5";

		Action.TRIP.doCommand(line, drivers);
	}

	@Test
	public void doCommand_Trip_() {

		final Map<String, Driver> drivers = new HashMap<>();
		drivers.put("freyja", new Driver("freyja"));
		final String line = "Trip freyja 07:15 08:15 27.27";

		final Optional<String> result = Action.TRIP.doCommand(line, drivers);
		assertNotNull(result);
		assertFalse(result.isPresent());

		final Driver freyja = drivers.get("freyja");
		assertNotNull(freyja);
		assertEquals(1, freyja.getTrips().size());

		final Trip trip = freyja.getTrips().get(0);
		assertNotNull(trip);
		assertEquals(27.27, trip.getDistance(), 0.01);
		assertEquals(LocalTime.of(7, 15), trip.getStart());
		assertEquals(LocalTime.of(8, 15), trip.getEnd());
	}
}