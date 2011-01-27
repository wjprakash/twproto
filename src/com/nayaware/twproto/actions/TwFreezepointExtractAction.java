/*
 * TwFreezepointExtractAction.java
 * Author - Winston Prakash
 */

package com.nayaware.twproto.actions;

import com.nayaware.twproto.events.*;
import com.nayaware.twproto.nodes.*;
import com.nayaware.twproto.ui.*;
import com.nayaware.twproto.util.*;

public class TwFreezepointExtractAction extends TwAction {
	/** Creates new TwBringoverAction */
	public TwFreezepointExtractAction() {
		super("Extract ..");
		setType(TwResources.TW_FREEZEPOINT_EXTRACT_ACTION);
		setToolTipText("Freezepoint Extract");
	}

	public void performAction(TwActionEvent e) {
	}
}
