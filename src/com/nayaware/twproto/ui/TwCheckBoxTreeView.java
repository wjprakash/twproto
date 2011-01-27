/**
 * TwCheckBoxTreeView
 * Author Winston Prakash
 */

package com.nayaware.twproto.ui;

import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;

import org.openide.explorer.*;
import org.openide.nodes.Node;

import org.openide.TopManager;
import org.openide.loaders.*;
import org.openide.windows.*;
import org.openide.util.Utilities;
import org.openide.util.NbBundle;
import org.openide.explorer.view.*;
import org.openide.explorer.propertysheet.*;
import org.openide.explorer.*;

import java.awt.*;
import java.util.*;
import javax.swing.*;

import com.nayaware.twproto.nodes.*;
import com.nayaware.twproto.util.*;

public class TwCheckBoxTreeView extends TreeView {
	Vector checkedNodes = new Vector();
	TwCheckBoxTreeRenderer chkCellRenderer;
	boolean checked = false;
	boolean showCheckedOnly = false;
	boolean showSelected;

	/**
	 * Constructor.
	 * 
	 */
	public TwCheckBoxTreeView() {
		tree.getSelectionModel().setSelectionMode(
				TreeSelectionModel.SINGLE_TREE_SELECTION);
		chkCellRenderer = new TwCheckBoxTreeRenderer();
		tree.setCellRenderer(chkCellRenderer);
		tree.addMouseListener(new TwCheckBoxTreeMouseListener(tree));
	}

	/*
	 * @return true if this TreeView accept the selected beans.
	 */
	protected boolean selectionAccept(Node[] nodes) {
		if (nodes.length == 0)
			return true;

		Node parent = nodes[0].getParentNode();
		for (int i = 1; i < nodes.length; i++) {
			if (nodes[i].getParentNode() != parent) {
				return false;
			}
		}
		return true;
	}

	/*
	 * Called whenever the value of the selection changes.
	 * 
	 * @param listSelectionEvent the event that characterizes the change.
	 */
	protected void selectionChanged(Node[] nodes, ExplorerManager man)
			throws PropertyVetoException {
		if (nodes.length > 0) {
			man.setExploredContext(nodes[0]);
		}
		man.setSelectedNodes(nodes);
	}

	/**
	 * Expand the given path and makes it visible.
	 * 
	 * @param path
	 *            the path
	 */
	protected void showPath(TreePath path) {
		tree.makeVisible(path);
		Rectangle rect = tree.getPathBounds(path);
		if (rect != null) {
			rect.width += rect.x;
			rect.x = 0;
			tree.scrollRectToVisible(rect);
		}
		tree.setSelectionPath(path);
	}

	/**
	 * Shows selection to reflect the current state of the selection in the
	 * explorer.
	 * 
	 * @param paths
	 *            array of paths that should be selected
	 */
	protected void showSelection(TreePath[] paths) {
		if (paths.length == 0) {
			tree.setSelectionPaths(new TreePath[0]);
		} else {
			tree.setSelectionPath(paths[0].getParentPath());
		}
	}

	/**
	 * Permit use of explored contexts.
	 * 
	 * @return <code>true</code> always
	 */
	protected boolean useExploredContextMenu() {
		return true;
	}

	/**
	 * Create model.
	 */
	protected NodeTreeModel createModel() {
		return new NodeTreeModel();
	}

	public Vector getSelection() {
		Vector selection = new Vector();
		for (int i = 0; i < checkedNodes.size(); i++) {
			TreePath path = (TreePath) checkedNodes.get(i);
			TwNode node = (TwNode) Visualizer.findNode(path
					.getLastPathComponent());
			selection.add(node.getPath());
		}
		return selection;
	}

	public void setChecked(TreePath path) {
		if (checkedNodes.contains(path)) {
			checkedNodes.remove(path);
			setParentsUnchecked(path);
			setDescendantsUnchecked(path);
		} else {
			checkedNodes.add(path);
			setParentsChecked(path);
			setDescendantsChecked(path);
		}
		setParentsChecked(path);
		repaintPath(path);
	}

	private void setDescendantsChecked(TreePath path) {
		if (!tree.hasBeenExpanded(path)) {
			return;
		}
		Object cmp = path.getLastPathComponent();
		int cnt = tree.getModel().getChildCount(cmp);
		for (int i = 0; i < cnt; i++) {
			Object ch = tree.getModel().getChild(cmp, i);
			TreePath p = path.pathByAddingChild(ch);
			if (!checkedNodes.contains(p)) {
				checkedNodes.add(p);
				repaintPath(p);
			}
			setDescendantsChecked(p);
		}
	}

	private void setDescendantsUnchecked(TreePath path) {
		if (!tree.hasBeenExpanded(path)) {
			return;
		}
		Object cmp = path.getLastPathComponent();

		int cnt = tree.getModel().getChildCount(cmp);
		for (int i = 0; i < cnt; i++) {
			Object ch = tree.getModel().getChild(cmp, i);
			TreePath p = path.pathByAddingChild(ch);
			if (checkedNodes.contains(p)) {
				checkedNodes.remove(p);
				repaintPath(p);
			}
			setDescendantsUnchecked(p);
		}
	}

	private boolean isAnyChildChecked(TreePath path) {
		if (path == null)
			return false;
		if (checkedNodes == null)
			return false;
		for (int i = 0; i < checkedNodes.size(); i++) {
			TreePath p = (TreePath) checkedNodes.elementAt(i);
			if (path.isDescendant(p)) {
				return true;
			}
		}
		return false;
	}

	private boolean isParentChecked(TreePath path) {
		if (path == null)
			return false;
		if (checkedNodes == null)
			return false;
		TreePath p = path.getParentPath();
		if (checkedNodes.contains(p)) {
			return true;
		}
		return false;
	}

	private void setParentsChecked(TreePath path) {
		boolean toadd = true;
		TreePath p = path.getParentPath();
		if (p == null)
			return;
		Object cmp = p.getLastPathComponent();
		int cnt = tree.getModel().getChildCount(cmp);
		for (int i = 0; i < cnt; i++) {
			Object ch = tree.getModel().getChild(cmp, i);
			TreePath pp = p.pathByAddingChild(ch);
			if (!checkedNodes.contains(pp)) {
				toadd = false;
			}
		}
		if (toadd) {
			checkedNodes.add(p);
		}
		repaintPath(p);
		setParentsChecked(p);
	}

	private void setParentsUnchecked(TreePath path) {
		TreePath p = path.getParentPath();
		if (p == null)
			return;
		if (checkedNodes.contains(p)) {
			checkedNodes.remove(p);
		}
		repaintPath(p);
		setParentsUnchecked(p);
	}

	public boolean isChecked(TreePath path) {
		return (checkedNodes.contains(path));
	}

	protected Object convertPath(TreePath path) {
		return (path);
	}

	private void repaintPath(TreePath path) {
		Rectangle rr = tree.getPathBounds(path);
		if (rr == null)
			return;
		repaint(rr.x, rr.y, rr.width, rr.height);
	}

	class TwCheckBoxTreeRenderer extends JPanel implements TreeCellRenderer {

		public JCheckBox cb;
		GridBagConstraints cc1 = new GridBagConstraints();
		GridBagLayout gb1 = new GridBagLayout();

		public TwCheckBoxTreeRenderer() {
			super();
			this.setBackground(Color.white);
			cb = new JCheckBox();
			// cb.setBackground(Color.gray);
			cb.setMargin(new Insets(0, 0, 0, 0));
			cb.setBorder(null);
			setLayout(gb1);
			cc1.fill = GridBagConstraints.HORIZONTAL;
			cc1.weightx = 0.0;
			cc1.weighty = 0.0;
			cc1.gridx = 0;
			cc1.gridy = 0;
			gb1.setConstraints(cb, cc1);
			add(cb);
		}

		public Component getTreeCellRendererComponent(JTree tree, Object value,
				boolean selected, boolean expanded, boolean leaf, int row,
				boolean hasFocus) {
			boolean checked = false, anyChecked = false, parentChecked = false;
			if (!(checked = isChecked(tree.getPathForRow(row)))) {
				anyChecked = isAnyChildChecked(tree.getPathForRow(row));
				// parentChecked = isParentChecked(tree.getPathForRow(row));
			}
			if (checked || anyChecked) { // || parentChecked ){
				cb.setBackground(Color.lightGray);
				cb.setSelected(true);
			} else {
				cb.setBackground(Color.white);
				cb.setSelected(false);
			}
			Component comp = NodeRenderer.sharedInstance()
					.getTreeCellRendererComponent(tree, value, selected,
							expanded, leaf, row, hasFocus);
			cc1.weightx = 0.1;
			cc1.weighty = 0.0;
			cc1.gridx = 1;
			gb1.setConstraints(comp, cc1);
			add(comp);
			return (this);
		}

	}

	class TwCheckBoxTreeMouseListener extends MouseAdapter {

		JTree tree;

		public TwCheckBoxTreeMouseListener(JTree tree) {
			this.tree = tree;
		}

		public void mousePressed(MouseEvent e) {
			int selRow = tree.getRowForLocation(e.getX(), e.getY());
			if (selRow != -1) {
				if (!tree.isRowSelected(selRow)) {
					tree.setSelectionRow(selRow);
				}
				// Node[] na = manager.getSelectedNodes();
				// TwNode node = na[0];
				// System.out.println(node.getPath());
				Rectangle rect = tree.getRowBounds(selRow);
				rect.width = chkCellRenderer.cb.getBounds().width;
				if (rect.contains(e.getX(), e.getY())) {
					setChecked(tree.getPathForRow(selRow));
				}
			}
		}
	}

	public static void main(String ignore[]) throws Exception {
		TopManager tm = TopManager.getDefault();
		WindowManager wm = tm.getWindowManager();
		Workspace ws = wm.getCurrentWorkspace();
		Mode navigator = ws.findMode("TwNavigator");

		if (navigator == null) {
			navigator = ws
					.createMode("TwNavigator", "Teamware Navigator", null);
			Rectangle wsSpace = ws.getBounds();
			Rectangle bounds = new Rectangle(wsSpace.x, wsSpace.y,
					wsSpace.width / 2, wsSpace.height / 2);
			navigator.setBounds(bounds);
		}

		ExplorerPanel expPanel = new ExplorerPanel();

		TreeView view = new TwCheckBoxTreeView();
		expPanel.setLayout(new BorderLayout());
		expPanel.add(view, BorderLayout.CENTER);
		expPanel.getExplorerManager().setRootContext(
				new TwDirectoryNode("C:\\orion"));
		expPanel.open(ws);
	}
}
