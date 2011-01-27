package com.nayaware.twproto.actions;

import org.openide.nodes.*;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.CallableSystemAction;

/**
 * Action that can always be invoked and work procedurally.
 * 
 * @author Winston Prakash
 */
public class TwShowHierarchyViewAction extends CallableSystemAction {

	private static final long serialVersionUID = 3901209609559150003L;

	public void performAction() {
		// do what you want
	}

	public String getName() {
		return "Show Hierarchy View";
	}

	protected String iconResource() {
		return "TwShowNavigatorActionIcon.gif";
	}

	public HelpCtx getHelpCtx() {
		return HelpCtx.DEFAULT_HELP;
		// If you will provide context help then use:
		// return new HelpCtx (TwShowNavigatorAction.class);
	}

	protected boolean enable(Node[] nodes) {
		return false;
	}
}
