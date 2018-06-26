package com.fb.zk.util.timer;

import com.fb.zk.common.ITimerTaskControl;

import java.util.Hashtable;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


public class TimeTaskManager {

	private Hashtable<String, ITimerTaskControl> mTaskHolder;

	private ScheduledThreadPoolExecutor mTimer = null;

	public TimeTaskManager() {
		mTaskHolder = new Hashtable<String, ITimerTaskControl>();
	}


	public void initialize() {
		if (mTimer == null) {
			mTimer = new ScheduledThreadPoolExecutor(5);
		}

	}

	public void addTask(ITimerTaskControl task, int startTime, int freq) {
		if (!mTaskHolder.containsKey(task.getTaskName())) {
			mTimer.scheduleAtFixedRate((Runnable) task, startTime, freq, TimeUnit.MICROSECONDS);
			mTaskHolder.put(task.getTaskName(), task);
		}

	}

	public void removeTask(String taskName) {
		if (mTaskHolder.containsKey(taskName)) {
			mTimer.remove((Runnable) (mTaskHolder.get(taskName)));
			mTaskHolder.remove(taskName);
		}

	}

	public ITimerTaskControl taskLookUp(String taskName) {
		if (mTaskHolder != null) {
			return mTaskHolder.get(taskName);
		}
		return null;

	}

}
