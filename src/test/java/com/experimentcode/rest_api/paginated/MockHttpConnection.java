package com.experimentcode.rest_api.paginated;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MockHttpConnection extends HttpURLConnection {

	protected MockHttpConnection(URL u) {
		super(u);
		// TODO Auto-generated constructor stub
	}
	
	public MockHttpConnection() {
		super(null);
	}

	private boolean isDisconnectCalled;
	private boolean isConnectCalled;
	private boolean isUsingProxy;
	private InputStream mockStream;

	@Override
	public void disconnect() {
		this.isDisconnectCalled=true;
	}

	@Override
	public boolean usingProxy() {
		
		return this.isUsingProxy;
	}
	
	public void setProxy() {
		this.isUsingProxy=true;
	}

	@Override
	public void connect() throws IOException {
		this.isConnectCalled=true;

	}

	public boolean isDisconnectCalled() {
		return isDisconnectCalled;
	}

	public boolean isConnectCalled() {
		return isConnectCalled;
	}

	public boolean isUsingProxy() {
		return isUsingProxy;
	}

	@Override
	public InputStream getInputStream() throws IOException {
		return mockStream;
	}

	public void setMockStream(InputStream mockStream) {
		this.mockStream = mockStream;
	}
}
