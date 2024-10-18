package com.phyohtet.restaurant.util;

import java.util.function.Supplier;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class BackgroundLoaderService<T> extends Service<T> {

	private final Supplier<T> loader;

	public BackgroundLoaderService(Supplier<T> loader) {
		this.loader = loader;
	}

	@Override
	protected Task<T> createTask() {
		return new DataLoaderTask<>(loader);
	}

	@Override
	protected void running() {
		Navigator.showLoading(true);
	}

	@Override
	protected void succeeded() {
		Navigator.showLoading(false);
	}

	@Override
	protected void failed() {
		Navigator.showLoading(false);
	}


}
