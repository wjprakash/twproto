/*
 * TwSelectCheckedinAction.java
 * Author - Winston Prakash
 */

package com.nayaware.twproto.actions;

import com.nayaware.twproto.events.*;
import com.nayaware.twproto.nodes.*;
import com.nayaware.twproto.ui.*;
import com.nayaware.twproto.util.*;

public class TwSelectCheckedinFilesAction extends TwAction {

	/** Creates new TwSelectCheckedinAction */
	public TwSelectCheckedinFilesAction() {
		super("Select checked in files");
		setType(TwResources.TW_SELECT_CHECKEDIN_FILES_ACTION);
	}

	public void performAction(TwActionEvent e) {
	}
}
