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

import javax.swing.*;
import javax.swing.table.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import java.util.EventObject;
import java.lang.reflect.*;
import java.util.HashMap;

import org.openide.nodes.*;
import org.openide.explorer.propertysheet.*;
import org.openide.explorer.view.*;
import org.openide.explorer.ExplorerManager;
import org.openide.ErrorManager;
import org.openide.util.Mutex;

/**
 * Table view of node properties. Table header displays property display names
 * and each cell contains (<code>PropertyPanel</code>) for displaying/editting
 * of properties. Each property row belongs to one node.
 * 
 * @author Jan Rojcek
 */
public class TableSheet extends JScrollPane {

	/** table */
	transient protected JTable table;

	/** model */
	transient private NodeTableModel tableModel;

	/**
	 * Create table view with default table model.
	 */
	public TableSheet() {
		tableModel = new NodeTableModel();
		initializeView();
	}

	/**
	 * Create table view with users table model.
	 */
	public TableSheet(NodeTableModel tableModel) {
		this.tableModel = tableModel;
		initializeView();
	}

	private void initializeView() {
		table = createTable();
		initializeTable();

		setViewportView(table);

		// do not care about focus
		setRequestFocusEnabled(false);
	}

	/**
	 * Set rows.
	 * 
	 * @param props
	 *            rows
	 */
	public void setNodes(Node[] nodes) {
		tableModel.setNodes(nodes);
	}

	/**
	 * Set columns.
	 * 
	 * @param nodes
	 *            columns
	 */
	public void setProperties(Node.Property[] props) {
		tableModel.setProperties(props);
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
	public final void setAutoResizeMode(int mode) {
		table.setAutoResizeMode(mode);
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
	public final int getAutoResizeMode() {
		return table.getAutoResizeMode();
	}

	/**
	 * Sets preferred width of table column
	 * 
	 * @param index
	 *            column index
	 * @param width
	 *            preferred column width
	 */
	public final void setColumnPreferredWidth(int index, int width) {
		table.getColumnModel().getColumn(index).setPreferredWidth(width);
	}

	/**
	 * Gets preferred width of table column
	 * 
	 * @param index
	 *            column index
	 * @param width
	 *            preferred column width
	 */
	public final int getColumnPreferredWidth(int index) {
		return table.getColumnModel().getColumn(index).getPreferredWidth();
	}

	/**
	 * Allows to subclasses provide its own table.
	 * 
	 * @param tm
	 *            node table model
	 * @return table which will be placed into scroll pane
	 */
	JTable createTable() {
		return new JTable();
	}

	/**
	 * Allows to subclasses initialize table
	 * 
	 * @param t
	 */
	private void initializeTable() {
		table.setModel(tableModel);
		table.setDefaultRenderer(Node.Property.class,
				new PropertyPanelTableCell());
		table.setDefaultEditor(Node.Property.class,
				new PropertyPanelTableCell());

		table.setShowGrid(false);
		table.setIntercellSpacing(new Dimension(0, 0));
	}

	/**
	 * Notifies an exception to error manager or prints its it to stderr.
	 * 
	 * @param ex
	 *            exception to notify
	 */
	private static void notify(int severity, Throwable ex) {
		ErrorManager err = (ErrorManager) org.openide.util.Lookup.getDefault()
				.lookup(ErrorManager.class);

		if (err != null) {
			err.notify(severity, ex);
		} else {
			ex.printStackTrace();
		}
	}

	/**
	 * TableCellEditor/Renderer implementation. Component returned is the
	 * PropertyPanel
	 */
	private class PropertyPanelTableCell extends AbstractCellEditor implements
			TableCellEditor, PropertyChangeListener, TableModelListener,
			TableCellRenderer {
		/** Property model used in cell editor property panel */
		private WrapperPropertyModel propModel;
		/** Actually editted node (its property) */
		private Node node;
		/** Editted property */
		private Node.Property prop;

		/**
		 * Returns <code>null<code>.
		 * 
		 * @return <code>null</code>
		 */
		public Object getCellEditorValue() {
			return null;
		}

		/**
		 * Returns editor of property.
		 * 
		 * @param table
		 * @param value
		 * @param isSelected
		 * @param r
		 *            row
		 * @param c
		 *            column
		 * @return <code>PropertyPanel</code>
		 */
		public Component getTableCellEditorComponent(JTable table,
				Object value, boolean isSelected, int r, int c) {
			prop = (Node.Property) value;
			propModel = new WrapperPropertyModel(prop);
			node = tableModel.nodeForRow(r);
			node.addPropertyChangeListener(this);
			tableModel.addTableModelListener(this);
			// create property panel
			PropertyPanel propPanel = new PropertyPanel(propModel,
					prop.canWrite() ? 0 // PropertyPanel.PREF_INPUT_STATE
							: PropertyPanel.PREF_READ_ONLY);
			return propPanel;
		}

		/**
		 * Cell should not be selected
		 * 
		 * @param ev
		 *            event
		 * @return <code>false</code>
		 */
		public boolean shouldSelectCell(EventObject ev) {
			return false;
		}

		/**
		 * Return true.
		 * 
		 * @param e
		 *            event
		 * @return <code>true</code>
		 */
		public boolean isCellEditable(EventObject e) {
			return true;
		}

		/**
		 * Forwards node property change to property model
		 * 
		 * @param evt
		 *            event
		 */
		public void propertyChange(PropertyChangeEvent evt) {
			Mutex.EVENT.readAccess(new Runnable() {
				public void run() {
					propModel.firePropertyChange();
				}
			});
		}

		/**
		 * Detaches listeners. Calls <code>fireEditingStopped</code> and returns
		 * true.
		 * 
		 * @return true
		 */
		public boolean stopCellEditing() {
			if (prop != null)
				detachEditor();
			return super.stopCellEditing();
		}

		/**
		 * Detaches listeners. Calls <code>fireEditingCanceled</code>.
		 */
		public void cancelCellEditing() {
			if (prop != null)
				detachEditor();
			super.cancelCellEditing();
		}

		/**
		 * Table has changed. If underlied property was switched then cancel
		 * editing.
		 * 
		 * @param e
		 *            event
		 */
		public void tableChanged(TableModelEvent e) {
			int r = table.getEditingRow();
			int c = table.getEditingColumn();
			if ((r < 0) || (c < 0) || (r >= tableModel.getRowCount())
					|| (c >= tableModel.getColumnCount())
					|| (prop != tableModel.getValueAt(r, c)))
				cancelCellEditing();
		}

		/**
		 * Removes listeners and frees resources.
		 */
		private void detachEditor() {
			node.removePropertyChangeListener(this);
			tableModel.removeTableModelListener(this);
			node = null;
			prop = null;
			propModel = null;
		}

		//
		// Renderer
		//

		/** Null panel is used if cell value is null */
		private JLabel nullPanel;
		/** Cache for property panels <PropertyEditor.class, PropertyPanel> */
		private HashMap panelCache = new HashMap();
		/** Cache for propert models <PropertyEditor.class, PropertyModel> */
		private HashMap modelCache = new HashMap();

		/**
		 * Getter for actual cell renderer.
		 * 
		 * @param table
		 * @param value
		 * @param isSelected
		 * @param hasFocus
		 * @param row
		 * @param column
		 * @return <code>PropertyPanel</code>
		 */
		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {
			Node.Property prop = (Node.Property) value;
			if (prop != null) {
				Class editorClass = prop.getPropertyEditor().getClass();
				PropertyPanel propPanel = (PropertyPanel) panelCache
						.get(editorClass);
				// if there is not property panel with same editor class then
				// create one
				if (propPanel != null) {
					WrapperPropertyModel propModel = (WrapperPropertyModel) modelCache
							.get(editorClass);
					propModel.setWrappedProperty(prop);
				} else {
					WrapperPropertyModel propModel = new WrapperPropertyModel(
							prop);
					// Now it doesn't differentiate between read-only and
					// read-write property
					propPanel = new PropertyPanel(propModel, 0);
					// prop.canWrite() ? 0 : PropertyPanel.PREF_READ_ONLY);
					panelCache.put(editorClass, propPanel);
					modelCache.put(editorClass, propModel);
				}
				return propPanel;
			}
			if (nullPanel == null) {
				nullPanel = new JLabel("-");
				nullPanel.setHorizontalAlignment(JLabel.CENTER);
				nullPanel.setOpaque(true);
				nullPanel.setBorder(new javax.swing.border.CompoundBorder(
						new javax.swing.border.MatteBorder(0, 0, 1, 1,
								SystemColor.controlDkShadow),
						new javax.swing.border.MatteBorder(1, 1, 0, 0,
								SystemColor.controlLtHighlight)));
			}
			return nullPanel;
		}
	}

	/**
	 * Wraps Node.Property with PropertyModel.
	 */
	private static class WrapperPropertyModel implements ExPropertyModel {

		PropertyChangeSupport support = new PropertyChangeSupport(this);
		/** Wrapped property */
		Node.Property prop;

		/**
		 * Creates new wrapped property
		 * 
		 * @param prop
		 *            wrapped property
		 */
		public WrapperPropertyModel(Node.Property prop) {
			this.prop = prop;
		}

		/**
		 * Setter for wrapped property
		 * 
		 * @param prop
		 *            wrapped property
		 */
		public void setWrappedProperty(Node.Property prop) {
			this.prop = prop;
			firePropertyChange();
		}

		/**
		 * Getter for current value of a property.
		 */
		public Object getValue() throws InvocationTargetException {
			try {
				return prop.getValue();
			} catch (IllegalAccessException e) {
				TableSheet.notify(ErrorManager.INFORMATIONAL, e);
				throw new InvocationTargetException(e);
			}
		}

		/**
		 * Setter for a value of a property.
		 * 
		 * @param v
		 *            the value
		 * @exeception InvocationTargetException
		 */
		public void setValue(Object v) throws InvocationTargetException {
			if (!prop.canWrite())
				return;
			try {
				prop.setValue(v);
			} catch (IllegalAccessException e) {
				TableSheet.notify(ErrorManager.INFORMATIONAL, e);
				throw new InvocationTargetException(e);
			}
		}

		/**
		 * The class of the property.
		 */
		public Class getPropertyType() {
			return prop.getValueType();
		}

		/**
		 * The class of the property editor or <CODE>null</CODE> if default
		 * property editor should be used.
		 */
		public Class getPropertyEditorClass() {
			return prop.getPropertyEditor().getClass();
		}

		/**
		 * Add listener to change of the value.
		 */
		public void addPropertyChangeListener(PropertyChangeListener l) {
			support.addPropertyChangeListener(l);
		}

		/**
		 * Remove listener to change of the value.
		 */
		public void removePropertyChangeListener(PropertyChangeListener l) {
			support.removePropertyChangeListener(l);
		}

		public void firePropertyChange() {
			support.firePropertyChange(PROP_VALUE, null, null);
		}

		/**
		 * Returns an array of beans/nodes that this property belongs to.
		 */
		public Object[] getBeans() {
			return new Object[0];
		}

		/**
		 * Returns descriptor describing the property.
		 */
		public FeatureDescriptor getFeatureDescriptor() {
			return prop;
		}

	}

	/**
	 * Synchronized table view with other view (TreeView, ListView). Two views
	 * (scroll panes) have only one vertical scroll bar. Right view is allways
	 * table view, left view could be any scroll pane. Use
	 * ControlledTableView.compoundScrollPane() to get compound view.
	 */
	static class ControlledTableView extends TableSheet {

		/** Scroll pane which controls vertical scroll bar */
		JScrollPane controllingView;
		/** Table like header of controlling view */
		JLabel header;

		/**
		 * Creates controlled scroll pane with <code>contView</code> on the
		 * left, table view on the right
		 */
		ControlledTableView(JScrollPane contrView) {
			super();
			this.controllingView = contrView;
			initializeView();
		}

		/**
		 * Creates controlled scroll pane with <code>contView</code> on the
		 * left, table view on the right.
		 */
		ControlledTableView(JScrollPane contrView, NodeTableModel ntm) {
			super(ntm);
			this.controllingView = contrView;
			initializeView();
		}

		/**
		 * initialize view
		 */
		private void initializeView() {
			// change UI so only left view controlls vertical scrollbar
			setUI(new ChangedScrollPaneUI());

			// adjustment of controlling view
			Component comp = controllingView.getViewport().getView();
			JPanel compBackground = new JPanel() {
				// overriden to assume height of right view horizontal scroll
				// bar
				public Dimension getPreferredSize() {
					if ((!controllingView.getHorizontalScrollBar().isVisible())
							&& (getHorizontalScrollBar().isVisible())) {
						invalidate();
						Dimension dim = super.getPreferredSize();
						dim.height += getHorizontalScrollBar()
								.getPreferredSize().height;
						return dim;
					}
					return super.getPreferredSize();
				}
			};
			compBackground.setLayout(new GridLayout());
			// left view is inserted so we can control its height
			compBackground.add(comp);
			controllingView.setViewportView(compBackground);
			// boths view share one vertical scrollbar
			setVerticalScrollBar(controllingView.getVerticalScrollBar());
			setHorizontalScrollBar(new JScrollBar(JScrollBar.HORIZONTAL));
			getViewport().setBackground(Color.white);
			// table like header
			header = (JLabel) new JTable()
					.getTableHeader()
					.getDefaultRenderer()
					.getTableCellRendererComponent(null, " ", false, false, 0,
							0);
			controllingView.setPreferredSize(new Dimension(200, 0));
		}

		/**
		 * Overriden to return table with controlled height
		 * 
		 * @param tm
		 *            table model
		 * @return table
		 */
		JTable createTable() {
			return new JTable() {
				// overriden to assume height of left view horizontal scroll bar
				public Dimension getPreferredSize() {
					Dimension dim = super.getPreferredSize();
					if ((!getHorizontalScrollBar().isVisible())
							&& (controllingView != null)
							&& (controllingView.getHorizontalScrollBar()
									.isVisible()))
						dim.height += controllingView.getHorizontalScrollBar()
								.getPreferredSize().height;
					return dim;
				}
			};
		}

		/**
		 * Overriden because I can't set border to null by calling
		 * setBorder(null).
		 * 
		 * @param border
		 */
		public void setBorder(javax.swing.border.Border border) {
			super.setBorder(null);
		}

		/**
		 * Is used to synchronize table row height with left view.
		 */
		void setRowHeight(int h) {
			table.setRowHeight(h);
			getVerticalScrollBar().setUnitIncrement(h);
		}

		/**
		 * Sets text of table like header above left scroll pane.
		 */
		void setHeaderText(String text) {
			header.setText(text);
		}

		/**
		 * Sets preferred size of left scroll pane.
		 */
		void setControllingViewWidth(int width) {
			controllingView.setPreferredSize(new Dimension(width, 0));
		}

		/**
		 * Gets preferred size of left scroll pane.
		 */
		int getControllingViewWidth() {
			return controllingView.getPreferredSize().width;
		}

		/**
		 * Returns component which contains two synchronized scroll panes. Above
		 * left one is placed table like header.
		 */
		JComponent compoundScrollPane() {
			JPanel leftPanel = new JPanel(new BorderLayout());
			leftPanel.add(header, BorderLayout.NORTH);
			leftPanel.add(controllingView, BorderLayout.CENTER);

			JPanel dummyPanel = new JPanel(new BorderLayout());
			dummyPanel.add(this);

			JPanel panel = new JPanel();
			BoxLayout layout = new BoxLayout(panel, BoxLayout.X_AXIS);
			panel.setLayout(layout);
			panel.add(leftPanel);
			panel.add(dummyPanel);
			return panel;
		}
	}

	/**
	 * Overriden to assure that table view will not change vertical scroll bar
	 * position
	 */
	static class ChangedScrollPaneUI extends
			javax.swing.plaf.basic.BasicScrollPaneUI {

		protected void syncScrollPaneWithViewport() {
			JViewport viewport = scrollpane.getViewport();
			JScrollBar vsb = scrollpane.getVerticalScrollBar();
			JScrollBar hsb = scrollpane.getHorizontalScrollBar();
			JViewport rowHead = scrollpane.getRowHeader();
			JViewport colHead = scrollpane.getColumnHeader();

			if (viewport != null) {
				Dimension extentSize = viewport.getExtentSize();
				Dimension viewSize = viewport.getViewSize();
				Point viewPosition = viewport.getViewPosition();

				/*
				 * if (vsb != null) { int extent = extentSize.height; int max =
				 * viewSize.height; int value = Math.max(0,
				 * Math.min(viewPosition.y, max - extent)); vsb.setValues(value,
				 * extent, 0, max); }
				 */
				if (hsb != null) {
					int extent = extentSize.width;
					int max = viewSize.width;
					int value = Math.max(0,
							Math.min(viewPosition.x, max - extent));
					hsb.setValues(value, extent, 0, max);
				}

				if (rowHead != null) {
					Point p = rowHead.getViewPosition();
					p.y = viewport.getViewPosition().y;
					rowHead.setViewPosition(p);
				}

				if (colHead != null) {
					Point p = colHead.getViewPosition();
					p.x = viewport.getViewPosition().x;
					colHead.setViewPosition(p);
				}
			}
		}
	}

	/**
	 * Overriden because I didn't find better way how to use scroll pane border
	 * in JPanel. So panel returned by ControlledTableView.compoundScrollPane()
	 * should be placed into JScrollPane with MyScrollPaneLayout. Use
	 * JScrollPane.add("CompoundScrollPane",
	 * ControlledTableView.compoundScrollPane()).
	 */
	static class MyScrollPaneLayout extends javax.swing.ScrollPaneLayout {
		final static String COMPOUND_SCROLLPANE = "CompoundScrollPane";
		private Component view;

		public void addLayoutComponent(String s, Component c) {
			if (s.equals(COMPOUND_SCROLLPANE)) {
				view = c;
			}
		}

		public void removeLayoutComponent(Component c) {
			if (c == view)
				view = null;
		}

		/** return vies preferred size */
		public Dimension preferredLayoutSize(Container parent) {
			Dimension dim = view == null ? new Dimension(0, 0) : view
					.getPreferredSize();
			Insets insets = parent.getInsets();
			dim.width += insets.left + insets.right;
			dim.height += insets.top + insets.bottom;
			return dim;
		}

		public Dimension minimumLayoutSize(Container parent) {
			return preferredLayoutSize(parent);
		}

		public void layoutContainer(Container parent) {
			if (view == null)
				return;
			Insets insets = parent.getInsets();
			Dimension dim = parent.getSize();
			view.setBounds(insets.left, insets.top, dim.width - insets.left
					- insets.right, dim.height - insets.top - insets.bottom);
		}
	}
}
