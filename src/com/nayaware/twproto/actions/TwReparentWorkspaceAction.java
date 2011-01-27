/*
 * TwRenameWorkspaceAction.java
 * Author - Winston Prakash
 */

package com.nayaware.twproto.actions;

import javax.swing.*;

import org.openide.nodes.*;

import com.nayaware.twproto.dataobjects.*;
import com.nayaware.twproto.nodes.*;
import com.nayaware.twproto.ui.*;
import com.nayaware.twproto.util.*;

public class TwReparentWorkspaceAction extends TwAction {
	TwReparentWorkspacePanel repWsPanel = new TwReparentWorkspacePanel();
	TwWorkspaceNode wsNode = null;

	public TwReparentWorkspaceAction() {
		super("Reparent ..");
		setType(TwResources.TW_RENAME_WORKSPACE_ACTION);
		setToolTipText("Reparent Workspace");
		twActionDialog = new TwActionDialog(new javax.swing.JFrame(), true);
		twActionDialog.setUserPanel(repWsPanel);
	}

	public void performAction(Node[] nodes) {
		if (nodes.length > 0) {
			Node node = null;
			if (nodes[0] instanceof TwFilterNode)
				node = ((TwFilterNode) nodes[0]).getOriginalNode();
			else
				node = nodes[0];
			switch (((TwNode) node).getType()) {
			case TwResources.TW_WORKSPACE_NODE:
				wsNode = (TwWorkspaceNode) node;
				reparentWorkspace(wsNode.getWorkspace());
			}
		}
	}

	protected void reparentWorkspace(TwWorkspace ws) {
		repWsPanel.setWorkspace(ws);
		twActionDialog.setStartButtonName("Reparent Workspace");
		showActionDialog();
	}

	public void performDialogAction() {
		if (stopAction)
			return;
		twActionDialog.setProgressBarMinValue(0);
		twActionDialog.setProgressBarMaxValue(1);
		try {
			TwWorkspace wsParent = new TwWorkspaceObject(
					repWsPanel.getNewParent());
			if (!wsParent.exists()) {
				JOptionPane.showMessageDialog(null,
						"Specified Parent workspace - " + wsParent.getName()
								+ " does not exist");
				return;
			}
			wsNode.getWorkspace().setParent(wsParent);
			twActionDialog.setProgressBarValue(1);
			twActionDialog.close();
			wsNode.refresh();
		} catch (Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(null,
					"Could not reparent workspace - " + wsNode.getName()
							+ " to " + repWsPanel.getNewParent());
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
