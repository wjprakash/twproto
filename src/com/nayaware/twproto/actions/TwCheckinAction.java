/*
 * TwCheckinAction.java
 * Author - Winston Prakash
 */

package com.nayaware.twproto.actions;

import java.util.*;
import org.openide.nodes.Node;

import com.nayaware.twproto.dataobjects.*;
import com.nayaware.twproto.nodes.*;
import com.nayaware.twproto.ui.*;
import com.nayaware.twproto.util.*;

public class TwCheckinAction extends TwAction {

	TwCheckinPanel checkinPanel = new TwCheckinPanel();
	Vector fileList1 = new Vector();
	Vector fileList2 = new Vector();
	Vector checkinNodes = new Vector();

	/** Creates new TwCheckinAction */
	public TwCheckinAction() {
		super("Checkin File");
		setType(TwResources.TW_CHECKIN_ACTION);
		twActionDialog = new TwActionDialog(new javax.swing.JFrame(), true);
		twActionDialog.setUserPanel(new TwCheckinPanel());
	}

	public void performAction(Node[] nodes) {
		fileList1.removeAllElements();
		fileList2.removeAllElements();
		checkinNodes.removeAllElements();
		for (int i = 0; i < nodes.length; i++) {
			if (nodes[i] instanceof TwFileNode) {
				TwFileObject fileObject = ((TwFileNode) nodes[i])
						.getFileObject();
				if (fileObject.isCheckedOut() || (!fileObject.isUnderVC())) {
					fileList1.add(fileObject);
					checkinNodes.add(nodes[i]);
				} else {
					fileList2.add(fileObject);
				}
			}
		}
		checkinPanel.setFileList(fileList1, fileList2);
		twActionDialog.setStartButtonName("Checkin");
		if (fileList1.size() > 0) {
			twActionDialog.setProgressBarValue(0);
			showActionDialog();
		}
	}

	public void performDialogAction() {
		TwFileObject fileObject = null;
		twActionDialog.setProgressBarMinValue(0);
		twActionDialog.setProgressBarMaxValue(fileList1.size());
		if (fileList1.size() > 1) {
			fileList2 = checkinPanel.getSelectedFiles();
			for (int i = 0; i < fileList2.size(); i++) {
				if (stopAction)
					return;
				fileObject = (TwFileObject) fileList1.get(i);
				twActionDialog.setStatusMsg("Checking in file -"
						+ fileObject.getPath());
				try {
					if (!fileObject.isUnderVC()) {
						fileObject.putUnderVC(checkinPanel.getComment());
					} else {
						fileObject.checkIn(checkinPanel.getComment());
					}
					twActionDialog.appendOutput("Checked in file - "
							+ fileObject.getPath());
					TwFileNode node = (TwFileNode) checkinNodes.get(i);
					node.refresh();
				} catch (TWVCException ex) {
					twActionDialog
							.appendOutput("Error occured while checking in file - "
									+ fileObject.getPath());
					twActionDialog.appendOutput(ex.getMessage());
				}
				twActionDialog.setProgressBarValue(i);
				twActionDialog.close();
			}
		} else {
			if (stopAction)
				return;
			fileObject = (TwFileObject) fileList1.get(0);
			twActionDialog.setStatusMsg("Checking in file -"
					+ fileObject.getPath());
			try {
				if (!fileObject.isUnderVC()) {
					fileObject.putUnderVC(checkinPanel.getComment());
				} else {
					fileObject.checkIn(checkinPanel.getComment());
				}
				twActionDialog.appendOutput("Checked in file - "
						+ fileObject.getPath());
				TwFileNode node = (TwFileNode) checkinNodes.get(0);
				node.refresh();
				twActionDialog.setProgressBarValue(1);
				twActionDialog.close();
			} catch (TWVCException ex) {
				twActionDialog
						.appendOutput("Error occured while checking in file - "
								+ fileObject.getPath());
				twActionDialog.appendOutput(ex.getMessage());
				twActionDialog.viewOutput(true);
			}
		}
	}

	protected boolean enable(Node[] nodes) {
		boolean enableNode = false;
		for (int i = 0; i < nodes.length; i++) {
			if (nodes[i] instanceof TwFileNode) {
				TwFileObject fileObject = ((TwFileNode) nodes[i])
						.getFileObject();
				if (fileObject.isCheckedOut() || (!fileObject.isUnderVC()))
					enableNode = true;
			}
		}
		return enableNode;
	}
}
