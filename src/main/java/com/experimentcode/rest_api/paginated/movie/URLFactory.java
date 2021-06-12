package com.experimentcode.rest_api.paginated.movie;

import java.net.MalformedURLException;
import java.net.URL;

public class URLFactory implements IURLFactory{

	@Override
	public URL create(String url) throws MalformedURLException {
		return new URL(url);
	}
}
