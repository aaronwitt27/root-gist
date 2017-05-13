package com.awitt.root;

import java.io.Reader;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DriverTracker {
	private static final Logger LOGGER = LoggerFactory.getLogger(DriverTracker.class);

	final Reader reader;

	public DriverTracker(final Reader reader) {
		Validate.notNull(reader, "reader cannot be null");
		this.reader = reader;
	}

	public void doThing() {
		LOGGER.info("doing thing");
	}
}