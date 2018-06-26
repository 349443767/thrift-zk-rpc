package com.fb.zk.config;


import com.fb.zk.config.entity.ZKServerTargetEntity;

public class ZKStaticConfigHolder {

	private static ZKServerTargetEntity sZKServerTargetEntity = null;


	public static void setZKServerTargetEntity(final ZKServerTargetEntity entity) {
		sZKServerTargetEntity = entity;
	}

	public static ZKServerTargetEntity getZKServerTargetEntity() {
		return sZKServerTargetEntity;
	}

}
