/*
 * TwDetailViewAction.java
 * Author - Winston Prakash
 */

package com.nayaware.twproto.actions;

import com.nayaware.twproto.events.*;
import com.nayaware.twproto.nodes.*;
import com.nayaware.twproto.ui.*;
import com.nayaware.twproto.util.*;

public class TwViewDetailAction extends TwAction {

	/** Creates new TwDetailViewAction */
	public TwViewDetailAction() {
		super("Details");
		setType(TwResources.TW_DETAIL_VIEW_ACTION);
		setToolTipText("Detail View of the contents");
	}

	public void performAction(TwActionEvent e) {
		fireTwActionPerformed(new TwActionEvent(this));
	}
}
