/*
 * TwCopyAction.java
 */

package com.nayaware.twproto.actions;

import org.openide.nodes.*;

import com.nayaware.twproto.events.*;
import com.nayaware.twproto.nodes.*;
import com.nayaware.twproto.ui.*;
import com.nayaware.twproto.util.*;

public class TwCopyAction extends TwAction {

	/** Creates new TwCopyAction */
	public TwCopyAction() {
		super("Copy");
		setType(TwResources.TW_COPY_ACTION);
	}

	public void performAction(TwActionEvent e) {
	}

	protected boolean enable(Node[] nodes) {
		return false;
	}
}
