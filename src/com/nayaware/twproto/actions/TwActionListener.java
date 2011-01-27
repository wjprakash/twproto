/*
 * TwActionListener.java
 * Author - Winston Prakash
 */

package com.nayaware.twproto.actions;

import com.nayaware.twproto.events.*;

public interface TwActionListener {
	public void twActionPerformed(TwActionEvent evt);

	public void showErrorMessage(TwErrorEvent evt);
}
