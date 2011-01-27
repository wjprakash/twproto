/*
 * TwFileProperties.java
 * Author - Winston Prakash
 */
package com.nayaware.twproto.properties;

import org.openide.nodes.*;
import java.util.*;

import com.nayaware.twproto.dataobjects.*;
import com.nayaware.twproto.nodes.*;
import com.nayaware.twproto.util.*;

public class TwFileProperties {

	Sheet sheet;
	TwFileObject file;

	/** Creates new TestProperties */
	public TwFileProperties(TwFileObject fObj) {
		sheet = Sheet.createDefault();
		file = fObj;
	}

	public Sheet createSheet() {

		if (file == null)
			return sheet;

		Sheet.Set propSet = sheet.get(Sheet.PROPERTIES);
		propSet.setDisplayName("File Properties");
		propSet.setName("FileProperties");
		PropertySupport prop;

		prop = TwUtils.createProperty("name", String.class, "Name",
				"File Name", true, false, file.getName());
		propSet.put(prop);
		prop = TwUtils.createProperty("path", String.class, "Path",
				"File Path", true, false, file.getPath());
		propSet.put(prop);
		String status = "Checkin Needed";
		if (!file.isUnderVC()) {
			if (file.isCheckedOut())
				status = "Checked Out";
			else
				status = "Checked In";
		}
		prop = TwUtils.createProperty("status", String.class, "Status",
				"File Status", true, false, status);
		propSet.put(prop);

		String lastUser = file.getLastUserString();
		prop = TwUtils.createProperty("user", String.class, "Last User",
				"Last USer of the File", true, false, lastUser);
		propSet.put(prop);

		String comment = file.getCommentString();
		prop = TwUtils.createProperty("comment", String.class, "Comment",
				"Comment from the last Delta", true, false, comment);
		propSet.put(prop);

		String version = file.getVersionString();
		prop = TwUtils.createProperty("version", String.class,
				"Latest Version", "Version of the latest Delta", true, false,
				version);
		propSet.put(prop);

		sheet.put(propSet);

		return sheet;
	}
}
