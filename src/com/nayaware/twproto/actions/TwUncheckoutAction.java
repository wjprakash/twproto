/*
 * TwUncheckoutAction.java
 * Author - Winston Prakash
 */

package com.nayaware.twproto.actions;

import java.util.*;

import javax.swing.*;
import org.openide.nodes.Node;

import com.nayaware.twproto.dataobjects.*;
import com.nayaware.twproto.events.*;
import com.nayaware.twproto.nodes.*;
import com.nayaware.twproto.ui.*;
import com.nayaware.twproto.util.*;

public class TwUncheckoutAction extends TwAction {

	TwFileObject fileObject = null;
	TwCheckoutPanel checkoutPanel = new TwCheckoutPanel();
	Vector fileList1 = new Vector();
	Vector fileList2 = new Vector();
	Vector checkoutNodes = new Vector();

	/** Creates new TwUnCheckoutAction */
	public TwUncheckoutAction() {
		super("Uncheckout");
		setType(TwResources.TW_UNCHECKOUT_ACTION);
		twActionDialog = new TwActionDialog(new javax.swing.JFrame(), true);
		twActionDialog.setUserPanel(new TwCheckoutPanel());
	}

	public void performAction(Node[] nodes) {
		fileList1.removeAllElements();
		fileList2.removeAllElements();
		checkoutNodes.removeAllElements();
		for (int i = 0; i < nodes.length; i++) {
			if (nodes[i] instanceof TwFileNode) {
				TwFileObject fileObject = ((TwFileNode) nodes[i])
						.getFileObject();
				if (fileObject.isCheckedOut()) {
					fileList1.add(fileObject);
					checkoutNodes.add(nodes[i]);
				} else {
					fileList2.add(fileObject);
				}
			}
		}
		checkoutPanel.setFileList(fileList1, fileList2);
		twActionDialog.setStartButtonName("Unheckout");
		if (fileList1.size() > 1) {
			twActionDialog.setProgressBarValue(0);
			showActionDialog();
		} else {
			fileObject = (TwFileObject) fileList1.get(0);
			try {
				fileObject.unCheckOut();
				TwFileNode node = (TwFileNode) checkoutNodes.get(0);
				node.refresh();
			} catch (TWVCException ex) {
				JOptionPane.showMessageDialog(null, ex.getMessage());
			}
		}
	}

	public void performDialogAction() {
		if (stopAction)
			return;
		twActionDialog.setProgressBarMinValue(0);
		twActionDialog.setProgressBarMaxValue(fileList1.size());
		twActionDialog.setProgressBarValue(0);
		if (fileList1.size() > 1) {
			fileList2 = checkoutPanel.getSelectedFiles();
			for (int i = 0; i < fileList2.size(); i++) {
				if (stopAction)
					return;
				fileObject = (TwFileObject) fileList1.get(i);
				twActionDialog.setStatusMsg("Unchecking out file -"
						+ fileObject.getPath());
				try {
					fileObject.unCheckOut();
					twActionDialog.appendOutput("Unchecked out file - "
							+ fileObject.getPath());
					TwFileNode node = (TwFileNode) checkoutNodes.get(i);
					node.refresh();
				} catch (TWVCException ex) {
					twActionDialog
							.appendOutput("Error occured while Unchecking out file - "
									+ fileObject.getPath());
					twActionDialog.appendOutput(ex.getMessage());
				}
				twActionDialog.setProgressBarValue(i);
			}
		}
	}

	protected boolean enable(Node[] nodes) {
		boolean enableNode = false;
		for (int i = 0; i < nodes.length; i++) {
			if (nodes[i] instanceof TwFileNode) {
				TwFileObject fileObject = ((TwFileNode) nodes[i])
						.getFileObject();
				if (fileObject.isCheckedOut())
					enableNode = true;
			}
		}
		return enableNode;
	}
}
