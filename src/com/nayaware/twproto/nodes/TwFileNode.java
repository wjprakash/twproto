/*
 * TwFileNode.java
 * Author - Winston Prakash
 */

package com.nayaware.twproto.nodes;

import java.io.*;

import org.openide.actions.*;
import org.openide.nodes.*;
import org.openide.util.*;
import org.openide.util.actions.*;
import org.openide.util.datatransfer.*;

import com.nayaware.twproto.actions.*;
import com.nayaware.twproto.dataobjects.*;
import com.nayaware.twproto.properties.*;
import com.nayaware.twproto.util.TwResources;

public class TwFileNode extends TwNode {
	TwFileObject fileObject;

	/** Creates new TwFilel */
	public TwFileNode(String fileName) {
		super(Children.LEAF, fileName);
		fileObject = new TwFileObject(fileName);
		setType(TwResources.TW_FILE_NODE);
		setDefaultAction(SystemAction.get(PropertiesAction.class));
		refresh();
	}

	// Create the popup menu:
	protected SystemAction[] createActions() {
		return new SystemAction[] {
				SystemAction.get(OpenLocalExplorerAction.class), null,
				SystemAction.get(TwShowFileHistoryAction.class), null,
				SystemAction.get(TwCheckinAction.class),
				SystemAction.get(TwCheckoutAction.class),
				SystemAction.get(TwCheckoutOpenAction.class),
				SystemAction.get(TwUncheckoutAction.class), null,
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

	// Create a property sheet:
	protected Sheet createSheet() {
		TwFileProperties fileProperties = new TwFileProperties(getFileObject());
		return fileProperties.createSheet();
	}

	public TwFileObject getFileObject() {
		return fileObject;
	}

	public void refresh() {
		setFile(fileObject.getName());
		setDisplayName(getFile().getName());
		setShortDescription(" File: " + getPath());
		if (fileObject.isUnderVC()) {
			setType(TwResources.TW_FILE_CHECKEDIN_NODE);
			setName(getFile().getName() + "(Checked in)");
			if (fileObject.isCheckedOut()) {
				setType(TwResources.TW_FILE_CHECKEDOUT_NODE);
				setName(getFile().getName() + "(Checked out)");
			}
			if (fileObject.isInConflict()) {
				setType(TwResources.TW_FILE_CONFLICT_NODE);
				setName(getFile().getName() + "(In Conflict)");
			}
		} else {
			setName(getFile().getName() + "(Checkin Needed)");
		}
	}

	public boolean moveTo(TwNode obj) {
		switch (obj.getType()) {
		case TwResources.TW_FILE_NODE:
			break;
		case TwResources.TW_DIRECTORY_NODE:
			TwDirectoryNode dirNode = (TwDirectoryNode) obj;
			String toDir = dirNode.getPath();
			if (!fileObject.moveTo(toDir))
				return false;
			refresh();
			break;
		case TwResources.TW_WORKSPACE_DIR_NODE:
			TwWorkspaceDirNode wsDirNode = (TwWorkspaceDirNode) obj;
			toDir = wsDirNode.getPath();
			if (!fileObject.moveTo(toDir))
				return false;
			refresh();
			break;
		case TwResources.TW_WORKSPACE_NODE:
			TwWorkspaceNode workspaceNode = (TwWorkspaceNode) obj;
			toDir = workspaceNode.getPath();
			if (!fileObject.moveTo(toDir))
				return false;
			refresh();
		default:
			return false;
		}
		return true;
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
