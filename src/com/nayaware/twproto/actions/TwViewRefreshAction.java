/*
 * TwViewRefreshAction.java
 * Author - Winston Prakash
 */

package com.nayaware.twproto.actions;

import com.nayaware.twproto.events.*;
import com.nayaware.twproto.nodes.*;
import com.nayaware.twproto.ui.*;
import com.nayaware.twproto.util.*;

public class TwViewRefreshAction extends TwAction {

	/** Creates new TwViewRefreshAction */
	public TwViewRefreshAction() {
		super("Refresh");
		setType(TwResources.TW_VIEW_REFRESH_ACTION);
		setToolTipText("Refresh Content List");
	}

	public void performAction(TwActionEvent e) {
	}
}
