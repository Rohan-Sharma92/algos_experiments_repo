package com.experimentcode.rest_api.paginated.transaction;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;

import com.google.gson.JsonElement;

public class FetchPaginatedDataWithApacheHttpClient {

	public static final String BASE_URL = "https://jsonmock.hackerrank.com/api/transactions/search";

	public static void main(String[] args)
			throws IOException, URISyntaxException, InterruptedException, ParseException {
		// TODO Auto-generated method stub
		long startTime = System.currentTimeMillis();
		System.out.println("Start time:"+startTime);
		FetchPaginatedDataWithApacheHttpClient dataWithoutSpring = new FetchPaginatedDataWithApacheHttpClient();
		List<Integer> results = dataWithoutSpring.fetchResults(TransactionType.debit.name(), 4, "02-2019");
		long endTime = System.currentTimeMillis();
		System.out.println("End time:"+endTime);
		System.out.println("Time taken:"+(endTime-startTime));
		System.out.println("Transactions are: ");
		for(int i:results)
			System.out.print(i+" ");

	}
	
	private double getAmountValue(String amount) {
		Number number = null;
		try {
			number = NumberFormat.getCurrencyInstance(Locale.US).parse(amount);
		} catch (ParseException e) {
		}
		return number == null ? 0 : number.doubleValue();
	}

	public List<Integer> fetchResults(String txnType, int userId, String date)
			throws IOException, URISyntaxException, InterruptedException, ParseException {
		CountDownLatch latch=new CountDownLatch(1);
		List<JsonElement> transactionCache = new ArrayList<>();
		CloseableHttpAsyncClient client = HttpAsyncClients.createMinimal();
		client.start();
		ApacheClientHelper clientHelper=new ApacheClientHelper(client, transactionCache, latch);
		clientHelper.fetchData(txnType, userId, date, 0);
		latch.await();
		if (TransactionType.debit!=TransactionType.valueOf(txnType)) {
			List<JsonElement> debitCache = new ArrayList<>();
			latch = new CountDownLatch(1);
			clientHelper=new ApacheClientHelper(client, debitCache, latch);
			clientHelper.fetchData(TransactionType.debit.name(), userId, date, 0);
			latch.await();
			double average = (debitCache.stream()
					.mapToDouble(d -> getAmountValue(d.getAsJsonObject().get("amount").getAsString())).sum())
					/ debitCache.size();
			return debitCache.stream()
					.filter(t -> getAmountValue(t.getAsJsonObject().get("amount").getAsString()) > average)
					.map(t -> t.getAsJsonObject().get("id").getAsInt()).collect(Collectors.toList());
		}
		double average = (transactionCache.stream()
				.mapToDouble(d -> getAmountValue(d.getAsJsonObject().get("amount").getAsString())).sum())
				/ transactionCache.size();
		return transactionCache.stream()
				.filter(t -> getAmountValue(t.getAsJsonObject().get("amount").getAsString()) > average)
				.map(t -> t.getAsJsonObject().get("id").getAsInt()).collect(Collectors.toList());
	}

}
