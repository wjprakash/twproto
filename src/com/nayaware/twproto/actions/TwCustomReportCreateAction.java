/*
 * TwCustomReportCreateAction.java
 */

package com.nayaware.twproto.actions;

import org.openide.nodes.*;

import com.nayaware.twproto.events.*;
import com.nayaware.twproto.nodes.*;
import com.nayaware.twproto.ui.*;
import com.nayaware.twproto.util.*;

public class TwCustomReportCreateAction extends TwAction {
	/** Creates new TwBringoverAction */
	public TwCustomReportCreateAction() {
		super("Create Custom Report");
		setType(TwResources.TW_CUSTOM_REPORT_CREATE_ACTION);
	}

	public void performAction(TwActionEvent e) {
	}

	protected boolean enable(Node[] nodes) {
		return false;
	}
}
