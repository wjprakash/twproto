/*
 * TwActionDialogListener.java
 */

package com.nayaware.twproto.ui;

import com.nayaware.twproto.events.*;

public interface TwActionDialogListener {

	public void startAction(TwActionDialogEvent evt);

	public void stopAction(TwActionDialogEvent evt);

	public void closeActionDialog(TwActionDialogEvent evt);

	public void showSessionLog(TwActionDialogEvent evt);

	public void showActionHelp(TwActionDialogEvent evt);
}
