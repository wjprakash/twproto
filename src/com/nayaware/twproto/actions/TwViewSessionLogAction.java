/*
 * TwViewSessionLogAction.java
 * Author - Winston Prakash
 */

package com.nayaware.twproto.actions;

import com.nayaware.twproto.events.*;
import com.nayaware.twproto.nodes.*;
import com.nayaware.twproto.ui.*;
import com.nayaware.twproto.util.*;

public class TwViewSessionLogAction extends TwAction {

	/** Creates new TwViewSessionLogAction */
	public TwViewSessionLogAction() {
		super("Session Log");
		setType(TwResources.TW_VIEW_SESSION_LOG_ACTION);
		setToolTipText("Show Session Log");
	}

	public void performAction(TwActionEvent e) {
	}

}
