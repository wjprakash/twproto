package com.nayaware.twproto.ui;

/*
 * TwNavigatorMode.java
 * @author  Winston Prakash
 */

import org.openide.TopManager;
import org.openide.windows.*;
import org.openide.util.Utilities;
import org.openide.util.NbBundle;
import org.openide.modules.ModuleInstall;

import java.awt.Rectangle;

public class TwNavigator {

	/** default top manager */
	private static TwNavigator defaultNavigator;

	private String modeName = "TwNavigator";
	private String modeDisplayName = "Teamware Navigator";

	private Workspace parentWorkspace = null;
	private static Mode navigatorMode = null;
	private static TwExplorerView twExplorerView = null;
	private static TwHierarchyView twHierarchyView = null;

	/** Creates new TwNavigatorMode */
	public TwNavigator() {
	}

	/**
	 * Get the default Teamware Navigator.
	 * 
	 * @return the default
	 */
	public static TwNavigator getDefault() {
		if (defaultNavigator != null) {
			return defaultNavigator;
		}

		return new TwNavigator();
		// return initializeNavigator();
	}

	/**
	 * Initializes the Teamware Navigator.
	 */
	private static synchronized TwNavigator initializeNavigator() {
		// synchronization check
		if (defaultNavigator != null) {
			return defaultNavigator;
		}

		try {
			// Class c = Class.forName("com.sun.teamware.module.TwNavigator");
			// defaultNavigator = (TwNavigator)c.newInstance();
			return new TwNavigator();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return defaultNavigator;
	}

	public String getModeName() {
		return modeName;
	}

	public void setModeName(String mName) {
		modeName = mName;
	}

	/**
	 * Create the Navigator Mode in the Specified Workspace
	 */
	public Mode createMode(Workspace ws) {
		if (ws != null) {
			parentWorkspace = ws;
			navigatorMode = parentWorkspace.createMode(modeName,
					modeDisplayName, null);
			if (navigatorMode != null) {
				// compute the bounds for browser
				Rectangle workingSpace = parentWorkspace.getBounds();
				Rectangle bounds = new Rectangle(workingSpace.x,
						workingSpace.y, 800, 600);
				navigatorMode.setBounds(bounds);
				return navigatorMode;
			} else {
				System.out.println("Could not create navigator Mode - "
						+ modeName);
				return null;
			}
		} else {
			System.out
					.println("Error(TwNavigator.createMode): workspace == null");
			return null;
		}
	}

	/**
	 * Show the Navigator Mode in the default Workspace
	 */
	public void show() {
		TopManager tm = TopManager.getDefault();
		WindowManager wm = tm.getWindowManager();
		parentWorkspace = wm.findWorkspace("TwVersioning");
		if (parentWorkspace == null)
			parentWorkspace = wm.getCurrentWorkspace();
		show(parentWorkspace);
	}

	/**
	 * Show the Navigator Mode in the specified Workspace
	 */
	public void show(Workspace ws) {
		if (ws != null) {
			navigatorMode = null; // For testing purpose remove later
			if (navigatorMode == null) {
				// Try creating the mode
				navigatorMode = createMode(ws);
				if (navigatorMode != null) {
					showExplorerView(ws);
					// showHierarchyView(ws);
				}

			}
		} else {
			System.out
					.println("Error(TwNavigator.show): Parent workspace == null");
			;
		}
		ws.activate();
		twExplorerView.open(ws);
		// twHierarchyView.open(ws);
		twExplorerView.requestFocus();
	}

	/**
	 * Close the TopComponents in the Navigator Mode
	 */
	public void close() {
		if (twExplorerView != null)
			twExplorerView.close();
		// if(twHierarchyView != null) twHierarchyView.close();
	}

	/**
	 * Create if not exists & dock the ExplorerView to the Navigator Mode
	 */
	public void showExplorerView(Workspace ws) {
		if (twExplorerView == null) {
			twExplorerView = new TwExplorerView();
			TopComponent tt = new TopComponent();
		}
		navigatorMode.dockInto(twExplorerView);

		// twExplorerView.open(ws);
	}

	/**
	 * Create if not exists & dock the HierarchyView to the Navigator Mode
	 */
	public void showHierarchyView(Workspace ws) {
		if (twHierarchyView == null) {
			twHierarchyView = new TwHierarchyView();
		}
		navigatorMode.dockInto(twHierarchyView);
		// twHierarchyView.open(ws);
	}

	public static void main(String ignore[]) throws Exception {
		TopManager tm = TopManager.getDefault();
		WindowManager wm = tm.getWindowManager();
		Workspace ws = wm.getCurrentWorkspace();
		TwNavigator twNavigator = TwNavigator.getDefault();
		twNavigator.show(ws);
	}
}
