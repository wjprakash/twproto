/*
 * TwListViewAction.java
 * Author - Winston Prakash
 */

package com.nayaware.twproto.actions;

import com.nayaware.twproto.events.*;
import com.nayaware.twproto.nodes.*;
import com.nayaware.twproto.ui.*;
import com.nayaware.twproto.util.*;

public class TwViewListAction extends TwAction {

	/** Creates new TwListViewAction */
	public TwViewListAction() {
		super("List");
		setType(TwResources.TW_LIST_VIEW_ACTION);
		setToolTipText("List View of the contents");
	}

	public void performAction(TwActionEvent e) {
		fireTwActionPerformed(new TwActionEvent(this));
	}
}
