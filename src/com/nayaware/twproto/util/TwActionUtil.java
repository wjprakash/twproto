package com.nayaware.twproto.util;

import java.awt.*;
import java.io.IOException;
import java.text.MessageFormat;
import javax.swing.AbstractButton;

import org.openide.cookies.InstanceCookie;
import org.openide.loaders.DataFolder;
import org.openide.loaders.DataObject;
import org.openide.loaders.InstanceDataObject;
import org.openide.util.Utilities;
import org.openide.util.actions.SystemAction;
import org.openide.util.NbBundle;

/**
 * Otherwise uncategorized useful static methods.
 * 
 * @author Ian Formanek
 */
public final class TwActionUtil {

	/**
	 * A method which creates action in specified folder without setting order.
	 * 
	 * @param actionClass
	 *            the class of the action to add to the menu
	 * @param folder
	 *            the folder representing the Menu in which the action should be
	 *            added
	 * @exception IOException
	 *                is throws when there is a problem creating the .instance
	 *                files on the underlying filesystem
	 * @see #removeAction
	 * @deprecated Use of XML filesystem layers to install actions is preferred.
	 */
	public static InstanceDataObject createAction(Class actionClass,
			DataFolder folder) throws IOException {
		String actionName = actionClass.getName();
		InstanceDataObject ido = InstanceDataObject.find(folder, null,
				actionName);
		if (ido != null)
			return ido;
		return InstanceDataObject.create(folder, null, actionName);
	}

	/**
	 * A method which helps to add items to existing menus wisely.
	 * 
	 * @param actionClass
	 *            the class of the action to add to the menu
	 * @param folder
	 *            the folder representing the Menu in which the action should be
	 *            added
	 * @param relativeTo
	 *            name of an item relative to which the action should be added
	 * @param after
	 *            if true, the action will be added after the relativeTo item,
	 *            if false, the action will be added before the relativeTo item
	 * @param skipSeparator
	 *            if true, existing separators between the relativeTo item and
	 *            the place to add will be retained (i.e. the action will not be
	 *            put right next to the item but after/before the separator)
	 * @param separatorBefore
	 *            if true, a new separator will be created right before this
	 *            action
	 * @param separatorAfter
	 *            if true, a new separator will be created right before this
	 *            action
	 * @exception IOException
	 *                is throws when there is a problem creating the .instance
	 *                files on the underlying filesystem
	 * @see #removeAction
	 * @deprecated Use of XML filesystem layers to install actions is preferred.
	 */
	public static InstanceDataObject createAction(Class actionClass,
			DataFolder folder, String relativeTo, boolean after,
			boolean skipSeparator, boolean separatorBefore,
			boolean separatorAfter) throws IOException {
		String actionName = actionClass.getName();

		InstanceDataObject ido = InstanceDataObject.find(folder, null,
				actionName);
		if (ido != null)
			return ido;

		DataObject[] children = folder.getChildren();
		int indexToUse = -1;
		for (int i = 0; i < children.length; i++) {
			if (children[i] instanceof InstanceDataObject
					&& children[i].getPrimaryFile().getName()
							.indexOf(relativeTo) != -1) {
				indexToUse = i;
				break;
			}
		}
		InstanceDataObject actionInstance = InstanceDataObject.create(folder,
				null, actionName);

		if (indexToUse != -1) {
			if (after) {
				indexToUse += 1;
				if (skipSeparator) {
					if ((indexToUse < children.length)
							&& (children[indexToUse].getPrimaryFile().getName()
									.indexOf("JSeparator") != -1)) { // NOI18N
						indexToUse += 1;
					}
				}
			} else {
				if (skipSeparator) {
					if ((indexToUse > 0)
							&& (children[indexToUse - 1].getPrimaryFile()
									.getName().indexOf("JSeparator") != -1)) { // NOI18N
						indexToUse -= 1;
					}
				}
			}

			String shorterName = actionName.substring(
					actionName.lastIndexOf(".") + 1, actionName.length());
			// [PENDING] separators only work in menus, not toolbars...
			InstanceDataObject beforeSeparator = separatorBefore ? InstanceDataObject
					.create(folder, "SepBefore" + shorterName, // NOI18N
							"javax.swing.JSeparator") : // NOI18N
					null;
			InstanceDataObject afterSeparator = separatorAfter ? InstanceDataObject
					.create(folder, "SepAfter" + shorterName, // NOI18N
							"javax.swing.JSeparator") : // NOI18N
					null;

			int itemsAdded = 0;
			if (separatorBefore)
				itemsAdded++;
			if (separatorAfter)
				itemsAdded++;
			int currentIndex = indexToUse;

			DataObject[] newOrder = new DataObject[children.length + 1
					+ itemsAdded];
			System.arraycopy(children, 0, newOrder, 0, indexToUse);

			if (separatorBefore)
				newOrder[currentIndex++] = beforeSeparator;
			newOrder[currentIndex++] = actionInstance;
			if (separatorAfter)
				newOrder[currentIndex++] = afterSeparator;

			System.arraycopy(children, indexToUse, newOrder, indexToUse + 1
					+ itemsAdded, children.length - indexToUse);
			folder.setOrder(newOrder);
		}
		return actionInstance;
	}

	/*
	 * private static String getActionRealName (Class actionClass) {
	 * SystemAction action = SystemAction.get (actionClass); if (action != null)
	 * { String actionName = action.getName (); // Kill trailing ...: if
	 * (actionName.endsWith ("...")) // NOI18N actionName = actionName.substring
	 * (0, actionName.length () - 3); // Remove mnemonics and useless spaces:
	 * return Utilities.replaceString (actionName.trim (), "&", ""); // NOI18N }
	 * else { return Utilities.getShortClassName (actionClass); } }
	 */

	/**
	 * An opposite to createAction method which helps to remove menu items
	 * wisely. This method also removes any separators added automatically by
	 * the createAction method. Note that it simply tries to remove any
	 * instances with this class name, regardless of the instance name, so this
	 * is not appropriate for removing specific separators. This is necessary to
	 * help remove actions after a locale switch.
	 * 
	 * @param actionClass
	 *            the class of the action to add to the menu
	 * @param folder
	 *            the folder representing the Menu in which the action should be
	 *            added
	 * @exception IOException
	 *                is throws when there is a problem removing the .instance
	 *                files
	 * @see #createAction(Class, DataFolder)
	 * @see #createAction(Class, DataFolder, String, boolean, boolean, boolean,
	 *      boolean)
	 * @deprecated Use of XML filesystem layers to install actions is preferred.
	 */
	public static void removeAction(Class actionClass, DataFolder folder)
			throws IOException {
		DataObject[] children = folder.getChildren();
		for (int i = 0; i < children.length; i++) {
			if (!(children[i] instanceof InstanceDataObject))
				continue;
			InstanceCookie inst = (InstanceCookie) children[i]
					.getCookie(InstanceCookie.class);
			if (inst == null) {
				// [PENDING] this is an error of some sort
				continue;
			}
			Class instanceClass;
			try {
				instanceClass = inst.instanceClass();
			} catch (Exception e) {
				continue;
			}
			if (instanceClass.getName().equals(actionClass.getName())) {
				children[i].delete();
			}
		}
		String name = actionClass.getName();
		String shorterName = name.substring(name.lastIndexOf(".") + 1,
				name.length());
		InstanceDataObject.remove(folder, "SepBefore" + shorterName,
				"javax.swing.JSeparator"); // NOI18N
		InstanceDataObject.remove(folder, "SepAfter" + shorterName,
				"javax.swing.JSeparator"); // NOI18N
	}

	// -----------------------------------------------------------------------------
	// Windows management utilities

	/**
	 * Moves specified window to the center of the screen
	 * 
	 * @param w
	 *            the window to move
	 */
	public static void centerWindow(Window w) {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension dialogSize = w.getSize();
		w.setLocation((screenSize.width - dialogSize.width) / 2,
				(screenSize.height - dialogSize.height) / 2);
	}
}
