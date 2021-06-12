package com.experimentcode.rest_api.paginated.transaction;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

import okhttp3.OkHttpClient;

public class FetchPaginatedDataWithOkHttp {

	public static final String BASE_URL = "https://jsonmock.hackerrank.com/api/transactions/search";

	public static void main(String[] args)
			throws IOException, URISyntaxException, InterruptedException, ParseException {
		// TODO Auto-generated method stub
		long startTime = System.currentTimeMillis();
		System.out.println("Start time:"+startTime);
		FetchPaginatedDataWithOkHttp dataWithoutSpring = new FetchPaginatedDataWithOkHttp();
		List<Integer> results = dataWithoutSpring.fetchResults(TransactionType.debit.name(), 4, "02-2019");
		long endTime = System.currentTimeMillis();
		System.out.println("End time:"+endTime);
		System.out.println("Time taken:"+(endTime-startTime));
		System.out.println("Transactions are: ");
		for(int i:results)
			System.out.print(i+" ");

	}

	public List<Integer> fetchResults(String txnType, int userId, String date)
			throws IOException, URISyntaxException, InterruptedException, ParseException {
		OkHttpClient client = new OkHttpClient().newBuilder().build();
		List<TransactionData> data = new ArrayList<>();
		CountDownLatch latch = new CountDownLatch(1);
		PaginationHelper paginationHelper = new PaginationHelper(data, client, latch);
		paginationHelper.fetchPage(txnType, 0, userId);
		latch.await();
		List<TransactionData> monthlyTransactions = data.stream().filter(d -> isApplicable(d, date))
				.collect(Collectors.toList());
		if (TransactionType.debit!=TransactionType.valueOf(txnType)) {
			List<TransactionData> averageData = new ArrayList<>();
			latch = new CountDownLatch(1);
			PaginationHelper paginationHelperForAverage = new PaginationHelper(averageData, client, latch);
			paginationHelperForAverage.fetchPage(TransactionType.debit.name(), 0, userId);
			latch.await();
			List<TransactionData> monthlyDebitTransactions = data.stream().filter(d -> isApplicable(d, date))
					.collect(Collectors.toList());
			double average = (monthlyDebitTransactions.stream().mapToDouble(d -> d.getAmountValue()).sum())
					/ monthlyDebitTransactions.size();
			return monthlyTransactions.stream().filter(t -> t.getAmountValue() > average).map(t -> t.getId())
					.collect(Collectors.toList());
		}
		double average = (monthlyTransactions.stream().mapToDouble(d -> d.getAmountValue()).sum())
				/ monthlyTransactions.size();
		return monthlyTransactions.stream().filter(t -> t.getAmountValue() > average).map(t -> t.getId())
				.collect(Collectors.toList());
	}

	private boolean isApplicable(TransactionData data, String date) {
		Date transactionDate = data.getDate();
		try {
			Date otherDate = new SimpleDateFormat("MM-yyyy").parse(date);
			return transactionDate.getMonth() == otherDate.getMonth()
					&& transactionDate.getYear() == otherDate.getYear();
		} catch (ParseException e) {
			return false;
		}
	}

}
