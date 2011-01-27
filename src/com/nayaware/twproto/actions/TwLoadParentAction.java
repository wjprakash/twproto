/*
 * TwLoadParentAction.java
 * Author - Winston Prakash
 */

package com.nayaware.twproto.actions;

import org.openide.nodes.*;

import com.nayaware.twproto.dataobjects.*;
import com.nayaware.twproto.events.*;
import com.nayaware.twproto.nodes.*;
import com.nayaware.twproto.ui.*;
import com.nayaware.twproto.util.*;

public class TwLoadParentAction extends TwAction {

	/** Creates new TwLoadParentAction */
	public TwLoadParentAction() {
		super("Load Parent");
		setType(TwResources.TW_LOAD_PARENT_ACTION);
	}

	protected void performAction(Node[] nodes) {
		if (nodes.length > 0) {
			Node node = null;
			if (nodes[0] instanceof TwFilterNode)
				node = ((TwFilterNode) nodes[0]).getOriginalNode();
			else
				node = nodes[0];
			if (node instanceof TwWorkspaceNode) {
				TwWorkspaceNode wsNode = (TwWorkspaceNode) node;

				TwWorkspaceObject wsObj = wsNode.getWorkspace();
				// node.findChild(newChildren[0]);
				// if (found) { // donot add
				if (wsObj != null) {
					TwWorkspace wsParent = wsObj.getParent();
					if (wsParent != null) {
						System.out.println("Parent: " + wsParent.getPath());
						TwWorkspaceNode workspaceNode = new TwWorkspaceNode(
								wsParent.getPath());
						Node[] newchildren = { workspaceNode };
						Node parentNode = wsNode.getParentNode();
						parentNode.getChildren().add(newchildren);
					}
				}
			}
		}
	}

	protected boolean enable(Node[] nodes) {
		if (nodes.length > 0) {
			Node node = null;
			if (nodes[0] instanceof TwFilterNode)
				node = ((TwFilterNode) nodes[0]).getOriginalNode();
			else
				node = nodes[0];
			if (node instanceof TwWorkspaceNode) {
				TwWorkspaceNode wsNode = (TwWorkspaceNode) node;

				TwWorkspace wsObj = wsNode.getWorkspace();
				if (wsObj != null) {
					TwWorkspace wsParent = wsObj.getParent();
					if (wsParent != null) {
						return true;
					} else {
						return false;
					}
				}
			}
		}
		return false;
	}
}
