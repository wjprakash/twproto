/*
 * TwLoadChildrenAction.java
 * Author - Winston Prakash
 */

package com.nayaware.twproto.actions;

import org.openide.nodes.*;

import com.nayaware.twproto.dataobjects.*;
import com.nayaware.twproto.events.*;
import com.nayaware.twproto.nodes.*;
import com.nayaware.twproto.ui.*;
import com.nayaware.twproto.util.*;

public class TwLoadChildrenAction extends TwAction {
	TwLoadChildrenPanel loadChildrenPanel = new TwLoadChildrenPanel();
	TwWorkspaceNode wsNode = null;

	/** Creates new TwLoadChildrenAction */
	public TwLoadChildrenAction() {
		super("Load Children");
		setType(TwResources.TW_LOAD_CHILDREN_ACTION);
		twActionDialog = new TwActionDialog(new javax.swing.JFrame(), true);
		twActionDialog.setUserPanel(loadChildrenPanel);
	}

	protected void performAction(Node[] nodes) {
		if (nodes.length > 0) {
			Node node = null;
			if (nodes[0] instanceof TwFilterNode)
				node = ((TwFilterNode) nodes[0]).getOriginalNode();
			else
				node = nodes[0];
			switch (((TwNode) node).getType()) {
			case TwResources.TW_WORKSPACE_NODE:
				wsNode = (TwWorkspaceNode) node;
				loadChildrenPanel.setWorkspace(wsNode.getWorkspace());
				twActionDialog.setStartButtonName("Load Children");
				showActionDialog();
			}
		}
	}

	public void performDialogAction() {
		if (stopAction)
			return;
		twActionDialog.setProgressBarMinValue(0);
		twActionDialog.setProgressBarMaxValue(1);
		TwWorkspace[] children = loadChildrenPanel.getSelection();
		if (children != null) {
			TwWorkspaceNode[] newChildren = new TwWorkspaceNode[children.length];
			for (int i = 0; i < children.length; i++) {
				newChildren[i] = new TwWorkspaceNode(children[i].getPath());
				twActionDialog.setProgressBarMaxValue(i);
			}
			Node parentNode = wsNode.getParentNode();
			parentNode.getChildren().add(newChildren);
		}
		twActionDialog.close();
	}

	protected boolean enable(Node[] nodes) {
		if (nodes.length > 0) {
			Node node = null;
			if (nodes[0] instanceof TwFilterNode)
				node = ((TwFilterNode) nodes[0]).getOriginalNode();
			else
				node = nodes[0];
			if (node instanceof TwWorkspaceNode) {
				TwWorkspaceNode wsNode = (TwWorkspaceNode) node;
				TwWorkspace wsObj = wsNode.getWorkspace();
				if (wsObj != null) {
					TwWorkspace[] children = wsObj.getChildren();
					if (children != null) {
						return true;
					} else {
						return false;
					}
				}
			}
		}
		return false;
	}
}
