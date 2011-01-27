/*
 * TwFreezepointUpdateAction.java
 * Author - Winston Prakash
 */

package com.nayaware.twproto.actions;

import com.nayaware.twproto.events.*;
import com.nayaware.twproto.nodes.*;
import com.nayaware.twproto.ui.*;
import com.nayaware.twproto.util.*;

public class TwFreezepointUpdateAction extends TwAction {
	/** Creates new TwBringoverAction */
	public TwFreezepointUpdateAction() {
		super("Update ");
		setType(TwResources.TW_FREEZEPOINT_UPDATE_ACTION);
	}

	public void performAction(TwActionEvent e) {
	}
}
