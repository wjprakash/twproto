/*
 * TwEditorPane.java
 * Author  Winston Prakash
 */
package com.nayaware.twproto.ui;

import org.openide.util.NbBundle;
import org.openide.windows.*;

import java.awt.Toolkit;
import java.io.*;
import java.net.*;
import javax.swing.text.*;

import org.openide.TopManager;
import org.openide.actions.*;
import org.openide.awt.UndoRedo;
import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.actions.*;
import org.openide.util.datatransfer.PasteType;
import org.openide.util.io.*;

public class TwEditorPane extends TopComponent /* or CloneableTopComponent */{

	public TwEditorPane() {
		initComponents();
		setCloseOperation(CLOSE_LAST);
	}

	protected boolean closeLast() {
		// Prompt the user for saving
		return true;
	}

	public void loadFile(String filename) {
		File file = new File(filename);
		if (file.exists() && file.isFile()) {
			try {
				BufferedReader bufReader = new BufferedReader(new FileReader(
						file));
				Document doc = textPane.getDocument();
				String str = null, str1 = null;
				int count = 0;
				while ((str1 = bufReader.readLine()) != null) {
					// System.out.println(str+"\n");
					str = str + str1 + "\n";
					if (count++ < 500)
						continue;
					doc.insertString(doc.getLength(), str + "\n", null);
					str = null;
					count = 0;
				}
				if (str != null)
					doc.insertString(doc.getLength(), str + "\n", null);
			} catch (Exception ex) {
				System.out.println(ex.getMessage());
				ex.printStackTrace();
			}
		}
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the FormEditor.
	 */
	private void initComponents() {// GEN-BEGIN:initComponents
		scrollPane = new javax.swing.JScrollPane();
		textPane = new javax.swing.JTextPane();

		setLayout(new java.awt.BorderLayout());

		scrollPane.setPreferredSize(new java.awt.Dimension(200, 300));
		textPane.setPreferredSize(new java.awt.Dimension(6, 6));
		scrollPane.setViewportView(textPane);

		add(scrollPane, java.awt.BorderLayout.CENTER);

	}// GEN-END:initComponents

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JScrollPane scrollPane;
	private javax.swing.JTextPane textPane;

	// End of variables declaration//GEN-END:variables

	// popup menu
	public SystemAction[] getSystemActions() {
		SystemAction[] supe = super.getSystemActions();
		// SystemAction[] mine = new SystemAction[supe.length + 1];
		// System.arraycopy (supe, 0, mine, 0, supe.length);
		// mine[supe.length] = SystemAction.get (SomeActionOfMine.class);
		// return mine;
		return super.getSystemActions();
	}

}