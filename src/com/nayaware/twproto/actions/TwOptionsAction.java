/*
 * TwRepositoryConnectAction.java
 * Author - Winston Prakash
 */

package com.nayaware.twproto.actions;

import com.nayaware.twproto.events.*;
import com.nayaware.twproto.nodes.*;
import com.nayaware.twproto.ui.*;
import com.nayaware.twproto.util.*;

public class TwOptionsAction extends TwAction {
	/** Creates new TwBringoverAction */
	public TwOptionsAction() {
		super("Edit Options ..");
		setType(TwResources.TW_OPTIONS_ACTION);
		setToolTipText("Edit Options");
	}

	public void performAction(TwActionEvent e) {
	}
}
