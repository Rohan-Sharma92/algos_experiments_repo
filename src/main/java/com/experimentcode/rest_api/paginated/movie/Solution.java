package com.experimentcode.rest_api.paginated.movie;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class Solution {
	/*
	 * Complete the function below.
	 */
	public static final String BASE_URL = "https://jsonmock.hackerrank.com/api/movies/search/";
	private IURLFactory factory;

	public Solution(IURLFactory factory) {
		this.factory = factory;
	}

	public String[] getMovieTitles(String substr) {
		int startPage = 1;
		int endPage = -1;
		List<String> titles = new ArrayList<>();
		while (startPage == 1 || startPage <= endPage) {
			try {
				String url = generateURL(substr, startPage);
				HttpURLConnection c = (HttpURLConnection) factory.create(url).openConnection();
				c.setRequestMethod("GET");
				c.setRequestProperty("accept", "application/json");
				BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream()));
				String line = null;
				while ((line = br.readLine()) != null) {
					JsonObject jsonObject = new Gson().fromJson(line, JsonObject.class);
					if (endPage == -1)
						endPage = jsonObject.get("total_pages").getAsInt();
					JsonArray dataArray = jsonObject.getAsJsonArray("data");
					for (JsonElement e : dataArray) {
						titles.add(e.getAsJsonObject().get("Title").getAsString());
					}
				}
				startPage++;
				br.close();
				c.disconnect();
			} catch (IOException e) {
			}
		}
		Collections.sort(titles, (a, b) -> a.compareTo(b));
		return titles.toArray(new String[] {});

	}

	public String[] getMovieTitlesUsingApacheHttpClient(String substr) {
		CountDownLatch latch=new CountDownLatch(1);
		List<String> titles = new ArrayList<>();
		CloseableHttpAsyncClient client = HttpAsyncClients.createMinimal();
		client.start();
		ApacheClientHelper helper=new ApacheClientHelper(client, titles, latch);
		helper.fetchData(substr, 1);
		try {
			latch.await();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Collections.sort(titles, (a, b) -> a.compareTo(b));
		return titles.toArray(new String[] {});

	}

	private String generateURL(String substr, int startPage) throws UnsupportedEncodingException {
		String query = String.format("Title=%s&page=%s", substr, startPage);
		return BASE_URL + "?" + query;
	}

	public static void main(String[] args) throws IOException {
		String[] res;
		String _substr;
		URLFactory factory = new URLFactory();
		Solution s = new Solution(factory);
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		try {
			_substr = br.readLine();
		} catch (Exception e) {
			_substr = null;
		}

		res = s.getMovieTitlesUsingApacheHttpClient(_substr);
		for (int res_i = 0; res_i < res.length; res_i++) {
			System.out.println(res[res_i]);
		}
	}
}
