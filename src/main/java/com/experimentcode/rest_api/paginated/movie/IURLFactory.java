package com.experimentcode.rest_api.paginated.movie;

import java.net.MalformedURLException;
import java.net.URL;

public interface IURLFactory {

	public URL create(String url) throws MalformedURLException;
}
