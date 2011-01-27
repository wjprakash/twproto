/*
 * TwBringoverAction.java
 * Author - Winston Prakash
 */

package com.nayaware.twproto.actions;

import java.util.*;
import javax.swing.*;
import java.io.*;

import org.openide.nodes.Node;

import com.nayaware.twproto.dataobjects.*;
import com.nayaware.twproto.nodes.*;
import com.nayaware.twproto.ui.*;
import com.nayaware.twproto.util.*;

import com.sun.teamware.lib.common.*;

public class TwBringoverAction extends TwAction {

	TwTransactionPanel transactionPanel = new TwTransactionPanel(
			TwTransactionPanel.BRINGOVER_UPDATE);

	/** Creates new TwBringoverAction */
	public TwBringoverAction() {
		super("Bringover ..");
		setType(TwResources.TW_BRINGOVER_ACTION);
		twActionDialog = new TwActionDialog(new javax.swing.JFrame(), true);
		twActionDialog.setTitle("Teamware:Bringover");
		twActionDialog.setUserPanel(transactionPanel);
	}

	public void performAction(Node[] nodes) {
		if (nodes.length > 0) {
			Node node = null;
			if (nodes[0] instanceof TwFilterNode)
				node = ((TwFilterNode) nodes[0]).getOriginalNode();
			else
				node = nodes[0];
			if (node instanceof TwWorkspaceNode) {
				TwWorkspaceNode wsNode = (TwWorkspaceNode) node;
				// Set waiting cursor if needed
				twActionDialog.clearOutput();
				transactionPanel.setWorkspace(wsNode.getWorkspace());
				showActionDialog();
			} else {
				JOptionPane.showMessageDialog(null, node.getName()
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

		String classDir = TwUtil.getClassLocation(this);
		if (classDir == null) {
			JOptionPane.showMessageDialog(null,
					"Could not find the native command");
			return;
		}
		String platform = TwUtil.getPlatformVariant();
		String cmdName = null;
		if (platform.equals("intel-win"))
			cmdName = "bringover.exe";
		else
			cmdName = "bringover";

		String cmdPath = classDir + File.separator + ".." + File.separator
				+ "extbin" + File.separator + cmdName;
		File file = new File(cmdPath);
		if (!file.exists()) {
			JOptionPane.showMessageDialog(null,
					"Could not find the native command");
			return;
		}

		String cmd = file.getAbsolutePath() + " -n -w " + childWs + " -p "
				+ parentWs + " " + selection;
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
		if (nodes.length > 0) {
			Node node = null;
			if (nodes[0] instanceof TwFilterNode)
				node = ((TwFilterNode) nodes[0]).getOriginalNode();
			else
				node = nodes[0];
			if (node instanceof TwWorkspaceNode) {
				TwWorkspaceNode wsNode = (TwWorkspaceNode) node;
				if (wsNode.getWorkspace().hasParent())
					return true;
				else
					return false;
			}
			return false;
		}
		return false;
	}
}
