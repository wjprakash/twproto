/*
 * TwTestAction.java
 * Author - Winston Prakash
 */

package com.nayaware.twproto.actions;

import com.nayaware.twproto.events.*;
import com.nayaware.twproto.nodes.*;
import com.nayaware.twproto.ui.*;
import com.nayaware.twproto.util.*;

import java.io.*;

public class TwTestAction extends TwAction {

	TwActionDialog actionDialog = new TwActionDialog(null, true);

	/** Creates new TwTestAction */
	public TwTestAction() {
		super("Test Action Dialog ..");
	}

	public void performAction(TwActionEvent e) {
		TwCommand command = new TwCommand();
		String outputLine;
		actionDialog.setProgressBarMinValue(1);
		actionDialog.setProgressBarMaxValue(30);
		actionDialog.clearOutput();
		for (int i = 0; i < 30; i++) {
			try {
				File tmpFile = File.createTempFile("test",
						"." + String.valueOf(i));
				FileWriter fileWriter = new FileWriter(tmpFile);
				fileWriter.write("This is a test file \n");
				fileWriter.close();
				String cmd = "sccs create " + tmpFile.getAbsolutePath();
				command.execute(cmd);
				while ((outputLine = command.getOutputLine()) != null) {
					actionDialog.setProgressBarValue(i);
					actionDialog.setStatusMsg("Checking in new -"
							+ tmpFile.getAbsolutePath());
					actionDialog.appendOutput(outputLine);
					if (stopAction) {
						command.interrupt();
						return;
					}
				}
				while ((outputLine = command.getErrorLine()) != null) {
					actionDialog.appendOutput(outputLine);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
}
