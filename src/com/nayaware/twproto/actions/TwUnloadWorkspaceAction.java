/*
 * TwUnloadWorkspaceAction.java
 * Author - Winston Prakash
 */

package com.nayaware.twproto.actions;

import com.nayaware.twproto.events.*;
import com.nayaware.twproto.nodes.*;
import com.nayaware.twproto.ui.*;
import com.nayaware.twproto.util.*;

import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.NodeAction;

import javax.swing.Action;

public class TwUnloadWorkspaceAction extends TwAction {

	public TwUnloadWorkspaceAction() {
		super("Unload Workspace");
		setType(TwResources.TW_UNLOAD_WORKSPACE_ACTION);
	}

	protected void performAction(Node[] nodes) {
		if (nodes.length > 0) {
			Node node = null;
			if (nodes[0] instanceof TwFilterNode)
				node = ((TwFilterNode) nodes[0]).getOriginalNode();
			else
				node = nodes[0];
			if (node instanceof TwWorkspaceNode) {
				Node parent = node.getParentNode();
				Node[] oldChildren = { (TwWorkspaceNode) node };
				parent.getChildren().remove(oldChildren);
			}
		}
	}

	protected boolean enable(Node[] nodes) {
		if (nodes.length > 0) {
			Node node = null;
			if (nodes[0] instanceof TwFilterNode)
				node = ((TwFilterNode) nodes[0]).getOriginalNode();
			else
				node = nodes[0];
			if (node instanceof TwWorkspaceNode) {
				return true;
			}
		}
		return false;
	}
}
