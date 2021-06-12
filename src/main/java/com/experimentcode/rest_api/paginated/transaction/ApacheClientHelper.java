package com.experimentcode.rest_api.paginated.transaction;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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

	private List<JsonElement> data;
	private CloseableHttpAsyncClient client;
	private CountDownLatch latch;

	public ApacheClientHelper(CloseableHttpAsyncClient client, List<JsonElement> data,CountDownLatch latch) {
		this.client = client;
		this.data = data;
		this.latch = latch;
	}
	
	public void fetchData(String txnType,int userId,String date,int page) {
		URI uri=null;
		try {
			uri = generateURL(txnType, page);
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
					JsonObject jsonObject = new Gson().fromJson(response, JsonObject.class);
					int curPage=jsonObject.get("page").getAsInt();
					int lastPage=jsonObject.get("total_pages").getAsInt();
					JsonArray transactionData = jsonObject.get("data").getAsJsonArray();
					for (JsonElement d : transactionData) {
						if (isApplicable(d.getAsJsonObject().get("timestamp").getAsLong(), date)
								&& d.getAsJsonObject().get("userId").getAsInt() == userId)
							data.add(d);
					}
				if(curPage<lastPage) {
					new ApacheClientHelper(client, data, latch).fetchData(txnType,userId, date,curPage+1);	
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
		URI uri = new URIBuilder(FetchPaginatedDataWithApacheHttpClient.BASE_URL).addParameter("txnType", substr)
				.addParameter("page", String.valueOf(page)).build();
		return uri;
	}
	
	private boolean isApplicable(Long timestamp, String date) {
		Date transactionDate = new Date(timestamp);
		try {
			Date otherDate = new SimpleDateFormat("MM-yyyy").parse(date);
			return transactionDate.getMonth() == otherDate.getMonth()
					&& transactionDate.getYear() == otherDate.getYear();
		} catch (ParseException e) {
			return false;
		}
	}
}
