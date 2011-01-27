/*
 * TwRenameWorkspaceAction.java
 * Author - Winston Prakash
 */

package com.nayaware.twproto.actions;

import com.nayaware.twproto.events.*;
import com.nayaware.twproto.nodes.*;
import com.nayaware.twproto.ui.*;
import com.nayaware.twproto.util.*;

public class TwResolveFileAction extends TwAction {
	public TwResolveFileAction() {
		super("Resolve ..");
		setType(TwResources.TW_RENAME_WORKSPACE_ACTION);
		setToolTipText("Resolve the conflict file");
	}

	public void performAction(TwActionEvent e) {
	}
}
