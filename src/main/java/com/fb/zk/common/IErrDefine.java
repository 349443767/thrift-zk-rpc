package com.fb.zk.common;

public interface IErrDefine {
	/**
	 * ok
	 */
	int ERR_NONE = 0;

	/**
	 * could not read target server list
	 */
	int ERR_TARGET_SERVER_READING = -100;

	int ERR_TARGET_SERVER_CONNECT = -200;

	int ERR_TARGET_SERVER_EMPTY = -201;

	int ERR_TARGET_SERVER_CONNECT_FAIL_ALL = -202;

	int ERR_ZK_CLIENT_STATE_NOT_STARTED = -301;

	int ERR_SERVER_DETAIL_PARAM = -401;

	int ERR_SERVICE_ALREADY_EXISTS = -402;

	int ERR_SERVICE_INJECTED_FAILED = -403;

	int ERR_SERVICE_DISC_SRV_NAME_EMPTY = -501;

	int ERR_SERVICE_DISC_SRV_INIT = -502;

	int ERR_SERVICE_REMOCE_FAILED = -601;


}
