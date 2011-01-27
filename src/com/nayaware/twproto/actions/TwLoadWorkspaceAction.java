/**
 * TwLoadWorkspaceAction
 * Author - Winston Prakash
 */

package com.nayaware.twproto.actions;

import org.openide.nodes.*;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.NodeAction;

import javax.swing.Action;
import java.io.*;

import com.nayaware.twproto.events.*;
import com.nayaware.twproto.nodes.*;
import com.nayaware.twproto.ui.*;
import com.nayaware.twproto.util.*;

public class TwLoadWorkspaceAction extends TwAction { // TwAction {

	TwWorkspaceNode workspaceNode;

	TwLoadWorkspaceAction() {
		super("Load Workspace");
		// setType(TwResources.TW_LOAD_WORKSPACE_ACTION);
	}

	protected void performAction(Node[] nodes) {
		if (nodes.length > 0) {
			Node node = null;
			if (nodes[0] instanceof TwFilterNode)
				node = ((TwFilterNode) nodes[0]).getOriginalNode();
			else
				node = nodes[0];
			if (node instanceof TwLocalWorkspaceListNode) {
				final TwLocalWorkspaceListNode listNode = (TwLocalWorkspaceListNode) node;
				Runnable runnable = new Runnable() {
					public void run() {
						TwFileChooser chooser = new TwFileChooser();
						chooser.setType(TwFileChooser.WORKSPACE_CHOOSER);
						chooser.show();
						File file = chooser.getSelection();
						if (file != null) {
							workspaceNode = new TwWorkspaceNode(
									file.getAbsolutePath());
							// node.findChild(newChildren[0]);
							// if (found) { // donot add
							Node[] newchildren = { workspaceNode };
							listNode.getChildren().add(newchildren);
						}
					}
				};

				Thread thr = new Thread(runnable);
				thr.start();
			}
		}
	}

	protected boolean enable(Node[] nodes) {
		return true;
	}

	public TwWorkspaceNode getWorkspaceNode() {
		return workspaceNode;
	}
}
