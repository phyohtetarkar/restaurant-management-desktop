package com.phyohtet.restaurant.util;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringContextManager {
	
	private static ConfigurableApplicationContext CTX;

	static {
		CTX = new ClassPathXmlApplicationContext("spring-config.xml");
	}
	
	public static ConfigurableApplicationContext getContext() {
		return CTX;
	}
	
	public static void close() {
		CTX.close();
	}
}
