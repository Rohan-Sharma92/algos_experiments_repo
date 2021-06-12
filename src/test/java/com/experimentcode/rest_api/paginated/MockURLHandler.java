package com.experimentcode.rest_api.paginated;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

public class MockURLHandler extends URLStreamHandler {

	private URLConnection connection;

	@Override
	protected URLConnection openConnection(URL u) throws IOException {
		return connection;
	}

	public void setConnection(URLConnection connection) {
		this.connection = connection;
	}

}
