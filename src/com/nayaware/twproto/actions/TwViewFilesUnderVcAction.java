/*
 * TwViewFilesUnderVcAction.java
 * Author - Winston Prakash
 */

package com.nayaware.twproto.actions;

import com.nayaware.twproto.events.*;
import com.nayaware.twproto.nodes.*;
import com.nayaware.twproto.ui.*;
import com.nayaware.twproto.util.*;

public class TwViewFilesUnderVcAction extends TwAction {

	/** Creates new TwViewFilesUnderVcAction */
	public TwViewFilesUnderVcAction() {
		super("Files under version control");
		setType(TwResources.TW_VIEW_FILES_UNDER_VC_ACTION);
		setToolTipText("Show Files under version control");
	}

	public void performAction(TwActionEvent e) {
		setPresenterState(true);
		fireTwActionPerformed(new TwActionEvent(this));
	}
}
