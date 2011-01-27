/*
 * TwConflictChildren.java
 * Author - Winston Prakash
 */

package com.nayaware.twproto.nodes;

import java.util.*;
import java.io.*;

import org.openide.nodes.*;

import com.nayaware.twproto.util.*;

public class TwDirectoryChildren extends Children.Array {

	protected TwDirectoryNode dirNode;
	protected File dir;
	protected boolean isExplored = false;

	public TwDirectoryChildren() {
	}

	public void setDirectoryNode(TwDirectoryNode node) {
		dirNode = node;
		dir = dirNode.getFile();
	}

	protected void addNotify() {
		System.out.println("Add Notify Called");
		if (!isExplored) {
			File[] files = dir.listFiles();
			if (files == null)
				return;
			TwNode[] conts = new TwNode[files.length];
			for (int i = 0; i < conts.length; i++) {
				if (files[i].isDirectory()) {
					conts[i] = new TwDirectoryNode(files[i].getAbsolutePath());
					File checkDir = new File(files[i], "Codemgr_wsdata");
					if (checkDir.exists()) {
						conts[i].setType(TwResources.TW_WORKSPACE_DIR_NODE);
					}
				} else {
					conts[i] = new TwFileNode(files[i].getAbsolutePath());
				}
			}
			this.add(conts);
		}
	}

	protected void removeNotify() {
		System.out.println("Remove Notify Called");
	}
}
