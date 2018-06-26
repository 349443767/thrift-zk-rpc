package com.fb.zk.config;


import com.fb.zk.common.IErrDefine;
import com.fb.zk.manager.ZkManager;
import com.fb.zk.util.timer.instance.ZKServerTargetReloader;

public class ZKStaticConfigLoader {

	public static String mTmp_Cfg_Path = null;

	private ZKServerTargetReloader mTarServerLoader;

	public ZKStaticConfigLoader() {
		mTarServerLoader = new ZKServerTargetReloader();
	}

	public int loadAll() {
		int result = loadZKTargetServerList();
		if (result != IErrDefine.ERR_NONE) {
			return result;
		}
		return IErrDefine.ERR_NONE;
	}


	private int loadZKTargetServerList() {
		ZkManager.getInstance().getTimeTaskManager()
				.addTask(mTarServerLoader, ZKServerTargetReloader.TIME_DELAY, ZKServerTargetReloader.FREQ);
		return mTarServerLoader.reloadConfigAsync();
	}

}
