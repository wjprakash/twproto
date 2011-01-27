package com.nayaware.twproto.dataobjects;

import java.io.File;
import java.util.Vector;
import java.util.Iterator;
import java.util.LinkedList;

import com.sun.teamware.lib.cmm.CmmWorkspace;
import com.sun.teamware.lib.cmm.CmmMdChildren;
import com.sun.teamware.lib.cmm.CmmMdParent;
import com.sun.teamware.lib.cmm.CmmDescription;
import com.sun.teamware.lib.sc.ScCmd;

public class TwWorkspaceObject implements TwWorkspace {

	private CmmWorkspace workspace = null;
	private String fullname = null;
	private String name = null;

	/**
	 * Creates a new TwWorkspace instance from the given File.
	 */
	public TwWorkspaceObject(TwFile file) {
		fullname = file.getAbsolutePath();
		name = file.getName();
		workspace = new CmmWorkspace(fullname);
	}

	/**
	 * Creates a new TwWorkspace instance from the given filename string.
	 */
	public TwWorkspaceObject(String filename) {
		fullname = (new File(filename)).getAbsolutePath();
		name = (new File(filename)).getName();
		workspace = new CmmWorkspace(fullname);
	}

	public void create() throws Exception {
		if (!workspace.exists()) {
			workspace.createWorkspace();
		}
	}

	public boolean exists() {
		fillWs();
		try {
			return (workspace.exists());
		} catch (Exception e) {
			return (false);
		}
	}

	public void setParent(TwWorkspace parent) throws Exception {
		if (!exists()) {
			return;
		}
		workspace.setParent(new CmmWorkspace(parent.getPath()));
	}

	public TwWorkspace getParent() {
		if (!exists()) {
			return (null);
		}
		LinkedList par;
		int num = 0;
		try {
			CmmMdParent mdParent = workspace.getMdParent();
			par = mdParent.getContents();
			if ((par != null) && (par.size() > 0)) {
				String nName = (String) par.getFirst();
				TwWorkspace parent = new TwWorkspaceObject(nName);
				return (parent);
			} else {
				return null;
			}
		} catch (Exception e) {
		}
		return (null);
	}

	public TwWorkspace[] getChildren() {
		if (!exists()) {
			return (null);
		}
		LinkedList chs;
		int num = 0;
		try {
			CmmMdChildren mdChildren = workspace.getMdChildren();
			chs = mdChildren.getContents();
			if ((chs != null) && (chs.size() > 0)) {
				TwWorkspace[] children = new TwWorkspace[chs.size()];
				for (Iterator chs_i = chs.iterator(); chs_i.hasNext();) {
					String nName = (String) chs_i.next();
					children[num] = new TwWorkspaceObject(nName);
					num++;
				}
				return (children);
			} else {
				return null;
			}
		} catch (Exception e) {
		}
		return (null);
	}

	public LinkedList getConflicts() {
		try {
			if (workspace.exists()) {
				LinkedList conf = workspace.getConflicts();
				if (conf != null) {
					return conf;
				}
			}
		} catch (Exception e) {
		}
		return null;
	}

	public boolean isInConflict() {
		try {
			LinkedList conf = workspace.getConflicts();
			if ((conf != null) && (conf.size() > 0)) {
				return true;
			}
		} catch (Exception e) {
		}
		return false;
	}

	public boolean hasParent() {
		TwWorkspace parent = null;
		try {
			parent = getParent();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (parent == null)
			return false;
		else
			return true;
	}

	public boolean hasChildren() {
		TwWorkspace[] children = null;
		try {
			children = getChildren();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (children == null)
			return false;
		else
			return true;
	}

	public boolean isChild(TwWorkspace checkingWs) {
		CmmWorkspace checkingChild = checkingWs.getCmmWorkspace();
		try {
			LinkedList children = workspace.getChildren();
			if (children == null)
				return false;
			for (int i = 0; i < children.size(); i++) {
				CmmWorkspace child = (CmmWorkspace) children.get(i);
				if (child.equals(checkingChild))
					return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean isParent(TwWorkspace checkingWs) {
		CmmWorkspace checkingParent = checkingWs.getCmmWorkspace();
		try {
			CmmWorkspace parent = workspace.getParent();
			if (parent == null)
				return false;
			if (parent.equals(checkingParent))
				return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public String getDescription() {
		CmmDescription mdn;

		try {
			mdn = workspace.getDescription();
			if (mdn.getName() != null) {
				return new String(mdn.getName());
			}
		} catch (Exception ex) {
			return workspace.getName();
		}
		return null;
	}

	public CmmWorkspace getCmmWorkspace() {
		return workspace;
	}

	public void moveTo(String newLocation) throws Exception {
		fillWs();
		CmmWorkspace dest = new CmmWorkspace(newLocation);
		workspace.moveWorkspace(dest);
	}

	public void delete(boolean deleteMetadataOnly) throws Exception {
		fillWs();
		workspace.deleteWorkspace(deleteMetadataOnly);
	}

	public String getName() {
		return (name);
	}

	public String getFullName() {
		return (fullname);
	}

	public String getPath() {
		return (fullname);
	}

	public String toString() {
		return (getName());
	}

	private void fillWs() {
		workspace = new CmmWorkspace(fullname);
		fullname = workspace.getName();
	}

	public void rename(String newName) throws Exception {
		File oldFile = new File(getPath());
		File newFile = new File(oldFile.getParentFile(), newName);
		workspace.moveWorkspace(new CmmWorkspace(newFile.getAbsolutePath()));
		workspace = new CmmWorkspace(newFile.getAbsolutePath());
		fullname = newFile.getAbsolutePath();
		name = newFile.getName();
		System.out.println(getName());
	}
}
