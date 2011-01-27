/*
 * TwMoveWorkspaceAction.java
 * Author - Winston Prakash
 */

package com.nayaware.twproto.actions;

import com.nayaware.twproto.events.*;
import com.nayaware.twproto.nodes.*;
import com.nayaware.twproto.ui.*;
import com.nayaware.twproto.util.*;

public class TwMoveWorkspaceAction extends TwAction {
	/** Creates new TwBringoverAction */
	public TwMoveWorkspaceAction() {
		super("Move ..");
		setType(TwResources.TW_MOVE_WORKSPACE_ACTION);
		setToolTipText("Move Teamware Workspace");
	}

	public void performAction(TwActionEvent e) {
	}
}
