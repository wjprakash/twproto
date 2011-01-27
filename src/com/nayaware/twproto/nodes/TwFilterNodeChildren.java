/** TwFilterNodechildren.
 * Author Winston Prakash
 */

package com.nayaware.twproto.nodes;

import org.openide.nodes.*;

import com.nayaware.twproto.nodes.*;

public class TwFilterNodeChildren extends FilterNode.Children {

	public TwFilterNodeChildren(Node orig) {
		super(orig);
	}

	public Object clone() {
		return new TwFilterNodeChildren(original);
	}

	protected Node copyNode(Node child) {
		return new TwFilterNode(child);
	}

	/*
	 * Filter out the files for TreeView
	 */
	protected Node[] createNodes(Object key) {
		TwNode child = (TwNode) key;
		if (child instanceof TwFileNode) {
			return new Node[] {};
		} else {
			return new Node[] { copyNode(child) };
		}
	}
}
