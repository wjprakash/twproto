/** TwFilterNode
 * Author Winston Prakash
 */

package com.nayaware.twproto.nodes;

import org.openide.nodes.*;

public class TwFilterNode extends FilterNode {

	public TwFilterNode(Node original) {
		super(original, new TwFilterNodeChildren(original));
	}

	public Node cloneNode() {
		return new TwFilterNode(getOriginal());
	}

	/*
	 * Just making the getOrigianl() public
	 */
	public Node getOriginalNode() {
		return getOriginal();
	}

}
