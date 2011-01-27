/*
 * showAllFilesAction.java
 * Author - Winston Prakash
 */

package com.nayaware.twproto.actions;

import com.nayaware.twproto.events.*;
import com.nayaware.twproto.nodes.*;
import com.nayaware.twproto.ui.*;
import com.nayaware.twproto.util.*;

public class TwViewAllFilesAction extends TwAction {

	/** Creates new showAllFilesAction */
	public TwViewAllFilesAction() {
		super("All files");
		setType(TwResources.TW_VIEW_ALL_FILES_ACTION);
	}

	public void performAction(TwActionEvent e) {
		setPresenterState(true);
		fireTwActionPerformed(new TwActionEvent(this));
	}
}
