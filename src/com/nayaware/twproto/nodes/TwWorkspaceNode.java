/*
 * TwWorkspaceNode.java
 * Author - Winston Prakash
 */

package com.nayaware.twproto.nodes;

import java.awt.*;
import java.io.*;
import java.util.*;

import org.openide.actions.*;
import org.openide.nodes.*;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.datatransfer.*;
import org.openide.util.actions.*;

import com.nayaware.twproto.actions.*;
import com.nayaware.twproto.dataobjects.*;
import com.nayaware.twproto.properties.*;
import com.nayaware.twproto.util.*;

public class TwWorkspaceNode extends TwNode {

	Image icon = null;
	protected File dir;
	protected TwWorkspaceObject workspace;

	public TwWorkspaceNode(String dirName) {
		super(new TwWorkspaceChildren(), dirName);
		workspace = new TwWorkspaceObject(getPath());
		getWorkspaceChildren().setWorkspaceNode(this);
		setType(TwResources.TW_WORKSPACE_NODE);
		setDefaultAction(SystemAction.get(PropertiesAction.class));
		refresh();
	}

	protected TwWorkspaceChildren getWorkspaceChildren() {
		return (TwWorkspaceChildren) getChildren();
	}

	// Create the popup menu:
	protected SystemAction[] createActions() {
		return new SystemAction[] {
				SystemAction.get(OpenLocalExplorerAction.class), null,
				SystemAction.get(TwUnloadWorkspaceAction.class), null,
				SystemAction.get(TwLoadParentAction.class),
				SystemAction.get(TwLoadChildrenAction.class), null,
				SystemAction.get(TwBringoverAction.class),
				SystemAction.get(TwPutbackAction.class), null,
				SystemAction.get(CutAction.class),
				SystemAction.get(CopyAction.class),
				SystemAction.get(PasteAction.class), null,
				SystemAction.get(TwDeleteAction.class),
				SystemAction.get(TwRenameWorkspaceAction.class), null,
				SystemAction.get(TwReparentWorkspaceAction.class),
				SystemAction.get(TwFindAction.class),
				SystemAction.get(TwUpdateNametableAction.class),
				SystemAction.get(TwUndoWorkspaceAction.class), null,
				SystemAction.get(NewAction.class), null,
				SystemAction.get(TwShowWorkspaceHistoryAction.class),
				SystemAction.get(PropertiesAction.class) };
	}

	public NewType[] getNewTypes() {
		return new NewType[] { new NewType() {
			public String getName() {
				// return NbBundle.getMessage (TestNode.class, "LBL_NewType");
				return "Freezepoint";
			}

			public void create() throws IOException {
				/*
				 * TwFreezepointNode fptNode = new
				 * TwFreezepointNode("New_Freezepoint"); fptNode.create();
				 * Node[] nodes = {fptNode}; getWorkspaceChildren().add(nodes);
				 */
				TwNewFreezepointAction fptAction = (TwNewFreezepointAction) SystemAction
						.get(TwNewFreezepointAction.class);
				fptAction.performAction();
			}
		}, new NewType() {
			public String getName() {
				// return NbBundle.getMessage (TestNode.class, "LBL_NewType");
				return "Child Workspace";
			}

			public void create() throws IOException {
				/*
				 * TwWorkspaceNode wsNode = new
				 * TwWorkspaceNode("New_Freezepoint"); wsNode.create(); Node[]
				 * nodes = {fptNode}; getWorkspaceChildren().add(nodes);
				 */
				TwNewChildWorkspaceAction wsAction = (TwNewChildWorkspaceAction) SystemAction
						.get(TwNewChildWorkspaceAction.class);
				wsAction.performAction();
			}
		} };
	}

	public HelpCtx getHelpCtx() {
		return HelpCtx.DEFAULT_HELP;
		// return new HelpCtx (TwWorkspaceNode.class);
	}

	// Create a property sheet:
	protected Sheet createSheet() {
		TwWorkspaceProperties workspaceProperties = new TwWorkspaceProperties(
				workspace);
		return workspaceProperties.createSheet();
	}

	public TwWorkspaceObject getWorkspace() {
		return workspace;
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

	public void refresh() {
		setFile(workspace.getPath());
		setShortDescription(" Workspace - " + getName() + "(" + getPath() + ")");
		setDisplayName(workspace.getDescription());
	}
}
