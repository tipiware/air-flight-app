package com.ecommerce.server.utility;

import org.slf4j.Logger;

import org.slf4j.LoggerFactory;

public class GlobalResources {
	
	public static Logger getLogger(Class className) {
		return LoggerFactory.getLogger(className);
	}
}
