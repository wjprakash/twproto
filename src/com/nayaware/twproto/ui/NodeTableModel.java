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

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

import org.openide.nodes.Node;

/**
 * Table model with properties (<code>Node.Property</code>) as columns and nodes
 * (<code>Node</code>) as rows. It is used as model for displaying node
 * properties in table. Each column is represented by <code>Node.Property</code>
 * object. Each row is represented by <code>Node</code> object. Each cell
 * contains <code>Node.Property</code> property which equals with column object
 * and should be in property sets of row representant (<code>Node</code>).
 * 
 * @author Jan Rojcek
 * @since 1.7
 */
public class NodeTableModel extends javax.swing.table.AbstractTableModel {

	/** columns of model */
	private Node.Property[] propertyColumns = new Node.Property[] {};
	/** rows of model */
	private Node[] nodeRows = new Node[] {};
	/** listener on node properties changes, recreates displayed data */
	private PropertyChangeListener pcl = new PropertyChangeListener() {
		public void propertyChange(PropertyChangeEvent ev) {
			fireTableDataChanged();
		}
	};

	/**
	 * Set rows.
	 * 
	 * @param props
	 *            rows
	 */
	public void setNodes(Node[] nodes) {
		for (int i = 0; i < nodeRows.length; i++)
			nodeRows[i].removePropertyChangeListener(pcl);
		nodeRows = nodes;
		for (int i = 0; i < nodeRows.length; i++)
			nodeRows[i].addPropertyChangeListener(pcl);
		fireTableDataChanged();
	}

	/**
	 * Set columns.
	 * 
	 * @param nodes
	 *            columns
	 */
	public void setProperties(Node.Property[] props) {
		propertyColumns = props;
		fireTableStructureChanged();
	}

	/**
	 * Returns node property if found in nodes property sets. Could be overriden
	 * to return property which is not in nodes property sets.
	 * 
	 * @param node
	 *            represents single row
	 * @param prop
	 *            represents column
	 * @return nodes property
	 */
	protected Node.Property getPropertyFor(Node node, Node.Property prop) {
		Node.PropertySet[] propSets = node.getPropertySets();
		for (int i = 0; i < propSets.length; i++) {
			Node.Property[] props = propSets[i].getProperties();
			for (int j = 0; j < props.length; j++) {
				if (prop.equals(props[j]))
					return props[j];
			}
		}
		return null;
	}

	/**
	 * Helper method to ask for a node representant of row.
	 */
	Node nodeForRow(int row) {
		return nodeRows[row];
	}

	//
	// TableModel methods
	//

	/**
	 * Getter for row count.
	 * 
	 * @return row count
	 */
	public int getRowCount() {
		return nodeRows.length;
	}

	/**
	 * Getter for column count.
	 * 
	 * @return column count
	 */
	public int getColumnCount() {
		return propertyColumns.length;
	}

	/**
	 * Getter for property.
	 * 
	 * @param row
	 *            table row index
	 * @param column
	 *            table column index
	 * @return property at (row, column)
	 */
	public Object getValueAt(int row, int column) {
		return getPropertyFor(nodeRows[row], propertyColumns[column]);
	}

	/**
	 * Cell is editable only if it has non null value.
	 * 
	 * @param row
	 *            table row index
	 * @param column
	 *            table column index
	 * @return true if cell contains non null value
	 */
	public boolean isCellEditable(int row, int column) {
		return getValueAt(row, column) != null;
	}

	/**
	 * Getter for column class.
	 * 
	 * @param column
	 *            table column index
	 * @return <code>Node.Property.class</code>
	 */
	public Class getColumnClass(int column) {
		return Node.Property.class;
	}

	/**
	 * Getter for column name
	 * 
	 * @param column
	 *            table column index
	 * @return display name of property which represents column
	 */
	public String getColumnName(int column) {
		return propertyColumns[column].getDisplayName();
	}
}
