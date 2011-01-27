/*
 * TwDeleteAction.java
 * Author - Winston Prakash
 */

package com.nayaware.twproto.actions;

import java.util.*;
import javax.swing.*;

import org.openide.nodes.*;

import com.nayaware.twproto.dataobjects.*;
import com.nayaware.twproto.events.*;
import com.nayaware.twproto.nodes.*;
import com.nayaware.twproto.ui.*;
import com.nayaware.twproto.util.*;

public class TwDeleteAction extends TwAction {

	TwDeleteWorkspacePanel delWsPanel = new TwDeleteWorkspacePanel();
	Vector wsList = new Vector();
	TwWorkspaceNode wsNode = null;

	/** Creates new TwDeleteAction */
	public TwDeleteAction() {
		super("Delete ..");
		setType(TwResources.TW_DELETE_ACTION);
		twActionDialog = new TwActionDialog(new javax.swing.JFrame(), true);
		twActionDialog.setUserPanel(delWsPanel);
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
				deleteWorkspace(wsNode.getWorkspace());
			}
		}
	}

	protected void deleteWorkspace(TwWorkspace ws) {
		delWsPanel.setWorkspace(ws);
		twActionDialog.setStartButtonName("Delete Workspace");
		showActionDialog();
	}

	public void performDialogAction() {
		if (stopAction)
			return;
		twActionDialog.setProgressBarMinValue(0);
		twActionDialog.setProgressBarMaxValue(1);
		delWsPanel.getDeleteType();
		try {
			if (delWsPanel.getDeleteType() == TwDeleteWorkspacePanel.META_DATA_ONLY)
				wsNode.getWorkspace().delete(true);
			else
				wsNode.getWorkspace().delete(false);
			Node parent = wsNode.getParentNode();
			Node[] oldChildren = { wsNode };
			parent.getChildren().remove(oldChildren);
			twActionDialog.setProgressBarValue(1);
			twActionDialog.close();

		} catch (Exception ex) {
			twActionDialog
					.appendOutput("Error occured while deleting workspace - "
							+ wsNode.getWorkspace().getPath());
			twActionDialog.appendOutput(ex.getMessage());
			JOptionPane.showMessageDialog(null, ex.getMessage());
			twActionDialog.viewOutput(true);
		}

	}

	protected boolean enable(Node[] nodes) {
		boolean enableNode = false;
		if (nodes.length > 0) {
			Node node = null;
			if (nodes[0] instanceof TwFilterNode)
				node = ((TwFilterNode) nodes[0]).getOriginalNode();
			else
				node = nodes[0];
			if (node instanceof TwWorkspaceNode)
				enableNode = true;
		}
		return enableNode;
	}
}
