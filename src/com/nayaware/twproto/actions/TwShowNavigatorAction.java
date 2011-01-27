/** TwShowNavigatorAction.java
 *
 * Author  Winston Prakash
 */
package com.nayaware.twproto.actions;

import org.openide.TopManager;
import org.openide.windows.*;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.CallableSystemAction;

import com.nayaware.twproto.dataobjects.*;
import com.nayaware.twproto.nodes.*;
import com.nayaware.twproto.ui.*;
import com.nayaware.twproto.util.*;

public class TwShowNavigatorAction extends CallableSystemAction {

	public void performAction() {
		TopManager tm = TopManager.getDefault();
		WindowManager wm = tm.getWindowManager();
		Workspace ws = wm.findWorkspace("TwVersioning");
		if (ws == null)
			ws = wm.getCurrentWorkspace();
		TwNavigator twNavigator = TwNavigator.getDefault();
		twNavigator.show(ws);
	}

	public String getName() {
		return "Show Navigator Window";
	}

	protected String iconResource() {
		return "Navigator.gif";
	}

	public HelpCtx getHelpCtx() {
		return HelpCtx.DEFAULT_HELP;
	}

}
