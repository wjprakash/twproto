/*
 * TwNewFreezepointAction.java
 * Author - Winston Prakash
 */

package com.nayaware.twproto.actions;

import com.nayaware.twproto.events.*;
import com.nayaware.twproto.nodes.*;
import com.nayaware.twproto.ui.*;
import com.nayaware.twproto.util.*;

public class TwNewFreezepointAction extends TwAction {

	/** Creates new TwNewFreezepointAction */
	public TwNewFreezepointAction() {
		super("Freezepoint ..");
		setType(TwResources.TW_NEW_FREEZEPOINT_ACTION);
		// setToolTip("Create New Freezepoint");
	}

	public void performAction(TwActionEvent e) {
	}

}
