package com.nayaware.twproto.dataobjects;

import java.io.*;

public interface TwFile {

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
	public abstract boolean isUnderVC();

	/**
	 * Tests whether the file denoted by this object is a TeamWare workspace
	 * 
	 * @return true if and only if the file denoted by this object is a
	 *         directory, and this directory is a TeamWare workspace (contains
	 *         Codemgr_wsdata directory).
	 */
	public boolean isTWWorkspace();

	/**
	 * Tests whether the file denoted by this object belongs to a TeamWare
	 * workspace
	 * 
	 * @return true if and only if the file denoted by this object belongs to a
	 *         TeamWare workspace.
	 */
	public boolean isUnderTWWorkspace();

	/**
	 * Returns name of the workspace (absolute path) if the file denoted by this
	 * object is under workspace. Returns null otherwise.
	 */
	public String getContainingWorkspaceName();

	/**
	 * Tests whether the file denoted by this object is checked out.
	 * 
	 * @return true if and only if the file denoted by this object is under
	 *         Version Control, and is checked out.
	 * 
	 * @see isUnderVC()
	 */
	public boolean isCheckedOut();

	/**
	 * Tests whether the working copy of the file denoted by this object exists
	 * 
	 * @return true if and only if the file denoted by this object exists.
	 */
	public boolean clearFileExists();

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
	public TwFile[] listTwFiles();

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
	public void deleteVCFile(boolean putInTrash) throws TWVCException;

	/**
	 * Deletes not VC file or directory not inside workspace denoted by this
	 * object.
	 * 
	 * 
	 * @return true if and only if the file or directory is successfully
	 *         deleted.
	 */
	public boolean delete();

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
	public void renameVCFile(String newName) throws TWVCException;

	/**
	 * Returns SCCS file history (output of 'sccs prt' command).
	 * 
	 * @throws TWVCException
	 *             if an error occurs during the version control operation.
	 */
	public String fileHistory() throws TWVCException;

	/**
	 * Puts the file denoted by this object under Version Control.
	 * 
	 * @param initComment
	 *            initial comment for the first revision of this file.
	 * 
	 * @throws TWVCException
	 *             if an error occurs during the version control operation.
	 */
	public boolean putUnderVC(String initComment) throws TWVCException;

	/**
	 * Checks out the latest revision of the file denoted by this object.
	 * 
	 * @throws TWVCException
	 *             if an error occurs during the version control operation.
	 */
	public void checkOut() throws TWVCException;

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
	public void checkOut(String SID, String newName) throws TWVCException;

	/**
	 * Checks in the file denoted by this object.
	 * 
	 * @param comment
	 *            comment to the revision to be checked in.
	 * 
	 * @throws TWVCException
	 *             if an error occurs during the version control operation.
	 */
	public boolean checkIn(String comment) throws TWVCException;

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
			throws TWVCException;

	/**
	 * Unchecks out the file denoted by this object.
	 * 
	 * @throws TWVCException
	 *             if an error occurs during the version control operation.
	 */
	public void unCheckOut() throws TWVCException;

	/**
	 * Unchecks out the file denoted by this object.
	 * 
	 * @param SID
	 *            the string representing the file revision to be unchecked out.
	 * 
	 * @throws TWVCException
	 *             if an error occurs during the version control operation.
	 */
	public void unCheckOut(String SID) throws TWVCException;

	/**
	 * Creates working copy the file denoted by this object.
	 * 
	 * @throws TWVCException
	 *             if an error occurs during the version control operation.
	 */
	public void getClearFile() throws TWVCException;

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
	public void getClearFile(String SID, String options) throws TWVCException;

	public boolean isInConflict();

	public String getAbsolutePath();

	public String getPath();

	public String getName();

	public void create() throws IOException;

	public String getVersionString();

	public String getLastUserString();

	public String getCommentString();

	public boolean checkedOutDifferent();
}
