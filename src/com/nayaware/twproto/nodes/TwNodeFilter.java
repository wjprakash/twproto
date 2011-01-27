/*
 * TwFilter.java
 * Author - Winston Prakash
 */

package com.nayaware.twproto.nodes;

import com.nayaware.twproto.dataobjects.*;
import com.nayaware.twproto.util.TwResources;

public class TwNodeFilter {

	public static final int FILES_ALL = 0;
	public static final int FILES_UNDER_VC = 1;
	public static final int FILES_NOT_UNDER_VC = 2;
	public static final int FILES_CHECKED_IN = 3;
	public static final int FILES_CHECKED_OUT = 4;

	protected int filterType;

	public TwNodeFilter(int type) {
		filterType = type;
	}

	/**
	 * Accept or reject the files based on the specified filter type
	 */
	public boolean accept(TwNode node) {
		if (node.getType() == TwResources.TW_FILE_NODE) {
			TwFileObject file = ((TwFileNode) node).getFileObject();
			switch (filterType) {
			case FILES_UNDER_VC:
				return file.isUnderVC();
			case FILES_NOT_UNDER_VC:
				return !file.isUnderVC();
			case FILES_CHECKED_IN:
				return (file.isUnderVC()) && (!file.isCheckedOut());
			case FILES_CHECKED_OUT:
				return file.isCheckedOut();
			case FILES_ALL:
				return true;
			}
		}
		return false;
	}
}
