package com.rahul.learn.util;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class AppConfig implements ApplicationContextAware {
	
	@Autowired
	static ApplicationContext appCtx;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		appCtx = applicationContext;
	}

	public static ApplicationContext getAppCtx() {
		return appCtx;
	}

	public static void setAppCtx(ApplicationContext appCtx) {
		AppConfig.appCtx = appCtx;
	}
	
}
