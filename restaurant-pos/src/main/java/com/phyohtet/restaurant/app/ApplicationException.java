package com.phyohtet.restaurant.app;

import java.io.Serializable;

@SuppressWarnings("serial")
public class ApplicationException extends RuntimeException implements Serializable {

	private boolean needToAlert;

	public ApplicationException(String message, Throwable t) {
		super(message, t);
	}

	public ApplicationException(String message) {
		super(message);
		needToAlert = true;
	}

	public ApplicationException(Throwable t) {
		super(t);
	}

	public boolean isNeedToAlert() {
		return needToAlert;
	}
}
