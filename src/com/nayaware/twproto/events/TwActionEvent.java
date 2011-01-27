/*
 * TwActionEvent.java
 * Author - Winston Prakash
 */

package com.nayaware.twproto.events;

import org.openide.nodes.*;

import com.nayaware.twproto.actions.*;
import com.nayaware.twproto.nodes.*;

public class TwActionEvent {

	protected TwAction twAction;
	protected Node[] nodes;

	/**
	 * Creates new TwActionEvent with the supplied TwAction
	 */
	public TwActionEvent(TwAction action) {
		twAction = action;
	}

	/**
	 * Creates new TwActionEvent with the supplied TwAction and TwNodes
	 */
	public TwActionEvent(TwAction action, Node[] ns) {
		twAction = action;
		nodes = ns;
	}

	/**
	 * Set the source TwAction that fires this event
	 */
	public void setSource(TwAction action) {
		twAction = action;
	}

	/**
	 * Get the source TwAction that fires this event
	 */
	public TwAction getSource() {
		return twAction;
	}

	/**
	 * Set the TwNode on which the TwAction was performed
	 */
	public void setNodes(Node[] ns) {
		nodes = ns;
	}

	/**
	 * Get the TwNode on which the TwAction was performed
	 */
	public Node[] getNodes() {
		return nodes;
	}
}
