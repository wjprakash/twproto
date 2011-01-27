/*
 * TwWorkspaceProperties.java
 * Author - Winston Prakash
 */
package com.nayaware.twproto.properties;

import org.openide.nodes.*;
import java.util.*;

import com.nayaware.twproto.dataobjects.*;
import com.nayaware.twproto.nodes.*;
import com.nayaware.twproto.util.*;

public class TwWorkspaceProperties {

	Sheet sheet;
	TwWorkspace workspace = null;

	/** Creates new TestProperties */
	public TwWorkspaceProperties(TwWorkspace ws) {
		sheet = Sheet.createDefault();
		workspace = ws;
	}

	public Sheet createSheet() {

		if (workspace == null)
			return sheet;

		Sheet.Set propSet = sheet.get(Sheet.PROPERTIES);
		propSet.setDisplayName("Workspace Properties");
		propSet.setName("WsProperties");
		PropertySupport prop;

		prop = TwUtils.createProperty("wsName", String.class, "Name",
				"Workspace Name", true, true, workspace.getName());
		propSet.put(prop);
		prop = TwUtils.createProperty("wsPath", String.class, "Path",
				"Workspace Path", true, true, workspace.getPath());
		propSet.put(prop);
		prop = TwUtils.createProperty("wsShortDesc", String.class,
				"Short Description", "Workspace Short Description", true, true,
				workspace.getDescription());
		propSet.put(prop);
		String parent = "None";
		if (workspace.hasParent())
			parent = workspace.getParent().getName();
		prop = TwUtils.createProperty("wsParent", String.class, "Parent",
				"Workspace Parent", true, true, parent);
		propSet.put(prop);

		String children = "None";
		TwWorkspace[] wsChildren = workspace.getChildren();
		if (wsChildren != null) {
			children = "";
			for (int i = 0; i < wsChildren.length; i++) {
				children = children + wsChildren[i].getName();
			}
		}

		prop = TwUtils.createProperty("wsChildren", String.class, "Children",
				"Workspace Children", true, true, children);
		propSet.put(prop);

		sheet.put(propSet);

		return sheet;
	}
}
