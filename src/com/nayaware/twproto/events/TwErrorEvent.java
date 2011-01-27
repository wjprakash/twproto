/*
 * TwErrorEvent.java
 * Author - Winston Prakash
 */

package com.nayaware.twproto.events;

import com.nayaware.twproto.actions.*;

public class TwErrorEvent {

	protected TwAction twAction;
	protected String errorMessage;

	/**
	 * Creates new TwErrorEvent with the supplied error message
	 */
	public TwErrorEvent(String errMsg) {
		errorMessage = errMsg;
	}

	public TwErrorEvent(String errMsg, TwAction action) {
		errorMessage = errMsg;
		twAction = action;
	}

	/**
	 * Set the error message
	 */
	public void getMessage(String msg) {
		errorMessage = msg;
	}

	/**
	 * Get the error message
	 */
	public String getMessage() {
		return errorMessage;
	}

	/**
	 * Set the source TwAction that fires this error event
	 */
	public void setSource(TwAction action) {
		twAction = action;
	}

	/**
	 * Get the source TwAction that fires this error event
	 */
	public TwAction getSource() {
		return twAction;
	}
}
