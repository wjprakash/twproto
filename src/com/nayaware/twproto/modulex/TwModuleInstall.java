/*
 * TwModuleInstall.java
 * @author  Winston Prakash
 */
package com.nayaware.twproto.modulex;

import java.awt.*;
import java.awt.event.*;

import org.openide.TopManager;
import org.openide.loaders.*;
import org.openide.windows.*;
import org.openide.util.Utilities;
import org.openide.util.NbBundle;
import org.openide.modules.ModuleInstall;

import com.nayaware.twproto.ui.*;

/**
 * Module installation class for Teamware
 */

public class TwModuleInstall extends ModuleInstall {

	public static final String WORKSPACE_NAME = "TwVersioning";
	public static final String WORKSPACE_DISPLAY_NAME = "Versioning";

	/**
	 * Create TwModuleInstall (constructor)
	 */
	public TwModuleInstall() {
	}

	/**
	 * Called when the module is installed. Installs the necessary functionality
	 */
	public void installed() {
		installWorkspace();
	}

	/**
	 * Called when the module is uninstalled. Removes all the installed
	 * functionality
	 */
	public void uninstalled() {
		uninstallWorkspace();
	}

	/**
	 * Called when the module is restored. Performs the required initializations
	 * By default updated() & installed() call this method.
	 */
	public void restored() {
		final Window mainWindow = TopManager.getDefault().getWindowManager()
				.getMainWindow();
		mainWindow.addWindowListener(new WindowAdapter() {
			public void windowOpened(WindowEvent ev) {
				// notify me no more
				mainWindow.removeWindowListener(this);
				installWorkspace();
			}
		});
	}

	/**
	 * Install versioning workspace if doesnot exist
	 */
	private void installWorkspace() {
		WindowManager wm = TopManager.getDefault().getWindowManager();
		Workspace ws = wm.findWorkspace(WORKSPACE_NAME);
		// create the workspace if not found
		if (ws == null) {
			ws = wm.createWorkspace(WORKSPACE_NAME, WORKSPACE_DISPLAY_NAME);
			Workspace[] curWorkspaces = wm.getWorkspaces();
			Workspace[] newWorkspaces = new Workspace[curWorkspaces.length + 1];
			for (int i = 0; i < curWorkspaces.length; i++) {
				newWorkspaces[i] = curWorkspaces[i];
			}
			newWorkspaces[curWorkspaces.length] = ws;
			wm.setWorkspaces(newWorkspaces);
		}
		TwNavigator twNavigator = TwNavigator.getDefault();
		twNavigator.show();
	}

	/** Remove Installed workspace, as it is no longer needed. */
	void uninstallWorkspace() {

		TwNavigator twNavigator = TwNavigator.getDefault();
		twNavigator.close();

		/*
		 * WindowManager wm = TopManager.getDefault().getWindowManager();
		 * Workspace ws = wm.findWorkspace(WORKSPACE_NAME); if (ws != null){
		 * ws.remove(); }else{ return; }
		 */
	}

	public static void main(String ignore[]) throws Exception {
		TwModuleInstall twModuleInstall = new TwModuleInstall();
		// twModuleInstall.installed();
		twModuleInstall.uninstalled();
	}
}
