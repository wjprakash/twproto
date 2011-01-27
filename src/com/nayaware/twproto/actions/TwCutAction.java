/*
 * TwCutAction.java
 * Author - Winston Prakash
 */

package com.nayaware.twproto.actions;

import com.nayaware.twproto.events.*;
import com.nayaware.twproto.nodes.*;
import com.nayaware.twproto.ui.*;
import com.nayaware.twproto.util.*;

public class TwCutAction extends TwAction {

	/** Creates new TwCutAction */
	public TwCutAction() {
		super("Cut");
		setType(TwResources.TW_CUT_ACTION);
	}

	public void performAction(TwActionEvent e) {
	}
}
