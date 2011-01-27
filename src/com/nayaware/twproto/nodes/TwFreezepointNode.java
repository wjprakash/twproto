/*
 * TwFreezepointNode.java
 * Author - Winston Prakash
 */

package com.nayaware.twproto.nodes;

import java.io.*;
import java.util.*;

import org.openide.actions.*;
import org.openide.nodes.*;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.SystemAction;
import org.openide.util.datatransfer.*;

import com.nayaware.twproto.actions.*;
import com.nayaware.twproto.properties.*;
import com.nayaware.twproto.util.TwResources;

public class TwFreezepointNode extends TwNode {

	protected TwWorkspaceNode workspaceNode;

	public TwFreezepointNode(String fileName) {
		super(Children.LEAF, fileName);
		setName("TwFreezepointNode");
		setDisplayName(workspaceNode.getName() + " Freezepoints");
		setShortDescription(" Freezepoint in workspace - "
				+ workspaceNode.getName());
		setType(TwResources.TW_FREEZEPOINT_LIST_NODE);
		setDefaultAction(SystemAction.get(PropertiesAction.class));
	}

	public void setWorkspaceNode(TwWorkspaceNode ws) {
		workspaceNode = ws;
	}

	public void create() throws IOException {
		File file = getFile();
		if (!file.exists())
			file.createNewFile();
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
				SystemAction.get(PropertiesAction.class), };
	}

	public TwWorkspaceNode getWorkspaceNode() {
		return workspaceNode;
	}

	public HelpCtx getHelpCtx() {
		return HelpCtx.DEFAULT_HELP;

	}

	protected TwFreezepointChildren getFreezepointChildren() {
		return (TwFreezepointChildren) getChildren();
	}

	// Create a property sheet:
	protected Sheet createSheet() {
		TwFreezepointProperties freezepointProperties = new TwFreezepointProperties(
				this);
		return freezepointProperties.createSheet();
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
