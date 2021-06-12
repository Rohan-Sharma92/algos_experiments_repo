package com.experimentcode.rest_api.paginated.movie;

import java.io.ByteArrayInputStream;

import org.junit.Assert;
import org.junit.Test;

import com.experimentcode.rest_api.paginated.MockHttpConnection;
import com.experimentcode.rest_api.paginated.MockURLFactory;
import com.experimentcode.rest_api.paginated.MockURLHandler;
import com.experimentcode.rest_api.paginated.movie.Solution;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;



public class SolutionTest {

	@Test
	public void test() {
		MockURLHandler handler = new MockURLHandler();
		MockURLFactory factory = new MockURLFactory();
		MockHttpConnection connection = new MockHttpConnection();
		factory.setURLHandler(handler);
		handler.setConnection(connection);
		Solution s = new Solution(factory);
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("total_pages", 1);
		JsonArray data = new JsonArray();
		JsonObject element = new JsonObject();
		element.addProperty("Title", "hello");
		data.add(element);
		jsonObject.add("data", data);
		connection.setMockStream(new ByteArrayInputStream(jsonObject.toString().getBytes()));
		String[] titles = s.getMovieTitles("spiderman");
		Assert.assertEquals(titles.length, 1);
		Assert.assertEquals(titles[0],"hello");
	}
}
