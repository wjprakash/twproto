/*
 * TwNode.java
 * Author - Winston Prakash
 */

package com.nayaware.twproto.nodes;

import java.io.*;
import javax.swing.*;
import java.util.*;
import java.awt.*;
import java.beans.*;

import org.openide.nodes.*;
import org.openide.util.NbBundle;

import com.nayaware.twproto.util.TwResources;

public abstract class TwNode extends AbstractNode {

	protected int nodeType = TwResources.TW_UNKNOWN_NODE;
	protected File file;

	protected String path = null;
	protected String relativePath = null;
	protected String fullPath = null;

	protected TwNodeFilter filter;

	protected String versionString = " ";
	protected String lastUserString = " ";
	protected String commentString = " ";

	protected Image icon;

	protected TwNode parent;

	/** Creates new TwNode */
	public TwNode(Children children) {
		super(children);
	}

	/** Creates new TwNode */
	public TwNode(Children children, String fileName) {
		this(children);
		setFile(fileName);
	}

	/** Creates new TwNode */
	public TwNode(Children children, File fullPath) {
		this(children);
		setFile(fullPath);
	}

	public void setType(int t) {
		this.nodeType = t;
		icon = null;
	}

	public int getType() {
		return nodeType;
	}

	public Image getIcon() {
		return getIcon(BeanInfo.ICON_COLOR_16x16);
	}

	public Image getIcon(int type) {
		if (icon == null)
			icon = TwResources.getIcon(getType(), type);
		return icon;
	}

	public Image getOpenedIcon(int type) {
		return getIcon(type);
	}

	public void setParent(TwNode par) {
		parent = par;
	}

	public TwNode getParent() {
		return parent;
	}

	public void setPath(String p) {
		this.path = p;
	}

	public String getPath() {
		return path;
	}

	public String getRelativePath() {
		return relativePath;
	}

	public String getFullPath() {
		return fullPath;
	}

	public String toString() {
		return getName() + "(" + getPath() + ")";
	}

	public void setFile(String fileName) {
		setFile(new File(fileName));
	}

	public void setFile(File f) {
		this.file = f;
		setPath(file.getAbsolutePath());
		setName(file.getName());
	}

	public File getFile() {
		return file;
	}

	public boolean equals(TwNode node) {
		if (this.getType() == node.getType()) {
			if (getName().equals(node.getName()))
				return (node.getPath().equals(getPath()));
		}
		return false;
	}

	public void addChild(TwNode child) {
		TwNode[] children = { child };
		getChildren().add(children);
	}

	public void addChildren(TwNode[] children) {
		getChildren().add(children);
	}

	public void removeChild(TwNode child) {
		Node[] children = { child };
		getChildren().remove(children);
	}

	public void removeChildren(TwNode[] children) {
		Node[] nodes = new Node[children.length];
		for (int i = 0; i < children.length; i++) {
			nodes[i] = children[i];
		}
		getChildren().remove(children);
	}

	public void removeAllChildren() {
		getChildren().remove(getChildren().getNodes());
	}

	/*
	 * Refresh the contents of the object
	 */
	public void refresh() {
	}

	/*
	 * Filter and Refresh the contents of the object
	 */
	public void refresh(TwNodeFilter filter) {
	}

	/**
	 * Move self onto another object. viz: File object to a directory object
	 */
	public boolean moveTo(TwNode node) {
		return false;
	}

	/**
	 * Copy self onto another objects. viz: File object onto a directory object
	 */
	public boolean copyTo(TwNode object) {
		return false;
	}

	/**
	 * Returns the string to be dispayed in the Content List table.
	 */
	public String getVersionString() {
		return versionString;
	}

	/**
	 * Returns the string to be dispayed in the Content List table.
	 */
	public String getLastUserString() {
		return lastUserString;
	}

	/**
	 * Returns the string to be dispayed in the Content List table.
	 */
	public String getCommentString() {
		return commentString;
	}
}
