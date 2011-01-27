/*
 * TwBringoverAction.java
 * Author - Winston Prakash
 */

package com.nayaware.twproto.actions;

import org.openide.nodes.*;

import com.nayaware.twproto.events.*;
import com.nayaware.twproto.ui.*;
import com.nayaware.twproto.util.*;

public class TwAddFilesToWorkspaceAction extends TwAction {
	TwActionDialog actionDialog = new TwActionDialog(null, true);

	/** Creates new TwBringoverAction */
	public TwAddFilesToWorkspaceAction() {
		super("Add Files to Workspace ..");
		setType(TwResources.TW_ADD_FILES_TO_WORKSPACE_ACTION);
	}

	public void performAction(TwActionEvent e) {
	}

	protected boolean enable(Node[] nodes) {
		return false;
	}
}
