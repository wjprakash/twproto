/*
 * TwUndoWorkspaceAction.java
 * Author - Winston Prakash
 */

package com.nayaware.twproto.actions;

import org.openide.nodes.Node;

import com.nayaware.twproto.events.*;
import com.nayaware.twproto.nodes.*;
import com.nayaware.twproto.ui.*;
import com.nayaware.twproto.util.*;

public class TwUndoWorkspaceAction extends TwAction {

	/** Creates new TwUndoWorkspaceAction */
	public TwUndoWorkspaceAction() {
		super("Undo Workspace Action ..");
		setType(TwResources.TW_UNDO_WORKSPACE_ACTION);
		// setToolTip("Undo throws last transaction in the workspace");
	}

	public void performAction(TwActionEvent e) {
	}

	protected boolean enable(Node[] nodes) {
		return false;
	}
}
