/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 1997-2010 Oracle and/or its affiliates. All rights reserved.
 *
 * Oracle and Java are registered trademarks of Oracle and/or its affiliates.
 * Other names may be trademarks of their respective owners.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common
 * Development and Distribution License("CDDL") (collectively, the
 * "License"). You may not use this file except in compliance with the
 * License. You can obtain a copy of the License at
 * http://www.netbeans.org/cddl-gplv2.html
 * or nbbuild/licenses/CDDL-GPL-2-CP. See the License for the
 * specific language governing permissions and limitations under the
 * License.  When distributing the software, include this License Header
 * Notice in each file and include the License file at
 * nbbuild/licenses/CDDL-GPL-2-CP.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the GPL Version 2 section of the License file that
 * accompanied this code. If applicable, add the following below the
 * License Header, with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 *
 * Contributor(s):
 *
 * The Original Software is NetBeans. The Initial Developer of the Original
 * Software is Sun Microsystems, Inc. Portions Copyright 1997-2008 Sun
 * Microsystems, Inc. All Rights Reserved.
 *
 * If you wish your version of this file to be governed by only the CDDL
 * or only the GPL Version 2, indicate your decision by adding
 * "[Contributor] elects to include this software in this distribution
 * under the [CDDL or GPL Version 2] license." If you do not indicate a
 * single choice of license, a recipient has the option to distribute
 * your version of this file under either the CDDL, the GPL Version 2 or
 * to extend the choice of license to its licensees as provided above.
 * However, if you add GPL Version 2 code and therefore, elected the GPL
 * Version 2 license, then the option applies only if the new code is
 * made subject to such option by the copyright holder.
 */

package com.nayaware.twproto.ui;

import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListDataEvent;
import javax.swing.table.TableColumn;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import java.awt.Dimension;

import org.openide.explorer.view.ListView;
import org.openide.explorer.view.NodeListModel;
import org.openide.explorer.view.Visualizer;
import org.openide.explorer.ExplorerManager;
import org.openide.util.WeakListener;
import org.openide.nodes.*;

import com.nayaware.twproto.nodes.*;
import com.nayaware.twproto.util.*;

/**
 * Explorer view. Allows to view list of nodes on the left and its properties in
 * table on the right.
 * 
 * @author jrojcek
 * @since 1.7
 */
public class ListTableView extends TwListView {

	/** flag that allows group more requests into one */
	private boolean tableChanging = false;
	/** manager is used only for synchronization of header name */
	private ExplorerManager manager;
	/** listener on explorer manager */
	private PropertyChangeListener wlpc;
	/** table view that is controlled by list view */
	private TableSheet.ControlledTableView controlledTableView;
	/** listener on changes in list model */
	private Listener listener;
	/** preferred size */
	private Dimension prefSize;

	/**
	 * Create ListTableView with default NodeTableModel
	 */
	public ListTableView() {
		this(null);
	}

	/**
	 * Creates ListTableView with provided NodeTableModel.
	 * 
	 * @param ntm
	 *            node table model
	 */
	public ListTableView(NodeTableModel ntm) {
		// do not use scroll bars, this scroll pane is dummy, only for border
		// painting
		setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		// remove list from viewportview
		setViewportView(null);

		// insert list into new scrollpane
		JScrollPane listView = new JScrollPane(list);
		listView.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		listView.setBorder(null);

		// create table view controlled by new scrollpane
		controlledTableView = ntm == null ? new TableSheet.ControlledTableView(
				listView) : new TableSheet.ControlledTableView(listView, ntm);
		// hack to avoid moving of compound view
		setLayout(new TableSheet.MyScrollPaneLayout());
		// add compound scrollpane (JPanel) into this scrollpane
		add(TableSheet.MyScrollPaneLayout.COMPOUND_SCROLLPANE,
				controlledTableView.compoundScrollPane());

		listener = new Listener();
		delayedFireTableDataChanged();

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
	 * Set preferred width of list view
	 * 
	 * @param width
	 *            preferred width
	 */
	public void setListPreferredWidth(int width) {
		controlledTableView.setControllingViewWidth(width);
		Dimension dim = getPreferredSize();
		controlledTableView.setPreferredSize(new Dimension(dim.width - width,
				dim.height));
	}

	/**
	 * Get preferred size of list view
	 * 
	 * @return preferred width of list view
	 */
	public final int getListPreferredWidth() {
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
			controlledTableView.setHeaderText(manager.getExploredContext()
					.getDisplayName());
		}

		list.getModel().addListDataListener(listener);
		delayedFireTableDataChanged();
	}

	public void removeNotify() {
		super.removeNotify();
		list.getModel().removeListDataListener(listener);
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
				if (list.getCellBounds(0, 0) != null)
					controlledTableView.setRowHeight(list.getCellBounds(0, 0).height);
				changeTableModel();
				tableChanging = false;
			}
		});
	}

	/** Change table model. Sets rows (nodes) of table mmodel */
	private void changeTableModel() {
		Node[] nodes = new Node[list.getModel().getSize()];
		for (int i = 0; i < list.getModel().getSize(); i++) {
			nodes[i] = Visualizer.findNode(list.getModel().getElementAt(i));
		}
		controlledTableView.setNodes(nodes);
	}

	/**
	 * Listenes on changes in list model.
	 */
	private class Listener implements ListDataListener, PropertyChangeListener {
		/**
		 * Sent after the indices in the index0,index1 interval have been
		 * inserted in the data model. The new interval includes both index0 and
		 * index1.
		 * 
		 * @param e
		 *            a ListDataEvent encapuslating the event information
		 */
		public void intervalAdded(ListDataEvent e) {
			delayedFireTableDataChanged();
		}

		/**
		 * Sent after the indices in the index0,index1 interval have been
		 * removed from the data model. The interval includes both index0 and
		 * index1.
		 * 
		 * @param e
		 *            a ListDataEvent encapuslating the event information
		 */
		public void intervalRemoved(ListDataEvent e) {
			delayedFireTableDataChanged();
		}

		/**
		 * Sent when the contents of the list has changed in a way that's too
		 * complex to characterize with the previous methods. Index0 and index1
		 * bracket the change.
		 * 
		 * @param e
		 *            a ListDataEvent encapuslating the event information
		 */
		public void contentsChanged(ListDataEvent e) {
			delayedFireTableDataChanged();
		}

		/**
		 * Updates header name.
		 * 
		 * @param evt
		 *            event
		 */
		public void propertyChange(PropertyChangeEvent evt) {
			if (ExplorerManager.PROP_EXPLORED_CONTEXT.equals(evt
					.getPropertyName()))
				controlledTableView.setHeaderText(manager.getExploredContext()
						.getDisplayName());
		}
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				TwNode n = new TwDirectoryNode("C:\\Test\\ws2");
				ExplorerManager em = new ExplorerManager();
				em.setRootContext(n);
				ListTableView ttv = new ListTableView();
				ttv.setProperties(createProperySet());
				// ttv.model.setDepth(2);
				// ttv.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
				// ttv.setProperties(n.getChildren().getNodes()[0].getPropertySets()[0].getProperties());
				org.openide.explorer.ExplorerPanel ep = new org.openide.explorer.ExplorerPanel(
						em);
				ep.setLayout(new java.awt.BorderLayout());
				ep.add("Center", ttv);
				ep.open();
			}
		});
	}

	public static Node.Property[] createProperySet() {
		return new Node.Property[] {
				TwUtils.createProperty("user", String.class, "Last User",
						"Last USer of the File", true, false, null),
				TwUtils.createProperty("version", String.class,
						"Latest Version", "Version of the latest Delta", true,
						false, null),
				TwUtils.createProperty("comment", String.class, "Comment",
						"Comment from the last Delta", true, false, null) };
	}
}
