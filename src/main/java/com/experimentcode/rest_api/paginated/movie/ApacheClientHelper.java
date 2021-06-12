package com.experimentcode.rest_api.paginated.movie;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class ApacheClientHelper {

	private List<String> data;
	private CloseableHttpAsyncClient client;
	private CountDownLatch latch;

	public ApacheClientHelper(CloseableHttpAsyncClient client, List<String> data,CountDownLatch latch) {
		this.client = client;
		this.data = data;
		this.latch = latch;
	}
	
	public void fetchData(String substr,int page) {
		URI uri=null;
		try {
			uri = generateURL(substr, page);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		HttpGet request=new HttpGet(uri);
		client.execute(request, new FutureCallback<HttpResponse>() {
			
			@Override
			public void failed(Exception ex) {
				latch.countDown();
			}
			
			@Override
			public void completed(HttpResponse result) {
				BasicResponseHandler handler = new BasicResponseHandler();
				String response;
				try {
					response = handler.handleResponse(result);
				JsonObject object = new Gson().fromJson(response,JsonObject.class);
				int curPage=object.get("page").getAsInt();
				int lastPage=object.get("total_pages").getAsInt();
				JsonArray jsonArray = object.get("data").getAsJsonArray();
				for(JsonElement e: jsonArray) {
					data.add(e.getAsJsonObject().get("Title").getAsString());
				}
				if(curPage<lastPage) {
					new ApacheClientHelper(client, data, latch).fetchData(substr, curPage+1);	
				}else {
					latch.countDown();
				}
				
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			@Override
			public void cancelled() {
				latch.countDown();
			}
		});
	}
	
	private URI generateURL(String substr, Integer page) throws URISyntaxException {
		URI uri = new URIBuilder(Solution.BASE_URL).addParameter("Title", substr)
				.addParameter("page", String.valueOf(page)).build();
		return uri;
	}
}
