package com.nayaware.twproto.util;

import java.util.*;
import javax.swing.*;

import com.sun.teamware.vertool.vtMain;
import com.sun.teamware.lib.common.TeamwareApp;
import com.sun.teamware.lib.common.TeamwareAppListener;
import com.sun.teamware.lib.common.TwUtil;

public class TwAppUtil {

	/*
	 * public static final int VERTOOL_APP = 0; public static final int
	 * CODEMGR_APP = 1; public static final int FREEZEPT_APP = 2; public static
	 * final int FILEMERGE_APP = 3;
	 */

	static TeamwareApp verTool;
	static int appType = 0;

	static public void showFileHistory(String fileName) {
		String[] args = { fileName };
		startApp(TeamwareApp.VERTOOL_APP, args);
	}

	static public TeamwareApp getVertool() {
		return verTool;
	}

	static public void setVertool(TeamwareApp vTool) {
		verTool = vTool;
	}

	static public void startApp(int aType, String[] args) {

		appType = aType;
		switch (appType) {

		case TeamwareApp.VERTOOL_APP:
			if (verTool == null) {
				verTool = TeamwareApp.createInstance(appType);
			}

			if (args == null) {
				verTool.loadApplication(new String[0]);
			} else {
				verTool.loadApplication(args);
			}

		}
	}

	static class TwAppListener implements TeamwareAppListener {

		//
		// Called when user tries to exit application.
		// We redefine exit action so that it will iconify app window.
		//
		public void applicationClosing(EventObject event) {
			// get the app instance
			TeamwareApp src = (TeamwareApp) event.getSource();

			// ask application frame and set it iconified
			// src.getApplicationFrame().setState(Frame.ICONIFIED);
			src.getApplicationFrame().dispose();
		}

		//
		// Called when application is succesfully created
		//
		public void applicationLoaded(EventObject event) {
			// footer.setText(" ");
			// get the app instance
			TeamwareApp src = (TeamwareApp) event.getSource();

			src.showApp();
		}

		//
		// Called when error occured while loading application
		//
		public void applicationFailed(EventObject event) {
			// footer.setText("Failed.");
			// System.err.println("Failed");

			// get the app instance
			TeamwareApp src = (TeamwareApp) event.getSource();

			// show error message to the user
			// JOptionPane.showMessageDialog(frame, src.getFailureReason());
			System.err.println(src.getFailureReason());

			// since application was not completely load, reset global variable.
			verTool = null;
		}
	}
}
