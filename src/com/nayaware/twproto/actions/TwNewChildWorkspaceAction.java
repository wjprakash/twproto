/*
 * TwNewChildWorkspaceAction.java
 */

package com.nayaware.twproto.actions;

import java.util.*;
import javax.swing.*;

import org.openide.nodes.Node;

import com.nayaware.twproto.dataobjects.*;
import com.nayaware.twproto.nodes.*;
import com.nayaware.twproto.ui.*;
import com.nayaware.twproto.util.*;

public class TwNewChildWorkspaceAction extends TwAction {

	TwTransactionPanel transactionPanel = new TwTransactionPanel(
			TwTransactionPanel.BRINGOVER_CREATE);

	/** Creates new TwNewChildWorkspaceAction */
	public TwNewChildWorkspaceAction() {
		super("Child Workspace ..");
		setType(TwResources.TW_NEW_CHILD_WORKSPACE_ACTION);
		twActionDialog = new TwActionDialog(new javax.swing.JFrame(), true);
		twActionDialog.setTitle("Teamware:Create Child Workspace");
		twActionDialog.setUserPanel(transactionPanel);
	}

	public void performAction(Node[] nodes) {
		if (nodes.length > 0) {
			if (nodes[0] instanceof TwWorkspaceNode) {
				TwWorkspaceNode wsNode = (TwWorkspaceNode) nodes[0];
				// Set waiting cursor if needed
				twActionDialog.clearOutput();
				transactionPanel.setWorkspace(wsNode.getWorkspace());
				showActionDialog();
			} else {
				JOptionPane.showMessageDialog(null, nodes[0].getName()
						+ ": Not a workspace");
			}
		}
	}

	public void performDialogAction() {
		TwCommand command = new TwCommand();
		String outputLine;
		String childWs = transactionPanel.getChildWorkspace();
		String parentWs = transactionPanel.getParentWorkspace();
		String selection = transactionPanel.getSelection();
		String cmd = "c:\\ffjuser30\\extbin\\bringover -n -w " + childWs
				+ " -p " + parentWs + " " + selection;
		command.execute(cmd);
		while ((outputLine = command.getOutputLine()) != null) {
			// twActionDialog.setProgressBarValue(count++);
			twActionDialog.setStatusMsg("Bringover ..");
			twActionDialog.appendOutput(outputLine);
			if (stopAction) {
				command.interrupt();
				return;
			}
		}
		if (command.getReturnStatus() != 0) {
			while ((outputLine = command.getErrorLine()) != null) {
				twActionDialog.appendOutput(outputLine);
				if (stopAction) {
					command.interrupt();
					return;
				}
			}
		}
	}

	protected boolean enable(Node[] nodes) {
		if (nodes[0] instanceof TwWorkspaceNode)
			return true;
		else
			return false;
	}
}
