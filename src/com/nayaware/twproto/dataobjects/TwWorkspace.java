package com.nayaware.twproto.dataobjects;

import java.util.LinkedList;

import com.sun.teamware.lib.cmm.CmmWorkspace;

public interface TwWorkspace {
	public boolean exists();

	public void create() throws Exception;

	public TwWorkspace getParent();

	public TwWorkspace[] getChildren();

	public void moveTo(String newLocation) throws Exception;

	public void delete(boolean deleteMetadataOnly) throws Exception;

	public String getName();

	public String getFullName();

	public String getPath();

	public LinkedList getConflicts();

	public boolean isInConflict();

	public void setParent(TwWorkspace parent) throws Exception;

	public boolean hasParent();

	public boolean hasChildren();

	public boolean isChild(TwWorkspace ws);

	public boolean isParent(TwWorkspace ws);

	public String getDescription();

	public CmmWorkspace getCmmWorkspace();

	public void rename(String newName) throws Exception;

	public String toString();
}
