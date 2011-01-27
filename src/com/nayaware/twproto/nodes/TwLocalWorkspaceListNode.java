package com.nayaware.twproto.nodes;

import org.openide.actions.*;
import org.openide.nodes.*;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.SystemAction;
import org.openide.util.datatransfer.*;

import java.beans.*;
import javax.swing.event.*;
import javax.swing.*;
import java.io.*;
import java.awt.*;

import org.openide.nodes.*;
import org.openide.util.NbBundle;

import com.nayaware.twproto.actions.*;
import com.nayaware.twproto.dataobjects.*;
import com.nayaware.twproto.nodes.*;
import com.nayaware.twproto.ui.*;
import com.nayaware.twproto.util.*;

public class TwLocalWorkspaceListNode extends TwNode {

	public TwLocalWorkspaceListNode() {
		super(new TwLocalWorkspaceListChildren());
		setDefaultAction(SystemAction.get(TwLoadWorkspaceAction.class));
		setName("TwLocalWorkspaceListNode");
		setDisplayName("Teamware Local Workspaces");
		setShortDescription("Teamware Local Workspace List Node");
		setType(TwResources.TW_LOCAL_WS_LIST_NODE);
	}

	// Create the popup menu:
	protected SystemAction[] createActions() {
		return new SystemAction[] {
				SystemAction.get(TwLoadWorkspaceAction.class),
				SystemAction.get(NewAction.class), null,
				SystemAction.get(TwShowHierarchyViewAction.class) };
	}

	public NewType[] getNewTypes() {
		return new NewType[] { new NewType() {
			public String getName() {
				return "Workspace";
			}

			public void create() throws IOException {
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
									return;
								} else {
									ws.create();
									TwWorkspaceNode workspaceNode = new TwWorkspaceNode(
											ws.getPath());
									Node[] newchildren = { workspaceNode };
									getChildren().add(newchildren);
								}
							} catch (Exception ex) {
								JOptionPane.showMessageDialog(
										null,
										"Error occured while accessing "
												+ ws.getName());
							}
						}
					}
				};

				Thread thr = new Thread(runnable);
				thr.start();
			}
		} };
	}

	public HelpCtx getHelpCtx() {
		return HelpCtx.DEFAULT_HELP;
		// return new HelpCtx (TwWorkspaceNode.class);
	}

	protected TwLocalWorkspaceListChildren getLocalWorkspaceListChildren() {
		return (TwLocalWorkspaceListChildren) getChildren();
	}

	// Handle renaming:

	public boolean canRename() {
		return false;
	}

	// Handle deleting:

	public boolean canDestroy() {
		return false;
	}
}
