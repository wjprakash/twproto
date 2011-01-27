package com.nayaware.twproto.nodes;

import java.util.*;

import org.openide.nodes.*;

import com.nayaware.twproto.dataobjects.*;
import com.nayaware.twproto.properties.*;
import com.sun.teamware.lib.cmm.*;

public class TwWorkspaceChildren extends Children.Array {

	protected TwWorkspaceNode wsNode = null;
	protected TwWorkspace workspace;
	protected Node[] childrenNodes = new Node[5];
	protected boolean explored = false;

	public TwWorkspaceChildren() {
	}

	public void setWorkspaceNode(TwWorkspaceNode node) {
		wsNode = node;
		workspace = wsNode.getWorkspace();
	}

	protected void addNotify() {
		if (!explored) {
			int childCount = 0;
			if (workspace != null) {
				TwWorkspaceDirNode wsDirNode = new TwWorkspaceDirNode(
						workspace, workspace.getPath(), true);
				if (wsDirNode != null)
					childrenNodes[childCount++] = wsDirNode;
			}

			if (workspace.isInConflict()) {
				TwConflictNode confNode = new TwConflictNode(workspace);
				if (confNode != null)
					childrenNodes[childCount++] = confNode;
			}

			/*
			 * if(wsNode.hasFreezepoints()) { childrenNodes[childCount++]=new
			 * TwConflictNode(wsNode); }
			 */

			if (childCount > 0) {
				Node[] nodes = new Node[childCount];
				for (int i = 0; i < childCount; i++)
					nodes[i] = childrenNodes[i];
				add(nodes);
			}
			explored = true;
		}
	}

	protected void removeNotify() {
	}
}
