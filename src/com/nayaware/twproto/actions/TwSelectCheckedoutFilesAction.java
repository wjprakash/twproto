/*
 * TwSelectCheckedoutAction.java
 * Author - Winston Prakash
 */

package com.nayaware.twproto.actions;

import com.nayaware.twproto.events.*;
import com.nayaware.twproto.nodes.*;
import com.nayaware.twproto.ui.*;
import com.nayaware.twproto.util.*;

public class TwSelectCheckedoutFilesAction extends TwAction {

	/** Creates new TwSelectCheckedoutAction */
	public TwSelectCheckedoutFilesAction() {
		super("Select checked out files");
		setType(TwResources.TW_SELECT_CHECKEDOUT_FILES_ACTION);
	}

	public void performAction(TwActionEvent e) {
	}
}
