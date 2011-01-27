/*
 * TwViewFilesNotUnderVcAction.java
 * Author - Winston Prakash
 */

package com.nayaware.twproto.actions;

import com.nayaware.twproto.events.*;
import com.nayaware.twproto.nodes.*;
import com.nayaware.twproto.ui.*;
import com.nayaware.twproto.util.*;

public class TwViewFilesNotUnderVcAction extends TwAction {

	/** Creates new TwViewFilesNotUnderVcAction */
	public TwViewFilesNotUnderVcAction() {
		super("Files not under version control");
		setType(TwResources.TW_VIEW_FILES_NOT_UNDER_VC_ACTION);
		setToolTipText("Show Files not under version control");
	}

	public void performAction(TwActionEvent e) {
		setPresenterState(true);
		fireTwActionPerformed(new TwActionEvent(this));
	}
}
