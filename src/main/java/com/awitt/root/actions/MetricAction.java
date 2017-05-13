package com.awitt.root.actions;

import com.awitt.root.DriverTracker;

/**
 * All possible actions that can be processed by {@link DriverTracker}.
 */
public enum Actions {

	/** Registers a driver in the applciation **/
	DRIVER,

	/** Records a trip attributed to a registered driver **/
	TRIP;
}