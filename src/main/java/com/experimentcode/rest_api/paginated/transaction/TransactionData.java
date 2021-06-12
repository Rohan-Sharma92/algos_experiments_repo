package com.experimentcode.rest_api.paginated.transaction;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;

public class TransactionData {

	private Integer id;

	private Integer userId;

	private String userName;

	private TransactionType txnType;

	private Long timestamp;

	private String amount;

	private Location location;

	private String ip;

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public TransactionType getTxnType() {
		return txnType;
	}

	public void setTxnType(TransactionType txnType) {
		this.txnType = txnType;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getDate() {
		Date date = new Date(timestamp);
		return date;
	}

	public double getAmountValue() {
		Number number = null;
		try {
			number = NumberFormat.getCurrencyInstance(Locale.US).parse(getAmount());
		} catch (ParseException e) {
		}
		return number==null?0:number.doubleValue();
	}
}
