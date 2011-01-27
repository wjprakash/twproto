package com.nayaware.twproto.util;

/*
 * TwImageFactory.java
 * @author  Winston Prakash
 */

import java.net.URL;
import java.awt.Image;
import java.awt.Toolkit;
import java.beans.BeanInfo;
import javax.swing.ImageIcon;

public abstract class TwResources {

	private static final String ICON_BASE = "com/sun/teamware/resources/";

	// Temware object Icon ID

	public static final int TW_UNKNOWN_NODE = 0;
	public static final int TW_DESKTOP_NODE = 1;
	public static final int TW_FILE_NODE = 2;
	public static final int TW_DIRECTORY_NODE = 3;
	public static final int TW_WORKSPACE_NODE = 4;
	public static final int TW_REPOSITORY_WS_NODE = 5;
	public static final int TW_WORKSPACE_DIR_NODE = 6;
	public static final int TW_LOCAL_WS_LIST_NODE = 7;
	public static final int TW_REPOSITORY_WS_LIST_NODE = 8;
	public static final int TW_REPOSITORY_LIST_NODE = 9;
	public static final int TW_REPOSITORY_NODE = 10;
	public static final int TW_FREEZEPOINT_NODE = 11;
	public static final int TW_FREEZEPOINT_LIST_NODE = 12;
	public static final int TW_CONFLICT_LIST_NODE = 13;
	public static final int TW_USER_NODE = 14;
	public static final int TW_COMMENT_NODE = 15;
	public static final int TW_FILE_CHECKEDIN_NODE = 16;
	public static final int TW_FILE_CHECKEDOUT_NODE = 17;
	public static final int TW_FILE_CONFLICT_NODE = 18;

	// Temware Action Icon ID
	public static final int TW_UNKNOWN_ACTION = 100;
	public static final int TW_NEW_WS_ACTION = 101;
	public static final int TW_LOAD_WORKSPACE_ACTION = 102;
	public static final int TW_UNLOAD_WORKSPACE_ACTION = 103;
	public static final int TW_ADD_FILES_TO_WORKSPACE_ACTION = 104;
	public static final int TW_BRINGOVER_ACTION = 105;
	public static final int TW_PUTBACK_ACTION = 106;
	public static final int TW_CHECKIN_ACTION = 107;
	public static final int TW_CHECKOUT_ACTION = 108;
	public static final int TW_UNCHECKOUT_ACTION = 109;
	public static final int TW_LIST_VIEW_ACTION = 110;
	public static final int TW_DETAIL_VIEW_ACTION = 111;
	public static final int TW_SHOW_NAVIGATOR_ACTION = 112;

	public static final int TW_LOAD_PARENT_ACTION = 112;
	public static final int TW_LOAD_CHILDREN_ACTION = 113;
	public static final int TW_REPORT_WORKSPACE_DIFF_ACTION = 114;
	public static final int TW_REPORT_PUTBACK_COMMENTS_ACTION = 115;
	public static final int TW_TOOL_FILEMERGE_ACTION = 116;
	public static final int TW_TOOL_IMPORT_ACTION = 117;
	public static final int TW_EDIT_LOCKS_ACTION = 118;
	public static final int TW_MOVE_WORKSPACE_ACTION = 119;
	public static final int TW_RENAME_WORKSPACE_ACTION = 120;
	public static final int TW_CHANGE_PARENT_ACTION = 121;

	public static final int TW_VIEW_ALL_FILES_ACTION = 122;
	public static final int TW_VIEW_FILES_UNDER_VC_ACTION = 123;
	public static final int TW_VIEW_FILES_NOT_UNDER_VC_ACTION = 124;
	public static final int TW_VIEW_CHECKEDIN_FILES_ACTION = 125;
	public static final int TW_VIEW_CHECKEDOUT_FILES_ACTION = 126;
	public static final int TW_VIEW_WORKSPACE_EXPLORER_ACTION = 127;
	public static final int TW_VIEW_WORKSPACE_HIERARCHY_ACTION = 128;
	public static final int TW_VIEW_OUTPUT_WINDOW_ACTION = 129;
	public static final int TW_VIEW_SESSION_LOG_ACTION = 130;
	public static final int TW_VIEW_REFRESH_ACTION = 131;

	public static final int TW_FREEZEPOINT_EXTRACT_ACTION = 132;
	public static final int TW_FREEZEPOINT_CREATE_WORKSPACE_ACTION = 133;
	public static final int TW_FREEZEPOINT_UPDATE_ACTION = 134;

	public static final int TW_CUSTOM_REPORT_CREATE_ACTION = 135;
	public static final int TW_CUSTOM_REPORT_DELETE_ACTION = 136;
	public static final int TW_REPOSITORY_CONNECT_ACTION = 137;
	public static final int TW_REPOSITORY_DISCONNECT_ACTION = 138;
	public static final int TW_UPDATE_NAMETABLE_ACTION = 139;
	public static final int TW_OPTIONS_ACTION = 140;

	public static final int TW_CUT_ACTION = 140;
	public static final int TW_PASTE_ACTION = 141;
	public static final int TW_FIND_ACTION = 142;
	public static final int TW_UNDO_WORKSPACE_ACTION = 143;
	public static final int TW_SELECT_CHECKEDOUT_FILES_ACTION = 144;
	public static final int TW_SELECT_ALL_FILE_ACTION = 145;

	public static final int TW_NEW_DIRECTORY_ACTION = 146;
	public static final int TW_NEW_CHILD_WORKSPACE_ACTION = 147;
	public static final int TW_NEW_FREEZEPOINT_ACTION = 148;
	public static final int TW_COPY_ACTION = 149;
	public static final int TW_SELECT_CHECKEDIN_FILES_ACTION = 150;
	public static final int TW_DELETE_ACTION = 151;
	public static final int TW_CHECKOUT_OPEN_ACTION = 152;

	// Teamware Misc Icon ID

	public static final int TW_UNKNOWN = 200;
	public static final int TW_UP_DIR = 201;
	public static final int TW_HOME_DIR = 202;
	public static final int TW_LIST_VIEW = 203;
	public static final int TW_DETAIL_VIEW = 204;
	public static final int TW_SPLASH_SCREEN = 205;

	static String[] nodeIcon16File = { "unknown.gif", "desktop.gif",
			"file.gif", "dir.gif", "ws_local.gif", "ws_repository.gif",
			"ws_dir.gif", "TwLocalWorkspaceList.gif", "repository_ws_list.gif",
			"repository_list.gif", "repository.gif", "freezepoint.gif",
			"freezepoint_list.gif", "conflict.gif", "user.gif", "comment.gif",
			"file_ci.gif", "file_co.gif", "file_conf.gif" };
	static String[] nodeIcon32File = { "unknown.gif", "desktop.gif",
			"file.gif", "dir.gif", "ws_local.gif", "ws_repository.gif",
			"ws_dir.gif", "TwLocalWorkspaceList32.gif",
			"repository_ws_list.gif", "repository_list.gif", "repository.gif",
			"freezepoint.gif", "freezepoint_list.gif", "conflict.gif",
			"user.gif", "comment.gif", "file_ci.gif", "file_co.gif",
			"file_conf.gif" };

	static String[] actionIcon16File = { "unknown.gif", "newwinAction.gif",
			"newWorkspaceAction.gif", "checkinAction.gif",
			"checkoutAction.gif", "cancelCheckoutAction.gif",
			"bringoverAction.gif", "putbackAction.gif", "list_view.gif",
			"detail_view.gif", "Navigator.gif" };

	static String[] actionIcon32File = { "unknown.gif", "newwinAction.gif",
			"newWorkspaceAction.gif", "checkinAction.gif",
			"checkoutAction.gif", "cancelCheckoutAction.gif",
			"bringoverAction.gif", "putbackAction.gif", "list_view.gif",
			"detail_view.gif", "Navigator.gif" };

	static String[] miscIcon16File = { "unknown.gif", "up_dir.gif",
			"home_dir.gif", "list_view.gif", "detail_view.gif", "twsplash.gif" };
	static String[] miscIcon32File = { "unknown.gif", "up_dir.gif",
			"home_dir.gif", "list_view.gif", "detail_view.gif", "twsplash.gif" };
	static String[] badgeFile = { "unknown_badge.gif", "checkedin_badge.gif",
			"checkedout_badge.gif" };

	public static Image getIcon(int iconId, int type) {
		Image icon = null;
		URL iconURL = null;

		if (iconId < 100) {
			if ((type == BeanInfo.ICON_COLOR_16x16)
					|| (type == BeanInfo.ICON_MONO_16x16)) {
				iconURL = TwResources.class.getClassLoader().getResource(
						ICON_BASE + nodeIcon16File[iconId]);
			} else {
				iconURL = TwResources.class.getClassLoader().getResource(
						ICON_BASE + nodeIcon32File[iconId]);
			}
		}
		if (iconId >= 100 && iconId < 200) {
			if ((type == BeanInfo.ICON_COLOR_16x16)
					|| (type == BeanInfo.ICON_MONO_16x16)) {
				iconURL = TwResources.class.getClassLoader().getResource(
						ICON_BASE + actionIcon16File[iconId - 100]);
			} else {
				iconURL = TwResources.class.getClassLoader().getResource(
						ICON_BASE + actionIcon32File[iconId - 100]);
			}
		}
		if (iconId >= 200 && iconId < 300) {
			if ((type == BeanInfo.ICON_COLOR_16x16)
					|| (type == BeanInfo.ICON_MONO_16x16)) {
				iconURL = TwResources.class.getClassLoader().getResource(
						ICON_BASE + miscIcon16File[iconId - 200]);
			} else {
				iconURL = TwResources.class.getClassLoader().getResource(
						ICON_BASE + miscIcon32File[iconId - 200]);
			}
		}
		if (iconId >= 300 && iconId < 400) {
			iconURL = TwResources.class.getClassLoader().getResource(
					ICON_BASE + badgeFile[iconId - 300]);
		}
		if (iconURL != null) {
			icon = java.awt.Toolkit.getDefaultToolkit().getImage(iconURL);
		}
		return (icon);
	}

	public static ImageIcon getImageIcon(int iconId, int type) {
		ImageIcon icon = null;
		URL iconURL = null;

		if (iconId < 100) {
			if ((type == BeanInfo.ICON_COLOR_16x16)
					|| (type == BeanInfo.ICON_MONO_16x16)) {
				iconURL = TwResources.class.getClassLoader().getResource(
						ICON_BASE + nodeIcon16File[iconId]);
			} else {
				iconURL = TwResources.class.getClassLoader().getResource(
						ICON_BASE + nodeIcon32File[iconId]);
			}
		}
		if (iconId >= 100 && iconId < 200) {
			if ((type == BeanInfo.ICON_COLOR_16x16)
					|| (type == BeanInfo.ICON_MONO_16x16)) {
				iconURL = TwResources.class.getClassLoader().getResource(
						ICON_BASE + actionIcon16File[iconId - 100]);
			} else {
				iconURL = TwResources.class.getClassLoader().getResource(
						ICON_BASE + actionIcon32File[iconId - 100]);
			}
		}
		if (iconId >= 200 && iconId < 300) {
			if ((type == BeanInfo.ICON_COLOR_16x16)
					|| (type == BeanInfo.ICON_MONO_16x16)) {
				iconURL = TwResources.class.getClassLoader().getResource(
						ICON_BASE + miscIcon16File[iconId - 200]);
			} else {
				iconURL = TwResources.class.getClassLoader().getResource(
						ICON_BASE + miscIcon32File[iconId - 200]);
			}
		}
		if (iconURL != null) {
			icon = new ImageIcon(iconURL);
		}
		return (icon);
	}

	public static Image getBadge(int iconId) {
		return TwResources.getIcon(iconId, BeanInfo.ICON_COLOR_16x16);
	}
}
