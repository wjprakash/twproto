/*
 * TwUpdatenametableAction.java
 * Author - Winston Prakash
 */

package com.nayaware.twproto.actions;

import org.openide.nodes.Node;

import com.nayaware.twproto.events.*;
import com.nayaware.twproto.nodes.*;
import com.nayaware.twproto.ui.*;
import com.nayaware.twproto.util.*;

public class TwUpdateNametableAction extends TwAction {

	TwActionDialog actionDialog = new TwActionDialog(null, true);

	/** Creates new TwBringoverAction */
	public TwUpdateNametableAction() {
		super("Update Nametable ..");
		setType(TwResources.TW_UPDATE_NAMETABLE_ACTION);
	}

	public void performAction(TwActionEvent e) {
	}

	protected boolean enable(Node[] nodes) {
		return false;
	}
}
