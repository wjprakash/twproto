/*
 * TwDirectoryProperties.java
 * Author - Winston Prakash
 */

package com.nayaware.twproto.properties;

import org.openide.nodes.*;
import java.util.*;

import com.nayaware.twproto.nodes.*;

public class TwDirectoryProperties {

	Sheet sheet;

	/** Creates new TestProperties */
	public TwDirectoryProperties(TwDirectoryNode node) {
		sheet = Sheet.createDefault();
	}

	public Sheet createSheet() {
		Sheet.Set propSet = sheet.get(Sheet.PROPERTIES);
		propSet.setDisplayName("Child Properties");
		propSet.setName("MainProperties");
		PropertySupport prop;
		prop = new PropertySupport.ReadOnly("Child name", String.class,
				"Test Property", "Test Short Description") {
			public Object getValue() {
				return "Test Name";
			}
		};
		propSet.put(prop);
		prop = new PropertySupport.ReadOnly("Child Directory", String.class,
				"Test Property", "Test Short Description") {
			public Object getValue() {
				return "Child Dir Name";
			}
		};
		propSet.put(prop);

		sheet.put(propSet);

		Sheet.Set propSet1 = new Sheet.Set();
		propSet1.setName("AuxProperties");
		propSet1.setDisplayName("Child Aux Properties");
		prop = new PropertySupport.ReadOnly("Child namex", String.class,
				"Test Property", "Test Short Description") {
			public Object getValue() {
				return "Test Namexxx";
			}
		};

		propSet1.put(prop);
		prop = new PropertySupport.ReadOnly("Child Directoryxxx", String.class,
				"Test Property", "Test Short Description") {
			public Object getValue() {
				return "Child Dir Namexxx";
			}
		};
		propSet1.put(prop);
		sheet.put(propSet1);
		return sheet;
	}
}
