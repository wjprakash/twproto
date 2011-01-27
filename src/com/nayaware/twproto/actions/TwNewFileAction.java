/*
 * TwNewFileAction.java
 * Author - Winston Prakash
 */

package com.nayaware.twproto.actions;

import com.nayaware.twproto.events.*;
import com.nayaware.twproto.nodes.*;
import com.nayaware.twproto.ui.*;
import com.nayaware.twproto.util.*;

public class TwNewFileAction extends TwAction {

	/** Creates new TwNewFileAction */
	public TwNewFileAction() {
		super("File ..");
		setType(TwResources.TW_CHECKIN_ACTION);
		// setTooltip("Create New file");
	}

	public void performAction(TwActionEvent e) {
	}
}
