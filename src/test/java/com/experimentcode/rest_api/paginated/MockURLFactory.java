package com.experimentcode.rest_api.paginated;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLStreamHandler;

import com.experimentcode.rest_api.paginated.movie.IURLFactory;

public class MockURLFactory implements IURLFactory {

	private URLStreamHandler handler;

	@Override
	public URL create(String url) throws MalformedURLException {
		return new URL(null, url, handler);
	}

	public void setURLHandler(URLStreamHandler handler) {
		this.handler = handler;

	}

}
