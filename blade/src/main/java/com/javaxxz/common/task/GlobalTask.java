package com.javaxxz.common.task;

import com.javaxxz.core.toolbox.kit.DateKit;

public class GlobalTask implements Runnable {

	@Override
	public void run() {
		
		System.out.println("任务调度执行:" + DateKit.getTime());
	}

}
