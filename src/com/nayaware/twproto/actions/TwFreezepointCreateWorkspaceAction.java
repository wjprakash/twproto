/*
 * TwFreezepointCreateWorkspaceAction.java
 * Author - Winston Prakash
 */

package com.nayaware.twproto.actions;

import com.nayaware.twproto.events.*;
import com.nayaware.twproto.nodes.*;
import com.nayaware.twproto.ui.*;
import com.nayaware.twproto.util.*;

public class TwFreezepointCreateWorkspaceAction extends TwAction {
	TwActionDialog actionDialog = new TwActionDialog(null, true);

	/** Creates new TwBringoverAction */
	public TwFreezepointCreateWorkspaceAction() {
		super("Create Workspace ..");
		setType(TwResources.TW_FREEZEPOINT_CREATE_WORKSPACE_ACTION);
		setToolTipText("Create Workspace from freezepoint");
	}

	public void performAction(TwActionEvent e) {
	}
}
