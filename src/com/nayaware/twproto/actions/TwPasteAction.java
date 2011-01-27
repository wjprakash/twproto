/*
 * TwPasteAction.java
 * Author - Winston Prakash
 */

package com.nayaware.twproto.actions;

import com.nayaware.twproto.events.*;
import com.nayaware.twproto.nodes.*;
import com.nayaware.twproto.ui.*;
import com.nayaware.twproto.util.*;

public class TwPasteAction extends TwAction {

	/** Creates new TwPasteAction */
	public TwPasteAction() {
		super("Paste");
		setType(TwResources.TW_PASTE_ACTION);
	}

	public void performAction(TwActionEvent e) {
	}
}
