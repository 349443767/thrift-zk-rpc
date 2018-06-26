package com.fb.zk.util.timer.instance;

import com.fb.zk.common.IErrDefine;
import com.fb.zk.common.ITimerTaskControl;
import com.fb.zk.common.IZKCfgDefine;
import com.fb.zk.config.ZKStaticConfigHolder;
import com.fb.zk.config.ZKStaticConfigLoader;
import com.fb.zk.config.entity.ZKServerTargetEntity;
import com.fb.zk.util.xml.XMLParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.File;
import java.util.Hashtable;

public class ZKServerTargetReloader implements Runnable, ITimerTaskControl {

	private final static Logger logger = LoggerFactory.getLogger(ZKServerTargetReloader.class);

	public static final int FREQ = 5 * 60 * 1000;

	public static final int TIME_DELAY = FREQ;


	private static final String TAG = ZKServerTargetReloader.class.getSimpleName();

	private static final int SEED = Integer.MAX_VALUE;

	private File mServerTarget = null;

	private static String sCfg_Path = null;

	private String mCurTaskName;

	public ZKServerTargetReloader() {
		mCurTaskName = TAG + (int) (Math.random() * SEED);

		if (ZKStaticConfigLoader.mTmp_Cfg_Path != null) {
			sCfg_Path = ZKStaticConfigLoader.mTmp_Cfg_Path + IZKCfgDefine.SERVER_TARGET_LIST;
		} else {
			sCfg_Path = IZKCfgDefine.DEFAULT_CFG_PATH + IZKCfgDefine.SERVER_TARGET_LIST;
		}

	}

	@Override
	public String getTaskName() {
		return this.mCurTaskName;
	}

	public int reloadConfigAsync() {

		try {
			File tmpTargetFile = new File(sCfg_Path);
			if (mServerTarget != null) {
				//return if two files have equal size
				if (tmpTargetFile.length() == mServerTarget.length()) {
					return IErrDefine.ERR_NONE;
				}
				reloadConfig();
			} else {
				reloadConfig();
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return IErrDefine.ERR_TARGET_SERVER_READING;
		}


		return IErrDefine.ERR_NONE;

	}

	@Override
	public void run() {
		reloadConfig();

	}

	private int reloadConfig() {

		try {
			Hashtable<String, String> tmpTable = new Hashtable<String, String>();
			mServerTarget = new File(sCfg_Path);
			Element root = XMLParser.getInstance().getRoot(mServerTarget);
			NodeList list = root.getElementsByTagName("zk_server");
			for (int i = 0; i < list.getLength(); i++) {
				Element n = (Element) list.item(i);
				String ip = n.getElementsByTagName("ip").item(0).getFirstChild().getNodeValue();
				String port = n.getElementsByTagName("port").item(0).getFirstChild().getNodeValue();
				tmpTable.put(ip, port);

			}
			if (ZKStaticConfigHolder.getZKServerTargetEntity() == null) {
				ZKStaticConfigHolder.setZKServerTargetEntity(new ZKServerTargetEntity(tmpTable));
			} else {
				ZKStaticConfigHolder.getZKServerTargetEntity().setTargetList(tmpTable);
			}


		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return IErrDefine.ERR_TARGET_SERVER_READING;
		}

		return IErrDefine.ERR_NONE;

	}

}
