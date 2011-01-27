/*
 * TwTreeView.java
 * Author  Winston Prakash
 */

package com.nayaware.twproto.ui;

import java.awt.*;
import java.beans.*;
import javax.swing.tree.*;

import org.openide.explorer.*;
import org.openide.explorer.view.*;
import org.openide.nodes.*;

public class TwTreeView extends TreeView {

	/** Creates new TwTreeView */
	public TwTreeView() {
		tree.getSelectionModel().setSelectionMode(
				TreeSelectionModel.SINGLE_TREE_SELECTION);
	}

	protected NodeTreeModel createModel() {
		return new NodeTreeModel();
	}

	protected boolean selectionAccept(Node[] nodes) {
		/*
		 * if(nodes.length > 0) { if(nodes[0].isLeaf()) return false; else
		 * return true; } return false;
		 */
		return true;
	}

	protected void showSelection(TreePath[] paths) {
		if (paths.length == 0) {
			tree.setSelectionPaths(new TreePath[0]);
		} else {
			tree.setSelectionPath(paths[0]);
		}
	}

	protected void selectionChanged(Node[] nodes, ExplorerManager em)
			throws PropertyVetoException {
		if (nodes.length > 0) {
			if (!nodes[0].isLeaf())
				em.setExploredContext(nodes[0]);
			em.setSelectedNodes(nodes);
		}
	}

	protected void showPath(TreePath path) {
		tree.makeVisible(path);
		Rectangle rect = tree.getPathBounds(path);
		if (rect != null) {
			rect.width += rect.x;
			rect.x = 0;
			tree.scrollRectToVisible(rect);
		}
		tree.setSelectionPath(path);
	}

	public void setEnabled(boolean enabled) {
		this.tree.setEnabled(enabled);
	}

	public boolean isEnabled() {
		return this.tree.isEnabled();
	}
}
