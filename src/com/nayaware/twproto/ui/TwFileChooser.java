/*
 * TwObjectChooser.java
 * Author - Winston Prakash
 */

package com.nayaware.twproto.ui;

import com.nayaware.twproto.nodes.*;
import com.nayaware.twproto.util.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileView;
import java.beans.BeanInfo;

/**
 * TwObject Chooser is a common chooser for files, directories & workspaces The
 * chooser type is set by the method setChooserType() When set as workspace
 * chooser the workspace directory will not be explored
 */
public class TwFileChooser extends JDialog implements ActionListener {

	public static final int FILE_CHOOSER = 0;
	public static final int DIRECTORY_CHOOSER = 1;
	public static final int WORKSPACE_CHOOSER = 2;

	protected JPanel localWorkspaceChooserPanel;
	protected JPanel remoteWorkspaceChooserPanel;
	protected JFileChooser fileChooser;

	protected int chooserType = FILE_CHOOSER;

	protected String initialDir = null;

	protected File selectedFile = null;

	protected File[] roots = null;

	/**
	 * Creates new form TwObjectChooser
	 */
	public TwFileChooser() {
		super();
		setModal(true);
		initialize();
	}

	/**
	 * Creates new form TwObjectChooser with JFrame as parent & either
	 * modal/modaless
	 */
	public TwFileChooser(JFrame parent, boolean modal) {
		super(parent, modal);
		initialize();
	}

	protected void initialize() {
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent evt) {
				closeDialog();
			}
		});
		setInitialDirectory(System.getProperty("user.home"));
		roots = File.listRoots();
	}

	/**
	 * Set the type of chooser (File, Directory or Workspace)
	 */
	public void setType(int type) {
		chooserType = type;
	}

	public void setInitialDirectory(String home) {
		initialDir = home;
	}

	/**
	 * Create local Object Chooser Panel
	 */
	protected void createLocalObjectChooser() {
		localWorkspaceChooserPanel = new JPanel();
		localWorkspaceChooserPanel.setLayout(new BorderLayout());
		fileChooser = new JFileChooser();
		fileChooser.setMultiSelectionEnabled(false);
		fileChooser.addActionListener(this);
		fileChooser.setApproveButtonText("Select");
		// fileChooser.setApproveButtonMnemonic('S');
		localWorkspaceChooserPanel.add(fileChooser, BorderLayout.CENTER);
	}

	/**
	 * Create Remote Object Chooser Panel
	 */
	protected void createRemoteObjectChooser() {
		remoteWorkspaceChooserPanel = new JPanel();
		remoteWorkspaceChooserPanel.setLayout(new BorderLayout());
		remoteWorkspaceChooserPanel.add(new JLabel("Not Yet Implemented"),
				BorderLayout.CENTER);
	}

	/**
	 * Close the object chooser Dialog
	 */
	protected void closeDialog() {
		setVisible(false);
		dispose();
	}

	/**
	 * Get ths selected file. Returs the selection as java class "File" Returns
	 * null if there is no selection
	 */
	public File getSelection() {
		return selectedFile;
	}

	/**
	 * Shows the chooser dialog. The corresponding filters are set based on the
	 * chooser type set. The default is the file chooser (All files shown)
	 */
	public void show() {
		switch (chooserType) {
		case FILE_CHOOSER:
			createLocalObjectChooser();
			fileChooser.setFileView(new TwFileSelectionView());
			fileChooser.addChoosableFileFilter(new TwDirectoryFilter());
			fileChooser.addChoosableFileFilter(new TwFileFilter());
			fileChooser.setFileSelectionMode(fileChooser.FILES_ONLY);
			this.getContentPane().add(localWorkspaceChooserPanel);
			break;
		case DIRECTORY_CHOOSER:
			createLocalObjectChooser();
			fileChooser.setFileView(new TwDirectorySelectionView());
			fileChooser.addChoosableFileFilter(new TwDirectoryFilter());
			fileChooser.addChoosableFileFilter(new TwFileFilter());
			fileChooser.setFileSelectionMode(fileChooser.DIRECTORIES_ONLY);
			this.getContentPane().add(localWorkspaceChooserPanel);
			break;
		case WORKSPACE_CHOOSER:
			JTabbedPane tabbedPane = new JTabbedPane();
			createLocalObjectChooser();
			fileChooser.addChoosableFileFilter(new TwWorkspaceFilter());
			fileChooser.addChoosableFileFilter(new TwDirectoryFilter());
			fileChooser.setFileView(new TwWorkspaceSelectionView());
			fileChooser.setFileSelectionMode(fileChooser.FILES_AND_DIRECTORIES);
			tabbedPane.addTab("Local Workspace", localWorkspaceChooserPanel);
			createRemoteObjectChooser();
			tabbedPane.addTab("Repository Workspace",
					remoteWorkspaceChooserPanel);
			this.getContentPane().add(tabbedPane);
			break;
		default:
		}
		pack();
		Component parent = getParent();
		Dimension dlgDim = getSize();
		Dimension frameDim = new Dimension(0, 0);
		if (parent != null)
			frameDim = parent.getSize();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Point loc = new Point(screenSize.width / 2, screenSize.height / 2);
		loc.translate((frameDim.width - dlgDim.width) / 2,
				(frameDim.height - dlgDim.height) / 2);
		loc.x = Math.max(0, Math.min(loc.x, screenSize.width - dlgDim.width));
		loc.y = Math.max(0, Math.min(loc.y, screenSize.height - dlgDim.height));
		setLocation(loc.x, loc.y);
		super.show();
	}

	/**
	 * Implements the ActionListener method actionPerformed for the buttons
	 */

	public void actionPerformed(ActionEvent evt) {
		File selection = null;
		if (evt.getActionCommand() == "ApproveSelection") {
			selection = fileChooser.getSelectedFile();
			switch (chooserType) {
			case DIRECTORY_CHOOSER:
				if (!selection.isDirectory()) {
					JOptionPane.showMessageDialog(null,
							"Selection is not a directory");
					return;
				}
				break;
			case WORKSPACE_CHOOSER:
				if (selection.isDirectory()) {
					File checkDir = new File(selection, "Codemgr_wsdata");
					if (!checkDir.exists()) {
						JOptionPane.showMessageDialog(null,
								"Selection is not a teamware workspace");
						return;
					}
				} else {
					JOptionPane.showMessageDialog(null,
							"Selection is not a teamware workspace");
					return;
				}
				break;
			}
		}
		selectedFile = selection;
		closeDialog();
	}

	private class TwFileFilter extends FileFilter {
		// Accept all directories
		public boolean accept(File f) {
			return true;
		}

		// The description of this filter
		public String getDescription() {
			return "All Files";
		}
	}

	private class TwDirectoryFilter extends FileFilter {
		// Accept all directories
		public boolean accept(File f) {
			if (f.isDirectory()) {
				return true;
			} else {
				return false;
			}
		}

		// The description of this filter
		public String getDescription() {
			return "All Directories";
		}
	}

	private class TwWorkspaceFilter extends FileFilter {
		// Accept all directories
		public boolean accept(File f) {
			// Check here if the enquiry if for one of the roots.
			// Then skip it as this causes "A: drive open error" in Win 2000
			for (int i = 0; i < roots.length; i++) {
				if (roots[i].equals(f))
					return true;
			}
			if (f.isDirectory()) {
				File checkDir = new File(f, "Codemgr_wsdata");
				if (checkDir.exists()) {
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}
		}

		// The description of this filter
		public String getDescription() {
			return "All Workspaces";
		}
	}

	private class TwFileSelectionView extends FileView {
		public Icon getIcon(File f) {
			// If the file is system root then let the look & feel give the icon
			for (int i = 0; i < roots.length; i++) {
				if (roots[i].equals(f))
					return null;
			}
			if (f.isDirectory())
				return TwResources.getImageIcon(TwResources.TW_DIRECTORY_NODE,
						BeanInfo.ICON_COLOR_16x16);
			else
				return TwResources.getImageIcon(TwResources.TW_FILE_NODE,
						BeanInfo.ICON_COLOR_16x16);
		}
	}

	private class TwDirectorySelectionView extends FileView {
		public Icon getIcon(File f) {
			// If the file is system root then let the look & feel give the icon
			for (int i = 0; i < roots.length; i++) {
				if (roots[i].equals(f))
					return null;
			}
			if (f.isDirectory())
				return TwResources.getImageIcon(TwResources.TW_DIRECTORY_NODE,
						BeanInfo.ICON_COLOR_16x16);
			else
				return TwResources.getImageIcon(TwResources.TW_FILE_NODE,
						BeanInfo.ICON_COLOR_16x16);
		}
	}

	private class TwWorkspaceSelectionView extends FileView {
		public Boolean isTraversable(File f) {
			if (isWorkspaceDir(f))
				return Boolean.FALSE;
			else
				return Boolean.TRUE;
		}

		public Icon getIcon(File f) {
			// If the file is system root then let the look & feel give the icon
			for (int i = 0; i < roots.length; i++) {
				if (roots[i].equals(f))
					return null;
			}
			if (f.isDirectory()) {
				if (isWorkspaceDir(f)) {
					return TwResources.getImageIcon(
							TwResources.TW_WORKSPACE_DIR_NODE,
							BeanInfo.ICON_COLOR_16x16);
				} else {
					return TwResources.getImageIcon(
							TwResources.TW_DIRECTORY_NODE,
							BeanInfo.ICON_COLOR_16x16);
				}
			} else {
				return TwResources.getImageIcon(TwResources.TW_FILE_NODE,
						BeanInfo.ICON_COLOR_16x16);
			}
		}

		protected boolean isWorkspaceDir(File f) {
			File checkDir = new File(f, "Codemgr_wsdata");
			if (checkDir.exists())
				return true;
			else
				return false;
		}
	}
}
