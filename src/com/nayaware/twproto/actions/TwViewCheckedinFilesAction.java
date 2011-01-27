/*
 * TwViewCheckedinFilesAction.java
 * Author - Winston Prakash
 */

package com.nayaware.twproto.actions;

import com.nayaware.twproto.events.*;
import com.nayaware.twproto.nodes.*;
import com.nayaware.twproto.ui.*;
import com.nayaware.twproto.util.*;

public class TwViewCheckedinFilesAction extends TwAction {

	/** Creates new TwViewCheckedinFilesAction */
	public TwViewCheckedinFilesAction() {
		super("Files checked in");
		setType(TwResources.TW_VIEW_CHECKEDIN_FILES_ACTION);
		setToolTipText("Show Files checked in");
	}

	public void performAction(TwActionEvent e) {
		setPresenterState(true);
		fireTwActionPerformed(new TwActionEvent(this));
	}
}
