/*
 * TwNewDirectoryAction.java
 * Author - Winston Prakash
 */

package com.nayaware.twproto.actions;

import com.nayaware.twproto.events.*;
import com.nayaware.twproto.nodes.*;
import com.nayaware.twproto.ui.*;
import com.nayaware.twproto.util.*;

public class TwNewDirectoryAction extends TwAction {

	/** Creates new TwNewDirectoryAction */
	public TwNewDirectoryAction() {
		super("New Directory ..");
		setType(TwResources.TW_NEW_DIRECTORY_ACTION);
		// setToolTip("Create New Directory");
	}

	public void performAction(TwActionEvent e) {
	}

}
