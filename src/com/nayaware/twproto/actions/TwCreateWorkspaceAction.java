/*
 * TwCreateWorkspaceAction.java
 * Author Winston Prakash
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

public class TwCreateWorkspaceAction extends TwAction {

	TwWorkspaceNode workspaceNode;

	/** Creates new TwNewChildWorkspaceAction */
	public TwCreateWorkspaceAction() {
		super("New Workspace ..");
		setType(TwResources.TW_NEW_WS_ACTION);
	}

	public void performAction(Node[] nodes) {
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
						chooser.setType(TwFileChooser.DIRECTORY_CHOOSER);
						chooser.show();
						File file = chooser.getSelection();
						if (file != null) {
							TwWorkspace ws = new TwWorkspaceObject(
									file.getAbsolutePath());
							try {
								if (ws.exists()) {
									JOptionPane.showMessageDialog(
											null,
											"Workspace Already Exists"
													+ ws.getName());
								} else {
									ws.create();
									TwWorkspaceNode workspaceNode = new TwWorkspaceNode(
											ws.getPath());
									Node[] newchildren = { workspaceNode };
									listNode.getChildren().add(newchildren);
								}
							} catch (Exception ex) {
								JOptionPane.showMessageDialog(
										null,
										"Error occured while accessing "
												+ ws.getName());
							}
						} else {
							JOptionPane.showMessageDialog(null,
									"Null File Selected");
						}
					}
				};

				Thread thr = new Thread(runnable);
				thr.start();
			}
		}
	}

	protected boolean enable(Node[] nodes) {
		if (nodes[0] instanceof TwLocalWorkspaceListNode)
			return true;
		else
			return false;
	}
}
