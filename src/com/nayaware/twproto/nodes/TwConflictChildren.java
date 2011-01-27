/*
 * TwConflictChildren.java
 * Author - Winston Prakash
 */

package com.nayaware.twproto.nodes;

import java.util.*;
import java.io.*;

import org.openide.nodes.*;

import com.nayaware.twproto.dataobjects.*;

public class TwConflictChildren extends Children.Array {

	protected TwWorkspace workspace;
	protected TwConflictNode confNode;
	protected boolean isExplored = false;

	public TwConflictChildren() {
	}

	public void setConflictNode(TwConflictNode cNode) {
		confNode = cNode;
		workspace = confNode.getWorkspace();
	}

	protected void addNotify() {
		System.out.println("Add Notify Called");
		if (!isExplored) {
			LinkedList conflicts = workspace.getConflicts();
			Node[] nodes = new Node[conflicts.size()];
			for (int i = 0; i < conflicts.size(); i++) {
				String relName = (String) conflicts.get(i);
				String absName = workspace.getPath() + File.separator + relName;
				nodes[i] = new TwFileNode(absName);
			}
			this.add(nodes);
		}
	}

	protected void removeNotify() {
		System.out.println("Remove Notify Called");
	}
}
