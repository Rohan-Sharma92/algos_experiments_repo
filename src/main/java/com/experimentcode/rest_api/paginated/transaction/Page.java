package com.experimentcode.rest_api.paginated.transaction;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class Page {

	@SerializedName("page")
	private Integer page;

	@SerializedName("total_pages")
	private Integer totalPages;

	@SerializedName("per_page")
	private Integer perPage;

	@SerializedName("total")
	private Integer total;
	
	@SerializedName("data")
	private List<TransactionData> data;

	public List<TransactionData> getData() {
		return data;
	}

	public void setData(List<TransactionData> data) {
		this.data = data;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Integer getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(Integer totalPages) {
		this.totalPages = totalPages;
	}

	public Integer getPerPage() {
		return perPage;
	}

	public void setPerPage(Integer per_page) {
		this.perPage = per_page;
	}

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer page_total) {
		this.total = page_total;
	}
	
	@Override
	public String toString() {
		return "Page:"+page;
	}
}
