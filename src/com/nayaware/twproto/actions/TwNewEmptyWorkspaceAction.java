/*
 * TwNewEmptyWorkspaceAction.java
 * Author - Winston Prakash
 */

package com.nayaware.twproto.actions;

import com.nayaware.twproto.events.*;
import com.nayaware.twproto.nodes.*;
import com.nayaware.twproto.ui.*;
import com.nayaware.twproto.util.*;

public class TwNewEmptyWorkspaceAction extends TwAction {

	/** Creates new TwNewEmptyWorkspaceAction */
	public TwNewEmptyWorkspaceAction() {
		super("Empty Workspace ..");
		setType(TwResources.TW_NEW_WS_ACTION);
		setToolTipText("Create new empty workspace");
	}

	public void performAction(TwActionEvent e) {
	}
}
