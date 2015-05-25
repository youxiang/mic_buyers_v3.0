package com.focustech.common.http;

import android.content.Context;

public final class HttpConfiguration
{
	final Context context;

	private HttpConfiguration(Builder builder)
	{
		context = builder.context;
	}

	public static class Builder
	{
		private Context context;

		public Builder(Context context)
		{
			this.context = context.getApplicationContext();
		}

		public HttpConfiguration build()
		{
			return new HttpConfiguration(this);
		}
	}
}
