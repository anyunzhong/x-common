package net.datafans.common.exception;

public class CacheKeyNotFoundException extends Exception {
	private static final long serialVersionUID = 1L;

	public CacheKeyNotFoundException(String key) {
		super(key + ": not found");
	}

}
