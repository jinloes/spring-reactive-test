package com.jinloes;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

/**
 * Created by jinloes on 1/25/17.
 */
public class LongPollingClient {
	public static void main(String[] args) throws IOException {
		OkHttpClient client = new OkHttpClient();
		Request request = new Request.Builder()
				.url("http://localhost:8080/long_poll")
				.build();
		try (Response response = client.newCall(request).execute()) {
			System.out.println(response.body().string());
		}
	}
}
