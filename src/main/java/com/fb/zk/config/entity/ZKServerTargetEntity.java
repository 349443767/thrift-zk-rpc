package com.fb.zk.config.entity;

import java.util.Hashtable;

public class ZKServerTargetEntity {


	private Hashtable<String, String> mTargetFullSet;

	public ZKServerTargetEntity(Hashtable<String, String> table) {
		this.mTargetFullSet = table;
	}

	public void setTargetList(Hashtable<String, String> target) {
		mTargetFullSet = target;
	}

	public Hashtable<String, String> getTargetList() {
		return mTargetFullSet;
	}


}
