package com.fb.thrift.pool;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

public class PoolConfig extends GenericObjectPoolConfig {


	private static PoolConfig sDefaultPoolConfig = null;
	private static int sDefMaxIdle = 200;
	private static int sDefTotal = 200;
	private static int sDefWaitMillis = 1000 * 60;
	private static int sDefMinEvictalbeIdle = 1000 * 60 * 2;
	private static boolean sDefTestOnBorrow = false;
	private static boolean sDefTestOnCreate = false;
	private static boolean sDefTestOnReturn = false;
	private static int sDefTimeBetweenEviction = 1000 * 60;
	private static boolean sDefBlockWhenExh = false;
	private static boolean sDefLifo = true;


	/**
	 * Current PoolConfig should be static within same process
	 * all Target SRV connection should maintained by ObjectPool with same config
	 * @return
	 */
	public static PoolConfig getDefaultPoolConfig() {
		if (sDefaultPoolConfig == null) {
			sDefaultPoolConfig = new PoolConfig();
		}
		sDefaultPoolConfig.setMaxIdle(sDefMaxIdle);
		sDefaultPoolConfig.setMaxTotal(sDefTotal);
		sDefaultPoolConfig.setMaxWaitMillis(sDefWaitMillis);
		sDefaultPoolConfig.setMinEvictableIdleTimeMillis(sDefMinEvictalbeIdle);
		sDefaultPoolConfig.setTestOnBorrow(sDefTestOnBorrow);
		sDefaultPoolConfig.setTestOnReturn(sDefTestOnCreate);
		sDefaultPoolConfig.setTestOnCreate(sDefTestOnReturn);
		sDefaultPoolConfig.setTimeBetweenEvictionRunsMillis(sDefTimeBetweenEviction);
		sDefaultPoolConfig.setBlockWhenExhausted(sDefBlockWhenExh);
		sDefaultPoolConfig.setLifo(sDefLifo);
		return sDefaultPoolConfig;

	}

	public static void initMaxIdle(int count) {
		sDefMaxIdle = count;
	}

	public static void initMaxTotal(int count) {
		sDefTotal = count;
	}

	public static void initWaitMillis(int timeOnMillies) {
		sDefWaitMillis = timeOnMillies;
	}

	public static void initMinEvictalbeIdle(int timeOnMillies) {
		sDefMinEvictalbeIdle = timeOnMillies;
	}

	public static void initTestOnBorrow(boolean flag) {
		sDefTestOnBorrow = flag;
	}

	public static void initTestOnCreate(boolean flag) {
		sDefTestOnCreate = flag;
	}

	public static void initTestOnReturn(boolean flag) {
		sDefTestOnReturn = flag;
	}

	public static void initTimeBetweenEviction(int count) {
		sDefTimeBetweenEviction = count;
	}

	public static void initBlockWhenExh(boolean flag) {
		sDefBlockWhenExh = flag;
	}

	public static void initLifo(boolean flag) {
		sDefLifo = flag;
	}


}
