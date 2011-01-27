/*
 * TwAction.java
 * Author - Winston Prakash
 */

package com.nayaware.twproto.actions;

import com.nayaware.twproto.events.*;
import com.nayaware.twproto.nodes.*;
import com.nayaware.twproto.ui.*;

import javax.swing.*;
import java.util.*;
import java.awt.*;

import org.openide.util.actions.*;
import org.openide.util.HelpCtx;
import org.openide.nodes.*;

/**
 * TwAction implements the TwActionDialogListener methods when the buttons in
 * the TwActionDialog fires these events they will be listened by TwAction and
 * appropriate action will be taken. There are explicit methods to set/get the
 * TwActionDialog. If no TwActionDialog is set then no dialog will be shown by
 * the action
 */

public class TwAction extends NodeAction implements TwActionDialogListener {

	// Type of menu presenter
	public static final int TW_BUTTON = 0;
	public static final int TW_RADIOBUTTON = 1;
	public static final int TW_CHECKBOX = 2;

	protected TwActionDialog twActionDialog = null;
	protected HashMap parameters;
	protected int type;
	protected Vector actionListeners = new Vector();

	protected TwNode defaultNode = null;
	protected TwNode[] twNodes = null;

	protected JComponent presenter = null;

	/**
	 * while the action is performed it should periodically check the variable
	 * <code>stopAction</code> to find out if there is a request to stop
	 * performing the action. If <code>stopAction</code> is true then stop
	 * performing the action.
	 */
	protected boolean stopAction = false;

	/** Creates new TwAction */
	public TwAction() {
	}

	/**
	 * Defines an <code>TwAction</code> object with the specified description
	 * string and a default icon.
	 */
	public TwAction(String name) {
		this();
		putValue(Action.NAME, name);
	}

	/**
	 * Defines an <code>Action</code> object with the specified description
	 * string and a the specified icon.
	 */
	public TwAction(String name, Icon icon) {
		this(name);
		putValue(Action.SMALL_ICON, icon);
	}

	/**
	 * Defines an <code>Action</code> object with the specified description
	 * string and a the specified icon and ToolTip text
	 */
	public TwAction(String name, Icon icon, String toolTip) {
		this(name, icon);
		putValue("ToolTip", toolTip);
	}

	/**
	 * Sets the name of the action
	 */
	public void setName(String name) {
		putValue(Action.NAME, name);
	}

	/**
	 * Gets the name of the action
	 */
	public String getName() {
		return (String) getValue(Action.NAME);
	}

	/**
	 * Sets the ToolTip text of the Toolbar icon
	 */
	public void setToolTipText(String toolTip) {
		putValue("ToolTip", toolTip);
	}

	/**
	 * Gets the ToolTip text of the Toolbar icon
	 */
	public String getToolTipText() {
		return (String) getValue("ToolTip");
	}

	/**
	 * Set the type of the Action
	 */
	public void setType(int t) {
		this.type = t;
	}

	/**
	 * get the type of the Action
	 */
	public int getType() {
		return type;
	}

	/**
	 * Gets the Icon reource associated with the action small icon Currently set
	 * to null
	 */
	public String iconResource() {
		return null;
	}

	public boolean isEnable() {
		return super.isEnabled();
	}

	/**
	 * Adds a <code>TwActionListener</code> to the listener list. The listener
	 * is registered for the specified action.
	 * 
	 * @param listener
	 *            The <code>TwActionListener</code> to be added
	 * 
	 * @see TwActionListener
	 */
	public void addTwActionListener(TwActionListener twActionListener) {
		actionListeners.add(twActionListener);
	}

	/**
	 * remove the <code>TwActionListener</code> from the listener list. This
	 * listener should be a registered listener for the specified action.
	 * 
	 * @param listener
	 *            The <code>TwActionListener</code> to be added
	 * 
	 * @see TwActionListener
	 */
	public void removeTwActionListener(TwActionListener twActionListener) {
		actionListeners.remove(twActionListener);
	}

	/**
	 * Sets the current default object. This object is used to set the state of
	 * the action as enbled or disabled. Subclasses may overrides this method
	 */
	public void setDefaultNode(TwNode defNode) {
		defaultNode = defNode;
	}

	/**
	 * Return the current default object of the action
	 */
	public TwNode getDefaultNode() {
		return defaultNode;
	}

	/**
	 * Sets the objects on which the action will be performed
	 */
	public void setNodes(TwNode[] objs) {
		twNodes = objs;
	}

	/**
	 * return the objects on which the action will be performed
	 */
	public TwNode[] getNodes() {
		return twNodes;
	}

	/**
	 * Set the component that presents the action of any
	 */

	public void setPresenter(JComponent comp) {
		presenter = comp;
	}

	/**
	 * Set the component that presents the action of any
	 */

	public JComponent getPresenter() {
		return presenter;
	}

	/**
	 * Set the state of the presenter in case of Radio Button or Check Box
	 */

	public void setPresenterState(boolean state) {
		if (presenter != null) {
			if (presenter instanceof JRadioButtonMenuItem) {
				JRadioButtonMenuItem menuItem = (JRadioButtonMenuItem) presenter;
				menuItem.setSelected(true);
			}
			if (presenter instanceof JCheckBoxMenuItem) {
				JCheckBoxMenuItem menuItem = (JCheckBoxMenuItem) presenter;
				menuItem.setSelected(true);
			}
		}
	}

	public boolean getPresenterState() {
		if (presenter != null) {
			if (presenter instanceof JRadioButtonMenuItem) {
				JRadioButtonMenuItem menuItem = (JRadioButtonMenuItem) presenter;
				return menuItem.isSelected();
			}
			if (presenter instanceof JCheckBoxMenuItem) {
				JCheckBoxMenuItem menuItem = (JCheckBoxMenuItem) presenter;
				return menuItem.isSelected();
			}
		}
		return false;
	}

	/**
	 * Returns the Menu Presenter By default creates a Menu Item with action as
	 * a template and return it as Menu Presenter Action itself will be listener
	 * to the JMenuItem The default listener method is
	 * <code>actionPerformed()</code>
	 */
	public JMenuItem getMenuPresenter() {
		return getMenuPresenter(0);
	}

	/**
	 * Returns the particular type of Menu Presenter (Radio Button or Check Box)
	 */
	public JMenuItem getMenuPresenter(int type) {
		JMenuItem menuItem = null;
		switch (type) {
		case 0:
			menuItem = new JMenuItem(this);
			break;
		case 1:
			menuItem = new JRadioButtonMenuItem(this);
			break;
		case 2:
			menuItem = new JCheckBoxMenuItem(this);
			break;
		}
		if (menuItem != null) {
			menuItem.setIcon(null);
			menuItem.setFont(new Font("Arial", 0, 12));
		}
		setPresenter(presenter);
		return menuItem;
	}

	/**
	 * Returns the Menu Presenter By default creates a Menu with action as a
	 * template and return it as Tool Bar Presenter Action itself will be a
	 * listener to the Tool Bar Button The default listener method is
	 * <code>actionPerformed()</code>
	 */
	public JComponent getToolBarPresenter() {
		JButton button = new JButton(this);
		button.setText(null);
		button.setToolTipText(this.getToolTipText());
		button.setIcon(this.getIcon());
		setPresenter(button);
		return button;
	}

	/**
	 * This method invokes the action in a separate thread. This method and
	 * <code>performAction()</code> should typically overridden by the subclass
	 * to complete the specific action.
	 */

	protected void performAction(Node[] nodes) {

		final TwActionEvent twActionEvent = new TwActionEvent(this, nodes);
		Runnable runnable = new Runnable() {
			public void run() {
			}
		};
		Thread thr = new Thread(runnable);
		thr.start();
	}

	/**
	 * Called by <code> actionPerformed</code> in a separate thread Note: This
	 * method should be overridden by the subclass to complete the specific
	 * action.
	 */

	public void performDialogAction() {

	}

	public void showActionDialog() {
		showActionDialog(twActionDialog, true);
	}

	/**
	 * Show the Action Dialog if any exists as modal;
	 */
	public void showActionDialog(TwActionDialog dialog, boolean modal) {
		if (dialog == null)
			JOptionPane.showMessageDialog(null,
					"ShowActionDialog called without setting ActionDialog");
		twActionDialog = dialog;
		twActionDialog.setActionDialogListener(this);
		twActionDialog.setModal(modal);
		twActionDialog.setUserPanelValues();
		Runnable runnable = new Runnable() {
			public void run() {
				twActionDialog.show();
			}
		};
		Thread thr = new Thread(runnable);
		thr.start();
	}

	/**
	 * Sends the event <code>fireTwActionPerformed</code> to the
	 * <code>TwActionListener</code> to inform that the specific action has been
	 * performed
	 */
	public void fireTwActionPerformed(TwActionEvent twActionEvent) {
		if (actionListeners == null)
			return;
		for (int i = 0; i < actionListeners.size(); i++) {
			TwActionListener twActionListener = (TwActionListener) actionListeners
					.elementAt(i);
			twActionListener.twActionPerformed(twActionEvent);
		}
	}

	/**
	 * Sends the event <code>twErrorMessage</code> to the
	 * <code>TwActionListener</code> to inform that the specific action has been
	 * performed
	 */
	public void fireTwErrorMessage(TwErrorEvent twErrorEvent) {
		if (actionListeners == null)
			return;
		for (int i = 0; i < actionListeners.size(); i++) {
			TwActionListener twActionListener = (TwActionListener) actionListeners
					.elementAt(i);
			twActionListener.showErrorMessage(twErrorEvent);
		}
	}

	/*
	 * Implements the TwActionDialogListener method startAction
	 */
	public void startAction(TwActionDialogEvent evt) {
		final TwActionEvent twActionEvent = new TwActionEvent(this, twNodes);
		stopAction = false;
		Runnable runnable = new Runnable() {
			public void run() {
				twActionDialog.enableButton(TwActionDialog.START_BUTTON, false);
				twActionDialog.enableButton(TwActionDialog.STOP_BUTTON, true);
				twActionDialog.enableButton(TwActionDialog.CLOSE_BUTTON, false);
				performDialogAction();
				twActionDialog.enableButton(TwActionDialog.START_BUTTON, true);
				twActionDialog.enableButton(TwActionDialog.STOP_BUTTON, false);
				twActionDialog.enableButton(TwActionDialog.CLOSE_BUTTON, true);
			}
		};
		Thread thr = new Thread(runnable);
		thr.start();
	}

	/*
	 * Implements the TwActionDialogListener method stopAction
	 */
	public void stopAction(TwActionDialogEvent evt) {
		// Send request to exit from performAction() thread
		// performAction should periodically check this variable.
		stopAction = true;
	}

	/*
	 * Implements the TwActionDialogListener method closeActionDialog
	 */
	public void closeActionDialog(TwActionDialogEvent evt) {
		twActionDialog.setVisible(false);
		twActionDialog.dispose();
	}

	/*
	 * Implements the TwActionDialogListener method showSessionLog
	 */
	public void showSessionLog(TwActionDialogEvent evt) {
	}

	/*
	 * Implements the TwActionDialogListener method showActionHelp
	 */
	public void showActionHelp(TwActionDialogEvent evt) {
	}

	protected boolean enable(Node[] nodes) {
		return true;
	}

	public HelpCtx getHelpCtx() {
		return HelpCtx.DEFAULT_HELP;
	}

}
