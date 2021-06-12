package com.experimentcode.rest_api.paginated.transaction;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

import com.google.gson.Gson;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PaginationHelper {

	private final List<TransactionData> data;
	private final OkHttpClient client;
	private final CountDownLatch latch;

	public PaginationHelper(List<TransactionData> data, OkHttpClient client, CountDownLatch latch) {
		this.data = data;
		this.client = client;
		this.latch = latch;
	}

	public void fetchPage(String txnType, int page, int userId) {
		String url = getURL(txnType, page);

		Request request = new Request.Builder().url(url).build();
		Call call = client.newCall(request);
		Gson gson = new Gson();
		call.enqueue(new Callback() {

			@Override
			public void onResponse(Call arg0, Response response) throws IOException {
				String body = response.body().string();
				Page page = gson.fromJson(body, Page.class);
				List<TransactionData> transactions = page.getData().stream().filter(data -> data.getUserId() == userId)
						.collect(Collectors.toList());
				data.addAll(transactions);
				if (page.getPage() != page.getTotalPages()) {
					new PaginationHelper(data, client, latch).fetchPage(txnType, page.getPage() + 1, userId);
				} else {
					latch.countDown();
				}
			}

			@Override
			public void onFailure(Call arg0, IOException arg1) {

			}
		});
	}

	private String getURL(String txnType, Integer page) {
		HttpUrl.Builder urlBuilder = HttpUrl.parse(FetchPaginatedDataWithOkHttp.BASE_URL).newBuilder();
		urlBuilder.addQueryParameter("txnType", txnType);
		urlBuilder.addQueryParameter("page", String.valueOf(page));
		String url = urlBuilder.build().toString();
		return url;
	}
}
