/*
 * TwWorkspaceDirChildren.java
 * Author - Winston Prakash
 */

package com.nayaware.twproto.nodes;

import java.util.*;
import java.io.*;

import org.openide.nodes.*;

public class TwWorkspaceDirChildren extends Children.Array {

	protected TwWorkspaceDirNode dirNode;
	protected boolean isExplored = false;

	public TwWorkspaceDirChildren() {
	}

	public void setWorkspaceDirNode(TwWorkspaceDirNode dNode) {
		dirNode = dNode;
	}

	protected void addNotify() {
		System.out.println("Add Notify Called");
		if (!isExplored) {
			int i = 0;

			File dir = dirNode.getFile();
			File[] files = dir.listFiles();
			if (files != null) {
				HashSet children = new HashSet();
				for (i = 0; i < files.length; i++) {
					if (files[i].isDirectory()) {
						if (!filterChild(files[i])) {
							children.add(new TwWorkspaceDirNode(dirNode
									.getWorkspace(), files[i].getAbsolutePath()));
						}
					} else {
						children.add(new TwFileNode(files[i].getAbsolutePath()));
					}
				}
				Node[] nodes = new Node[children.size()];
				Iterator iter = children.iterator();
				i = 0;
				while (iter.hasNext()) {
					nodes[i++] = (Node) iter.next();
				}
				this.add(nodes);
			}
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

	protected void removeNotify() {
		System.out.println("Remove Notify Called");
	}
}
