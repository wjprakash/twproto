/*
 * TwDirectoryNode.java
 * Author - Winston Prakash
 */

package com.nayaware.twproto.nodes;

import java.io.*;
import java.util.*;
import java.awt.datatransfer.*;

import org.openide.actions.*;
import org.openide.nodes.*;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.SystemAction;
import org.openide.util.datatransfer.*;

import com.nayaware.twproto.actions.*;
import com.nayaware.twproto.properties.*;
import com.nayaware.twproto.util.TwResources;

public class TwDirectoryNode extends TwNode {

	/** Creates new TwDirectoryNode */
	public TwDirectoryNode(String dirName) {
		this(dirName, false);
	}

	// Constructor to build root directories
	public TwDirectoryNode(String dirName, boolean rootDir) {
		super(new TwDirectoryChildren(), dirName);
		getDirectoryChildren().setDirectoryNode(this);
		setName("TwDirectoryNode");
		setDisplayName(getPath());
		if (rootDir == true)
			setShortDescription(" Root Directory - " + getPath());
		else
			setShortDescription("Directory - " + getPath());
		setType(TwResources.TW_DIRECTORY_NODE);
		setDefaultAction(SystemAction.get(PropertiesAction.class));
	}

	public void create() {
		File file = getFile();
		if (!file.exists())
			file.mkdir();
	}

	// Create the popup menu:
	protected SystemAction[] createActions() {
		return new SystemAction[] {
				SystemAction.get(OpenLocalExplorerAction.class), null,
				SystemAction.get(ReorderAction.class), null,
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

	}

	protected TwDirectoryChildren getDirectoryChildren() {
		return (TwDirectoryChildren) getChildren();
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
				 * getDirectoryChildren().add(nodes);
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
				 * nodes = {dirNode}; getDirectoryChildren().add(nodes);
				 */
			}
		} };
	}

	// Create a property sheet:
	protected Sheet createSheet() {
		TwDirectoryProperties directoryProperties = new TwDirectoryProperties(
				this);
		return directoryProperties.createSheet();
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
						getDirectoryChildren().add(ns1);
					}
					ns = NodeTransfer.nodes(t, NodeTransfer.COPY);
					if (ns != null) {
						Node[] ns1 = new Node[ns.length];
						for (int i = 0; i < ns.length; i++) {
							ns[i] = ns[i].cloneNode();
							ns[i].destroy();
						}
						getDirectoryChildren().add(ns1);
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
