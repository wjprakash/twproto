/*
 * TwWorkspaceDirNode.java
 * Author - Winston Prakash
 */

package com.nayaware.twproto.nodes;

import java.io.*;
import java.util.*;
import java.awt.datatransfer.*;

import java.io.File;
import java.io.FileFilter;
import javax.swing.ImageIcon;
import java.util.*;
import java.io.*;

import org.openide.actions.*;
import org.openide.nodes.*;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.SystemAction;
import org.openide.util.datatransfer.*;

import com.nayaware.twproto.actions.*;
import com.nayaware.twproto.dataobjects.*;
import com.nayaware.twproto.properties.*;
import com.nayaware.twproto.util.TwResources;

public class TwWorkspaceDirNode extends TwNode {

	protected TwDirectoryNode dirNode;
	protected TwWorkspace workspace;

	public TwWorkspaceDirNode(TwWorkspace ws, String path) {
		this(ws, path, false);
		setDisplayName(getFile().getName());
		setShortDescription(getPath() + " (Workspace - " + ws.getName() + ")");
	}

	public TwWorkspaceDirNode(TwWorkspace ws, String path, boolean wsRoot) {
		super(new TwWorkspaceDirChildren(), path);
		getWorkspaceDirChildren().setWorkspaceDirNode(this);
		setDisplayName(getFile().getName());
		workspace = ws;
		if (wsRoot) {
			setType(TwResources.TW_WORKSPACE_DIR_NODE);
			setShortDescription(" Root Directory of Workspace - "
					+ ws.getName());
			setDisplayName("Contents of " + ws.getName());
		} else {
			setType(TwResources.TW_DIRECTORY_NODE);
			setShortDescription("Directory in Workspace - " + ws.getName());
		}
		setDefaultAction(SystemAction.get(PropertiesAction.class));
	}

	// Create the popup menu:
	protected SystemAction[] createActions() {
		return new SystemAction[] {
				SystemAction.get(OpenLocalExplorerAction.class), null,
				SystemAction.get(CutAction.class),
				SystemAction.get(CopyAction.class),
				SystemAction.get(PasteAction.class), null,
				SystemAction.get(DeleteAction.class),
				SystemAction.get(RenameAction.class), null,
				SystemAction.get(NewAction.class), null,
				SystemAction.get(PropertiesAction.class), };
	}

	public HelpCtx getHelpCtx() {
		return HelpCtx.DEFAULT_HELP;
		// return new HelpCtx (TwWorkspaceNode.class);
	}

	protected TwWorkspaceDirChildren getWorkspaceDirChildren() {
		return (TwWorkspaceDirChildren) getChildren();
	}

	// Create a property sheet:
	protected Sheet createSheet() {
		TwWorkspaceDirProperties workspaceDirProperties = new TwWorkspaceDirProperties(
				this);
		return workspaceDirProperties.createSheet();
	}

	public NewType[] getNewTypes() {
		return new NewType[] { new NewType() {
			public String getName() {
				// return NbBundle.getMessage (TestNode.class, "LBL_NewType");
				return "File";
			}

			public void create() throws IOException {
				/*
				 * TwFileNode fileNode = new TwFileNode("New_File");
				 * fileNode.create(); Node[] nodes = {fileNode};
				 * getWorkspaceDirChildren().add(nodes);
				 */
			}
		}, new NewType() {
			public String getName() {
				// return NbBundle.getMessage (TestNode.class, "LBL_NewType");
				return "Directory";
			}

			public void create() throws IOException {
				/*
				 * TwDirectoryNode dirNode = new
				 * TwDirectoryNode("New_Directory"); dirNode.create(); Node[]
				 * nodes = {dirNode}; getWorkspaceDirChildren().add(nodes);
				 */
			}
		} };
	}

	public void setWorkspace(TwWorkspace ws) {
		workspace = ws;
		setFile(workspace.getPath());
	}

	public TwWorkspace getWorkspace() {
		return workspace;
	}

	public boolean equals(TwNode node) {
		if (node.getType() != TwResources.TW_WORKSPACE_DIR_NODE) {
			return false;
		}
		if (workspace.equals(((TwWorkspaceDirNode) node).getWorkspace())) {
			return super.equals(node);
		} else {
			return false;
		}
	}

	protected boolean filterChild(File file) {
		String fileName = file.getName();
		if (fileName.equals("Codemgr_wsdata"))
			return true;
		if (fileName.equals("SCCS"))
			return true;
		return false;
	}

	// Permit things to be pasted into this node:
	protected void createPasteTypes(final Transferable t, List l) {

		super.createPasteTypes(t, l);

		// Check and get all the nodes that can be copied
		Node[] ns = NodeTransfer.nodes(t, NodeTransfer.COPY);

		if (ns != null) {
			for (int i = 0; i < ns.length; i++) {
				System.out.println("COPY: " + ns[i].getName());
				// Browse through the nodes and see if they can be copied
				// If so then create the PasteType for copying
				Node n2 = getChildren().findChild(ns[i].getName());
				if (n2 == ns[i]) {
					System.out.println("COPY: Node Exists");
					return;
				} // Cannot copy onto the same node

			}
		} else {

			// Check and get all the nodes that can be copied
			ns = NodeTransfer.nodes(t, NodeTransfer.MOVE);

			if (ns != null) {
				for (int i = 0; i < ns.length; i++) {
					System.out.println("MOVE: " + ns[i].getName());
					// Browse through the nodes and see if they can be moved
					if (!ns[i].canDestroy())
						return;
					Node n2 = getChildren().findChild(ns[i].getName());
					if (n2 == ns[i]) {
						System.out.println("MOVE: Node Exists");
						return;
					}// Cannot move onto the same node
						// If so then create the PasteType for copying
				}
			}
		}
		// MyCookie cookie = (MyCookie) NodeTransfer.cookie (t,
		// NodeTransfer.COPY, MyCookie.class);

		if (ns != null) {
			l.add(new PasteType() {
				public String getName() {
					// return NbBundle.getMessage (TestNode.class,
					// "LBL_PasteType");
					return "Paste";
				}

				public Transferable paste() throws IOException {
					Node[] ns = NodeTransfer.nodes(t, NodeTransfer.COPY);
					if (ns != null) {
						Node[] ns1 = new Node[ns.length];
						for (int i = 0; i < ns.length; i++) {
							ns1[i] = ns[i].cloneNode();
						}
						getWorkspaceDirChildren().add(ns1);
					}
					ns = NodeTransfer.nodes(t, NodeTransfer.COPY);
					if (ns != null) {
						Node[] ns1 = new Node[ns.length];
						for (int i = 0; i < ns.length; i++) {
							ns[i] = ns[i].cloneNode();
							ns[i].destroy();
						}
						getWorkspaceDirChildren().add(ns1);
					}
					return null; // leave the clipboard as is
					// return ExTransferable.EMPTY; // clear the clipboard:
				}
			});
		}
	}

	// Handle renaming:

	public boolean canRename() {
		return true;
	}

	public void setName(String nue) {
		super.setName(nue);
		// rename underlying object
	}

	// Handle deleting:

	public boolean canDestroy() {
		return true;
	}

	public void destroy() throws IOException {
		super.destroy();
		// delete underlying object
	}

	// Handle copying and cutting specially:

	public boolean canCopy() {
		return true;
	}

	public boolean canCut() {
		return true;
	}
}
