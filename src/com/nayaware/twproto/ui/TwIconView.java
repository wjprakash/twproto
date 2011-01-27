package com.nayaware.twproto.ui;

import java.awt.*;
import javax.swing.*;
import java.beans.*;
import javax.swing.tree.*;
import java.io.*;

import org.openide.explorer.*;
import org.openide.explorer.view.*;
import org.openide.nodes.*;

import com.nayaware.twproto.nodes.*;

import org.openide.awt.ListPane;

public class TwIconView extends TwListView implements Externalizable {

	public TwIconView() {
	}

	/**
	 * Creates the list that will display the data.
	 */
	protected JList createList() {
		JList list = new ListPane();
		list.setCellRenderer(new NodeRenderer(true));
		return list;
	}

}
