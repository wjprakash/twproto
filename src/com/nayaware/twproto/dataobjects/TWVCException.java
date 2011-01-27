package com.nayaware.twproto.dataobjects;

import com.sun.teamware.lib.sc.ScCmd;

public class TWVCException extends Exception {

	private ScCmd VCcmd;

	/**
	 * Creates a new TWVCException object from the given ScCmd object.
	 * 
	 * @param cmd
	 *            the object representing Version Control operation.
	 */
	public TWVCException(ScCmd cmd) {
		super(/* NOI18N */"Version Control failure");
		VCcmd = cmd;
	}

	/**
	 * Returns the error message from the Version Control System command.
	 */
	public String getMessage() {
		if (VCcmd.errors != null) {
			return VCcmd.errors;
		} else if (VCcmd.results != null) {
			return VCcmd.results;
		} else {
			return (/* NOI18N */"error code: " + VCcmd.ret_stat);
		}
	}

	/**
	 * Returns the command string which caused the exception.
	 */
	public String getCommand() {
		return (VCcmd.cmd);
	}

	/**
	 * Returns exit status of the Version Control System command.
	 */
	public int getExitValue() {
		return (VCcmd.ret_stat);
	}

}
