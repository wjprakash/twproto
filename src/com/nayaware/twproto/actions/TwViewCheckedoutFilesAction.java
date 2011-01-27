/*
 * TwViewCheckedoutFilesAction.java
 * Author - Winston Prakash
 */

package com.nayaware.twproto.actions;

import com.nayaware.twproto.events.*;
import com.nayaware.twproto.nodes.*;
import com.nayaware.twproto.ui.*;
import com.nayaware.twproto.util.*;

public class TwViewCheckedoutFilesAction extends TwAction {

	/** Creates new TwViewCheckedoutFilesAction */
	public TwViewCheckedoutFilesAction() {
		super("Files checked out");
		setType(TwResources.TW_VIEW_CHECKEDOUT_FILES_ACTION);
		setToolTipText("Show All checked out Files");
	}

	public void performAction(TwActionEvent e) {
		setPresenterState(true);
		fireTwActionPerformed(new TwActionEvent(this));
	}
}
