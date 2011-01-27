/*
 * TwRenameWorkspaceAction.java
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

public class TwRenameWorkspaceAction extends TwAction {

	TwRenameWorkspacePanel renWsPanel = new TwRenameWorkspacePanel();
	Vector wsList = new Vector();
	TwWorkspaceNode wsNode = null;

	public TwRenameWorkspaceAction() {
		super("Rename ..");
		setType(TwResources.TW_RENAME_WORKSPACE_ACTION);
		setToolTipText("Rename Workspace");
		twActionDialog = new TwActionDialog(new javax.swing.JFrame(), true);
		twActionDialog.setUserPanel(renWsPanel);
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
				renameWorkspace(wsNode.getWorkspace());
			}
		}
	}

	protected void renameWorkspace(TwWorkspace ws) {
		renWsPanel.setWorkspace(ws);
		twActionDialog.setStartButtonName("Rename Workspace");
		showActionDialog();
	}

	public void performDialogAction() {
		if (stopAction)
			return;
		twActionDialog.setProgressBarMinValue(0);
		twActionDialog.setProgressBarMaxValue(1);
		try {
			wsNode.getWorkspace().rename(renWsPanel.getNewName());
			twActionDialog.setProgressBarValue(1);
			twActionDialog.close();
			wsNode.refresh();
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			ex.printStackTrace();
			JOptionPane.showMessageDialog(null, "Could not rename workspace "
					+ wsNode.getName() + " to " + renWsPanel.getNewName());
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
