/*
 * TwSelectAllAction.java
 * Author - Winston Prakash
 */

package com.nayaware.twproto.actions;

import com.nayaware.twproto.events.*;
import com.nayaware.twproto.nodes.*;
import com.nayaware.twproto.ui.*;
import com.nayaware.twproto.util.*;

public class TwSelectAllFilesAction extends TwAction {

	/** Creates new TwSelectAllAction */
	public TwSelectAllFilesAction() {
		super("Select All");
		setType(TwResources.TW_SELECT_ALL_FILE_ACTION);
	}

	public void performAction(TwActionEvent e) {
	}

}
