/*
 * TwConflictNode.java
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
import com.nayaware.twproto.dataobjects.*;
import com.nayaware.twproto.properties.*;
import com.nayaware.twproto.util.*;
import com.sun.teamware.lib.cmm.*;

public class TwConflictNode extends TwNode {

	protected TwWorkspace workspace;

	public TwConflictNode(TwWorkspace ws) {
		super(new TwConflictChildren());
		getConflictChildren().setConflictNode(this);
		workspace = ws;
		setName("TwConflictNode");
		setDisplayName(workspace.getName() + " conflicts");
		setShortDescription(" Conflicts in workspace - " + workspace.getName());
		setType(TwResources.TW_CONFLICT_LIST_NODE);
		setDefaultAction(SystemAction.get(PropertiesAction.class));
	}

	// Create the popup menu:
	protected SystemAction[] createActions() {
		return new SystemAction[] {
				SystemAction.get(OpenLocalExplorerAction.class), null,
				SystemAction.get(TwResolveFileAction.class), null,
				SystemAction.get(CutAction.class),
				SystemAction.get(CopyAction.class),
				SystemAction.get(PasteAction.class), null,
				SystemAction.get(DeleteAction.class),
				SystemAction.get(RenameAction.class), null,
				SystemAction.get(PropertiesAction.class), };
	}

	public TwWorkspace getWorkspace() {
		return workspace;
	}

	public HelpCtx getHelpCtx() {
		return HelpCtx.DEFAULT_HELP;

	}

	protected TwConflictChildren getConflictChildren() {
		return (TwConflictChildren) getChildren();
	}

	// Create a property sheet:
	protected Sheet createSheet() {
		TwConflictProperties conflictProperties = new TwConflictProperties(this);
		return conflictProperties.createSheet();
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
