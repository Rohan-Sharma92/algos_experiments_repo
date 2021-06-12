package com.experimentcode.rest_api.paginated.transaction;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class FetchPaginatedDataWithPlainJava {

	public static final String BASE_URL = "https://jsonmock.hackerrank.com/api/transactions/search";

	public static void main(String[] args)
			throws IOException, URISyntaxException, InterruptedException, ParseException {
		// TODO Auto-generated method stub
		FetchPaginatedDataWithPlainJava dataWithoutSpring = new FetchPaginatedDataWithPlainJava();
		long startTime = System.currentTimeMillis();
		System.out.println("Start time:"+startTime);
		List<Integer> results = dataWithoutSpring.fetchUsingPlainJava(TransactionType.debit.name(), 4, "02-2019");
		long endTime = System.currentTimeMillis();
		System.out.println("End time:"+endTime);
		System.out.println("Time taken:"+(endTime-startTime));
		System.out.println("Transactions are: ");
		for (int i : results)
			System.out.print(i + " ");

	}

	public List<Integer> fetchUsingPlainJava(String txnType, int userId, String date) {
		List<JsonElement> transactionCache = new ArrayList<>();
		fetchTxnData(txnType, userId, date, transactionCache);
		if (TransactionType.debit != TransactionType.valueOf(txnType)) {
			List<JsonElement> debitCache = new ArrayList<>();
			fetchTxnData(TransactionType.debit.name(), userId, date, debitCache);
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

	private double getAmountValue(String amount) {
		Number number = null;
		try {
			number = NumberFormat.getCurrencyInstance(Locale.US).parse(amount);
		} catch (ParseException e) {
		}
		return number == null ? 0 : number.doubleValue();
	}

	private void fetchTxnData(String txnType, int userId, String date, List<JsonElement> elements) {
		int startPage = 0;
		int endPage = -1;
		while (startPage == 0 || startPage <= endPage) {
			try {
				HttpURLConnection openConnection = (HttpURLConnection) new URL(
						generateURL(txnType, startPage)).openConnection();
				openConnection.setRequestMethod("GET");
				BufferedReader bufferedReader = new BufferedReader(
						new InputStreamReader(openConnection.getInputStream()));
				String input = null;
				while ((input = bufferedReader.readLine()) != null) {
					JsonObject jsonObject = new Gson().fromJson(input, JsonObject.class);
					if (endPage == -1) {
						endPage = jsonObject.get("total").getAsInt();
					}
					JsonArray transactionData = jsonObject.get("data").getAsJsonArray();
					for (JsonElement d : transactionData) {
						if (isApplicable(d.getAsJsonObject().get("timestamp").getAsLong(), date)
								&& d.getAsJsonObject().get("userId").getAsInt() == userId)
							elements.add(d);
					}
				}
				startPage++;
				bufferedReader.close();
				openConnection.disconnect();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private String generateURL(String txnType, int startPage) {
		return BASE_URL + "?txnType=" + txnType + "&page=" + startPage;
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
