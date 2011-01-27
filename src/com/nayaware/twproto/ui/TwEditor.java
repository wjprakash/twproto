/*
 * TwEditor.java
 * @author  Winston Prakash
 */

package com.nayaware.twproto.ui;

import org.openide.TopManager;
import org.openide.windows.*;
import org.openide.util.Utilities;
import org.openide.util.NbBundle;
import org.openide.modules.ModuleInstall;

import java.awt.Rectangle;

public class TwEditor {

	/** default Teamware Editor */
	private static TwEditor defaultEditor;

	private String modeName = "TwEditor";
	private String modeDisplayName = "Teamware Editor";

	private Mode editorMode = null;

	/** Creates new TwEditorMode */
	public TwEditor() {
	}

	/**
	 * Get the default Teamware Editor.
	 * 
	 * @return the default
	 */
	public static TwEditor getDefault() {
		if (defaultEditor != null) {
			return defaultEditor;
		}

		return new TwEditor();
		// return initializeEditor();
	}

	/**
	 * Initializes the Teamware Editor.
	 */
	private static synchronized TwEditor initializeEditor() {
		// synchronization check
		if (defaultEditor != null) {
			return defaultEditor;
		}

		try {
			// Class c = Class.forName("com.sun.teamware.module.TwEditor");
			// defaultEditor = (TwEditor)c.newInstance();
			return new TwEditor();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return defaultEditor;
	}

	public String getModeName() {
		return modeName;
	}

	public void setModeName(String mName) {
		modeName = mName;
	}

	/**
	 * Create the Editor Mode in the Specified Workspace
	 */
	public Mode createMode(Workspace ws) {
		if (ws != null) {
			editorMode = ws.createMode(modeName, modeDisplayName, null);
			if (editorMode != null) {
				// compute the bounds for browser
				Rectangle workingSpace = ws.getBounds();
				Rectangle bounds = new Rectangle(workingSpace.x,
						workingSpace.y, workingSpace.width / 2,
						3 * workingSpace.height / 4);
				editorMode.setBounds(bounds);
				return editorMode;
			} else {
				System.out
						.println("Could not create Editor Mode - " + modeName);
				return null;
			}
		} else {
			System.out.println("Error(TwEditor.createMode): workspace == null");
			return null;
		}
	}

	/**
	 * Show the File in the Editor Mode in the specified Workspace
	 */
	public void showFile(String filename) {
		Workspace ws = TopManager.getDefault().getWindowManager()
				.getCurrentWorkspace();
		if (ws != null) {
			editorMode = null; // For testing purpose remove later
			if (editorMode == null) {
				// Try creating the mode
				editorMode = createMode(ws);
				if (editorMode != null) {
					TwEditorPane editorPane = new TwEditorPane();
					editorPane.setName(filename);
					editorMode.dockInto(editorPane);
					editorPane.open(ws);
					editorPane.loadFile(filename);
				}

			}
		} else {
			System.out
					.println("Error(TwEditor.show): Parent workspace == null");
			;
		}
	}

	public static void main(String ignore[]) throws Exception {
		TwEditor.getDefault().showFile(
				"C:\\Test\\ws1\\main\\TwWindowManager.java");
		TwEditor.getDefault().showFile("C:\\forte4j\\bin\\runide.exe");
	}
}
