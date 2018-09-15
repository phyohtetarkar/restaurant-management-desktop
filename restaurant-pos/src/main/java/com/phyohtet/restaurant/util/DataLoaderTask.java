package com.phyohtet.restaurant.util;

import java.util.function.Supplier;

import javafx.concurrent.Task;

public class DataLoaderTask<T> extends Task<T> {

	private Supplier<T> loader;

	public DataLoaderTask(Supplier<T> loader) {
		this.loader = loader;
	}

	@Override
	protected T call() throws Exception {
		return loader.get();
	}
}
