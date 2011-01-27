/*
 *                 Sun Public License Notice
 * 
 * The contents of this file are subject to the Sun Public License
 * Version 1.0 (the "License"). You may not use this file except in
 * compliance with the License. A copy of the License is available at
 * http://www.sun.com/
 * 
 * The Original Code is NetBeans. The Initial Developer of the Original
 * Code is Sun Microsystems, Inc. Portions Copyright 1997-2000 Sun
 * Microsystems, Inc. All Rights Reserved.
 */

package com.nayaware.twproto.ui;

import javax.swing.event.*;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.table.TableColumn;
import javax.swing.tree.TreePath;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import java.awt.*;

import org.openide.explorer.view.BeanTreeView;
import org.openide.explorer.view.Visualizer;
import org.openide.explorer.ExplorerManager;
import org.openide.util.WeakListener;
import org.openide.nodes.*;

/**
 * Explorer view. Allows to view tree of nodes on the left and its properties in
 * table on the right.
 * 
 * @author jrojcek
 * @since 1.7
 */
public class TreeTableView extends BeanTreeView {

	/** flag that allows group more requests into one */
	private boolean tableChanging = false;
	/** manager is used only for synchronization of header name */
	private ExplorerManager manager;
	/** listener on explorer manager */
	private PropertyChangeListener wlpc;
	/** table view that is controlled by tree view */
	private TableSheet.ControlledTableView controlledTableView;
	/** listener on changes in tree and its model */
	private Listener listener;
	/** preferred size */
	private Dimension prefSize;

	/**
	 * Create TreeTableView with default NodeTableModel
	 */
	public TreeTableView() {
		this(null);
	}

	/**
	 * Creates TreeTableView with provided NodeTableModel.
	 * 
	 * @param ntm
	 *            node table model
	 */
	public TreeTableView(NodeTableModel ntm) {
		tree.putClientProperty("JTree.lineStyle", "None"); // NOI18N
		tree.setUI(new TableLikeTreeUI());
		// do not use scroll bars, this scroll pane is dummy, only for border
		// painting
		setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		// remove tree from viewportview
		setViewportView(null);

		// insert tree into new scrollpane
		JScrollPane treeView = new JScrollPane(tree);
		treeView.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		treeView.setBorder(null);

		// create table view controlled by new scrollpane
		controlledTableView = ntm == null ? new TableSheet.ControlledTableView(
				treeView) : new TableSheet.ControlledTableView(treeView, ntm);
		// hack to avoid moving of compound view
		setLayout(new TableSheet.MyScrollPaneLayout());
		// add compound scrollpane (JPanel) into this scrollpane
		add(TableSheet.MyScrollPaneLayout.COMPOUND_SCROLLPANE,
				controlledTableView.compoundScrollPane());

		listener = new Listener();
		changeTableModel();

		setPreferredSize(new Dimension(400, 400));
	}

	/**
	 * Set columns.
	 * 
	 * @param props
	 *            each column is constructed from Node.Property
	 */
	public void setProperties(Node.Property[] props) {
		controlledTableView.setProperties(props);
	}

	/**
	 * Sets resize mode of table.
	 * 
	 * @param mode
	 *            - One of 5 legal values:
	 * 
	 *            <pre>
	 * JTable.AUTO_RESIZE_OFF,
	 *                                           JTable.AUTO_RESIZE_NEXT_COLUMN,
	 *                                           JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS, 
	 *                                           JTable.AUTO_RESIZE_LAST_COLUMN, 
	 *                                           JTable.AUTO_RESIZE_ALL_COLUMNS
	 * </pre>
	 */
	public final void setTableAutoResizeMode(int mode) {
		controlledTableView.setAutoResizeMode(mode);
	}

	/**
	 * Gets resize mode of table.
	 * 
	 * @return mode - One of 5 legal values:
	 * 
	 *         <pre>
	 * JTable.AUTO_RESIZE_OFF,
	 *                                           JTable.AUTO_RESIZE_NEXT_COLUMN,
	 *                                           JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS, 
	 *                                           JTable.AUTO_RESIZE_LAST_COLUMN, 
	 *                                           JTable.AUTO_RESIZE_ALL_COLUMNS
	 * </pre>
	 */
	public final int getTableAutoResizeMode() {
		return controlledTableView.getAutoResizeMode();
	}

	/**
	 * Sets preferred width of table column
	 * 
	 * @param index
	 *            column index
	 * @param width
	 *            preferred column width
	 */
	public final void setTableColumnPreferredWidth(int index, int width) {
		controlledTableView.setColumnPreferredWidth(index, width);
	}

	/**
	 * Gets preferred width of table column
	 * 
	 * @param index
	 *            column index
	 * @return preferred column width
	 */
	public final int getTableColumnPreferredWidth(int index) {
		return controlledTableView.getColumnPreferredWidth(index);
	}

	/**
	 * Set preferred size of tree view
	 * 
	 * @param width
	 *            preferred width of tree view
	 */
	public final void setTreePreferredWidth(int width) {
		controlledTableView.setControllingViewWidth(width);
		Dimension dim = getPreferredSize();
		controlledTableView.setPreferredSize(new Dimension(dim.width - width,
				dim.height));
	}

	/**
	 * Get preferred size of tree view
	 * 
	 * @return preferred width of tree view
	 */
	public final int getTreePreferredWidth() {
		return controlledTableView.getControllingViewWidth();
	}

	public void setPreferredSize(Dimension dim) {
		super.setPreferredSize(dim);
		prefSize = dim;
		controlledTableView.setPreferredSize(new Dimension(dim.width
				- controlledTableView.getControllingViewWidth(), dim.height));
	}

	public Dimension getPreferredSize() {
		return prefSize;
	}

	public void addNotify() {
		super.addNotify();

		ExplorerManager newManager = ExplorerManager.find(this);
		if (newManager != manager) {
			if (manager != null) {
				manager.removePropertyChangeListener(wlpc);
			}
			manager = newManager;
			manager.addPropertyChangeListener(wlpc = WeakListener
					.propertyChange(listener, manager));
			controlledTableView.setHeaderText(manager.getRootContext()
					.getDisplayName());
		}
		tree.addTreeExpansionListener(listener);
		tree.getModel().addTreeModelListener(listener);
		changeTableModel();
	}

	public void removeNotify() {
		super.removeNotify();
		tree.removeTreeExpansionListener(listener);
		tree.getModel().removeTreeModelListener(listener);
		// clear node listeners
		controlledTableView.setNodes(new Node[] {});
	}

	/** Chnage table data in awt thread */
	private void delayedFireTableDataChanged() {
		if (tableChanging)
			return;
		tableChanging = true;
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				if (tree.getRowBounds(0) != null)
					controlledTableView.setRowHeight(tree.getRowBounds(0).height);
				changeTableModel();
				tableChanging = false;
			}
		});
	}

	/** Change table model. Sets rows (nodes) of table model */
	private void changeTableModel() {
		Node[] nodes = new Node[tree.getRowCount()];
		for (int i = 0; i < tree.getRowCount(); i++) {
			nodes[i] = Visualizer.findNode(tree.getPathForRow(i)
					.getLastPathComponent());
		}
		controlledTableView.setNodes(nodes);
	}

	/**
	 * Listenes on changes in tree and its model.
	 */
	private class Listener implements TreeExpansionListener, TreeModelListener,
			PropertyChangeListener {

		public void treeExpanded(TreeExpansionEvent e) {
			changeTableModel();
		}

		public void treeCollapsed(TreeExpansionEvent e) {
			changeTableModel();
		}

		public void treeNodesChanged(TreeModelEvent e) {
			delayedFireTableDataChanged();
		}

		public void treeNodesInserted(TreeModelEvent e) {
			delayedFireTableDataChanged();
		}

		public void treeNodesRemoved(TreeModelEvent e) {
			delayedFireTableDataChanged();
		}

		public void treeStructureChanged(TreeModelEvent e) {
			delayedFireTableDataChanged();
		}

		/**
		 * Updates header name.
		 * 
		 * @param evt
		 *            event
		 */
		public void propertyChange(PropertyChangeEvent evt) {
			if (ExplorerManager.PROP_ROOT_CONTEXT.equals(evt.getPropertyName()))
				controlledTableView.setHeaderText(manager.getExploredContext()
						.getDisplayName());
		}
	}

	/**
	 * Overriden to paint row separator lines
	 */
	private class TableLikeTreeUI extends javax.swing.plaf.metal.MetalTreeUI {

		protected void paintRow(Graphics g, Rectangle clipBounds,
				Insets insets, Rectangle bounds, TreePath path, int row,
				boolean isExpanded, boolean hasBeenExpanded, boolean isLeaf) {
			super.paintRow(g, clipBounds, insets, bounds, path, row,
					isExpanded, hasBeenExpanded, isLeaf);
			Graphics cg = g.create(0, bounds.y, super.tree.getSize().width,
					bounds.height);
			try {
				cg.setColor(SystemColor.controlDkShadow);
				cg.drawLine(0, bounds.height - 1,
						super.tree.getSize().width - 1, bounds.height - 1);
			} finally {
				cg.dispose();
			}
		}
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				Node n = org.openide.TopManager.getDefault().getPlaces()
						.nodes().repository();
				ExplorerManager em = new ExplorerManager();
				em.setRootContext(n);
				TreeTableView ttv = new TreeTableView();
				// ttv.model.setDepth(2);
				// ttv.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
				ttv.setProperties(n.getChildren().getNodes()[0]
						.getPropertySets()[0].getProperties());
				org.openide.explorer.ExplorerPanel ep = new org.openide.explorer.ExplorerPanel(
						em);
				ep.setLayout(new java.awt.BorderLayout());
				ep.add("Center", ttv);
				ep.open();
			}
		});
	}
}
