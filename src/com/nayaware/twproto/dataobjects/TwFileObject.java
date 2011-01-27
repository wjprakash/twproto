package com.nayaware.twproto.dataobjects;

import java.io.*;
import java.math.*;
import java.util.Vector;

import com.sun.teamware.lib.ccs.SccsSfile;
import com.sun.teamware.lib.cmm.CmmWorkspace;
import com.sun.teamware.lib.sc.ScCmd;
import com.sun.teamware.lib.sc.ScSfile;
import com.sun.teamware.lib.sc.ScConflict;
import com.sun.teamware.lib.common.TwUtil;
import com.sun.teamware.lib.ccs.SccsDelta;
import com.sun.teamware.lib.ccs.SccsSfile;
import com.sun.teamware.lib.sc.ScCmd;
import com.sun.teamware.lib.sc.ScDelta;

public class TwFileObject extends java.io.File implements TwFile {

	private SccsSfile sfile;
	private CmmWorkspace workspace;
	private String fullname;
	private Vector filelist;

	/**
	 * Creates a new TwFile instance from the given File.
	 */
	public TwFileObject(File file) {
		super(file.getAbsolutePath());
	}

	/**
	 * Creates a new TwFile instance from the given filename string.
	 */
	public TwFileObject(String filename) {
		super(filename);
	}

	/**
	 * Tests whether the file denoted by this object is under Version Control.
	 * 
	 * @return true if and only if the file denoted by this object is not a
	 *         directory, and is under version control. The existance of this
	 *         file is not requiered.
	 * 
	 * @see clearFileExists()
	 * @see java.io.File.exists()
	 */
	public boolean isUnderVC() {
		fullname = getAbsolutePath();
		sfile = new SccsSfile(fullname, ScSfile.SFILE_NOREAD);
		return sfile.under_sc;
	}

	/**
	 * Tests whether the file denoted by this object is a TeamWare workspace
	 * 
	 * @return true if and only if the file denoted by this object is a
	 *         directory, and this directory is a TeamWare workspace (contains
	 *         Codemgr_wsdata directory).
	 */
	public boolean isTWWorkspace() {
		if (!isDirectory()) {
			return false;
		}
		fullname = getAbsolutePath();
		workspace = new CmmWorkspace(fullname);
		try {
			return workspace.exists();
		} catch (Exception e) {
		}
		return false;
	}

	/**
	 * Tests whether the file denoted by this object belongs to a TeamWare
	 * workspace
	 * 
	 * @return true if and only if the file denoted by this object belongs to a
	 *         TeamWare workspace.
	 */
	public boolean isUnderTWWorkspace() {
		fullname = getAbsolutePath();
		workspace = CmmWorkspace.findContainingWorkspace(fullname);
		return (workspace != null);
	}

	/**
	 * Returns name of the workspace (absolute path) if the file denoted by this
	 * object is under workspace. Returns null otherwise.
	 */
	public String getContainingWorkspaceName() {
		fullname = getAbsolutePath();
		workspace = CmmWorkspace.findContainingWorkspace(fullname);
		if (workspace == null) {
			return (null);
		} else {
			return (workspace.getName());
		}
	}

	/**
	 * Tests whether the file denoted by this object is checked out.
	 * 
	 * @return true if and only if the file denoted by this object is under
	 *         Version Control, and is checked out.
	 * 
	 * @see isUnderVC()
	 */
	public boolean isCheckedOut() {
		formSfile();
		return (sfile.checkedOut);
	}

	public boolean moveTo(String toDir) {
		ScCmd cmd = sfile.move(toDir);
		if (cmd.ret_stat != 0)
			return false;
		String stdout = cmd.results;
		if ((stdout == null) || (stdout.length() == 0)) {
			stdout = cmd.errors;
		}
		if ((stdout == null) || (stdout.length() == 0)) {
			return (false);
		}
		return true;
	}

	/* Check if the file is in conflict */

	public boolean isInConflict() {
		ScConflict conflictInfo = null;
		boolean inConflict;
		try {
			conflictInfo = new ScConflict(sfile);
			conflictInfo.get();
			if (conflictInfo.childBranch == null) {
				// According to the s-file, the file is not in conflict.
				inConflict = false;
			} else {
				inConflict = true;
			}
		} catch (Exception ex) {
			inConflict = false;
		}
		return inConflict;
	}

	/**
	 * Tests whether the working copy of the file denoted by this object exists
	 * 
	 * @return true if and only if the file denoted by this object exists.
	 */
	public boolean clearFileExists() {
		return (exists());
	}

	/**
	 * Returns an array of TwFile objects denoting the files in this directory.
	 * 
	 * If this object does not denote a directory, then this method returns
	 * null. Otherwise an array of TwFile objects is returned, one for each file
	 * or directory in the directory. The resulting array will not contain
	 * version control specific directory SCCS, as well as TeamWare specific
	 * directory Codemgr_wsdata. On the other hand, it may contain objects
	 * denoting notexistent files (i.e. version control file exists, but working
	 * copy does not exist for this file). There is no guarantee that the name
	 * strings in the resulting array will appear in any specific order; they
	 * are not, in particular, guaranteed to appear in alphabetical order.
	 * 
	 * @return An array of TwFile objects denoting the files and directories in
	 *         the directory denoted by this abstract pathname. The array will
	 *         be empty if the directory is empty. Returns null if this object
	 *         does not denote a directory, or if an I/O error occurs.
	 */
	public TwFile[] listTwFiles() {
		if (!isDirectory()) {
			return null;
		}
		File dirp;
		File[] entry;
		int i;
		String SCCSname;
		String gfile;
		String nm;
		fullname = getAbsolutePath();
		String name;
		TwFile toAdd;

		filelist = new Vector();

		try {
			name = fullname + File.separator + /* NOI18N */"SCCS";
			dirp = new File(name);
			entry = dirp.listFiles();
			if (dirp != null && entry != null) {
				for (i = 0; i < entry.length; i++) {
					nm = entry[i].getName();
					SCCSname = name + File.separator + nm;
					if (materializeGfile(SCCSname)) {
						gfile = getClearName(SCCSname);
						toAdd = new TwFileObject(gfile);
						filelist.add(toAdd);
					}
				}
			}
			dirp = this;
			entry = dirp.listFiles();
			if (dirp != null && entry != null) {
				for (i = 0; i < entry.length; i++) {
					nm = entry[i].getName();
					if (nm.equals(/* NOI18N */"SCCS")) {
						continue;
					}
					if (nm.equals(/* NOI18N */"Codemgr_wsdata")) {
						continue;
					}
					name = fullname + File.separator + nm;
					if (!findSfile(name)) {
						toAdd = new TwFileObject(name);
						filelist.add(toAdd);
					}
				}
			}
		} catch (Exception e) {
		}
		int count = filelist.size();
		TwFile[] res = new TwFile[count];
		for (int j = 0; j < count; j++) {
			res[j] = (TwFile) filelist.elementAt(j);
		}
		return (res);
	}

	/**
	 * Deletes VC file system entry denoted by this object. Object should
	 * represent one of the following: - VC file inside workspace - VC file not
	 * inside workspace - directory inside workspace
	 * 
	 * For others use @see delete()
	 * 
	 * @param putInTrash
	 *            if putInTrash is true it moves object to deleted_files
	 *            directory. if putInTrash is false it removes object. For
	 *            object inside workspace putInTrash doesn't work and method
	 *            simply deletes object.
	 * 
	 * @throws TWVCException
	 *             if an error occurs during the version control operation.
	 */
	public void deleteVCFile(boolean putInTrash) throws TWVCException {
		ScCmd cmd;
		fullname = getAbsolutePath();
		if (isDirectory()) {
			if (putInTrash) {
				String cmdstr = TwUtil.findYourself( /* NOI18N */"workspace",
						this);
				String tmpstr = TwUtil.preparePathForShell(fullname, false);

				cmd = new ScCmd(cmdstr, /* NOI18N */"filerm", tmpstr);
				cmd.execute(dirname(fullname));
			} else {
				cmd = new ScCmd(/* NOI18N */"java.io.File.delete()", null);
				try {
					TwUtil.removeDir(fullname, true);
				} catch (Exception e) {
					cmd.errors = e.getMessage();
					cmd.ret_stat = 1;
				}
			}
		} else {
			sfile = new SccsSfile(fullname, ScSfile.SFILE_NOREAD);
			cmd = sfile.delete();
		}
		if (cmd.ret_stat != 0) {
			throw new TWVCException(cmd);
		}
	}

	/**
	 * Deletes not VC file or directory not inside workspace denoted by this
	 * object.
	 * 
	 * 
	 * @return true if and only if the file or directory is successfully
	 *         deleted.
	 */
	public boolean delete() {
		fullname = getAbsolutePath();
		try {
			if (isDirectory()) {
				TwUtil.removeDir(fullname, true);
			} else {
				return super.delete();
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Renames VC file system entry denoted by this object. Object should
	 * represent one of the following: - VC file inside workspace - VC file not
	 * inside workspace - directory inside workspace
	 * 
	 * For others use @see java.io.File.renameTo()
	 * 
	 * @throws TWVCException
	 *             if an error occurs during the version control operation.
	 */
	public void renameVCFile(String newName) throws TWVCException {
		ScCmd cmd;
		fullname = getAbsolutePath();
		if (isDirectory()) {
			String cmdstr = TwUtil.findYourself( /* NOI18N */"workspace", this);
			String tmpstr = TwUtil.preparePathForShell(fullname, false) + " ";
			tmpstr += TwUtil.preparePathForShell(newName, false);

			cmd = new ScCmd(cmdstr, /* NOI18N */"filemv", tmpstr);
			cmd.execute(dirname(fullname));
		} else {
			sfile = new SccsSfile(fullname, ScSfile.SFILE_NOREAD);
			cmd = sfile.move(newName);
		}
		if (cmd.ret_stat != 0) {
			throw new TWVCException(cmd);
		}
	}

	/**
	 * Returns SCCS file history (output of 'sccs prt' command).
	 * 
	 * @throws TWVCException
	 *             if an error occurs during the version control operation.
	 */
	public String fileHistory() throws TWVCException {
		formSfile();
		ScCmd cmd = sfile.prt();
		if (cmd.ret_stat != 0) {
			throw new TWVCException(cmd);
		}
		return (cmd.results);
	}

	/**
	 * Puts the file denoted by this object under Version Control.
	 * 
	 * @param initComment
	 *            initial comment for the first revision of this file.
	 * 
	 * @throws TWVCException
	 *             if an error occurs during the version control operation.
	 */
	public boolean putUnderVC(String initComment) throws TWVCException {
		formSfile();
		try {
			if (!sfile.checkForNewline(true)) {
				return false;
			}
		} catch (Exception ex) {
			return false;
		}
		ScCmd cmd = sfile.putUnderSc(initComment);
		if (cmd.ret_stat != 0) {
			throw new TWVCException(cmd);
		}
		return true;
	}

	/**
	 * Checks out the latest revision of the file denoted by this object.
	 * 
	 * @throws TWVCException
	 *             if an error occurs during the version control operation.
	 */
	public void checkOut() throws TWVCException {
		checkOut(null, null);
	}

	/**
	 * Checks out the file denoted by this object.
	 * 
	 * @param SID
	 *            the string representing the file revision to be checked out
	 * @param newName
	 *            the name of the file where the writable working copy will be
	 *            created.
	 * 
	 * @throws TWVCException
	 *             if an error occurs during the version control operation.
	 */
	public void checkOut(String SID, String newName) throws TWVCException {
		formSfile();
		ScCmd cmd = sfile.checkOut(SID, newName);
		if (cmd.ret_stat != 0) {
			throw new TWVCException(cmd);
		}
	}

	/**
	 * Checks in the file denoted by this object.
	 * 
	 * @param comment
	 *            comment to the revision to be checked in.
	 * 
	 * @throws TWVCException
	 *             if an error occurs during the version control operation.
	 */
	public boolean checkIn(String comment) throws TWVCException {
		return checkIn(null, comment, null);
	}

	/**
	 * Checks in the file denoted by this object.
	 * 
	 * @param SID
	 *            the string representing the file revision to be checked in.
	 * @param comment
	 *            comment to the revision to be checked in.
	 * @param options
	 *            any options to be passed to the version control command.
	 * 
	 * @throws TWVCException
	 *             if an error occurs during the version control operation.
	 */
	public boolean checkIn(String SID, String comment, String options)
			throws TWVCException {
		formSfile();
		try {
			if (!sfile.checkForNewline(false)) {
				return false;
			}
		} catch (Exception ex) {
			return false;
		}
		ScCmd cmd = sfile.checkIn(SID, comment, options);
		if (cmd.ret_stat != 0) {
			throw new TWVCException(cmd);
		}
		return true;
	}

	/**
	 * Unchecks out the file denoted by this object.
	 * 
	 * @throws TWVCException
	 *             if an error occurs during the version control operation.
	 */
	public void unCheckOut() throws TWVCException {
		unCheckOut(null);
	}

	/**
	 * Unchecks out the file denoted by this object.
	 * 
	 * @param SID
	 *            the string representing the file revision to be unchecked out.
	 * 
	 * @throws TWVCException
	 *             if an error occurs during the version control operation.
	 */
	public void unCheckOut(String SID) throws TWVCException {
		formSfile();
		ScCmd cmd = sfile.uncheckout(SID);
		if (cmd.ret_stat != 0) {
			throw new TWVCException(cmd);
		}
	}

	/**
	 * Creates working copy the file denoted by this object.
	 * 
	 * @throws TWVCException
	 *             if an error occurs during the version control operation.
	 */
	public void getClearFile() throws TWVCException {
		getClearFile(null, null);
	}

	/**
	 * Creates working copy the file denoted by this object.
	 * 
	 * @param SID
	 *            the string representing the file revision to get.
	 * @param options
	 *            any options to be passed to the version control command.
	 * 
	 * @throws TWVCException
	 *             if an error occurs during the version control operation.
	 */
	public void getClearFile(String SID, String options) throws TWVCException {
		formSfile();
		ScCmd cmd = sfile.getClearFile(SID, options);
		if (cmd.ret_stat != 0) {
			throw new TWVCException(cmd);
		}
	}

	/** create a new physical object if doesn't exists */
	public void create() throws IOException {
		if (!exists())
			createNewFile();
	}

	private void formSfile() {
		fullname = getAbsolutePath();
		sfile = new SccsSfile(fullname, ScSfile.SFILE_NOREAD);
	}

	private boolean materializeGfile(String filename) {

		String gfile;
		String base;
		gfile = getClearName(filename);
		base = getBaseName(filename);
		if (!findSfile(gfile)) {
			if (base.charAt(0) == 's' && base.charAt(1) == '.') {
				return true;
			}
		}
		return false;
	}

	private String getClearName(String s) {
		String res = s;
		int bbb = s.indexOf( /* NOI18N */"SCCS" + File.separator
				+ /* NOI18N */"s.");
		if (bbb >= 0) {
			res = s.substring(0, bbb) + s.substring(bbb + 7);
		}
		return (res);
	}

	private String getBaseName(String s) {
		String res = s;
		int bbb = s.lastIndexOf(File.separator);
		if (bbb >= 0) {
			res = s.substring(bbb + 1);
		}
		return (res);
	}

	private boolean findSfile(String my_name) {
		File f;
		TwFile data;

		f = new File(my_name);
		for (int i = 0; i < filelist.size(); i++) {
			data = (TwFile) filelist.elementAt(i);
			if (data.equals(f)) {
				return true;
			}
		}
		return false;

	}

	private static String dirname(String string) {
		int slash;
		String dir = null;

		slash = string.lastIndexOf(File.separator);
		if (slash >= 0) {
			dir = string.substring(0, slash);
		} else {
			dir = new String(".");
		}
		return dir;
	}

	public String getVersionString() {
		if (isUnderVC()) {
			SccsDelta d = (SccsDelta) getLastDelta();
			if (d == null) {
				return ("?");
			} else {
				return d.sid;
			}
		} else {
			return "new";
		}
	}

	public String getLastUserString() {
		if (isUnderVC()) {
			SccsDelta d = (SccsDelta) getLastDelta();
			if (d == null) {
				return ("?");
			} else {
				return d.user;
			}
		} else {
			return "";
		}
	}

	public String getCommentString() {
		if (isUnderVC()) {
			SccsDelta d = (SccsDelta) getLastDelta();
			if (d == null) {
				return ("?");
			} else {
				if (d.comment != null) {
					if (d.comment[0] != null) {
						return d.comment[0];
					} else {
						return "";
					}
				}
				return "";
			}
		} else {
			return "";
		}
	}

	private ScDelta getLastDelta() {
		return sfile.getLastDelta();
	}

	public boolean checkedOutDifferent() {
		String user;
		boolean res = true;
		ScCmd cmd;

		BigInteger flg = BigInteger.valueOf(0);
		flg = flg.setBit(SccsSfile.SUPPRESS_EXP_KW);
		cmd = sfile.checkDifferent(flg);

		if (cmd.ret_stat != 0) {
			return (true);
		}
		String stdout = cmd.results;
		if ((stdout == null) || (stdout.length() == 0)) {
			stdout = cmd.errors;
		}
		if ((stdout == null) || (stdout.length() == 0)) {
			return (false);
		}
		return true;
	}

}
