/*
 * TwFreezepointChildren.java
 * Author - Winston Prakash
 */

package com.nayaware.twproto.nodes;

import java.util.*;
import java.io.*;

import org.openide.nodes.*;

public class TwFreezepointChildren extends Children.Array {

	protected TwWorkspaceNode workspaceNode;
	protected boolean isExplored = false;

	public TwFreezepointChildren() {
	}

	public void setWorkspaceNode(TwWorkspaceNode wsNode) {
		workspaceNode = wsNode;
	}

	protected void addNotify() {
		System.out.println("Add Notify Called");
		if (!isExplored) {
		}
	}

	protected void removeNotify() {
		System.out.println("Remove Notify Called");
	}
}
