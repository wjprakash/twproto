/*
 * TwUtilities.java
 * Author  Winston Prakash
 */

package com.nayaware.twproto.util;

import org.openide.nodes.*;

public class TwUtils {

	public static PropertySupport createProperty(String name, Class type,
			String dispName, String shortDesc, final boolean bRead,
			final boolean bWrite, final Object pValue) {
		PropertySupport prop = new PropertySupport(name, type, dispName,
				shortDesc, bRead, bWrite) {
			Object propValue = pValue;

			public Object getValue() {
				return propValue;
			}

			public void setValue(Object value) {
				propValue = value;
			}

			public boolean canWrite() {
				return bWrite;
			}

			public boolean canRead() {
				return bRead;
			}
		};
		return prop;
	}
}
