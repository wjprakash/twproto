/**
 * TwExplorerView.java
 * Author - Winston Prakash
 */

package com.nayaware.twproto.ui;

import org.openide.TopManager;
import org.openide.loaders.*;
import org.openide.windows.*;
import org.openide.util.NbBundle;
import org.openide.windows.*;
import org.openide.explorer.*;
import org.openide.explorer.view.*;
import org.openide.nodes.*;

import com.nayaware.twproto.nodes.*;
import com.nayaware.twproto.util.*;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.beans.*;
import java.awt.event.*;

public class TwExplorerView extends ExplorerPanel {

	JLabel contentLabel = new JLabel("Local Workspace List");
	private JSplitPane splitPane;
	// private ListView view = new TwIconView();
	private ListView view = new TwListView();
	// private ListView view = new ListTableView();
	private JPanel contentPanel = new JPanel();
	private JPanel headerPanel = new JPanel();

	public TwExplorerView() {
		initComponents();
		setCloseOperation(CLOSE_LAST);
		setName("Explorer View");
	}

	private void initComponents() {
		TreeView treeView = new TwTreeView() {
			protected void selectionChanged(Node[] nodes, ExplorerManager em)
					throws PropertyVetoException {
				if (nodes.length > 0) {
					if (!nodes[0].isLeaf()) {
						TwNode node = (TwNode) nodes[0];
						contentLabel.setText(node.getPath());
						em.setExploredContext(nodes[0]);
					}
					em.setSelectedNodes(nodes);
				}
			}
		};
		treeView.setSize(100, 400);
		splitPane = new javax.swing.JSplitPane();
		splitPane.setLeftComponent(treeView);
		splitPane.setRightComponent(createContentsPanel());
		splitPane.setOneTouchExpandable(true);
		splitPane.setDividerSize(10);
		splitPane.setDividerLocation(250);
		add(splitPane);
		// getExplorerManager().setRootContext(new TwFilterNode(new
		// TwLocalWorkspaceListNode()));
		getExplorerManager().setRootContext(new TwLocalWorkspaceListNode());
	}

	private JPanel createContentsPanel() {

		// ((ListTableView)view).setProperties(createProperySet());

		contentPanel.setLayout(new BorderLayout());

		headerPanel.setLayout(new BorderLayout());
		// headerPanel.setBorder(new EtchedBorder());

		JLabel hdrLabel = new JLabel("Contents of:");
		hdrLabel.setPreferredSize(new Dimension(75, 25));

		headerPanel.add(hdrLabel, BorderLayout.WEST);

		contentLabel.setPreferredSize(new Dimension(150, 25));
		contentLabel.setBorder(new EtchedBorder());

		headerPanel.add(contentLabel, BorderLayout.CENTER);

		JToolBar hdrToolBar = new JToolBar();
		hdrToolBar.setBorderPainted(false);

		JButton upButton = new JButton();
		upButton.setIcon(TwResources.getImageIcon(TwResources.TW_UP_DIR,
				BeanInfo.ICON_COLOR_16x16));
		upButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				Node node = getExplorerManager().getExploredContext();
				if (node.getParentNode() != null)
					getExplorerManager().setExploredContext(
							node.getParentNode());
			}
		});
		hdrToolBar.add(upButton);

		JButton detailButton = new JButton();
		detailButton.setIcon(TwResources.getImageIcon(
				TwResources.TW_DETAIL_VIEW, BeanInfo.ICON_COLOR_16x16));
		detailButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				view = new ListTableView();
				((ListTableView) view).setProperties(createProperySet());
				contentPanel.removeAll();
				contentPanel.add(headerPanel, BorderLayout.NORTH);
				view.setSize(500, 600);
				contentPanel.add(view, BorderLayout.CENTER);
			}
		});
		hdrToolBar.add(detailButton);

		JButton listButton = new JButton();
		listButton.setIcon(TwResources.getImageIcon(TwResources.TW_LIST_VIEW,
				BeanInfo.ICON_COLOR_16x16));
		listButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				view = new TwListView();
				contentPanel.removeAll();
				contentPanel.add(headerPanel, BorderLayout.NORTH);
				view.setSize(500, 600);
				contentPanel.add(view, BorderLayout.CENTER);
			}
		});
		hdrToolBar.add(listButton);

		headerPanel.add(hdrToolBar, BorderLayout.EAST);

		contentPanel.add(headerPanel, BorderLayout.NORTH);
		view.setSize(500, 600);
		contentPanel.add(view, BorderLayout.CENTER);
		return contentPanel;

	}

	public void open(Workspace ws) {
		super.open(ws);
	}

	protected void updateTitle() {
	}

	protected Node.Property[] createProperySet() {
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
