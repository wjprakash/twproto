/** TwShowWorkspaceHistoryAction.java
 *
 * Author  Winston Prakash
 */

package com.nayaware.twproto.actions;

import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.actions.NodeAction;

import javax.swing.JMenuItem;
import java.util.EventObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.File;

import com.nayaware.twproto.dataobjects.*;
import com.nayaware.twproto.nodes.*;
import com.nayaware.twproto.util.*;
import com.sun.teamware.vertool.vtMain;
import com.sun.teamware.lib.common.TeamwareApp;
import com.sun.teamware.lib.common.TeamwareAppListener;
import com.sun.teamware.lib.common.TwUtil;

public class TwShowWorkspaceHistoryAction extends TwAction {

	TeamwareApp vertool = null;

	public TwShowWorkspaceHistoryAction() {
		super("View History");
		// setType(TwResources.TW_LOAD_WORKSPACE_ACTION);
	}

	/**
	 * enable the action only if it is under VC and is not a shadow file
	 **/
	protected boolean enable(Node[] nodes) {

		boolean enableAction = false;

		/*
		 * if ((nodes.length != 1) || (!(nodes[0] instanceof TwFileNode)))
		 * return false;
		 * 
		 * TwFileNode fileNode = (TwFileNode) nodes[0]; TwFileObject fileObject
		 * = fileNode.getFileObject(); if (fileObject.isUnderVC()) enableAction
		 * = true;
		 */

		return enableAction;
	}

	/* getMenuPresenter */
	public JMenuItem getMenuPresenter() {
		JMenuItem menu = new JMenuItem(getName());
		menu.setEnabled(isEnabled());
		menu.setActionCommand(getName());
		menu.addActionListener(this);
		return menu;
	}

	protected void performAction(Node[] nodes) {

		TwFileNode fileNode = (TwFileNode) nodes[0];
		TwFileObject fileObject = fileNode.getFileObject();
		TwAppUtil.showFileHistory(fileObject.getAbsolutePath());
	}
}
