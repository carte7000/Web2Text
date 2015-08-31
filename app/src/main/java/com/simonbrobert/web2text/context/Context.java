package com.simonbrobert.web2text.context;

import android.app.Activity;
import android.content.Intent;

public abstract class Context {

	public void apply(android.content.Context context) {
		registerServices(context);
		injectData();
	}

	protected abstract void registerServices(android.content.Context context);

	protected abstract void injectData();

}
