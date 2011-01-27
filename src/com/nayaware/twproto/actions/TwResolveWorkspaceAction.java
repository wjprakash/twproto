/*
 * TwRenameWorkspaceAction.java
 * Author - Winston Prakash
 */

package com.nayaware.twproto.actions;

import com.nayaware.twproto.events.*;
import com.nayaware.twproto.nodes.*;
import com.nayaware.twproto.ui.*;
import com.nayaware.twproto.util.*;

public class TwResolveWorkspaceAction extends TwAction {
	public TwResolveWorkspaceAction() {
		super("Resolve ..");
		setType(TwResources.TW_RENAME_WORKSPACE_ACTION);
		setToolTipText("Resolve the conflicted Workspace");
	}

	public void performAction(TwActionEvent e) {
	}
}
