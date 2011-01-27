/*
 * TwListView.java
 * Author  Winston Prakash
 */

package com.nayaware.twproto.ui;

import java.awt.*;
import java.beans.*;
import javax.swing.tree.*;

import org.openide.explorer.*;
import org.openide.explorer.view.*;
import org.openide.nodes.*;

import com.nayaware.twproto.nodes.*;

public class TwListView extends ListView {

	Node filterNode = null;
	Node rootNode = null;

	/** Creates new TwListView */
	public TwListView() {
	}

	protected boolean selectionAccept(Node[] nodes) {
		return true;
	}

	protected void selectionChanged(Node[] nodes, ExplorerManager em)
			throws PropertyVetoException {
		em.setSelectedNodes(nodes);
		/*
		 * Children children; if(nodes.length == 0) return; if(filterNode !=
		 * null){ children = filterNode.getChildren(); if(nodes[0] instanceof
		 * TwFileNode) { System.out.println("Adding child");
		 * children.add(nodes); } Node child =
		 * children.findChild(nodes[0].getName()); if(child != null) { Node[]
		 * nodes1 = {child}; em.setSelectedNodes(nodes1); } }
		 */
	}

	protected void showSelection(int[] indexes) {
		list.setSelectedIndices(indexes);
	}

	protected NodeListModel createModel() {
		// return new TwNodeListModel();
		return new NodeListModel();
	}

	public void showParent() {
	}

	class TwNodeListModel extends NodeListModel {
		public void setNode(final Node root) {
			if (root instanceof TwFilterNode) {
				filterNode = root;
				rootNode = ((TwFilterNode) root).getOriginalNode();
			} else {
				filterNode = null;
				rootNode = root;
			}
			super.setNode(rootNode);
		}
	}
}
